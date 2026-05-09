package com.dlb.chess.test.pgn.report;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.test.RestrictTestConstants;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgnall.AbstractPgnTest;
import com.dlb.chess.test.pgntest.PgnExpectedValue;

/**
 * Analyzes PGN test cases and validates expected values. Default scope (when
 * {@link RestrictTestConstants#IS_RESTRICT_PGN_EXPECTED_ANALYSIS} is {@code true}) is restricted to a fast smoke subset
 * suitable for routine CI runs:
 *
 * <p>
 * To widen scope locally, flip {@link RestrictTestConstants#IS_RESTRICT_PGN_EXPECTED_ANALYSIS} to {@code false} (or the
 * master gate {@code IS_RESTRICT_PGN}) and the full restricted corpus runs.
 */
class TestPgnReportAgainstTestCase extends AbstractPgnTest {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestPgnReportAgainstTestCase.class);

  /** Cap on files tested per category when the smoke restriction is active. */
  private static final int MAX_FILES_PER_FOLDER = 2;

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
    for (final PgnFileTestCaseList testCaseList : PgnExpectedValue.getRestrictedTestListList()) {
      if (RestrictTestConstants.IS_RESTRICT_PGN_EXPECTED_ANALYSIS && !testCaseList.pgnTest().getIsBasicTest()) {
        continue;
      }

      var processedFilesInFolder = 0;
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        if (RestrictTestConstants.IS_RESTRICT_PGN_EXPECTED_ANALYSIS && processedFilesInFolder >= MAX_FILES_PER_FOLDER) {
          break;
        }
        logger.info(testCase.pgnFileName());
        testReportAgainstTestCase(testCaseList, testCase);
        processedFilesInFolder++;
      }
    }
  }

}
