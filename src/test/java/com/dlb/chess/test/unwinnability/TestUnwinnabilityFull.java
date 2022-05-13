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
import com.dlb.chess.test.pgntest.enums.UnwinnableFullResultTest;
import com.dlb.chess.test.winnable.WinnableCalculator;
import com.dlb.chess.test.winnable.enums.Winnable;
import com.dlb.chess.unwinnability.full.UnwinnableFullCalculator;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;

public class TestUnwinnabilityFull {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestUnwinnabilityFull.class);

  @SuppressWarnings("static-method")
  // @Test
  void testStartPosition() {
    final Board board = new Board();
    assertEquals(UnwinnableFull.NO, UnwinnableFullCalculator.unwinnableFull(board, Side.WHITE));
  }

  @SuppressWarnings("static-method")
  // @Test
  void testFen() {
    final var fen = "8/6k1/7p/2p3pP/3r4/2K5/p7/8 w - - 0 55";
    final Board board = new Board(fen);
    assertEquals(UnwinnableFull.NO, UnwinnableFullCalculator.unwinnableFull(board, Side.WHITE));
  }

  // not terminating so far
  // ae_04.pgn
  // ae_15_QRvIMh3z.pgn
  // ae_16.pgn

  // wrong
  // ae_06.pgn

  @SuppressWarnings("static-method")
  @Test
  void testPgnFile() {
    final PgnFileTestCase pgnFileTestCase = PgnExpectedValue.findPgnFileBelongingPgnTestCase("a2l4gphm.pgn");
    final ApiBoard board = new Board(pgnFileTestCase.fen());
    logger.info(pgnFileTestCase.pgnFileName());

    check(pgnFileTestCase.unwinnableFullResultTest(), board);
  }

  @SuppressWarnings("static-method")
  // @Test
  void testFolder() throws Exception {
    final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(PgnTest.UNFAIR_AMBRONA_EXAMPLES);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      final ApiBoard board = new Board(testCase.fen());

      logger.info(testCase.pgnFileName());

      check(testCase.unwinnableFullResultTest(), board);
    }
  }

  private static final boolean IS_START_FROM_PGN_FILE = true;
  private static final String START_FROM_PGN_FILE_NAME = "02_white_rook_knight.pgn";

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
          case "capture_first_move_half_move_clock_100_black_to_move.pgn":
            continue;
          default:
            break;
        }
        final ApiBoard board = new Board(testCase.fen());
        logger.info(testCase.pgnFileName());

        // not having move
        {
          final Winnable winnable = WinnableCalculator.calculateWinnable(board, board.getHavingMove().getOppositeSide());
          final UnwinnableFull unwinnableFull = UnwinnableFullCalculator.unwinnableFull(board,
              board.getHavingMove().getOppositeSide());

          switch (winnable) {
            case NO:
              assertEquals(UnwinnableFull.YES, unwinnableFull);
              break;
            case UNKNOWN:
              break;
            case YES:
              assertEquals(UnwinnableFull.NO, unwinnableFull);
              break;
            default:
              throw new IllegalArgumentException();
          }
        }

        // having move
        {
          final Winnable winnable = WinnableCalculator.calculateWinnable(board, board.getHavingMove());
          final UnwinnableFull unwinnableFull = UnwinnableFullCalculator.unwinnableFull(board, board.getHavingMove());

          switch (winnable) {
            case NO:
              assertEquals(UnwinnableFull.YES, unwinnableFull);
              break;
            case UNKNOWN:
              break;
            case YES:
              assertEquals(UnwinnableFull.NO, unwinnableFull);
              break;
            default:
              throw new IllegalArgumentException();
          }
        }
      }
    }
  }

  private static void check(UnwinnableFullResultTest unwinnableFullResultTest, ApiBoard board) {
    switch (unwinnableFullResultTest) {
      case UNWINNABLE:
      case UNWINNABLE_NOT_QUICK:
        assertEquals(UnwinnableFull.YES,
            UnwinnableFullCalculator.unwinnableFull(board, board.getHavingMove().getOppositeSide()));
        break;
      case WINNABLE:
        assertEquals(UnwinnableFull.NO,
            UnwinnableFullCalculator.unwinnableFull(board, board.getHavingMove().getOppositeSide()));
        break;
      default:
        throw new IllegalArgumentException();
    }
  }
}
