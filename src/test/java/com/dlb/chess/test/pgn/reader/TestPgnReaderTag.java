package com.dlb.chess.test.pgn.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.pgn.reader.enums.ResultTagValue;
import com.dlb.chess.pgn.reader.model.PgnFile;
import com.dlb.chess.test.pgntest.PgnTestConstants;
import com.dlb.chess.utility.TagUtility;

class TestPgnReaderTag extends AbstractTestPgnReader {
  private static final String PGN_TEST_FOLDER_PATH = PgnTestConstants.PGN_READER_NON_STRICT_TEST_ROOT_FOLDER_PATH
      + "\\tag";

  @SuppressWarnings("static-method")
  @Test
  void testResultTagOnly() {

    final PgnFile actual = PgnStrictCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH, "99_original.pgn");

    final PgnFile expected = PgnCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH, "01_result_tag_only.pgn");

    assertTrue(TagUtility.hasResult(expected.tagList()));
    assertEquals(ResultTagValue.ONGOING.getValue(), TagUtility.readResult(expected.tagList()));

    assertEqualsButTagList(actual, expected);
  }

  @SuppressWarnings("static-method")
  @Test
  void testNoSpaceBetweenTagNameAndValue() {

    final PgnFile actual = PgnStrictCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH, "99_original.pgn");

    final PgnFile expected = PgnCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH,
        "02_no_space_between_tag_name_and_value.pgn");

    assertEquals(actual, expected);
  }

  @SuppressWarnings("static-method")
  @Test
  void testTooMuchSpaces() {

    final PgnFile actual = PgnStrictCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH, "99_original.pgn");

    final PgnFile expected = PgnCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH, "03_too_much_space_in_tag.pgn");

    assertEquals(actual, expected);
  }

  @SuppressWarnings("static-method")
  @Test
  void testIgnoreSetUpTag() {

    final PgnFile actual = PgnStrictCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH, "99_original.pgn");

    {
      final PgnFile expected = PgnCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH, "04_setup_tag_zero_remove.pgn");
      assertEquals(actual, expected);
    }
    {
      final PgnFile expected = PgnCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH, "05_setup_tag_one_remove.pgn");
      assertEquals(actual, expected);
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testTabs() {
    final PgnFile actual = PgnStrictCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH, "99_original.pgn");

    {
      final PgnFile expected = PgnCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH, "06_having_tabs.pgn");
      assertEquals(actual, expected);
    }
    {
      final PgnFile expected = PgnCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH, "07_having_tabs.pgn");
      assertEquals(actual, expected);
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testEmptyLines() {

    final PgnFile actual = PgnStrictCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH, "99_original.pgn");

    {
      final PgnFile expected = PgnCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH, "08_empty_lines.pgn");
      assertEquals(actual, expected);
    }
    {
      final PgnFile expected = PgnCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH, "09_empty_lines.pgn");
      assertEquals(actual, expected);
    }

  }

  @SuppressWarnings("static-method")
  @Test
  void testNoTags() {
    final PgnFile actual = PgnStrictCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH, "98_original.pgn");

    final PgnFile expected = PgnCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH, "10_empty_tag_list.pgn");
    assertEqualsButTagListAndResult(actual, expected);
  }

}
