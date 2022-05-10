package com.dlb.chess.test.special;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.constants.EnumConstants;

class TestSpecialReverseMethods implements EnumConstants {

  @SuppressWarnings("static-method")
  @Test
  void testSquareDirections() {
    for (final Square square : Square.values()) {
      if (square == Square.NONE) {
        continue;
      }
      for (final Side side : Side.values()) {
        if (side == Side.NONE) {
          continue;
        }
        if (Square.calculateHasLeftDiagonalSquare(side, square)) {
          final Square calculatedSquare = Square.calculateLeftDiagonalSquare(side, square);
          assertTrue(Square.calculateIsLeftDiagonalSquare(side, square, calculatedSquare));
          final Square revertedSquare = Square.calculateLeftDiagonalSquare(side.getOppositeSide(), calculatedSquare);
          assertEquals(square, revertedSquare);
        }
        if (Square.calculateHasRightDiagonalSquare(side, square)) {
          final Square calculatedSquare = Square.calculateRightDiagonalSquare(side, square);
          assertTrue(Square.calculateIsRightDiagonalSquare(side, square, calculatedSquare));
          final Square revertedSquare = Square.calculateRightDiagonalSquare(side.getOppositeSide(), calculatedSquare);
          assertEquals(square, revertedSquare);
        }
        if (Square.calculateHasAheadSquare(side, square)) {
          final Square calculatedSquare = Square.calculateAheadSquare(side, square);
          final Square revertedSquare = Square.calculateBehindSquare(side, calculatedSquare);
          assertEquals(square, revertedSquare);
        }
        if (Square.calculateHasBehindSquare(side, square)) {
          final Square calculatedSquare = Square.calculateBehindSquare(side, square);
          final Square revertedSquare = Square.calculateAheadSquare(side, calculatedSquare);
          assertEquals(square, revertedSquare);
        }
        if (Square.calculateHasLeftSquare(side, square)) {
          final Square calculatedSquare = Square.calculateLeftSquare(side, square);
          final Square revertedSquare = Square.calculateRightSquare(side, calculatedSquare);
          assertEquals(square, revertedSquare);
        }
        if (Square.calculateHasRightSquare(side, square)) {
          final Square calculatedSquare = Square.calculateRightSquare(side, square);
          final Square revertedSquare = Square.calculateLeftSquare(side, calculatedSquare);
          assertEquals(square, revertedSquare);
        }
      }
    }
  }

}
