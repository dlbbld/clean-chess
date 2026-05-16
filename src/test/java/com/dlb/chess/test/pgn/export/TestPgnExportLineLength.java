package com.dlb.chess.test.pgn.export;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.common.Nulls;
import com.dlb.chess.pgn.PgnCreate;
import com.dlb.chess.pgn.PgnGame;
import com.dlb.chess.test.common.utility.FileUtility;
import com.dlb.chess.test.pgn.parser.PgnCacheForStrictPgnParserTestCases;
import com.dlb.chess.test.pgntest.constants.PgnTestConstants;

class TestPgnExportLineLength {

  private static final Path TEST_FOLDER_PATH = Nulls.pathResolve(PgnTestConstants.PGN_EXPORT_TEST_ROOT_FOLDER_PATH,
      "lineLength");

  private static final Logger logger = Nulls.getLogger(TestPgnExportLineLength.class);

  @SuppressWarnings("static-method")
  @Test
  void test() {
    final List<String> pgnFileNameList = calculatePgnNameList();
    assertFalse(pgnFileNameList.isEmpty(), "The PGN export line-length test folder must contain PGN files");

    for (final String pgnFileName : pgnFileNameList) {
      checkFile(pgnFileName);
    }
  }

  private static List<String> calculatePgnNameList() {
    final List<String> result = new ArrayList<>();

    for (final String fileName : FileUtility.readFileNameList(TEST_FOLDER_PATH)) {
      if (fileName.endsWith(".pgn")) {
        result.add(fileName);
      }
    }
    result.sort(String::compareTo);
    return result;
  }

  private static void checkFile(String pgnFileName) {

    logger.info(pgnFileName);

    final List<String> fileLinesExpectedFromFileSystem = FileUtility.readFileLines(TEST_FOLDER_PATH, pgnFileName);

    final PgnGame pgnGameFromFileSystem = PgnCacheForStrictPgnParserTestCases.getPgn(TEST_FOLDER_PATH, pgnFileName);
    final List<String> fileLinesActualFromPgn = PgnCreate.createPgnLines(pgnGameFromFileSystem);
    assertEquals(fileLinesExpectedFromFileSystem, fileLinesActualFromPgn);
  }
}
