package com.dlb.chess.test.pgn.parser.beyond;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.enums.GameStatus;
import com.dlb.chess.pgn.parser.StrictPgnParser;
import com.dlb.chess.pgn.parser.enums.StrictPgnParserValidationProblem;
import com.dlb.chess.pgn.parser.exceptions.StrictPgnParserValidationException;
import com.dlb.chess.san.enums.SanValidationProblem;

/**
 * Strict-parser counterpart of {@link TestLenientPgnParserBeyondTermination}: every fixture
 * under {@code src/test/resources/pgnParser/common/beyond/} must be rejected by the strict
 * pipeline, with the expected {@link GameStatus} carried on the exception.
 *
 * <p>
 * Like the lenient counterpart, each fixture has its own {@code @Test} method with the
 * expected {@link GameStatus} hard-coded inline. No runtime filename parsing.
 *
 * <p>
 * The strict parser raises {@link StrictPgnParserValidationException} with
 * {@link StrictPgnParserValidationProblem#SAN} and
 * {@link SanValidationProblem#GAME_ALREADY_ENDED}; the
 * {@link StrictPgnParserValidationException#getGameStatus()} accessor returns the specific
 * termination cause.
 */
class TestStrictPgnParserBeyondTermination {

  private static final Path BEYOND_FOLDER = NonNullWrapperCommon.resolve(
      com.dlb.chess.common.constants.ConfigurationConstants.PROJECT_ROOT_FOLDER_PATH,
      "src/test/resources/pgnParser/common/beyond");

  @SuppressWarnings("static-method")
  @Test
  void test01PlayBeyondCheckmateWithWhiteMove() {
    assertRejectedWith("01_play_beyond_checkmate_with_white_move.pgn", GameStatus.CHECKMATE);
  }

  @SuppressWarnings("static-method")
  @Test
  void test02PlayBeyondCheckmateWithBlackMove() {
    assertRejectedWith("02_play_beyond_checkmate_with_black_move.pgn", GameStatus.CHECKMATE);
  }

  @SuppressWarnings("static-method")
  @Test
  void test03PlayBeyondStalemateWithWhiteMove() {
    assertRejectedWith("03_play_beyond_stalemate_with_white_move.pgn", GameStatus.STALEMATE);
  }

  @SuppressWarnings("static-method")
  @Test
  void test04PlayBeyondStalemateWithBlackMove() {
    assertRejectedWith("04_play_beyond_stalemate_with_black_move.pgn", GameStatus.STALEMATE);
  }

  @SuppressWarnings("static-method")
  @Test
  void test05PlayBeyondInsufficientMaterialWithWhiteMove() {
    assertRejectedWith("05_play_beyond_insufficient_material_with_white_move.pgn",
        GameStatus.INSUFFICIENT_MATERIAL_BOTH);
  }

  @SuppressWarnings("static-method")
  @Test
  void test06PlayBeyondInsufficientMaterialWithBlackMove() {
    assertRejectedWith("06_play_beyond_insufficient_material_with_black_move.pgn",
        GameStatus.INSUFFICIENT_MATERIAL_BOTH);
  }

  @SuppressWarnings("static-method")
  @Test
  void test07PlayBeyondFivefoldRepetitionWithWhiteMove() {
    assertRejectedWith("07_play_beyond_fivefold_repetition_with_white_move.pgn",
        GameStatus.FIVE_FOLD_REPETITION_RULE);
  }

  @SuppressWarnings("static-method")
  @Test
  void test08PlayBeyondFivefoldRepetitionWithBlackMove() {
    assertRejectedWith("08_play_beyond_fivefold_repetition_with_black_move.pgn",
        GameStatus.FIVE_FOLD_REPETITION_RULE);
  }

  @SuppressWarnings("static-method")
  @Test
  void test09PlayBeyondSeventyFiveMoveRuleWithWhiteMove() {
    assertRejectedWith("09_play_beyond_seventy_five_move_rule_with_white_move.pgn",
        GameStatus.SEVENTY_FIVE_MOVE_RULE);
  }

  @SuppressWarnings("static-method")
  @Test
  void test10PlayBeyondSeventyFiveMoveRuleWithBlackMove() {
    assertRejectedWith("10_play_beyond_seventy_five_move_rule_with_black_move.pgn",
        GameStatus.SEVENTY_FIVE_MOVE_RULE);
  }

  private static void assertRejectedWith(String pgnFileName, GameStatus expectedStatus) {
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
      assertEquals(expectedStatus, e.getGameStatus(),
          "Expected termination by " + expectedStatus + " for " + pgnFileName
              + ", got: " + e.getGameStatus());
    }
  }
}
