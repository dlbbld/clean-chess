package com.dlb.chess.test.pgn.export;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.utility.FileUtility;
import com.dlb.chess.pgn.create.PgnCreate;
import com.dlb.chess.pgn.reader.model.PgnFile;
import com.dlb.chess.test.pgn.reader.PgnStrictCacheForTestCases;
import com.dlb.chess.test.pgntest.PgnTestConstants;

class TestPgnExportLineLength {

  private static final String TEST_FOLDER_PATH = PgnTestConstants.PGN_EXPORT_TEST_ROOT_FOLDER_PATH + "\\lineLength80";

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestPgnExportLineLength.class);

  @SuppressWarnings("static-method")
  @Test
  void test() throws IOException {
    checkFile("almtwali_vs_danielbaechli_2020.pgn");
    checkFile("blatny_holzke_1997.pgn");
    checkFile("gvetadze_milliet_2014.pgn");

  }

  private static void checkFile(String pgnFileName) throws IOException {

    logger.info(pgnFileName);

    final PgnFile pgnFileFromFileSystem = PgnStrictCacheForTestCases.getPgn(TEST_FOLDER_PATH, pgnFileName);

    final List<String> fileLinesExpectedFromFileSystem = FileUtility.readFileLines(TEST_FOLDER_PATH, pgnFileName);
    final List<String> fileLinesActualFromPgn = PgnCreate.createPgnFileLines(pgnFileFromFileSystem);

    assertEquals(fileLinesExpectedFromFileSystem, fileLinesActualFromPgn);
  }
}
