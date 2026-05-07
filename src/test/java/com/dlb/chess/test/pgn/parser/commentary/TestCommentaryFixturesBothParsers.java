package com.dlb.chess.test.pgn.parser.commentary;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.pgn.parser.LenientPgnParser;
import com.dlb.chess.pgn.parser.StrictPgnParser;
import com.dlb.chess.pgn.parser.model.PgnFile;
import com.dlb.chess.test.pgntest.constants.PgnTestConstants;

/**
 * Cross-parser fidelity: each fixture must yield the same {@link PgnFile} through both strict and lenient. New
 * fixtures are picked up via the {@link MethodSource} feeders below.
 */
class TestCommentaryFixturesBothParsers {

  private static final Path COMMENTARY_FOLDER_PATH = NonNullWrapperCommon
      .resolve(PgnTestConstants.PGN_PARSER_TEST_ROOT_FOLDER_PATH, "common/commentary");

  private static final Path LEADING_COMMENTARY_SUCCESS_FOLDER_PATH = NonNullWrapperCommon
      .resolve(COMMENTARY_FOLDER_PATH, "pregameCommentary/success");

  private static final Path NON_LEADING_COMMENTARY_SUCCESS_FOLDER_PATH = NonNullWrapperCommon
      .resolve(COMMENTARY_FOLDER_PATH, "nonPregameCommentary/success");

  private static final Path COMBINED_COMMENTARY_SUCCESS_FOLDER_PATH = NonNullWrapperCommon
      .resolve(COMMENTARY_FOLDER_PATH, "combinedCommentary/success");

  @ParameterizedTest(name = "pregameCommentary/success/{0}")
  @MethodSource("pregameCommentaryFixtures")
  @SuppressWarnings("static-method")
  void parseAgreesAcrossParsers_pregameCommentary(String pgnFileName) {
    assertParseAgrees(LEADING_COMMENTARY_SUCCESS_FOLDER_PATH, pgnFileName);
  }

  @ParameterizedTest(name = "nonPregameCommentary/success/{0}")
  @MethodSource("nonPregameCommentaryFixtures")
  @SuppressWarnings("static-method")
  void parseAgreesAcrossParsers_nonPregameCommentary(String pgnFileName) {
    assertParseAgrees(NON_LEADING_COMMENTARY_SUCCESS_FOLDER_PATH, pgnFileName);
  }

  @ParameterizedTest(name = "combinedCommentary/success/{0}")
  @MethodSource("combinedCommentaryFixtures")
  @SuppressWarnings("static-method")
  void parseAgreesAcrossParsers_combinedCommentary(String pgnFileName) {
    assertParseAgrees(COMBINED_COMMENTARY_SUCCESS_FOLDER_PATH, pgnFileName);
  }

  private static void assertParseAgrees(Path folder, String pgnFileName) {
    final PgnFile strictModel = StrictPgnParser.parse(folder, pgnFileName);
    final PgnFile lenientModel = LenientPgnParser.parse(folder, pgnFileName);
    assertEquals(strictModel, lenientModel,
        "Strict and lenient parsers disagree on " + folder.getFileName() + "/" + pgnFileName);
  }

  static Stream<Arguments> pregameCommentaryFixtures() {
    return Stream.of(Arguments.of("01_example.pgn"), Arguments.of("02_example.pgn"), Arguments.of("03_example.pgn"),
        Arguments.of("10_example_left_curly_bracket_in_comment.pgn"));
  }

  static Stream<Arguments> nonPregameCommentaryFixtures() {
    return Stream.of(Arguments.of("02_1_example.pgn"), Arguments.of("02_2_example.pgn"),
        Arguments.of("02_3_example.pgn"), Arguments.of("02_4_example.pgn"), Arguments.of("03_1_example.pgn"),
        Arguments.of("03_2_example.pgn"), Arguments.of("03_3_example.pgn"), Arguments.of("03_4_example.pgn"),
        Arguments.of("03_5_example.pgn"), Arguments.of("03_6_example.pgn"), Arguments.of("04_1_example.pgn"),
        Arguments.of("04_2_example.pgn"), Arguments.of("04_3_example.pgn"), Arguments.of("04_4_example.pgn"),
        Arguments.of("05_1_example.pgn"), Arguments.of("06_example_whitespace.pgn"),
        Arguments.of("07_example_left_curly_bracket_in_comment.pgn"));
  }

  static Stream<Arguments> combinedCommentaryFixtures() {
    return Stream.of(Arguments.of("01_example.pgn"), Arguments.of("02_1_example.pgn"), Arguments.of("02_2_example.pgn"),
        Arguments.of("02_3_example.pgn"), Arguments.of("02_4_example.pgn"), Arguments.of("03_1_example.pgn"),
        Arguments.of("03_2_example.pgn"), Arguments.of("03_3_example.pgn"), Arguments.of("03_4_example.pgn"),
        Arguments.of("03_5_example.pgn"), Arguments.of("03_6_example.pgn"), Arguments.of("04_1_example.pgn"),
        Arguments.of("04_2_example.pgn"), Arguments.of("04_3_example.pgn"), Arguments.of("04_4_example.pgn"),
        Arguments.of("05_1_example.pgn"), Arguments.of("06_example_whitespace.pgn"));
  }
}
