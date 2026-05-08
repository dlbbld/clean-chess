# Tasks

Master list. Each task keeps its original number for stable referencing. Completed tasks move to **Done** at the bottom.

---

## Current release — project organization & big-picture cleanup

Theme: documentation, obvious issues, major bugs. No new features.

### 2. pom.xml / build configuration
- [x] Drop `maven.compiler.release` 26 → 17
- [ ] Reconcile `groupId` between `pom.xml` and `README.md`
- [ ] Move `log4j-core` to `<scope>runtime</scope>` (or remove); keep only `log4j-api` as compile dep
- [ ] Remove `src/main/resources/log4j2.xml` (or move to `src/test/resources`) — library should not impose logging config

### 4. Public API immutability
- [ ] `Board` — defensive copies on the three list-returning accessors (lines ~661, 666, 706)
- [ ] `PgnFile` — defensive copy of list components in compact constructor and accessors

### 5. Public API discoverability & naming
- [ ] `isSeventyFiftyMove()` → `isSeventyFiveMove()`
- [ ] Pull CHA methods (`isUnwinnableQuick/Full`, `isDeadPositionQuick/Full`) down from `AbstractBoard` to `Board` — or document the split somewhere visible
- [ ] Rename one of `analyze` / `analysis` so they don't differ by one letter
- [ ] Collapse `Analyzer extends AnalyzerPrint` into one `final` class
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
- [ ] Verify no lingering uppercase references

### 14. License hygiene
- [x] Add copyright/preamble header to `LICENSE` before the GPL v3 text
- [x] Add License section to `README.md` with CHA-derivation note
- [ ] (Optional, separate pass) Add GPL v3 source-file headers to all `.java` files under `src/main/`. Mechanical, noisy in diffs — best done in isolation.

### 3 (residual). Audit broader public API surface
Deferred from Task 3. Do **after** the lenient-SAN release lands so files don't churn twice.
- [ ] Audit ~248 top-level public types under `src/main`
- [ ] Drop non-API types to package-private
- [ ] Decide stable public-API boundary

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
