package com.dlb.chess.test.pgntest.basic;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.Nulls;
import com.dlb.chess.test.model.PgnTestCase;
import com.dlb.chess.test.model.PgnTestCaseList;
import com.dlb.chess.test.pgn.setup.PgnTestCaseCatalog;
import com.dlb.chess.test.pgntest.enums.PgnTest;

class TestBasicCheckmateVariousBlack extends AbstractTestBasic {

  private static final Logger logger = Nulls.getLogger(TestBasicCheckmateVariousBlack.class);

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
    final PgnTestCaseList testCaseList = PgnTestCaseCatalog.getTestList(PgnTest.BASIC_CHECKMATE_VARIOUS_BLACK);
    for (final PgnTestCase testCase : testCaseList.list()) {
      logger.info(testCase.pgnFileName());
      final Board board = testCase.finalPosition();
      checkCheckmate(board);
    }
  }

}
