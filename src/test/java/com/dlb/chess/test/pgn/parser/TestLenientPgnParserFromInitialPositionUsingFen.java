package com.dlb.chess.test.pgn.parser;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.Nulls;
import com.dlb.chess.pgn.PgnGame;
import com.dlb.chess.test.pgntest.constants.PgnTestConstants;

class TestLenientPgnParserFromInitialPositionUsingFen extends AbstractTestLenientPgnParser {
  private static final Path PGN_TEST_FOLDER_PATH = Nulls
      .pathResolve(PgnTestConstants.LENIENT_PGN_PARSER_TEST_ROOT_FOLDER_PATH, "fromInitialPositionUsingFen");

  @SuppressWarnings("static-method")
  @Test
  void test() {

    final PgnGame expected = PgnCacheForLenientPgnParserTestCases.getPgn(PGN_TEST_FOLDER_PATH, "99_original.pgn");

    {
      final PgnGame actual = PgnCacheForLenientPgnParserTestCases.getPgn(PGN_TEST_FOLDER_PATH, "01_example.pgn");
      assertEqualsArchival(expected, actual);
    }
    {
      final PgnGame actual = PgnCacheForLenientPgnParserTestCases.getPgn(PGN_TEST_FOLDER_PATH, "02_example.pgn");
      assertEqualsArchival(expected, actual);
    }
    {
      final PgnGame actual = PgnCacheForLenientPgnParserTestCases.getPgn(PGN_TEST_FOLDER_PATH, "02_example.pgn");
      assertEqualsArchival(expected, actual);
    }
  }

}
