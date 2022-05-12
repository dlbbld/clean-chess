package com.dlb.chess.test.unwinnability;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.logging.log4j.Logger;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.unwinnability.quick.UnwinnableQuick;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuickResult;

public class TestUnwinnabilityQuick {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestUnwinnabilityQuick.class);

  @SuppressWarnings("static-method")
  // @Test
  void testSingle() {
    // final Board board = new Board("5r1k/6P1/7K/5q2/8/8/8/8 b - - 0 51");
    final Board board = new Board("rnbq1bnr/pppp2pp/PN6/R4k2/4pp2/5N2/1PPPPPPP/2BQKB1R b K - 5 8");
    assertEquals(UnwinnableQuickResult.WINNABLE,
        UnwinnableQuick.unwinnableQuick(board, board.getHavingMove().getOppositeSide()));
  }

  @SuppressWarnings("static-method")
  // @Test
  void testFolder() throws Exception {
    final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(PgnTest.LICHESS_NOT_QUICK);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      final ApiBoard board = new Board(testCase.fen());
      logger.info(testCase.pgnFileName());

      assertEquals(UnwinnableQuickResult.POSSIBLY_WINNABLE,
          UnwinnableQuick.unwinnableQuick(board, board.getHavingMove().getOppositeSide()));
    }
  }

}
