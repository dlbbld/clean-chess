package com.dlb.chess.test.validatenewmove;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.enums.GameStatus;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.enums.MoveCheck;
import com.dlb.chess.exceptions.InvalidMoveException;

/**
 * Surface-level tests for the strict-pipeline game-end pre-check in
 * {@link com.dlb.chess.board.ValidateNewMove#validateNewMove}: one scenario per FIDE-automatic
 * termination ({@link GameStatus#CHECKMATE}, {@link GameStatus#STALEMATE},
 * {@link GameStatus#INSUFFICIENT_MATERIAL_BOTH}, {@link GameStatus#FIVE_FOLD_REPETITION_RULE},
 * {@link GameStatus#SEVENTY_FIVE_MOVE_RULE}). Each verifies that any move attempted on a
 * terminal-state board is rejected with {@link MoveCheck#GAME_ALREADY_ENDED} and that the
 * thrown {@link InvalidMoveException} carries the originating {@link GameStatus} as payload.
 *
 * <p>The companion {@code TestSanValidationGameEnded} mirrors this set against the SAN
 * pipeline.
 */
class TestValidateNewMoveGameEnded implements EnumConstants {

  // --- CHECKMATE ---

  @SuppressWarnings("static-method")
  @Test
  void testGameEndedByCheckmate() {
    // Fool's mate: white is checkmated by black queen on h4.
    final ApiBoard board = new Board("rnb1kbnr/pppp1ppp/8/4p3/6Pq/5P2/PPPPP2P/RNBQKBNR w KQkq - 1 3");
    check(board, new MoveSpecification(E1, E2), GameStatus.CHECKMATE);
  }

  // --- STALEMATE ---

  @SuppressWarnings("static-method")
  @Test
  void testGameEndedByStalemate() {
    // Classic K + Q stalemate: black king h8 has no legal move, no check, white to move.
    // White-to-move queries; it's actually black-to-move stalemate so we set black-to-move
    // and ask validation to reject any black move attempt.
    final ApiBoard board = new Board("7k/8/6Q1/8/8/8/8/K7 b - - 0 1");
    check(board, new MoveSpecification(H8, G8), GameStatus.STALEMATE);
  }

  // --- INSUFFICIENT_MATERIAL_BOTH ---

  @SuppressWarnings("static-method")
  @Test
  void testGameEndedByInsufficientMaterialBoth() {
    // K vs K: dead position under FIDE 5.2.2.
    final ApiBoard board = new Board("4k3/8/8/8/8/8/8/4K3 w - - 0 1");
    check(board, new MoveSpecification(E1, E2), GameStatus.INSUFFICIENT_MATERIAL_BOTH);
  }

  // --- SEVENTY_FIVE_MOVE_RULE ---

  @SuppressWarnings("static-method")
  @Test
  void testGameEndedBySeventyFiveMoveRule() {
    // FEN with halfmove clock at the 75-move threshold (150) — the position is the terminal
    // moment of the 75-move rule. Any further move is rejected.
    final ApiBoard board = new Board("4k3/8/4P3/8/8/8/2N1B3/3KQ2R w - - 150 76");
    check(board, new MoveSpecification(D1, D2), GameStatus.SEVENTY_FIVE_MOVE_RULE);
  }

  // --- FIVE_FOLD_REPETITION_RULE ---

  @SuppressWarnings("static-method")
  @Test
  void testGameEndedByFivefoldRepetition() {
    // Drive a board to fivefold by alternating knight moves between two squares for both
    // sides, so the same position recurs 5 times.
    final ApiBoard board = new Board();
    // Sequence: 1.Nf3 Nf6 2.Ng1 Ng8 3.Nf3 Nf6 4.Ng1 Ng8 5.Nf3 Nf6 6.Ng1 Ng8 7.Nf3 Nf6 8.Ng1 Ng8
    // After move 8...Ng8 the starting position has occurred 5 times (move 0, 2, 4, 6, 8).
    board.performMoves("Nf3", "Nf6", "Ng1", "Ng8", "Nf3", "Nf6", "Ng1", "Ng8", "Nf3", "Nf6", "Ng1", "Ng8", "Nf3",
        "Nf6", "Ng1", "Ng8");
    check(board, new MoveSpecification(E2, E4), GameStatus.FIVE_FOLD_REPETITION_RULE);
  }

  // --- helpers ---

  private static void check(ApiBoard board, MoveSpecification move, GameStatus expectedGameStatus) {
    var isException = false;
    try {
      board.performMove(move);
    } catch (final InvalidMoveException e) {
      isException = true;
      assertEquals(MoveCheck.GAME_ALREADY_ENDED, e.getMoveCheck());
      assertNotNull(e.getGameStatus(), "GAME_ALREADY_ENDED must carry a GameStatus payload");
      assertEquals(expectedGameStatus, e.getGameStatus());
    }
    assertTrue(isException, "Expected InvalidMoveException with GAME_ALREADY_ENDED");
  }
}
