package com.dlb.chess.test.custom;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.enums.CastlingMove;
import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.model.MoveSpecification;

class TestMoveSpecificationConstructorValidation {

  // we want to test the constructor checks for they are essential for further operation
  // so that only move specifications are created with the system thinks it can handle
  @SuppressWarnings("static-method")
  @Test
  void test() {
    checkException(Square.NONE, Square.A2);
    checkException(Square.A1, Square.NONE);
    checkException(Square.A1, Square.A1);

    checkException(Square.NONE, Square.A2, PromotionPieceType.BISHOP);
    checkException(Square.A1, Square.NONE, PromotionPieceType.BISHOP);
    checkException(Square.A1, Square.A2, PromotionPieceType.NONE);
    checkException(Square.A1, Square.A1, PromotionPieceType.BISHOP);

    checkException(CastlingMove.NONE);
  }

  @SuppressWarnings("unused")
  private static void checkException(Square fromSquare, Square toSquare) {
    boolean isException;
    try {
      new MoveSpecification(fromSquare, toSquare);
      isException = false;
    } catch (final IllegalArgumentException iae) {
      isException = true;
    }
    assertTrue(isException);
  }

  @SuppressWarnings("unused")
  private static void checkException(Square fromSquare, Square toSquare, PromotionPieceType promotionPieceType) {
    boolean isException;
    try {
      new MoveSpecification(fromSquare, toSquare, promotionPieceType);
      isException = false;
    } catch (final IllegalArgumentException iae) {
      isException = true;
    }
    assertTrue(isException);
  }

  @SuppressWarnings("unused")
  private static void checkException(CastlingMove castlingMove) {
    boolean isException;
    try {
      new MoveSpecification(castlingMove);
      isException = false;
    } catch (final IllegalArgumentException iae) {
      isException = true;
    }
    assertTrue(isException);
  }

}
