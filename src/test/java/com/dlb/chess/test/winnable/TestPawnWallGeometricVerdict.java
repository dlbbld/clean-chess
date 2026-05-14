package com.dlb.chess.test.winnable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;

/**
 * Verifies the geometric {@link PawnWall#calculate(Board)} verdict against the {@link PawnWallKingWalkOracle} BFS
 * second-opinion check.
 *
 * <p>
 * Contract (see {@code pawn-wall-soundness.md}):
 *
 * <ul>
 * <li>{@code geometric_YES} ⟹ {@code BFS_YES} (soundness — must hold).</li>
 * <li>{@code BFS_YES} ⟹ {@code geometric_YES} is <em>not</em> required; the geometric check is allowed to be
 * conservative.</li>
 * </ul>
 *
 * <p>
 * Each fixture below is a position where the production geometric check should classify as {@link
 * PawnWallVerdict#YES} <em>and</em> the BFS oracle confirms neither king can reach an opposing pawn.
 */
class TestPawnWallGeometricVerdict {

  @SuppressWarnings("static-method")
  @Test
  void testHorizontalPawnWall() {
    // Rank 3 is fully covered: own pawns on b3/d3/f3/h3, attacked squares a3/c3/e3/g3 (Black pawns on b4/d4/f4/h4).
    // White king on e1, Black king on e8 - both far from the wall, no piece can breach.
    final Board board = new Board("4k3/8/8/p1p1p1p1/PpPpPpPp/1P1P1P1P/8/4K3 w - - 0 1");

    assertGeometricAndBfsAgreeOnYes(board);
  }

  @SuppressWarnings("static-method")
  @Test
  void testZigZagPawnWall() {
    // Chain a3-b3-c3-d3-d4-e4-e5-f5-f6-g6-h6 using only horizontal/vertical adjacencies. White king on e1 reaches
    // up to g5/h5 on the kingside but no further; queenside sealed at rank 3.
    final Board board = new Board("4k3/5p1p/4pP1P/3pP3/p1pP4/P1P5/8/4K3 w - - 0 1");

    assertGeometricAndBfsAgreeOnYes(board);
  }

  /**
   * Asserts the geometric check returns {@link PawnWallVerdict#YES} on this fixture <em>and</em> the BFS oracle
   * confirms both kings are trapped behind a permanent barrier. Test failure means either the geometric check
   * accepted a position the BFS does not, or the test corpus is wrong.
   */
  private static void assertGeometricAndBfsAgreeOnYes(Board board) {
    assertEquals(PawnWallVerdict.YES, PawnWall.calculate(board), "geometric check must classify this fixture as YES");
    assertTrue(PawnWallKingWalkOracle.isKingTrappedBehindPermanentBarrier(board, Side.WHITE),
        "BFS oracle: White king must be trapped behind the wall");
    assertTrue(PawnWallKingWalkOracle.isKingTrappedBehindPermanentBarrier(board, Side.BLACK),
        "BFS oracle: Black king must be trapped behind the wall");
  }

}
