package com.dlb.chess.test.unwinnability;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.Nulls;
import com.dlb.chess.test.RestrictTestConstants;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgn.setup.CreatePgnTestCases;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.test.unwinnability.oracle.LimitedUnwinnabilityOracle;
import com.dlb.chess.test.unwinnability.oracle.enums.LimitedUnwinnabilityVerdict;
import com.dlb.chess.unwinnability.UnwinnabilityQuickVerdict;
import com.dlb.chess.unwinnability.UnwinnableQuickAnalyzer;

class TestUnwinnabilityQuickAgainstLimitedOracle {

  private static final Logger logger = Nulls.getLogger(TestUnwinnabilityQuickAgainstLimitedOracle.class);

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {

    for (final PgnTest pgnTest : PgnTest.values()) {
      final PgnFileTestCaseList testCaseList = CreatePgnTestCases.getTestList(pgnTest);
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        if (RestrictTestConstants.IS_RESTRICT_PGN_UNWINNABILITY_QUICK_AGAINST_LIMITED_ORACLE_TEST) {
          switch (testCaseList.pgnTest()) {
            case CHA_LICHESS_QUICK_NOT_DEPTH_THREE:
            case CHA_LICHESS_QUICK_NOT_DEPTH_THREE_HELPMATE:
            case CHA_LICHESS_QUICK_DEPTH_THREE:
            case CHA_LICHESS_QUICK_DEPTH_FOUR:
            case CHA_LICHESS_NOT_QUICK:
            case CHA_AMBRONA:
              break;
            // $CASES-OMITTED$
            default:
              continue;
          }
        }

        switch (testCase.pgnFileName()) {
          // here my tool sees unwinnability but not the quick analysis
          case "ambrona_10.pgn":
          case "ambrona_16.pgn":
          case "pawn_wall_norgaard_additional_own_pawns_not_marched_up_with_opponent_pawns_between.pgn":
            continue;
          default:
            break;
        }

        final Board board = testCase.position();

        logger.info(testCase.pgnFileName());

        final LimitedUnwinnabilityVerdict verdictWhite = LimitedUnwinnabilityOracle.calculateUnwinnability(board,
            Side.WHITE);
        final UnwinnabilityQuickVerdict unwinnableQuickWhite = UnwinnableQuickAnalyzer.unwinnableQuick(board,
            Side.WHITE);
        check(verdictWhite, unwinnableQuickWhite);

        final LimitedUnwinnabilityVerdict verdictBlack = LimitedUnwinnabilityOracle.calculateUnwinnability(board,
            Side.BLACK);
        final UnwinnabilityQuickVerdict unwinnableQuickBlack = UnwinnableQuickAnalyzer.unwinnableQuick(board,
            Side.BLACK);

        check(verdictBlack, unwinnableQuickBlack);
      }
    }
  }

  private static void check(LimitedUnwinnabilityVerdict verdict, UnwinnabilityQuickVerdict unwinnableQuick) {
    switch (verdict) {
      case UNWINNABLE:
        assertEquals(UnwinnabilityQuickVerdict.UNWINNABLE, unwinnableQuick);
        break;
      case WINNABLE:
        assertNotEquals(UnwinnabilityQuickVerdict.UNWINNABLE, unwinnableQuick);
        break;
      case UNKNOWN:
        break;
      default:
        throw new IllegalArgumentException();
    }

    switch (unwinnableQuick) {
      case WINNABLE:
        assertNotEquals(LimitedUnwinnabilityVerdict.UNWINNABLE, verdict);
        break;
      case UNWINNABLE:
        assertNotEquals(LimitedUnwinnabilityVerdict.WINNABLE, verdict);
        break;
      case POSSIBLY_WINNABLE:
        break;
      default:
        throw new IllegalArgumentException();
    }
  }

}
