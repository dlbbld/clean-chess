package com.dlb.chess.test.pgnall;

import org.junit.jupiter.api.Test;

import com.dlb.chess.analysis.Analyzer;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;

class TestPgnExpectedValue extends AbstractPgnTest {

  // for custom testing
  // order of priority as listed
  private static final boolean IS_CHECK_FROM_PGN_TEST = false;
  private static final PgnTest CHECK_FROM_PGN_TEST = PgnTest.BASIC_CAPTURE_WHITE;

  private static final boolean IS_CHECK_ONLY_PGN_TEST = false;
  private static final PgnTest CHECK_ONLY_PGN_TEST = PgnTest.BASIC_CAPTURE_WHITE;

  private static final boolean IS_CHECK_FROM_PGN_FILE_NAME = false;
  private static final String CHECK_PGN_FILE_NAME = "pUEeHLfu.pgn";

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
    var isFromOrAfterFromFolder = false;
    var isPgnFileNameOrAfterPgnFileName = false;
    for (final PgnFileTestCaseList testCaseList : PgnExpectedValue.getRestrictedTestListList()) {
      if (!isFromOrAfterFromFolder && CHECK_FROM_PGN_TEST == testCaseList.pgnTest()) {
        isFromOrAfterFromFolder = true;
      }

      if (isContinueDirectoryLevel(IS_CHECK_FROM_PGN_TEST, IS_CHECK_ONLY_PGN_TEST, isFromOrAfterFromFolder,
          testCaseList.pgnTest())) {
        continue;
      }

      for (final PgnFileTestCase testCase : testCaseList.list()) {
        if (!isPgnFileNameOrAfterPgnFileName && CHECK_PGN_FILE_NAME.equals(testCase.pgnFileName())) {
          isPgnFileNameOrAfterPgnFileName = true;
        }

        if (isContinueFileLevel(IS_CHECK_FROM_PGN_TEST, IS_CHECK_ONLY_PGN_TEST, IS_CHECK_FROM_PGN_FILE_NAME,
            isPgnFileNameOrAfterPgnFileName)) {
          continue;
        }

        testGame(testCaseList, testCase);
      }
    }
  }

  // must be in method with parameters for constants to avoid dead code warnings
  private static boolean isContinueDirectoryLevel(boolean isCheckFromPgnTest, boolean isCheckOnlyPgnTest,
      boolean isFromOrAfterFromFolder, PgnTest currentPgnTest) {
    if (isCheckFromPgnTest) {
      if (!isFromOrAfterFromFolder) {
        return true;
      }
    } else if (isCheckOnlyPgnTest && CHECK_ONLY_PGN_TEST != currentPgnTest) {
      return true;
    }
    return false;
  }

  // must be in method with parameters for constants to avoid dead code warnings
  private static boolean isContinueFileLevel(boolean isCheckFromPgnTest, boolean isCheckOnlyPgnTest,
      boolean isCheckFromPgnFileName, boolean isPgnFileNameOrAfterPgnFileName) {
    if (!isCheckFromPgnTest && !isCheckOnlyPgnTest && isCheckFromPgnFileName) {
      return !isPgnFileNameOrAfterPgnFileName;
    }
    return false;
  }

  private static void testGame(PgnFileTestCaseList testCaseList, PgnFileTestCase testCase) throws Exception {

    final var analysis = Analyzer.calculateAnalysis(testCaseList.pgnTest().getFolderPath(), testCase.pgnFileName());
    testGame(testCase, analysis);
  }

}
