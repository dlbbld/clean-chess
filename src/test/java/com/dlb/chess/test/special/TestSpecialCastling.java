package com.dlb.chess.test.special;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.san.exceptions.SanValidationException;

class TestSpecialCastling implements EnumConstants {

  @SuppressWarnings("static-method")
  @Test
  void testWhite() throws Exception {
    {
      // white has king-side castling rights but not queen side
      // try queen side castling
      final ApiBoard board = new Board("4k3/8/8/8/8/8/8/R3K2R w K - 0 100");

      assertTrue(board.performMove("O-O"));
      board.unperformMove();

      checkException(board, "O-O-O");
    }
    {
      // white has queen-side castling rights but not king side
      // try king side castling
      final ApiBoard board = new Board("4k3/8/8/8/8/8/8/R3K2R w Q - 0 100");

      assertTrue(board.performMove("O-O-O"));
      board.unperformMove();

      checkException(board, "O-O");
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testBlack() throws Exception {
    {
      // black has king-side castling rights but not queen side
      // try queen side castling
      final ApiBoard board = new Board("r3k2r/8/8/8/8/8/8/R3K2R b k - 0 100");

      assertTrue(board.performMove("O-O"));
      board.unperformMove();

      checkException(board, "O-O-O");
    }
    {
      // black has queen-side castling rights but not king side
      // try king side castling
      final ApiBoard board = new Board("r3k2r/8/8/8/8/8/8/R3K2R b q - 0 100");

      assertTrue(board.performMove("O-O-O"));
      board.unperformMove();

      checkException(board, "O-O");
    }
  }

  private static void checkException(ApiBoard board, String san) {
    var isCorrectException = false;
    try {
      board.performMove(san);
    } catch (@SuppressWarnings("unused") final SanValidationException sve) {
      isCorrectException = true;
    }
    assertTrue(isCorrectException);
  }
}
