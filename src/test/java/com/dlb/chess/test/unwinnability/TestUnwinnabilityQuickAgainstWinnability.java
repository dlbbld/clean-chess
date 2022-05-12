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
import com.dlb.chess.test.winnable.WinnableUtility;
import com.dlb.chess.test.winnable.enums.Winnable;
import com.dlb.chess.unwinnability.quick.UnwinnableQuick;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuickResult;

public class TestUnwinnabilityQuickAgainstWinnability {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestUnwinnabilityQuickAgainstWinnability.class);

  private static final boolean IS_START_FROM_PGN_FILE = false;
  private static final String START_FROM_PGN_FILE_NAME = "seventy_five_01_2_end_with_seventy_five.pgn";

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
          final Winnable winnable = WinnableUtility.calculateWinnable(board, board.getHavingMove().getOppositeSide());
          final UnwinnableQuickResult unwinnableQuick = UnwinnableQuick.unwinnableQuick(board,
              board.getHavingMove().getOppositeSide());

          checkResult(winnable, unwinnableQuick);
        }

        // having move
        {
          final Winnable winnable = WinnableUtility.calculateWinnable(board, board.getHavingMove());
          final UnwinnableQuickResult unwinnableQuick = UnwinnableQuick.unwinnableQuick(board, board.getHavingMove());

          checkResult(winnable, unwinnableQuick);
        }
      }
    }
  }

  private static void checkResult(Winnable winnable, UnwinnableQuickResult unwinnableQuick) {
    switch (winnable) {
      case NO:
        assertEquals(UnwinnableQuickResult.UNWINNABLE, unwinnableQuick);
        break;
      case YES:
        assertNotEquals(UnwinnableQuickResult.UNWINNABLE, unwinnableQuick);
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
