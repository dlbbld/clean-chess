package com.dlb.chess.test.unwinnability;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.test.pgntest.enums.UnwinnableFullResultTest;
import com.dlb.chess.unwinnability.quick.UnwinnableQuickCalculator;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuick;

public class TestUnwinnabilityQuick {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestUnwinnabilityQuick.class);

  @SuppressWarnings("static-method")
  // @Test
  void testStartPosition() {
    final Board board = new Board();
    assertEquals(UnwinnableQuick.NO,
        UnwinnableQuickCalculator.unwinnableQuick(board, board.getHavingMove().getOppositeSide()));
  }

  @SuppressWarnings("static-method")
  // @Test
  void testFen() {
    final var fen = "rnbq1bnr/pppp2pp/PN6/R4k2/4pp2/5N2/1PPPPPPP/2BQKB1R b K - 5 8";
    final Board board = new Board(fen);
    assertEquals(UnwinnableQuick.NO,
        UnwinnableQuickCalculator.unwinnableQuick(board, board.getHavingMove().getOppositeSide()));
  }

  @SuppressWarnings("static-method")
  @Test
  void testPgnFile() {
    final PgnFileTestCase pgnFileTestCase = PgnExpectedValue.findPgnFileBelongingPgnTestCase("f6c1lu7R.pgn");
    final ApiBoard board = new Board(pgnFileTestCase.fen());
    logger.info(pgnFileTestCase.pgnFileName());

    assertEquals(UnwinnableQuick.MOST_LIKELY_WINNABLE,
        UnwinnableQuickCalculator.unwinnableQuick(board, board.getHavingMove().getOppositeSide()));
    // check(pgnFileTestCase.unwinnableFullResultTest(), board);
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
    final UnwinnableQuick unwinnableQuickResult = UnwinnableQuickCalculator.unwinnableQuick(board,
        board.getHavingMove().getOppositeSide());
    switch (unwinnableFullResultTest) {
      case UNWINNABLE:
        assertEquals(UnwinnableQuick.YES, unwinnableQuickResult);
        break;
      case UNWINNABLE_NOT_QUICK:
        assertEquals(UnwinnableQuick.MOST_LIKELY_WINNABLE, unwinnableQuickResult);
        break;
      case WINNABLE:

        final var isIncomplete = unwinnableQuickResult == UnwinnableQuick.NO
            || unwinnableQuickResult == UnwinnableQuick.MOST_LIKELY_WINNABLE;
        assertTrue(isIncomplete);
        break;
      default:
        throw new IllegalArgumentException();
    }
  }
}
