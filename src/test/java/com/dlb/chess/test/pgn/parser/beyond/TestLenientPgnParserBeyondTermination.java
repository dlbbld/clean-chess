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
import com.dlb.chess.pgn.parser.LenientPgnParser;
import com.dlb.chess.pgn.parser.enums.LenientPgnParserValidationProblem;
import com.dlb.chess.pgn.parser.exceptions.LenientPgnParserValidationException;
import com.dlb.chess.san.enums.SanValidationProblem;

/**
 * Verifies that the lenient PGN parser rejects every fixture under
 * {@code src/test/resources/pgnParser/common/beyond/} (root, not the legacy bucket) and that the
 * rejection reason carries the specific {@link GameStatus} that ended the game.
 *
 * <p>
 * The fixtures are deliberately authored to play exactly one halfmove past a FIDE-automatic
 * termination (checkmate, stalemate, mutual insufficient material, fivefold repetition, 75-move
 * rule). The parser raises {@link LenientPgnParserValidationException} with
 * {@link LenientPgnParserValidationProblem#SAN} and
 * {@link SanValidationProblem#GAME_ALREADY_ENDED}; the message text names the specific
 * {@link GameStatus} that ended the game, which the test asserts to distinguish the five
 * termination causes.
 *
 * <p>
 * The by-color split (with-white-move vs with-black-move) exercises both side-to-move codepaths.
 */
class TestLenientPgnParserBeyondTermination {

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
      LenientPgnParser.parse(BEYOND_FOLDER, pgnFileName);
      fail("Lenient parser was expected to reject " + pgnFileName
          + " (it plays past " + expectedStatus + ") but parsing succeeded");
    } catch (final LenientPgnParserValidationException e) {
      assertEquals(LenientPgnParserValidationProblem.SAN, e.getLenientPgnParserValidationProblem(),
          "Expected SAN-level rejection for " + pgnFileName);
      assertEquals(SanValidationProblem.GAME_ALREADY_ENDED, e.getSanValidationProblem(),
          "Expected GAME_ALREADY_ENDED rejection for " + pgnFileName + ", got: "
              + e.getSanValidationProblem());
      // The parser flattens the SanValidationException into a message string; the GameStatus
      // appears verbatim in the message, e.g. "...The game has already ended by CHECKMATE...".
      // Assert it to distinguish the five termination causes.
      assertTrue(e.getMessage().contains(expectedStatus.name()),
          "Expected message to mention " + expectedStatus.name() + " for " + pgnFileName
              + ", got: " + e.getMessage());
    }
  }
}
