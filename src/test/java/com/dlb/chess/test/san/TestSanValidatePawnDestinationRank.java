package com.dlb.chess.test.san;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.san.SanValidation;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;

class TestSanValidatePawnDestinationRank {

  @SuppressWarnings("static-method")
  @Test
  void testWhite() {

    {
      final ApiBoard board = new Board("4k3/5p2/8/8/8/8/3P4/4K3 w - - 0 100");

      checkException("d2", board);
      checkException("d1=Q", board);

      checkException("dxc2", board);
      checkException("dxc1=Q", board);

    }

  }

  @SuppressWarnings("static-method")
  @Test
  void testBlack() {

    {
      final ApiBoard board = new Board("4k3/5p2/8/8/8/8/3P4/4K3 b - - 0 100");

      checkException("f7", board);
      checkException("f8=Q", board);

      checkException("fxg7", board);
      checkException("fxg8=Q", board);

    }

  }

  private static void checkException(String san, ApiBoard board) {
    checkException(san, board, SanValidationProblem.PAWN_NON_REACHABLE_RANK);
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