package com.dlb.chess.test.pgn.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.dlb.chess.pgn.reader.model.PgnFile;
import com.dlb.chess.test.pgntest.PgnTestConstants;

class TestPgnReaderFromCustomPosition extends AbstractTestPgnReader {
  private static final String PGN_TEST_FOLDER_PATH = PgnTestConstants.PGN_READER_TEST_ROOT_FOLDER_PATH
      + "\\fromCustomPosition";

  @SuppressWarnings("static-method")
  @Test
  void testWhite() {

    final PgnFile expected = PgnStrictCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH, "98_white_start_original.pgn");

    {
      final PgnFile actual = PgnCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH, "01_white_start.pgn");
      assertEquals(expected, actual);
    }
    {
      final PgnFile actual = PgnCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH, "02_white_start.pgn");
      assertEquals(expected, actual);
    }
    {
      final PgnFile actual = PgnCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH, "03_white_start.pgn");
      assertEquals(expected, actual);
    }
    {
      final PgnFile actual = PgnCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH, "04_white_start.pgn");
      assertEquals(expected, actual);
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testBlack() {

    final PgnFile expected = PgnStrictCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH, "99_black_start_original.pgn");

    {
      final PgnFile actual = PgnCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH, "05_black_start.pgn");
      assertEquals(expected, actual);
    }
    {
      final PgnFile actual = PgnCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH, "06_black_start.pgn");
      assertEquals(expected, actual);
    }
    {
      final PgnFile actual = PgnCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH, "07_black_start.pgn");
      assertEquals(expected, actual);
    }
    {
      final PgnFile actual = PgnCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH, "08_black_start.pgn");
      assertEquals(expected, actual);
    }
  }

}
