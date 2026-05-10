# Tasks

Order within each section is the source of truth. Completed tasks move to **Done** at the bottom.

---

## Current release — cleanup follow-through

Theme: doc correctness, dead/personal code purge, library packaging hygiene, naming follow-through from the rename in the previous release, and one design-consistency fix (`isGameEnd` vs CHA opt-in). No new features.

### README correctness
- [x] Fix the three headline code samples that no longer compile: `Analyzer.printAnalysis(pgn)` → `Reporter.printReport(pgn)` (`README.md:126`, `:160`, `:205`)
- [x] Fix the "Create PGN for game" example: `System.out.println(pgnFile)` does not produce the formatted PGN shown in the comment (`PgnFile` is a record with no `toString()` override). Replace with `System.out.println(PgnCreate.createPgnFileString(pgnFile))`
- [x] Update "Not supported" section: PGN move suffix annotations *are* parsed and exported (see `StrictPgnParser`, `LenientPgnParser`, `PgnCreate`), and UTF-8 BOM *is* stripped by the lenient parser. Both bullets need to either go or be re-stated as the actual current limits
- [x] Split audiences explicitly under "Building/Installing": one paragraph for *using clean-chess as a dependency* (no Eclipse, just Maven/Gradle coords), one for *contributing* (link to `setup.md`). Today the section reads as if every consumer needs Eclipse

### specification.md correctness
- [x] §2.2 overclaims compact-constructor validation — `Fen`, `Tag`, `PgnHalfMove` have no validation; `PgnFile` only copies lists. Either soften the claim, or add real boundary validation to those records (preferable: validate, since "errors at the construction boundary" is a load-bearing project value)
- [x] §4 architecture table is missing 6 top-level packages: `distance`, `exceptions`, `internationalization` (or its successor — see below), `range`, `squares`, `utility`; plus 2 pgn subpackages: `pgn.diagnostic` and `pgn.writer`. Add rows or explicitly note them as utility/internal
- [x] If `isGameEnd` semantics change (next subsection), update the relevant termination wording — N/A: `isGameEnd` was deleted entirely; spec §3.1 was already framed at the abstract FIDE level, not method-level, so no doc update needed

### messages.properties cleanup
The buggy `analysis.board.score.blackWin=0-0` is gone — the entire `analysis.board.*` block was DGT/trainer-only and unused; removal verified clean by audit.

- [x] Rename remaining `analysis.*` keys to `report.*` to follow through on the package rename from the previous release. The Java side is renamed; the surviving `analysis.repetition.*` and `analysis.yawnmove.*` keys still say `analysis`; subsequent `report.yawnmove.*` → `report.noProgressMove.*` follow-through done
- [x] Fix the comment typo `##analzyer` → `##analyzer` at line 251 — comment removed entirely; cleanest resolution since the section header was no longer load-bearing

### TestMessage fixture cleanup
The 12 `test*` / `test.message.*` keys at the bottom of `messages.properties` plus `TestMessage.java` itself are mostly testing JDK behavior (`ResourceBundle.getString`, `MessageFormat.format`) rather than library code. The one custom layer worth covering is `Message.normalizeSpace(...)`. The current shape is "I was getting comfortable with the API" code that mature projects clean up.

- [x] Drop the basic key-lookup tests (`testBasic`) — they exercise `ResourceBundle.getString`, which the JDK has tested
- [x] Drop the placeholder substitution tests (`testWithoutPlaceholder`, `testWithOnePlaceholder`, `testWithTwoPlaceholders`) — they exercise `MessageFormat.format`, also JDK-tested
- [x] Keep one or two whitespace tests covering `NonNullWrapperCommon.normalizeSpace` (the genuinely custom behavior), but drive them with synthetic strings — no property-file fixtures needed (relocated to `TestNonNullWrapperCommon` as `testNormalizeSpace`)
- [x] Delete the 12 `test*` / `test.message.*` keys + the `#testing` block from `messages.properties` once the tests no longer need them
- [x] Net effect: the production JAR no longer ships test fixture data; `TestMessage.java` deleted entirely (along with its empty `internationalization/` test package); whitespace coverage now lives next to the helper it covers

### Remove all DGT-derived material (paid-work content)
The DGT/trainer code originates from paid work and should not ship in this open-source library. The constants in `src/main` are already on the dead-code list above; the test material below is the larger part.

- [x] Remove `DGT_MY_BLUETOOTH_BOARD_ID = 23944`, `DGT_MY_USB_BOARD_ID = 43462`, `DGT_ACTIVE_BOARD_ID`, and the Google text-to-speech credentials comment from `ConfigurationConstants.java`
- [x] Drop `DGT_LIVE_CHESS` and `DGT_CENTAUR` from [`PgnTest.java:89-90`](src/test/java/com/dlb/chess/test/pgntest/enums/PgnTest.java:89)
- [x] Drop `createTestCasesDgtCentaur()` and `createTestCasesDgtLiveChess()` plus their dispatch entries from [`PgnExpectedValue.java`](src/test/java/com/dlb/chess/test/pgntest/PgnExpectedValue.java) (8 + 1 fixture entries, ~50 lines around 3472–3515)
- [x] Drop the `dgt/liveChess` block in [`TestLegacyPgnParsePlaysBeyondAudit.java:227`](src/test/java/com/dlb/chess/test/pgn/parser/beyond/TestLegacyPgnParsePlaysBeyondAudit.java:227)
- [x] Delete fixture directories `src/test/resources/pgn/review/dgt/` (centaur + liveChess subdirs, ~9 PGNs) and `src/test/resources/pgnParser/legacy/common/beyond/dgt/liveChess/` (~1 PGN)
- [x] Verify `git grep -i dgt` returns zero hits afterwards

### Rename `pgn/cua` → `pgn/cha` (correct CHA abbreviation)
The Chess Unwinnability Analyzer is abbreviated **CHA** by Miguel Ambrona (matches the repo name `D3-Chess`, the binary, and all code identifiers in his project). The test fixture tree under `src/test/resources/pgn/cua/` used the wrong abbreviation. Scope expanded substantially during the work — full reorganization of the lichess subtree (drop redundant `unfair/` wrapper, hierarchical layout under `lichess/quick/{depth,notDepth}Three`), filename normalization (`lichess_<id>.pgn` canonical, `lichess_<id>_helpmate.pgn` derived), basename-uniqueness preservation, and prose comment cleanup.

- [x] Rename `src/test/resources/pgn/cua/` → `src/test/resources/pgn/cha/`
- [x] Update path strings in [`PgnTest.java`](src/test/java/com/dlb/chess/test/pgntest/enums/PgnTest.java) to `cha/...`
- [x] Verify `git grep -i "cua"` is clean (no other code or doc references)
- [x] Use `git mv` so history is preserved on the directory rename
- [x] Bonus: full enum/method/comment rename pass (`UNFAIR_*` → `CHA_*`, `unfair_lichess_examples_*.pgn` → `lichess_<id>.pgn`, "unfair" prose → "incorrectly", helpmate suffix decoration)

### Dead-code & personal-data purge
- [x] Delete `MultiplePgnSplitUtility.java` (or relocate to `src/test/java`). It has a `main()`, a hardcoded path component `otherdb/mb-3.45/mb-3.45.pgn`, and an 8-step ChessBase-recipe in comments. No call sites — slipped through the previous "strip demo/dev code" pass
- [x] `KnightDistance.java` — the `// This code contributed by Rajput-Ji` attribution is opaque (looks like GeeksforGeeks origin). Either cite the source URL + verify GPL-v3 compatibility, or rewrite in project style. Drop the `public static void main()` regardless
- [x] Remove the four `private static final boolean IS_DEBUG = false;` dead branches in `unwinnability/findhelpmate/*` (`AbstractFindHelpmate`, `FindHelpmateExhaust`, `FindHelpMateInterrupt`, `mobility/Mobility`). They write `fenListMine.txt` to `TEMP_FOLDER_PATH` when toggled — either delete or replace with proper logger calls
- [x] Move `ConfigurationConstants.PROJECT_ROOT_FOLDER_PATH` and `BasicUtility.readProjectFolderPath()` to a test-only utility. They are used exclusively by tests but currently execute on every consumer's first class-load of `ConfigurationConstants`
- [x] `internationalization.Message` is a `class` with only static methods and no instances — add a private constructor (matches `BasicUtility`, `Reporter`, etc.)

### Library packaging hygiene (do not pollute consumer classpaths)
- [x] Move `src/main/resources/log4j2.xml` and `log4j-config-2.xsd` to `src/test/resources`. Today they ship in the published JAR and log4j2 picks them up on consuming applications' classpaths
- [x] Drop `log4j-core` from runtime dependencies in `pom.xml` — keep only `log4j-api` for the library, let consumers pick the backend. `log4j-core` can stay at `<scope>test</scope>` for the test suite
- [x] Verify with a fresh consumer project that no `log4j2.xml` or `log4j-core` arrives transitively

### isGameEnd vs CHA opt-in design (P1 from review)
[`specification.md:84`](specification.md:84) states CHA is opt-in and not part of the per-move status path, but `ChessBoard.isGameEnd()` was calling `isDeadPositionQuick()`. Meanwhile [`BasicChessUtility.calculateGameStatus()`](src/main/java/com/dlb/chess/common/utility/BasicChessUtility.java:107) — which move validation and PGN export consult — does *not* include quick CHA. So a caller could see "game ended" from one public API while validation/export said "ongoing".

- [x] Decide the canonical policy: either (a) remove `isDeadPositionQuick()` from `isGameEnd()` so all paths agree CHA is opt-in, or (b) make `BasicChessUtility.calculateGameStatus()` include it too — chose **option (d)**: delete `isGameEnd` and its private helper `isGameDraw` entirely, since both were unused. Avoids picking a contested semantic for an API nobody calls.
- [x] If the surface changes meaningfully, update specification.md §3.1 and any package-info.java that describes termination — N/A; both already framed in terms of FIDE-level termination categories and `BasicChessUtility.calculateGameStatus`, not the deleted `isGameEnd`

### FileUtility error handling (P2 from review)
[`FileUtility.writeFile`](src/main/java/com/dlb/chess/common/utility/FileUtility.java:98) catches `IOException` and only prints the stack trace at line 107. [`PgnWriter.writePgnFile`](src/main/java/com/dlb/chess/pgn/writer/PgnWriter.java:24) returns `void`, so callers can believe a PGN was written when it was not.

- [x] `FileUtility.writeFile` must throw `FileSystemAccessException` on `IOException`, matching the read/append/delete methods
- [x] Audit every method in `FileUtility` for the same pattern; align all error paths
- [x] Confirm `PgnWriter.writePgnFile` propagates the failure to its caller

### "Yawn move" → standard chess terminology
The codebase uses `YawnMoveUtility`, `YawnHalfMove`, `YawnPrint`, `YawnRepresentation`, `analysis.yawnmove.*` keys. Neither FIDE, the chess community, the README, nor `specification.md` use this term. The user-facing output already says "Sequences without capture and pawn move…", so the term exists purely as private vocabulary.

- [x] Decide canonical name: `HalfmoveClock*` (matches FIDE), `NoProgress*`, or `FiftyMove*` — chose `NoProgress` (more self-explanatory than `Reversible`/`HalfmoveClock` for general Java audience; matches `messages.properties` user-facing prose "without capture and pawn move")
- [x] Rename Java identifiers across `src/main` and `src/test` — 59-file pass: `YawnHalfMove` → `NoProgressHalfMove`, `YawnIndex` → `NoProgressIndex`, `YawnPrint` → `NoProgressPrint`, `YawnMoveUtility` → `NoProgressMoveUtility`, `calculateYawnMoveRule` → `calculateNoProgressMoveRule`, plus all variables, parameters, and test fixture filenames/folders
- [x] Rename matching keys in `messages.properties` (rolls into the `analysis.*` → `report.*` rename above) — `report.yawnmove.*` → `report.noProgressMove.*`, surfaced by new `TestReporterPrintReport` smoke test
- [x] Update specification.md if it ever uses the term — N/A: specification.md doesn't reference the term

### Eclipse fresh-checkout fidelity
The previous release set the theme "fresh checkout works without manual setup steps". These two contradicted it.

- [x] `.classpath` line 29 still references `JavaSE-26`. Update to `JavaSE-17` to match `pom.xml` and `setup.md`. Without this, fresh Eclipse checkout shows "JavaSE-26 not available" until the user runs Maven > Update Project
- [x] `.project` declares the `ch.acanda.eclipse.pmd.builder.PMDBuilder` builder and a PMD nature, plus `.eclipse-pmd` exists at the repo root. `setup.md` does not mention installing eclipse-pmd. Recommend: remove the PMD builder/nature from `.project` and delete `.eclipse-pmd` (Checkstyle is sufficient). Alternative: document the PMD plug-in install in `setup.md` — chose removal; `.eclipse-pmd` deleted, PMDBuilder removed from `.project`. Bonus: missing `forbiddenReference=warning` JDT compiler setting added

### Javadoc on the public API
- [x] Class-level Javadoc on `Board` and `Reporter` (the two headline classes; today both have none)
- [x] Method-level Javadoc on `Board`'s public methods at minimum: the three constructors, `performMove(String)`, `performMove(MoveSpecification)`, `performMoves(String...)`, `unperformMove`, `isCheckmate`, `isStalemate`, `isThreefoldRepetition`, `isFiftyMoveRule`, `isUnwinnableQuick`, `isUnwinnableFull`, `isDeadPositionQuick`, `isDeadPositionFull`. One sentence each is enough — `isFiftyMove`/`isThreefoldRepetition`/`isSeventyFiveMove`/`isFivefoldRepetition` documented on `Board`; the four CHA default methods (`isUnwinnableQuick`/`isUnwinnableFull`/`isDeadPositionQuick`/`isDeadPositionFull`) documented on `ChessBoard` where they're defined
- [x] Package-level Javadoc on the public-facing packages (`pgn.parser`, `pgn.create`, `unwinnability`, `report`, `san`, `fen`, `model`, `enums`). Model after [`board/package-info.java`](src/main/java/com/dlb/chess/board/package-info.java) — that one is excellent
- [x] Configure `maven-javadoc-plugin` in `pom.xml` so JitPack ships a `-javadoc.jar` — verified `mvn javadoc:jar` produces `target/clean-chess-3.3.0-javadoc.jar` (~1.9 MB). `<doclint>none</doclint>` for now to avoid breaking the build on legacy missing tags; turning doclint strict is a future tightening

### Externalized-messages package — narrow fixes (not a redesign)
The `messages.properties` mechanism itself is good engineering — externalization buys terminology-consistency review and translation-readiness independent of whether translation ever happens. Two narrow fixes, neither requiring a redesign:

- [x] **Package name.** `internationalization` overstates what ships. The package contains externalized messages; it does not contain locales. Rename to `messages` (or `i18n`). The future-translation argument fits a `messages` package fine — translators just add `messages_de.properties` regardless of what the package is called — chose `messages` (most honest, no abbreviation). Java package + resource path + 16 import statements + 2 javadoc refs + spec §4 row all updated
- [x] **`ConfigurationConstants.LOCALE = Locale.US` is a trap** for the day someone adds a locale. Switch to `Locale.ROOT` (semantically "the default bundle") or `Locale.getDefault()` (respects JVM/user environment). Today the choice is invisible because no message uses locale-sensitive `MessageFormat` constructs (no `{0,number}`, no `{1,date}`); the hardcoding becomes load-bearing the moment any message does — chose `Locale.ROOT` (predictable for library consumers; locale-neutral); added a comment explaining why

### Smaller items
- [x] `tasks.md` references commit hashes in the **Done** section that won't survive a squash-merge into `main`. Either strip the hashes when an item moves to Done (it's the *fact* that's load-bearing, not the SHA), or note that tasks.md is dev scaffolding and not a permanent record — stripped

The Maven Central / `groupId` migration has its own dedicated release at the bottom of this file — see *Future release — publish to Maven Central*.

### Second-pass review follow-ups (pre-release polish)
After all 14 cleanup-follow-through subsections closed, a fresh-eyes review surfaced a remaining set of items. Treating them as the final pre-release polish.

- [x] **`Board.calculateLegalMove` made package-private.** Was `public static` — exposed a way for outside code to construct a `LegalMove` value object that lies about its contract (the type carries derived data — moving piece, captured piece, en-passant role — that's only correct when the input was actually validated as legal, an invariant only the rule pipeline can guarantee). The only caller (`TestLegalMovesAgainstCreatedUsingValidation`) was refactored to compare `MoveSpecification` sets directly, which is the meaningful chess-correctness invariant; the `LegalMove`-set comparison was internal-consistency only.
- [x] **`Board.createPositionAfterMove` moved to `StaticPositionUtility`.** Was `public static` on `Board` — but the function is a pure transformation of an immutable `StaticPosition`; doesn't depend on any `Board` state and shouldn't live on the game class. New home was already the only external caller.
- [x] **`coding-conventions.md`: documented "Records carry data, not behavior".** The MVC-style separation: records are the M; computational and business logic operating on them lives in dedicated utility/service classes. Compact-constructor validation, `Comparable` when ordering is intrinsic, and language-provided `equals`/`hashCode`/`toString` remain explicitly allowed; domain-operation methods do not.
- [x] **README version mismatch fixed.** The Gradle snippet said `compile 'com.github.dlbbld:clean-chess:3.2'` while the Maven snippet correctly said `3.3.0`. Updated to `implementation 'com.github.dlbbld:clean-chess:3.3.0'` — version bumped, plus the deprecated `compile` configuration (removed in Gradle 7) replaced with the modern `implementation`.
- [x] **`Reporter` decoupled from stdout.** Added `calculateReportText(...)` — three overloads matching `printReport(...)`, returning the same human-readable report as a single `\n`-joined string. Lets non-CLI consumers (web responses, file writes, GUIs) use the report data without capturing stdout. Implementation refactored to share line-building logic between print and text variants. New smoke test covers both methods.
- [x] **`InvalidMoveException` brought into the project's exception hierarchy.** Was `extends RuntimeException` directly — bypassed both `ChessApiRuntimeException` and `UsageException`. Now `extends UsageException`, so consumers can `catch (UsageException e)` to handle all caller-fault cases uniformly (alongside the SAN/FEN/PGN validation exceptions).
- [x] **Thread-safety statement added.** Class-level Javadoc on `Board` says it's mutable and not thread-safe; warns about putting a `Board` in a hash-based collection. New §2.4 in `specification.md` covers the project-wide contract: records are immutable + thread-safe; static utility classes are stateless + thread-safe; parsers expose stateless static entry points.
- [x] **`CHANGELOG.md` created.** Starts with the upcoming release as `Unreleased`. Follows the [Keep a Changelog](https://keepachangelog.com/) convention. Pre-3.3 history remains in the git tag list; the changelog tracks releases going forward.
- [x] **`CONTRIBUTING.md` created.** Central "how to contribute" entry point linking `setup.md`, `coding-conventions.md`, `agents.md`, and `tasks.md`. Closes the "I want to help" affordance gap on the GitHub repo view.
- [x] **JAR `MANIFEST.MF` carries Implementation-Title/Version/Vendor.** Configured in `maven-jar-plugin`. Consumers can now call `Package.getImplementationVersion()` to discover the runtime version of clean-chess — useful for logging, error messages, version-awareness in downstream apps.

---

## Future release — Lenient SAN

Single feature, headline release.

### Lenient SAN parser/validator
- [ ] Audit what `SanValidation` and `Board.performMove(String)` accept/reject today
- [ ] Define diagnostic taxonomy: `OVERSPECIFIED_FILE_DISAMBIGUATION`, `OVERSPECIFIED_RANK_DISAMBIGUATION`, `OVERSPECIFIED_SQUARE_DISAMBIGUATION`, `MISSING_CHECK_SUFFIX`, `MISSING_CHECKMATE_SUFFIX`, `WRONG_CHECK_SUFFIX_FOR_CHECKMATE`, `LONG_ALGEBRAIC_NOTATION`, `ZERO_INSTEAD_OF_O_CASTLING`, `MISSING_PROMOTION_EQUALS`, `EXPLICIT_PAWN_LETTER`
- [ ] `LenientSanParserValidationResult` with forgiven-items channel (typed code + original token + canonical SAN)
- [ ] Public API: `LenientSanParser.parseText(String, Board)`, `validateText(String, Board)`, `LenientSanParserValidationException`
- [ ] `Board.performMoveLenient(String)` — opt-in at call site; `performMove(String)` stays strict
- [ ] Document in `specification.md` §3.3

### Wire LenientSanParser into LenientPgnParser
Depends on the previous task.
- [ ] Replace strict `SanValidation` call inside lenient PGN movetext path with `LenientSanParser`
- [ ] Plumb forgiven items into `LenientPgnParserValidationResult`
- [ ] Update lenient-PGN test fixtures (one fixture per forgiven code)
- [ ] Update README "Lenient PGN parser" section — new tolerances are a real selling point

## Future release — API surface

### Audit broader public API surface (residual from "strip demo/dev code")
Deferred. Do **after** the lenient-SAN release lands so files don't churn twice.
- [ ] Audit ~248 top-level public types under `src/main`
- [ ] Drop non-API types to package-private
- [ ] Decide stable public-API boundary

## Future release — Auto-CHA (DeepSquare moment)

### GPL v3 source-file headers

Add a short GPL preamble at the top of every source file, in the style CHA (D3-Chess) uses on its own files. The unwinnability release is the natural moment because the file headers are the strongest place to assert the CHA derivation that this release leans into.

Reference — CHA's header style:

```
/*
  Chess Unwinnability Analyzer, an implementation of a decision procedure for
  checking whether a certain player can deliver checkmate (i.e. win) in a given
  chess position.

  This software leverages Stockfish as a backend for chess-related functions.
  Stockfish is free software provided under the GNU General Public License
  (see <http://www.gnu.org/licenses/>) and so is this tool.
  The full source code of Stockfish can be found here:
  <https://github.com/official-stockfish/Stockfish>.

  Chess Unwinnability Analyzer is distributed in the hope that it will be
  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU GPL for more
  details.
*/
```

For clean-chess, adapt: short project description, copyright line, GPL v3 reference, credit to CHA as the derivation source for the unwinnability code, standard no-warranty boilerplate.

- [ ] Design the clean-chess header text (one paragraph + boilerplate)
- [ ] Apply to every `.java` file under `src/main/` and `src/test/` via a script
- [ ] One isolated commit (noisy diff, do not bundle with semantic changes)

### Auto-CHA after every move
- [ ] Per-move pipeline: invoke `isUnwinnableQuick` for both sides after every legal move; both unwinnable ⇒ `DEAD_POSITION` automatic termination
- [ ] Update `isAutomaticallyTerminated()` to include this case
- [ ] `BoardConfig` (record) with `autoChaEnabled` boolean; default `true` in production, `false` for tests and bulk PGN parsers
- [ ] Test factory/base class that constructs disabled boards
- [ ] `StrictPgnParser` and `LenientPgnParser` construct boards with auto-CHA disabled by default
- [ ] README: remove the "CHA is not run automatically after every move" note
- [ ] `specification.md` §3.1 termination table: add "Dead position (CHA quick) — automatic"
- [ ] `specification.md` §3.2: invert the framing — quick is now per-move automatic; full remains opt-in
- [ ] Performance check: with disable flag on, suite within ~10% of current
- [ ] Targeted regression tests on positions that should auto-terminate

---

## Future release — python-chess as primary cross-validation reference

This is **discussion + design first**, implementation later. The project currently uses Carlos's `chesslib` (`LibraryCarlosBoard`) as a cross-validation reference, with limitations: cannot import PGN from a non-initial position, smaller test surface, less actively maintained. python-chess is the de-facto reference in chess software, actively maintained, and handles arbitrary positions.

### Pattern recommendation — generation-based, not live invocation

- A Python script using python-chess generates expected outputs (legal moves, FEN, SAN, LAN, repetition counts, halfmove clock, dead-position verdicts) for a battery of fixtures, writes to a fixed file path.
- Java tests read the file and compare to clean-chess output.
- The Python script runs only when fixtures are added or regenerated, **not** during `mvn test`.
- Chess outputs are deterministic per input; cached reference data doesn't go stale.

`GeneratePythonTestCases.java` already exists — that's the foundation. Audit it and extend.

### Discussion items to settle before coding

- [ ] Inventory exactly what python-chess will be used as reference for: legal-move generation, SAN/LAN, FEN, repetition counts, fifty-move clock, threefold/fivefold, dead-position (does python-chess support this directly or via heuristic?), CHA-style unwinnability (it doesn't — that stays unique to clean-chess).
- [ ] Decide: gradual migration (both chesslib and python-chess as references during transition) or hard cutover (drop chesslib at the same time).
- [ ] Decide: drop `LibraryCarlosBoard` entirely once python-chess covers its usage, or keep it for rule-engine-style cross-checks.
- [ ] Document the toolchain requirement: contributors need Python 3 + `pip install chess` (the package). Goes in `setup.md`.
- [ ] Plan the regeneration workflow: how is "I added a fixture; now regenerate the python-chess-expected outputs" triggered cleanly? Maven goal? Script? Make target?

### Implementation tasks (after the discussion)

- [ ] Decide and document the file format for stored expected outputs (JSON? line-based?)
- [ ] Refactor `GeneratePythonTestCases` (or replace) to produce the agreed format
- [ ] Migrate at least one cross-validation test from chesslib to python-chess as a proof-of-concept
- [ ] Phase out chesslib usage if the discussion lands there

---

## Future release — publish to Maven Central

The capstone release. Publish to Central only when the library has stabilised — every prior release done, identity questions settled, and any tasks that surface during the prerequisite work itself addressed first. Maven Central artifacts are immutable: once published, an artifactId+version pair lives forever in the public record. The bar for moving from JitPack to Central is therefore "we are confident this artifact represents the project well, indefinitely."

### Prerequisites — must be true before any Central work begins
- [ ] All earlier releases completed (cleanup follow-through, lenient SAN, API-surface audit, auto-CHA, python-chess cross-validation)
- [ ] Rename decision resolved — clean-chess → DeepSquare or final name. Once published, the artifactId is permanent
- [ ] Every task that surfaces during the prerequisite releases has been addressed (re-evaluate this list at the moment of starting; the bar is "library is mature")

### Sonatype Central Portal setup
- [ ] Create Sonatype Central account at https://central.sonatype.com, sign in via GitHub
- [ ] Verify the `io.github.dlbbld` namespace (auto-verified for GitHub-signed-in users — no domain needed)
- [ ] Generate a GPG key, publish it to a public keyserver (e.g. `keyserver.ubuntu.com`), record the keyID
- [ ] Configure `~/.m2/settings.xml` with Sonatype Portal credentials and GPG passphrase

### `pom.xml` — Central-required metadata
- [ ] `<groupId>` → `io.github.dlbbld` (currently `com.github.dlbbld`, the JitPack convention)
- [ ] `<version>` → strict semver (`4.x` → `4.x.0`)
- [ ] Add `<name>`, `<description>`, `<url>` (link to GitHub repo)
- [ ] Add `<licenses>` block (GPL v3, with full URL)
- [ ] Add `<developers>` block
- [ ] Add `<scm>` block (`connection`, `developerConnection`, `url`)

### `pom.xml` — required plugins
- [ ] `central-publishing-maven-plugin` (the new Sonatype Portal plugin — *not* the deprecated `nexus-staging-maven-plugin` / OSSRH that older tutorials still document)
- [ ] `maven-gpg-plugin` for artifact signing
- [ ] `maven-javadoc-plugin` to produce a javadoc jar (`maven-source-plugin` is already present)

### JAR-content audit at publish time
Whatever ships in the first Central artifact is in the public record forever. Re-audit at publish time.

- [ ] Re-audit `src/main/resources` end-to-end: anything developer-facing, test-only, or environment-specific should not ship
- [ ] Re-audit `src/main/java` for any utility classes that should have been package-private rather than public (folds the residual API-surface work in if not already done by the API-surface release)
- [ ] (The test-fixture message keys are handled in the cleanup follow-through release; this audit is the safety net for anything similar that surfaces between now and publish)

### First publish + workflow
- [ ] Update README: drop the JitPack `<repositories>` block, leave only the plain Maven dependency snippet (no extra repository declarations needed for Central)
- [ ] Drop the JitPack URL and any related framing from README and other docs
- [ ] First publish via the Central Portal — staged release, manual approval the first time
- [ ] Verify the artifact appears at https://central.sonatype.com/artifact/io.github.dlbbld/...
- [ ] Document the per-release workflow (version bump → tag → `mvn deploy` → Portal release) in `setup.md` under a new "Releasing" section, or in a dedicated `release.md`

### Post-publish
- [ ] Decide whether JitPack stays available in parallel (free, harmless) or should be deprecated by removing the JitPack publish hook
- [ ] (Optional) Add a Maven Central status badge to the README

---

## Backlog — captured but unscheduled

Items here are not assigned to any release. Captured so they don't get lost; revisit if/when scope or motivation aligns.

### FEN parser tier audit and rename
Three FEN parsers exist: `FenParserRaw`, `FenParserAdvanced`, `FenParserAdvancedFurther`. The third is largely unused. Boundaries between the three are unclear and the docs overclaim what each tier proves (see "FEN-validation documentation overclaims" below).
- [ ] Audit each tier — what each parser actually validates vs what its docs claim
- [ ] Document each tier's contract precisely in `specification.md`
- [ ] Decide: keep three tiers, collapse `AdvancedFurther` into `Advanced`, or drop it

### Move FEN-letter parsing off `Side` and `BasicChessUtility` onto the FEN parser
Layering violation surfaced by the API-surface audit. `Side.calculate(String)` parses the FEN single-letter side indicator (`"w"` / `"b"`) into a `Side` enum value — but FEN-syntax knowledge does not belong on the chess-rules `Side` enum. The same parsing also appears, redundantly, on `BasicChessUtility.calculateSideHavingMoveForSide(String)`. Both should be deleted; their logic belongs in `FenParserRaw` / `FenParserAdvanced` (wherever the rest of FEN field parsing lives).

While in `FenParserAdvanced`, `validateHavingMove` currently checks for `w` or `b` via a regular expression — overkill for a two-character alphabet. A direct equality check that throws the advanced FEN validation exception on mismatch is the natural shape.

- [ ] Move FEN-letter → `Side` parsing into the FEN parser layer; pick the right home (likely `FenParserRaw` since this is purely lexical)
- [ ] Delete `Side.calculate(String)` from the `Side` enum
- [ ] Delete `BasicChessUtility.calculateSideHavingMoveForSide(String)` (duplicate of the above)
- [ ] Replace the regex in `FenParserAdvanced.validateHavingMove` with a direct equality check + advanced-FEN-validation throw
- [ ] Update any test callers of the removed methods to go through the FEN parser instead

### Replace `UciValidateHelper` enum with computed lookup
Auto-generated 1984-line enum, ~111 KB class file (~50% of the JAR's bytecode mass). Used as a list-of-strings rather than as an enum. A generation loop in the static init of its only caller would replace 1984 lines of source with ~12.
- [ ] Replace the enum with a `Set<String>` (or similar) computed via a constructor loop
- [ ] Verify JAR shrinks by ~50%

### FEN-validation documentation overclaims
`fen/package-info.java` and `Board.java` (class-level + constructor docs) say advanced FEN rejects positions "no real game could reach." The code does strong structural and rule-consistency checks but does not prove full game reachability. Also: package text says halfmove clock "at or above 150" while code accepts exactly 150.
- [ ] Soften prose to "advanced structural and rule-consistency validation"
- [ ] List exactly what is enforced; drop the unsubstantiated reachability claim
- [ ] Fix the "at or above" off-by-one

### Broken Javadoc link in `fen/package-info.java`
Links to `com.dlb.chess.fen.FenParser` which does not exist. `mvn javadoc:jar` succeeds only because `<doclint>none</doclint>` in pom.xml; the warning is still emitted. Real target is `FenParserRaw` + `FenParserAdvanced`.
- [ ] Fix the link

### CHA / unwinnability wording teaches the wrong mental model
README and `unwinnability/package-info.java` use "worst play / worst-case play by the opponent." In game-theory English, "worst-case opponent" reads like *best defensive play*, but CHA / winnability is the opposite: whether any legal continuation can lead to mate (cooperative / helpmate-style existence). Worth fixing because it is the flagship concept.
- [ ] Rewrite around "no legal sequence exists, even with the opponent's cooperation" or "no theoretical mating sequence exists under any legal continuation"

### README inconsistency — CHA full "100% accurate" vs `UNDETERMINED`
README says CHA full is "slower but 100% accurate," then a few lines down documents the `UNDETERMINED` outcome (and again later in the doc).
- [ ] Reword to "complete when it returns WINNABLE / UNWINNABLE; bounded search may return UNDETERMINED"

### Remove the `EnPassantCaptureRuleThreefold` dual-path
The `EnPassantCaptureRuleThreefold` enum (`DO_IGNORE` / `DO_NOT_IGNORE`) drives a second, parallel repetition-tracking code path that ignores en passant availability when comparing dynamic positions. It was added as a research tool: in FIDE rules, two positions with the same piece arrangement but different en-passant availability are *not* the same position for threefold-repetition purposes — but chess.com (and other platforms) implemented the lazy "visual repetition" rule and don't check en-passant availability. The dual path made it easy to find PGN games where the two interpretations diverge, producing examples to demonstrate the platform-side bug.

That research goal is no longer load-bearing for the library. The dual code path costs ongoing complexity (two flavours of `equals`-like comparison, two parallel data structures on `Report`, two flavours of every repetition test fixture) for a feature whose audience was one researcher. As clean-chess matures, the library should implement the FIDE rule cleanly and only.

- [ ] Drop the `EnPassantCaptureRuleThreefold` enum
- [ ] Remove `Report.repetitionListListInitialEnPassantCapture()` and any other dual-path fields/methods on `Report`
- [ ] Collapse `RepetitionUtility.getCountRepetition` and the surrounding repetition-tracking machinery to the single FIDE-correct path
- [ ] Drop the dual-path test fixtures, reports, and representation code in `com.dlb.chess.test.report.representation.*`
- [ ] Strip the explanatory paragraph in `RepetitionUtility`'s class-level Javadoc about "two different ways" of counting repetition
- [ ] Verify `git grep -i "ignoring en passant"` (or similar phrasing) returns zero hits afterwards

### Replace `EnumConstants` constant interface
`com.dlb.chess.common.constants.EnumConstants` is a `public interface` whose only purpose is to expose ~90 `public static final` aliases for `Square.*`, `Side.*`, `Piece.*`, `PieceType.*`, `Rank.*`, `File.*` so implementing classes inherit them unqualified. This is the classic "constant interface" anti-pattern (Effective Java item 22): interfaces should describe a contract/behavior, not be a convenience-inheritance vehicle for constants. The mechanism reads as beginner Java and leaks an internal vocabulary choice into the public type surface — `ChessBoard extends EnumConstants` is the clearest symptom (the chess contract has nothing to do with how implementers prefer to spell `Square.E4`). Used by 43 files under `src/main` plus tests.

Replacement strategy options, depending on intended audience:
- public-API constants: `public final class EnumConstants` with `public static final` fields and a private constructor (callers `import static`)
- internal-only: make package-private and split closer to where they belong (domain-grouped, e.g. `BoardSquares`, `PieceLetters`)
- derived enum collections: prefer local `EnumSet` / `ImmutableSet` factories in the utility that needs them, or dedicated package-private constants classes by domain

- [ ] Pick a replacement strategy (default lean: package-private utility class with `import static`, since the constants are internal vocabulary and the audit reduces public surface anyway)
- [ ] Drop `extends EnumConstants` from `ChessBoard` regardless of strategy — the interface should not carry constants
- [ ] Convert the 43 src/main call sites + tests to static imports
- [ ] Folds naturally into the API-surface reduction release; treat as a cleanup target there

### Profound-level square geometry — promote single-step calculations to lookup tables
The codebase already uses lookup tables for the geometry that matters — `OrthogonalRange`, `DiagonalRange`, `KnightEmptyBoardSquares`, `BishopEmptyBoardSquares`, `RookEmptyBoardSquares`, `DiagonalLineUtility`. Single-step instance-style methods on `Square` (`calculateLeftSquare`, `calculateLeftDiagonalSquare`, `calculateAheadSquare`, etc.) and `File` / `Rank` are the calculate-on-demand holdouts in an otherwise table-based codebase. The "calculate" form has a deeper testing problem: any independent test implementation faces a definitional regress ("left of E4 from White is D4 — but what does *left* mean if not what `calculateLeft` returns?"), which is how `Square.calculateIsLeftDiagonalSquare` ended up as a tautological method that tested itself against itself.

The fix is to promote these single-step relationships to data:
- `Map<Square, Map<Side, Square>>` (or `EnumMap<Square, EnumMap<Side, Square>>`) constants for left, right, ahead, behind, left-diagonal, right-diagonal
- The "has" predicates collapse to `map.containsKey(...)` or `value != NONE`
- The map is built once at class load; tests verify the table by inspection or via python-chess cross-reference (folds into the existing python-chess backlog)
- The bug surface shrinks to one place: the table-builder

- [ ] Inventory single-step `calculate*` methods on `Square` / `File` / `Rank` that are pure square→square (or square+side→square) lookups
- [ ] Replace each with a precomputed `EnumMap` constant + a thin accessor
- [ ] Generate the expected tables either by hand-curation or by python-chess cross-reference (latter is preferred once the python-chess infrastructure lands)
- [ ] Drop the algorithm-vs-algorithm test patterns; tests become "look up in production table, compare to reference table"
- [ ] Folds naturally into the DeepSquare rename moment — this kind of foundational rigor is exactly what the rename signals
- [ ] **Companion concern — bloated lookup-table implementations.** `PawnDiagonalSquares` is 826 lines of generated code (per-square `addWhiteA1`, `addWhiteA2`, … methods) to express what is conceptually "for each pawn from-square, the 0–2 diagonal capture squares." The same shape recurs across the `com.dlb.chess.squares.emptyboard.*` family (`Knight`, `Bishop`, `Rook`, `Queen`, `King`, `PawnOneAdvance`, `PawnTwoAdvance`, `PawnAnyAdvance`). These tables are correctly precomputed, but their implementation should be a single `static {}` initializer that loops over `Square.REAL` and computes each entry via simple file/rank arithmetic — not hundreds of method-per-square stubs. Replacing them collapses ~thousand-line files to dozens of lines while preserving the precomputed-table API. Same theme as the main bullet: keep the lookup, sane the implementation.

### Records carry data, not behavior — sweep for violations
The project rule (documented in `coding-conventions.md`): records carry data; domain logic that operates on them lives in dedicated utility / service classes. Permitted on a record: compact-constructor validation, `Comparable` when ordering is intrinsic, and language-provided `equals` / `hashCode` / `toString`. Domain-operation methods are not.

Surfaced by the unused-code-detector pass on `StaticPosition`: the record carries multiple non-data methods — `createChangedPosition` (three overloads), `isPawn`, `isOwnPawn`, `isOpponentPawn`, `isOwnKing`, `isOpponentKing`, almost certainly more. Some have only test callers (suggesting test scaffolding), some have production callers, one (`isOwnKing`) has zero callers anywhere.

- [ ] Catalog every non-permitted member on `StaticPosition` and assign a disposition per member: delete (no callers anywhere), move to a test-side helper that **takes** a `StaticPosition` rather than duplicating it (test-only callers), or move to a `StaticPositionUtility` (production callers).
- [ ] Sweep every record under `src/main/java` for the same pattern. Records to check include at least `Fen`, `Tag`, `PgnFile`, `LegalMove`, `MoveSpecification`, `StaticPosition`, plus any other top-level `record` declarations under `src/main`.
- [ ] Apply the dispositions; verify only the permitted member shapes remain on each record.
- [ ] Naturally folds into the API-surface reduction release, since most "move to utility" relocations open the door to making the utility itself package-private.

### Rename `NonNullWrapperCommon` to `Nulls`
The class is used pervasively (every JDT-null-safe wrapper for JDK calls goes through it), and `NonNullWrapperCommon` is too long for something so frequent. `Nulls` is short, pronounceable, says the domain (this utility exists because of nullness handling), and discoverable in the IDE. Rejected alternatives: `NNVC` (cryptic codeword), `Safe` / `Checked` (vague), `NonNulls` (awkward plural).
- [ ] Rename class `NonNullWrapperCommon` → `Nulls`; update all call sites (uses appear in most files in the project — bulk rename)
- [ ] Verify the methods are still all about nullness handling; if any aren't, reconsider the name

---

## Done

- [x] **README.md cleanup pass** (Java 17)
- [x] **Strip demo/dev code from src/main**
  - `readme/ReadMeForRepository` deleted
  - `trainer/SanTrainerServer` deleted
  - `Message.main()` removed
  - Broader 248-public-types audit deferred (see *Audit broader public API surface*, above)
- [x] **Public API immutability — done deeper than originally scoped**
  - Originally just "defensive copies"; ended up making the immutability visible at the type level via Guava `ImmutableList<X>` / `ImmutableSet<X>`.
  - `ChessBoard` interface: all 8 list/set accessors return `ImmutableList<X>` or `ImmutableSet<X>`.
  - `Board.legalMoveSetList: List<ImmutableSet<LegalMove>>` — inner sets sealed at write time via `AbstractLegalMoves.calculateLegalMoves` boundary.
  - `Board` persistent-field accessors wrap with `NonNullWrapperCommon.copyOfList` per call (Approach A — clarity over micro-optimisation).
  - `Board` freshly-built accessors wrap once at end of method.
  - `LegalMoveCalculation` record's two move-set components are `ImmutableSet<X>`.
  - `PgnFile` record: `tagList`, `halfMoveList` are `ImmutableList<X>` with compact-constructor defensive copy.
  - `LibraryCarlosBoard` (test impl) mirrors the new return types.
- [x] **Collapse AbstractBoard → default methods on ChessBoard interface**
  - All 12 stateless derivations from `AbstractBoard` moved to `ChessBoard` as `default` methods (`getCastlingRight(Side)`, `canClaimFiftyMoveRule`, `canClaimThreefoldRepetitionRule`, CHA quartet, `calculateInsufficientMaterial`, `isGameEnd`, `getFullMoveNumberForNextHalfMove`, `getLegalMovesSan`, `getLegalMovesUci`).
  - `isGameDraw` kept as a private interface method (Java 9+ feature).
  - `ChessBoard extends EnumConstants` so existing implementations keep their unqualified enum constants without breakage.
  - `Board` and `LibraryCarlosBoard` now `implements ChessBoard` directly.
  - `AbstractBoard.java` deleted.
  - Net effect: no public abstract class on the API surface; CHA methods appear directly in IDE completion on `Board`; cross-validation contract is the interface (the right level for it).
- [x] **Eclipse compiler warnings & infos cleanup (out-of-band)**
  - Project now compiles warning-free under JDT settings.
  - Includes: `unexpectedValidationErrorMessage` `@NonNull` annotation, three `pcve.getMessage()` / `e.getMessage()` `@SuppressWarnings("null")` extractions, `boxing` no-op suppression removed, log4j2.xml schema declaration + DTD download fix, class-level `@SuppressWarnings("null")` for JUnit Assertions / JDK BiFunction in 4 test classes.
