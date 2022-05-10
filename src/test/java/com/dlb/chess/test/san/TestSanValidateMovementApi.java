package com.dlb.chess.test.san;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.interfaces.ApiBoard;

class TestSanValidateMovementApi extends AbstractTestSanValidate {

  @SuppressWarnings("static-method")
  @Test
  void testPromotionRankWhite() {

    final ApiBoard board = new Board();
    board.performMoves("e4", "d5");
    checkExceptionPromotionRank("exd5=Q", board);
  }

  @SuppressWarnings("static-method")
  @Test
  void testPromotionRankBlack() {

    final ApiBoard board = new Board();
    board.performMoves("e4", "d5");
    board.performMoves("a3");
    checkExceptionPromotionRank("dxe4=N", board);
  }

  @SuppressWarnings("static-method")
  @Test
  void testImpossibleMovementWhite() {

    // white
    final ApiBoard board = new Board();

    // pawn backwards
    checkExceptionPawnToRank("d1", board);

    // pawn capture right
    checkExceptionPawnFromFile("dxf6", board);

    // pawn capture left
    checkExceptionPawnFromFile("dxb6", board);

    // rook nothing

    // knight using file
    checkExceptionNonPawnFromFile("Nbe3", board);

    // knight using rank
    checkExceptionNonPawnFromRank("N1c4", board);

    // knight using square - impossible
    checkExceptionNonPawnFromSquare("Nb1b2", board);

    // knight using square - onto itself
    checkExceptionMovingOntoItself("Nb1b1", board);

    // bishop using file
    checkExceptionNonPawnFromFile("Baa8", board);

    // bishop using rank
    checkExceptionNonPawnFromRank("B1a1", board);

    // bishop using square - impossible
    checkExceptionNonPawnFromSquare("Ba1b1", board);

    // bishop using square - onto itself
    checkExceptionMovingOntoItself("Ba1a1", board);

    // queen using file - nothing
    // queen using rank - nothing
    // queen using square - impossible
    // queen using square - onto itself
    checkExceptionMovingOntoItself("Qa1a1", board);

    // king nothing
  }

  @SuppressWarnings("static-method")
  @Test
  void testImpossibleMovementBlack() {

    // black
    // only difference in pawn backward move

    final ApiBoard board = new Board();
    board.performMoves("e4");
    // pawn backwards
    checkExceptionPawnToRank("d8", board);

    // pawn capture right
    checkExceptionPawnFromFile("dxf6", board);
    // pawn capture left
    checkExceptionPawnFromFile("dxb6", board);

    // rook nothing

    // knight using file
    checkExceptionNonPawnFromFile("Nbe3", board);
    // knight using rank
    checkExceptionNonPawnFromRank("N1c4", board);
    // knight using square - impossible
    checkExceptionNonPawnFromSquare("Nb1b2", board);
    // knight using square - onto itself
    checkExceptionMovingOntoItself("Nb1b1", board);

    // bishop using file
    checkExceptionNonPawnFromFile("Baa8", board);
    // bishop using rank
    checkExceptionNonPawnFromRank("B1a1", board);
    // bishop using square - impossible
    checkExceptionNonPawnFromSquare("Ba1b1", board);
    // bishop using square - onto itself
    checkExceptionMovingOntoItself("Ba1a1", board);

    // queen using file - nothing
    // queen using rank - nothing
    // queen using square - impossible
    // queen using square - onto itself
    checkExceptionMovingOntoItself("Qa1a1", board);

    // king nothing
  }

}