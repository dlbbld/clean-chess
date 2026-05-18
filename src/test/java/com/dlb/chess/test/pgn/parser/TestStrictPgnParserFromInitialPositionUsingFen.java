package com.dlb.chess.test.pgn.parser;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.Nulls;
import com.dlb.chess.pgn.PgnGame;
import com.dlb.chess.test.pgntest.constants.PgnTestConstants;

class TestStrictPgnParserFromInitialPositionUsingFen extends AbstractTestLenientPgnParser {
  private static final Path PGN_TEST_FOLDER_PATH = Nulls
      .pathResolve(PgnTestConstants.STRICT_PGN_PARSER_TEST_ROOT_FOLDER_PATH, "fromInitialPositionUsingFen");

  @SuppressWarnings("static-method")
  @Test
  void test() {

    final PgnGame expected = PgnCacheForStrictPgnParserTestCases.getPgn(PGN_TEST_FOLDER_PATH, "99_original.pgn");

    {
      final PgnGame actual = PgnCacheForStrictPgnParserTestCases.getPgn(PGN_TEST_FOLDER_PATH, "01_example.pgn");
      assertEqualsArchival(expected, actual);
    }
    {
      final PgnGame actual = PgnCacheForStrictPgnParserTestCases.getPgn(PGN_TEST_FOLDER_PATH, "02_example.pgn");
      assertEqualsArchival(expected, actual);
    }
    {
      final PgnGame actual = PgnCacheForStrictPgnParserTestCases.getPgn(PGN_TEST_FOLDER_PATH, "02_example.pgn");
      assertEqualsArchival(expected, actual);
    }
  }

}
