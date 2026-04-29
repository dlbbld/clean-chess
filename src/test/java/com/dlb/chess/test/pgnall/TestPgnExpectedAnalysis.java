package com.dlb.chess.test.pgnall;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.test.RestrictTestConstants;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;

/**
 * Analyzes PGN test cases and validates expected values. Default scope (when
 * {@link RestrictTestConstants#IS_RESTRICT_PGN_EXPECTED_ANALYSIS} is {@code true}) is restricted
 * to a fast smoke subset suitable for routine CI runs:
 *
 * <ul>
 * <li>only basic {@link com.dlb.chess.test.pgntest.enums.PgnTest} categories (skips SPECIAL,
 *     UNFAIR_*, etc. — non-basic categories are slower and only useful for full release
 *     validation);</li>
 * <li>at most {@link #MAX_FILES_PER_FOLDER} files per category.</li>
 * </ul>
 *
 * <p>
 * To widen scope locally, flip {@link RestrictTestConstants#IS_RESTRICT_PGN_EXPECTED_ANALYSIS} to
 * {@code false} (or the master gate {@code IS_RESTRICT_PGN}) and the full restricted corpus runs.
 */
class TestPgnExpectedAnalysis extends AbstractPgnTest {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestPgnExpectedAnalysis.class);

  /** Cap on files tested per category when the smoke restriction is active. */
  private static final int MAX_FILES_PER_FOLDER = 2;

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
    for (final PgnFileTestCaseList testCaseList : PgnExpectedValue.getRestrictedTestListList()) {
      if (RestrictTestConstants.IS_RESTRICT_PGN_EXPECTED_ANALYSIS && !testCaseList.pgnTest().getIsBasicTest()) {
        continue;
      }

      var filesInFolder = 0;
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        if (RestrictTestConstants.IS_RESTRICT_PGN_EXPECTED_ANALYSIS && filesInFolder >= MAX_FILES_PER_FOLDER) {
          break;
        }
        logger.info(testCase.pgnFileName());
        testGame(testCaseList, testCase);
        filesInFolder++;
      }
    }
  }

}
