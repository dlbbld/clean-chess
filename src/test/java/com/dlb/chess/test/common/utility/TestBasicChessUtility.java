package com.dlb.chess.test.common.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.utility.BasicChessUtility;
import com.dlb.chess.common.utility.FenUtility;
import com.dlb.chess.fen.constants.FenConstants;

class TestBasicChessUtility {

  @SuppressWarnings("static-method")
  @Test
  void testSideHavingMoveForSide() {

    assertEquals(Side.WHITE, BasicChessUtility.calculateSideHavingMoveForSide("w"));
    assertEquals(Side.BLACK, BasicChessUtility.calculateSideHavingMoveForSide("b"));

    var isException = false;
    try {
      assertEquals(Side.BLACK, BasicChessUtility.calculateSideHavingMoveForSide("x"));
    } catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
      isException = true;
    }
    assertTrue(isException);
  }

  @SuppressWarnings("static-method")
  @Test
  void testSideHavingMoveForFen() {

    assertEquals(Side.WHITE, FenUtility.calculateSideHavingMoveForFen(FenConstants.FEN_INITIAL_STR));
    assertEquals(Side.BLACK, FenUtility.calculateSideHavingMoveForFen(FenConstants.FEN_AFTER_E4_STR));

    var isException = false;
    try {
      @SuppressWarnings("null") @NonNull final String invalidFen = FenConstants.FEN_INITIAL_STR.replace(" w ", " x ");
      FenUtility.calculateSideHavingMoveForFen(invalidFen);
    } catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
      isException = true;
    }
    assertTrue(isException);
  }

  @SuppressWarnings("static-method")
  @Test
  void testSideMoved() {

    assertEquals(Side.BLACK, BasicChessUtility.calculateSideMoved(Side.WHITE, -2));
    assertEquals(Side.WHITE, BasicChessUtility.calculateSideMoved(Side.WHITE, -1));
    assertEquals(Side.BLACK, BasicChessUtility.calculateSideMoved(Side.WHITE, 0));

    assertEquals(Side.WHITE, BasicChessUtility.calculateSideMoved(Side.WHITE, 1));
    assertEquals(Side.BLACK, BasicChessUtility.calculateSideMoved(Side.WHITE, 2));

    assertEquals(Side.WHITE, BasicChessUtility.calculateSideMoved(Side.BLACK, -2));
    assertEquals(Side.BLACK, BasicChessUtility.calculateSideMoved(Side.BLACK, -1));
    assertEquals(Side.WHITE, BasicChessUtility.calculateSideMoved(Side.BLACK, 0));

    assertEquals(Side.BLACK, BasicChessUtility.calculateSideMoved(Side.BLACK, 1));
    assertEquals(Side.WHITE, BasicChessUtility.calculateSideMoved(Side.BLACK, 2));

  }

  @SuppressWarnings("static-method")
  @Test
  void testFullMoveNumberBackwards() {

    // not performed
    assertEquals(9, BasicChessUtility.calculateFullMoveNumber(Side.WHITE, 10, 0));
    assertEquals(9, BasicChessUtility.calculateFullMoveNumber(Side.WHITE, 10, -1));

    assertEquals(8, BasicChessUtility.calculateFullMoveNumber(Side.WHITE, 10, -2));
    assertEquals(8, BasicChessUtility.calculateFullMoveNumber(Side.WHITE, 10, -3));

    assertEquals(7, BasicChessUtility.calculateFullMoveNumber(Side.WHITE, 10, -4));
    assertEquals(7, BasicChessUtility.calculateFullMoveNumber(Side.WHITE, 10, -5));

    assertEquals(10, BasicChessUtility.calculateFullMoveNumber(Side.BLACK, 10, 0));
    assertEquals(9, BasicChessUtility.calculateFullMoveNumber(Side.BLACK, 10, -1));

    assertEquals(9, BasicChessUtility.calculateFullMoveNumber(Side.BLACK, 10, -2));
    assertEquals(8, BasicChessUtility.calculateFullMoveNumber(Side.BLACK, 10, -3));

    assertEquals(8, BasicChessUtility.calculateFullMoveNumber(Side.BLACK, 10, -4));
    assertEquals(7, BasicChessUtility.calculateFullMoveNumber(Side.BLACK, 10, -5));

    // performed
    assertEquals(1, BasicChessUtility.calculateFullMoveNumber(Side.WHITE, 1, 1));
    assertEquals(1, BasicChessUtility.calculateFullMoveNumber(Side.WHITE, 1, 2));

    assertEquals(2, BasicChessUtility.calculateFullMoveNumber(Side.WHITE, 1, 3));
    assertEquals(2, BasicChessUtility.calculateFullMoveNumber(Side.WHITE, 1, 4));

    assertEquals(3, BasicChessUtility.calculateFullMoveNumber(Side.WHITE, 1, 5));
    assertEquals(3, BasicChessUtility.calculateFullMoveNumber(Side.WHITE, 1, 6));

    assertEquals(1, BasicChessUtility.calculateFullMoveNumber(Side.BLACK, 1, 1));
    assertEquals(2, BasicChessUtility.calculateFullMoveNumber(Side.BLACK, 1, 2));

    assertEquals(2, BasicChessUtility.calculateFullMoveNumber(Side.BLACK, 1, 3));
    assertEquals(3, BasicChessUtility.calculateFullMoveNumber(Side.BLACK, 1, 4));

    assertEquals(3, BasicChessUtility.calculateFullMoveNumber(Side.BLACK, 1, 5));
    assertEquals(4, BasicChessUtility.calculateFullMoveNumber(Side.BLACK, 1, 6));
  }
}