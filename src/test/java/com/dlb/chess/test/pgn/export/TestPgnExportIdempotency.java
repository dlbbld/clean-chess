package com.dlb.chess.test.pgn.export;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.pgn.create.PgnCreate;
import com.dlb.chess.pgn.reader.PgnReader;
import com.dlb.chess.pgn.reader.model.PgnFile;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgn.reader.PgnCacheForTestCases;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.PgnTestConstants;

class TestPgnExportIdempotency {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestPgnExportIdempotency.class);

  @SuppressWarnings({ "static-method" })
  @Test
  void test() {
    for (final PgnFileTestCaseList testCaseList : PgnExpectedValue.getRestrictedTestListList()) {
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        if (PgnTestConstants.IS_RESTRICT_PGN_EXPORT_IDEMPOTENCY_TEST && !testCaseList.pgnTest().getIsBasicTest()) {
          continue;
        }

        final String pgnFileName = testCase.pgnFileName();

        logger.info(pgnFileName);

        final PgnFile importedPgn = PgnCacheForTestCases.getPgn(testCaseList.pgnTest().getFolderPath(), pgnFileName);

        final List<String> exportedLines = PgnCreate.createPgnFileLines(importedPgn);
        final PgnFile exportedLinesImportedPgn = PgnReader.readPgn(exportedLines);

        assertEquals(importedPgn, exportedLinesImportedPgn);

        final List<String> exportedLinesImportedPgnExportedLines = PgnCreate.createPgnFileLines(exportedLinesImportedPgn);

        assertEquals(exportedLines, exportedLinesImportedPgnExportedLines);

      }
    }

  }

}
