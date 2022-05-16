package com.dlb.chess.test.unwinnability;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.utility.PgnExtensionUtility;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.test.pgntest.enums.UnwinnableFullResultTest;
import com.dlb.chess.unwinnability.full.UnwinnableFullCalculator;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;

public class TestFindHelpMate {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestFindHelpMate.class);

  @SuppressWarnings("static-method")
  @Test
  void testFolder() throws Exception {

    final PgnFileTestCaseList testCaseHavingHelpmateList = PgnExpectedValue
        .getTestList(PgnTest.UNFAIR_LICHESS_ANALYSIS_HELPMATE);

    final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(PgnTest.UNFAIR_LICHESS_ANALYSIS_GAMES);
    for (final PgnFileTestCase testCase : testCaseList.list()) {

      if (calculateIsHavingHelpmateHavingMove(testCase, testCaseHavingHelpmateList)) {
        final ApiBoard board = new Board(testCase.fen());
        logger.info(testCase.pgnFileName());

        final UnwinnableFull unwinnableFull = UnwinnableFullCalculator.unwinnableFull(board, board.getHavingMove());

        CheckFull.check(UnwinnableFullResultTest.WINNABLE, unwinnableFull);
      }
    }
  }

  private static boolean calculateIsHavingHelpmateHavingMove(PgnFileTestCase testCase,
      PgnFileTestCaseList testCaseHavingHelpmateList) {
    for (final PgnFileTestCase pgnFileTestCaseHavingHelpmate : testCaseHavingHelpmateList.list()) {
      final String lichessIdTestCase = PgnExtensionUtility.removePgnFileExtension(testCase.pgnFileName());
      if (pgnFileTestCaseHavingHelpmate.pgnFileName().startsWith(lichessIdTestCase)) {
        return true;
      }
    }
    return false;
  }
}
