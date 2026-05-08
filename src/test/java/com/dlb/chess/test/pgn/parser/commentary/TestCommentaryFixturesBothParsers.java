package com.dlb.chess.test.pgn.parser.commentary;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.pgn.parser.LenientPgnParser;
import com.dlb.chess.pgn.parser.StrictPgnParser;
import com.dlb.chess.pgn.parser.model.PgnFile;
import com.dlb.chess.test.pgntest.constants.PgnTestConstants;

/**
 * Cross-parser fidelity: each fixture must yield the same {@link PgnFile} through both strict and lenient.
 */
class TestCommentaryFixturesBothParsers {

  private static final Path COMMENTARY_FOLDER_PATH = NonNullWrapperCommon
      .pathResolve(PgnTestConstants.PGN_PARSER_TEST_ROOT_FOLDER_PATH, "common/commentary");

  private static final Path LEADING_COMMENTARY_SUCCESS_FOLDER_PATH = NonNullWrapperCommon
      .pathResolve(COMMENTARY_FOLDER_PATH, "pregameCommentary/success");

  private static final Path NON_LEADING_COMMENTARY_SUCCESS_FOLDER_PATH = NonNullWrapperCommon
      .pathResolve(COMMENTARY_FOLDER_PATH, "nonPregameCommentary/success");

  private static final Path COMBINED_COMMENTARY_SUCCESS_FOLDER_PATH = NonNullWrapperCommon
      .pathResolve(COMMENTARY_FOLDER_PATH, "combinedCommentary/success");

  @ParameterizedTest(name = "pregameCommentary/success/{0}")
  @ValueSource(strings = { "01_example.pgn", "02_example.pgn", "03_example.pgn",
      "10_example_left_curly_bracket_in_comment.pgn" })
  @SuppressWarnings("static-method")
  void parseAgreesAcrossParsersPregameCommentary(String pgnFileName) {
    assertParseAgrees(LEADING_COMMENTARY_SUCCESS_FOLDER_PATH, pgnFileName);
  }

  @ParameterizedTest(name = "nonPregameCommentary/success/{0}")
  @ValueSource(strings = { "02_1_example.pgn", "02_2_example.pgn", "02_3_example.pgn", "02_4_example.pgn",
      "03_1_example.pgn", "03_2_example.pgn", "03_3_example.pgn", "03_4_example.pgn", "03_5_example.pgn",
      "03_6_example.pgn", "04_1_example.pgn", "04_2_example.pgn", "04_3_example.pgn", "04_4_example.pgn",
      "05_1_example.pgn", "06_example_whitespace.pgn", "07_example_left_curly_bracket_in_comment.pgn" })
  @SuppressWarnings("static-method")
  void parseAgreesAcrossParsersNonPregameCommentary(String pgnFileName) {
    assertParseAgrees(NON_LEADING_COMMENTARY_SUCCESS_FOLDER_PATH, pgnFileName);
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

  private static void assertParseAgrees(Path folder, String pgnFileName) {
    final PgnFile strictModel = StrictPgnParser.parse(folder, pgnFileName);
    final PgnFile lenientModel = LenientPgnParser.parse(folder, pgnFileName);
    assertEquals(strictModel, lenientModel,
        "Strict and lenient parsers disagree on " + folder.getFileName() + "/" + pgnFileName);
  }
}
