package com.dlb.chess.test.san;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;

class TestSanValidateCheckmateOrCheck {

  @SuppressWarnings("static-method")
  @Test
  void testWhite() {
    final ApiBoard board = new Board();

    checkException("e4#", SanValidationProblem.CHECKMATE_BUT_NONE, board);
    checkException("e4+", SanValidationProblem.CHECK_BUT_NONE, board);

    board.performMoves("e4", "e5", "Bc4", "Bc5");
    checkException("Bxf7#", SanValidationProblem.CHECKMATE_BUT_CHECK_ONLY, board);
    checkException("Bxf7", SanValidationProblem.NONE_BUT_CHECK, board);

    board.performMoves("a3", "Nc6", "Qf3", "d6");
    checkException("Qxf7+", SanValidationProblem.CHECK_BUT_CHECKMATE, board);
    checkException("Qxf7", SanValidationProblem.NONE_BUT_CHECKMATE, board);
  }

  @SuppressWarnings("static-method")
  @Test
  void testBlack() {
    final ApiBoard board = new Board();

    board.performMove("e4");

    checkException("e5#", SanValidationProblem.CHECKMATE_BUT_NONE, board);
    checkException("e5+", SanValidationProblem.CHECK_BUT_NONE, board);

    board.performMoves("e5", "Bc4", "Bc5", "Nc3");
    checkException("Bxf2#", SanValidationProblem.CHECKMATE_BUT_CHECK_ONLY, board);
    checkException("Bxf2", SanValidationProblem.NONE_BUT_CHECK, board);

    board.performMoves("Qf6", "d3");
    checkException("Qxf2+", SanValidationProblem.CHECK_BUT_CHECKMATE, board);
    checkException("Qxf2", SanValidationProblem.NONE_BUT_CHECKMATE, board);
  }

  private static void checkException(String san, SanValidationProblem problem, ApiBoard board) {
    boolean isException;
    try {
      board.performMove(san);
      isException = false;
    } catch (final SanValidationException e) {
      isException = true;
      assertEquals(problem, e.getSanValidationProblem());
    }
    assertTrue(isException);
  }

}