package com.dlb.chess.test.pgn.parser.against;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.pgn.PgnFile;
import com.dlb.chess.test.RestrictTestConstants;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgn.parser.PgnCacheForLenientPgnParserTestCases;
import com.dlb.chess.test.pgn.parser.PgnCacheForStrictPgnParserTestCases;
import com.dlb.chess.test.pgntest.PgnExpectedValue;

class TestStrictPgnParserAgainstLenientPgnParser {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestStrictPgnParserAgainstLenientPgnParser.class);

  @SuppressWarnings({ "static-method" })
  @Test
  void test() {
    // true (default) → curated parser-integration smoke subset (~45 files).
    // false → full ALL_EXCEPT_LONGEST_POSSIBLE corpus for a pre-release / regression sweep.
    final var source = RestrictTestConstants.IS_RESTRICT_PGN_STRICT_AGAINST_LENIENT_TEST
        ? PgnExpectedValue.getParserIntegrationSmokeList()
        : PgnExpectedValue.getRestrictedTestListList();
    for (final PgnFileTestCaseList testCaseList : source) {
      for (final PgnFileTestCase testCase : testCaseList.list()) {

        final String pgnFileName = testCase.pgnFileName();

        logger.info(pgnFileName);

        final PgnFile pgnFileStandard = PgnCacheForLenientPgnParserTestCases
            .getPgn(testCaseList.pgnTest().getFolderPath(), pgnFileName);

        final PgnFile pgnFileStrict = PgnCacheForStrictPgnParserTestCases.getPgn(testCaseList.pgnTest().getFolderPath(),
            pgnFileName);

        assertEquals(pgnFileStandard, pgnFileStrict);
      }
    }

  }

}
