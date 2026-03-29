package com.dlb.chess.test.pgn.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.pgn.parser.model.PgnFile;
import com.dlb.chess.test.pgntest.PgnTestConstants;

class TestLenientPgnParserFromCustomPosition extends AbstractTestLenientPgnParser {
  private static final Path PGN_TEST_FOLDER_PATH = NonNullWrapperCommon
      .resolve(PgnTestConstants.LENIENT_PGN_PARSER_TEST_ROOT_FOLDER_PATH, "fromCustomPosition");

  @SuppressWarnings("static-method")
  @Test
  void testWhite() {

    final PgnFile expected = PgnCacheForStrictPgnParserTestCases.getPgn(PGN_TEST_FOLDER_PATH, "98_white_start_original.pgn");

    {
      final PgnFile actual = PgnCacheForLenientPgnParserTestCases.getPgn(PGN_TEST_FOLDER_PATH, "01_white_start.pgn");
      assertEquals(expected, actual);
    }
    {
      final PgnFile actual = PgnCacheForLenientPgnParserTestCases.getPgn(PGN_TEST_FOLDER_PATH, "02_white_start.pgn");
      assertEquals(expected, actual);
    }
    {
      final PgnFile actual = PgnCacheForLenientPgnParserTestCases.getPgn(PGN_TEST_FOLDER_PATH, "03_white_start.pgn");
      assertEquals(expected, actual);
    }
    {
      final PgnFile actual = PgnCacheForLenientPgnParserTestCases.getPgn(PGN_TEST_FOLDER_PATH, "04_white_start.pgn");
      assertEquals(expected, actual);
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testBlack() {

    final PgnFile expected = PgnCacheForStrictPgnParserTestCases.getPgn(PGN_TEST_FOLDER_PATH, "99_black_start_original.pgn");

    {
      final PgnFile actual = PgnCacheForLenientPgnParserTestCases.getPgn(PGN_TEST_FOLDER_PATH, "05_black_start.pgn");
      assertEquals(expected, actual);
    }
    {
      final PgnFile actual = PgnCacheForLenientPgnParserTestCases.getPgn(PGN_TEST_FOLDER_PATH, "06_black_start.pgn");
      assertEquals(expected, actual);
    }
    {
      final PgnFile actual = PgnCacheForLenientPgnParserTestCases.getPgn(PGN_TEST_FOLDER_PATH, "07_black_start.pgn");
      assertEquals(expected, actual);
    }
    {
      final PgnFile actual = PgnCacheForLenientPgnParserTestCases.getPgn(PGN_TEST_FOLDER_PATH, "08_black_start.pgn");
      assertEquals(expected, actual);
    }
  }

}
