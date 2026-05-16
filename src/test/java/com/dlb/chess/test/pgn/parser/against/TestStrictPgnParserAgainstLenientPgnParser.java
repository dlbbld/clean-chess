package com.dlb.chess.test.pgn.parser.against;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.common.Nulls;
import com.dlb.chess.pgn.PgnGame;
import com.dlb.chess.test.RestrictTestConstants;
import com.dlb.chess.test.model.PgnTestCase;
import com.dlb.chess.test.model.PgnTestCaseList;
import com.dlb.chess.test.pgn.parser.PgnCacheForLenientPgnParserTestCases;
import com.dlb.chess.test.pgn.parser.PgnCacheForStrictPgnParserTestCases;
import com.dlb.chess.test.pgn.setup.PgnTestCaseCatalog;

class TestStrictPgnParserAgainstLenientPgnParser {

  private static final Logger logger = Nulls.getLogger(TestStrictPgnParserAgainstLenientPgnParser.class);

  @SuppressWarnings({ "static-method" })
  @Test
  void test() {
    // true (default) → curated parser-integration smoke subset (~45 files).
    // false → full ALL_EXCEPT_LONGEST_POSSIBLE corpus for a pre-release / regression sweep.
    final var source = RestrictTestConstants.IS_RESTRICT_PGN_STRICT_AGAINST_LENIENT_TEST
        ? PgnTestCaseCatalog.getParserIntegrationSmokeList()
        : PgnTestCaseCatalog.getRestrictedTestListList();
    for (final PgnTestCaseList testCaseList : source) {
      for (final PgnTestCase testCase : testCaseList.list()) {

        final String pgnFileName = testCase.pgnFileName();

        logger.info(pgnFileName);

        final PgnGame pgnGameStandard = PgnCacheForLenientPgnParserTestCases
            .getPgn(testCaseList.pgnTest().getFolderPath(), pgnFileName);

        final PgnGame pgnGameStrict = PgnCacheForStrictPgnParserTestCases.getPgn(testCaseList.pgnTest().getFolderPath(),
            pgnFileName);

        assertEquals(pgnGameStandard, pgnGameStrict);
      }
    }

  }

}
