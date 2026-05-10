# Changelog

Releases from 3.3 onward. Earlier history is in git tags only.

## [Unreleased]

## [4.0.0] - 2026-05-10

Lenient SAN release. New parser pipeline accepts a defined set of forgivable deviations from canonical SAN; major API rename across move execution and parser entry points.

### Breaking
- `Board.performMove(String)` → `Board.moveStrict(String)`; now returns `StrictSanParserValidationResult` (the `MoveSpecification` is on the result) rather than `boolean`.
- `Board.performMove(MoveSpecification)` → `Board.move(MoveSpecification)`; still returns `boolean`.
- `Board.performMoves(String...)` → `Board.movesStrict(String...)`; still returns `boolean`.
- `Board.unperformMove()` → `Board.unmove()`.
- New `Board.moveLenient(String)` returns `LenientSanParserValidationResult` carrying the move plus the list of forgiven SAN deviations.
- `SanValidation` class renamed to `StrictSanParser`; entry-point method `validateSan(String, ChessBoard)` renamed to `parseText(String, ChessBoard)`; return type changed from `MoveSpecification` to `StrictSanParserValidationResult`.
- `ChessBoard` interface methods renamed to match (`move`, `moveStrict`, `moveLenient`, `movesStrict`, `unmove`).

### Notable
- New lenient SAN pipeline (`com.dlb.chess.san.lenient.LenientSanParser`) with a 21-code forgiven-item taxonomy: castling-with-zero, UCI / long-algebraic notation, explicit pawn letter, missing promotion equals, missing / spurious capture marker, six check / checkmate suffix mismatches, three over-specification cases, non-standard rank disambiguation, and four case-variation codes. Surfaces every accepted deviation as a typed `ForgivenItem` with the original token and canonical equivalent. See `specification.md` §3.3.1.
- `LenientPgnParser` now wires the lenient SAN pipeline into the movetext path. `LenientPgnParserValidationResult` gains `pgnFile` and `sanForgivenItems` fields; `validateText` is now the rich entry point that returns the parsed file alongside the validation status.
- PGN tokenizer recognises `0-0` / `0-0-0` as castling SAN tokens rather than (invalid) termination markers.
- README "Lenient PGN parser" section documents the SAN tolerances with a worked example.

### Internal
- Strict SAN pipeline reused unchanged for chess validation; the lenient layer is a thin input-shape transformer plus a strict-replay-with-recovery loop.

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
