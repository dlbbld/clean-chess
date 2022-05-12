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
import com.dlb.chess.test.winnable.WinnableCalculator;
import com.dlb.chess.test.winnable.enums.Winnable;
import com.dlb.chess.unwinnability.quick.UnwinnableQuickCalculator;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuick;

public class TestUnwinnabilityQuickAgainstWinnability {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestUnwinnabilityQuickAgainstWinnability.class);

  private static final boolean IS_START_FROM_PGN_FILE = true;
  private static final String START_FROM_PGN_FILE_NAME = "Ob5ozxgG.pgn";

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
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
          final Winnable winnable = WinnableCalculator.calculateWinnable(board, board.getHavingMove().getOppositeSide());
          final UnwinnableQuick unwinnableQuick = UnwinnableQuickCalculator.unwinnableQuick(board,
              board.getHavingMove().getOppositeSide());

          checkResult(winnable, unwinnableQuick);
        }

        // having move
        {
          final Winnable winnable = WinnableCalculator.calculateWinnable(board, board.getHavingMove());
          final UnwinnableQuick unwinnableQuick = UnwinnableQuickCalculator.unwinnableQuick(board, board.getHavingMove());

          checkResult(winnable, unwinnableQuick);
        }
      }
    }
  }

  private static void checkResult(Winnable winnable, UnwinnableQuick unwinnableQuick) {
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
