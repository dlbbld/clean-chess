package com.dlb.chess.test.san;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.san.validate.SanValidation;

class TestSanValidatePawnForwardMoves {

  @SuppressWarnings("static-method")
  @Test
  void testWhite() {

    {
      final ApiBoard board = new Board();

      board.performMove("d4");
      board.performMove("d5");
      checkExceptionOpponent("d5", board);
    }

    {
      final ApiBoard board = new Board();

      board.performMove("d4");
      board.performMove("e5");
      board.performMove("dxe5");
      board.performMove("a6");
      board.performMove("e4");
      board.performMove("a5");
      checkExceptionOwn("e5", board);
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testBlack() {

    {
      final ApiBoard board = new Board();

      board.performMove("d4");
      board.performMove("d5");
      board.performMove("a3");
      checkExceptionOpponent("d4", board);
    }

    {
      final ApiBoard board = new Board();

      board.performMove("d4");
      board.performMove("e5");
      board.performMove("a3");
      board.performMove("exd4");
      board.performMove("a4");
      board.performMove("d5");
      board.performMove("a5");
      checkExceptionOwn("d4", board);
    }
  }

  private static void checkExceptionOpponent(String san, ApiBoard board) {
    checkException(san, board, SanValidationProblem.NON_CAPTURING_MOVING_ONTO_OPPONENT_PIECE);
  }

  private static void checkExceptionOwn(String san, ApiBoard board) {
    checkException(san, board, SanValidationProblem.MOVING_ONTO_OWN_PIECE);
  }

  private static void checkException(String san, ApiBoard board, SanValidationProblem svp) {
    boolean isException;
    try {
      SanValidation.validateSan(san, board);
      isException = false;
    } catch (final SanValidationException e) {
      isException = true;
      assertEquals(svp, e.getSanValidationProblem());
    }
    assertTrue(isException);
  }

}