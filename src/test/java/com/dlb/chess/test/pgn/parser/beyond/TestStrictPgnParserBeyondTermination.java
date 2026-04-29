package com.dlb.chess.test.pgn.parser.beyond;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.enums.GameStatus;
import com.dlb.chess.pgn.parser.StrictPgnParser;
import com.dlb.chess.pgn.parser.enums.StrictPgnParserValidationProblem;
import com.dlb.chess.pgn.parser.exceptions.StrictPgnParserValidationException;
import com.dlb.chess.san.enums.SanValidationProblem;

/**
 * Strict-parser counterpart of
 * {@link TestLenientPgnParserBeyondTermination}: every fixture under
 * {@code src/test/resources/pgnParser/common/beyond/} must be rejected by the strict pipeline.
 *
 * <p>
 * The strict parser raises {@link StrictPgnParserValidationException} with
 * {@link StrictPgnParserValidationProblem#SAN} and
 * {@link SanValidationProblem#GAME_ALREADY_ENDED}; the message text names the specific
 * {@link GameStatus}, which the test asserts to distinguish the five termination causes
 * (checkmate, stalemate, mutual insufficient material, fivefold repetition, 75-move rule).
 */
class TestStrictPgnParserBeyondTermination {

  private static final Path BEYOND_FOLDER = NonNullWrapperCommon.resolve(
      com.dlb.chess.common.constants.ConfigurationConstants.PROJECT_ROOT_FOLDER_PATH,
      "src/test/resources/pgnParser/common/beyond");

  static Stream<Arguments> fixtures() {
    return Stream.of(
        Arguments.of("01_play_beyond_checkmate_with_white_move.pgn", GameStatus.CHECKMATE),
        Arguments.of("02_play_beyond_checkmate_with_black_move.pgn", GameStatus.CHECKMATE),
        Arguments.of("03_play_beyond_stalemate_with_white_move.pgn", GameStatus.STALEMATE),
        Arguments.of("04_play_beyond_stalemate_with_black_move.pgn", GameStatus.STALEMATE),
        Arguments.of("05_play_beyond_insufficient_material_with_white_move.pgn",
            GameStatus.INSUFFICIENT_MATERIAL_BOTH),
        Arguments.of("06_play_beyond_insufficient_material_with_black_move.pgn",
            GameStatus.INSUFFICIENT_MATERIAL_BOTH),
        Arguments.of("07_play_beyond_fivefold_repetition_with_white_move.pgn",
            GameStatus.FIVE_FOLD_REPETITION_RULE),
        Arguments.of("08_play_beyond_fivefold_repetition_with_black_move.pgn",
            GameStatus.FIVE_FOLD_REPETITION_RULE),
        Arguments.of("09_play_beyond_seventy_five_move_rule_with_white_move.pgn",
            GameStatus.SEVENTY_FIVE_MOVE_RULE),
        Arguments.of("10_play_beyond_seventy_five_move_rule_with_black_move.pgn",
            GameStatus.SEVENTY_FIVE_MOVE_RULE));
  }

  @SuppressWarnings("static-method")
  @ParameterizedTest(name = "{0} -> {1}")
  @MethodSource("fixtures")
  void rejectsWithExpectedTerminationReason(String pgnFileName, GameStatus expectedStatus) {
    try {
      StrictPgnParser.parse(BEYOND_FOLDER, pgnFileName);
      fail("Strict parser was expected to reject " + pgnFileName
          + " (it plays past " + expectedStatus + ") but parsing succeeded");
    } catch (final StrictPgnParserValidationException e) {
      assertEquals(StrictPgnParserValidationProblem.SAN, e.getStrictPgnParserValidationProblem(),
          "Expected SAN-level rejection for " + pgnFileName);
      assertEquals(SanValidationProblem.GAME_ALREADY_ENDED, e.getSanValidationProblem(),
          "Expected GAME_ALREADY_ENDED rejection for " + pgnFileName + ", got: "
              + e.getSanValidationProblem());
      assertTrue(e.getMessage().contains(expectedStatus.name()),
          "Expected message to mention " + expectedStatus.name() + " for " + pgnFileName
              + ", got: " + e.getMessage());
    }
  }
}
