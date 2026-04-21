package com.dlb.chess.test.san.destinationsquare;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.test.san.AbstractTestSanValidate;

class TestSanValidateNonMovement extends AbstractTestSanValidate {

  @SuppressWarnings("static-method")
  @Test
  void test() {

    // attention for kings this is disallowed by format definition

    final ApiBoard board = new Board();

    checkExceptionNonMovement("Ra1a1", board);
    checkExceptionNonMovement("Nb1b1", board);
    checkExceptionNonMovement("Bc1c1", board);
    checkExceptionNonMovement("Qd1d1", board);
    checkExceptionFormat("Ke1e1", SanValidationProblem.FORMAT_KING_NON_CASTLING_NON_CAPTURE_OVERLENGTH, board);

    board.performMove("e4");

    checkExceptionNonMovement("Ra8a8", board);
    checkExceptionNonMovement("Nb8b8", board);
    checkExceptionNonMovement("Bc8c8", board);
    checkExceptionNonMovement("Qd8d8", board);
    checkExceptionFormat("Ke8e8", SanValidationProblem.FORMAT_KING_NON_CASTLING_NON_CAPTURE_OVERLENGTH, board);

    board.performMove("e5");

    // rooks after moved
    board.performMove("a4");
    board.performMove("h5");
    board.performMove("Ra2");
    board.performMove("Rh7");
    checkExceptionNonMovement("Ra2a2", board);
    board.performMove("Ra3");
    checkExceptionNonMovement("Rh7h7", board);
    board.performMove("Rh6");

    // knights after moved
    board.performMove("Nc3");
    board.performMove("Nf6");
    checkExceptionNonMovement("Nc3c3", board);
    board.performMove("Nd5");
    checkExceptionNonMovement("Nf6f6", board);
    board.performMove("Nxe4");

    // bishops after moved
    board.performMove("Bc4");
    board.performMove("d6");
    checkExceptionNonMovement("Bc4c4", board);
    board.performMove("Bf1");
    board.performMove("Bd7");
    board.performMove("Bc4");
    checkExceptionNonMovement("Bd7d7", board);
    board.performMove("Bg4");

    // queens after moved
    board.performMove("Qxg4");
    board.performMove("Qd7");
    checkExceptionNonMovement("Qg4g4", board);
    board.performMove("Qh3");
    checkExceptionNonMovement("Qd7d7", board);
    board.performMove("Qc6");

    // kings after moved
    board.performMove("Kd1");
    board.performMove("Kd8");
    checkExceptionFormat("Kd1d1", SanValidationProblem.FORMAT_KING_NON_CASTLING_NON_CAPTURE_OVERLENGTH, board);
    board.performMove("Ke2");
    checkExceptionFormat("Kd8d8", SanValidationProblem.FORMAT_KING_NON_CASTLING_NON_CAPTURE_OVERLENGTH, board);
    board.performMove("Ke8");

  }

}