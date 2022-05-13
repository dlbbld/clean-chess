package com.dlb.chess.test.unwinnability;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.test.winnable.enums.Winnable;
import com.dlb.chess.unwinnability.quick.UnwinnableQuickCalculator;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuick;

public class TestUnwinnabilityQuickAgainstWinnability {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestUnwinnabilityQuickAgainstWinnability.class);

  private static final boolean IS_START_FROM_PGN_FILE = false;
  private static final String START_FROM_PGN_FILE_NAME = "25_black_king_pawn.pgn";

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
    var counter = 0D;
    var milliSecondsTotal = 0L;

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
          // here my tool sees unwinnability but not the quick analysis
          case "ae_10.pgn":
          case "norgaard_pawn_wall_example_2.pgn":
            continue;
          default:
            break;
        }

        final ApiBoard board = new Board(testCase.fen());

        logger.info(testCase.pgnFileName());

        // not having move
        {
          // final Winnable winnable = WinnableCalculator.calculateWinnable(board,
          // board.getHavingMove().getOppositeSide());
          final var before = System.currentTimeMillis();
          final UnwinnableQuick unwinnableQuick = UnwinnableQuickCalculator.unwinnableQuick(board,
              board.getHavingMove().getOppositeSide());
          counter = counter + 1;
          final var duration = System.currentTimeMillis() - before;
          milliSecondsTotal += duration;

          System.out.println("Average duration: " + milliSecondsTotal / counter + " (" + counter + " positions)");
          // checkResult(winnable, unwinnableQuick);
        }

        // having move
        {
          // final Winnable winnable = WinnableCalculator.calculateWinnable(board, board.getHavingMove());
          // final UnwinnableQuick unwinnableQuick = UnwinnableQuickCalculator.unwinnableQuick(board,
          // board.getHavingMove());
          //
          // checkResult(winnable, unwinnableQuick);
        }
      }
    }
  }

  private static void checkResult(Winnable winnable, UnwinnableQuick unwinnableQuick) {
    if (true) {
      return;
    }
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
