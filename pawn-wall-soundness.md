# Pawn-wall detection — sound classifier (DeepSquare release)

Tightening `PawnWall.calculateHasPawnWall` so that a positive verdict is always correct. Currently the predicate returns `true` for some positions that are not actually pawn walls, which makes it unsafe to use as an unwinnability signal — the caller pattern `if (calculateHasPawnWall(board)) return Winnable.NO;` propagates every false positive into a wrong unwinnability claim.

This work is deferred to the **DeepSquare / Auto-CHA release** rather than the current cleanup because the most natural implementation involves a king-walk BFS in test code, and the concern is the cumulative cost across the test suite. The right home is the per-move CHA pipeline, where the same machinery can replace or back-stop the local heuristic.

---

## Background — what the current algorithm checks

In `src/test/java/com/dlb/chess/test/winnable/PawnWall.java`, the predicate today:

1. Returns `false` if any rook, knight, or queen is on the board (these are too mobile to reason about locally).
2. Requires every pawn to be blocked — no advance, no capture, no en passant — i.e., the wall is **locked**.
3. For each side with bishops: runs a BFS through empty / enemy squares from each bishop's square. If any BFS reaches an opponent pawn, the bishop can break the wall → `false`.
4. Verifies a pawn-wall line structure via `calculateHasPawnWallLine`.
5. Verifies each king is on its own side of the wall via `calculateIsKingBehindPawnWall`.

If all pass: returns `true`.

## The soundness problem

Step 5 only checks the kings' **current** position. It does not consider whether either king can **walk** through the wall by capturing undefended opposing pawns, possibly after maneuvering own pieces out of the way.

### Concrete false-positive fixture

```
7k/8/1p6/1Pp5/2Pp4/pB1Pp1p1/P1B1P1P1/3B2K1 b - - 0 1
```

- White: K g1; B d1, B c2, B b3; pawns a2, e2, g2, d3, c4, b5
- Black: k h8; pawns a3, b6, c5, d4, e3, g3

All white pawns are on light squares; all black pawns are on dark squares; the three white bishops are all light-square bishops. The locked-wall check passes; the bishop BFS check passes (light-square bishops cannot reach dark-square pawns); the kings start on their own sides. Current algorithm returns `true`.

But the position is **not** a pawn wall: the white king can walk

```
g1 → f1 → e1 → d1 (after the d1 bishop moves)
        → c2 (after the c2 bishop moves)
        → b3 (after the b3 bishop moves)
        → a3  (capture: a3 is an undefended black pawn)
```

and breach the wall. The white bishops form part of the apparent "barrier" only because they happen to sit on the king's escape squares — but they can be maneuvered away.

### Concrete sound-YES fixture (for regression)

```
7k/8/1p6/pPp5/P1Pp4/3Pp1p1/2B1P1P1/6K1 b - - 0 1
```

- White: K g1; B c2; pawns a4, b5, c4, d3, e2, g2
- Black: k h8; pawns a5, b6, c5, d4, e3, g3

Same colour-locked bishop, locked wall. Black-pawn attacks on the white side cover `b2, c3, d2, f2, h2, b4`. From g1, the king's reach through `{empty, own-bishop-passable, undefended-opponent-pawn}` is `{a1, a2, a3, b1, b2, b3, c1, c2(passable), d1, e1, f1, g1, h1}` — none adjacent to or on any black pawn. The sound classifier must return `YES` here.

---

## Proposed soundness model

### Return type — tri-state

```java
public enum PawnWallVerdict { YES, NO, UNKNOWN }
```

`YES` is sound (always correct). `NO` is a definite negative on a clear violation. `UNKNOWN` is the safe fallback for cases the classifier cannot decide. All current callers in `WinnableAnalyzer` and `WinnableUtilityAnalyzeChaLichess` only consume the positive verdict — they fall through on anything else — so the migration is mechanical:

```java
if (PawnWall.calculate(board) == PawnWallVerdict.YES) return Winnable.NO;
```

### The permanent-barrier principle

A square is part of the king's **permanent** barrier iff it stays a barrier across every reachable future position. In a locked-wall configuration, two and only two kinds of squares are permanent:

1. **Own pawns** — immobile because the wall is locked.
2. **Squares attacked by opposing pawns** — those attacks are permanent because the attacking pawns are themselves locked.

Own non-pawn pieces (bishops, knights, rooks, queens) are **not** permanent barriers: they can be moved out of the way. Defense of an opposing pawn by an opposing piece is **not** permanent for the same reason; only defense by another opposing pawn counts.

### En passant

A locked wall implies no pawn can advance now or in any future continuation. En passant requires a two-square pawn advance, which is therefore impossible. Pawn attack squares never change; the wall structure never mutates. This is what makes the static barrier analysis sound across all reachable game states. Worth a one-line comment in the code.

### King-walk BFS

For each side, BFS from the king's square. A square is **passable** iff:
- Currently empty, **OR**
- Currently occupied by a same-side **non-pawn** piece, **OR**
- Currently occupied by an **undefended** opposing pawn (defended only by other opposing pawns counts as defense; defense by an opposing piece does not).

A square is **impassable** iff:
- Currently occupied by a same-side pawn, **OR**
- Attacked by any opposing pawn, **OR**
- Currently occupied by an opposing pawn that is defended by another opposing pawn.

If the BFS reaches any opposing pawn, the king can breach the wall ⇒ verdict is not `YES`.

### Tier hierarchy for sound `YES`

| Tier | Pieces present (besides kings + pawns) | Additional check |
|---|---|---|
| 0 | None | — |
| 1 | Bishops, but each bishop is colour-locked vs. all opposite-side pawns | — |
| 2 | General bishops | BFS bishop-reach |

All tiers also require: locked wall + king-walk BFS shows neither king can reach any opposing pawn.

Tier 1 is strictly between 0 and 2 — more permissive than 0, more conservative than 2. The current code's BFS bishop-reach subsumes Tier 1 in practice (BFS from a colour-locked bishop never escapes its colour class). Tier 1 is worth listing explicitly because it can be decided by inspection without any traversal.

---

## Implementation checklist

- [ ] Introduce `PawnWallVerdict { YES, NO, UNKNOWN }`.
- [ ] Refactor `calculateHasPawnWall` to return the enum.
- [ ] Migrate the three call sites in `WinnableAnalyzer` and `WinnableUtilityAnalyzeChaLichess` to `verdict == YES`.
- [ ] Verify the locked-wall pre-check is genuinely complete (no advance, no capture, no en passant available now → none available in any future, because the wall is static).
- [ ] Implement `calculatePermanentBarrier(Side)` — own pawns ∪ squares attacked by opposing pawns.
- [ ] Implement `calculatePawnDefenders(Square pawn)` — only opposing pawns count as defenders.
- [ ] Implement `calculateKingReach(Side)` — BFS over the complement of the permanent barrier, treating own non-pawn pieces as passable, undefended opposing pawns as passable (king captures them), defended opposing pawns as impassable.
- [ ] If `calculateKingReach(side)` includes any opposing pawn for either side, the verdict is not `YES`.
- [ ] Re-enable the false-positive fixture in `TestPawnWallUtility` — expect verdict ≠ `YES`:
  ```
  7k/8/1p6/1Pp5/2Pp4/pB1Pp1p1/P1B1P1P1/3B2K1 b - - 0 1
  ```
- [ ] Add the sound-`YES` fixture as a positive regression test:
  ```
  7k/8/1p6/pPp5/P1Pp4/3Pp1p1/2B1P1P1/6K1 b - - 0 1
  ```
- [ ] Audit existing `testHelperLichess(..., true)` fixtures: each must still return `YES` under the tightened rule, otherwise either it was never a sound classification or the test must accept `UNKNOWN` (or be removed).
- [ ] Performance check: BFS is O(64) per side per call — confirm the test-suite total stays within budget, otherwise restructure as a cached or per-position memoised computation.

## Caller-side rollout

`WinnableAnalyzer.java` (2 sites) and `WinnableUtilityAnalyzeChaLichess.java` (3 sites) all use the pattern

```java
if (PawnWall.calculateHasPawnWall(board)) return Winnable.NO;
```

Replace each with

```java
if (PawnWall.calculate(board) == PawnWallVerdict.YES) return Winnable.NO;
```

`NO` and `UNKNOWN` both fall through to other detection paths (CHA full, etc.), which is the same semantics those call sites have today for `false`. No behavioural change for non-pawn-wall positions; pure tightening of when `Winnable.NO` is asserted.

## Relationship to Auto-CHA

Auto-CHA after every move (the main DeepSquare item) renders this local heuristic largely redundant: a CHA-quick call already detects unwinnability soundly across all piece configurations. Two reasonable end-states:

1. **Delete the local heuristic** once Auto-CHA is wired in. The pre-CHA fast path becomes unnecessary.
2. **Keep the local heuristic as a fast pre-filter** in front of CHA-quick. The tightened sound version above is required for this — an unsound pre-filter would short-circuit CHA on positions that aren't actually unwinnable.

Decide at implementation time which path to take; the work in this document is a prerequisite for option 2 and a fallback if option 1 is delayed.
