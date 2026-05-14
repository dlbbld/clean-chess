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

The unwinnability `findHelpMate` search used to key its visited-position set by `Board.fen()` — the full FEN string. Every node serialised a FEN, re-parsed it via `FenParserRaw`, and rebuilt a stripped-down FEN-shape string for the map key. For a search visiting many positions, FEN serialisation was the hot path.

Landed in `996bcd3a` (May 14, 2026) as a structured `TranspositionKey` record on `FindHelpmateExhaust`: `(StaticPosition, Side havingMove, CastlingRight white, CastlingRight black, Square enPassantCaptureTargetSquare)`. Records get `equals` / `hashCode` for free; no allocation of FEN strings; no regex parsing; exact equality (no hash-collision risk).

**En passant normalization.** Unlike `DynamicPosition` (which uses a boolean for en-passant availability — sufficient for in-game threefold repetition because pawn-irreversibility makes the boolean a complete distinguisher along a single game's history), the transposition key crosses the move-tree, not game history. Two different paths can converge on the same piece arrangement with the same side-to-move and castling rights but a *different* en-passant target square. The target is therefore included in the key, but **normalised**: if no opposing pawn can actually capture on the target (a "phantom" e.p. that FEN records but is unreachable), the field is set to `Square.NONE`. This uses the existing `calculateIsEraseEnPassantCaptureTargetSquare` logic from the old FEN-string approach.

- [x] Decide on the key shape — chose structured record over long Zobrist hash. Simpler, no per-square-piece random tables, allocation-heavier per node but exact equality
- [x] Swap `findHelpMate`'s visited-position store to use the new key
- [x] Include the en-passant target square in the key (not just availability), normalised to `NONE` when no capture is actually available
- [x] Confirm CHA-quick and CHA-full correctness unchanged (full `mvn -q test` passes)
- [x] Tests covering counter-ignoring, phantom-e.p.-normalising, and capturable-e.p.-preserving behaviour (`TestFindHelpmateExhaustTranspositionKey`)

**Deferred to the bitboard release.** A long Zobrist hash (with per-square-piece random tables, XOR'd incrementally on each move) becomes natural once `BitboardPosition` lands — the bitboard update sites are exactly where the Zobrist XORs go. If profiling at that point shows the record allocation matters, promote to a `long` key then. Until then the structured record is the right shape.

### Switch `DynamicPosition` to hold the normalized en-passant target square; collapse `TranspositionKey` into it

After the helpmate transposition-key work landed (`996bcd3a`), `DynamicPosition` and `TranspositionKey` are structurally near-identical: both are `(StaticPosition, Side, CastlingRight, CastlingRight, ?)`. The only difference is that `DynamicPosition` carries a `boolean isEnPassantCapturePossible` and `TranspositionKey` carries a `Square enPassantCaptureTargetSquare` (normalized to `NONE` when no opposing pawn can actually capture).

**Semantic equivalence:** within a single game's history, pawn-irreversibility means the boolean and the normalized-square carry equivalent equality information — the threefold-repetition behavior is unchanged either way. The boolean was correct for `DynamicPosition`'s use case. The reason to switch is architectural symmetry, not correctness: if `DynamicPosition` adopts the square, then `TranspositionKey` becomes structurally identical and can collapse into `DynamicPosition`. One type, one normalization rule, one conceptual model of "chess-position equivalence." The helpmate transposition map keys directly on `DynamicPosition` and `FindHelpmateExhaust.calculateTranspositionKey` disappears.

The FEN export path is untouched — it reads `Board.getEnPassantCaptureTargetSquare()` (the raw, FEN-spec target square), which is separate from `DynamicPosition`'s normalized square. Those two pieces of information serve different purposes (FEN compliance vs. position equivalence) and the distinction stays.

Landed in `2d01c2ab` (May 14, 2026); follow-up `[hash-of-this-commit]` adds the `DynamicPosition.isEnPassantCapturePossible()` derived accessor for public-API source compatibility and a test for the king-safety-aware tightening.

- [x] Change `DynamicPosition`'s `boolean isEnPassantCapturePossible` to `Square enPassantCaptureTargetSquare`; the field holds the e.p. target square when an opposing pawn can actually capture there, `Square.NONE` otherwise
- [x] Update Board's two `DynamicPosition` construction sites (initial and post-move) to pass the normalized square instead of the boolean
- [x] Update `RepetitionUtility`'s manual `DynamicPosition` equality (line 34 today) to compare the square component
- [x] Update `Board.isEnPassantCapturePossible()` public API to read `getDynamicPosition().enPassantCaptureTargetSquare() != Square.NONE` — preserves the public method for existing callers
- [x] Preserve `DynamicPosition.isEnPassantCapturePossible()` as a derived accessor on the record so external callers of the old auto-generated accessor still compile
- [x] In `FindHelpmateExhaust`: switch the transposition map to `HashMap<DynamicPosition, Integer>`, delete the `TranspositionKey` record, delete `calculateTranspositionKey`, callers use `board.getDynamicPosition()` directly
- [x] Verify the existing helpmate transposition-key tests still pass under the new shape; add a pinned-e.p.-capturer test to cover the king-safety-aware tightening (positions where an adjacent pawn exists but cannot legally capture due to a pin now correctly collapse to `Square.NONE`)

`FindHelpmateExhaust.calculateIsEraseEnPassantCaptureTargetSquare` is **kept**, contrary to the original sketch: it has one remaining caller, the debug-only `calculateStockfishFen` helper (cold code behind `IS_DEBUG = false`).

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

See [pawn-wall-soundness.md](pawn-wall-soundness.md) for the full design: production geometric check (chain-finder over orthogonally-adjacent barrier squares), BFS king-walk as a test oracle, and the asymmetric agreement contract `geometric_YES ⟹ BFS_YES`. Initial scaffolding has landed; the geometric check still has a known false-positive case (see follow-up below).

- [x] Introduce `PawnWallVerdict { YES, NO, UNKNOWN }`
- [x] `PawnWall.calculate(Board) -> PawnWallVerdict` wrapping the existing geometric predicate
- [x] Migrate the three call sites in `WinnableAnalyzer` and `WinnableUtilityAnalyzeChaLichess` to `verdict == YES`
- [x] BFS king-walk implemented as the test-only `PawnWallKingWalkOracle`
- [x] Test class `TestPawnWallGeometricVerdict` verifies the agreement contract on the horizontal-wall and zig-zag-wall fixtures from `pawn-wall-soundness.md`

#### Follow-up — tighten the geometric check to reject the false-positive fixture

The current chain-finder accepts the position `7k/8/1p6/1Pp5/2Pp4/pB1Pp1p1/P1B1P1P1/3B2K1 b - - 0 1` (documented in `pawn-wall-soundness.md`). The chain `a5-b5-c5-c4-d4-d3-e3-e2-f2-g2-h2` is geometrically valid, but the king can capture the undefended Black pawn on a3 (sitting on the king's side of the chain) by routing through bishop-occupied squares (c2, b3). The geometric check returns `YES`; the BFS oracle would return "not trapped." This fixture is not in the test corpus yet (would cause a test failure with the current code).

Two candidate tightenings:

- Reject chains where any opposing pawn exists on the king's side of the chain. (Catches a3.)
- Reject positions where any own non-pawn piece sits on a square that's currently masking the chain (i.e., the chain would be incomplete without that piece as a "stand-in barrier"). (Catches the c2/b3 bishops.)

Both are conservative — they reject some sound walls. Once Auto-CHA per move is wired in, this whole local heuristic may be deletable. Decide at implementation time.

- [ ] Add the false-positive fixture to the test corpus once the geometric check is tightened
- [ ] Decide: tighten the geometric check, or delete the local heuristic once Auto-CHA is in place

#### Follow-up — the all-pawns-involved soundness check landed

`PawnWall.calculate(Board)` now wraps the existing chain check with an additional rule: **every pawn on the board must be either a chain element or an attacker of a chain element**. Floating pawns — those not contributing to the barrier — admit helpmates where the king captures the floater (or allows it to be captured) and a promotion follows. This rule rejects the ambrona_10 false positive (`7k/8/1p6/1Pp5/2Pp4/pB1Pp1p1/P1B1P1P1/3B2K1`): the `a2`/`a3` pair are floating, and CHA-full correctly says WINNABLE.

The test corpus cross-checks the geometric verdict against two independent oracles:

1. The BFS king-walk (`PawnWallKingWalkOracle`) — verifies the king truly cannot reach the opposing king's square.
2. `UnwinnableQuickAnalyzer` — Ambrona's quick unwinnability oracle. The main soundness gate.

The geometric classifier is now intentionally narrower than full unwinnability: many positions Ambrona's analyzer correctly classifies as `UNWINNABLE` fall through to `UNKNOWN` here (e.g. Norgaard examples with extra backed-up pawns that can't actually promote). The geometric classifier exists for the geometric pattern itself; `UnwinnableQuick` is the canonical unwinnability check.

#### Future work — bishop-mate edge cases

The current all-pawns-involved rule is sound for the known false-positive class (floating-pawn promotion). It's possible some bishop-only mating patterns (positions where multiple same-coloured bishops force mate even though the king is trapped behind a sound wall) could still slip through — none have been observed in the corpus yet. If one surfaces, the test catches it via the `UnwinnableQuick != UNWINNABLE` disagreement.

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

### Profound-level square geometry — promote single-step calculations to lookup tables
The codebase already uses lookup tables for the geometry that matters — `OrthogonalRange`, `DiagonalRange`, `KnightEmptyBoardSquares`, `BishopEmptyBoardSquares`, `RookEmptyBoardSquares`, `DiagonalLineUtility`. Single-step instance-style methods on `Square` (`calculateLeftSquare`, `calculateLeftDiagonalSquare`, `calculateAheadSquare`, etc.) and `File` / `Rank` are the calculate-on-demand holdouts in an otherwise table-based codebase. The "calculate" form has a deeper testing problem: any independent test implementation faces a definitional regress ("left of E4 from White is D4 — but what does *left* mean if not what `calculateLeft` returns?"), which is how `Square.calculateIsLeftDiagonalSquare` ended up as a tautological method that tested itself against itself.

The fix is to promote these single-step relationships to data:
- `Map<Square, Map<Side, Square>>` (or `EnumMap<Square, EnumMap<Side, Square>>`) constants for left, right, ahead, behind, left-diagonal, right-diagonal
- The "has" predicates collapse to `map.containsKey(...)` or `value != NONE`
- The map is built once at class load; tests verify the table by inspection or via python-chess cross-reference (folds into the existing python-chess backlog)
- The bug surface shrinks to one place: the table-builder

Marked obsolete because the bitboard release will replace this whole layer of square arithmetic with bit-level operations; doing the `EnumMap` refactor first would be throwaway work. The definitional-regress testing problem also dissolves once `BitboardPosition` exists as an independent oracle.

- [ ] Inventory single-step `calculate*` methods on `Square` / `File` / `Rank` that are pure square→square (or square+side→square) lookups
- [ ] Replace each with a precomputed `EnumMap` constant + a thin accessor
- [ ] Generate the expected tables either by hand-curation or by python-chess cross-reference (latter is preferred once the python-chess infrastructure lands)
- [ ] Drop the algorithm-vs-algorithm test patterns; tests become "look up in production table, compare to reference table"
- [ ] **Companion concern — bloated lookup-table implementations.** `PawnDiagonalSquares` is 826 lines of generated code (per-square `addWhiteA1`, `addWhiteA2`, … methods) to express what is conceptually "for each pawn from-square, the 0–2 diagonal capture squares." The same shape recurs across the `com.dlb.chess.squares.emptyboard.*` family (`Knight`, `Bishop`, `Rook`, `Queen`, `King`, `PawnOneAdvance`, `PawnTwoAdvance`, `PawnAnyAdvance`). These tables are correctly precomputed, but their implementation should be a single `static {}` initializer that loops over `Square.REAL` and computes each entry via simple file/rank arithmetic — not hundreds of method-per-square stubs. Replacing them collapses ~thousand-line files to dozens of lines while preserving the precomputed-table API. Same theme as the main bullet: keep the lookup, sane the implementation.

---
