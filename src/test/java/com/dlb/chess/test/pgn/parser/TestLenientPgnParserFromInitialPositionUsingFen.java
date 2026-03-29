package com.dlb.chess.test.pgn.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.pgn.parser.model.PgnFile;
import com.dlb.chess.test.pgntest.PgnTestConstants;

class TestLenientPgnParserFromInitialPositionUsingFen extends AbstractTestLenientPgnParser {
  private static final Path PGN_TEST_FOLDER_PATH = NonNullWrapperCommon
      .resolve(PgnTestConstants.LENIENT_PGN_PARSER_TEST_ROOT_FOLDER_PATH, "fromInitialPositionUsingFen");

  @SuppressWarnings("static-method")
  @Test
  void test() {

    final PgnFile expected = PgnCacheForLenientPgnParserTestCases.getPgn(PGN_TEST_FOLDER_PATH, "99_original.pgn");

    {
      final PgnFile actual = PgnCacheForLenientPgnParserTestCases.getPgn(PGN_TEST_FOLDER_PATH, "01_example.pgn");
      assertEquals(expected, actual);
    }
    {
      final PgnFile actual = PgnCacheForLenientPgnParserTestCases.getPgn(PGN_TEST_FOLDER_PATH, "02_example.pgn");
      assertEquals(expected, actual);
    }
    {
      final PgnFile actual = PgnCacheForLenientPgnParserTestCases.getPgn(PGN_TEST_FOLDER_PATH, "02_example.pgn");
      assertEquals(expected, actual);
    }
  }

}
