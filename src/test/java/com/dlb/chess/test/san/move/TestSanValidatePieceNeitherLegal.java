package com.dlb.chess.test.san.move;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.san.validate.SanValidation;

class TestSanValidatePieceNeitherLegal {

  @SuppressWarnings("static-method")
  @Test
  void testPieceNeither1NoLegalMove() {

    final ApiBoard board = new Board();

    // white
    checkException(board, "Ra3", SanValidationProblem.PIECE_NEITHER_NOT_REACHABLE_MULTIPLE);
    checkException(board, "Nb4", SanValidationProblem.PIECE_NEITHER_NOT_REACHABLE_MULTIPLE);
    checkException(board, "Be3", SanValidationProblem.PIECE_NEITHER_NOT_REACHABLE_MULTIPLE);
    checkException(board, "Qd5", SanValidationProblem.PIECE_NEITHER_NOT_REACHABLE_SINGLE);
    checkException(board, "Rxa8", SanValidationProblem.PIECE_NEITHER_NOT_REACHABLE_MULTIPLE);
    checkException(board, "Nxb8", SanValidationProblem.PIECE_NEITHER_NOT_REACHABLE_MULTIPLE);
    checkException(board, "Bxc8", SanValidationProblem.PIECE_NEITHER_NOT_REACHABLE_MULTIPLE);
    checkException(board, "Qxd8", SanValidationProblem.PIECE_NEITHER_NOT_REACHABLE_SINGLE);

    // black
    board.performMoves("e4");
    checkException(board, "Ra6", SanValidationProblem.PIECE_NEITHER_NOT_REACHABLE_MULTIPLE);
    checkException(board, "Ne5", SanValidationProblem.PIECE_NEITHER_NOT_REACHABLE_MULTIPLE);
    checkException(board, "Bg4", SanValidationProblem.PIECE_NEITHER_NOT_REACHABLE_MULTIPLE);
    checkException(board, "Qd6", SanValidationProblem.PIECE_NEITHER_NOT_REACHABLE_SINGLE);
    checkException(board, "Rxa2", SanValidationProblem.PIECE_NEITHER_NOT_REACHABLE_MULTIPLE);
    checkException(board, "Nxb2", SanValidationProblem.PIECE_NEITHER_NOT_REACHABLE_MULTIPLE);
    checkException(board, "Bxc2", SanValidationProblem.PIECE_NEITHER_NOT_REACHABLE_MULTIPLE);
    checkException(board, "Qxd2", SanValidationProblem.PIECE_NEITHER_NOT_REACHABLE_SINGLE);

  }

  @SuppressWarnings("static-method")
  @Test
  void testPieceNeither2MultipleLegalMoves() {

    final ApiBoard board = new Board();

    // white
    // white rook
    board.performMoves("a4", "a5", "h4", "h5", "Ra3", "Ra6");
    checkException(board, "Rh3", SanValidationProblem.PIECE_NEITHER_MULTIPLE_LEGAL_MOVES);

    board.performMove("Rah3");
    // black rook
    checkException(board, "Rh6", SanValidationProblem.PIECE_NEITHER_MULTIPLE_LEGAL_MOVES);

    board.performMove("Rah6");

    // white knight
    board.performMoves("Nc3", "Nc6", "Nf3", "Nf6", "Nd4", "Nd5");
    checkException(board, "Nb5", SanValidationProblem.PIECE_NEITHER_MULTIPLE_LEGAL_MOVES);

    board.performMove("Ncb5");
    // black knight
    checkException(board, "Nb4", SanValidationProblem.PIECE_NEITHER_MULTIPLE_LEGAL_MOVES);

    board.performMove("Ncb4");

    // white bishop
    board.performMoves("f4", "c5", "f5", "c4", "f6", "c3", "fxg7", "cxb2", "gxh8=B", "e6", "d3", "Bd6", "Bd2", "Ke7",
        "Qa1", "bxa1=B", "Bxh6", "Bxd4");
    checkException(board, "Bg7", SanValidationProblem.PIECE_NEITHER_MULTIPLE_LEGAL_MOVES);

    board.performMove("B6g7");
    // black bishop
    checkException(board, "Be5", SanValidationProblem.PIECE_NEITHER_MULTIPLE_LEGAL_MOVES);

    board.performMove("B4e5");

    // white queen
    board.performMoves("c4", "f5", "cxd5", "f4", "dxe6", "f3", "exd7", "fxg2", "dxc8=Q", "gxh1=Q", "d4", "Qe4", "dxe5",
        "Kf7", "exd6");
    checkException(board, "Qxh4+", SanValidationProblem.PIECE_NEITHER_MULTIPLE_LEGAL_MOVES);

    board.performMove("Qdxh4+");
    board.performMoves("Kd1", "Na2", "d7", "Nb4", "d8=Q", "Nc2", "Na7", "Kg6", "Nb5", "Kh7", "Qdc7");
    // black queen
    checkException(board, "Qf4", SanValidationProblem.PIECE_NEITHER_MULTIPLE_LEGAL_MOVES);

    board.performMove("Qhf4");
    // white queen
    checkException(board, "Qd7", SanValidationProblem.PIECE_NEITHER_MULTIPLE_LEGAL_MOVES);

    board.performMove("Q8d7");
    board.performMoves("Qfe3", "Qf7");
    // black queen
    checkException(board, "Qf4", SanValidationProblem.PIECE_NEITHER_MULTIPLE_LEGAL_MOVES);

    board.performMove("Q4f4");
  }

  private static void checkException(ApiBoard board, String san, SanValidationProblem expectedValidation) {
    boolean isException;
    try {
      SanValidation.validateSan(san, board);
      isException = false;
    } catch (final SanValidationException e) {
      isException = true;
      assertEquals(expectedValidation, e.getSanValidationProblem());
    }
    assertTrue(isException);
  }
}
