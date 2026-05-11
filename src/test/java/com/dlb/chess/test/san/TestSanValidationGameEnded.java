package com.dlb.chess.test.san;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.enums.GameStatus;
import com.dlb.chess.san.SanValidationProblem;
import com.dlb.chess.san.SanValidationException;
import com.dlb.chess.san.StrictSanParser;

/**
 * Surface-level tests for the strict-pipeline game-end pre-check in {@link StrictSanParser#parseText}: one scenario per
 * FIDE-automatic termination ({@link GameStatus#CHECKMATE}, {@link GameStatus#STALEMATE},
 * {@link GameStatus#INSUFFICIENT_MATERIAL_BOTH}, {@link GameStatus#FIVE_FOLD_REPETITION_RULE},
 * {@link GameStatus#SEVENTY_FIVE_MOVE_RULE}). Each verifies that any SAN attempted on a terminal-state board is
 * rejected with {@link SanValidationProblem#GAME_ALREADY_ENDED} and that the thrown {@link SanValidationException}
 * carries the originating {@link GameStatus} as payload.
 *
 * <p>
 * The companion {@code TestValidateNewMoveGameEnded} mirrors this set against the MoveSpecification pipeline.
 */
class TestSanValidationGameEnded {

  @SuppressWarnings("static-method")
  @Test
  void testGameEndedByCheckmate() {
    // Fool's mate.
    final Board board = new Board("rnb1kbnr/pppp1ppp/8/4p3/6Pq/5P2/PPPPP2P/RNBQKBNR w KQkq - 1 3");
    check("Ke2", board, GameStatus.CHECKMATE);
  }

  @SuppressWarnings("static-method")
  @Test
  void testGameEndedByStalemate() {
    final Board board = new Board("7k/8/6Q1/8/8/8/8/K7 b - - 0 1");
    check("Kg8", board, GameStatus.STALEMATE);
  }

  @SuppressWarnings("static-method")
  @Test
  void testGameEndedByInsufficientMaterialBoth() {
    final Board board = new Board("4k3/8/8/8/8/8/8/4K3 w - - 0 1");
    check("Ke2", board, GameStatus.INSUFFICIENT_MATERIAL_BOTH);
  }

  @SuppressWarnings("static-method")
  @Test
  void testGameEndedBySeventyFiveMoveRule() {
    final Board board = new Board("4k3/8/4P3/8/8/8/2N1B3/3KQ2R w - - 150 76");
    check("Kd2", board, GameStatus.SEVENTY_FIVE_MOVE_RULE);
  }

  @SuppressWarnings("static-method")
  @Test
  void testGameEndedByFivefoldRepetition() {
    final Board board = new Board();
    board.movesStrict("Nf3", "Nf6", "Ng1", "Ng8", "Nf3", "Nf6", "Ng1", "Ng8", "Nf3", "Nf6", "Ng1", "Ng8", "Nf3", "Nf6",
        "Ng1", "Ng8");
    check("e4", board, GameStatus.FIVE_FOLD_REPETITION_RULE);
  }

  // --- helpers ---

  private static void check(String san, Board board, GameStatus expectedGameStatus) {
    var isException = false;
    try {
      StrictSanParser.parseText(san, board);
    } catch (final SanValidationException e) {
      isException = true;
      assertEquals(SanValidationProblem.GAME_ALREADY_ENDED, e.getSanValidationProblem());
      assertNotNull(e.getGameStatus(), "GAME_ALREADY_ENDED must carry a GameStatus payload");
      assertEquals(expectedGameStatus, e.getGameStatus());
    }
    assertTrue(isException, "Expected SanValidationException with GAME_ALREADY_ENDED");
  }
}
