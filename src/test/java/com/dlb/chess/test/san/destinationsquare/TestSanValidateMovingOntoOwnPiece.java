package com.dlb.chess.test.san.destinationsquare;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.test.san.AbstractTestSanValidate;

class TestSanValidateMovingOntoOwnPiece extends AbstractTestSanValidate {

  @SuppressWarnings("static-method")
  @Test
  void testUnderstanding() {
    final ApiBoard board = new Board("8/3k4/3r4/R7/4K3/8/8/R7 b - - 0 1");

    checkExceptionRnbqkMovingOntoOwnPiece("Rd6", board);
    board.performMoves("Rc6");
    // rook
    checkExceptionRnbqkMovingOntoOwnPiece("Ra1", board);
  }

  @SuppressWarnings("static-method")
  @Test
  void testWhite() {

    final ApiBoard board = new Board();

    // rook
    checkExceptionRnbqkCapturingOwnPiece("Rxa2", board);

    // knight
    checkExceptionRnbqkCapturingOwnPiece("Nxd2", board);

    // bishop
    checkExceptionRnbqkCapturingOwnPiece("Bxa2", board);

    // queen
    checkExceptionRnbqkCapturingOwnPiece("Qxd2", board);

    // king
    checkExceptionRnbqkCapturingOwnPiece("Kxf1", board);

    board.performMoves("Nc3");
    board.performMoves("a6");

    // pawn
    checkExceptionPawnCaptureOwnPiece("bxc3", board);
  }

  @SuppressWarnings("static-method")
  @Test
  void testBlack() {

    final ApiBoard board = new Board();
    board.performMoves("e4");

    // rook
    checkExceptionRnbqkCapturingOwnPiece("Rxa7", board);

    // knight
    checkExceptionRnbqkCapturingOwnPiece("Nxd7", board);

    // bishop
    checkExceptionRnbqkCapturingOwnPiece("Bxa7", board);

    // queen
    checkExceptionRnbqkCapturingOwnPiece("Qxd7", board);

    // king
    checkExceptionRnbqkCapturingOwnPiece("Kxf7", board);

    board.performMoves("Nc6");
    board.performMoves("d4");

    // pawn
    checkExceptionPawnCaptureOwnPiece("bxc6", board);
  }
}
