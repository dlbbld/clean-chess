package com.dlb.chess.test.unwinnability;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.enums.InsufficientMaterial;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgn.setup.CreatePgnTestCases;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.unwinnability.UnwinnabilityFullVerdict;
import com.dlb.chess.unwinnability.UnwinnabilityQuickVerdict;
import com.dlb.chess.unwinnability.UnwinnableFullAnalyzer;
import com.dlb.chess.unwinnability.UnwinnableQuickAnalyzer;

class TestUnwinnabilityAgainstInsufficientMaterial {

  private static final PgnTest[] PGN_TEST_ARRAY = {
      PgnTest.BASIC_INSUFFICIENT_MATERIAL_BOTH,
      PgnTest.BASIC_INSUFFICIENT_MATERIAL_ONLY_WHITE,
      PgnTest.BASIC_INSUFFICIENT_MATERIAL_ONLY_BLACK,
      PgnTest.RANDOM_INSUFFICIENT_MATERIAL
  };

  @SuppressWarnings("static-method")
  @Test
  void testInsufficientMaterialImpliesUnwinnability() {
    var testedSideCount = 0;
    for (final PgnTest pgnTest : PGN_TEST_ARRAY) {
      final PgnFileTestCaseList testCaseList = CreatePgnTestCases.getTestList(pgnTest);
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        final Board board = new Board(testCase.fen());
        testedSideCount += test(board, testCase, Side.WHITE);
        testedSideCount += test(board, testCase, Side.BLACK);
      }
    }
    assertTrue(testedSideCount > 0);
  }

  private static int test(Board board, PgnFileTestCase testCase, Side side) {
    if (!isInsufficientMaterial(testCase.insufficientMaterial(), side)) {
      return 0;
    }

    final var message = testCase.pgnFileName() + " " + side;
    assertEquals(UnwinnabilityQuickVerdict.UNWINNABLE, UnwinnableQuickAnalyzer.unwinnableQuick(board, side), message);
    assertEquals(UnwinnabilityFullVerdict.UNWINNABLE, UnwinnableFullAnalyzer.unwinnableFull(board, side).verdict(),
        message);
    return 1;
  }

  private static boolean isInsufficientMaterial(InsufficientMaterial insufficientMaterial, Side side) {
    return insufficientMaterial == InsufficientMaterial.BOTH
        || insufficientMaterial == InsufficientMaterial.WHITE_ONLY && side == Side.WHITE
        || insufficientMaterial == InsufficientMaterial.BLACK_ONLY && side == Side.BLACK;
  }
}
