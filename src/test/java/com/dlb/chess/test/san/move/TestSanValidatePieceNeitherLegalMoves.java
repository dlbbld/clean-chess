package com.dlb.chess.test.san.move;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.san.validate.SanValidation;

class TestSanValidatePieceNeitherLegalMoves {

  // --- Not reachable, single piece ---

  @SuppressWarnings("static-method")
  @Test
  void testWhiteNotReachableSingle() {
    // Single white bishop on c1, blocked by pawns. Cannot reach e3.
    final ApiBoard board = new Board("k7/8/8/8/8/8/PPPPPPPP/2B1K3 w - - 0 1");
    checkException("Be3", board, SanValidationProblem.PIECE_NEITHER_NOT_REACHABLE_SINGLE);
  }

  @SuppressWarnings("static-method")
  @Test
  void testBlackNotReachableSingle() {
    // Single black bishop on c8, blocked by pawns. Cannot reach e6.
    final ApiBoard board = new Board("2b1k3/pppppppp/8/8/8/8/8/4K3 b - - 0 1");
    checkException("Be6", board, SanValidationProblem.PIECE_NEITHER_NOT_REACHABLE_SINGLE);
  }

  // --- Not reachable, multiple pieces ---

  @SuppressWarnings("static-method")
  @Test
  void testWhiteNotReachableMultiple() {
    // Two white knights on b1 and g1, blocked by pawns. Neither can reach e5.
    final ApiBoard board = new Board("k7/8/8/8/8/8/PPPPPPPP/1N2K1N1 w - - 0 1");
    checkException("Ne5", board, SanValidationProblem.PIECE_NEITHER_NOT_REACHABLE_MULTIPLE);
  }

  @SuppressWarnings("static-method")
  @Test
  void testBlackNotReachableMultiple() {
    // Two black knights on b8 and g8, blocked by pawns. Neither can reach e4.
    final ApiBoard board = new Board("1n2k1n1/pppppppp/8/8/8/8/8/4K3 b - - 0 1");
    checkException("Ne4", board, SanValidationProblem.PIECE_NEITHER_NOT_REACHABLE_MULTIPLE);
  }

  // --- King in check, single pseudo-legal move ---

  @SuppressWarnings("static-method")
  @Test
  void testWhiteKingInCheckSingle() {
    // White bishop on e4 pinned along e-file (king e1, rook e8). Bishop can reach d5 but would expose king.
    final ApiBoard board = new Board("4r2k/8/8/8/4B3/8/8/4K3 w - - 0 1");
    checkException("Bd5", board, SanValidationProblem.PIECE_NEITHER_KING_IN_CHECK_SINGLE);
  }

  @SuppressWarnings("static-method")
  @Test
  void testBlackKingInCheckSingle() {
    // Black bishop on e5 pinned along e-file (king e8, rook e1). Bishop can reach d4 but would expose king.
    final ApiBoard board = new Board("4k3/8/8/4b3/8/8/8/4R2K b - - 0 1");
    checkException("Bd4", board, SanValidationProblem.PIECE_NEITHER_KING_IN_CHECK_SINGLE);
  }

  // --- King in check, multiple pseudo-legal moves ---

  @SuppressWarnings("static-method")
  @Test
  void testWhiteKingInCheckMultiple() {
    // White knights on d2 and f2, pinned on separate diagonals from e1 (bishops b4 and g3).
    // Both can reach e4 but each would expose king on its diagonal.
    final ApiBoard board = new Board("k7/8/8/8/1b6/6b1/3N1N2/4K3 w - - 0 1");
    checkException("Ne4", board, SanValidationProblem.PIECE_NEITHER_KING_IN_CHECK_MULTIPLE);
  }

  @SuppressWarnings("static-method")
  @Test
  void testBlackKingInCheckMultiple() {
    // Black knights on d7 and f7, pinned on separate diagonals from e8 (bishops b5 and g6).
    // Both can reach e5 but each would expose king on its diagonal.
    final ApiBoard board = new Board("4k3/3n1n2/6B1/1B6/8/8/8/K7 b - - 0 1");
    checkException("Ne5", board, SanValidationProblem.PIECE_NEITHER_KING_IN_CHECK_MULTIPLE);
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
