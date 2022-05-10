package com.dlb.chess.test.pgn.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.dlb.chess.pgn.reader.enums.PgnReaderValidationProblem;
import com.dlb.chess.pgn.reader.model.PgnFile;
import com.dlb.chess.test.pgntest.PgnTestConstants;

class TestPgnReaderResult extends AbstractTestPgnReaderException {
  private static final String PGN_TEST_FOLDER_PATH = PgnTestConstants.PGN_READER_TEST_ROOT_FOLDER_PATH + "\\result";

  @SuppressWarnings("static-method")
  @Test
  void test() {

    {
      final PgnFile expected = PgnStrictCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH, "99_original_ongoing.pgn");
      final PgnFile actual = PgnCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH,
          "01_no_result_tag_no_termination_tag.pgn");
      assertEquals(expected, actual);
    }
    {
      final PgnFile expected = PgnStrictCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH, "98_original_win.pgn");
      final PgnFile actual = PgnCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH,
          "02_no_result_tag_has_termination_tag.pgn");
      assertEquals(expected, actual);
    }
    {
      final PgnFile expected = PgnStrictCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH, "98_original_win.pgn");
      final PgnFile actual = PgnCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH,
          "03_has_result_tag_no_termination_tag.pgn");
      assertEquals(expected, actual);
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testException() {

    checkReadPgnException("04_has_result_tag_has_termination_tag_different.pgn",
        PgnReaderValidationProblem.TAG_RESULT_BOTH_SET_BUT_DIFFERENT);
    checkReadPgnException("05_has_result_tag_has_termination_tag_different.pgn",
        PgnReaderValidationProblem.TAG_RESULT_BOTH_SET_BUT_DIFFERENT);
    checkReadPgnException("06_has_result_tag_has_termination_tag_different.pgn",
        PgnReaderValidationProblem.TAG_RESULT_BOTH_SET_BUT_DIFFERENT);
  }

  private static void checkReadPgnException(String pgnFileName, PgnReaderValidationProblem expected) {
    checkReadPgnException(PGN_TEST_FOLDER_PATH, pgnFileName, expected);
  }
}