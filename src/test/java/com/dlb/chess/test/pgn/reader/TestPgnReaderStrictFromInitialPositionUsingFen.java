package com.dlb.chess.test.pgn.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.dlb.chess.pgn.reader.model.PgnFile;
import com.dlb.chess.test.pgntest.PgnTestConstants;

class TestPgnReaderStrictFromInitialPositionUsingFen extends AbstractTestPgnReader {
  private static final String PGN_TEST_FOLDER_PATH = PgnTestConstants.PGN_READER_STRICT_TEST_ROOT_FOLDER_PATH
      + "\\fromInitialPositionUsingFen";

  @SuppressWarnings("static-method")
  @Test
  void test() {

    final PgnFile expected = PgnStrictCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH, "99_original.pgn");

    {
      final PgnFile actual = PgnStrictCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH, "01_example.pgn");
      assertEquals(expected, actual);
    }
    {
      final PgnFile actual = PgnStrictCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH, "02_example.pgn");
      assertEquals(expected, actual);
    }
    {
      final PgnFile actual = PgnStrictCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH, "02_example.pgn");
      assertEquals(expected, actual);
    }
  }

}
