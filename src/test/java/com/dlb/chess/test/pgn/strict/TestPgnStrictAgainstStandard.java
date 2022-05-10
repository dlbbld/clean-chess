package com.dlb.chess.test.pgn.strict;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.pgn.reader.model.PgnFile;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgn.reader.PgnCacheForTestCases;
import com.dlb.chess.test.pgn.reader.PgnStrictCacheForTestCases;
import com.dlb.chess.test.pgntest.PgnExpectedValue;

class TestPgnStrictAgainstStandard {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestPgnStrictAgainstStandard.class);

  @SuppressWarnings({ "static-method" })
  @Test
  void test() {
    for (final PgnFileTestCaseList testCaseList : PgnExpectedValue.getRestrictedTestListList()) {
      for (final PgnFileTestCase testCase : testCaseList.list()) {

        final String pgnFileName = testCase.pgnFileName();

        logger.info(pgnFileName);

        // if (!"2_4_2_lasker_alekhine_1914.pgn".equals(pgnFileName)) {
        // continue;
        // }

        final PgnFile pgnFileStandard = PgnCacheForTestCases.getPgn(testCaseList.pgnTest().getFolderPath(),
            pgnFileName);

        final PgnFile pgnFileStrict = PgnStrictCacheForTestCases.getPgn(testCaseList.pgnTest().getFolderPath(),
            pgnFileName);

        assertEquals(pgnFileStandard, pgnFileStrict);
      }
    }

  }

}
