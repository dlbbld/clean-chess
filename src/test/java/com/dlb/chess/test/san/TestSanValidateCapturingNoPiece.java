package com.dlb.chess.test.san;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.san.SanValidation;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;

class TestSanValidateCapturingNoPiece {

  @SuppressWarnings("static-method")
  @Test
  void testWhite() {

    final ApiBoard board = new Board();

    // pawn
    checkException("axb3", board);

    // rook
    board.performMoves("a3");
    board.performMoves("a6");

    // knight
    checkException("Nxc3", board);

    // bishop
    board.performMoves("b3");
    board.performMoves("b6");
    checkException("Bxb2", board);

    // queen
    board.performMoves("Bb2");
    board.performMoves("Bb7");
    checkException("Qxc1", board);

    // king
    board.performMoves("Qc1");
    board.performMoves("Qc8");
    checkException("Kxd1", board);
  }

  @SuppressWarnings("static-method")
  @Test
  void testBlack() {

    final ApiBoard board = new Board();

    // pawn
    board.performMoves("a3");
    checkException("axb6", board);

    // rook

    board.performMoves("a6");
    board.performMoves("a4");
    checkException("Rxa7", board);

    // knight
    checkException("Nxc6", board);

    // bishop
    board.performMoves("b6");
    board.performMoves("b3");
    checkException("Bxb7", board);

    // queen
    board.performMoves("Bb7");
    board.performMoves("Bb2");
    checkException("Qxc8", board);

    // king
    board.performMoves("Qc8");
    board.performMoves("Qc1");
    checkException("Kxd8", board);
  }

  private static void checkException(String san, ApiBoard board) {
    boolean isException;
    try {
      SanValidation.calculateMoveSpecificationForSan(san, board);
      isException = false;
    } catch (final SanValidationException e) {
      isException = true;
      assertEquals(SanValidationProblem.CAPTURING_NO_PIECE, e.getSanValidationProblem());
    }
    assertTrue(isException);
  }

}