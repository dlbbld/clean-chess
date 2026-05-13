# Changelog

Releases from 3.3 onward. Earlier history is in git tags only.

## [Unreleased]

### Notable
- **Lenient FEN parser added.** `LenientFenParser` (and `Board.fromFenLenient(String)`) runs a syntactic-tolerance pre-pass before delegating to `FenParserAdvanced`. Forgives whitespace deviations (leading/trailing/extra/tab-or-newline), missing trailing counters (four-field FENs from Stockfish UCI, five-field FENs with missing fullmove), uppercase side-to-move, non-canonical castling order, non-ASCII dashes in the en-passant field (em-dash, en-dash, etc.), uppercase en-passant target square, and trailing garbage tokens. The lenient layer also auto-corrects the half-move-clock-vs-full-move-number inconsistency by bumping the full-move number up to the minimum consistent value. Every transform that fires surfaces as a typed `ForgivenFenItem` on the validation result; strict semantic invariants are unchanged (a FEN with a missing king, a pawn on rank 1, or an impossible double-check still fails). Twelve `ForgivenFenItemCode` values; one test fixture per code plus an end-to-end deficient-FEN case.
- **Half-move-clock vs full-move-number consistency check promoted into `FenParserAdvanced`.** A FEN like `... 15 1` (15 half-moves played, claiming move 1) is physically impossible and is now rejected by strict FEN parsing as `INVALID_HALF_MOVE_CLOCK_TOO_BIG_RELATIVE_TO_FULL_MOVE_NUMBER`. The check previously lived in the test-only `FenParserAdvancedFurther` (which was kept out of main because of the practical incompatibility with FEN exporters that emit `fullMoveNumber=1` as a placeholder); the lenient FEN parser's auto-correction makes that practical concern moot. The `fullMoveNumber=1 ⇒ initial-or-after-first-move` branch from `FenParserAdvancedFurther` is dropped entirely — too unfriendly to real-world exporters.
- **`LenientPgnParser` routes the FEN tag through `LenientFenParser`.** Symmetry with movetext leniency: a deficient FEN tag in a lenient-parsed PGN no longer fails, the FEN-level forgiveness is applied silently. Strict PGN parsing reads the FEN tag through strict advanced parsing — unchanged.
- **PGN parse, semantics, and export separated into four explicit jobs.** Parsers now preserve input as given — no fabrication into the parse model, no tag-list normalisation. Export gains an explicit `WriteMode { SEMANTIC, ARCHIVAL }` axis: semantic emits the parse model as-given (the default), archival produces a PGN spec §8.1.1-conformant artifact (opt-in). The library's posture is honest preservation by default; archival storage is a mode the caller asks for, not a tax the parser levies.
- **Strict parser stops requiring the full Seven Tag Roster.** PGN spec §8.1.1 introduces STR as required *"for archival storage of PGN data,"* not for general spec-compliant PGN. Strict parsing now requires only the semantic essentials — a Result tag (whose value must match the termination marker) and the SetUp/FEN coupling. A four-tag PGN (Result + a few extras) parses cleanly through `StrictPgnParser`.
- **`PgnFile` carries a new `terminationMarker` field.** The movetext game-termination marker is now an independent signal from the `Result` tag in `tagList()`. Both, either, or neither may be present in lenient-parsed input. Strict-parsed input always has both (and they match). This replaces the previous implicit conflation where "Result tag absent" was indistinguishable from "ongoing."
- **New `tagForgivenItems()` channel on `LenientPgnParserValidationResult`** lists tag-level deviations the lenient layer tolerated (missing STR entries, Result tag absent, FEN-without-SetUp, redundant initial-position FEN/SetUp). Mirrors the existing `sanForgivenItems()` channel for SAN-level deviations.
- **`PgnCreate.createPgnFile(Board)` produces the minimal honest shape** — empty `tagList` for an initial-position board, `[SetUp, FEN]` for a non-initial position, `terminationMarker` derived from the board's game-status. STR fabrication moves to the archival export path.
- **Spec-correct placeholder values in archival output.** Date now uses `????.??.??` per PGN spec §8.1.1.3 (previously `?`). Result defaults to `*` per §8.1.1.7 when neither tag nor termination marker is provided.
- **`specification.md` §3.3.2** documents the four-jobs contract and the strict parser's revised mandate.

### Breaking
- `FenAdvancedValidationProblem` gains `INVALID_HALF_MOVE_CLOCK_TOO_BIG_RELATIVE_TO_FULL_MOVE_NUMBER`. FENs with half-move-clock greater than the maximum consistent with the full-move number (e.g. `... 15 1`) now fail strict FEN parsing — they previously passed. Callers that constructed `Board` via `new Board(String)` from such FENs should switch to `Board.fromFenLenient(String)` for auto-correction.
- `PgnFile` record gains a 5th component `@Nullable ResultTagValue terminationMarker`. Any direct constructor call needs the extra argument.
- `LenientPgnParserValidationResult` record gains a 6th component `ImmutableList<ForgivenTagItem> tagForgivenItems`. Any direct constructor call needs the extra argument.
- `LenientPgnParserValidationException` 5-argument constructor replaced by a 6-argument constructor that also carries the accumulated tag-level forgiven items.
- `StrictPgnParserValidationProblem.TAG_NOT_ALL_REQUIRED_TAGS_SET` renamed to `TAG_RESULT_MISSING` — the strict parser only requires the Result tag now, not the full STR.
- `TagPlaceHolderUtility` (package-private) deleted; archival fill now lives in `PgnArchivalNormalization`.
- `PgnCreate.createPgnFile(Board)` no longer fabricates the Seven Tag Roster into the model. Consumers who relied on the produced `PgnFile` containing STR placeholders should switch to `WriteMode.ARCHIVAL` on the writer.
- `PgnWriter.writePgnFile(PgnFile, ...)` defaults to `WriteMode.SEMANTIC` — the output preserves the parse model rather than fabricating canonical form. Pass `WriteMode.ARCHIVAL` explicitly for the prior fabrication behavior.

### Migration
For typical use (parse a PGN, replay it, write it back): no source change needed — the writer defaults to semantic mode, which round-trips the parse model. If your previous workflow depended on the writer producing a canonical (STR-filled, sorted) artifact, pass `WriteMode.ARCHIVAL` to the relevant `PgnWriter` / `PgnCreate` entry point.

If your code matches on `StrictPgnParserValidationProblem.TAG_NOT_ALL_REQUIRED_TAGS_SET`, rename to `TAG_RESULT_MISSING`. The strict parser now only rejects when the Result tag is missing; other STR-omitted PGNs that previously raised this error now parse successfully and surface the missing tags as lenient-parser forgiven items if processed via the lenient path.

If your code constructs `Board` from FEN strings with `fullMoveNumber=1` placeholders on non-initial positions (a common "speculative" pattern from FEN exporters that don't know the actual move number), switch to `Board.fromFenLenient(String)`. The lenient FEN parser auto-corrects the inconsistent counter and surfaces a `HALF_MOVE_CLOCK_INCONSISTENT_WITH_FULL_MOVE_NUMBER` forgiven item.

## [6.0.0] - 2026-05-13

Technical-cleanup release. No new features. The model is tighter, generator-printed code is gone, a research-only dual code path is retired, and one pawn-wall bug is fixed (with the known remaining false-positive class scoped to the DeepSquare release).

### Notable
- `LegalMove` now carries a `LegalMoveKind` category (`NORMAL` / `CASTLING` / `EN_PASSANT_CAPTURE` / `PAWN_TWO_SQUARE_ADVANCE` / `PROMOTION`). Consumers stop recomputing the category from `MoveSpecification` fields.
- FEN single-letter vocabulary moved off chess-domain types into `FenSideSymbol` / `FenPieceSymbol`. The chess `Side` / `Piece` enums no longer know FEN syntax.
- En-passant threefold-repetition research dual-path retired. The FIDE-correct path is the only path.
- Square geometry consolidated to compact precomputed tables; ~thousand-line generated tables collapsed to small static initializers.
- Large generated enums replaced with computed lookups: `UciValidateHelper` (1984 lines) and the seven SAN-validator strict enums (5105 lines combined).
- Pawn-wall classifier: bishop-reachability now BFS-correct (closes a previously-known false negative). The known false-positive class is documented in [pawn-wall-soundness.md](pawn-wall-soundness.md) and deferred to the DeepSquare release.
- FEN-validation documentation no longer overclaims "no real game could reach"; reframed as structural and rule-consistency validation.
- CHA / unwinnability documentation reframed: README and `unwinnability/package-info.java` now describe unwinnability as "no legal sequence can end with that side giving checkmate, even if the opponent cooperates" (replacing the misleading "worst-case play" framing). Same pass corrects the README's "CHA full is 100% accurate" to align with the documented `UNDETERMINED` outcome.
- `NonNullWrapperCommon` renamed to `Nulls` (pervasively used; short name was overdue).
- Javadoc tightened: `doclint=all,-missing` with `failOnError=true`. Opt-in (not bound to `mvn package`) — run explicitly via `mvn javadoc:javadoc` or `mvn javadoc:jar`. 15 broken `{@link}` references uncovered when the gate was turned on are fixed; the published javadoc now builds clean.

### Breaking
- `LegalMove` constructor: `LegalMoveKind` is now mandatory; the three-argument form is removed.
- `EnPassantRole` enum removed. Use `legalMove.kind() == LegalMoveKind.EN_PASSANT_CAPTURE` / `LegalMoveKind.PAWN_TWO_SQUARE_ADVANCE`.
- `EnPassantCaptureRuleThreefold` enum and the dual-path repetition fields on `Report` removed.
- `UciValidateHelper` enum removed (internal).
- `Side.calculate(String)`, `Side.getFenLetter()`, `Piece.getLetter()` removed — FEN-letter knowledge belongs to the FEN parser, not chess-domain types.
- `NonNullWrapperCommon` class renamed to `Nulls`.

### Migration
For typical use (`Board`, the parsers, the reporters): none. The breaking surface is internal vocabulary plus a single record shape change.

## [5.0.0] - 2026-05-11

Reduce public API surface release. No feature changes; the surface is narrowed to what was always intended — play chess correctly and report rule-true outcomes. Material arithmetic and other internal helpers that supported that intent are now internal.

### Breaking — packages removed
- `com.dlb.chess.utility` — split into the feature packages that own each helper.
- `com.dlb.chess.range` — absorbed into `com.dlb.chess.squares` (the only consumer).
- `com.dlb.chess.distance` — absorbed into `com.dlb.chess.unwinnability` and made internal.

### Breaking — types no longer public
- `MaterialUtility`, `InsufficientMaterialUtility` — material-arithmetic helpers that were never part of the contract. Insufficient-material termination remains observable via `Board.isInsufficientMaterial(Side)` and `Board.calculateInsufficientMaterial()`.
- `ChessBoard` interface — collapsed into `Board`. There is one board type.
- Numerous internal helpers across `squares`, `moves`, `san`, `unwinnability`, `pgn`, `report` that were public only because they sat in `src/main/java`. After this release the implementation classes that exist to serve a public entry point are package-private.

### Migration
For typical use (`Board`, the parsers, the reporters): none.

If your code referenced one of the removed-or-demoted types, it was reaching into internals. The current public API is the supported surface; please open an issue if you have a legitimate use case that no longer fits.

### Notable
- Sub-package flattens for `squares`, `moves`, `unwinnability`, `san`, `pgn`, `report` — each feature is now a single coherent package rather than a tree of mostly-trivial sub-packages.
- Removed unused public enum convenience methods for double-step file/rank movement and thin rank predicates.

## [4.0.0] - 2026-05-10

Lenient SAN release. New parser pipeline accepts a defined set of forgivable deviations from canonical SAN; the move-execution and parser API is renamed across the board to make the strict / lenient axis explicit at every call site.

### Breaking — move execution
- `Board.performMove(String)` → `Board.moveStrict(String)`; now returns `StrictSanParserValidationResult` (the resolved `MoveSpecification` is on the result) rather than `boolean`.
- `Board.performMove(MoveSpecification)` → `Board.move(MoveSpecification)`; still returns `boolean`.
- `Board.performMoves(String...)` → `Board.movesStrict(String...)`; still returns `boolean`.
- `Board.unperformMove()` → `Board.unmove()`.
- New `Board.moveLenient(String)` returns `LenientSanParserValidationResult` carrying the resolved `MoveSpecification` together with the list of forgiven SAN deviations.
- `ChessBoard` interface methods renamed to match (`move`, `moveStrict`, `moveLenient`, `movesStrict`, `unmove`).

### Breaking — SAN parser
- `SanValidation` renamed to `StrictSanParser`. Entry-point method `validateSan(String, ChessBoard)` renamed to `parseText(String, ChessBoard)`. Return type changed from `MoveSpecification` to `StrictSanParserValidationResult` (one-field record); to keep the prior shape, append `.moveSpecification()`.

### Migration
For most callers, the change is mechanical:
| Before | After |
|---|---|
| `board.performMove("e4")` | `board.moveStrict("e4")` |
| `board.performMove(moveSpec)` | `board.move(moveSpec)` |
| `board.performMoves("e4", "e5")` | `board.movesStrict("e4", "e5")` |
| `board.performMoveLenient("nf3")` | `board.moveLenient("nf3")` |
| `board.unperformMove()` | `board.unmove()` |
| `SanValidation.validateSan(san, board)` | `StrictSanParser.parseText(san, board).moveSpecification()` |

### Notable
- New lenient SAN pipeline (`com.dlb.chess.san.lenient.LenientSanParser`) with a 21-code forgiven-item taxonomy: castling-with-zero, UCI / long-algebraic notation, explicit pawn letter, missing promotion equals, missing / spurious capture marker, six check / checkmate suffix mismatches, three over-specification cases, non-standard rank disambiguation, and four case-variation codes. Every accepted deviation surfaces as a typed `ForgivenItem` with the original token and the canonical-SAN equivalent — consumers can silently accept or warn. See `specification.md` §3.3.1.
- `LenientPgnParser` now wires the lenient SAN pipeline into the movetext path. `LenientPgnParserValidationResult` gains `pgnFile` and `sanForgivenItems` fields; `validateText` is now the rich entry point that returns the parsed file alongside the validation status.
- `LenientSanParserValidationException` carries `GameStatus` so the `GAME_ALREADY_ENDED` propagation works end-to-end through the PGN layer.
- PGN tokenizer recognises `0-0` / `0-0-0` as castling SAN tokens rather than (invalid) termination markers.
- README "Lenient PGN parser" section documents the SAN tolerances with a worked example.

### Internal
- Strict SAN pipeline reused unchanged for chess validation; the lenient layer is a thin input-shape transformer plus a strict-replay-with-recovery loop. Deliberately not recovered: mixed `0-O` castling, pawn `SPURIOUS_CAPTURE_MARKER` (no clean string mutation; recovery would silently swap the user's intended pawn), and game-already-ended (top-of-pipeline guard, identical to strict).

## [3.3.0] - 2026-05-09

Cleanup follow-through release. Documentation, naming, packaging, and design-consistency polish across the library.

### Breaking
- Package `com.dlb.chess.internationalization` renamed to `com.dlb.chess.messages`.
- Type prefix `Yawn*` renamed to `NoProgress*` (`YawnHalfMove` → `NoProgressHalfMove`, etc.) — matches FIDE / chess-community terminology.
- `ChessBoard.isGameEnd()` / `isGameDraw()` removed (were unused; carried a contested CHA-opt-in semantics).
- `InvalidMoveException` now extends `UsageException` (was extending `RuntimeException` directly).
- `Board.calculateLegalMove(...)` narrowed to package-private (was public-static; carried a contract no external caller could uphold).
- `Board.createPositionAfterMove(...)` moved to `StaticPositionUtility` (pure transformation, doesn't belong on the game class).

### Notable
- New `Reporter.calculateReportText(...)` returns the report as a string — for non-CLI consumers (web responses, file writes, GUIs).
- `log4j-core` dropped from runtime dependencies; `log4j2.xml` no longer ships in the JAR. Consumers no longer inherit the library's logging backend or configuration.
- `FileUtility.writeFile(...)` now propagates `IOException` (was silently swallowing).
- Javadoc on the public API; `mvn package` now produces a `-javadoc.jar`.
- JAR manifest carries `Implementation-Title` / `-Version` / `-Vendor`.
- Thread-safety contract documented in `specification.md` §2.4.
- New `CHANGELOG.md` (this file) and `CONTRIBUTING.md`.

### Internal
- All DGT-derived paid-work content removed.
- Test fixture tree reorganized (`pgn/cua` → `pgn/cha`, lichess subtree given hierarchical layout).
- `ConfigurationConstants.LOCALE` switched from `Locale.US` to `Locale.ROOT`.
- Test-only `GameStatusAnalysis`, `PROJECT_ROOT_FOLDER_PATH` relocated out of `src/main`.
- Many smaller items — see `tasks.md` "Current release — cleanup follow-through" for the per-task breakdown.
