package com.dlb.chess.test.pgn.export;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.pgn.create.PgnCreate;
import com.dlb.chess.pgn.parser.LenientPgnParser;
import com.dlb.chess.pgn.parser.model.PgnFile;
import com.dlb.chess.test.RestrictTestConstants;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgn.parser.PgnCacheForLenientPgnParserTestCases;
import com.dlb.chess.test.pgntest.PgnExpectedValue;

class TestPgnExportIdempotency {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestPgnExportIdempotency.class);

  @SuppressWarnings({ "static-method" })
  @Test
  void test() {
    // true (default) → curated export-roundtrip smoke subset (~20 files).
    // false → full ALL_EXCEPT_LONGEST_POSSIBLE corpus for a pre-release / regression sweep.
    final List<PgnFileTestCaseList> source = RestrictTestConstants.IS_RESTRICT_PGN_EXPORT_IDEMPOTENCY_TEST
        ? PgnExpectedValue.getExportRoundtripSmokeList()
        : PgnExpectedValue.getRestrictedTestListList();
    for (final PgnFileTestCaseList testCaseList : source) {
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        final String pgnFileName = testCase.pgnFileName();

        logger.info(pgnFileName);

        final PgnFile importedPgn = PgnCacheForLenientPgnParserTestCases.getPgn(testCaseList.pgnTest().getFolderPath(),
            pgnFileName);

        final List<String> exportedLines = PgnCreate.createPgnFileLines(importedPgn);
        final PgnFile exportedLinesImportedPgn = LenientPgnParser.parse(exportedLines);

        assertEquals(importedPgn, exportedLinesImportedPgn);

        final List<String> exportedLinesImportedPgnExportedLines = PgnCreate
            .createPgnFileLines(exportedLinesImportedPgn);

        assertEquals(exportedLines, exportedLinesImportedPgnExportedLines);

      }
    }

  }

}
