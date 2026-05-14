package com.dlb.chess.test.winnable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.pgn.setup.CreatePgnTestCases;
import com.dlb.chess.test.pgntest.enums.PgnTest;

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
 * The two hand-coded tests below pin the soundness model with the textbook horizontal-wall and zig-zag-wall fixtures
 * from {@code pawn-wall-soundness.md}. The folder iteration runs the same agreement check across the full
 * {@code src/test/resources/pgn/cha/pawnWall} corpus (encoded in {@link PgnTest#CHA_PAWN_WALL}) — every fixture where
 * the geometric check returns {@link PawnWallVerdict#YES} must independently pass the BFS oracle for both sides.
 */
class TestPawnWallGeometricVerdict {

  @SuppressWarnings("static-method")
  @Test
  void testHorizontalPawnWall() {
    // Rank 3 is fully covered: own pawns on b3/d3/f3/h3, attacked squares a3/c3/e3/g3 (Black pawns on b4/d4/f4/h4).
    // White king on e1, Black king on e8 - both far from the wall, no piece can breach.
    final Board board = new Board("4k3/8/8/p1p1p1p1/PpPpPpPp/1P1P1P1P/8/4K3 w - - 0 1");

    assertGeometricAndBfsAgreeOnYes(board, "textbook horizontal wall");
  }

  @SuppressWarnings("static-method")
  @Test
  void testZigZagPawnWall() {
    // Chain a3-b3-c3-d3-d4-e4-e5-f5-f6-g6-h6 using only horizontal/vertical adjacencies. White king on e1 reaches
    // up to g5/h5 on the kingside but no further; queenside sealed at rank 3.
    final Board board = new Board("4k3/5p1p/4pP1P/3pP3/p1pP4/P1P5/8/4K3 w - - 0 1");

    assertGeometricAndBfsAgreeOnYes(board, "textbook zig-zag wall");
  }

  @SuppressWarnings("static-method")
  @Test
  void testAllPawnWallFolderFixtures() {
    for (final PgnFileTestCase testCase : CreatePgnTestCases.getTestList(PgnTest.CHA_PAWN_WALL).list()) {
      final Board board = new Board(testCase.fen());
      // Asymmetric contract: only YES verdicts must agree with the BFS oracle. NO/UNKNOWN fixtures are skipped -
      // the geometric check is allowed to be conservative there, and Auto-CHA / CHA-quick covers them at a higher
      // level.
      if (PawnWall.calculate(board) != PawnWallVerdict.YES) {
        continue;
      }
      assertTrue(PawnWallKingWalkOracle.isKingTrappedBehindPermanentBarrier(board, Side.WHITE),
          "Geometric YES but BFS says White king is not trapped: " + testCase.pgnFileName() + " - " + testCase.fen());
      assertTrue(PawnWallKingWalkOracle.isKingTrappedBehindPermanentBarrier(board, Side.BLACK),
          "Geometric YES but BFS says Black king is not trapped: " + testCase.pgnFileName() + " - " + testCase.fen());
    }
  }

  /**
   * Asserts the geometric check returns {@link PawnWallVerdict#YES} on this fixture <em>and</em> the BFS oracle
   * confirms both kings are trapped behind a permanent barrier. Test failure means either the geometric check
   * accepted a position the BFS does not, or the test corpus is wrong.
   */
  private static void assertGeometricAndBfsAgreeOnYes(Board board, String label) {
    assertEquals(PawnWallVerdict.YES, PawnWall.calculate(board),
        label + ": geometric check must classify this fixture as YES");
    assertTrue(PawnWallKingWalkOracle.isKingTrappedBehindPermanentBarrier(board, Side.WHITE),
        label + ": BFS oracle - White king must be trapped behind the wall");
    assertTrue(PawnWallKingWalkOracle.isKingTrappedBehindPermanentBarrier(board, Side.BLACK),
        label + ": BFS oracle - Black king must be trapped behind the wall");
  }

}
