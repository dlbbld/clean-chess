package com.dlb.chess.test.san.destinationsquare;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.test.san.AbstractTestSanValidate;

class TestSanValidateMovingOntoItselfSquare extends AbstractTestSanValidate {

  @SuppressWarnings("static-method")
  @Test
  void test() {

    // attention for kings this is disallowed by format definition

    final ApiBoard board = new Board();

    checkExceptionMovingOntoItselfSquare("Ra1a1", board);
    checkExceptionMovingOntoItselfSquare("Nb1b1", board);
    checkExceptionMovingOntoItselfSquare("Bc1c1", board);
    checkExceptionMovingOntoItselfSquare("Qd1d1", board);
    checkExceptionFormat("Ke1e1", SanValidationProblem.FORMAT_KING_SQUARE_SPECIFIED, board);

    board.performMove("e4");

    checkExceptionMovingOntoItselfSquare("Ra8a8", board);
    checkExceptionMovingOntoItselfSquare("Nb8b8", board);
    checkExceptionMovingOntoItselfSquare("Bc8c8", board);
    checkExceptionMovingOntoItselfSquare("Qd8d8", board);
    checkExceptionFormat("Ke8e8", SanValidationProblem.FORMAT_KING_SQUARE_SPECIFIED, board);

    board.performMove("e5");

    // rooks after moved
    board.performMove("a4");
    board.performMove("h5");
    board.performMove("Ra2");
    board.performMove("Rh7");
    checkExceptionMovingOntoItselfSquare("Ra2a2", board);
    board.performMove("Ra3");
    checkExceptionMovingOntoItselfSquare("Rh7h7", board);
    board.performMove("Rh6");

    // knights after moved
    board.performMove("Nc3");
    board.performMove("Nf6");
    checkExceptionMovingOntoItselfSquare("Nc3c3", board);
    board.performMove("Nd5");
    checkExceptionMovingOntoItselfSquare("Nf6f6", board);
    board.performMove("Nxe4");

    // bishops after moved
    board.performMove("Bc4");
    board.performMove("d6");
    checkExceptionMovingOntoItselfSquare("Bc4c4", board);
    board.performMove("Bf1");
    board.performMove("Bd7");
    board.performMove("Bc4");
    checkExceptionMovingOntoItselfSquare("Bd7d7", board);
    board.performMove("Bg4");

    // queens after moved
    board.performMove("Qxg4");
    board.performMove("Qd7");
    checkExceptionMovingOntoItselfSquare("Qg4g4", board);
    board.performMove("Qh3");
    checkExceptionMovingOntoItselfSquare("Qd7d7", board);
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