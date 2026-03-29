package com.dlb.chess.test.pgn.export;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.utility.FileUtility;
import com.dlb.chess.pgn.create.PgnCreate;
import com.dlb.chess.pgn.parser.model.PgnFile;
import com.dlb.chess.test.pgn.parser.PgnCacheForStrictPgnParserTestCases;
import com.dlb.chess.test.pgntest.PgnTestConstants;

class TestPgnExportLineLength {

  private static final Path TEST_FOLDER_PATH = NonNullWrapperCommon
      .resolve(PgnTestConstants.PGN_EXPORT_TEST_ROOT_FOLDER_PATH, "lineLength");

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestPgnExportLineLength.class);

  @SuppressWarnings("static-method")
  @Test
  void test() {
    checkFile("almtwali_vs_danielbaechli_2020.pgn");
    checkFile("blatny_holzke_1997.pgn");
    checkFile("gvetadze_milliet_2014.pgn");

  }

  private static void checkFile(String pgnFileName) {

    logger.info(pgnFileName);

    final List<String> fileLinesExpectedFromFileSystem = FileUtility.readFileLines(TEST_FOLDER_PATH, pgnFileName);

    final PgnFile pgnFileFromFileSystem = PgnCacheForStrictPgnParserTestCases.getPgn(TEST_FOLDER_PATH, pgnFileName);
    final List<String> fileLinesActualFromPgn = PgnCreate.createPgnFileLines(pgnFileFromFileSystem);
    assertEquals(fileLinesExpectedFromFileSystem, fileLinesActualFromPgn);
  }
}
