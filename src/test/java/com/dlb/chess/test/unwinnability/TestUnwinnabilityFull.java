package com.dlb.chess.test.unwinnability;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.test.winnable.WinnableUtility;
import com.dlb.chess.test.winnable.enums.Winnable;
import com.dlb.chess.unwinnability.full.UnwinnableFull;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFullResult;

public class TestUnwinnabilityFull {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestUnwinnabilityFull.class);

  @SuppressWarnings("static-method")
  // @Test
  void testSingle() {
    // final Board board = new Board("8/6k1/7p/2p3pP/3r4/2K5/p7/8 w - - 0 55");
    final Board board = new Board();

    assertEquals(UnwinnableFullResult.WINNABLE, UnwinnableFull.unwinnableFull(board, Side.WHITE));
  }

  @SuppressWarnings("static-method")
  // @Test
  void testFolder() throws Exception {
    final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(PgnTest.LICHESS_NOT_QUICK);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      final ApiBoard board = new Board(testCase.fen());

      logger.info(testCase.pgnFileName());

      assertEquals(UnwinnableFullResult.UNWINNABLE,
          UnwinnableFull.unwinnableFull(board, board.getHavingMove().getOppositeSide()));
    }
  }

  private static final boolean IS_START_FROM_PGN_FILE = true;
  private static final String START_FROM_PGN_FILE_NAME = "02_white_rook_knight.pgn";

  @SuppressWarnings("static-method")
  @Test
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
          case "capture_first_move_half_move_clock_100_black_to_move.pgn":
            continue;
          default:
            break;
        }
        final ApiBoard board = new Board(testCase.fen());
        logger.info(testCase.pgnFileName());

        // not having move
        {
          final Winnable winnable = WinnableUtility.calculateWinnable(board, board.getHavingMove().getOppositeSide());
          final UnwinnableFullResult unwinnableFull = UnwinnableFull.unwinnableFull(board,
              board.getHavingMove().getOppositeSide());

          switch (winnable) {
            case NO:
              assertEquals(UnwinnableFullResult.UNWINNABLE, unwinnableFull);
              break;
            case UNKNOWN:
              break;
            case YES:
              assertEquals(UnwinnableFullResult.WINNABLE, unwinnableFull);
              break;
            default:
              throw new IllegalArgumentException();
          }
        }

        // having move
        {
          final Winnable winnable = WinnableUtility.calculateWinnable(board, board.getHavingMove());
          final UnwinnableFullResult unwinnableFull = UnwinnableFull.unwinnableFull(board, board.getHavingMove());

          switch (winnable) {
            case NO:
              assertEquals(UnwinnableFullResult.UNWINNABLE, unwinnableFull);
              break;
            case UNKNOWN:
              break;
            case YES:
              assertEquals(UnwinnableFullResult.WINNABLE, unwinnableFull);
              break;
            default:
              throw new IllegalArgumentException();
          }
        }
      }
    }
  }
}
