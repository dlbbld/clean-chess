package com.dlb.chess.test.common.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.dlb.chess.enums.MoveSuffixAnnotation;
import com.dlb.chess.pgn.reader.enums.PgnReaderStrictValidationProblem;
import com.dlb.chess.pgn.reader.exceptions.PgnReaderStrictValidationException;
import com.dlb.chess.pgn.reader.model.PgnFile;
import com.dlb.chess.test.pgn.reader.PgnStrictCacheForTestCases;
import com.dlb.chess.test.pgntest.PgnTestConstants;

class TestMoveTextUtilityUsingPgn extends AbstractTestMovetextUtility {

  private static final Path PGN_CUSTOM_TEST_FOLDER_PATH = PgnTestConstants.PGN_READER_STRICT_TEST_ROOT_FOLDER_PATH
      .resolve("movementSpecification");

  private static final Path PGN_TEST_LEADING_COMMENTARY_EXCEPTION_FOLDER_PATH = PGN_CUSTOM_TEST_FOLDER_PATH
      .resolve("commentary/leadingCommentary/exception");

  @SuppressWarnings("static-method")
  @Test
  void testLeadingCommentaryException() {
    checkLeadingCommentaryException("01_movetext_commentary_ends_after_start_brace.pgn",
        PgnReaderStrictValidationProblem.MOVETEXT_COMMENTARY_ENDS_AFTER_START_BRACE);
    checkLeadingCommentaryException("02_1_movetext_commentary_start_brace_not_followed_by_end_brace.pgn",
        PgnReaderStrictValidationProblem.MOVETEXT_COMMENTARY_START_BRACE_NOT_FOLLOWED_BY_END_BRACE);
    checkLeadingCommentaryException("02_1_movetext_commentary_start_brace_not_followed_by_end_brace.pgn",
        PgnReaderStrictValidationProblem.MOVETEXT_COMMENTARY_START_BRACE_NOT_FOLLOWED_BY_END_BRACE);
    checkLeadingCommentaryException("02_3_movetext_commentary_start_brace_not_followed_by_end_brace.pgn",
        PgnReaderStrictValidationProblem.MOVETEXT_COMMENTARY_START_BRACE_NOT_FOLLOWED_BY_END_BRACE);
    checkLeadingCommentaryException("03_movetext_commentary_not_followed_by_space.pgn",
        PgnReaderStrictValidationProblem.MOVETEXT_COMMENTARY_NOT_FOLLOWED_BY_SPACE);
    checkLeadingCommentaryException("04_movetext_commentary_followed_by_space_but_ending.pgn",
        PgnReaderStrictValidationProblem.MOVETEXT_COMMENTARY_FOLLOWED_BY_SPACE_BUT_ENDING);
    checkLeadingCommentaryException("05_movetext_commentary_followed_by_two_spaces.pgn",
        PgnReaderStrictValidationProblem.MOVETEXT_COMMENTARY_FOLLOWED_BY_TWO_SPACES);

    // here for initial commentary MOVETEXT_COMMENTARY_NOT_FOLLOWED_BY_VALUE is
    // MOVETEXT_COMMENTARY_FOLLOWED_BY_SPACE_BUT_ENDING (later having higher precedence)
    checkLeadingCommentaryException("06_movetext_commentary_not_followed_by_value.pgn",
        PgnReaderStrictValidationProblem.MOVETEXT_COMMENTARY_FOLLOWED_BY_SPACE_BUT_ENDING);

    checkLeadingCommentaryException("07_movetext_commentary_end_brace_without_start_brace.pgn",
        PgnReaderStrictValidationProblem.MOVETEXT_COMMENTARY_END_BRACE_WITHOUT_START_BRACE);
  }

  private static void checkLeadingCommentaryException(String pgnFileName,
      PgnReaderStrictValidationProblem expectedProblem) {
    var isException = false;
    try {
      PgnStrictCacheForTestCases.getPgn(PGN_TEST_LEADING_COMMENTARY_EXCEPTION_FOLDER_PATH, pgnFileName);
    } catch (final PgnReaderStrictValidationException pre) {
      assertEquals(expectedProblem, pre.getPgnReaderStrictValidationProblem());
      isException = true;
    }
    assertTrue(isException);
  }

  private static final Path PGN_TEST_NON_LEADING_COMMENTARY_EXCEPTION_FOLDER_PATH = PGN_CUSTOM_TEST_FOLDER_PATH
      .resolve("commentary/nonLeadingCommentary/exception");

  @SuppressWarnings("static-method")
  void testNonLeadingCommentaryException() {
    checkNonLeadingCommentaryException("01_movetext_commentary_ends_after_start_brace.pgn",
        PgnReaderStrictValidationProblem.MOVETEXT_COMMENTARY_ENDS_AFTER_START_BRACE);
    checkNonLeadingCommentaryException("02_1_movetext_commentary_start_brace_not_followed_by_end_brace.pgn",
        PgnReaderStrictValidationProblem.MOVETEXT_COMMENTARY_START_BRACE_NOT_FOLLOWED_BY_END_BRACE);
    checkNonLeadingCommentaryException("02_2_movetext_commentary_start_brace_not_followed_by_end_brace.pgn",
        PgnReaderStrictValidationProblem.MOVETEXT_COMMENTARY_START_BRACE_NOT_FOLLOWED_BY_END_BRACE);
    checkNonLeadingCommentaryException("02_3_movetext_commentary_start_brace_not_followed_by_end_brace.pgn",
        PgnReaderStrictValidationProblem.MOVETEXT_COMMENTARY_START_BRACE_NOT_FOLLOWED_BY_END_BRACE);
    checkNonLeadingCommentaryException("02_4_movetext_commentary_start_brace_not_followed_by_end_brace.pgn",
        PgnReaderStrictValidationProblem.MOVETEXT_COMMENTARY_START_BRACE_NOT_FOLLOWED_BY_END_BRACE);
    checkNonLeadingCommentaryException("03_1_movetext_commentary_not_followed_by_space.pgn",
        PgnReaderStrictValidationProblem.MOVETEXT_COMMENTARY_NOT_FOLLOWED_BY_SPACE);
    checkNonLeadingCommentaryException("03_2_movetext_commentary_not_followed_by_space.pgn",
        PgnReaderStrictValidationProblem.MOVETEXT_COMMENTARY_NOT_FOLLOWED_BY_SPACE);
    checkNonLeadingCommentaryException("04_movetext_commentary_followed_by_space_but_ending.pgn",
        PgnReaderStrictValidationProblem.MOVETEXT_COMMENTARY_FOLLOWED_BY_SPACE_BUT_ENDING);
    checkNonLeadingCommentaryException("05_1_movetext_commentary_followed_by_two_spaces.pgn",
        PgnReaderStrictValidationProblem.MOVETEXT_COMMENTARY_FOLLOWED_BY_TWO_SPACES);
    checkNonLeadingCommentaryException("05_2_movetext_commentary_followed_by_two_spaces.pgn",
        PgnReaderStrictValidationProblem.MOVETEXT_COMMENTARY_FOLLOWED_BY_TWO_SPACES);
    checkNonLeadingCommentaryException("06_movetext_commentary_not_followed_by_value.pgn",
        PgnReaderStrictValidationProblem.MOVETEXT_COMMENTARY_NOT_FOLLOWED_BY_VALUE);
    checkNonLeadingCommentaryException("07_movetext_commentary_start_brace_not_followed_by_end_brace.pgn",
        PgnReaderStrictValidationProblem.MOVETEXT_COMMENTARY_START_BRACE_NOT_FOLLOWED_BY_END_BRACE);
    checkNonLeadingCommentaryException("07_1_movetext_commentary_end_brace_without_start_brace.pgn",
        PgnReaderStrictValidationProblem.MOVETEXT_COMMENTARY_END_BRACE_WITHOUT_START_BRACE);
    checkNonLeadingCommentaryException("07_2_movetext_commentary_end_brace_without_start_brace.pgn",
        PgnReaderStrictValidationProblem.MOVETEXT_COMMENTARY_END_BRACE_WITHOUT_START_BRACE);
    checkNonLeadingCommentaryException("07_3_movetext_commentary_end_brace_without_start_brace.pgn",
        PgnReaderStrictValidationProblem.MOVETEXT_COMMENTARY_END_BRACE_WITHOUT_START_BRACE);
  }

  private static void checkNonLeadingCommentaryException(String pgnFileName,
      PgnReaderStrictValidationProblem expectedProblem) {
    var isException = false;
    try {
      PgnStrictCacheForTestCases.getPgn(PGN_TEST_NON_LEADING_COMMENTARY_EXCEPTION_FOLDER_PATH, pgnFileName);
    } catch (final PgnReaderStrictValidationException pre) {
      assertEquals(expectedProblem, pre.getPgnReaderStrictValidationProblem());
      isException = true;
    }
    assertTrue(isException);
  }

  private static final Path PGN_TEST_MOVE_SUFFIX_ANNOTATION_EXCEPTION_FOLDER_PATH = PGN_CUSTOM_TEST_FOLDER_PATH
      .resolve("moveSuffixAnnotation/exception");

  @SuppressWarnings({ "static-method" })
  @Test
  void testMoveSuffixAnnotationException() {
    checkMoveSuffixAnnotationException("01_example.pgn",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_SUFFIX_ANNOTATION_INVALID);
    checkMoveSuffixAnnotationException("02_example.pgn",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_SUFFIX_ANNOTATION_INVALID);
    checkMoveSuffixAnnotationException("03_example.pgn",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_SUFFIX_ANNOTATION_INVALID);
  }

  private static void checkMoveSuffixAnnotationException(String pgnFileName,
      PgnReaderStrictValidationProblem expectedProblem) {
    var isException = false;
    try {
      PgnStrictCacheForTestCases.getPgn(PGN_TEST_MOVE_SUFFIX_ANNOTATION_EXCEPTION_FOLDER_PATH, pgnFileName);
    } catch (final PgnReaderStrictValidationException pre) {
      assertEquals(expectedProblem, pre.getPgnReaderStrictValidationProblem());
      isException = true;
    }
    assertTrue(isException);
  }

  private static final Path PGN_TEST_COMBINED_EXCEPTION_FOLDER_PATH = PGN_CUSTOM_TEST_FOLDER_PATH
      .resolve("combined/exception");

  @SuppressWarnings({ "static-method" })
  @Test
  void testCombinedException() {
    checkCombinedException("01_example.pgn", PgnReaderStrictValidationProblem.MOVETEXT_SAN_CHARACTER_INVALID);
    checkCombinedException("02_example.pgn", PgnReaderStrictValidationProblem.MOVETEXT_MOVE_SUFFIX_ANNOTATION_INVALID);
    checkCombinedException("03_example.pgn",
        PgnReaderStrictValidationProblem.MOVETEXT_COMMENTARY_START_BRACE_NOT_FOLLOWED_BY_END_BRACE);
  }

  private static void checkCombinedException(String pgnFileName, PgnReaderStrictValidationProblem expectedProblem) {
    var isException = false;
    try {
      PgnStrictCacheForTestCases.getPgn(PGN_TEST_COMBINED_EXCEPTION_FOLDER_PATH, pgnFileName);
    } catch (final PgnReaderStrictValidationException pre) {
      assertEquals(expectedProblem, pre.getPgnReaderStrictValidationProblem());
      isException = true;
    }
    assertTrue(isException);
  }

  private static final Path PGN_TEST_LEADING_COMMENTARY_SUCCESS_FOLDER_PATH = PGN_CUSTOM_TEST_FOLDER_PATH
      .resolve("commentary/leadingCommentary/success");

  @SuppressWarnings("static-method")
  @Test
  void testLeadingCommentarySuccess() {
    checkLeadingCommentarySuccess("01_example.pgn", "comment");
    checkLeadingCommentarySuccess("02_example.pgn", " comment");
    checkLeadingCommentarySuccess("03_example.pgn", "comment ");
    checkLeadingCommentarySuccess("04_example.pgn", "first comment across multiple lines");
    checkLeadingCommentarySuccess("05_example.pgn", "second comment across multiple lines");
    checkLeadingCommentarySuccess("06_example.pgn", "third comment across multiple lines");
    checkLeadingCommentarySuccess("07_example.pgn", "fourth comment across multiple lines");
    checkLeadingCommentarySuccess("08_example.pgn", "fifth comment across multiple lines");
    checkLeadingCommentarySuccess("09_example_whitespace.pgn", " sixth  comment across  multiple  lines ");
    checkLeadingCommentarySuccess("10_example_left_curly_bracket_in_comment.pgn",
        "{comment acr{oss multiple{{li{nes with left curly brackets{");
  }

  private static void checkLeadingCommentarySuccess(String pgnFileName, String leadingCommentary) {
    final PgnFile pgnFile = PgnStrictCacheForTestCases.getPgn(PGN_TEST_LEADING_COMMENTARY_SUCCESS_FOLDER_PATH,
        pgnFileName);
    assertEquals(leadingCommentary, pgnFile.leadingCommentary());
  }

  private static final Path PGN_TEST_NON_LEADING_COMMENTARY_SUCCESS_FOLDER_PATH = PGN_CUSTOM_TEST_FOLDER_PATH
      .resolve("commentary/nonLeadingCommentary/success");

  @SuppressWarnings({ "static-method", "null" })
  @Test
  void testNonLeadingCommentarySuccess() {
    checkNonLeadingCommentarySuccess("01_example.pgn", Arrays.asList("", "", "", ""));

    checkNonLeadingCommentarySuccess("02_1_example.pgn", Arrays.asList("comment1", "", "", ""));
    checkNonLeadingCommentarySuccess("02_2_example.pgn", Arrays.asList("", "comment2", "", ""));
    checkNonLeadingCommentarySuccess("02_3_example.pgn", Arrays.asList("", "", "comment3", ""));
    checkNonLeadingCommentarySuccess("02_4_example.pgn", Arrays.asList("", "", "", "comment4"));

    checkNonLeadingCommentarySuccess("03_1_example.pgn", Arrays.asList("comment1", "comment2", "", ""));
    checkNonLeadingCommentarySuccess("03_2_example.pgn", Arrays.asList("comment1", "", "comment3", ""));
    checkNonLeadingCommentarySuccess("03_3_example.pgn", Arrays.asList("comment1", "", "", "comment4"));
    checkNonLeadingCommentarySuccess("03_4_example.pgn", Arrays.asList("", "comment2", "comment3", ""));
    checkNonLeadingCommentarySuccess("03_5_example.pgn", Arrays.asList("", "comment2", "", "comment4"));
    checkNonLeadingCommentarySuccess("03_6_example.pgn", Arrays.asList("", "", "comment3", "comment4"));

    checkNonLeadingCommentarySuccess("04_1_example.pgn", Arrays.asList("comment1", "comment2", "comment3", ""));
    checkNonLeadingCommentarySuccess("04_2_example.pgn", Arrays.asList("comment1", "comment2", "", "comment4"));
    checkNonLeadingCommentarySuccess("04_3_example.pgn", Arrays.asList("comment1", "", "comment3", "comment4"));
    checkNonLeadingCommentarySuccess("04_4_example.pgn", Arrays.asList("", "comment2", "comment3", "comment4"));

    checkNonLeadingCommentarySuccess("05_1_example.pgn", Arrays.asList("comment1", "comment2", "comment3", "comment4"));

    checkNonLeadingCommentarySuccess("06_example_whitespace.pgn",
        Arrays.asList(" comment comment", "comment  comment", "comment comment ", " comment  comment "));

    checkNonLeadingCommentarySuccess("07_example_left_curly_bracket_in_comment.pgn",
        Arrays.asList("{comment1", "com{ment2", "comment3{", "{com{ment4{"));
  }

  private static void checkNonLeadingCommentarySuccess(String pgnFileName, List<String> commentaryListExpected) {
    final PgnFile pgnFile = PgnStrictCacheForTestCases.getPgn(PGN_TEST_NON_LEADING_COMMENTARY_SUCCESS_FOLDER_PATH,
        pgnFileName);

    assertEquals("", pgnFile.leadingCommentary());

    assertEquals(commentaryListExpected, calculateCommentaryList(pgnFile.halfMoveList()));
  }

  private static final Path PGN_TEST_COMBINED_COMMENTARY_SUCCESS_FOLDER_PATH = PGN_CUSTOM_TEST_FOLDER_PATH
      .resolve("commentary/combinedCommentary/success");

  @SuppressWarnings({ "static-method", "null" })
  @Test
  void testCombinedCommentarySuccess() {
    checkCombinedCommentarySuccess("01_example.pgn", "leading commentary", Arrays.asList("", "", "", ""));

    checkCombinedCommentarySuccess("02_1_example.pgn", "leading commentary", Arrays.asList("comment1", "", "", ""));
    checkCombinedCommentarySuccess("02_2_example.pgn", "leading commentary", Arrays.asList("", "comment2", "", ""));
    checkCombinedCommentarySuccess("02_3_example.pgn", "leading commentary", Arrays.asList("", "", "comment3", ""));
    checkCombinedCommentarySuccess("02_4_example.pgn", "leading commentary", Arrays.asList("", "", "", "comment4"));

    checkCombinedCommentarySuccess("03_1_example.pgn", "leading commentary",
        Arrays.asList("comment1", "comment2", "", ""));
    checkCombinedCommentarySuccess("03_2_example.pgn", "leading commentary",
        Arrays.asList("comment1", "", "comment3", ""));
    checkCombinedCommentarySuccess("03_3_example.pgn", "leading commentary",
        Arrays.asList("comment1", "", "", "comment4"));
    checkCombinedCommentarySuccess("03_4_example.pgn", "leading commentary",
        Arrays.asList("", "comment2", "comment3", ""));
    checkCombinedCommentarySuccess("03_5_example.pgn", "leading commentary",
        Arrays.asList("", "comment2", "", "comment4"));
    checkCombinedCommentarySuccess("03_6_example.pgn", "leading commentary",
        Arrays.asList("", "", "comment3", "comment4"));

    checkCombinedCommentarySuccess("04_1_example.pgn", "leading commentary",
        Arrays.asList("comment1", "comment2", "comment3", ""));
    checkCombinedCommentarySuccess("04_2_example.pgn", "leading commentary",
        Arrays.asList("comment1", "comment2", "", "comment4"));
    checkCombinedCommentarySuccess("04_3_example.pgn", "leading commentary",
        Arrays.asList("comment1", "", "comment3", "comment4"));
    checkCombinedCommentarySuccess("04_4_example.pgn", "leading commentary",
        Arrays.asList("", "comment2", "comment3", "comment4"));

    checkCombinedCommentarySuccess("05_1_example.pgn", "leading commentary",
        Arrays.asList("comment1", "comment2", "comment3", "comment4"));

    checkCombinedCommentarySuccess("06_example_whitespace.pgn", " leading  commentary  ",
        Arrays.asList(" comment", "comment ", " comment ", "  comment  "));
  }

  private static void checkCombinedCommentarySuccess(String pgnFileName, String leadingCommentary,
      List<String> commentaryListExpected) {
    final PgnFile pgnFile = PgnStrictCacheForTestCases.getPgn(PGN_TEST_COMBINED_COMMENTARY_SUCCESS_FOLDER_PATH,
        pgnFileName);

    assertEquals(leadingCommentary, pgnFile.leadingCommentary());
    assertEquals(commentaryListExpected, calculateCommentaryList(pgnFile.halfMoveList()));
  }

  private static final Path PGN_TEST_MOVE_SUFFIX_ANNOTATION_SUCCESS_FOLDER_PATH = PGN_CUSTOM_TEST_FOLDER_PATH
      .resolve("moveSuffixAnnotation/success");

  @SuppressWarnings({ "static-method", "null" })
  @Test
  void testMoveSuffixAnnotationSuccess() {
    checkMoveSuffixAnnotationSuccess("01_example.pgn",
        Arrays.asList(MoveSuffixAnnotation.BLUNDER, MoveSuffixAnnotation.NONE, MoveSuffixAnnotation.BRILLIANT_MOVE));
    checkMoveSuffixAnnotationSuccess("02_example.pgn",
        Arrays.asList(MoveSuffixAnnotation.BLUNDER, MoveSuffixAnnotation.NONE, MoveSuffixAnnotation.GOOD_MOVE));

  }

  private static void checkMoveSuffixAnnotationSuccess(String pgnFileName,
      List<MoveSuffixAnnotation> moveSuffixAnnotationListExpected) {
    final PgnFile pgnFile = PgnStrictCacheForTestCases.getPgn(PGN_TEST_MOVE_SUFFIX_ANNOTATION_SUCCESS_FOLDER_PATH,
        pgnFileName);

    assertEquals(moveSuffixAnnotationListExpected, calculateMoveSuffixAnnotationList(pgnFile.halfMoveList()));
  }

  private static final Path PGN_TEST_COMBINED_SUCCESS_FOLDER_PATH = PGN_CUSTOM_TEST_FOLDER_PATH
      .resolve("combined/success");

  @SuppressWarnings({ "static-method", "null" })
  @Test
  void testCombinedSuccess() {
    checkCombinedSuccess("01_example.pgn", "leading commentary", Arrays.asList("e4", "d5", "d4"),
        Arrays.asList(MoveSuffixAnnotation.BLUNDER, MoveSuffixAnnotation.NONE, MoveSuffixAnnotation.BRILLIANT_MOVE),
        Arrays.asList("commentWhite1", "commentBlack", "commentWhite2"));

    checkCombinedSuccess("02_example.pgn", "leading commentary", Arrays.asList("d5", "a3", "Qd6"),
        Arrays.asList(MoveSuffixAnnotation.BLUNDER, MoveSuffixAnnotation.NONE, MoveSuffixAnnotation.BRILLIANT_MOVE),
        Arrays.asList("commentBlack1", "commentWhite", "commentBlack2"));

  }

  private static void checkCombinedSuccess(String pgnFileName, String leadingCommentaryExpected,
      List<String> sanListExpected, List<MoveSuffixAnnotation> moveSuffixAnnotationListExpected,
      List<String> commentaryListExpected) {
    final PgnFile pgnFile = PgnStrictCacheForTestCases.getPgn(PGN_TEST_COMBINED_SUCCESS_FOLDER_PATH, pgnFileName);

    assertEquals(leadingCommentaryExpected, pgnFile.leadingCommentary());

    assertEquals(sanListExpected, calculateSanList(pgnFile.halfMoveList()));
    assertEquals(moveSuffixAnnotationListExpected, calculateMoveSuffixAnnotationList(pgnFile.halfMoveList()));
    assertEquals(commentaryListExpected, calculateCommentaryList(pgnFile.halfMoveList()));
  }
}
