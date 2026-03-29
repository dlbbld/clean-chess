package com.dlb.chess.test.pgn.parser.against;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.pgn.parser.model.PgnFile;
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
    for (final PgnFileTestCaseList testCaseList : PgnExpectedValue.getRestrictedTestListList()) {
      for (final PgnFileTestCase testCase : testCaseList.list()) {

        final String pgnFileName = testCase.pgnFileName();

        logger.info(pgnFileName);

        // if (!"03_claim_for_own_move_incorrect_castling_right_lost_for_king_move.pgn".equals(pgnFileName)) {
        // continue;
        // }

        final PgnFile pgnFileStandard = PgnCacheForLenientPgnParserTestCases.getPgn(testCaseList.pgnTest().getFolderPath(),
            pgnFileName);

        final PgnFile pgnFileStrict = PgnCacheForStrictPgnParserTestCases.getPgn(testCaseList.pgnTest().getFolderPath(),
            pgnFileName);

        assertEquals(pgnFileStandard, pgnFileStrict);
      }
    }

  }

}
