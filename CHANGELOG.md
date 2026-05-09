# Changelog

All notable changes to clean-chess are documented in this file.

The format follows [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and the project adheres to [semantic versioning](https://semver.org/).

For the release history before 3.3, see the git tag list — this changelog starts here.

## [Unreleased]

### Added
- `Reporter.calculateReportText(...)` — three overloads matching `printReport(...)`, returning the same human-readable report as a single string with `\n`-joined lines. Lets non-CLI consumers (web responses, file writes, GUIs) use the report data without capturing stdout.
- Smoke test `TestReporterPrintReport` — covers both `printReport` (no-throw on missing message keys) and `calculateReportText` (non-empty output, LF line endings).
- Cross-validation test `TestKnightDistanceAgainstAmbronaReference` — independently ports Ambrona's closed-form knight-distance algorithm and asserts agreement with the production BFS implementation across all 4096 ordered square pairs.
- Class-level Javadoc on `Board` and `Reporter`; method-level Javadoc on `Board`'s public surface (constructors, `performMove`, `unperformMove`, FIDE rule predicates) and on the four CHA default methods on `ChessBoard`.
- Package-level Javadoc on the eight public-facing packages (`pgn.parser`, `pgn.create`, `unwinnability`, `report`, `san`, `fen`, `model`, `enums`), modeled after `board/package-info.java`.
- `maven-javadoc-plugin` wired in `pom.xml` — `mvn package` now produces a `-javadoc.jar` alongside the main artifact.
- Section 2.4 in `specification.md` documenting the project's thread-safety contract.
- `CONTRIBUTING.md` — central entry point for contributors, linking the existing `setup.md`, `coding-conventions.md`, and `agents.md`.
- "Records carry data, not behavior" section in `coding-conventions.md` documenting the design principle (records as the M in MVC; computational and business logic operating on them lives in dedicated utility/service classes).

### Changed
- Renamed Java package `com.dlb.chess.internationalization` → `com.dlb.chess.messages` (matching matching resource path); the package contained externalized messages, not locales — the old name overstated what shipped.
- `ConfigurationConstants.LOCALE`: `Locale.US` → `Locale.ROOT` — locale-neutral default, predictable for library consumers regardless of where the JVM runs.
- Renamed `Yawn*` → `NoProgress*` across the codebase (`YawnHalfMove` → `NoProgressHalfMove`, `YawnMoveUtility` → `NoProgressMoveUtility`, `report.yawnmove.*` keys → `report.noProgressMove.*`, plus test fixture filenames and folders). The internal "yawn move" jargon is replaced with the standard chess concept "no-progress halfmove" (a halfmove that doesn't reset the FIDE 50/75-move-rule clock).
- Renamed test fixture tree `pgn/cua/` → `pgn/cha/` — the project ports Miguel Ambrona's *Chess Unwinnability Analyzer*, which he abbreviates **CHA** in his project (repo, binary, source identifiers); the test fixtures used the wrong abbreviation. Scope expanded during the work to a full reorganization: the lichess subtree gained a hierarchical layout, fixture filenames were normalized to `lichess_<id>.pgn` (canonical) and `lichess_<id>_helpmate.pgn` (derived).
- Renamed `Reporter.calculateAnalysis` → `Reporter.calculateReport` (and matched `Report` model class); test directory `test/analysis` → `test/report`; message-bundle keys `analysis.*` → `report.*`. Closes the remaining naming drift after the previous release renamed the `analysis` package to `report`.
- `InvalidMoveException` now extends `UsageException` (was `RuntimeException` directly), bringing it into the project's exception hierarchy — consumers can `catch (UsageException e)` to handle all caller-fault cases uniformly.
- `Board.calculateLegalMove` is now package-private. Constructing a `LegalMove` value object requires the caller to have already verified the input `MoveSpecification` is legal — an invariant only the rule pipeline can guarantee.
- `Board.createPositionAfterMove` moved to `StaticPositionUtility` — pure-function utility belongs with the type it transforms, not on the game class.
- `KnightDistance` rewritten — the previous implementation was copied externally with thin attribution; the new implementation builds the 64×64 distance table at class init via BFS over the knight graph.
- Eclipse `.classpath` now references `JavaSE-17` (was stale `JavaSE-26` after the JDK target switch).
- `internationalization.Message` has a private constructor (matches the project's other utility-class style).
- README's "Building/Installing" section split into separate audiences — "Using clean-chess as a dependency" (Maven/Gradle coordinates) and "Building from source" (CLI + setup.md link for contributors).
- `FileUtility.writeFile` now throws `FileSystemAccessException` on `IOException` instead of silently printing the stack trace and returning, matching the read/append/delete methods. `appendFile` modernized to use the `java.nio.file.Files` API.

### Removed
- `isGameEnd()` and `isGameDraw()` from `ChessBoard` — both were unused, and `isGameEnd` carried a contested CHA-opt-in semantics (called `isDeadPositionQuick()` while `BasicChessUtility.calculateGameStatus()` did not). Deletion avoids picking a contested meaning for an API nobody calls.
- `TestMessage.java` and the 12 fixture keys it consumed (`test*` and `test.message.*`) from `messages.properties` — the tests were exercising JDK behavior (`ResourceBundle.getString`, `MessageFormat.format`) rather than library code; the one custom layer worth covering (`NonNullWrapperCommon.normalizeSpace`) moved to `TestNonNullWrapperCommon` driven by synthetic strings.
- All DGT-derived material — paid-work content that should not have been in the open-source library. Constants (`DGT_MY_BLUETOOTH_BOARD_ID` etc.) from `ConfigurationConstants`, enum entries `DGT_LIVE_CHESS` / `DGT_CENTAUR` from `PgnTest`, factory methods + dispatch entries in `PgnExpectedValue`, and ~10 fixture PGNs across two resource trees.
- `MultiplePgnSplitUtility.java` from `src/main` (relocated to `src/test`) — had a `main()` and a personal-database hardcoded path. The capability was preserved as a parameterized test-only utility.
- `log4j2.xml` and `log4j-config-2.xsd` no longer ship in the published JAR (relocated to `src/test/resources`). `log4j-core` dropped from runtime to test scope. Consumers no longer inherit the library's logging configuration or backend choice.
- `ConfigurationConstants.PROJECT_ROOT_FOLDER_PATH` and `BasicUtility.readProjectFolderPath()` — used exclusively by tests but executing on every consumer's first class-load. Relocated to `ConfigurationTestConstants` in test scope.
- The four `IS_DEBUG = false` dead branches under `unwinnability/findhelpmate/*` (replaced with proper Log4j logging gated by the same flag).
- `analysis.board.*` message-bundle block — DGT/trainer-only, unused, contained a wrong score string (`blackWin=0-0` should have been `0-1`).

### Fixed
- README headline code samples that no longer compiled (`Analyzer.printAnalysis(pgn)` was renamed to `Reporter.printReport(pgn)` in the previous release; three example blocks were stale).
- README's "Create PGN for game" example called `System.out.println(pgnFile)` and showed formatted PGN output as the result — but `PgnFile` is a record with no `toString()` override, so the actual output was the auto-generated `PgnFile[...]` representation. Replaced with `PgnCreate.createPgnFileString(pgnFile)`.
- README's "Not supported" section claimed PGN move suffix annotations and BOM input were unsupported — both are actually supported (suffix annotations are fully parsed, modeled, and round-tripped on export by both parsers; BOM is stripped on input by the lenient parser).
- `messages.properties` `report.yawnmove.*` keys had drifted from `Reporter.java`'s `report.noProgressMove.*` lookups, causing `Reporter.printReport(...)` to throw `MissingResourceException` at runtime on any pgn with content. Surfaced by the new `TestReporterPrintReport` smoke test.
- `WinnableUtilityAnalyzeChaLichess.java` — corrected `Analzye` typo in filename + class name.
- `.eclipse-pmd` and the PMD builder/nature in `.project` removed — `setup.md` only documents the Checkstyle plug-in, so the PMD references caused fresh-checkout warnings.
- README "Not supported" version mismatch between Maven (3.3.0) and Gradle (3.2) snippets, plus Gradle's deprecated `compile` configuration replaced with `implementation`.

