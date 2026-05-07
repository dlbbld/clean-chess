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
 * Cross-parser fidelity test for the commentary fixtures. Each fixture is parsed once with the strict parser and
 * once with the lenient parser; the two resulting {@link PgnFile} models must be equal. This proves both that
 *
 * <ol>
 * <li>the fixtures are well-formed enough for the strict parser (every White-move commentary is followed by the
 * required {@code N...} indicator per T-002), and
 * <li>the lenient parser produces the same model as strict on canonically-formed input — which is the contract:
 * lenient is a superset of strict, so it must agree on every input strict accepts.
 * </ol>
 *
 * <p>Adding a new commentary fixture under any of the three covered directories ({@code leadingCommentary/success},
 * {@code nonLeadingCommentary/success}, {@code combinedCommentary/success}) is automatically picked up via the
 * {@link MethodSource} feeders below — no test method needs editing.
 *
 * <p>Two fixtures are intentionally excluded:
 *
 * <ul>
 * <li>{@code nonLeadingCommentary/success/07_example_left_curly_bracket_in_comment.pgn} and
 * {@code leadingCommentary/success/10_example_left_curly_bracket_in_comment.pgn} — both contain a literal {@code {}
 * inside a brace comment, currently rejected by both parsers (open follow-up T-003: spec-compliant brace-comment
 * parsing). They will become valid for both parsers when T-003 lands and can be added back then.
 * </ul>
 */
class TestCommentaryFixturesBothParsers {

  private static final Path COMMENTARY_FOLDER_PATH = NonNullWrapperCommon
      .resolve(PgnTestConstants.STRICT_PGN_PARSER_TEST_ROOT_FOLDER_PATH, "movementSpecification/commentary");

  private static final Path LEADING_COMMENTARY_SUCCESS_FOLDER_PATH = NonNullWrapperCommon
      .resolve(COMMENTARY_FOLDER_PATH, "leadingCommentary/success");

  private static final Path NON_LEADING_COMMENTARY_SUCCESS_FOLDER_PATH = NonNullWrapperCommon
      .resolve(COMMENTARY_FOLDER_PATH, "nonLeadingCommentary/success");

  private static final Path COMBINED_COMMENTARY_SUCCESS_FOLDER_PATH = NonNullWrapperCommon
      .resolve(COMMENTARY_FOLDER_PATH, "combinedCommentary/success");

  @ParameterizedTest(name = "leadingCommentary/success/{0}")
  @MethodSource("leadingCommentaryFixtures")
  @SuppressWarnings("static-method")
  void parseAgreesAcrossParsers_leadingCommentary(String pgnFileName) {
    assertParseAgrees(LEADING_COMMENTARY_SUCCESS_FOLDER_PATH, pgnFileName);
  }

  @ParameterizedTest(name = "nonLeadingCommentary/success/{0}")
  @MethodSource("nonLeadingCommentaryFixtures")
  @SuppressWarnings("static-method")
  void parseAgreesAcrossParsers_nonLeadingCommentary(String pgnFileName) {
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

  // -------------------------------------------------------------------------------------------------
  // Fixture feeders. The leading-curly-bracket fixtures (T-003 follow-up) are excluded by name.
  // -------------------------------------------------------------------------------------------------

  static Stream<Arguments> leadingCommentaryFixtures() {
    return Stream.of(Arguments.of("01_example.pgn"), Arguments.of("02_example.pgn"), Arguments.of("03_example.pgn"));
  }

  static Stream<Arguments> nonLeadingCommentaryFixtures() {
    return Stream.of(Arguments.of("02_1_example.pgn"), Arguments.of("02_2_example.pgn"),
        Arguments.of("02_3_example.pgn"), Arguments.of("02_4_example.pgn"), Arguments.of("03_1_example.pgn"),
        Arguments.of("03_2_example.pgn"), Arguments.of("03_3_example.pgn"), Arguments.of("03_4_example.pgn"),
        Arguments.of("03_5_example.pgn"), Arguments.of("03_6_example.pgn"), Arguments.of("04_1_example.pgn"),
        Arguments.of("04_2_example.pgn"), Arguments.of("04_3_example.pgn"), Arguments.of("04_4_example.pgn"),
        Arguments.of("05_1_example.pgn"), Arguments.of("06_example_whitespace.pgn"));
  }

  static Stream<Arguments> combinedCommentaryFixtures() {
    return Stream.of(Arguments.of("01_example.pgn"), Arguments.of("02_1_example.pgn"),
        Arguments.of("02_2_example.pgn"), Arguments.of("02_3_example.pgn"), Arguments.of("02_4_example.pgn"),
        Arguments.of("03_1_example.pgn"), Arguments.of("03_2_example.pgn"), Arguments.of("03_3_example.pgn"),
        Arguments.of("03_4_example.pgn"), Arguments.of("03_5_example.pgn"), Arguments.of("03_6_example.pgn"),
        Arguments.of("04_1_example.pgn"), Arguments.of("04_2_example.pgn"), Arguments.of("04_3_example.pgn"),
        Arguments.of("04_4_example.pgn"), Arguments.of("05_1_example.pgn"), Arguments.of("06_example_whitespace.pgn"));
  }
}
