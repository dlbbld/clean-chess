package com.dlb.chess.test.pgn.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.pgn.parser.model.PgnFile;
import com.dlb.chess.test.pgntest.PgnTestConstants;

class TestLenientPgnParserResult extends AbstractTestLenientPgnParserException {
  private static final Path PGN_TEST_FOLDER_PATH = NonNullWrapperCommon
      .resolve(PgnTestConstants.LENIENT_PGN_PARSER_TEST_ROOT_FOLDER_PATH, "result");

  @SuppressWarnings("static-method")
  @Test
  void test() {

    {
      final PgnFile expected = PgnCacheForStrictPgnParserTestCases.getPgn(PGN_TEST_FOLDER_PATH,
          "99_original_ongoing.pgn");
      final PgnFile actual = PgnCacheForLenientPgnParserTestCases.getPgn(PGN_TEST_FOLDER_PATH,
          "01_no_result_tag_no_termination_tag.pgn");
      assertEquals(expected, actual);
    }
    {
      final PgnFile expected = PgnCacheForStrictPgnParserTestCases.getPgn(PGN_TEST_FOLDER_PATH, "98_original_win.pgn");
      final PgnFile actual = PgnCacheForLenientPgnParserTestCases.getPgn(PGN_TEST_FOLDER_PATH,
          "02_no_result_tag_has_termination_tag.pgn");
      assertEquals(expected, actual);
    }
    {
      final PgnFile expected = PgnCacheForStrictPgnParserTestCases.getPgn(PGN_TEST_FOLDER_PATH, "98_original_win.pgn");
      final PgnFile actual = PgnCacheForLenientPgnParserTestCases.getPgn(PGN_TEST_FOLDER_PATH,
          "03_has_result_tag_no_termination_tag.pgn");
      assertEquals(expected, actual);
    }
  }

}