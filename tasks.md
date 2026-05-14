# Tasks

Order within each section is the source of truth. Completed tasks move to **Done** at the bottom.

---

## Release sequence

The next four releases, in order:

1. **DeepSquare moment** — Auto-CHA per-move + Zobrist transposition key + pawn-wall sound classifier + foundational refactors. Completes the FIDE-rules correctness story. Still on the `StaticPosition` reference implementation; performance acceptable for live game analysis.
2. **Bitboard backend** — performance overhaul. New `BitboardPosition` alongside the existing `StaticPosition`, verified bit-exact via differential testing. Move generation, attacks, check detection switch to bitboards; `StaticPosition` retained as the reference oracle. Required before Maven Central — public users expect engine-class performance.
3. **python-chess primary cross-validation + PGN/FEN test coverage expansion** — reactivate the python-chess test path (currently dormant: `GeneratePythonTestCases.java` exists but is not consumed). Make python-chess the main move-test reference; keep `chesslib` as a second witness. Expand PGN/FEN import-export coverage — the area `chesslib` cannot exercise (non-initial-position PGN via the `FEN`/`SetUp` tags).
4. **Maven Central publication** — the public release. Gated on all three preceding releases.

The story when the sequence completes: *clean-chess started as a correctness-first reference implementation, built from the FIDE rules without consulting existing libraries. It found correctness bugs in python-chess and ScalaChess along the way. Once the rules were stable, a bitboard backend was added and verified bit-exact against the original reference layer. Then cross-validation against python-chess was reactivated as primary, with `chesslib` retained as a second witness. Only then published to Maven Central.*

---

## Current release — DeepSquare moment

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

### Speed up `findHelpMate` — transposition key instead of FEN string for visited-position storage

The unwinnability `findHelpMate` search keys its visited-position set by `Board.fen()` — the full FEN string. On every node the FEN is re-serialised; on every lookup the string is re-hashed character by character; and the position is implicitly re-parsed when the next FEN is built for comparison. For a search that visits many positions, FEN serialisation is the hot path.

Replace the FEN string with a lightweight transposition key — a single `long` (or a small wrapper of two `long`s if collision-resistance matters) that fingerprints the position via Zobrist-style hashing or equivalent. Equality becomes a long compare, not a string compare, and there is no FEN round-trip in the loop.

This is the biggest performance win available **before** the bitboard release. Currently `findHelpMate` takes ~1 minute on positions where CHA-C runs in seconds; the FEN-string visited set is a meaningful fraction of that gap. The bitboard release then closes the rest.

Do this **before** Auto-CHA: measure findHelpMate improvement first, then layer Auto-CHA on top.

- [ ] Decide on the key shape (`long` Zobrist hash, or wider key with collision guard)
- [ ] Implement on `Board` (or the dynamic-position carrier) — incrementally updated on each move rather than computed from scratch
- [ ] Swap `findHelpMate`'s visited-position store to use the new key
- [ ] Verify search speed improvement on representative unwinnability fixtures
- [ ] Confirm CHA-quick and CHA-full correctness unchanged

### Dynamic position should store the en passant capture target square, not just a boolean

The dynamic position today carries a `boolean` for en passant availability — "possible / not possible." Functionally this is correct: the flag is reset after every pawn capture or pawn move, so the rule (en passant is legal only on the very next half-move after a double-step pawn advance) is enforced. But semantically it is wrong: en passant rules are square-specific, not abstract. The actual chess rule, and what FEN encodes (`e3`, `d6`, …), is the *square* the capturing pawn would land on. The implementation works because the target square is reconstructed elsewhere from the last half-move; storing it on the dynamic position would be the honest shape.

Folds naturally with the transposition-key task above: a square-valued field hashes more cleanly than a boolean tied to an out-of-band reconstruction.

- [ ] Replace the `boolean` field with an `enPassantCaptureTargetSquare` field (using the existing square enum, with `NONE` for "no en passant available")
- [ ] Drop wherever the target square is currently reconstructed from the last half-move; the dynamic position becomes the source of truth
- [ ] Verify that `equals` / `hashCode` for dynamic-position comparison (used in threefold repetition) treat the square correctly — same piece arrangement with different en passant targets must remain non-equal per FIDE rules (already enforced in 6.0.0 via the `EnPassantCaptureRuleThreefold` removal)

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

### Pawn-wall classifier — sound tri-state verdict

See [pawn-wall-soundness.md](pawn-wall-soundness.md) for the full design: tri-state `YES / NO / UNKNOWN` return, permanent-barrier principle (own pawns + pawn-attacked squares only — own pieces don't count), king-walk BFS, fixtures, implementation checklist, and the option of dropping the local heuristic once Auto-CHA is in place.

---

## Next release — Bitboard backend

The performance overhaul. Same library, faster — same answers verified bit-exact against the existing `StaticPosition` reference. Ships before Maven Central because the public-facing library needs acceptable performance: users expect engine-class speed, not reference-implementation-class speed. People reach for Carlos's `chesslib` over alternatives because it has bitboards.

### Approach — differential testing

The existing `StaticPosition` (square-array, slow-and-right) becomes the test oracle for a new `BitboardPosition` (bitboard, fast). Both representations live alongside; every test position runs through both and results must agree bit-exact. This is the classic differential-testing pattern (SQLite's TH3, LLVM's optimization-level cross-checks).

The architectural advantage clean-chess has: the two representations are independently derived from the FIDE rules, not from a common ancestor — so when they disagree, that's a real signal. Most chess engines added bitboards without a pre-existing reference; clean-chess's slow-and-right phase becomes the gift that pays back here.

### Action items — design phase (settle first)

- [ ] Relationship between `BitboardPosition` and `StaticPosition`: independent siblings (sync on every move) vs bitboard primary + `StaticPosition` as derived view. Lean: bitboard primary, `StaticPosition` computed on demand for human-readable purposes / introspection
- [ ] Magic-bitboard tables for sliding pieces (rook, bishop, queen): standard magic numbers vs PEXT (BMI2). Java + portability concern → magic
- [ ] Differential-test harness shape: every existing test runs both representations under an assertion, or a dedicated equivalence test that walks all production positions?
- [ ] Incremental make/unmake on bitboards vs full recomputation per move

### Action items — implementation

- [ ] New package `com.dlb.chess.bitboard` with `BitboardPosition` and supporting bitboard utilities
- [ ] Piece-on-square query
- [ ] Knight attack table; pawn attack tables (per side)
- [ ] Magic bitboards for sliding pieces (rook, bishop, queen)
- [ ] King attacks
- [ ] Attacked-squares computation
- [ ] Legal-move generation on bitboards
- [ ] Pin detection / check detection
- [ ] Incremental make/unmake support
- [ ] Production hot paths in `Board` switch to use `BitboardPosition`
- [ ] `StaticPosition` retained as reference oracle in tests; can be computed from `BitboardPosition` on demand
- [ ] Zobrist hash (introduced in the DeepSquare release as a string-FEN replacement) promoted to a properly bitboard-aware incremental hash
- [ ] Performance baseline: measure `findHelpMate` on representative unwinnability fixtures; target within 5× of `chesslib`

### Notes

- Auto-CHA per-move (in the DeepSquare release) uses `isUnwinnableQuick`, which is already cheap — no bitboard dependency there. The performance pain that motivates this release is `findHelpMate` (full unwinnability search).
- The DeepSquare-release Zobrist task partially addresses `findHelpMate` performance without bitboards (FEN-string visited set → `long` key). This release takes it the rest of the way.

---

## Future release — python-chess primary cross-validation + PGN/FEN test coverage expansion

The third release. Reactivates the python-chess test path (currently dormant), makes python-chess the main move-test reference, and expands PGN import/export test coverage — especially the FEN-anchored cases that `chesslib` cannot exercise.

### Context

The project historically tested against python-chess via `GeneratePythonTestCases.java`, which generates a Python test script from clean-chess fixtures. **That generator exists in the codebase but there is no active test that runs the generated Python script** — the comparison pipeline is dormant. Reactivating it is part of this release.

Carlos's `chesslib` (`LibraryCarlosBoard`) cannot import PGN from a non-initial-position via the `FEN`/`SetUp` tags. That gap is why python-chess becomes the *primary* cross-validation reference after this release. `chesslib` is retained as a second witness — having two independent oracles is more valuable than having one.

### Pattern recommendation — generation-based, not live invocation

- A Python script using python-chess generates expected outputs (legal moves, FEN, SAN, LAN, repetition counts, halfmove clock, dead-position verdicts) for a battery of fixtures, writes to a fixed file path.
- Java tests read the file and compare to clean-chess output.
- The Python script runs only when fixtures are added or regenerated, **not** during `mvn test`.
- Chess outputs are deterministic per input; cached reference data doesn't go stale.

### Discussion items to settle before coding

- [ ] Inventory exactly what python-chess will be used as reference for: legal-move generation, SAN/LAN, FEN, repetition counts, fifty-move clock, threefold/fivefold, dead-position (does python-chess support this directly or via heuristic?), CHA-style unwinnability (it doesn't — that stays unique to clean-chess).
- [ ] Decide: gradual migration (both `chesslib` and python-chess as references during transition) or hard cutover. Lean: gradual — keep `chesslib` as a second witness permanently.
- [ ] Document the toolchain requirement: contributors need Python 3 + `pip install chess`. Goes in `setup.md`.
- [ ] Plan the regeneration workflow: how is "I added a fixture; now regenerate the python-chess-expected outputs" triggered cleanly? Maven goal? Script? Make target?

### Reactivation work

- [ ] Audit `GeneratePythonTestCases.java` — current state, what it produces, what's still wired up after the dormancy period
- [ ] Decide and document the file format for stored expected outputs (JSON? line-based? CSV?)
- [ ] Refactor (or replace) the generator to produce the agreed format
- [ ] Build the Java-side consumer: read the expected-outputs file, compare to clean-chess output, fail loudly on mismatch
- [ ] Migrate at least one cross-validation test from `chesslib` to python-chess as a proof-of-concept

### python-chess as primary reference

- [ ] Migrate cross-validation tests from `chesslib` to python-chess for the surface python-chess covers
- [ ] Keep `LibraryCarlosBoard` as a second oracle — do not delete; two independent witnesses is the right shape

### PGN import/export test coverage expansion

The area `chesslib` cannot test and python-chess can: PGN imported from a non-initial position via the `FEN`/`SetUp` tags. Currently the test corpus skews toward initial-position games; expanding here is overdue, and python-chess being primary makes it feasible for the first time.

- [ ] Catalog the missing PGN-import-with-FEN test cases: short examples per side-to-move, per castling-right combination, per en-passant target square, per non-trivial half-move-clock / full-move-number
- [ ] Cross-validate each against python-chess output
- [ ] PGN export coverage: round-trip tests for PGN files that started with a non-initial `FEN` tag — both archival and semantic export modes
- [ ] FEN export coverage: round-trip from python-chess-generated FEN strings (real-world FEN exporters produce inputs the strict parser may not love)

---

## Future release — publish to Maven Central

The capstone release. Publish to Central only when the library has stabilised — every prior release done, identity questions settled, and any tasks that surface during the prerequisite work itself addressed first. Maven Central artifacts are immutable: once published, an artifactId+version pair lives forever in the public record. The bar for moving from JitPack to Central is therefore "we are confident this artifact represents the project well, indefinitely."

### Prerequisites — must be true before any Central work begins
- [ ] DeepSquare release complete (Auto-CHA + Zobrist + pawn-wall classifier + foundational refactors)
- [ ] Bitboard release complete (performance acceptable, differential-test harness green)
- [ ] python-chess primary + PGN/FEN coverage release complete
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

### Records carry data, not behavior — sweep for violations
The project rule (documented in `coding-conventions.md`): records carry data; domain logic that operates on them lives in dedicated utility / service classes. Permitted on a record: compact-constructor validation, `Comparable` when ordering is intrinsic, and language-provided `equals` / `hashCode` / `toString`. Domain-operation methods are not.

Surfaced by the unused-code-detector pass on `StaticPosition`: the record carries multiple non-data methods — `createChangedPosition` (three overloads), `isPawn`, `isOwnPawn`, `isOpponentPawn`, `isOwnKing`, `isOpponentKing`, almost certainly more. Some have only test callers (suggesting test scaffolding), some have production callers, one (`isOwnKing`) has zero callers anywhere.

- [ ] Catalog every non-permitted member on `StaticPosition` and assign a disposition per member: delete (no callers anywhere), move to a test-side helper that **takes** a `StaticPosition` rather than duplicating it (test-only callers), or move to a `StaticPositionUtility` (production callers).
- [ ] Sweep every record under `src/main/java` for the same pattern. Records to check include at least `Fen`, `Tag`, `PgnFile`, `LegalMove`, `MoveSpecification`, `StaticPosition`, plus any other top-level `record` declarations under `src/main`.
- [ ] Apply the dispositions; verify only the permitted member shapes remain on each record.
- [ ] Naturally folds into the API-surface reduction release, since most "move to utility" relocations open the door to making the utility itself package-private.

---

## Obsolete

Items deemed no longer worth pursuing. Captured so the decision is visible.

### Replace `EnumConstants` constant interface
`com.dlb.chess.common.constants.EnumConstants` is a `public interface` whose only purpose is to expose ~90 `public static final` aliases for `Square.*`, `Side.*`, `Piece.*`, `PieceType.*`, `Rank.*`, `File.*` so implementing classes inherit them unqualified. This is the classic "constant interface" anti-pattern (Effective Java item 22): interfaces should describe a contract/behavior, not be a convenience-inheritance vehicle for constants. The mechanism reads as beginner Java and leaks an internal vocabulary choice into the public type surface — `ChessBoard extends EnumConstants` is the clearest symptom (the chess contract has nothing to do with how implementers prefer to spell `Square.E4`). Used by 43 files under `src/main` plus tests.

Replacement strategy options, depending on intended audience:
- public-API constants: `public final class EnumConstants` with `public static final` fields and a private constructor (callers `import static`)
- internal-only: make package-private and split closer to where they belong (domain-grouped, e.g. `BoardSquares`, `PieceLetters`)
- derived enum collections: prefer local `EnumSet` / `ImmutableSet` factories in the utility that needs them, or dedicated package-private constants classes by domain

- [ ] Pick a replacement strategy (default lean: package-private utility class with `import static`, since the constants are internal vocabulary and the audit reduces public surface anyway)
- [ ] Drop `extends EnumConstants` from `ChessBoard` regardless of strategy — the interface should not carry constants
- [ ] Convert the 43 src/main call sites + tests to static imports
- [ ] Folds naturally into the API-surface reduction release, since most "move to utility" relocations open the door to making the utility itself package-private.

---
