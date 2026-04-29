package com.dlb.chess.test.pgn.parser.beyond;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.enums.GameStatus;
import com.dlb.chess.pgn.parser.LenientPgnParser;
import com.dlb.chess.pgn.parser.enums.LenientPgnParserValidationProblem;
import com.dlb.chess.pgn.parser.exceptions.LenientPgnParserValidationException;
import com.dlb.chess.san.enums.SanValidationProblem;

/**
 * Verifies that the lenient PGN parser rejects every fixture under
 * {@code src/test/resources/pgnParser/common/beyond/} and that the rejection reason carries the
 * specific {@link GameStatus} that ended the game.
 *
 * <p>
 * Each fixture has its own {@code @Test} method with the expected {@link GameStatus} hard-coded
 * in the method body. There is no runtime filename-parsing or table-driven indirection — the
 * expected (fixture, status) pair is pinned literally per test method, so a fixture rename or a
 * misclassification surfaces as a compile-time or test-name change rather than a silent
 * re-classification.
 *
 * <p>
 * The lenient parser raises {@link LenientPgnParserValidationException} with
 * {@link LenientPgnParserValidationProblem#SAN} and
 * {@link SanValidationProblem#GAME_ALREADY_ENDED}; the
 * {@link LenientPgnParserValidationException#getGameStatus()} accessor returns the specific
 * termination cause, which is asserted directly (no message-substring check).
 */
class TestLenientPgnParserBeyondTermination {

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

  /**
   * Common assertion body for the per-fixture tests above. Each test method supplies the
   * fixture filename and the expected {@link GameStatus} verbatim; this method does the I/O
   * and the typed assertions.
   */
  private static void assertRejectedWith(String pgnFileName, GameStatus expectedStatus) {
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
      assertEquals(expectedStatus, e.getGameStatus(),
          "Expected termination by " + expectedStatus + " for " + pgnFileName
              + ", got: " + e.getGameStatus());
    }
  }
}
