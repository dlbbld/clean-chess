package com.dlb.chess.test.san.destinationsquare;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.san.validate.SanValidation;

class TestSanValidateCapturingNoPiece {

  @SuppressWarnings("static-method")
  @Test
  void testWhite() {

    final ApiBoard board = new Board();

    // pawn
    checkExceptionPawn("axb3", board);

    // rook
    board.performMoves("a3");
    board.performMoves("a6");

    // knight
    checkExceptionRnbqk("Nxc3", board);

    // bishop
    board.performMoves("b3");
    board.performMoves("b6");
    checkExceptionRnbqk("Bxb2", board);

    // queen
    board.performMoves("Bb2");
    board.performMoves("Bb7");
    checkExceptionRnbqk("Qxc1", board);

    // king
    board.performMoves("Qc1");
    board.performMoves("Qc8");
    checkExceptionRnbqk("Kxd1", board);
  }

  @SuppressWarnings("static-method")
  @Test
  void testBlack() {

    final ApiBoard board = new Board();

    // pawn
    board.performMoves("a3");
    checkExceptionPawn("axb6", board);

    // rook

    board.performMoves("a6");
    board.performMoves("a4");
    checkExceptionRnbqk("Rxa7", board);

    // knight
    checkExceptionRnbqk("Nxc6", board);

    // bishop
    board.performMoves("b6");
    board.performMoves("b3");
    checkExceptionRnbqk("Bxb7", board);

    // queen
    board.performMoves("Bb7");
    board.performMoves("Bb2");
    checkExceptionRnbqk("Qxc8", board);

    // king
    board.performMoves("Qc8");
    board.performMoves("Qc1");
    checkExceptionRnbqk("Kxd8", board);
  }

  private static void checkExceptionPawn(String san, ApiBoard board) {
    checkException(san, board, SanValidationProblem.MOVEMENT_PAWN_DIAGONAL_REQUIRES_OPPONENT_PIECE);
  }

  private static void checkExceptionRnbqk(String san, ApiBoard board) {
    checkException(san, board, SanValidationProblem.DESTINATION_EMPTY_CAPTURE_SYMBOL_RNBQK);
  }

  private static void checkException(String san, ApiBoard board, SanValidationProblem expected) {
    boolean isException;
    try {
      SanValidation.validateSan(san, board);
      isException = false;
    } catch (final SanValidationException e) {
      isException = true;
      assertEquals(expected, e.getSanValidationProblem());
    }
    assertTrue(isException);
  }

}