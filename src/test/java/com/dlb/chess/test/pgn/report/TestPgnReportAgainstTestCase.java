package com.dlb.chess.test.pgn.report;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.common.Nulls;
import com.dlb.chess.test.RestrictTestConstants;
import com.dlb.chess.test.model.PgnTestCase;
import com.dlb.chess.test.model.PgnTestCaseList;
import com.dlb.chess.test.pgn.setup.PgnTestCaseCatalog;

/**
 * Analyzes PGN test cases and validates expected values. Default scope (when
 * {@link RestrictTestConstants#IS_RESTRICT_PGN_REPORT_TEST} is {@code true}) is restricted to a fast smoke subset
 * suitable for routine CI runs:
 *
 * <p>
 * To widen scope locally, flip {@link RestrictTestConstants#IS_RESTRICT_PGN_REPORT_TEST} to {@code false} (or the
 * master gate {@code IS_RESTRICT_PGN}) and the full restricted corpus runs.
 */
class TestPgnReportAgainstTestCase extends AbstractPgnReportTest {

  private static final Logger logger = Nulls.getLogger(TestPgnReportAgainstTestCase.class);

  /** Cap on files tested per category when the smoke restriction is active. */
  private static final int MAX_FILES_PER_FOLDER = 1;

  // "unused" suppresses Eclipse JDT's "Dead code" warning that fires on the IS_RESTRICT_PGN_REPORT_TEST gate:
  // the constant resolves at runtime (Boolean.getBoolean) but Eclipse's flow analysis treats the gate as
  // statically true in the default configuration, marking the continue / break bodies as unreachable.
  @SuppressWarnings({ "static-method" })
  @Test
  void test() throws Exception {
    for (final PgnTestCaseList testCaseList : PgnTestCaseCatalog.getRestrictedTestListList()) {
      if (RestrictTestConstants.IS_RESTRICT_PGN_REPORT_TEST && !testCaseList.pgnTest().getIsBasicTest()) {
        continue;
      }

      var processedFilesInFolder = 0;
      for (final PgnTestCase testCase : testCaseList.list()) {
        if (RestrictTestConstants.IS_RESTRICT_PGN_REPORT_TEST && processedFilesInFolder >= MAX_FILES_PER_FOLDER) {
          break;
        }
        logger.info(testCase.pgnName());
        testReportAgainstTestCase(testCaseList, testCase);
        processedFilesInFolder++;
      }
    }
  }

}
