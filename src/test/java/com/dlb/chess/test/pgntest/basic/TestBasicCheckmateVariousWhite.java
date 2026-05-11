package com.dlb.chess.test.pgntest.basic;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.board.Board;
import com.dlb.chess.common.utility.GeneralUtility;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgn.setup.CreatePgnTestCases;
import com.dlb.chess.test.pgntest.enums.PgnTest;

class TestBasicCheckmateVariousWhite extends AbstractTestBasic {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestBasicCheckmateVariousWhite.class);

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
    final PgnFileTestCaseList testCaseList = CreatePgnTestCases.getTestList(PgnTest.BASIC_CHECKMATE_VARIOUS_WHITE);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      logger.info(testCase.pgnFileName());
      final Board board = GeneralUtility.calculateBoard(testCaseList.pgnTest().getFolderPath(),
          testCase.pgnFileName());
      checkCheckmate(board);
    }
  }

}
