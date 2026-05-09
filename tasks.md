# Tasks

Order within each section is the source of truth. Completed tasks move to **Done** at the bottom.

---

## Current release — cleanup follow-through

Theme: doc correctness, dead/personal code purge, library packaging hygiene, naming follow-through from the rename in the previous release, and one design-consistency fix (`isGameEnd` vs CHA opt-in). No new features.

### README correctness
- [ ] Fix the three headline code samples that no longer compile: `Analyzer.printAnalysis(pgn)` → `Reporter.printReport(pgn)` (`README.md:126`, `:160`, `:205`)
- [ ] Fix the "Create PGN for game" example: `System.out.println(pgnFile)` does not produce the formatted PGN shown in the comment (`PgnFile` is a record with no `toString()` override). Replace with `System.out.println(PgnCreate.createPgnFileString(pgnFile))`
- [ ] Update "Not supported" section: PGN move suffix annotations *are* parsed and exported (see `StrictPgnParser`, `LenientPgnParser`, `PgnCreate`), and UTF-8 BOM *is* stripped by the lenient parser. Both bullets need to either go or be re-stated as the actual current limits
- [ ] Split audiences explicitly under "Building/Installing": one paragraph for *using clean-chess as a dependency* (no Eclipse, just Maven/Gradle coords), one for *contributing* (link to `setup.md`). Today the section reads as if every consumer needs Eclipse

### specification.md correctness
- [ ] §2.2 overclaims compact-constructor validation — `Fen`, `Tag`, `PgnHalfMove` have no validation; `PgnFile` only copies lists. Either soften the claim, or add real boundary validation to those records (preferable: validate, since "errors at the construction boundary" is a load-bearing project value)
- [ ] §4 architecture table is missing 6 top-level packages: `distance`, `exceptions`, `internationalization` (or its successor — see below), `range`, `squares`, `utility`; plus 2 pgn subpackages: `pgn.diagnostic` and `pgn.writer`. Add rows or explicitly note them as utility/internal
- [ ] If `isGameEnd` semantics change (next subsection), update the relevant termination wording

### messages.properties cleanup
The buggy `analysis.board.score.blackWin=0-0` is gone (commit `3097c89c` — the entire `analysis.board.*` block was DGT/trainer-only and unused; removal verified clean by audit).

- [ ] Rename remaining `analysis.*` keys to `report.*` to follow through on the package rename from the previous release. The Java side is renamed; the surviving `analysis.repetition.*` and `analysis.yawnmove.*` keys still say `analysis`
- [ ] Fix the comment typo `##analzyer` → `##analyzer` at line 251

### TestMessage fixture cleanup
The 12 `test*` / `test.message.*` keys at the bottom of `messages.properties` plus `TestMessage.java` itself are mostly testing JDK behavior (`ResourceBundle.getString`, `MessageFormat.format`) rather than library code. The one custom layer worth covering is `Message.normalizeSpace(...)`. The current shape is "I was getting comfortable with the API" code that mature projects clean up.

- [ ] Drop the basic key-lookup tests (`testBasic`) — they exercise `ResourceBundle.getString`, which the JDK has tested
- [ ] Drop the placeholder substitution tests (`testWithoutPlaceholder`, `testWithOnePlaceholder`, `testWithTwoPlaceholders`) — they exercise `MessageFormat.format`, also JDK-tested
- [ ] Keep one or two whitespace tests covering `NonNullWrapperCommon.normalizeSpace` (the genuinely custom behavior), but drive them with synthetic strings — no property-file fixtures needed
- [ ] Delete the 12 `test*` / `test.message.*` keys + the `#testing` block from `messages.properties` once the tests no longer need them
- [ ] Net effect: the production JAR no longer ships test fixture data; `TestMessage.java` exists only to cover code we actually wrote

### Remove all DGT-derived material (paid-work content)
The DGT/trainer code originates from paid work and should not ship in this open-source library. The constants in `src/main` are already on the dead-code list above; the test material below is the larger part.

- [ ] Remove `DGT_MY_BLUETOOTH_BOARD_ID = 23944`, `DGT_MY_USB_BOARD_ID = 43462`, `DGT_ACTIVE_BOARD_ID`, and the Google text-to-speech credentials comment from `ConfigurationConstants.java`
- [ ] Drop `DGT_LIVE_CHESS` and `DGT_CENTAUR` from [`PgnTest.java:89-90`](src/test/java/com/dlb/chess/test/pgntest/enums/PgnTest.java:89)
- [ ] Drop `createTestCasesDgtCentaur()` and `createTestCasesDgtLiveChess()` plus their dispatch entries from [`PgnExpectedValue.java`](src/test/java/com/dlb/chess/test/pgntest/PgnExpectedValue.java) (8 + 1 fixture entries, ~50 lines around 3472–3515)
- [ ] Drop the `dgt/liveChess` block in [`TestLegacyPgnParsePlaysBeyondAudit.java:227`](src/test/java/com/dlb/chess/test/pgn/parser/beyond/TestLegacyPgnParsePlaysBeyondAudit.java:227)
- [ ] Delete fixture directories `src/test/resources/pgn/review/dgt/` (centaur + liveChess subdirs, ~9 PGNs) and `src/test/resources/pgnParser/legacy/common/beyond/dgt/liveChess/` (~1 PGN)
- [ ] Verify `git grep -i dgt` returns zero hits afterwards

### Rename `pgn/cua` → `pgn/cha` (correct CHA abbreviation)
The Chess Unwinnability Analyzer is abbreviated **CHA** by Miguel Ambrona (matches the repo name `D3-Chess`, the binary, and all code identifiers in his project). The test fixture tree under [`src/test/resources/pgn/cua/`](src/test/resources/pgn/cua) uses the wrong abbreviation; the folder name has been a long-standing source of confusion when navigating between this project and CHA's source.

- [ ] Rename `src/test/resources/pgn/cua/` → `src/test/resources/pgn/cha/` (preserve all subdirs: `pawnWall`, `unfair/ambrona`, `unfair/depthThree`, `unfair/lichess/examples`, `unfair/lichess/helpmate`, `unfair/notQuick`)
- [ ] Update the six `cua/...` path strings in [`PgnTest.java:92-98`](src/test/java/com/dlb/chess/test/pgntest/enums/PgnTest.java:92) to `cha/...`
- [ ] Verify `git grep -i "cua"` is clean (no other code or doc references)
- [ ] Use `git mv` so history is preserved on the directory rename

### Dead-code & personal-data purge
- [ ] Delete `MultiplePgnSplitUtility.java` (or relocate to `src/test/java`). It has a `main()`, a hardcoded path component `otherdb/mb-3.45/mb-3.45.pgn`, and an 8-step ChessBase-recipe in comments. No call sites — slipped through the previous "strip demo/dev code" pass
- [ ] `KnightDistance.java` — the `// This code contributed by Rajput-Ji` attribution is opaque (looks like GeeksforGeeks origin). Either cite the source URL + verify GPL-v3 compatibility, or rewrite in project style. Drop the `public static void main()` regardless
- [ ] Remove the four `private static final boolean IS_DEBUG = false;` dead branches in `unwinnability/findhelpmate/*` (`AbstractFindHelpmate`, `FindHelpmateExhaust`, `FindHelpMateInterrupt`, `mobility/Mobility`). They write `fenListMine.txt` to `TEMP_FOLDER_PATH` when toggled — either delete or replace with proper logger calls
- [ ] Move `ConfigurationConstants.PROJECT_ROOT_FOLDER_PATH` and `BasicUtility.readProjectFolderPath()` to a test-only utility. They are used exclusively by tests but currently execute on every consumer's first class-load of `ConfigurationConstants`
- [ ] `internationalization.Message` is a `class` with only static methods and no instances — add a private constructor (matches `BasicUtility`, `Reporter`, etc.)

### Library packaging hygiene (do not pollute consumer classpaths)
- [ ] Move `src/main/resources/log4j2.xml` and `log4j-config-2.xsd` to `src/test/resources`. Today they ship in the published JAR and log4j2 picks them up on consuming applications' classpaths
- [ ] Drop `log4j-core` from runtime dependencies in `pom.xml` — keep only `log4j-api` for the library, let consumers pick the backend. `log4j-core` can stay at `<scope>test</scope>` for the test suite
- [ ] Verify with a fresh consumer project that no `log4j2.xml` or `log4j-core` arrives transitively

### isGameEnd vs CHA opt-in design (P1 from review)
[`specification.md:84`](specification.md:84) states CHA is opt-in and not part of the per-move status path, but [`ChessBoard.isGameEnd()`](src/main/java/com/dlb/chess/common/interfaces/ChessBoard.java:159) calls `isDeadPositionQuick()`. Meanwhile [`BasicChessUtility.calculateGameStatus()`](src/main/java/com/dlb/chess/common/utility/BasicChessUtility.java:107) — which move validation and PGN export consult — does *not* include quick CHA. So a caller can see "game ended" from one public API while validation/export says "ongoing".

- [ ] Decide the canonical policy: either (a) remove `isDeadPositionQuick()` from `isGameEnd()` so all paths agree CHA is opt-in, or (b) make `BasicChessUtility.calculateGameStatus()` include it too — but then validation/export would auto-terminate on quick CHA, which contradicts the spec rationale
- [ ] (Recommended: option a — keeps the per-move surface CHA-free, defers auto-CHA to the dedicated future release)
- [ ] If the surface changes meaningfully, update specification.md §3.1 and any package-info.java that describes termination

### FileUtility error handling (P2 from review)
[`FileUtility.writeFile`](src/main/java/com/dlb/chess/common/utility/FileUtility.java:98) catches `IOException` and only prints the stack trace at line 107. [`PgnWriter.writePgnFile`](src/main/java/com/dlb/chess/pgn/writer/PgnWriter.java:24) returns `void`, so callers can believe a PGN was written when it was not.

- [ ] `FileUtility.writeFile` must throw `FileSystemAccessException` on `IOException`, matching the read/append/delete methods
- [ ] Audit every method in `FileUtility` for the same pattern; align all error paths
- [ ] Confirm `PgnWriter.writePgnFile` propagates the failure to its caller

### "Yawn move" → standard chess terminology
The codebase uses `YawnMoveUtility`, `YawnHalfMove`, `YawnPrint`, `YawnRepresentation`, `analysis.yawnmove.*` keys. Neither FIDE, the chess community, the README, nor `specification.md` use this term. The user-facing output already says "Sequences without capture and pawn move…", so the term exists purely as private vocabulary.

- [ ] Decide canonical name: `HalfmoveClock*` (matches FIDE), `NoProgress*`, or `FiftyMove*`
- [ ] Rename Java identifiers across `src/main` and `src/test`
- [ ] Rename matching keys in `messages.properties` (rolls into the `analysis.*` → `report.*` rename above)
- [ ] Update specification.md if it ever uses the term

### Eclipse fresh-checkout fidelity
The previous release set the theme "fresh checkout works without manual setup steps". These two contradict it.

- [ ] `.classpath` line 29 still references `JavaSE-26`. Update to `JavaSE-17` to match `pom.xml` and `setup.md`. Without this, fresh Eclipse checkout shows "JavaSE-26 not available" until the user runs Maven > Update Project
- [ ] `.project` declares the `ch.acanda.eclipse.pmd.builder.PMDBuilder` builder and a PMD nature, plus `.eclipse-pmd` exists at the repo root. `setup.md` does not mention installing eclipse-pmd. Recommend: remove the PMD builder/nature from `.project` and delete `.eclipse-pmd` (Checkstyle is sufficient). Alternative: document the PMD plug-in install in `setup.md`

### Javadoc on the public API
- [ ] Class-level Javadoc on `Board` and `Reporter` (the two headline classes; today both have none)
- [ ] Method-level Javadoc on `Board`'s public methods at minimum: the three constructors, `performMove(String)`, `performMove(MoveSpecification)`, `performMoves(String...)`, `unperformMove`, `isCheckmate`, `isStalemate`, `isThreefoldRepetition`, `isFiftyMoveRule`, `isUnwinnableQuick`, `isUnwinnableFull`, `isDeadPositionQuick`, `isDeadPositionFull`. One sentence each is enough
- [ ] Package-level Javadoc on the public-facing packages (`pgn.parser`, `pgn.create`, `unwinnability`, `report`, `san`, `fen`, `model`, `enums`). Model after [`board/package-info.java`](src/main/java/com/dlb/chess/board/package-info.java) — that one is excellent
- [ ] Configure `maven-javadoc-plugin` in `pom.xml` so JitPack ships a `-javadoc.jar`

### Externalized-messages package — narrow fixes (not a redesign)
The `messages.properties` mechanism itself is good engineering — externalization buys terminology-consistency review and translation-readiness independent of whether translation ever happens. Two narrow fixes, neither requiring a redesign:

- [ ] **Package name.** `internationalization` overstates what ships. The package contains externalized messages; it does not contain locales. Rename to `messages` (or `i18n`). The future-translation argument fits a `messages` package fine — translators just add `messages_de.properties` regardless of what the package is called
- [ ] **`ConfigurationConstants.LOCALE = Locale.US` is a trap** for the day someone adds a locale. Switch to `Locale.ROOT` (semantically "the default bundle") or `Locale.getDefault()` (respects JVM/user environment). Today the choice is invisible because no message uses locale-sensitive `MessageFormat` constructs (no `{0,number}`, no `{1,date}`); the hardcoding becomes load-bearing the moment any message does

### Smaller items
- [ ] `tasks.md` references commit hashes (`ef8de9c`, `7ac91e4`, `c104100`, …) in the **Done** section that won't survive a squash-merge into `main`. Either strip the hashes when an item moves to Done (it's the *fact* that's load-bearing, not the SHA), or note that tasks.md is dev scaffolding and not a permanent record

The Maven Central / `groupId` migration has its own dedicated release at the bottom of this file — see *Future release — publish to Maven Central*.

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

## Done

- [x] **README.md cleanup pass** (`c104100`, `21499ff` Java 17)
- [x] **Strip demo/dev code from src/main** (`aa0225e`)
  - `readme/ReadMeForRepository` deleted
  - `trainer/SanTrainerServer` deleted
  - `Message.main()` removed
  - Broader 248-public-types audit deferred (see *Audit broader public API surface*, above)
- [x] **Public API immutability — done deeper than originally scoped** (`55ee163`, `7707c75`, `f867357`)
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
- [x] **Eclipse compiler warnings & infos cleanup (out-of-band)** (`997f51c`, `142e19f`, `305bff5`, `7e85eb6`, `06ca493`, `498b300`)
  - Project now compiles warning-free under JDT settings.
  - Includes: `unexpectedValidationErrorMessage` `@NonNull` annotation, three `pcve.getMessage()` / `e.getMessage()` `@SuppressWarnings("null")` extractions, `boxing` no-op suppression removed, log4j2.xml schema declaration + DTD download fix, class-level `@SuppressWarnings("null")` for JUnit Assertions / JDK BiFunction in 4 test classes.
