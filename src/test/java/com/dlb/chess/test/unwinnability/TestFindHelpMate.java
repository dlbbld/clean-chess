package com.dlb.chess.test.unwinnability;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.utility.PgnExtensionUtility;
import com.dlb.chess.test.PrintDuration;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.test.pgntest.enums.UnwinnableFullResultTest;
import com.dlb.chess.unwinnability.full.UnwinnableFullAnalyzer;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;

public class TestFindHelpMate {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestFindHelpMate.class);

  private static final boolean IS_START_FROM_PGN_FILE = false;
  private static final String START_FROM_PGN_FILE_NAME = "m5WDCW16.pgn";

  @SuppressWarnings("static-method")
  // @Test
  void testFolder() throws Exception {
    final List<Long> milliSecondsList = new ArrayList<>();

    var hasFound = false;
    final PgnFileTestCaseList testCaseHavingHelpmateList = PgnExpectedValue
        .getTestList(PgnTest.UNFAIR_LICHESS_ANALYSIS_HELPMATE);

    final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(PgnTest.UNFAIR_LICHESS_ANALYSIS_GAMES);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      if (!hasFound) {
        if (IS_START_FROM_PGN_FILE) {
          if (START_FROM_PGN_FILE_NAME.equals(testCase.pgnFileName())) {
            hasFound = true;
          }
        } else {
          hasFound = true;
        }
      }
      if (!hasFound) {
        continue;
      }

      if (calculateIsHavingHelpmateHavingMove(testCase, testCaseHavingHelpmateList)) {
        final ApiBoard board = new Board(testCase.fen());
        logger.info(testCase.pgnFileName());

        final var beforeMilliSeconds = System.currentTimeMillis();
        final UnwinnableFull unwinnableFull = UnwinnableFullAnalyzer.unwinnableFull(board, board.getHavingMove());
        final var durationMilliSeconds = System.currentTimeMillis() - beforeMilliSeconds;
        milliSecondsList.add(durationMilliSeconds);
        PrintDuration.printDuration(milliSecondsList, logger);

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
