package com.dlb.chess.test.winnable;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.interfaces.ChessBoard;
import com.dlb.chess.common.utility.GeneralUtility;
import com.dlb.chess.pgn.LenientPgnParser;
import com.dlb.chess.pgn.PgnFile;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.test.winnable.enums.Winnable;
import com.dlb.chess.unwinnability.UnwinnableQuick;

class TestWinnability {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestWinnability.class);

  @SuppressWarnings("static-method")
  @Test
  void testStartPosition() {
    final Board board = new Board();

    assertEquals(Winnable.UNKNOWN, WinnableAnalyzer.calculateWinnable(board, Side.WHITE));
    assertEquals(Winnable.UNKNOWN, WinnableAnalyzer.calculateWinnable(board, Side.BLACK));
  }

  @SuppressWarnings("static-method")
  @Test
  void testFen() {
    final var fen = "rnbq1bnr/pppp2pp/PN6/R4k2/4pp2/5N2/1PPPPPPP/2BQKB1R b K - 5 8";
    final Board board = new Board(fen);

    assertEquals(Winnable.YES, WinnableAnalyzer.calculateWinnable(board, Side.WHITE));
    assertEquals(Winnable.UNKNOWN, WinnableAnalyzer.calculateWinnable(board, Side.BLACK));
  }

  @SuppressWarnings("static-method")
  @Test
  void testPgnFileValue() {
    final var pgnFileName = "pawn_wall_norgaard_example_2.pgn";

    final PgnTest pgnTest = PgnExpectedValue.findPgnTestPgnNotListed(pgnFileName);
    final PgnFile pgnFile = LenientPgnParser.parse(pgnTest.getFolderPath(), pgnFileName);
    final ChessBoard board = GeneralUtility.calculateBoard(pgnFile);
    logger.info(pgnFileName);

    assertEquals(Winnable.NO, WinnableAnalyzer.calculateWinnable(board, Side.WHITE));
    assertEquals(Winnable.NO, WinnableAnalyzer.calculateWinnable(board, Side.BLACK));
  }

  @SuppressWarnings("static-method")
  // TODO not workin
  // @Test
  void testFolder() throws Exception {
    final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(PgnTest.CHA_AMBRONA);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      final ChessBoard board = new Board(testCase.fen());
      logger.info(testCase.pgnFileName());

      check(testCase.unwinnableQuickWhite(), Side.WHITE, board);
      check(testCase.unwinnableQuickBlack(), Side.WHITE, board);
    }
  }

  private static void check(UnwinnableQuick unwinnableQuickSide, Side side, ChessBoard board) {
    final Winnable winnable = WinnableAnalyzer.calculateWinnable(board, side);
    switch (unwinnableQuickSide) {
      case UNWINNABLE -> assertEquals(Winnable.NO, winnable);
      case WINNABLE -> assertEquals(Winnable.YES, winnable);
      case POSSIBLY_WINNABLE -> assertEquals(Winnable.UNKNOWN, winnable);
      default -> throw new IllegalArgumentException();
    }
  }
}
