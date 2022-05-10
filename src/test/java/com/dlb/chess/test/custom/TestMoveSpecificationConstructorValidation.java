package com.dlb.chess.test.custom;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.enums.CastlingMove;
import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.model.MoveSpecification;

class TestMoveSpecificationConstructorValidation {

  // we want to test the constructor checks for they are essential for further operation
  // so that only move specifications are created with the system thinks it can handle
  @SuppressWarnings("static-method")
  @Test
  void test() {
    checkException(Side.NONE, Square.A1, Square.A2);
    checkException(Side.WHITE, Square.NONE, Square.A2);
    checkException(Side.WHITE, Square.A1, Square.NONE);

    checkException(Side.WHITE, Square.A1, Square.A1);

    checkException(Side.NONE, Square.A1, Square.A2, PromotionPieceType.BISHOP);
    checkException(Side.WHITE, Square.NONE, Square.A2, PromotionPieceType.BISHOP);
    checkException(Side.WHITE, Square.A1, Square.NONE, PromotionPieceType.BISHOP);
    checkException(Side.WHITE, Square.A1, Square.A2, PromotionPieceType.NONE);

    checkException(Side.WHITE, Square.A1, Square.A1, PromotionPieceType.BISHOP);

    checkException(Side.NONE, CastlingMove.KING_SIDE);
    checkException(Side.WHITE, CastlingMove.NONE);
  }

  @SuppressWarnings("unused")
  private static void checkException(Side havingMove, Square fromSquare, Square toSquare) {
    boolean isException;
    try {
      new MoveSpecification(havingMove, fromSquare, toSquare);
      isException = false;
    } catch (final IllegalArgumentException iae) {
      isException = true;
    }
    assertTrue(isException);
  }

  @SuppressWarnings("unused")
  private static void checkException(Side havingMove, Square fromSquare, Square toSquare,
      PromotionPieceType promotionPieceType) {
    boolean isException;
    try {
      new MoveSpecification(havingMove, fromSquare, toSquare, promotionPieceType);
      isException = false;
    } catch (final IllegalArgumentException iae) {
      isException = true;
    }
    assertTrue(isException);
  }

  @SuppressWarnings("unused")
  private static void checkException(Side havingMove, CastlingMove castlingMove) {
    boolean isException;
    try {
      new MoveSpecification(havingMove, castlingMove);
      isException = false;
    } catch (final IllegalArgumentException iae) {
      isException = true;
    }
    assertTrue(isException);
  }

}
