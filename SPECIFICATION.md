# clean-chess — Specification

The **technical specification** for clean-chess: design goals, architecture, philosophy, and the rule-level decisions that make the library what it is. Meant for someone (including future-self) who needs to understand *why* the library is shaped the way it is. User-facing documentation lives in `README.md`.

---

## 1. Purpose & non-goals

clean-chess is a Java chess library focused on **rule correctness and reproducibility**. Its flagship feature is a Java port of Miguel Ambrona's [Chess Unwinnability Analyzer (CHA)](https://github.com/miguel-ambrona/D3-Chess), the only published algorithm that decides unwinnability and dead-position questions correctly across all positions.

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

The codebase is written in as functional a style as Java reasonably permits: records, immutable value objects, pure helpers; mutable state is confined to a small number of well-defined classes (notably `Board`). The aspirational target is Haskell — total functions, types that make illegal states unrepresentable, no nulls.

Concretely:

- **Records as value objects** (`PgnCommentary`, `Fen`, `Tag`, `PgnHalfMove`, `PgnFile`, `MoveSpecification`). The compact constructor validates the contract; once an instance exists, downstream code does not re-validate. Errors are pushed to the construction boundary.
- **Heavy enum use** for closed domains (`Side`, `Piece`, `Square`, `File`, `Rank`, `MoveSuffixAnnotation`, `ResultTagValue`, etc.) — the compiler enforces exhaustive `switch` handling.
- **Eclipse JDT null annotations** (`@NonNull` / `@Nullable`) used pervasively, with the build configured so violations are errors. Null is a typed concern, not a runtime accident.
- **No reflection in the rule core.** What the type system says is what runs.

The result is a codebase where a substantial class of bugs — null-dereference, unhandled enum case, mutated-after-construction — cannot reach runtime.

### 2.3 Diagnostic quality

When validation fails, the library produces messages a human can act on. Each problem has a typed code (e.g. `StrictPgnParserValidationProblem.MOVETEXT_MOVE_NUMBER_REQUIRED_AFTER_COMMENTARY`) plus a human-readable message naming the offending construct, its position, and what was expected. Generic "illegal move" / "parse error" responses are avoided wherever a more specific category fits. This applies uniformly to SAN, FEN, and PGN validation, and to both SAN pipelines (programmatic and PGN-driven).

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

Miguel Ambrona's CHA is the only published algorithm that decides these cases correctly across the full range of positions. clean-chess implements it in Java, in two variants:

- **Quick** — microsecond-scale, structural, three-valued: `WINNABLE`, `UNWINNABLE`, `POSSIBLY_WINNABLE`. The third value is a deliberate honesty signal.
- **Full** — deep search, three-valued: `WINNABLE`, `UNWINNABLE`, `UNDETERMINED`. The undetermined case is bounded by a 500 000-position limit; most positions resolve well below that.

`Dead position` is the symmetric notion with the analogous three-valued return.

Both variants are **opt-in**. clean-chess does not invoke CHA automatically when a move is performed: the only deadness check in the per-move game-status query is the structural insufficient-material test (§3.1). The motivating concern is **bulk PGN analysis** — the library is also designed to process many games in batch, where a per-move CHA check would add significant cumulative cost. This is not a statement about quick CHA being slow: it runs in microsecond range and is fine to call directly during ordinary gameplay (e.g. on resignation or flag fall). The full variant is naturally heavier. Both are caller-invoked when the result is wanted.

### 3.3 SAN, FEN, PGN

- **SAN** — parsing, validation, and generation with canonical disambiguation. A single `Board.performMove(String)` validation is reached from both the programmatic and the PGN-driven paths.
- **FEN** — basic parsing validates structure; *advanced* parsing additionally validates position legality (no impossible double-checks, achievable pawn structure, castling rights consistent with rooks-and-king positions, etc.). `Board(String fen)` uses the advanced variant, so a `Board` cannot be constructed from a position no real game could reach.
- **PGN** — two parsers: **strict** (round-trip-canonical reference, enforces export-format invariants) and **lenient** (tolerates real-world PGN — spaced move-number indicators, missing seven-tag-roster entries, optional termination markers, extra whitespace). Both produce the same `PgnFile` model. The exporter (`PgnCreate`) aims for byte-stable round-trip with the strict parser. The two-parser split is deliberate: a single parser with a "strictness flag" inevitably grows conditional branches that obscure both rule sets — splitting keeps each parser readable and lets the two evolve independently.

---

## 4. Architecture

The top-level package `com.dlb.chess` is organised by concern:

| Package | Responsibility |
|---|---|
| `board` | `Board`, position state, move execution, game-status queries |
| `model` | Cross-cutting model types |
| `enums` | Domain enums shared across the codebase |
| `fen` | FEN parsing (basic and advanced) and validation |
| `san` | SAN parsing, validation, generation |
| `moves` | Legal move enumeration and execution helpers |
| `pgn.parser` | Strict and lenient PGN parsers; shared tokenizer in `pgn.parser.sequential` |
| `pgn.create` | PGN export |
| `unwinnability` | CHA implementation, quick and full |
| `analysis` | Game-level analysis: threefold-claim-ahead reports, repetition, 50-move sequences |
| `analyze` | Higher-level orchestration over `analysis` |
| `common` | Generic utilities and exceptions |

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

