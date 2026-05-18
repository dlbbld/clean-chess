package com.dlb.chess.test.unwinnability;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.Nulls;
import com.dlb.chess.test.model.PgnTestCase;
import com.dlb.chess.test.pgn.setup.PgnTestCaseCatalog;
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
    assertVerdicts("01_beyond_fivefold.pgn", UnwinnabilityQuickVerdict.POSSIBLY_WINNABLE,
        UnwinnabilityQuickVerdict.POSSIBLY_WINNABLE, UnwinnabilityFullVerdict.WINNABLE,
        UnwinnabilityFullVerdict.WINNABLE);
  }

  @SuppressWarnings("static-method")
  @Test
  void testFivefoldSecondGame() {
    // K+R vs lone K — White can helpmate; Black has insufficient material.
    assertVerdicts("02_beyond_fivefold.pgn", UnwinnabilityQuickVerdict.POSSIBLY_WINNABLE,
        UnwinnabilityQuickVerdict.UNWINNABLE, UnwinnabilityFullVerdict.WINNABLE, UnwinnabilityFullVerdict.UNWINNABLE);
  }

  @SuppressWarnings("static-method")
  @Test
  void testSeventyFiveFirstGame() {
    assertVerdicts("01_beyond_seventy_five.pgn", UnwinnabilityQuickVerdict.POSSIBLY_WINNABLE,
        UnwinnabilityQuickVerdict.POSSIBLY_WINNABLE, UnwinnabilityFullVerdict.WINNABLE,
        UnwinnabilityFullVerdict.WINNABLE);
  }

  @SuppressWarnings("static-method")
  @Test
  void testSeventyFiveSecondGame() {
    assertVerdicts("02_beyond_seventy_five.pgn", UnwinnabilityQuickVerdict.POSSIBLY_WINNABLE,
        UnwinnabilityQuickVerdict.UNWINNABLE, UnwinnabilityFullVerdict.WINNABLE, UnwinnabilityFullVerdict.UNWINNABLE);
  }

  private static void assertVerdicts(String pgnName, UnwinnabilityQuickVerdict quickWhite,
      UnwinnabilityQuickVerdict quickBlack, UnwinnabilityFullVerdict fullWhite, UnwinnabilityFullVerdict fullBlack) {
    logger.info(pgnName);
    final PgnTestCase testCase = PgnTestCaseCatalog.findTestCase(pgnName);
    final Board board = testCase.finalPosition();

    assertEquals(quickWhite, UnwinnableQuickAnalyzer.unwinnableQuick(board, Side.WHITE), pgnName);
    assertEquals(quickBlack, UnwinnableQuickAnalyzer.unwinnableQuick(board, Side.BLACK), pgnName);
    assertEquals(fullWhite, UnwinnableFullAnalyzer.unwinnableFull(board, Side.WHITE).verdict(), pgnName);
    assertEquals(fullBlack, UnwinnableFullAnalyzer.unwinnableFull(board, Side.BLACK).verdict(), pgnName);
  }
}
