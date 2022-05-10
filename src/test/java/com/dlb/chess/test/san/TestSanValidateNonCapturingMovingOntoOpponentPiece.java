package com.dlb.chess.test.san;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.san.SanValidation;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;

class TestSanValidateNonCapturingMovingOntoOpponentPiece {

  @SuppressWarnings("static-method")
  @Test
  void testWhite() {

    // rook
    {
      final ApiBoard board = new Board();

      board.performMove("a4");
      board.performMove("d5");
      board.performMove("Ra3");
      board.performMove("Bh3");
      checkException("Rh3", board);
    }
    // knight
    {
      final ApiBoard board = new Board();

      board.performMove("Nc3");
      board.performMove("Nf6");
      board.performMove("Ne4");
      board.performMove("a6");
      checkException("Nf6", board);
    }
    // bishop
    {
      final ApiBoard board = new Board();

      board.performMove("e4");
      board.performMove("e5");
      board.performMove("Bb5");
      board.performMove("Nc6");
      checkException("Bc6", board);
    }
    // queen
    {
      final ApiBoard board = new Board();

      board.performMove("e4");
      board.performMove("e5");
      board.performMove("Qf3");
      board.performMove("d5");
      checkException("Qf7", board);
    }
    // king
    {
      final ApiBoard board = new Board();

      board.performMove("e4");
      board.performMove("e5");
      board.performMove("d3");
      board.performMove("Bc5");
      board.performMove("f4");
      board.performMove("Bf2+");
      checkException("Kf2", board);
    }
    // pawn diagonal move must contain capture symbol by format specification
  }

  @SuppressWarnings("static-method")
  @Test
  void testBlack() {
    // rook
    {
      final ApiBoard board = new Board();

      board.performMove("e4");
      board.performMove("h5");
      board.performMove("Ba6");
      board.performMove("Rh6");
      board.performMove("a3");
      checkException("Ra6", board);
    }
    // knight
    {
      final ApiBoard board = new Board();

      board.performMove("Nc3");
      board.performMove("Nf6");
      board.performMove("Ne4");
      checkException("Ne4", board);
    }
    // bishop
    {
      final ApiBoard board = new Board();

      board.performMove("Nf3");
      board.performMove("d5");
      board.performMove("e4");
      board.performMove("Bg4");
      board.performMove("d4");
      checkException("Bf3", board);
    }
    // queen
    {
      final ApiBoard board = new Board();

      board.performMove("e4");
      board.performMove("e5");
      board.performMove("a3");
      board.performMove("Qf6");
      board.performMove("d4");
      checkException("Qf2", board);
    }
    // king
    {
      final ApiBoard board = new Board();

      board.performMove("e4");
      board.performMove("e5");
      board.performMove("Bc4");
      board.performMove("d6");
      board.performMove("Bxf7+");
      checkException("Kf7", board);
    }
    // pawn diagonal move must contain capture symbol by format specification
  }

  private static void checkException(String san, ApiBoard board) {
    boolean isException;
    try {
      SanValidation.calculateMoveSpecificationForSan(san, board);
      isException = false;
    } catch (final SanValidationException e) {
      isException = true;
      assertEquals(SanValidationProblem.NON_CAPTURING_MOVING_ONTO_OPPONENT_PIECE, e.getSanValidationProblem());
    }
    assertTrue(isException);
  }

}