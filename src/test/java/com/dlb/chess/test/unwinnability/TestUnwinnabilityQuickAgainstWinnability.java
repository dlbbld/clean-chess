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
import com.dlb.chess.test.winnable.WinnableAnalyzer;
import com.dlb.chess.test.winnable.enums.Winnable;
import com.dlb.chess.unwinnability.UnwinnableQuick;
import com.dlb.chess.unwinnability.UnwinnableQuickAnalyzer;

class TestUnwinnabilityQuickAgainstWinnability {

  private static final Logger logger = Nulls.getLogger(TestUnwinnabilityQuickAgainstWinnability.class);

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {

    for (final PgnTest pgnTest : PgnTest.values()) {
      final PgnFileTestCaseList testCaseList = CreatePgnTestCases.getTestList(pgnTest);
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        if (RestrictTestConstants.IS_RESTRICT_PGN_UNWINNABILITY_QUICK_AGAINST_WINNABILITY_TEST) {
          switch (testCaseList.pgnTest()) {
            case CHA_LICHESS_QUICK_NOT_DEPTH_THREE:
            case CHA_LICHESS_QUICK_NOT_DEPTH_THREE_HELPMATE:
            case CHA_LICHESS_QUICK_DEPTH_THREE:
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
          case "pawn_wall_norgaard_example_2.pgn":
            continue;
          default:
            break;
        }

        final Board board = new Board(testCase.fen());

        logger.info(testCase.pgnFileName());

        final Winnable winnableWhite = WinnableAnalyzer.calculateWinnable(board, Side.WHITE);
        final UnwinnableQuick unwinnableQuickWhite = UnwinnableQuickAnalyzer.unwinnableQuick(board, Side.WHITE);
        check(winnableWhite, unwinnableQuickWhite);

        final Winnable winnableBlack = WinnableAnalyzer.calculateWinnable(board, Side.BLACK);
        final UnwinnableQuick unwinnableQuickBlack = UnwinnableQuickAnalyzer.unwinnableQuick(board, Side.BLACK);

        check(winnableBlack, unwinnableQuickBlack);
      }
    }
  }

  private static void check(Winnable winnable, UnwinnableQuick unwinnableQuick) {
    switch (winnable) {
      case NO:
        assertEquals(UnwinnableQuick.UNWINNABLE, unwinnableQuick);
        break;
      case YES:
        assertNotEquals(UnwinnableQuick.UNWINNABLE, unwinnableQuick);
        break;
      case UNKNOWN:
        break;
      default:
        throw new IllegalArgumentException();
    }

    switch (unwinnableQuick) {
      case WINNABLE:
        assertNotEquals(Winnable.NO, winnable);
        break;
      case UNWINNABLE:
        assertNotEquals(Winnable.YES, winnable);
        break;
      case POSSIBLY_WINNABLE:
        break;
      default:
        throw new IllegalArgumentException();
    }
  }

}
