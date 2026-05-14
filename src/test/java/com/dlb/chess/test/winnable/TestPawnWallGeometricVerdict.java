package com.dlb.chess.test.winnable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.pgn.setup.CreatePgnTestCases;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.unwinnability.UnwinnableQuick;
import com.dlb.chess.unwinnability.UnwinnableQuickAnalyzer;

/**
 * Tests the geometric {@link PawnWallGeometricVerdict#calculate(Board)} verdict on the pawn-wall PGN corpus, with two independent
 * second opinions:
 *
 * <ol>
 * <li>{@link PawnWallKingWalkOracle} — verifies the king-walk reading of the position (neither king can reach the
 * opposing king's square through any sequence of legal moves).</li>
 * <li>{@link UnwinnableQuickAnalyzer} — Ambrona's quick unwinnability check; verifies the position is unwinnable
 * for both sides.</li>
 * </ol>
 *
 * <p>
 * Contract (see {@code pawn-wall-soundness.md}):
 *
 * <ul>
 * <li>{@code geometric_YES} ⟹ {@code BFS_YES} (king-walk soundness — must hold).</li>
 * <li>{@code geometric_YES} ⟹ {@code UnwinnableQuick == UNWINNABLE} for both sides (Ambrona-confirmed unwinnability —
 * must hold).</li>
 * <li>The reverse implications are not required; both second opinions are allowed to detect more positions than the
 * geometric check.</li>
 * </ul>
 *
 * <p>
 * The {@code UnwinnableQuick} cross-check is the main soundness gate. Per the user's framing: the pawn-wall
 * detection is a geometric side product — Ambrona's analyzer is the canonical unwinnability oracle. If the
 * geometric check says YES on a position where {@code UnwinnableQuick} disagrees, the geometric check has a false
 * positive worth investigating.
 */
class TestPawnWallGeometricVerdict {

  /**
   * Expected number of fixtures in {@link PgnTest#CHA_PAWN_WALL}. Pinned so test failures distinguish
   * "corpus loader broke" from "geometric check regressed."
   */
  private static final int EXPECTED_FIXTURE_COUNT = 26;

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
  void testAmbronaTenIsRejected() {
    // ambrona_10 - WINNABLE per CHA-full despite an apparent permanent barrier. The geometric chain
    // a5-b5-b4-c4-c3-d3-d2-e2-f2-g2-h2 is pawn/attack-only and the king-walk BFS confirms trapped, but the
    // helpmate exists: the king captures the undefended a3 pawn (routing through the c2/b3 bishop squares) and
    // White's a2 pawn marches to promotion. The all-pawns-involved check rejects this position because a2 and a3
    // are not part of any spanning chain - they are floating pawns.
    final Board board = new Board("7k/8/1p6/1Pp5/2Pp4/pB1Pp1p1/P1B1P1P1/3B2K1 b - - 0 1");

    assertEquals(PawnWallVerdict.UNKNOWN, PawnWallGeometricVerdict.calculate(board),
        "ambrona_10 has a floating a2/a3 pawn pair - must be rejected by the all-pawns-involved check");
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
    final List<PgnFileTestCase> fixtures = CreatePgnTestCases.getTestList(PgnTest.CHA_PAWN_WALL).list();
    assertEquals(EXPECTED_FIXTURE_COUNT, fixtures.size(),
        "pawnWall corpus must have " + EXPECTED_FIXTURE_COUNT + " fixtures - if this fails, the corpus loader is "
            + "broken or fixtures were added/removed");

    final List<String> yesFiles = new ArrayList<>();
    for (final PgnFileTestCase testCase : fixtures) {
      final Board board = new Board(testCase.fen());
      // Asymmetric contract: only YES verdicts get cross-checked. NO/UNKNOWN fixtures are skipped - both the BFS
      // oracle and UnwinnableQuick are allowed to detect more positions than the conservative geometric check.
      if (PawnWallGeometricVerdict.calculate(board) != PawnWallVerdict.YES) {
        continue;
      }
      yesFiles.add(testCase.pgnFileName());
      assertTrue(PawnWallKingWalkOracle.isKingTrappedBehindPermanentBarrier(board, Side.WHITE),
          "Geometric YES but BFS says White king is not trapped: " + testCase.pgnFileName() + " - " + testCase.fen());
      assertTrue(PawnWallKingWalkOracle.isKingTrappedBehindPermanentBarrier(board, Side.BLACK),
          "Geometric YES but BFS says Black king is not trapped: " + testCase.pgnFileName() + " - " + testCase.fen());
      assertEquals(UnwinnableQuick.UNWINNABLE, UnwinnableQuickAnalyzer.unwinnableQuick(board, Side.WHITE),
          "Geometric YES but UnwinnableQuick is not UNWINNABLE for White: " + testCase.pgnFileName() + " - "
              + testCase.fen());
      assertEquals(UnwinnableQuick.UNWINNABLE, UnwinnableQuickAnalyzer.unwinnableQuick(board, Side.BLACK),
          "Geometric YES but UnwinnableQuick is not UNWINNABLE for Black: " + testCase.pgnFileName() + " - "
              + testCase.fen());
    }
    // A meaningful subset of the corpus must return YES, otherwise the test passes vacuously (e.g. if the geometric
    // check regressed to always returning UNKNOWN).
    assertTrue(yesFiles.size() >= 5,
        "expected at least 5 geometric-YES fixtures (got " + yesFiles.size() + ": " + yesFiles + ")");
    // The bishop fixtures specifically must remain in the YES set - per the user, color-locked bishops behind a
    // genuine pawn wall are a valid case we want to keep detecting (the wall is sound; the bishop is harmless
    // because it can never reach an opposing pawn).
    assertTrue(yesFiles.contains("pawn_wall_bishop_1.pgn"),
        "pawn_wall_bishop_1 must remain in the geometric-YES set (color-locked bishop, valid wall). Got: " + yesFiles);
    // The classic textbook walls must be in the YES set.
    assertTrue(yesFiles.contains("pawn_wall_horizontal_1.pgn"),
        "pawn_wall_horizontal_1 must remain in the geometric-YES set. Got: " + yesFiles);
  }

  /**
   * Asserts the geometric check returns {@link PawnWallVerdict#YES} on this fixture <em>and</em> the BFS oracle
   * confirms both kings are trapped behind a permanent barrier. Test failure means either the geometric check
   * accepted a position the BFS does not, or the test corpus is wrong.
   */
  private static void assertGeometricAndBfsAgreeOnYes(Board board, String label) {
    assertEquals(PawnWallVerdict.YES, PawnWallGeometricVerdict.calculate(board),
        label + ": geometric check must classify this fixture as YES");
    assertTrue(PawnWallKingWalkOracle.isKingTrappedBehindPermanentBarrier(board, Side.WHITE),
        label + ": BFS oracle - White king must be trapped behind the wall");
    assertTrue(PawnWallKingWalkOracle.isKingTrappedBehindPermanentBarrier(board, Side.BLACK),
        label + ": BFS oracle - Black king must be trapped behind the wall");
  }

}
