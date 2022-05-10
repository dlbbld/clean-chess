package com.dlb.chess.test.san;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.interfaces.ApiBoard;

class TestSanValidateOntoItself extends AbstractTestSanValidate {

  @SuppressWarnings("static-method")
  @Test
  void test() {

    // attention for kings this is disallowed by format definition

    final ApiBoard board = new Board();

    checkExceptionMovingOntoItself("Ra1a1", board);
    checkExceptionMovingOntoItself("Nb1b1", board);
    checkExceptionMovingOntoItself("Bc1c1", board);
    checkExceptionMovingOntoItself("Qd1d1", board);
    checkExceptionFormat("Ke1e1", board);

    board.performMove("e4");

    checkExceptionMovingOntoItself("Ra8a8", board);
    checkExceptionMovingOntoItself("Nb8b8", board);
    checkExceptionMovingOntoItself("Bc8c8", board);
    checkExceptionMovingOntoItself("Qd8d8", board);
    checkExceptionFormat("Ke8e8", board);

    board.performMove("e5");

    // rooks after moved
    board.performMove("a4");
    board.performMove("h5");
    board.performMove("Ra2");
    board.performMove("Rh7");
    checkExceptionMovingOntoItself("Ra2a2", board);
    board.performMove("Ra3");
    checkExceptionMovingOntoItself("Rh7h7", board);
    board.performMove("Rh6");

    // knights after moved
    board.performMove("Nc3");
    board.performMove("Nf6");
    checkExceptionMovingOntoItself("Nc3c3", board);
    board.performMove("Nd5");
    checkExceptionMovingOntoItself("Nf6f6", board);
    board.performMove("Nxe4");

    // bishops after moved
    board.performMove("Bc4");
    board.performMove("d6");
    checkExceptionMovingOntoItself("Bc4c4", board);
    board.performMove("Bf1");
    board.performMove("Bd7");
    board.performMove("Bc4");
    checkExceptionMovingOntoItself("Bd7d7", board);
    board.performMove("Bg4");

    // queens after moved
    board.performMove("Qxg4");
    board.performMove("Qd7");
    checkExceptionMovingOntoItself("Qg4g4", board);
    board.performMove("Qh3");
    checkExceptionMovingOntoItself("Qd7d7", board);
    board.performMove("Qc6");

    // kings after moved
    board.performMove("Kd1");
    board.performMove("Kd8");
    checkExceptionFormat("Kd1d1", board);
    board.performMove("Ke2");
    checkExceptionFormat("Kd8d8", board);
    board.performMove("Ke8");

  }

}