package com.dlb.chess.test.model;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.enums.InsufficientMaterial;
import com.dlb.chess.pgn.PgnUtility;
import com.dlb.chess.report.CheckmateOrStalemate;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.unwinnability.UnwinnabilityFullVerdict;
import com.dlb.chess.unwinnability.UnwinnabilityQuickVerdict;

/**
 * A single fixture row in the test corpus. The {@link #finalFen()} string is the cached final position of the PGN file named
 * by {@link #pgnFileName()}; populated when the fixture is added to {@code CreatePgnTestCases}.
 *
 * <p>
 * Two ways to materialise a board from a fixture, chosen by the test author:
 *
 * <ul>
 * <li>{@link #finalPosition()} — history-less board built from {@link #finalFen()}. <b>Cheap.</b> Use whenever the test only
 * consults the final piece arrangement and clocks (most CHA-quick / CHA-full / static-evaluation tests).
 * <li>{@link #game(PgnTest)} — full PGN replay with move history attached. <b>Expensive.</b> Use only when the test
 * genuinely needs history-derived state (repetition counts, claimable-threefold/fifty-move-rule, last-move metadata
 * such as capture/promotion/castling/en-passant, PGN export round-trips, end-to-end pipeline tests).
 * </ul>
 *
 * <p>
 * The cost difference is significant with auto-CHA on: replay pays {@code N × isUnwinnableQuick(...)} per move; a
 * position-only test that mistakenly chose {@code game(...)} scales as the number of plies in the fixture.
 */
public record PgnFileTestCase(String pgnFileName, String expectedRepetition, String expectedNoProgressMoveRule,
    int firstCapture, int maxNoProgressSequence, CheckmateOrStalemate checkmateOrStalemate,
    int repetitionCountFinalPosition, InsufficientMaterial insufficientMaterial,
    UnwinnabilityFullVerdict unwinnableFullWhite, UnwinnabilityFullVerdict unwinnableFullBlack,
    UnwinnabilityQuickVerdict unwinnableQuickWhite, UnwinnabilityQuickVerdict unwinnableQuickBlack, String finalFen) {

  /**
   * History-less board built directly from the cached FEN. Cheap — no PGN parse, no move replay. Use this whenever the
   * test only needs the final position. Dead-position auto-detection is disabled so fixtures whose final position is
   * intentionally dead can still be analysed.
   */
  public Board finalPosition() {
    return new Board(finalFen(), false);
  }

  /**
   * Full PGN replay with move history. Expensive — parses the PGN and plays every half-move. Use only when the test
   * genuinely needs history-derived state (repetition counts, claimable threefold, last-move metadata, end-to-end
   * pipeline tests). Dead-position auto-detection is disabled during replay so fixtures may pass through positions the
   * quick analyzer would classify as dead.
   *
   * <p>
   * The {@code pgnTest} argument supplies the folder; pass {@code testCaseList.pgnTest()} when iterating a test list,
   * or {@link com.dlb.chess.test.pgn.setup.CreatePgnTestCases#findPgnTest(String)} when starting from a bare test case.
   */
  public Board game(PgnTest pgnTest) {
    return PgnUtility.calculateBoard(pgnTest.getFolderPath(), pgnFileName(), false);
  }

}
