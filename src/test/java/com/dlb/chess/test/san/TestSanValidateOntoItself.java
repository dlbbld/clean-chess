package com.dlb.chess.test.san;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.san.enums.SanValidationProblem;

class TestSanValidateOntoItself extends AbstractTestSanValidate {

  @SuppressWarnings("static-method")
  @Test
  void test() {

    // attention for kings this is disallowed by format definition

    final ApiBoard board = new Board();

    checkExceptionMovingOntoOwnPiece("Ra1a1", board);
    checkExceptionMovingOntoOwnPiece("Nb1b1", board);
    checkExceptionMovingOntoOwnPiece("Bc1c1", board);
    checkExceptionMovingOntoOwnPiece("Qd1d1", board);
    checkExceptionFormat("Ke1e1", SanValidationProblem.FORMAT_KING_SQUARE_SPECIFIED, board);

    board.performMove("e4");

    checkExceptionMovingOntoOwnPiece("Ra8a8", board);
    checkExceptionMovingOntoOwnPiece("Nb8b8", board);
    checkExceptionMovingOntoOwnPiece("Bc8c8", board);
    checkExceptionMovingOntoOwnPiece("Qd8d8", board);
    checkExceptionFormat("Ke8e8", SanValidationProblem.FORMAT_KING_SQUARE_SPECIFIED, board);

    board.performMove("e5");

    // rooks after moved
    board.performMove("a4");
    board.performMove("h5");
    board.performMove("Ra2");
    board.performMove("Rh7");
    checkExceptionMovingOntoOwnPiece("Ra2a2", board);
    board.performMove("Ra3");
    checkExceptionMovingOntoOwnPiece("Rh7h7", board);
    board.performMove("Rh6");

    // knights after moved
    board.performMove("Nc3");
    board.performMove("Nf6");
    checkExceptionMovingOntoOwnPiece("Nc3c3", board);
    board.performMove("Nd5");
    checkExceptionMovingOntoOwnPiece("Nf6f6", board);
    board.performMove("Nxe4");

    // bishops after moved
    board.performMove("Bc4");
    board.performMove("d6");
    checkExceptionMovingOntoOwnPiece("Bc4c4", board);
    board.performMove("Bf1");
    board.performMove("Bd7");
    board.performMove("Bc4");
    checkExceptionMovingOntoOwnPiece("Bd7d7", board);
    board.performMove("Bg4");

    // queens after moved
    board.performMove("Qxg4");
    board.performMove("Qd7");
    checkExceptionMovingOntoOwnPiece("Qg4g4", board);
    board.performMove("Qh3");
    checkExceptionMovingOntoOwnPiece("Qd7d7", board);
    board.performMove("Qc6");

    // kings after moved
    board.performMove("Kd1");
    board.performMove("Kd8");
    checkExceptionFormat("Kd1d1", SanValidationProblem.FORMAT_KING_SQUARE_SPECIFIED, board);
    board.performMove("Ke2");
    checkExceptionFormat("Kd8d8", SanValidationProblem.FORMAT_KING_SQUARE_SPECIFIED, board);
    board.performMove("Ke8");

  }

}