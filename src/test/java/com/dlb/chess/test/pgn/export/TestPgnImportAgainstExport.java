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

class TestPgnImportAgainstExport {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestPgnImportAgainstExport.class);

  @SuppressWarnings({ "static-method" })
  @Test
  void test() {
    for (final PgnFileTestCaseList testCaseList : PgnExpectedValue.getRestrictedTestListList()) {
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        if (PgnTestConstants.IS_RESTRICT_PGN_WRITER_TEST && !testCaseList.pgnTest().getIsBasicTest()) {
          continue;
        }

        final String pgnFileName = testCase.pgnFileName();

        logger.info(pgnFileName);

        final PgnFile pgnFileFromFileSystem = PgnCacheForTestCases.getPgn(testCaseList.pgnTest().getFolderPath(),
            pgnFileName);

        final List<String> export = PgnCreate.createPgnFileLines(pgnFileFromFileSystem);
        final PgnFile pgnFileFromReadingExport = PgnReader.readPgn(export);

        assertEquals(pgnFileFromFileSystem, pgnFileFromReadingExport);
      }
    }

  }

}
