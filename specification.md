# clean-chess — Specification

The **technical specification** for clean-chess: design goals, architecture, philosophy, and the rule-level decisions that make the library what it is. Meant for someone (including future-self) who needs to understand *why* the library is shaped the way it is. User-facing documentation lives in `README.md`.

---

## 1. Purpose & non-goals

clean-chess is a Java chess library focused on **rule correctness and reproducibility**. Its flagship feature is a Java port of Miguel Ambrona's [Chess Unwinnability Analyzer (CHA)](https://github.com/miguel-ambrona/D3-Chess), to the author's knowledge the only published algorithm that decides unwinnability and dead-position questions correctly across all positions.

The library is **not**:

- A chess engine — it does not search for best moves.
- A performance-first library — move generation is not optimised for raw throughput.
- A complete tournament-management toolkit — clock handling, draw-offer state machines, and similar interactive concerns belong outside the library (scope of the companion `dumb-chessboard` project).

In spirit it is a **proof-of-concept**: that the FIDE Laws of Chess can be implemented in Java at a high level of programming quality while remaining a usable library.

---

## 2. Philosophy

### 2.1 Clarity and reproducibility over clever optimisation

Other Java chess libraries are correct too — clean-chess does not claim a correctness advantage over them. What differs is **implementation style**: the project deliberately prefers straightforward, reproducible code paths over complex optimisations that buy speed at the cost of being expert-only to read or audit.

Two concrete examples:

- **Move history stores derived facts directly.** Whether a move was a two-square pawn advance or an en passant capture is recorded in the move history rather than recomputed from the position when needed. Engines like Stockfish compute these on demand because the savings matter at engine speeds. clean-chess stores them because the resulting code is shorter, more obviously correct, and easier to maintain.
- **Position repetition uses direct position equality, not Zobrist hashing.** Zobrist hashing is the standard fast technique and works correctly when implemented carefully — but it is empirically a frequent source of subtle bugs (this author helped fix a Zobrist bug in another open-source chess library before this rule was adopted here). Direct equality is slower, but harder to get wrong.

These choices cost performance and pay clarity. Most position-level questions still resolve in microseconds and full unwinnability searches in seconds. Where the FIDE Laws are ambiguous, the library follows the most rule-faithful reading.

### 2.2 Functional style and compile-time guarantees

The codebase is written in as functional a style as Java reasonably permits: records, immutable value objects, pure helpers; mutable state is confined to a small number of well-defined classes (notably `Board`). The aspirational target is Haskell — total functions and types that make illegal states unrepresentable. Where Java forces compromise (mutable accessors, nullable JDK return types), the compromises are localised and crossed with explicit annotations.

Concretely:

- **Records as value objects** (`PgnCommentary`, `Fen`, `Tag`, `PgnHalfMove`, `PgnFile`, `MoveSpecification`). Where a record carries a non-trivial textual or grammatical contract — `PgnCommentary` is the load-bearing example — the compact constructor enforces it, and downstream code does not re-validate. For records whose invariants are field-level (`Fen`, `Tag`, `PgnHalfMove`), validation lives one layer out, at the parser/factory boundary (`FenParserAdvanced`, `LenientPgnParser`, `StrictPgnParser`); a record never holds something that came in from outside the library without first passing through one of those entry points. `PgnFile`'s compact constructor performs defensive copies of its list components so the immutability claim holds end-to-end. The end result is the same — errors at construction time — but the boundary is occasionally one method out from the record itself.
- **Heavy enum use** for closed domains (`Side`, `Piece`, `Square`, `File`, `Rank`, `MoveSuffixAnnotation`, `ResultTagValue`, etc.) — the compiler enforces exhaustive `switch` handling.
- **Eclipse JDT null annotations** (`@NonNull` / `@Nullable`) used pervasively, with the build configured so violations are errors. Null is a typed concern, not a runtime accident.
- **No reflection in the rule core.** What the type system says is what runs.

The result is a codebase where a substantial class of bugs — null-dereference, unhandled enum case, mutated-after-construction — cannot reach runtime.

### 2.3 Diagnostic quality

When validation fails, the library produces messages a human can act on. Each problem has a typed code (e.g. `StrictPgnParserValidationProblem.MOVETEXT_MOVE_NUMBER_REQUIRED_AFTER_COMMENTARY`) plus a human-readable message naming the offending construct, its position, and what was expected. Generic "illegal move" / "parse error" responses are avoided wherever a more specific category fits. This applies uniformly to SAN, FEN, and PGN validation, and to both SAN pipelines (programmatic and PGN-driven).

### 2.4 Thread-safety

The library makes only modest thread-safety guarantees, all of them honest about the underlying types:

- **`Board` is mutable and not thread-safe.** Use one `Board` per thread, or synchronize externally. `Board.equals` / `Board.hashCode` reflect current game state, so a `Board` placed in a `HashMap` or `HashSet` and then mutated will violate the collection's invariants.
- **Records are immutable and thread-safe.** `Fen`, `PgnFile`, `PgnHalfMove`, `MoveSpecification`, `PgnCommentary`, `Tag`, `Report`, etc. — once constructed, they can be freely shared.
- **Static utility classes are stateless and thread-safe.** `Reporter`, `PgnCreate`, `KnightDistance`, `StaticPositionUtility`, `BasicChessUtility`, the various `*Validation` and `*Utility` classes — all entry points are static methods on stateless classes. Multiple threads can call them concurrently.
- **Parsers expose stateless static entry points.** `StrictPgnParser.parseText(String)` / `StrictPgnParser.parse(Path)` and the lenient counterparts construct a fresh parser instance per call internally; the parser instances themselves carry per-parse state and should not be shared. Stick to the static entry points.

In short: share records and call static utilities from anywhere; never share a `Board`.

---

## 3. Feature specification

### 3.1 FIDE rule fidelity and game termination

The library follows the FIDE Laws of Chess closely, distinguishing **automatic** terminations (the game is over in the model) from **claimable** ones (the library exposes the predicate; the game continues until claimed):

| Rule | Mode | FIDE article |
|---|---|---|
| Checkmate | automatic | 5.1 |
| Stalemate | automatic | 5.2.1 |
| Insufficient material (structural) | automatic | 5.2.2 / 9.4 |
| Fivefold repetition | automatic | 9.6.1 |
| 75-move rule | automatic | 9.6.2 |
| Threefold repetition | claimable | 9.2 |
| 50-move rule | claimable | 9.3 |

Once a position is automatically terminated, the board does not allow further moves; continuation past mandatory termination is a usage error. Insufficient material is detected by a fast structural test (king-vs-king, king + minor vs king, etc.); positions that are dead by *exhaustive* search but not by structural insufficiency are handled by the unwinnability path (§3.2), not here.

For the claimable rules, the library exposes both the **on-board** predicate (current position satisfies the rule) and the **with-move** predicate (some legal move would satisfy it), and produces analysis output that names which moves *would* satisfy the claim — surfacing missed claim opportunities that other libraries do not. Position equality follows the FIDE definition: same piece placement, same side to move, same castling rights, same en-passant possibilities.

### 3.2 Unwinnability — Chess Unwinnability Analyzer (CHA)

The library's **flagship feature**. A position is *unwinnable for a side* if that side has no theoretical mating sequence assuming worst-case play by the opponent. A *dead position* is one unwinnable for both sides. Insufficient material covers the trivial cases; positions like blocked pawn walls, certain wrong-bishop endgames, and many forced-only-moves continuations are dead but not insufficient — and most chess libraries get them wrong.

Miguel Ambrona's CHA is, to the author's knowledge, the only published algorithm that decides these cases correctly across the full range of positions. clean-chess implements it in Java, in two variants:

- **Quick** — microsecond-scale, structural, three-valued: `WINNABLE`, `UNWINNABLE`, `POSSIBLY_WINNABLE`. The third value is a deliberate honesty signal.
- **Full** — deep search, three-valued: `WINNABLE`, `UNWINNABLE`, `UNDETERMINED`. The undetermined case is bounded by a 500 000-position limit; most positions resolve well below that.

`Dead position` is the symmetric notion with the analogous three-valued return.

Both variants are **opt-in**. clean-chess does not invoke CHA automatically when a move is performed: the only deadness check in the per-move game-status query is the structural insufficient-material test (§3.1). The motivating concern is **bulk PGN analysis** — the library is also designed to process many games in batch, where a per-move CHA check would add significant cumulative cost. This is not a statement about quick CHA being slow: it runs in microsecond range and is fine to call directly during ordinary gameplay (e.g. on resignation or flag fall). The full variant is naturally heavier. Both are caller-invoked when the result is wanted.

### 3.3 SAN, FEN, PGN

- **SAN** — two pipelines: **strict** (canonical SAN only; reached from `Board.performMove(String)` and from the PGN-driven path) and **lenient** (accepts a defined set of forgivable deviations from canonical; reached from `Board.performMoveLenient(String)`). See §3.3.1 for the lenient taxonomy and algorithm.
- **FEN** — basic parsing validates structure; *advanced* parsing additionally validates position legality (no impossible double-checks, achievable pawn structure, castling rights consistent with rooks-and-king positions, etc.). `Board(String fen)` uses the advanced variant, so a `Board` cannot be constructed from a position no real game could reach.
- **PGN** — two parsers: **strict** (round-trip-canonical reference, enforces export-format invariants) and **lenient** (tolerates real-world PGN — spaced move-number indicators, missing seven-tag-roster entries, optional termination markers, extra whitespace). Both produce the same `PgnFile` model. The exporter (`PgnCreate`) aims for byte-stable round-trip with the strict parser. The two-parser split is deliberate: a single parser with a "strictness flag" inevitably grows conditional branches that obscure both rule sets — splitting keeps each parser readable and lets the two evolve independently.

#### 3.3.1 Lenient SAN

The strict SAN pipeline (`SanValidation`, reached via `Board.performMove(String)`) accepts only canonical SAN: file-preferred disambiguation, uppercase piece letter and lowercase file letter, the `=Q` promotion form, `O-O` / `O-O-O` castling, and an optional `+` / `#` suffix that must match the actual board state. Real-world PGN — ChessBase output, hand-edited files, engine traces — routinely deviates in forgivable ways.

The lenient SAN pipeline (`LenientSanParser`, reached via `Board.performMoveLenient(String)`) accepts these deviations when they uniquely identify a legal move. Every accepted deviation surfaces as a typed `ForgivenItem` carrying the deviation code, the original token, and the canonical-SAN equivalent — so consumers can silently accept or warn.

**Principle: canonical-first.** A string that already parses as canonical SAN never receives a different meaning under lenient — strict is tried first; only on rejection does the lenient layer engage. So `bxc6` always means pawn capture from the b-file, even if a bishop on the b-file could also capture on c6.

**Taxonomy — 21 codes** (`com.dlb.chess.san.enums.LenientSanValidationProblem`):

| Category | Code | Example |
|---|---|---|
| **Suffix mismatch** (6) | `MISSING_CHECK_SUFFIX` | `Nd7` when actually check |
| | `MISSING_CHECKMATE_SUFFIX` | `Nd7` when actually mate |
| | `SPURIOUS_CHECK_SUFFIX` | `Nd7+` when not check |
| | `SPURIOUS_CHECKMATE_SUFFIX` | `Nd7#` when not mate |
| | `WRONG_CHECK_SUFFIX_FOR_CHECKMATE` | `Nd7+` when actually mate |
| | `WRONG_CHECKMATE_SUFFIX_FOR_CHECK` | `Nd7#` when only check |
| **Capture marker** (2) | `MISSING_CAPTURE_MARKER` | `Be5` (piece) or `ed5` (pawn) when actually a capture |
| | `SPURIOUS_CAPTURE_MARKER` | `Bxe5` when destination empty |
| **Disambiguation** (4) | `OVERSPECIFIED_FILE_DISAMBIGUATION` | `Nbd7` when `Nd7` would suffice |
| | `OVERSPECIFIED_RANK_DISAMBIGUATION` | `N3d7` when `Nd7` would suffice |
| | `OVERSPECIFIED_SQUARE_DISAMBIGUATION` | `Nb3d7` when less would suffice |
| | `NON_STANDARD_RANK_DISAMBIGUATION` | `R1d4` where canonical uses file (`Rad4`) |
| **Notation form** (4) | `LONG_ALGEBRAIC_NOTATION` | `e2-e4`, `Nb1-d7` |
| | `UCI_NOTATION` | `e2e4`, `e7e8q`, `e1g1` (castling) |
| | `ZERO_INSTEAD_OF_O_CASTLING` | `0-0`, `0-0-0` |
| | `EXPLICIT_PAWN_LETTER` | `Pe4` |
| **Promotion form** (1) | `MISSING_PROMOTION_EQUALS` | `e8Q` |
| **Case variation** (4) | `LOWERCASE_PIECE_LETTER` | `nf3` |
| | `UPPERCASE_FILE_LETTER` | `NF3` |
| | `UPPERCASE_CAPTURE_MARKER` | `BXe5` |
| | `LOWERCASE_PROMOTION_PIECE` | `e8=q` |

Codes are not collapsed: each distinguishable deviation has its own code, and a single move can carry multiple codes (e.g. `nbxd7+` when actually mate emits `LOWERCASE_PIECE_LETTER` + `OVERSPECIFIED_FILE_DISAMBIGUATION` + `WRONG_CHECK_SUFFIX_FOR_CHECKMATE`).

**Algorithm — two-phase.** *Phase 1 (shape normalization)* performs pure-string transforms plus board-aware UCI translation (look up piece on from-square): castling-zero, mixed-castling rejection, `P`-stripping, hyphen-stripping (LAN), UCI form translation, missing-`=` insertion, all four case fixups. LAN and UCI are mutually exclusive at the input level — a hyphen means LAN (`LONG_ALGEBRAIC_NOTATION` only), no hyphen means UCI shape (`UCI_NOTATION` only). *Phase 2 (semantic recovery)* feeds the normalized candidate to the strict pipeline and, on a recoverable rejection (terminal-marker mismatch, capture-marker mismatch, over-specification, non-standard disambig), mutates the candidate and retries. Each lenient code can fire at most once per parse, bounding the loop.

**API.** `LenientSanParser.parseText(String, ChessBoard)` returns a `LenientSanParserValidationResult` (move + forgiven items). `LenientSanParser.validateText(String, ChessBoard)` is the same call with the result discarded — convenience for yes/no checks. `Board.performMoveLenient(String)` returns the same result type so the convenience path also surfaces forgiven items.

**Deliberate non-recoveries.** Three categories are rejected even by the lenient pipeline:
- **Mixed castling** (`0-O`, `O-0`) — no real-world tool emits this; allowing it would add parser complexity for zero practical value.
- **Pawn `SPURIOUS_CAPTURE_MARKER`** — `dxe5` when e5 is empty has no clean string mutation that yields canonical SAN; the only "recovery" would silently swap the user's intended pawn (d-file) for a different one (e-file). That crosses the line from forgiving sloppiness to overriding intent.
- **Game already terminated** — top-of-pipeline guard before the lenient layer engages, identical to strict; once a FIDE-automatic termination is reached no further moves are accepted, lenient or otherwise.

The strict pipeline remains the single source of chess-validation truth. Lenient is a thin input-shape transformation layer that reuses strict for everything else.

---

## 4. Architecture

The top-level package `com.dlb.chess` is organised by concern:

| Package | Responsibility |
|---|---|
| `board` | `Board`, position state, move execution, game-status queries |
| `model` | Cross-cutting model types |
| `enums` | Pipeline-level domain enums (`MoveCheck`, `MoveSuffixAnnotation`, etc.) shared across SAN and movement validation |
| `fen` | FEN parsing (basic and advanced) and validation |
| `san` | SAN parsing, validation, generation |
| `moves` | Legal move enumeration and execution helpers |
| `pgn.parser` | Strict and lenient PGN parsers; shared tokenizer in `pgn.parser.sequential` |
| `pgn.create` | PGN export |
| `pgn.writer` | File-system PGN writing |
| `pgn.diagnostic` | Standalone PGN diagnostics outside the strict pipeline (e.g. `GameContinuationScanner`) |
| `unwinnability` | CHA implementation, quick and full |
| `report` | Game-level reports: threefold-claim-ahead, repetition, 50-move sequences |
| `analyze` | Stateless chess-rule analysis used by SAN and movement validation pipelines |
| `utility` | PGN- and tag-level helpers used by the parsers and exporter (separate from `common.utility`) |
| `messages` | Validation-message bundle (`Message`, `messages.properties`) for SAN/FEN/PGN diagnostics |
| `distance` | Square-to-square distance metrics (king, knight) |
| `range` | Orthogonal and diagonal direction ranges for piece movement |
| `squares` | Precomputed square-to-square reachability and attack lookup tables (`emptyboard`, `pawn`, `to.attacked`, `to.potential`, `to.range`) |
| `exceptions` | Top-level move-pipeline exception (`InvalidMoveException`); other exceptions live in `common.exceptions` |
| `common` | Generic utilities, constants, and exceptions |

Packages depend in roughly that order (top to bottom).

---

## 5. PGN — intentional deviations from the specification

The library implements the PGN specification closely. Two areas are intentional departures, in both cases following **python-chess** as the de-facto reference (and, for pre-game commentary, also **Lichess**), where the formal spec is silent, ambiguous, or marked as not fully defined.

### 5.1 Newlines and tabs preserved in commentary content

The PGN specification's strict export format implies that brace commentary should fit on a single line, with newlines normalised and non-printing characters generally absent. Two facts qualify that:

- The export format itself is explicitly noted in the spec as **not fully defined**.
- The strict prohibition on non-printing characters in the spec applies to **string tokens** (tag values), not to brace commentary content. The commentary case is silent.

clean-chess takes the more permissive (and more useful) reading: **`\t` and `\n` inside `{...}` commentary are content**, preserved verbatim through `parse → export → parse`. The rationale is round-trip fidelity for real-world PGN, where comments often carry multi-line annotation. This matches python-chess.

The library still rejects malformed Unicode (lone surrogates, unassigned code points) and other control characters; the deviation is specifically about tab and LF. CR / CRLF are normalised to LF on input, so the model never carries CR directly.

### 5.2 Pre-game commentary

The PGN specification defines brace commentary attached to half-moves but **does not formally specify a "pre-game" commentary slot** — commentary that appears between the tag pair section and the first move. python-chess exposes this as `Game.comment`; Lichess supports it on import.

clean-chess follows the same convention: `PgnFile.pregameCommentary()` carries any `{...}` content found before the first half-move, validated under the same commentary contract as move-attached commentary. This is an additive extension rather than a contradiction — a PGN that uses pre-game commentary remains well-formed for any reader that ignores it.

### 5.3 Conformance for everything else

The rest of the library's PGN handling follows the specification: brace-grammar rules, move-number indicators, inner-brace-as-content, seven-tag-roster requirements, FIDE game-termination markers, the strict-vs-import format split. The PGN standard already documents the *what*; the code is the authoritative *how*.

---

## 6. Testing strategy

clean-chess relies on a large regression test suite:

- **Broad coverage by code area** — every package has dedicated tests; rule-level decisions have multi-fixture parameterised tests.
- **Edge-case fixtures** — positions and games chosen to stress the rule engine: 75-move-rule games, fivefold-repetition games, dead positions, near-misses, long forced sequences.
- **Long and random games** — hundreds of half-moves, including imported real-world games and synthetic stress tests; generated games surface bugs that targeted fixtures miss.
- **Cross-library validation** — selected fixtures are processed by other chess libraries; disagreements surface as test failures and have, in the past, led to bug reports against those libraries.

The test suite is the project's safety net. Refactors are expected to leave the test count unchanged or growing; if they don't, the change is suspect.

### 6.1 Restricted vs full suite

Day-to-day iteration runs a restricted subset (`mvn test`). A handful of long-running audits are gated by `RestrictTestConstants`: the cross-corpus parser audits, a multi-second unwinnability full-search test, the legacy parser-rejection audit. They take from a few seconds to a few minutes apiece and are not useful on every iteration.

The full suite is a Maven profile:

```
mvn test -Pfull
```

`-Pfull` sets the `clean-chess.full` system property, which flips every gate inside `RestrictTestConstants` and switches `PgnTestInclusion` to `ALL` (including the longest-possible-game corpus).

**Release-time requirement:** before tagging a release, run `mvn test -Pfull` and confirm green. The default suite is *not* sufficient to certify a release.

