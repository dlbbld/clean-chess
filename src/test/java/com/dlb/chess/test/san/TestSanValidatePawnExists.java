package com.dlb.chess.test.san;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.san.SanValidation;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;

class TestSanValidatePawnExists {

  @SuppressWarnings("static-method")
  @Test
  void testWhite() {

    {
      final ApiBoard board = new Board("4k3/5p2/8/8/8/8/3P4/4K3 w - - 0 100");

      checkException("c1", board, SanValidationProblem.FORMAT_PAWN_MISSING_PROMOTION);
      checkException("c2", board);
      checkException("c3", board);
      checkException("c4", board);
      checkException("c5", board);
      checkException("c6", board);
      checkException("c7", board);
      checkException("c8", board, SanValidationProblem.FORMAT_PAWN_MISSING_PROMOTION);

      checkException("fxg1", board, SanValidationProblem.FORMAT_PAWN_MISSING_PROMOTION);
      checkException("fxg2", board);
      checkException("fxg3", board);
      checkException("fxg4", board);
      checkException("fxg5", board);
      checkException("fxg6", board);
      checkException("fxg7", board);
      checkException("fxg8", board, SanValidationProblem.FORMAT_PAWN_MISSING_PROMOTION);
    }

  }

  @SuppressWarnings("static-method")
  @Test
  void testBlack() {

    {
      final ApiBoard board = new Board("4k3/5p2/8/8/8/8/3P4/4K3 b - - 0 100");

      checkException("h8", board, SanValidationProblem.FORMAT_PAWN_MISSING_PROMOTION);
      checkException("h7", board);
      checkException("h6", board);
      checkException("h5", board);
      checkException("h4", board);
      checkException("h3", board);
      checkException("h2", board);
      checkException("h1", board, SanValidationProblem.FORMAT_PAWN_MISSING_PROMOTION);

      checkException("bxa8", board, SanValidationProblem.FORMAT_PAWN_MISSING_PROMOTION);
      checkException("bxa7", board);
      checkException("bxa6", board);
      checkException("bxa5", board);
      checkException("bxa4", board);
      checkException("bxa3", board);
      checkException("bxa2", board);
      checkException("bxa1", board, SanValidationProblem.FORMAT_PAWN_MISSING_PROMOTION);

    }

  }

  private static void checkException(String san, ApiBoard board) {
    checkException(san, board, SanValidationProblem.PAWN_NO_PIECE_EXISTS);
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