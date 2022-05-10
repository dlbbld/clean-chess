package com.dlb.chess.test.san;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.san.SanValidation;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;

class TestSanValidateMovingOntoOwnPiece {

  @SuppressWarnings("static-method")
  @Test
  void testWhite() {

    final ApiBoard board = new Board();

    // rook
    checkException("Rxa2", board);

    // knight
    checkException("Nxd2", board);

    // bishop
    checkException("Bxa2", board);

    // queen
    checkException("Qxd2", board);

    // king
    checkException("Kxf1", board);

    board.performMoves("Nc3");
    board.performMoves("a6");

    // pawn
    checkException("bxc3", board);
  }

  @SuppressWarnings("static-method")
  @Test
  void testBlack() {

    final ApiBoard board = new Board();
    board.performMoves("e4");

    // rook
    checkException("Rxa7", board);

    // knight
    checkException("Nxd7", board);

    // bishop
    checkException("Bxa7", board);

    // queen
    checkException("Qxd7", board);

    // king
    checkException("Kxf7", board);

    board.performMoves("Nc6");
    board.performMoves("d4");

    // pawn
    checkException("bxc6", board);
  }

  private static void checkException(String san, ApiBoard board) {
    boolean isException;
    try {
      SanValidation.calculateMoveSpecificationForSan(san, board);
      isException = false;
    } catch (final SanValidationException e) {
      isException = true;
      assertEquals(SanValidationProblem.MOVING_ONTO_ONE_PIECE, e.getSanValidationProblem());
    }
    assertTrue(isException);
  }

}