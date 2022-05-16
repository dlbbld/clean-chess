package com.dlb.chess.test.winnable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.utility.GeneralUtility;
import com.dlb.chess.pgn.reader.PgnReader;
import com.dlb.chess.pgn.reader.model.PgnFile;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.test.pgntest.enums.UnwinnableFullResultTest;
import com.dlb.chess.test.winnable.enums.Winnable;

public class TestWinnability {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestWinnability.class);

  @SuppressWarnings("static-method")
  @Test
  void testStartPosition() {
    final Board board = new Board();
    assertEquals(Winnable.UNKNOWN,
        WinnableCalculator.calculateWinnable(board, board.getHavingMove().getOppositeSide()));
  }

  @SuppressWarnings("static-method")
  @Test
  void testFen() {
    final var fen = "rnbq1bnr/pppp2pp/PN6/R4k2/4pp2/5N2/1PPPPPPP/2BQKB1R b K - 5 8";
    final Board board = new Board(fen);
    assertEquals(Winnable.YES, WinnableCalculator.calculateWinnable(board, board.getHavingMove().getOppositeSide()));
  }

  @SuppressWarnings("static-method")
  @Test
  void testPgnFileValue() {
    final var pgnFileName = "norgaard_pawn_wall_example_2.pgn";

    final PgnTest pgnTest = PgnExpectedValue.findPgnFileBelongingPgnTestNotHavingTestValuesAlready(pgnFileName);
    final PgnFile pgnFile = PgnReader.readPgn(pgnTest.getFolderPath(), pgnFileName);
    final ApiBoard board = GeneralUtility.calculateBoard(pgnFile);
    logger.info(pgnFileName);

    assertEquals(Winnable.NO, WinnableCalculator.calculateWinnable(board, board.getHavingMove().getOppositeSide()));
  }

  @SuppressWarnings("static-method")
  // @Test
  void testFolder() throws Exception {
    final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(PgnTest.UNFAIR_AMBRONA_EXAMPLES);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      final ApiBoard board = new Board(testCase.fen());
      logger.info(testCase.pgnFileName());

      check(testCase.unwinnableFullResultTest(), board);
    }
  }

  private static void check(UnwinnableFullResultTest unwinnableFullResultTest, ApiBoard board) {
    final Winnable winnable = WinnableCalculator.calculateWinnable(board, board.getHavingMove().getOppositeSide());
    switch (unwinnableFullResultTest) {
      case UNWINNABLE:
        assertEquals(Winnable.NO, winnable);
        break;
      case UNWINNABLE_NOT_QUICK:
        break;
      case WINNABLE:
        final var isIncomplete = winnable == Winnable.YES || winnable == Winnable.UNKNOWN;
        assertTrue(isIncomplete);
        break;
      default:
        throw new IllegalArgumentException();
    }
  }
}
