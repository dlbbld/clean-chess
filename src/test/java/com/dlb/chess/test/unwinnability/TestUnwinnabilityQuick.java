package com.dlb.chess.test.unwinnability;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.logging.log4j.Logger;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.unwinnability.quick.UnwinnableQuick;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuickResult;
import com.dlb.chess.winnable.WinnableUtility;
import com.dlb.chess.winnable.enums.Winnable;

public class TestUnwinnabilityQuick {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestUnwinnabilityQuick.class);

  @SuppressWarnings("static-method")
  // @Test
  void testSingle() {
    // final Board board = new Board("5r1k/6P1/7K/5q2/8/8/8/8 b - - 0 51");
    final Board board = new Board("rnbq1bnr/pppp2pp/PN6/R4k2/4pp2/5N2/1PPPPPPP/2BQKB1R b K - 5 8");
    assertEquals(UnwinnableQuickResult.WINNABLE,
        UnwinnableQuick.unwinnableQuick(board, board.getHavingMove().getOppositeSide()));
  }

  @SuppressWarnings("static-method")
  // @Test
  void testFolder() throws Exception {
    final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(PgnTest.LICHESS_NOT_QUICK);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      final ApiBoard board = new Board(testCase.fen());
      logger.info(testCase.pgnFileName());

      assertEquals(UnwinnableQuickResult.POSSIBLY_WINNABLE,
          UnwinnableQuick.unwinnableQuick(board, board.getHavingMove().getOppositeSide()));
    }
  }

  private static final boolean IS_START_FROM_PGN_FILE = false;
  private static final String START_FROM_PGN_FILE_NAME = "norgaard_pawn_wall_example_2.pgn";

  @SuppressWarnings("static-method")
  // @Test
  void testAgainstMine() throws Exception {
    var hasFound = false;
    for (final PgnTest pgnTest : PgnTest.values()) {
      final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(pgnTest);
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        if (!hasFound) {
          if (IS_START_FROM_PGN_FILE) {
            if (START_FROM_PGN_FILE_NAME.equals(testCase.pgnFileName())) {
              hasFound = true;
            }
          } else {
            hasFound = true;
          }
        }
        if (!hasFound) {
          continue;
        }

        switch (testCase.pgnFileName()) {
          case "norgaard_pawn_wall_example_2.pgn":
            continue;
          default:
            break;
        }

        final ApiBoard board = new Board(testCase.fen());

        logger.info(testCase.pgnFileName());

        // not having move
        {
          final Winnable winnable = WinnableUtility.calculateWinnable(board, board.getHavingMove().getOppositeSide());
          final UnwinnableQuickResult unwinnableQuick = UnwinnableQuick.unwinnableQuick(board,
              board.getHavingMove().getOppositeSide());

          switch (winnable) {
            case NO:
              assertEquals(UnwinnableQuickResult.UNWINNABLE, unwinnableQuick);
              break;
            case UNKNOWN:
            case YES:
              break;
            default:
              throw new IllegalArgumentException();
          }
        }

        // having move
        {
          final Winnable winnable = WinnableUtility.calculateWinnable(board, board.getHavingMove());
          final UnwinnableQuickResult unwinnableQuick = UnwinnableQuick.unwinnableQuick(board, board.getHavingMove());

          switch (winnable) {
            case NO:
              assertEquals(UnwinnableQuickResult.UNWINNABLE, unwinnableQuick);
              break;
            case UNKNOWN:
            case YES:
              break;
            default:
              throw new IllegalArgumentException();
          }
        }
      }
    }
  }
}
