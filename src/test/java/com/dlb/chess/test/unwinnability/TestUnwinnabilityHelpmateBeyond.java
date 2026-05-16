package com.dlb.chess.test.unwinnability;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.Nulls;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.pgn.setup.CreatePgnTestCases;
import com.dlb.chess.unwinnability.UnwinnabilityFullVerdict;
import com.dlb.chess.unwinnability.UnwinnabilityQuickVerdict;
import com.dlb.chess.unwinnability.UnwinnableFullAnalyzer;
import com.dlb.chess.unwinnability.UnwinnableQuickAnalyzer;

class TestUnwinnabilityHelpmateBeyond {

  private static final Logger logger = Nulls.getLogger(TestUnwinnabilityHelpmateBeyond.class);

  @SuppressWarnings("static-method")
  @Test
  void testFivefoldFirstGame() {
    // Quick/Full work on a fresh, history-less copy, so the helpmate hiding behind the
    // fivefold boundary is not detected — both sides come back WINNABLE.
    assertVerdicts("01_beyond_fivefold.pgn", UnwinnabilityQuickVerdict.WINNABLE, UnwinnabilityQuickVerdict.WINNABLE,
        UnwinnabilityFullVerdict.WINNABLE, UnwinnabilityFullVerdict.WINNABLE);
  }

  @SuppressWarnings("static-method")
  @Test
  void testFivefoldSecondGame() {
    // K+R vs lone K — White can helpmate; Black has insufficient material.
    assertVerdicts("02_beyond_fivefold.pgn", UnwinnabilityQuickVerdict.WINNABLE, UnwinnabilityQuickVerdict.UNWINNABLE,
        UnwinnabilityFullVerdict.WINNABLE, UnwinnabilityFullVerdict.UNWINNABLE);
  }

  @SuppressWarnings("static-method")
  @Test
  void testSeventyFiveFirstGame() {
    // 75-move boundary is visible from the halfmove clock alone, so the helpmate sitting
    // beyond it is correctly ruled out for both sides.
    assertVerdicts("01_beyond_seventy_five.pgn", UnwinnabilityQuickVerdict.UNWINNABLE,
        UnwinnabilityQuickVerdict.UNWINNABLE, UnwinnabilityFullVerdict.UNWINNABLE,
        UnwinnabilityFullVerdict.UNWINNABLE);
  }

  @SuppressWarnings("static-method")
  @Test
  void testSeventyFiveSecondGame() {
    assertVerdicts("02_beyond_seventy_five.pgn", UnwinnabilityQuickVerdict.UNWINNABLE,
        UnwinnabilityQuickVerdict.UNWINNABLE, UnwinnabilityFullVerdict.UNWINNABLE,
        UnwinnabilityFullVerdict.UNWINNABLE);
  }

  private static void assertVerdicts(String pgnFileName, UnwinnabilityQuickVerdict quickWhite,
      UnwinnabilityQuickVerdict quickBlack, UnwinnabilityFullVerdict fullWhite,
      UnwinnabilityFullVerdict fullBlack) {
    logger.info(pgnFileName);
    final PgnFileTestCase testCase = CreatePgnTestCases.findTestCase(pgnFileName);
    final Board board = testCase.position();

    assertEquals(quickWhite, UnwinnableQuickAnalyzer.unwinnableQuick(board, Side.WHITE), pgnFileName);
    assertEquals(quickBlack, UnwinnableQuickAnalyzer.unwinnableQuick(board, Side.BLACK), pgnFileName);
    assertEquals(fullWhite, UnwinnableFullAnalyzer.unwinnableFull(board, Side.WHITE).verdict(), pgnFileName);
    assertEquals(fullBlack, UnwinnableFullAnalyzer.unwinnableFull(board, Side.BLACK).verdict(), pgnFileName);
  }
}
