# Pawn-wall detection — sound classifier (DeepSquare release)

Tightening `PawnWall.calculateHasPawnWall` so that a positive verdict is always correct. Currently the predicate returns `true` for some positions that are not actually pawn walls, which makes it unsafe to use as an unwinnability signal — the caller pattern `if (calculateHasPawnWall(board)) return Winnable.NO;` propagates every false positive into a wrong unwinnability claim.

This work is deferred to the **DeepSquare / Auto-CHA release** rather than the current cleanup because the most natural implementation involves a king-walk BFS in test code, and the concern is the cumulative cost across the test suite. The right home is the per-move CHA pipeline, where the same machinery can replace or back-stop the local heuristic.

---

## Design — geometric check in production, BFS as test oracle

**Production**: a geometric pawn-wall check operating on the pawn structure. Recognized shapes:

1. **Horizontal wall**: a rank `R` where every file has a barrier square at `R` (own pawn or opposing-pawn-attacked square); kings on opposite sides.
2. **Zig-zag wall**: a chain of barrier squares from file a to file h using only horizontal/vertical adjacencies (no diagonal hops — those leak); kings on opposite sides.

Anything else → `UNKNOWN`. Auto-CHA per move catches what the geometric check doesn't.

**Test oracle**: BFS king-walk over a corpus of pawn-wall fixtures.

- For each fixture where the geometric check returns `YES`, run the BFS independently.
- Assert: BFS confirms the king cannot reach any opposing pawn through passable squares (treating own non-pawn pieces as movable and undefended opposing pawns as capturable).
- If they disagree, either the geometric check accepted a false positive or the BFS is wrong — test failure either way.

The contract is asymmetric:

```
geometric_YES ⟹ BFS_YES   (must hold; soundness)
BFS_YES ⟹ geometric_YES   (not required; the geometric check is allowed to be conservative)
```

The BFS doesn't ship in production. It exists only in test code as the second opinion.

---

## Background — what the current algorithm checks

In `src/test/java/com/dlb/chess/test/winnable/PawnWall.java`, the predicate today:

1. Returns `false` if any rook, knight, or queen is on the board (these are too mobile to reason about locally).
2. Requires every pawn to be blocked — no advance, no capture, no en passant — i.e., the wall is **locked**.
3. For each side with bishops: runs a BFS through empty / enemy squares from each bishop's square. If any BFS reaches an opponent pawn, the bishop can break the wall → `false`.
4. Verifies a pawn-wall line structure via `calculateHasPawnWallLine` — a chain search through orthogonally-adjacent barrier squares from the leftmost file to the rightmost file.
5. Verifies each king is on its own side of the wall via `calculateIsKingBehindPawnWall`.

If all pass: returns `true`.

The chain search in step 4 is the geometric check this design preserves. Step 5 is the soundness gap below.

## The soundness problem

Step 5 only checks the kings' **current** position. It does not consider whether either king can **walk** through the wall by capturing undefended opposing pawns, possibly after maneuvering own pieces out of the way.

### Concrete false-positive fixture

```
7k/8/1p6/1Pp5/2Pp4/pB1Pp1p1/P1B1P1P1/3B2K1 b - - 0 1
```

- White: K g1; B d1, B c2, B b3; pawns a2, e2, g2, d3, c4, b5
- Black: k h8; pawns a3, b6, c5, d4, e3, g3

All white pawns are on light squares; all black pawns are on dark squares; the three white bishops are all light-square bishops. The locked-wall check passes; the bishop BFS check passes (light-square bishops cannot reach dark-square pawns); the kings start on their own sides. The geometric chain check finds a chain from file a to file h (e.g., a5–b5–c5–c4–d4–d3–e3–e2–f2–g2–h2). Current algorithm returns `true`.

But the position is **not** a sound pawn wall: the white king can walk

```
g1 → f1 → e1 → d1 (after the d1 bishop moves)
        → c2 (after the c2 bishop moves)
        → b3 (after the b3 bishop moves)
        → a3  (capture: a3 is an undefended black pawn)
```

and breach the wall by capturing an undefended Black pawn (a3) that sits on the king's side of the chain. The chain is geometrically valid but doesn't *dynamically* separate the position — moving the bishops aside opens a route, and a3 is captured before the BFS could even need to cross a chain square.

### Concrete sound-YES fixtures

**Horizontal wall** — rank 3 is fully covered:

```
4k3/8/8/p1p1p1p1/PpPpPpPp/1P1P1P1P/8/4K3 w - - 0 1
```

Every file at rank 3 is a barrier (own pawn or attacked). White king on e1 cannot enter rank 3. BFS terminates after exploring ranks 1–2. Sound `YES`.

**Zig-zag wall** — chain a3–b3–c3–d3–d4–e4–e5–f5–f6–g6–h6 using only orthogonal adjacencies:

```
4k3/5p1p/4pP1P/3pP3/p1pP4/P1P5/8/4K3 w - - 0 1
```

White king on e1 can reach rank 5 on the kingside (g5/h5) but no further; queenside sealed at rank 3. Doesn't touch any Black pawn. Sound `YES`.

---

## Return type — tri-state

```java
public enum PawnWallVerdict { YES, NO, UNKNOWN }
```

`YES` is sound (always correct). `NO` is a definite negative on a clear violation (e.g., no pawns on the board). `UNKNOWN` is the safe fallback for cases the classifier cannot decide. All current callers in `WinnableAnalyzer` and `WinnableUtilityAnalyzeChaLichess` only consume the positive verdict — they fall through on anything else — so the migration is mechanical:

```java
if (PawnWall.calculate(board) == PawnWallVerdict.YES) return Winnable.NO;
```

## Production geometric check

The existing chain-finder in `calculateHasPawnWallLine` already implements the zig-zag chain logic. It accepts horizontal walls as the simple case (chain on a single rank) and zig-zag walls (chain stepping orthogonally across ranks). The geometric check shape:

1. **Material precheck**: no rooks/knights/queens; at least one pawn. Otherwise → `UNKNOWN`.
2. **Locked-wall precheck**: every pawn is blocked (no advance, no capture, no en passant). Otherwise → `UNKNOWN`.
3. **Bishop-reach precheck**: if either side has bishops, the bishop's diagonal reach must not include any opposing pawn. Otherwise → `UNKNOWN`. (Color-locked bishops pass trivially.)
4. **Chain check** for each side: a chain of barrier squares (own pawn or opposing-pawn-attacked) from leftmost file to rightmost file using only orthogonal adjacencies.
5. **King-behind-wall check** for each side: own king on the correct side of the chain (current-position check; no walk modeling).

If all pass → `YES`. Otherwise → `UNKNOWN`.

Steps 1–5 are already implemented in `PawnWall.calculateHasPawnWall`; the public API is rewrapped to return `PawnWallVerdict.YES` when the existing predicate returns true, and `UNKNOWN` otherwise.

## Test oracle — BFS king-walk

Test-only. Implements the BFS that the production check doesn't run.

### Permanent-barrier principle

A square is part of the king's **permanent** barrier iff it stays a barrier across every reachable future position. In a locked-wall configuration, two and only two kinds of squares are permanent:

1. **Own pawns** — immobile because the wall is locked.
2. **Squares attacked by opposing pawns** — those attacks are permanent because the attacking pawns are themselves locked.

Own non-pawn pieces (bishops, knights, rooks, queens) are **not** permanent barriers: they can be moved out of the way. Defense of an opposing pawn by an opposing piece is **not** permanent for the same reason; only defense by another opposing pawn counts.

### En passant

A locked wall implies no pawn can advance now or in any future continuation. En passant requires a two-square pawn advance, which is therefore impossible. Pawn attack squares never change; the wall structure never mutates. This is what makes the static barrier analysis sound across all reachable game states.

### BFS

For each side, BFS from the king's square. A square is **passable** iff:

- Currently empty, **OR**
- Currently occupied by a same-side **non-pawn** piece, **OR**
- Currently occupied by an **undefended** opposing pawn (defended only by other opposing pawns counts as defense; defense by an opposing piece does not).

A square is **impassable** iff:

- Currently occupied by a same-side pawn, **OR**
- Attacked by any opposing pawn, **OR**
- Currently occupied by an opposing pawn that is defended by another opposing pawn.

If the BFS reaches any opposing pawn for either side, the wall is breachable.

### Test contract

For every fixture in the corpus:

- Run `PawnWall.calculate(board)`.
- If result is `YES`, run the BFS oracle. The oracle must confirm the king cannot reach any opposing pawn for either side. If it disagrees, **test failure** — the geometric check is unsound on this fixture.

For positions where the geometric check returns `NO` or `UNKNOWN`, no claim is made about the BFS result (the geometric check is allowed to be conservative).

---

## Action items

- [ ] Introduce `PawnWallVerdict { YES, NO, UNKNOWN }`.
- [ ] Add `PawnWall.calculate(Board) -> PawnWallVerdict` wrapping the existing predicate.
- [ ] Migrate the three call sites in `WinnableAnalyzer` and `WinnableUtilityAnalyzeChaLichess` to `verdict == YES`.
- [ ] Implement the BFS king-walk as a test-only helper (`PawnWallKingWalkOracle` or similar).
- [ ] Add the test class iterating the corpus: assert `geometric_YES ⟹ BFS_YES`.
- [ ] Fixtures: sound-`YES` horizontal wall, sound-`YES` zig-zag wall; the false-positive fixture (current code returns YES but BFS says no) is captured as a *known geometric-check bug* (see follow-up below).
- [ ] Audit existing `testHelperLichess(..., true)` fixtures: each must still return `YES` under the new wrapper. The wrapper preserves current behaviour, so this should be a no-op verification.

## Follow-up: tighten the geometric check to reject the false-positive fixture

The current chain-finder accepts the false positive above. The chain a5–b5–c5–c4–d4–d3–e3–e2–f2–g2–h2 is geometrically valid, but the position is breachable because:

1. There's an undefended Black pawn (a3) on the king's side of the chain.
2. The king can route around the chain through bishop-occupied squares.

Possible tightenings:

- Reject chains where any opposing pawn exists on the king's side of the chain. (Catches a3 in the false positive.)
- Reject chains where any own non-pawn piece sits on a square adjacent to a chain square that's also adjacent to an opposing-pawn region. (Catches the c2/b3 bishops.)

These are conservative — they will reject some sound walls that have pieces in incidental positions. The trade-off is sound `YES` vs. coverage. Captured as a follow-on task in `tasks.md`; not blocking the initial wrapper + test infrastructure.

## Relationship to Auto-CHA

Auto-CHA after every move (the main DeepSquare item) renders this local heuristic largely redundant: a CHA-quick call already detects unwinnability soundly across all piece configurations. Two reasonable end-states:

1. **Delete the local heuristic** once Auto-CHA is wired in. The pre-CHA fast path becomes unnecessary.
2. **Keep the local heuristic as a fast pre-filter** in front of CHA-quick. The sound version above is required for this — an unsound pre-filter would short-circuit CHA on positions that aren't actually unwinnable.

Decide at implementation time which path to take. The work in this document is a prerequisite for option 2 and a fallback if option 1 is delayed.
