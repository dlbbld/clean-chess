package com.dlb.chess.test.pgnall;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.test.RestrictTestConstants;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;

class TestPgnExpectedAnalysis extends AbstractPgnTest {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestPgnExpectedAnalysis.class);

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
    for (final PgnFileTestCaseList testCaseList : PgnExpectedValue.getRestrictedTestListList()) {
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        if (RestrictTestConstants.IS_RESTRICT_PGN_EXPECTED_ANALYSIS) {
          switch (testCaseList.pgnTest()) {
            case BASIC_CHECKMATE_WHITE:
            case BASIC_CHECKMATE_BLACK:
            case BASIC_STALEMATE:
            case BASIC_THREEFOLD:
            case BASIC_FIFTY:
              break;
            // $CASES-OMITTED$
            default:
              continue;
          }
        }

        logger.info(testCase.pgnFileName());
        testGame(testCaseList, testCase);
      }
    }
  }

}
