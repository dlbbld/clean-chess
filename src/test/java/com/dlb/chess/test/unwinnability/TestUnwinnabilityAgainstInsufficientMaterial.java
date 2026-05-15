package com.dlb.chess.test.unwinnability;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.pgn.setup.CreatePgnTestCases;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.unwinnability.UnwinnabilityFullVerdict;
import com.dlb.chess.unwinnability.UnwinnabilityQuickVerdict;
import com.dlb.chess.unwinnability.UnwinnableFullAnalyzer;
import com.dlb.chess.unwinnability.UnwinnableQuickAnalyzer;

/**
 * For every position where a side has insufficient material to mate, both the quick and the full unwinnability
 * analyzer must return {@code UNWINNABLE} for that side. This is the strongest external check of the analyzers'
 * lower bound: insufficient-material positions are a FIDE 5.2.2 dead-position subset, and any other verdict from
 * either analyzer would be a correctness bug.
 *
 * <p>
 * One test per insufficient-material category. The category determines which side(s) are asserted unwinnable.
 *
 * <p>
 * The {@code RANDOM_INSUFFICIENT_MATERIAL} corpus is deliberately not exercised here. The unwinnability question
 * for the final insufficient-material position is the same regardless of the move history that led to it; long
 * randomly-generated games that end in K-vs-K-style insufficient material add no coverage beyond what the {@code
 * BASIC_INSUFFICIENT_MATERIAL_*} fixtures provide.
 */
class TestUnwinnabilityAgainstInsufficientMaterial {

  @SuppressWarnings("static-method")
  @Test
  void testBothSidesInsufficient() {
    for (final PgnFileTestCase testCase : CreatePgnTestCases.getTestList(PgnTest.BASIC_INSUFFICIENT_MATERIAL_BOTH)
        .list()) {
      assertUnwinnable(testCase, Side.WHITE);
      assertUnwinnable(testCase, Side.BLACK);
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testOnlyWhiteInsufficient() {
    for (final PgnFileTestCase testCase : CreatePgnTestCases.getTestList(PgnTest.BASIC_INSUFFICIENT_MATERIAL_ONLY_WHITE)
        .list()) {
      assertUnwinnable(testCase, Side.WHITE);
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testOnlyBlackInsufficient() {
    for (final PgnFileTestCase testCase : CreatePgnTestCases.getTestList(PgnTest.BASIC_INSUFFICIENT_MATERIAL_ONLY_BLACK)
        .list()) {
      assertUnwinnable(testCase, Side.BLACK);
    }
  }

  private static void assertUnwinnable(PgnFileTestCase testCase, Side side) {
    final Board board = new Board(testCase.fen());
    final String message = testCase.pgnFileName() + " " + side;
    assertEquals(UnwinnabilityQuickVerdict.UNWINNABLE, UnwinnableQuickAnalyzer.unwinnableQuick(board, side), message);
    assertEquals(UnwinnabilityFullVerdict.UNWINNABLE, UnwinnableFullAnalyzer.unwinnableFull(board, side).verdict(),
        message);
  }
}
