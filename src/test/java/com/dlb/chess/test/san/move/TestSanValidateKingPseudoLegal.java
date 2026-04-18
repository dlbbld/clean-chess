package com.dlb.chess.test.san.move;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.san.validate.SanValidation;

class TestSanValidateKingPseudoLegal {

  // --- King left in check ---

  @SuppressWarnings("static-method")
  @Test
  void testWhiteKingLeftInCheck() {
    // White king e2 in check by black rook e8. Ke3 stays on the e-file, still attacked by the rook.
    final ApiBoard board = new Board("4r3/7k/8/8/8/8/4K3/8 w - - 0 1");
    checkException("Ke3", board, SanValidationProblem.KING_NON_CASTLING_KING_LEFT_IN_CHECK);
  }

  @SuppressWarnings("static-method")
  @Test
  void testBlackKingLeftInCheck() {
    // Black king e7 in check by white rook e1. Ke6 stays on the e-file, still attacked by the rook.
    final ApiBoard board = new Board("8/4k3/8/8/8/8/7K/4R3 b - - 0 1");
    checkException("Ke6", board, SanValidationProblem.KING_NON_CASTLING_KING_LEFT_IN_CHECK);
  }

  // --- King exposed to check ---

  @SuppressWarnings("static-method")
  @Test
  void testWhiteKingExposedToCheck() {
    // White king d1 not in check. Ke2 lands on the e-file attacked by black rook e8.
    final ApiBoard board = new Board("4r3/7k/8/8/8/8/8/3K4 w - - 0 1");
    checkException("Ke2", board, SanValidationProblem.KING_NON_CASTLING_KING_EXPOSED_TO_CHECK);
  }

  @SuppressWarnings("static-method")
  @Test
  void testBlackKingExposedToCheck() {
    // Black king d8 not in check. Ke7 lands on the e-file attacked by white rook e1.
    final ApiBoard board = new Board("3k4/8/8/8/8/8/7K/4R3 b - - 0 1");
    checkException("Ke7", board, SanValidationProblem.KING_NON_CASTLING_KING_EXPOSED_TO_CHECK);
  }

  private static void checkException(String san, ApiBoard board, SanValidationProblem expectedProblem) {
    boolean isException;
    try {
      SanValidation.validateSan(san, board);
      isException = false;
    } catch (final SanValidationException e) {
      isException = true;
      assertEquals(expectedProblem, e.getSanValidationProblem());
    }
    assertTrue(isException);
  }

}
