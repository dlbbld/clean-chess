package com.dlb.chess.test.unwinnability;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.utility.GeneralUtility;
import com.dlb.chess.pgn.reader.PgnReader;
import com.dlb.chess.pgn.reader.model.PgnFile;
import com.dlb.chess.test.PrintDuration;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.unwinnability.full.UnwinnableFullAnalyzer;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;

public class TestUnwinnabilityFull {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestUnwinnabilityFull.class);

  @SuppressWarnings("static-method")
  @Test
  void testStartPosition() {
    final Board board = new Board();
    assertEquals(UnwinnableFull.WINNABLE, UnwinnableFullAnalyzer.unwinnableFull(board, Side.WHITE));
  }

  @SuppressWarnings("static-method")
  // @Test
  void testFen() {
    final var fen = "6kR/5pp1/1K2p1p1/3r4/8/8/8/8 b - - 3 48";
    final Board board = new Board(fen);
    assertEquals(UnwinnableFull.WINNABLE, UnwinnableFullAnalyzer.unwinnableFull(board, Side.BLACK));
  }

  @SuppressWarnings("static-method")
  // @Test
  void testPgnFileValue() {
    final var pgnFileName = "ae_16.pgn";

    final PgnTest pgnTest = PgnExpectedValue.findPgnFileBelongingPgnTestNotHavingTestValuesAlready(pgnFileName);
    final PgnFile pgnFile = PgnReader.readPgn(pgnTest.getFolderPath(), pgnFileName);
    final ApiBoard board = GeneralUtility.calculateBoard(pgnFile);
    logger.info(pgnFileName);

    assertEquals(UnwinnableFull.WINNABLE, UnwinnableFullAnalyzer.unwinnableFull(board, Side.WHITE));
  }

  @SuppressWarnings("static-method")
  // @Test
  void testPgnFileExpected() {
    final PgnFileTestCase pgnFileTestCase = PgnExpectedValue.findPgnFileBelongingPgnTestCase("ae_10.pgn");
    final ApiBoard board = new Board(pgnFileTestCase.fen());
    logger.info(pgnFileTestCase.pgnFileName());

    final UnwinnableFull unwinnableFullHavingMove = UnwinnableFullAnalyzer.unwinnableFull(board, board.getHavingMove())
        .unwinnableFull();
    assertEquals(pgnFileTestCase.unwinnableFullHavingMove(), unwinnableFullHavingMove);

    final UnwinnableFull unwinnableFullNotHavingMove = UnwinnableFullAnalyzer
        .unwinnableFull(board, board.getHavingMove().getOppositeSide()).unwinnableFull();
    assertEquals(pgnFileTestCase.unwinnableFullNotHavingMove(), unwinnableFullNotHavingMove);

  }

  // not terminating so far
  // ae_04.pgn
  // ae_15_QRvIMh3z.pgn
  // ae_16.pgn
  // ae_06.pgn
  // ae_08.pgn

  @SuppressWarnings("static-method")
  // @Test
  void testFolder() throws Exception {
    final List<Long> milliSecondsList = new ArrayList<>();
    final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(PgnTest.UNFAIR_AMBRONA_EXAMPLES);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      final ApiBoard board = new Board(testCase.fen());

      logger.info(testCase.pgnFileName());

      {
        final var beforeMilliSeconds = System.currentTimeMillis();
        final UnwinnableFull unwinnableFullHavingMove = UnwinnableFullAnalyzer
            .unwinnableFull(board, board.getHavingMove().getOppositeSide()).unwinnableFull();
        milliSecondsList.add(System.currentTimeMillis() - beforeMilliSeconds);
        assertEquals(testCase.unwinnableFullHavingMove(), unwinnableFullHavingMove);
      }

      {
        final var beforeMilliSeconds = System.currentTimeMillis();
        final UnwinnableFull unwinnableFullNotHavingMove = UnwinnableFullAnalyzer
            .unwinnableFull(board, board.getHavingMove().getOppositeSide()).unwinnableFull();
        milliSecondsList.add(System.currentTimeMillis() - beforeMilliSeconds);
        assertEquals(testCase.unwinnableFullNotHavingMove(), unwinnableFullNotHavingMove);
      }

    }
    PrintDuration.printDuration(milliSecondsList, logger);
  }

  @SuppressWarnings("static-method")
  // @Test
  void testPerformance() throws Exception {
    final List<Long> milliSecondsList = new ArrayList<>();
    final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(PgnTest.UNFAIR_LICHESS_ANALYSIS_GAMES);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      final ApiBoard board = new Board(testCase.fen());

      logger.info(testCase.pgnFileName());

      final var beforeMilliSeconds = System.currentTimeMillis();
      final UnwinnableFull unwinnableFull = UnwinnableFullAnalyzer.unwinnableFull(board, board.getHavingMove())
          .unwinnableFull();
      final var durationMilliSeconds = System.currentTimeMillis() - beforeMilliSeconds;

      if (unwinnableFull == UnwinnableFull.WINNABLE) {
        milliSecondsList.add(durationMilliSeconds);
      }

    }
    PrintDuration.printDuration(milliSecondsList, logger);
  }
}
