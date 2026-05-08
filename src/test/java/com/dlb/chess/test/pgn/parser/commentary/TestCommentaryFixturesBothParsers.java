package com.dlb.chess.test.pgn.parser.commentary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.pgn.parser.LenientPgnParser;
import com.dlb.chess.pgn.parser.StrictPgnParser;
import com.dlb.chess.pgn.parser.enums.LenientPgnParserValidationProblem;
import com.dlb.chess.pgn.parser.enums.StrictPgnParserValidationProblem;
import com.dlb.chess.pgn.parser.exceptions.LenientPgnParserValidationException;
import com.dlb.chess.pgn.parser.exceptions.StrictPgnParserValidationException;
import com.dlb.chess.pgn.parser.model.LenientPgnParserValidationResult;
import com.dlb.chess.pgn.parser.model.PgnFile;
import com.dlb.chess.pgn.parser.model.StrictPgnParserValidationResult;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.test.pgntest.constants.PgnTestConstants;

/**
 * Cross-parser fidelity: each fixture must yield the same {@link PgnFile} through both strict and lenient.
 */
class TestCommentaryFixturesBothParsers {

  private static final Path COMMENTARY_FOLDER_PATH = NonNullWrapperCommon
      .pathResolve(PgnTestConstants.PGN_PARSER_TEST_ROOT_FOLDER_PATH, "common/commentary");

  private static final Path PREGAME_COMMENTARY_SUCCESS_FOLDER_PATH = NonNullWrapperCommon
      .pathResolve(COMMENTARY_FOLDER_PATH, "pregameCommentary/success");

  private static final Path PREGAME_COMMENTARY_EXCEPTION_FOLDER_PATH = NonNullWrapperCommon
      .pathResolve(COMMENTARY_FOLDER_PATH, "pregameCommentary/exception");

  private static final Path NON_PREGAME_COMMENTARY_SUCCESS_FOLDER_PATH = NonNullWrapperCommon
      .pathResolve(COMMENTARY_FOLDER_PATH, "nonPregameCommentary/success");

  private static final Path NON_PREGAME_COMMENTARY_EXCEPTION_FOLDER_PATH = NonNullWrapperCommon
      .pathResolve(COMMENTARY_FOLDER_PATH, "nonPregameCommentary/exception");

  private static final Path COMBINED_COMMENTARY_SUCCESS_FOLDER_PATH = NonNullWrapperCommon
      .pathResolve(COMMENTARY_FOLDER_PATH, "combinedCommentary/success");

  @ParameterizedTest(name = "pregameCommentary/success/{0}")
  @ValueSource(strings = { "01_example.pgn", "02_example.pgn", "03_example.pgn",
      "10_example_left_curly_bracket_in_comment.pgn" })
  @SuppressWarnings("static-method")
  void parseAgreesAcrossParsersPregameCommentary(String pgnFileName) {
    assertParseAgrees(PREGAME_COMMENTARY_SUCCESS_FOLDER_PATH, pgnFileName);
  }

  @ParameterizedTest(name = "nonPregameCommentary/success/{0}")
  @ValueSource(strings = { "02_1_example.pgn", "02_2_example.pgn", "02_3_example.pgn", "02_4_example.pgn",
      "03_1_example.pgn", "03_2_example.pgn", "03_3_example.pgn", "03_4_example.pgn", "03_5_example.pgn",
      "03_6_example.pgn", "04_1_example.pgn", "04_2_example.pgn", "04_3_example.pgn", "04_4_example.pgn",
      "05_1_example.pgn", "06_example_whitespace.pgn", "07_example_left_curly_bracket_in_comment.pgn" })
  @SuppressWarnings("static-method")
  void parseAgreesAcrossParsersNonPregameCommentary(String pgnFileName) {
    assertParseAgrees(NON_PREGAME_COMMENTARY_SUCCESS_FOLDER_PATH, pgnFileName);
  }

  @ParameterizedTest(name = "combinedCommentary/success/{0}")
  @ValueSource(strings = { "01_example.pgn", "02_1_example.pgn", "02_2_example.pgn", "02_3_example.pgn",
      "02_4_example.pgn", "03_1_example.pgn", "03_2_example.pgn", "03_3_example.pgn", "03_4_example.pgn",
      "03_5_example.pgn", "03_6_example.pgn", "04_1_example.pgn", "04_2_example.pgn", "04_3_example.pgn",
      "04_4_example.pgn", "05_1_example.pgn", "06_example_whitespace.pgn" })
  @SuppressWarnings("static-method")
  void parseAgreesAcrossParsersCombinedCommentary(String pgnFileName) {
    assertParseAgrees(COMBINED_COMMENTARY_SUCCESS_FOLDER_PATH, pgnFileName);
  }

  @ParameterizedTest(name = "strict/pregameCommentary/exception/unclosed/{0}")
  @ValueSource(strings = { "01_movetext_commentary_ends_after_start_brace.pgn",
      "02_1_movetext_commentary_start_brace_not_followed_by_end_brace.pgn",
      "02_2_movetext_commentary_start_brace_not_followed_by_end_brace.pgn",
      "02_3_movetext_commentary_start_brace_not_followed_by_end_brace.pgn" })
  @SuppressWarnings("static-method")
  void strictRejectsPregameCommentaryUnclosedFixtures(String pgnFileName) {
    assertStrictRejects(PREGAME_COMMENTARY_EXCEPTION_FOLDER_PATH, pgnFileName,
        StrictPgnParserValidationProblem.MOVETEXT_COMMENTARY_START_BRACE_NOT_FOLLOWED_BY_END_BRACE);
  }

  @ParameterizedTest(name = "strict/pregameCommentary/exception/spacing/{0}")
  @ValueSource(strings = { "03_movetext_commentary_not_followed_by_space.pgn",
      "04_movetext_commentary_followed_by_space_but_ending.pgn",
      "05_movetext_commentary_followed_by_two_spaces.pgn",
      "06_movetext_commentary_not_followed_by_value.pgn" })
  @SuppressWarnings("static-method")
  void strictRejectsPregameCommentarySpacingFixtures(String pgnFileName) {
    assertStrictRejects(PREGAME_COMMENTARY_EXCEPTION_FOLDER_PATH, pgnFileName,
        StrictPgnParserValidationProblem.MOVETEXT_COMMENTARY_NOT_FOLLOWED_BY_SPACE);
  }

  @ParameterizedTest(name = "strict/pregameCommentary/exception/stray-close/{0}")
  @ValueSource(strings = { "07_movetext_commentary_end_brace_without_start_brace.pgn" })
  @SuppressWarnings("static-method")
  void strictRejectsPregameCommentaryStrayCloseFixtures(String pgnFileName) {
    assertStrictRejects(PREGAME_COMMENTARY_EXCEPTION_FOLDER_PATH, pgnFileName,
        StrictPgnParserValidationProblem.MOVETEXT_COMMENTARY_END_BRACE_WITHOUT_START_BRACE);
  }

  @ParameterizedTest(name = "strict/nonPregameCommentary/exception/unclosed/{0}")
  @ValueSource(strings = { "01_movetext_commentary_ends_after_start_brace.pgn",
      "02_1_movetext_commentary_start_brace_not_followed_by_end_brace.pgn",
      "02_2_movetext_commentary_start_brace_not_followed_by_end_brace.pgn",
      "02_3_movetext_commentary_start_brace_not_followed_by_end_brace.pgn",
      "02_4_movetext_commentary_start_brace_not_followed_by_end_brace.pgn" })
  @SuppressWarnings("static-method")
  void strictRejectsNonPregameCommentaryUnclosedFixtures(String pgnFileName) {
    assertStrictRejects(NON_PREGAME_COMMENTARY_EXCEPTION_FOLDER_PATH, pgnFileName,
        StrictPgnParserValidationProblem.MOVETEXT_COMMENTARY_START_BRACE_NOT_FOLLOWED_BY_END_BRACE);
  }

  @ParameterizedTest(name = "strict/nonPregameCommentary/exception/spacing/{0}")
  @ValueSource(strings = { "03_1_movetext_commentary_not_followed_by_space.pgn",
      "03_2_movetext_commentary_not_followed_by_space.pgn",
      "04_movetext_commentary_followed_by_space_but_ending.pgn",
      "05_1_movetext_commentary_followed_by_two_spaces.pgn",
      "05_2_movetext_commentary_followed_by_two_spaces.pgn",
      "06_movetext_commentary_not_followed_by_value.pgn" })
  @SuppressWarnings("static-method")
  void strictRejectsNonPregameCommentarySpacingFixtures(String pgnFileName) {
    assertStrictRejects(NON_PREGAME_COMMENTARY_EXCEPTION_FOLDER_PATH, pgnFileName,
        StrictPgnParserValidationProblem.MOVETEXT_COMMENTARY_NOT_FOLLOWED_BY_SPACE);
  }

  @ParameterizedTest(name = "strict/nonPregameCommentary/exception/stray-close/{0}")
  @ValueSource(strings = { "07_1_movetext_commentary_end_brace_without_start_brace.pgn",
      "07_2_movetext_commentary_end_brace_without_start_brace.pgn",
      "07_3_movetext_commentary_end_brace_without_start_brace.pgn" })
  @SuppressWarnings("static-method")
  void strictRejectsNonPregameCommentaryStrayCloseFixtures(String pgnFileName) {
    assertStrictRejects(NON_PREGAME_COMMENTARY_EXCEPTION_FOLDER_PATH, pgnFileName,
        StrictPgnParserValidationProblem.MOVETEXT_COMMENTARY_END_BRACE_WITHOUT_START_BRACE);
  }

  @ParameterizedTest(name = "lenient/pregameCommentary/exception/unclosed/{0}")
  @ValueSource(strings = { "01_movetext_commentary_ends_after_start_brace.pgn",
      "02_1_movetext_commentary_start_brace_not_followed_by_end_brace.pgn",
      "02_2_movetext_commentary_start_brace_not_followed_by_end_brace.pgn",
      "02_3_movetext_commentary_start_brace_not_followed_by_end_brace.pgn" })
  @SuppressWarnings("static-method")
  void lenientRejectsPregameCommentaryUnclosedFixtures(String pgnFileName) {
    assertLenientRejects(PREGAME_COMMENTARY_EXCEPTION_FOLDER_PATH, pgnFileName,
        LenientPgnParserValidationProblem.MOVETEXT_COMMENTARY_START_BRACE_NOT_FOLLOWED_BY_END_BRACE);
  }

  @ParameterizedTest(name = "lenient/pregameCommentary/exception/stray-close/{0}")
  @ValueSource(strings = { "07_movetext_commentary_end_brace_without_start_brace.pgn" })
  @SuppressWarnings("static-method")
  void lenientRejectsPregameCommentaryStrayCloseFixtures(String pgnFileName) {
    assertLenientRejects(PREGAME_COMMENTARY_EXCEPTION_FOLDER_PATH, pgnFileName,
        LenientPgnParserValidationProblem.MOVETEXT_COMMENTARY_END_BRACE_WITHOUT_START_BRACE);
  }

  @ParameterizedTest(name = "lenient/pregameCommentary/strict-only-exception/{0}")
  @ValueSource(strings = { "03_movetext_commentary_not_followed_by_space.pgn",
      "04_movetext_commentary_followed_by_space_but_ending.pgn",
      "05_movetext_commentary_followed_by_two_spaces.pgn",
      "06_movetext_commentary_not_followed_by_value.pgn" })
  @SuppressWarnings("static-method")
  void lenientAcceptsPregameCommentaryStrictOnlyExceptionFixtures(String pgnFileName) {
    assertLenientAccepts(PREGAME_COMMENTARY_EXCEPTION_FOLDER_PATH, pgnFileName);
  }

  @ParameterizedTest(name = "lenient/nonPregameCommentary/exception/unclosed/{0}")
  @ValueSource(strings = { "01_movetext_commentary_ends_after_start_brace.pgn",
      "02_1_movetext_commentary_start_brace_not_followed_by_end_brace.pgn",
      "02_2_movetext_commentary_start_brace_not_followed_by_end_brace.pgn",
      "02_3_movetext_commentary_start_brace_not_followed_by_end_brace.pgn",
      "02_4_movetext_commentary_start_brace_not_followed_by_end_brace.pgn" })
  @SuppressWarnings("static-method")
  void lenientRejectsNonPregameCommentaryUnclosedFixtures(String pgnFileName) {
    assertLenientRejects(NON_PREGAME_COMMENTARY_EXCEPTION_FOLDER_PATH, pgnFileName,
        LenientPgnParserValidationProblem.MOVETEXT_COMMENTARY_START_BRACE_NOT_FOLLOWED_BY_END_BRACE);
  }

  @ParameterizedTest(name = "lenient/nonPregameCommentary/exception/stray-close/{0}")
  @ValueSource(strings = { "07_1_movetext_commentary_end_brace_without_start_brace.pgn",
      "07_2_movetext_commentary_end_brace_without_start_brace.pgn",
      "07_3_movetext_commentary_end_brace_without_start_brace.pgn" })
  @SuppressWarnings("static-method")
  void lenientRejectsNonPregameCommentaryStrayCloseFixtures(String pgnFileName) {
    assertLenientRejects(NON_PREGAME_COMMENTARY_EXCEPTION_FOLDER_PATH, pgnFileName,
        LenientPgnParserValidationProblem.MOVETEXT_COMMENTARY_END_BRACE_WITHOUT_START_BRACE);
  }

  @ParameterizedTest(name = "lenient/nonPregameCommentary/strict-only-exception/{0}")
  @ValueSource(strings = { "03_1_movetext_commentary_not_followed_by_space.pgn",
      "03_2_movetext_commentary_not_followed_by_space.pgn",
      "04_movetext_commentary_followed_by_space_but_ending.pgn",
      "05_1_movetext_commentary_followed_by_two_spaces.pgn",
      "05_2_movetext_commentary_followed_by_two_spaces.pgn",
      "06_movetext_commentary_not_followed_by_value.pgn" })
  @SuppressWarnings("static-method")
  void lenientAcceptsNonPregameCommentaryStrictOnlyExceptionFixtures(String pgnFileName) {
    assertLenientAccepts(NON_PREGAME_COMMENTARY_EXCEPTION_FOLDER_PATH, pgnFileName);
  }

  private static void assertParseAgrees(Path folder, String pgnFileName) {
    final PgnFile strictModel = StrictPgnParser.parse(folder, pgnFileName);
    final PgnFile lenientModel = LenientPgnParser.parse(folder, pgnFileName);
    assertEquals(strictModel, lenientModel,
        "Strict and lenient parsers disagree on " + folder.getFileName() + "/" + pgnFileName);
  }

  private static void assertStrictRejects(Path folder, String pgnFileName,
      StrictPgnParserValidationProblem expected) {
    final StrictPgnParserValidationException exception = assertThrows(StrictPgnParserValidationException.class,
        () -> StrictPgnParser.parse(folder, pgnFileName));
    assertEquals(expected, exception.getStrictPgnParserValidationProblem());
    assertEquals(SanValidationProblem.NONE, exception.getSanValidationProblem());

    final StrictPgnParserValidationResult result = StrictPgnParser.validate(folder, pgnFileName);
    assertFalse(result.isValid());
    assertEquals(expected, result.problemParser());
    assertEquals(SanValidationProblem.NONE, result.problemSan());
  }

  private static void assertLenientRejects(Path folder, String pgnFileName,
      LenientPgnParserValidationProblem expected) {
    final LenientPgnParserValidationException exception = assertThrows(LenientPgnParserValidationException.class,
        () -> LenientPgnParser.parse(folder, pgnFileName));
    assertEquals(expected, exception.getLenientPgnParserValidationProblem());
    assertEquals(SanValidationProblem.NONE, exception.getSanValidationProblem());

    final LenientPgnParserValidationResult result = LenientPgnParser.validate(folder, pgnFileName);
    assertFalse(result.isValid());
    assertEquals(expected, result.problemParser());
    assertEquals(SanValidationProblem.NONE, result.problemSan());
  }

  private static void assertLenientAccepts(Path folder, String pgnFileName) {
    LenientPgnParser.parse(folder, pgnFileName);

    final LenientPgnParserValidationResult result = LenientPgnParser.validate(folder, pgnFileName);
    assertTrue(result.isValid());
    assertEquals(LenientPgnParserValidationProblem.OK, result.problemParser());
    assertEquals(SanValidationProblem.NONE, result.problemSan());
  }
}
