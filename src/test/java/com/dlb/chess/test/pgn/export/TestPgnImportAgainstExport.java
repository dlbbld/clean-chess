package com.dlb.chess.test.pgn.export;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.pgn.PgnCreate;
import com.dlb.chess.pgn.LenientPgnParser;
import com.dlb.chess.pgn.PgnFile;
import com.dlb.chess.test.RestrictTestConstants;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgn.parser.PgnCacheForLenientPgnParserTestCases;
import com.dlb.chess.test.pgntest.PgnExpectedValue;

class TestPgnImportAgainstExport {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestPgnImportAgainstExport.class);

  @SuppressWarnings({ "static-method" })
  @Test
  void test() {
    // true (default) → curated export-roundtrip smoke subset (~20 files).
    // false → full ALL_EXCEPT_LONGEST_POSSIBLE corpus for a pre-release / regression sweep.
    final var source = RestrictTestConstants.IS_RESTRICT_PGN_WRITER_TEST
        ? PgnExpectedValue.getExportRoundtripSmokeList()
        : PgnExpectedValue.getRestrictedTestListList();
    for (final PgnFileTestCaseList testCaseList : source) {
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        final String pgnFileName = testCase.pgnFileName();

        logger.info(pgnFileName);

        final PgnFile pgnFileFromFileSystem = PgnCacheForLenientPgnParserTestCases
            .getPgn(testCaseList.pgnTest().getFolderPath(), pgnFileName);

        final List<String> export = PgnCreate.createPgnFileLines(pgnFileFromFileSystem);
        final PgnFile pgnFileFromReadingExport = LenientPgnParser.parse(export);

        assertEquals(pgnFileFromFileSystem, pgnFileFromReadingExport);
      }
    }

  }

}
