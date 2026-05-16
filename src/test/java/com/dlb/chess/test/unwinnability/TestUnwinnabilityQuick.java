package com.dlb.chess.test.unwinnability;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.Nulls;
import com.dlb.chess.test.RestrictTestConstants;
import com.dlb.chess.test.model.PgnTestCase;
import com.dlb.chess.test.model.PgnTestCaseList;
import com.dlb.chess.test.pgn.setup.PgnTestCaseCatalog;
import com.dlb.chess.test.unwinnability.againstcha.AmbronaUnwinnabilityOracle;
import com.dlb.chess.unwinnability.UnwinnabilityQuickVerdict;
import com.dlb.chess.unwinnability.UnwinnableQuickAnalyzer;

class TestUnwinnabilityQuick {

  private static final Logger logger = Nulls.getLogger(TestUnwinnabilityQuick.class);

  @SuppressWarnings("static-method")
  @Test
  void test() {
    for (final PgnTestCaseList testCaseList : PgnTestCaseCatalog.getRestrictedTestListList()) {
      if (RestrictTestConstants.IS_RESTRICT_UNWINNABILITY_QUICK_AGAINST_AMBRONA_ORACLE_TEST) {
        switch (testCaseList.pgnTest()) {
          case CHA_LICHESS_QUICK_NOT_DEPTH_THREE:
          case CHA_LICHESS_QUICK_DEPTH_THREE:
          case CHA_LICHESS_QUICK_DEPTH_FOUR:
          case CHA_LICHESS_NOT_QUICK:
          case CHA_AMBRONA:
          case CHA_PAWN_WALL_YES:
          case CHA_PAWN_WALL_NO:
          case CHA_SHALLOW_TERMINATION:
          case CHA_HELPMATE_BEYOND_FIVEFOLD:
          case CHA_HELPMATE_BEYOND_SEVENTY_FIVE:
            break;
          // $CASES-OMITTED$
          default:
            continue;
        }
      }
      for (final PgnTestCase testCase : testCaseList.list()) {
        final Board board = testCase.finalPosition();
        logger.info(testCase.pgnName());

        final UnwinnabilityQuickVerdict unwinnableQuickWhite = UnwinnableQuickAnalyzer.unwinnableQuick(board,
            Side.WHITE);
        assertEquals(AmbronaUnwinnabilityOracle.get(testCase.finalFen()).quickWhite(), unwinnableQuickWhite);

        final UnwinnabilityQuickVerdict unwinnableQuickBlack = UnwinnableQuickAnalyzer.unwinnableQuick(board,
            Side.BLACK);
        assertEquals(AmbronaUnwinnabilityOracle.get(testCase.finalFen()).quickBlack(), unwinnableQuickBlack);

      }
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testStartPosition() {
    final Board board = new Board(false);
    assertEquals(UnwinnabilityQuickVerdict.POSSIBLY_WINNABLE,
        UnwinnableQuickAnalyzer.unwinnableQuick(board, board.getHavingMove().getOppositeSide()));
  }

}
