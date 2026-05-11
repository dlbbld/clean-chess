package com.dlb.chess.test.unwinnability;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.interfaces.ChessBoard;
import com.dlb.chess.test.PrintDuration;
import com.dlb.chess.test.RestrictTestConstants;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.unwinnability.UnwinnableFullAnalyzer;
import com.dlb.chess.unwinnability.UnwinnableFull;

class TestUnwinnabilityFull {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestUnwinnabilityFull.class);

  @SuppressWarnings("static-method")
  @Test
  void testStartPosition() {
    final Board board = new Board();

    assertEquals(UnwinnableFull.WINNABLE, UnwinnableFullAnalyzer.unwinnableFull(board, Side.WHITE).unwinnableFull());
    assertEquals(UnwinnableFull.WINNABLE, UnwinnableFullAnalyzer.unwinnableFull(board, Side.BLACK).unwinnableFull());
  }

  @SuppressWarnings("static-method")
  @Test
  void testFen() {
    final var fen = "7k/8/2R3K1/8/8/8/8/8 b - - 149 100";
    final Board board = new Board(fen);

    assertEquals(UnwinnableFull.UNWINNABLE, UnwinnableFullAnalyzer.unwinnableFull(board, Side.WHITE).unwinnableFull());
    assertEquals(UnwinnableFull.UNWINNABLE, UnwinnableFullAnalyzer.unwinnableFull(board, Side.BLACK).unwinnableFull());
  }

  @SuppressWarnings("static-method")
  @Test
  void testPgnFileExpected() {
    assumeFalse(RestrictTestConstants.IS_EXCLUDE_LONG_RUNNING_UNWINNABILITY_FULL_PGN_FILE_EXPECTED_TEST);
    final var pgnFileName = "ambrona_10.pgn";

    final PgnFileTestCase pgnFileTestCase = PgnExpectedValue.findTestCase(pgnFileName);
    final ChessBoard board = new Board(pgnFileTestCase.fen());
    logger.info(pgnFileName);

    final UnwinnableFull unwinnableFullWhite = UnwinnableFullAnalyzer.unwinnableFull(board, Side.WHITE)
        .unwinnableFull();
    assertEquals(pgnFileTestCase.unwinnableFullWhite(), unwinnableFullWhite);

    final UnwinnableFull unwinnableFullBlack = UnwinnableFullAnalyzer.unwinnableFull(board, Side.BLACK)
        .unwinnableFull();
    assertEquals(pgnFileTestCase.unwinnableFullBlack(), unwinnableFullBlack);

  }

  @SuppressWarnings("static-method")
  @Test
  void testChaLichessExamples() throws Exception {
    final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(PgnTest.CHA_LICHESS_QUICK_NOT_DEPTH_THREE);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      final ChessBoard board = new Board(testCase.fen());

      logger.info(testCase.pgnFileName());

      final UnwinnableFull unwinnableFullNotHavingMove = UnwinnableFullAnalyzer
          .unwinnableFull(board, board.getHavingMove().getOppositeSide()).unwinnableFull();

      switch (board.getHavingMove().getOppositeSide()) {
        case WHITE -> assertEquals(testCase.unwinnableFullWhite(), unwinnableFullNotHavingMove);
        case BLACK -> assertEquals(testCase.unwinnableFullBlack(), unwinnableFullNotHavingMove);
        default -> throw new IllegalArgumentException();
      }

    }
  }

  static void testFolderPerformance(PgnTest pgnTest) throws Exception {
    final List<Long> milliSecondsList = new ArrayList<>();
    final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(pgnTest);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      final ChessBoard board = new Board(testCase.fen());

      logger.info(testCase.pgnFileName());

      {
        final var beforeMilliSeconds = System.currentTimeMillis();
        final UnwinnableFull unwinnableFullWhite = UnwinnableFullAnalyzer.unwinnableFull(board, Side.WHITE)
            .unwinnableFull();
        milliSecondsList.add(System.currentTimeMillis() - beforeMilliSeconds);
        assertEquals(testCase.unwinnableFullWhite(), unwinnableFullWhite);
      }

      {
        final var beforeMilliSeconds = System.currentTimeMillis();
        final UnwinnableFull unwinnableFullBlack = UnwinnableFullAnalyzer.unwinnableFull(board, Side.BLACK)
            .unwinnableFull();
        milliSecondsList.add(System.currentTimeMillis() - beforeMilliSeconds);
        assertEquals(testCase.unwinnableFullBlack(), unwinnableFullBlack);
      }

    }
    PrintDuration.printDuration(milliSecondsList, logger);
  }

}
