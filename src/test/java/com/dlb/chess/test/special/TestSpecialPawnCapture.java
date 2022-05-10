package com.dlb.chess.test.special;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.san.exceptions.SanValidationException;

class TestSpecialPawnCapture implements EnumConstants {

  @SuppressWarnings("static-method")
  @Test
  void testWhite() throws Exception {
    // two pawns can capture the same piece
    // caused a bug
    final ApiBoard board = new Board("rrrrrrrr/PPPPPPPP/8/8/8/8/8/2k1K3 w - - 0 100");

    assertTrue(board.performMove("bxa8=Q"));
    board.unperformMove();
    assertTrue(board.performMove("bxc8=Q"));
    board.unperformMove();

    assertTrue(board.performMove("cxd8=Q"));
    board.unperformMove();

    checkException(board, "exd8=Q");
  }

  @SuppressWarnings("static-method")
  @Test
  void testBlack() throws Exception {
    // two pawns can capture the same piece
    // caused a bug
    final ApiBoard board = new Board("3k1K2/8/8/8/8/8/pppppppp/QQQQQQQQ b - - 0 100");

    assertTrue(board.performMove("bxa1=Q"));
    board.unperformMove();
    assertTrue(board.performMove("bxc1=Q"));
    board.unperformMove();

    assertTrue(board.performMove("fxe1=Q"));
    board.unperformMove();

    checkException(board, "dxe1=Q");

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
