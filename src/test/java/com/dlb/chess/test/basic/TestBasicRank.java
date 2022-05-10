package com.dlb.chess.test.basic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.constants.ChessConstants;
import com.dlb.chess.common.constants.EnumConstants;

class TestBasicRank implements EnumConstants {

  @SuppressWarnings("static-method")
  @Test
  void testCount() throws Exception {
    var totalRanks = 0;
    for (final Rank rank : Rank.values()) {
      if (rank != Rank.NONE) {
        totalRanks++;
      }
    }
    assertEquals(8, totalRanks);
  }

  @SuppressWarnings("static-method")
  @Test
  void testMethodsDirect() throws Exception {
    assertFalse(Rank.exists(-1));
    assertFalse(Rank.exists(0));
    assertFalse(Rank.exists(9));

    assertTrue(Rank.exists(ChessConstants.RANK_1_NUMBER));
    assertTrue(Rank.exists(ChessConstants.RANK_2_NUMBER));
    assertTrue(Rank.exists(ChessConstants.RANK_3_NUMBER));
    assertTrue(Rank.exists(ChessConstants.RANK_4_NUMBER));
    assertTrue(Rank.exists(ChessConstants.RANK_5_NUMBER));
    assertTrue(Rank.exists(ChessConstants.RANK_6_NUMBER));
    assertTrue(Rank.exists(ChessConstants.RANK_7_NUMBER));
    assertTrue(Rank.exists(ChessConstants.RANK_8_NUMBER));

    assertEquals(Rank.RANK_1, Rank.calculateRank(ChessConstants.RANK_1_NUMBER));
    assertEquals(Rank.RANK_2, Rank.calculateRank(ChessConstants.RANK_2_NUMBER));
    assertEquals(Rank.RANK_3, Rank.calculateRank(ChessConstants.RANK_3_NUMBER));
    assertEquals(Rank.RANK_4, Rank.calculateRank(ChessConstants.RANK_4_NUMBER));
    assertEquals(Rank.RANK_5, Rank.calculateRank(ChessConstants.RANK_5_NUMBER));
    assertEquals(Rank.RANK_6, Rank.calculateRank(ChessConstants.RANK_6_NUMBER));
    assertEquals(Rank.RANK_7, Rank.calculateRank(ChessConstants.RANK_7_NUMBER));
    assertEquals(Rank.RANK_8, Rank.calculateRank(ChessConstants.RANK_8_NUMBER));
  }

  @SuppressWarnings("static-method")
  @Test
  void testMethodsAdjacent() throws Exception {
    // white existence
    assertTrue(Rank.calculateHasNextRank(WHITE, RANK_1));
    assertTrue(Rank.calculateHasNextRank(WHITE, RANK_2));
    assertTrue(Rank.calculateHasNextRank(WHITE, RANK_3));
    assertTrue(Rank.calculateHasNextRank(WHITE, RANK_4));
    assertTrue(Rank.calculateHasNextRank(WHITE, RANK_5));
    assertTrue(Rank.calculateHasNextRank(WHITE, RANK_6));
    assertTrue(Rank.calculateHasNextRank(WHITE, RANK_7));
    assertFalse(Rank.calculateHasNextRank(WHITE, RANK_8));

    assertFalse(Rank.calculateHasPreviousRank(WHITE, RANK_1));
    assertTrue(Rank.calculateHasPreviousRank(WHITE, RANK_2));
    assertTrue(Rank.calculateHasPreviousRank(WHITE, RANK_3));
    assertTrue(Rank.calculateHasPreviousRank(WHITE, RANK_4));
    assertTrue(Rank.calculateHasPreviousRank(WHITE, RANK_5));
    assertTrue(Rank.calculateHasPreviousRank(WHITE, RANK_6));
    assertTrue(Rank.calculateHasPreviousRank(WHITE, RANK_7));
    assertTrue(Rank.calculateHasPreviousRank(WHITE, RANK_8));

    // black existence
    assertFalse(Rank.calculateHasNextRank(BLACK, RANK_1));
    assertTrue(Rank.calculateHasNextRank(BLACK, RANK_2));
    assertTrue(Rank.calculateHasNextRank(BLACK, RANK_3));
    assertTrue(Rank.calculateHasNextRank(BLACK, RANK_4));
    assertTrue(Rank.calculateHasNextRank(BLACK, RANK_5));
    assertTrue(Rank.calculateHasNextRank(BLACK, RANK_6));
    assertTrue(Rank.calculateHasNextRank(BLACK, RANK_7));
    assertTrue(Rank.calculateHasNextRank(BLACK, RANK_8));

    assertTrue(Rank.calculateHasPreviousRank(BLACK, RANK_1));
    assertTrue(Rank.calculateHasPreviousRank(BLACK, RANK_2));
    assertTrue(Rank.calculateHasPreviousRank(BLACK, RANK_3));
    assertTrue(Rank.calculateHasPreviousRank(BLACK, RANK_4));
    assertTrue(Rank.calculateHasPreviousRank(BLACK, RANK_5));
    assertTrue(Rank.calculateHasPreviousRank(BLACK, RANK_6));
    assertTrue(Rank.calculateHasPreviousRank(BLACK, RANK_7));
    assertFalse(Rank.calculateHasPreviousRank(BLACK, RANK_8));

    // white value
    assertEquals(RANK_2, Rank.calculateNextRank(WHITE, RANK_1));
    assertEquals(RANK_3, Rank.calculateNextRank(WHITE, RANK_2));
    assertEquals(RANK_4, Rank.calculateNextRank(WHITE, RANK_3));
    assertEquals(RANK_5, Rank.calculateNextRank(WHITE, RANK_4));
    assertEquals(RANK_6, Rank.calculateNextRank(WHITE, RANK_5));
    assertEquals(RANK_7, Rank.calculateNextRank(WHITE, RANK_6));
    assertEquals(RANK_8, Rank.calculateNextRank(WHITE, RANK_7));
    checkExceptionNext(WHITE, RANK_8);

    checkExceptionPrevious(WHITE, RANK_1);
    assertEquals(RANK_1, Rank.calculatePreviousRank(WHITE, RANK_2));
    assertEquals(RANK_2, Rank.calculatePreviousRank(WHITE, RANK_3));
    assertEquals(RANK_3, Rank.calculatePreviousRank(WHITE, RANK_4));
    assertEquals(RANK_4, Rank.calculatePreviousRank(WHITE, RANK_5));
    assertEquals(RANK_5, Rank.calculatePreviousRank(WHITE, RANK_6));
    assertEquals(RANK_6, Rank.calculatePreviousRank(WHITE, RANK_7));
    assertEquals(RANK_7, Rank.calculatePreviousRank(WHITE, RANK_8));

    // black value
    checkExceptionNext(BLACK, RANK_1);
    assertEquals(RANK_1, Rank.calculateNextRank(BLACK, RANK_2));
    assertEquals(RANK_2, Rank.calculateNextRank(BLACK, RANK_3));
    assertEquals(RANK_3, Rank.calculateNextRank(BLACK, RANK_4));
    assertEquals(RANK_4, Rank.calculateNextRank(BLACK, RANK_5));
    assertEquals(RANK_5, Rank.calculateNextRank(BLACK, RANK_6));
    assertEquals(RANK_6, Rank.calculateNextRank(BLACK, RANK_7));
    assertEquals(RANK_7, Rank.calculateNextRank(BLACK, RANK_8));

    assertEquals(RANK_2, Rank.calculatePreviousRank(BLACK, RANK_1));
    assertEquals(RANK_3, Rank.calculatePreviousRank(BLACK, RANK_2));
    assertEquals(RANK_4, Rank.calculatePreviousRank(BLACK, RANK_3));
    assertEquals(RANK_5, Rank.calculatePreviousRank(BLACK, RANK_4));
    assertEquals(RANK_6, Rank.calculatePreviousRank(BLACK, RANK_5));
    assertEquals(RANK_7, Rank.calculatePreviousRank(BLACK, RANK_6));
    assertEquals(RANK_8, Rank.calculatePreviousRank(BLACK, RANK_7));
    checkExceptionPrevious(BLACK, RANK_8);
  }

  @SuppressWarnings("static-method")
  @Test
  void testMethodsAdjacentAdjacent() throws Exception {

    // white existence
    assertTrue(Rank.calculateHasNextNextRank(WHITE, RANK_1));
    assertTrue(Rank.calculateHasNextNextRank(WHITE, RANK_2));
    assertTrue(Rank.calculateHasNextNextRank(WHITE, RANK_3));
    assertTrue(Rank.calculateHasNextNextRank(WHITE, RANK_4));
    assertTrue(Rank.calculateHasNextNextRank(WHITE, RANK_5));
    assertTrue(Rank.calculateHasNextNextRank(WHITE, RANK_6));
    assertFalse(Rank.calculateHasNextNextRank(WHITE, RANK_7));
    assertFalse(Rank.calculateHasNextNextRank(WHITE, RANK_8));

    assertFalse(Rank.calculateHasPreviousPreviousRank(WHITE, RANK_1));
    assertFalse(Rank.calculateHasPreviousPreviousRank(WHITE, RANK_2));
    assertTrue(Rank.calculateHasPreviousPreviousRank(WHITE, RANK_3));
    assertTrue(Rank.calculateHasPreviousPreviousRank(WHITE, RANK_4));
    assertTrue(Rank.calculateHasPreviousPreviousRank(WHITE, RANK_5));
    assertTrue(Rank.calculateHasPreviousPreviousRank(WHITE, RANK_6));
    assertTrue(Rank.calculateHasPreviousPreviousRank(WHITE, RANK_7));
    assertTrue(Rank.calculateHasPreviousPreviousRank(WHITE, RANK_8));

    // black existence
    assertFalse(Rank.calculateHasNextNextRank(BLACK, RANK_1));
    assertFalse(Rank.calculateHasNextNextRank(BLACK, RANK_2));
    assertTrue(Rank.calculateHasNextNextRank(BLACK, RANK_3));
    assertTrue(Rank.calculateHasNextNextRank(BLACK, RANK_4));
    assertTrue(Rank.calculateHasNextNextRank(BLACK, RANK_5));
    assertTrue(Rank.calculateHasNextNextRank(BLACK, RANK_6));
    assertTrue(Rank.calculateHasNextNextRank(BLACK, RANK_7));
    assertTrue(Rank.calculateHasNextNextRank(BLACK, RANK_8));

    assertTrue(Rank.calculateHasPreviousPreviousRank(BLACK, RANK_1));
    assertTrue(Rank.calculateHasPreviousPreviousRank(BLACK, RANK_2));
    assertTrue(Rank.calculateHasPreviousPreviousRank(BLACK, RANK_3));
    assertTrue(Rank.calculateHasPreviousPreviousRank(BLACK, RANK_4));
    assertTrue(Rank.calculateHasPreviousPreviousRank(BLACK, RANK_5));
    assertTrue(Rank.calculateHasPreviousPreviousRank(BLACK, RANK_6));
    assertFalse(Rank.calculateHasPreviousPreviousRank(BLACK, RANK_7));
    assertFalse(Rank.calculateHasPreviousPreviousRank(BLACK, RANK_8));

    // white value
    assertEquals(RANK_3, Rank.calculateNextNextRank(WHITE, RANK_1));
    assertEquals(RANK_4, Rank.calculateNextNextRank(WHITE, RANK_2));
    assertEquals(RANK_5, Rank.calculateNextNextRank(WHITE, RANK_3));
    assertEquals(RANK_6, Rank.calculateNextNextRank(WHITE, RANK_4));
    assertEquals(RANK_7, Rank.calculateNextNextRank(WHITE, RANK_5));
    assertEquals(RANK_8, Rank.calculateNextNextRank(WHITE, RANK_6));
    checkExceptionNextNext(WHITE, RANK_7);
    checkExceptionNextNext(WHITE, RANK_8);

    checkExceptionPreviousPrevious(WHITE, RANK_1);
    checkExceptionPreviousPrevious(WHITE, RANK_2);
    assertEquals(RANK_1, Rank.calculatePreviousPreviousRank(WHITE, RANK_3));
    assertEquals(RANK_2, Rank.calculatePreviousPreviousRank(WHITE, RANK_4));
    assertEquals(RANK_3, Rank.calculatePreviousPreviousRank(WHITE, RANK_5));
    assertEquals(RANK_4, Rank.calculatePreviousPreviousRank(WHITE, RANK_6));
    assertEquals(RANK_5, Rank.calculatePreviousPreviousRank(WHITE, RANK_7));
    assertEquals(RANK_6, Rank.calculatePreviousPreviousRank(WHITE, RANK_8));

    // black value
    checkExceptionNextNext(BLACK, RANK_1);
    checkExceptionNextNext(BLACK, RANK_2);
    assertEquals(RANK_1, Rank.calculateNextNextRank(BLACK, RANK_3));
    assertEquals(RANK_2, Rank.calculateNextNextRank(BLACK, RANK_4));
    assertEquals(RANK_3, Rank.calculateNextNextRank(BLACK, RANK_5));
    assertEquals(RANK_4, Rank.calculateNextNextRank(BLACK, RANK_6));
    assertEquals(RANK_5, Rank.calculateNextNextRank(BLACK, RANK_7));
    assertEquals(RANK_6, Rank.calculateNextNextRank(BLACK, RANK_8));

    assertEquals(RANK_3, Rank.calculatePreviousPreviousRank(BLACK, RANK_1));
    assertEquals(RANK_4, Rank.calculatePreviousPreviousRank(BLACK, RANK_2));
    assertEquals(RANK_5, Rank.calculatePreviousPreviousRank(BLACK, RANK_3));
    assertEquals(RANK_6, Rank.calculatePreviousPreviousRank(BLACK, RANK_4));
    assertEquals(RANK_7, Rank.calculatePreviousPreviousRank(BLACK, RANK_5));
    assertEquals(RANK_8, Rank.calculatePreviousPreviousRank(BLACK, RANK_6));
    checkExceptionPreviousPrevious(BLACK, RANK_7);
    checkExceptionPreviousPrevious(BLACK, RANK_8);
  }

  private static void checkExceptionNext(Side side, Rank rank) {
    boolean isException;
    try {
      Rank.calculateNextRank(side, rank);
      isException = false;
    } catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
      isException = true;
    }
    assertTrue(isException);
  }

  private static void checkExceptionNextNext(Side side, Rank rank) {
    boolean isException;
    try {
      Rank.calculateNextNextRank(side, rank);
      isException = false;
    } catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
      isException = true;
    }
    assertTrue(isException);
  }

  private static void checkExceptionPrevious(Side side, Rank rank) {
    boolean isException;
    try {
      Rank.calculatePreviousRank(side, rank);
      isException = false;
    } catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
      isException = true;
    }
    assertTrue(isException);
  }

  private static void checkExceptionPreviousPrevious(Side side, Rank rank) {
    boolean isException;
    try {
      Rank.calculatePreviousPreviousRank(side, rank);
      isException = false;
    } catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
      isException = true;
    }
    assertTrue(isException);
  }
}
