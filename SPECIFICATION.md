# clean-chess — Specification

This document is the **technical specification** for clean-chess: design goals, architecture, philosophy, and the rule-level decisions that make the library what it is. It is meant for someone (including future-self) who needs to understand *why* the library is shaped the way it is — not how to use it. User-facing documentation lives in `README.md`.

---

## 1. Purpose & non-goals

clean-chess is a Java chess library focused on **rule correctness and reproducibility**. Its flagship feature is a Java port of Miguel Ambrona's [Chess Unwinnability Analyzer (CHA)](https://github.com/miguel-ambrona/D3-Chess), the only published algorithm that decides unwinnability and dead-position questions correctly across all positions.

The library is **not**:

- A chess engine — it does not search for best moves. Stockfish, Leela, etc. own that space.
- A performance-first library — move generation is not optimised for raw throughput. If you need to evaluate millions of positions per second, look elsewhere.
- A complete tournament-management toolkit — clock handling, draw-offer state machines, touch-move adjudication and similar interactive concerns belong outside the library (they are the scope of the companion `dumb-chessboard` project, which depends on clean-chess).

What it is:

- A **rules engine**: legal moves, check / checkmate / stalemate, FEN, SAN, PGN, repetition, move counters, insufficient material, unwinnability.
- A **diagnostic library**: when a SAN, FEN, or PGN is invalid, the library produces a precise, user-facing explanation rather than a generic "parse error".
- A **cross-validation reference**: many of the harder rule cases (deep unwinnability, edge-case repetition, unusual termination) are tested against other chess libraries to expose disagreements.

---

## 2. Project framing

clean-chess is, in spirit, a **proof-of-concept**: that the FIDE Laws of Chess can be implemented in Java at a high level of programming quality while remaining a usable library. The goal is to make rule-level questions answerable with confidence — not to compete with engines on speed or with full PGN platforms on breadth of features.

This framing matters for trade-offs. When correctness and performance conflict, correctness wins. When a feature would broaden the API but make it harder to keep the rule core sound, the feature is left out.

---

## 3. Philosophy

### 3.1 Correctness over performance

The library treats correctness as a hard constraint and performance as a secondary concern, in that order. It is still **reasonably performant** — most position-level questions resolve in microseconds, full unwinnability searches in seconds — but no path is optimised at the expense of rule fidelity.

Where ambiguity exists in the FIDE Laws or a rule has multiple interpretations, the library follows the most conservative (most rule-faithful) reading.

### 3.2 Functional style in Java

The codebase is written in as functional a style as Java reasonably permits. Records, immutable value objects, validate-on-construction, and pure helpers dominate; mutable state is confined to a small number of well-defined classes (notably `Board`).

The aspirational style is Haskell — total functions, types that make illegal states unrepresentable, no nulls. Java cannot reach that bar, but several library decisions move in that direction:

- **Heavy use of enums** for closed, finite domains: `Side`, `Piece`, `Square`, `File`, `Rank`, `MoveSuffixAnnotation`, `ResultTagValue`, `StandardTag`, `SetUpTagValue`, `MessageType`, etc. Anywhere the domain is enumerable, the library prefers an enum over a string or a numeric flag.
- **Records for data**: `PgnCommentary`, `Fen`, `Tag`, `PgnHalfMove`, `PgnFile`, `MoveSpecification`. These are immutable, structural-equality value objects.
- **Validate-on-construction**: where a record represents a value with constraints (e.g. PGN commentary content, FEN structure), the constraints are checked in the canonical / compact constructor. Once an instance exists, its content is guaranteed to satisfy the contract by typing — downstream code does not validate again.

### 3.3 Compile-time guarantees

The codebase aims to detect as many defects at compile time as Java permits:

- **Eclipse JDT null annotations** (`@NonNull`, `@Nullable`, `@SuppressWarnings("null")`) are used pervasively. Null is treated as a typed concern, not a runtime accident. The build is configured so that null-annotation violations are errors, not warnings.
- **Sealed/closed enum types** instead of strings or ints for domain values, so the compiler enforces exhaustive `switch` handling.
- **Final fields and records** to prevent accidental mutation.
- **No reflection-based magic** in the rule core. What the type system says is what runs.

The result is a codebase in which a substantial class of bugs — null-dereference, unhandled enum case, mutated-after-construction — cannot reach runtime.

### 3.4 Diagnostic quality

When validation fails, the library produces messages a human can act on. This is a deliberate design goal, not a side-effect:

- **Validation exceptions are detailed.** Each validation problem has a typed problem code (e.g. `StrictPgnParserValidationProblem.MOVETEXT_MOVE_NUMBER_REQUIRED_AFTER_COMMENTARY`) plus a human-readable message naming the offending construct, its position, and what was expected.
- **SAN validation messages name the SAN, the position constraint that failed, and the remediation.** Generic "illegal move" responses are avoided wherever a more specific category fits.
- **Both SAN pipelines produce human-friendly explanations.** SAN reaches the library through two paths: programmatic (`board.performMove("e4")`) and via the PGN parser. Both pipelines are held to the same diagnostic standard.

These diagnostics are not just for developer debugging — downstream consumers like `dumb-chessboard` forward them verbatim to end users.

---

## 4. Feature specification

### 4.1 FIDE rule fidelity

The library follows the FIDE Laws of Chess as closely as the platform permits. Specific points:

- **Mandatory game endings terminate the game.** Once a position is checkmate, stalemate, fivefold repetition, 75-move-rule, or insufficient-material-with-no-mate-possible, the game is **over** in the library's model. The board does not allow further moves to be played from a terminated position. Continuation past mandatory termination is treated as a usage error.
- **Claimable rules are not automatic.** Threefold repetition (FIDE 9.2) and the 50-move rule (FIDE 9.3) are *claimable* — the library exposes the predicates but does not auto-end on them, mirroring the FIDE distinction between "claimable" and "automatic" termination.
- **The 75-move rule and fivefold repetition rule are implemented strictly.** Both fire automatically and unconditionally. They are the post-2014 FIDE additions specifically designed to remove the need for a player to claim; the library treats them as the binding floor on game length.

### 4.2 Game termination

The library distinguishes the FIDE-automatic terminations:

| Rule | Automatic | FIDE article |
|---|---|---|
| Checkmate | yes | 5.1 |
| Stalemate | yes | 5.2.1 |
| Insufficient material (structural) | yes | 5.2.2 / 9.4 |
| Fivefold repetition | yes | 9.6.1 |
| 75-move rule | yes | 9.6.2 |
| Threefold repetition (claimable) | no | 9.2 |
| 50-move rule (claimable) | no | 9.3 |

A position-level query (`board.getGameStatus()`, `board.isCheckmate()`, etc.) returns the highest-priority automatic termination if one applies; otherwise the game is ongoing.

### 4.3 Repetition and move-counter rules

- **Threefold repetition (FIDE 9.2)** — claimable. The library exposes both the "on-board" predicate (the current position has occurred three times) and the "with-move" predicate (some legal move would produce a thrice-repeated position). Position equality follows the FIDE definition: same piece placement, same side to move, same castling rights, same en-passant possibilities.
- **Fivefold repetition (FIDE 9.6.1)** — automatic. Identical position-equality semantics; fires unconditionally on the fifth occurrence.
- **50-move rule (FIDE 9.3)** — claimable. Fires when the half-move clock reaches 100 with no capture or pawn move; same on-board / with-move duality as threefold.
- **75-move rule (FIDE 9.6.2)** — automatic. Half-move clock at 150; unconditional.

The library produces analysis output for the threefold and 50-move cases that names which moves *would* satisfy the claim — surfacing missed claim opportunities that other libraries do not.

### 4.4 Insufficient material

Structural insufficient-material detection (king-vs-king, king + minor vs king, etc.) is implemented per FIDE 5.2.2 / 9.4. This is a fast, structural test — it does not search; it answers from the piece configuration alone. Positions that are dead by *exhaustive* search but not by structural insufficiency are handled by the unwinnability path (§4.5), not here.

### 4.5 Unwinnability — Chess Unwinnability Analyzer (CHA)

This is the library's **flagship feature**.

A position is *unwinnable for a side* if that side has no theoretical mating sequence assuming worst-case play by the opponent. A *dead position* is one unwinnable for both sides. Insufficient material covers the trivial cases; positions like blocked pawn walls, certain wrong-bishop endgames, and many forced-only-moves continuations are dead but not insufficient — and most chess libraries either miss them or flag them incorrectly.

Miguel Ambrona's **Chess Unwinnability Analyzer (CHA)** is the only published algorithm that decides these cases correctly across the full range of positions. clean-chess implements that algorithm in Java. The flagship status is not rhetorical: getting unwinnability right has real consequences for game outcomes (resignations, flag falls, and dead-position auto-draws all hinge on it), and yet most chess software gets some of these positions wrong.

The library exposes both a **quick** and a **full** variant of the algorithm:

- **Quick** is microsecond-scale, structural, and uses three-valued logic: `WINNABLE`, `UNWINNABLE`, `POSSIBLY_WINNABLE`. The third value is a deliberate honesty signal — quick may not have enough information to decide, and it says so.
- **Full** is the deep search and uses three-valued logic too: `WINNABLE`, `UNWINNABLE`, `UNDETERMINED`. The undetermined case is bounded by a position-count limit (currently 500 000); most positions resolve well below that, but some pathological positions can exhaust it.

`Dead position` is the symmetric notion: a quick / full pair returns `DEAD_POSITION` / `NON_DEAD_POSITION` / `POSSIBLY_NON_DEAD_POSITION` (quick) or `DEAD_POSITION` / `NON_DEAD_POSITION` / `UNDETERMINED` (full).

The library credits Miguel Ambrona's original C implementation as the reference. His C code is industry-grade and the Java port follows its structure where doing so does not collide with Java's idioms.

### 4.6 SAN

SAN (Standard Algebraic Notation) is a first-class concern, not an afterthought. The library:

- **Parses and validates** SAN strings against a position. Validation produces a typed `SanValidationProblem` plus a human-readable message.
- **Generates SAN** for legal moves with the canonical disambiguation rules (file disambiguation preferred, then rank, then both).
- **Has two pipelines**: one that operates on a single SAN against a position (`board.performMove("e4")`) and one that drives the PGN movetext parser. Both pipelines reach the same `Board.performMove(String)` validation, so a SAN that round-trips through PGN is held to the same standard as one passed directly.

Diagnostic quality is the same on both pipelines — generic "illegal move" is avoided wherever a more specific category (piece-doesn't-exist, no-piece-of-that-kind-can-reach, ambiguous-disambiguation, would-leave-king-in-check, etc.) applies.

### 4.7 FEN

FEN parsing has a basic and an *advanced* variant:

- **Basic FEN parsing** validates structure: piece placement, side-to-move, castling rights, en-passant target, half-move clock, full-move number.
- **Advanced FEN parsing** additionally validates *position legality* — that the king is not in an impossible check from both sides, that pawn structure is achievable, that castling rights claim only what the rooks-and-king positions support, etc.

Advanced FEN parsing is what `Board(String fen)` uses, so a `Board` can never be constructed from a position that no real game could reach.

### 4.8 PGN

PGN parsing comes in two flavours — **strict** and **lenient** — for distinct use cases. The detailed contract is in §6; the headline is:

- **Strict** is for round-trip-canonical PGN. It enforces the export-format invariants (exact spacing, required move-number indicators, well-formed brace commentary, etc.) and rejects anything that deviates.
- **Lenient** is for real-world PGN found in the wild. It tolerates spaced move-number indicators, missing seven-tag-roster entries, optional termination markers, extra whitespace, etc., while still rejecting structurally broken input.

Both parsers produce the same model — `PgnFile` — so downstream code does not need to know which parser was used.

The library also exports PGN (`PgnCreate`). The exporter aims for byte-stable round-trip with the strict parser: `parse → export → parse` preserves the model.

---

## 5. Architecture

### 5.1 Type-driven validity

The recurring pattern in the library: a value is a record with a compact constructor that validates the contract. Once an instance exists, its content satisfies the contract by typing.

Examples:

- `PgnCommentary` — content rules for `{...}` commentary. The constructor enforces them. Downstream code (`PgnCreate`, the parsers) does not re-validate.
- `Fen` — structural validity of a FEN string. Once a `Fen` exists, the structure is sound.
- `MoveSpecification` — a from-square, to-square, and optional promotion, with internal consistency.
- `Tag` — a `(name, value)` pair following the PGN tag-name grammar.

This pattern pushes errors to the **construction boundary**. A function that takes a `PgnCommentary` does not need to defend against malformed content; if it received a `PgnCommentary`, the content is already valid.

The companion to this pattern is **fail-on-construction with a precise message**. Validation exceptions name the offending character or field, its index, and the specific rule violated. They are not "this is wrong, somewhere" — they are "at index 47 you have a closing brace `}`, which terminates the {...} grammar".

### 5.2 Two-parser split (strict vs lenient)

The library deliberately maintains two independent PGN parsers. Strict and lenient share the tokenizer (in `pgn.parser.sequential`) but have separate parser implementations, separate validation-problem enums, and separate exception types.

Why two:

- A single parser with a "strictness flag" inevitably grows conditional branches that obscure both sets of rules. Splitting keeps each parser readable.
- The two have genuinely different goals. Strict is the round-trip-canonical reference. Lenient is the "import what's out there" path. Combining them under one validation surface would force compromises on both sides.
- Both parsers can be evolved independently. A lenient tolerance does not require a strict-side change.

The cost is some duplication (mostly in the tag and movetext loops). The benefit is clarity at the call site and clarity in the rule statements.

### 5.3 Package layout

The top-level package `com.dlb.chess` is organised by concern, not by layer:

| Package | Responsibility |
|---|---|
| `board` | `Board`, position state, move execution, game-status queries |
| `model` | Cross-cutting model types (`PgnHalfMove`, etc.) |
| `enums` | Domain enums shared across the codebase |
| `fen` | FEN parsing (basic and advanced) and validation |
| `san` | SAN parsing, validation, generation |
| `moves` | Legal move enumeration and execution helpers |
| `pgn.parser` | Strict and lenient PGN parsers, plus the shared tokenizer in `pgn.parser.sequential` |
| `pgn.create` | PGN export |
| `unwinnability` | CHA implementation: quick and full variants for unwinnability and dead-position questions |
| `analysis` | Game-level analysis: threefold-claim-ahead, repetition reports, 50-move sequences, etc. |
| `analyze` | Higher-level orchestration over `analysis` |
| `common` | Generic utilities (null-wrapper helpers, file utilities, etc.) and exceptions |

Packages depend in roughly that order (top to bottom).

---

## 6. PGN — intentional deviations from the specification

The library implements the PGN specification closely. Two areas are intentional departures, in both cases following **python-chess** as the de-facto reference (and, for pre-game commentary, also **Lichess**), where the formal spec is silent, ambiguous, or marked as not fully defined.

### 6.1 Newlines and tabs preserved in commentary content

The PGN specification's strict export format implies that brace commentary should fit on a single line — newlines normalised to spaces, non-printing characters generally absent. Two factors qualify that:

- The export format itself is explicitly noted in the spec as **not fully defined**.
- The strict prohibition on non-printing characters in the spec applies to **string tokens** (tag values), not to brace commentary content. The commentary case is silent.

clean-chess takes the more permissive (and more useful) reading: **`\t` and `\n` inside `{...}` commentary are content**, preserved verbatim through `parse → export → parse`. The rationale is round-trip fidelity for real-world PGN, where comments often carry multi-line annotation. This matches python-chess.

The library still rejects malformed Unicode (lone surrogates, unassigned code points) and other control characters (e.g. bell, escape). The deviation is specifically about tab and LF; CR / CRLF are normalised to LF on input, so the model never carries CR directly.

### 6.2 Pre-game commentary

The PGN specification defines brace commentary attached to half-moves, but **does not formally specify a "pre-game" commentary slot** — commentary that appears between the tag pair section and the first move. python-chess exposes this as `Game.comment`; Lichess supports it on import.

clean-chess follows the same convention: `PgnFile.pregameCommentary()` carries any `{...}` content found before the first half-move, validated under the same commentary contract as move-attached commentary. This is an additive extension rather than a contradiction — a PGN that uses pre-game commentary remains well-formed for any reader that ignores it; readers that recognise it gain a structured place for game-level annotation.

### 6.3 Conformance for everything else

The rest of the library's PGN handling follows the specification: brace-grammar rules, move-number indicators (PGN §8.2.2), inner-brace-as-content (PGN §8.2.5), seven-tag-roster requirements, FIDE game-termination markers, the strict-vs-import format split. The PGN standard already documents the *what*; the code is the authoritative *how*.

---

## 7. Testing strategy

clean-chess relies heavily on a large regression test suite. The strategy:

- **Broad coverage by code area** — every package has dedicated tests; rule-level decisions have multi-fixture parameterised tests.
- **Edge-case fixtures** — positions and games chosen specifically to stress the rule engine: 75-move-rule games, fivefold-repetition games, dead positions, near-misses, long forced sequences.
- **Long games** — games with hundreds of half-moves, including imported real-world games and synthetic stress tests.
- **Random games** — generated games used to surface bugs that targeted fixtures miss.
- **Cross-library validation** — selected fixtures are also processed by other chess libraries; disagreements surface as test failures and have, in the past, led to bug reports against those other libraries (and to bug fixes here when *they* were right).
- **PGN test corpus** — PGN fixtures are organised under `src/test/resources/pgnParser/` with a clear separation between **strict-parser-only** fixtures, **lenient-parser-only** fixtures, fixtures that **both parsers must accept identically**, and **legacy** corpora used for audit-style regression checks.

The test suite is the project's safety net. Refactors are expected to leave the test count unchanged or growing; if they don't, the change is suspect.

---

## 8. Comment style

Comment policy for the codebase:

- **Keep:** decisions, trade-offs, spec references (e.g. `PGN spec §8.2.5`, `FIDE 9.6.2`), subtle invariants, counter-intuitive behaviour.
- **Drop:** restating the code, narration of implementation steps, double-bookkeeping of test intent, and especially **filesystem paths or other physically-mirrored facts in prose** — those duplicate information the code already carries and silently rot when the code is reorganised.

No fixed line-count rule; a longer comment is fine when the content is genuinely irreplaceable. Rule of thumb: if an AI could regenerate the comment from the code, the comment is a maintenance liability.
