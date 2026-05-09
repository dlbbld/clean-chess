package com.dlb.chess.test.unwinnability;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.interfaces.ChessBoard;
import com.dlb.chess.test.RestrictTestConstants;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.test.winnable.WinnableAnalyzer;
import com.dlb.chess.test.winnable.enums.Winnable;
import com.dlb.chess.unwinnability.quick.UnwinnableQuickAnalyzer;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuick;

class TestUnwinnabilityQuickAgainstWinnability {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestUnwinnabilityQuickAgainstWinnability.class);

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {

    for (final PgnTest pgnTest : PgnTest.values()) {
      final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(pgnTest);
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        if (RestrictTestConstants.IS_RESTRICT_PGN_UNWINNABILITY_QUICK_AGAINST_WINNABILITY_TEST) {
          switch (testCaseList.pgnTest()) {
            case CHA_LICHESS_QUICK_NOT_DEPTH_THREE_EXAMPLES:
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
          case "unfair_ambrona_10.pgn":
          case "unfair_ambrona_16.pgn":
          case "pawn_wall_norgaard_example_2.pgn":
            continue;
          default:
            break;
        }

        final ChessBoard board = new Board(testCase.fen());

        logger.info(testCase.pgnFileName());

        final Winnable winnableWhite = WinnableAnalyzer.calculateWinnable(board, Side.WHITE);
        final UnwinnableQuick unwinnableQuickWhite = UnwinnableQuickAnalyzer.unwinnableQuick(board, Side.WHITE);
        CheckQuick.check(winnableWhite, unwinnableQuickWhite);

        final Winnable winnableBlack = WinnableAnalyzer.calculateWinnable(board, Side.BLACK);
        final UnwinnableQuick unwinnableQuickBlack = UnwinnableQuickAnalyzer.unwinnableQuick(board, Side.BLACK);

        CheckQuick.check(winnableBlack, unwinnableQuickBlack);
      }
    }
  }

}
