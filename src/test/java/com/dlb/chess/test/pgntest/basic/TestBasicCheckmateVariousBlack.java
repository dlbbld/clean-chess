package com.dlb.chess.test.pgntest.basic;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.Nulls;
import com.dlb.chess.pgn.PgnUtility;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgn.setup.CreatePgnTestCases;
import com.dlb.chess.test.pgntest.enums.PgnTest;

class TestBasicCheckmateVariousBlack extends AbstractTestBasic {

  private static final Logger logger = Nulls.getLogger(TestBasicCheckmateVariousBlack.class);

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
    final PgnFileTestCaseList testCaseList = CreatePgnTestCases.getTestList(PgnTest.BASIC_CHECKMATE_VARIOUS_BLACK);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      logger.info(testCase.pgnFileName());
      final Board board = PgnUtility.calculateBoard(testCaseList.pgnTest().getFolderPath(), testCase.pgnFileName(), false);
      checkCheckmate(board);
    }
  }

}
