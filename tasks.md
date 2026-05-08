# Tasks

Master list. Each task keeps its original number for stable referencing. Completed tasks move to **Done** at the bottom.

---

## Current release — project organization & big-picture cleanup

Theme: documentation, obvious issues, major bugs. No new features.

### 2. pom.xml / build configuration
- [x] Drop `maven.compiler.release` 26 → 17
- [ ] Reconcile `groupId` between `pom.xml` and `README.md`
- [ ] Move `log4j-core` to `<scope>runtime</scope>` (or remove); keep only `log4j-api` as compile dep
- [x] Remove file appender from `src/main/resources/log4j2.xml` — library no longer writes to a temp file at runtime (commit `612a93b`; dangling `AppenderRef` references cleaned up afterwards). Console-only config retained as a safe default.

### 5. Public API discoverability & naming
- [x] `isSeventyFiftyMove()` → `isSeventyFiveMove()` (commit `eee221e`)
- [x] ~~Pull CHA methods down from `AbstractBoard` to `Board`~~ — superseded by Task 16 (default methods on `ChessBoard`); CHA methods now appear directly on the interface
- [x] Rename `analysis` → `report` (commit `7ac91e4`): `Analyzer` → `Reporter`, `AnalyzerPrint` → `ReportPrint`, `Analysis` (record) → `Report`. The remaining `analyze` package now differs by more than one letter.
- [ ] Collapse `Reporter extends ReportPrint` into one `final` class — renamed but the inheritance is still there (`Reporter.java:22`); the `printAnalysis(...)` static methods are pure delegations to `ReportPrint`'s package-private statics
- [ ] `ChessRuleAnalyzer` — drop `abstract`, make `final` with private constructor

### 6. Release-gate test runs
- [ ] Maven profile `-Pfull` enabling the heavy excluded suites (currently default-off via `RestrictTestConstants`)
- [ ] Document the requirement (release checklist note) **or** add a CI workflow that runs `-Pfull` on tag push

### 7. specification.md polish
- [ ] Soften "no nulls" claim (§2.2)
- [ ] Soften "the only published algorithm" about CHA (§1)

### 8. Eclipse / Checkstyle configuration in repo
- [ ] Export Checkstyle XML rule file to repo (currently `internal_config_*.xml` in plugin storage)
- [ ] Update `.checkstyle` to reference the in-repo file as `type="project"`
- [ ] Check in `.settings/org.eclipse.jdt.core.prefs` (compiler warnings)
- [ ] Check in formatter / Save Actions / cleanup-on-save profile (investigate which can be project-scoped)
- [ ] Update `coding-conventions.md` — remove "Setup for contributors — known gap" section
- [ ] Verify on a fresh checkout that the rule sets activate without manual file copies

### 12. Create setup.md
- [ ] Document project setup (currently kept separately, outside the repo)
- [ ] Cover: required JDK, required Eclipse plugins, project import steps, anything that has to be done by hand
- [ ] Cross-link from `README.md` and `coding-conventions.md`

### 13. Standardize markdown filename casing
- [x] Rename `SPECIFICATION.md` → `specification.md`
- [x] Rename `Agents.md` → `agents.md`
- [x] Update all cross-references (README, Javadocs)
- [x] Verify no lingering uppercase references (grep clean after `05a8a04`)

### 14. License hygiene
- [x] Add copyright/preamble header to `LICENSE` before the GPL v3 text
- [x] Add License section to `README.md` with CHA-derivation note
- Source-file GPL headers moved to Task 15 (deferred to unwinnability release)

### 3 (residual). Audit broader public API surface
Deferred from Task 3. Do **after** the lenient-SAN release lands so files don't churn twice.
- [ ] Audit ~248 top-level public types under `src/main`
- [ ] Drop non-API types to package-private
- [ ] Decide stable public-API boundary

### ~~16. Replace AbstractBoard with default methods on ChessBoard interface~~ — done (moved to Done)

### 17. python-chess as primary cross-validation reference (discussion + design)

This is **discussion + design first**, implementation later. The project currently uses Carlos's `chesslib` (`LibraryCarlosBoard`) as a cross-validation reference, with limitations: cannot import PGN from a non-initial position, smaller test surface, less actively maintained. python-chess is the de-facto reference in chess software, actively maintained, and handles arbitrary positions.

#### Pattern recommendation — generation-based, not live invocation

- A Python script using python-chess generates expected outputs (legal moves, FEN, SAN, LAN, repetition counts, halfmove clock, dead-position verdicts) for a battery of fixtures, writes to a fixed file path.
- Java tests read the file and compare to clean-chess output.
- The Python script runs only when fixtures are added or regenerated, **not** during `mvn test`.
- Chess outputs are deterministic per input; cached reference data doesn't go stale.

`GeneratePythonTestCases.java` already exists — that's the foundation. Audit it and extend.

#### Discussion items to settle before coding

- [ ] Inventory exactly what python-chess will be used as reference for: legal-move generation, SAN/LAN, FEN, repetition counts, fifty-move clock, threefold/fivefold, dead-position (does python-chess support this directly or via heuristic?), CHA-style unwinnability (it doesn't — that stays unique to clean-chess).
- [ ] Decide: gradual migration (both chesslib and python-chess as references during transition) or hard cutover (drop chesslib at the same time).
- [ ] Decide: drop `LibraryCarlosBoard` entirely once python-chess covers its usage, or keep it for rule-engine-style cross-checks.
- [ ] Document the toolchain requirement: contributors need Python 3 + `pip install chess` (the package). Goes in `setup.md` (Task 12).
- [ ] Plan the regeneration workflow: how is "I added a fixture; now regenerate the python-chess-expected outputs" triggered cleanly? Maven goal? Script? Make target?

#### Implementation tasks (after the discussion)

- [ ] Decide and document the file format for stored expected outputs (JSON? line-based?)
- [ ] Refactor `GeneratePythonTestCases` (or replace) to produce the agreed format
- [ ] Migrate at least one cross-validation test from chesslib to python-chess as a proof-of-concept
- [ ] Phase out chesslib usage if the discussion lands there

**Release placement:** This is post-4.0. Possibly bundled with the lenient-SAN release if the lenient-SAN test data benefits from python-chess reference (overspecified disambiguation tolerance is something python-chess also handles, so cross-checks would be valuable). Otherwise, its own release-or-no-release pass.

---

## Next release — Lenient SAN

Single feature, headline release.

### 9. Lenient SAN parser/validator
- [ ] Audit what `SanValidation` and `Board.performMove(String)` accept/reject today
- [ ] Define diagnostic taxonomy: `OVERSPECIFIED_FILE_DISAMBIGUATION`, `OVERSPECIFIED_RANK_DISAMBIGUATION`, `OVERSPECIFIED_SQUARE_DISAMBIGUATION`, `MISSING_CHECK_SUFFIX`, `MISSING_CHECKMATE_SUFFIX`, `WRONG_CHECK_SUFFIX_FOR_CHECKMATE`, `LONG_ALGEBRAIC_NOTATION`, `ZERO_INSTEAD_OF_O_CASTLING`, `MISSING_PROMOTION_EQUALS`, `EXPLICIT_PAWN_LETTER`
- [ ] `LenientSanParserValidationResult` with forgiven-items channel (typed code + original token + canonical SAN)
- [ ] Public API: `LenientSanParser.parseText(String, Board)`, `validateText(String, Board)`, `LenientSanParserValidationException`
- [ ] `Board.performMoveLenient(String)` — opt-in at call site; `performMove(String)` stays strict
- [ ] Document in `specification.md` §3.3

### 10. Wire LenientSanParser into LenientPgnParser
- [ ] Replace strict `SanValidation` call inside lenient PGN movetext path with `LenientSanParser`
- [ ] Plumb forgiven items into `LenientPgnParserValidationResult`
- [ ] Update lenient-PGN test fixtures (one fixture per forgiven code)
- [ ] Update README "Lenient PGN parser" section — new tolerances are a real selling point
- Depends on Task 9

---

## Future release — Auto-CHA (DeepSquare moment)

### 15. GPL v3 source-file headers

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

### 11. Auto-CHA after every move
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

## Done

- [x] **1. README.md cleanup pass** (`c104100`, `21499ff` Java 17)
- [x] **3. Strip demo/dev code from src/main** (`aa0225e`)
  - `readme/ReadMeForRepository` deleted
  - `trainer/SanTrainerServer` deleted
  - `Message.main()` removed
  - Broader 248-public-types audit deferred (see *Current release → 3 residual*)
- [x] **4. Public API immutability — done deeper than originally scoped** (`55ee163`, `7707c75`, `f867357`)
  - Originally just "defensive copies"; ended up making the immutability visible at the type level via Guava `ImmutableList<X>` / `ImmutableSet<X>`.
  - `ChessBoard` interface: all 8 list/set accessors return `ImmutableList<X>` or `ImmutableSet<X>`.
  - `Board.legalMoveSetList: List<ImmutableSet<LegalMove>>` — inner sets sealed at write time via `AbstractLegalMoves.calculateLegalMoves` boundary.
  - `Board` persistent-field accessors wrap with `NonNullWrapperCommon.copyOfList` per call (Approach A — clarity over micro-optimisation).
  - `Board` freshly-built accessors wrap once at end of method.
  - `LegalMoveCalculation` record's two move-set components are `ImmutableSet<X>`.
  - `PgnFile` record: `tagList`, `halfMoveList` are `ImmutableList<X>` with compact-constructor defensive copy.
  - `LibraryCarlosBoard` (test impl) mirrors the new return types.
- [x] **16. Collapse AbstractBoard → default methods on ChessBoard interface**
  - All 12 stateless derivations from `AbstractBoard` moved to `ChessBoard` as `default` methods (`getCastlingRight(Side)`, `canClaimFiftyMoveRule`, `canClaimThreefoldRepetitionRule`, CHA quartet, `calculateInsufficientMaterial`, `isGameEnd`, `getFullMoveNumberForNextHalfMove`, `getLegalMovesSan`, `getLegalMovesUci`).
  - `isGameDraw` kept as a private interface method (Java 9+ feature).
  - `ChessBoard extends EnumConstants` so existing implementations keep their unqualified enum constants without breakage.
  - `Board` and `LibraryCarlosBoard` now `implements ChessBoard` directly.
  - `AbstractBoard.java` deleted.
  - Net effect: no public abstract class on the API surface; CHA methods appear directly in IDE completion on `Board`; cross-validation contract is the interface (the right level for it).
- [x] **Eclipse compiler warnings & infos cleanup (out-of-band)** (`997f51c`, `142e19f`, `305bff5`, `7e85eb6`, `06ca493`, `498b300`)
  - Project now compiles warning-free under JDT settings.
  - Includes: `unexpectedValidationErrorMessage` `@NonNull` annotation, three `pcve.getMessage()` / `e.getMessage()` `@SuppressWarnings("null")` extractions, `boxing` no-op suppression removed, log4j2.xml schema declaration + DTD download fix, class-level `@SuppressWarnings("null")` for JUnit Assertions / JDK BiFunction in 4 test classes.
