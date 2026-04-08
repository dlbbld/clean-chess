package com.dlb.chess.test.san;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.interfaces.ApiBoard;

class TestSanValidateMovingOntoOwnPiece extends AbstractTestSanValidate {

  @SuppressWarnings("static-method")
  @Test
  void testUnderstanding() {
    final ApiBoard board = new Board("8/3k4/3r4/R7/4K3/8/8/R7 b - - 0 1");

    checkExceptionMovingOntoOwnPiece("Rd6", board);
    board.performMoves("Rc6");
    // rook
    checkExceptionMovingOntoOwnPiece("Ra1", board);
  }

  @SuppressWarnings("static-method")
  @Test
  void testWhite() {

    final ApiBoard board = new Board();

    // rook
    checkExceptionCapturingOwnPiece("Rxa2", board);

    // knight
    checkExceptionCapturingOwnPiece("Nxd2", board);

    // bishop
    checkExceptionCapturingOwnPiece("Bxa2", board);

    // queen
    checkExceptionCapturingOwnPiece("Qxd2", board);

    // king
    checkExceptionCapturingOwnPiece("Kxf1", board);

    board.performMoves("Nc3");
    board.performMoves("a6");

    // pawn
    checkExceptionCapturingOwnPiece("bxc3", board);
  }

  @SuppressWarnings("static-method")
  @Test
  void testBlack() {

    final ApiBoard board = new Board();
    board.performMoves("e4");

    // rook
    checkExceptionCapturingOwnPiece("Rxa7", board);

    // knight
    checkExceptionCapturingOwnPiece("Nxd7", board);

    // bishop
    checkExceptionCapturingOwnPiece("Bxa7", board);

    // queen
    checkExceptionCapturingOwnPiece("Qxd7", board);

    // king
    checkExceptionCapturingOwnPiece("Kxf7", board);

    board.performMoves("Nc6");
    board.performMoves("d4");

    // pawn
    checkExceptionCapturingOwnPiece("bxc6", board);
  }
}
