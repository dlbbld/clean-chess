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
import com.dlb.chess.unwinnability.quick.UnwinnableQuickAnalyzer;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuick;

class TestUnwinnabilityQuick {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestUnwinnabilityQuick.class);

  @SuppressWarnings("static-method")
  // @Test
  void testStartPosition() {
    final Board board = new Board();
    assertEquals(UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuickAnalyzer.unwinnableQuick(board, board.getHavingMove().getOppositeSide()));
  }

  @SuppressWarnings("static-method")
  @Test
  void testFen() {
    // pseudo pawn wall: possiby winnable, ok
    // final var fen = "8/8/3p4/1p2p2k/pP1pP1p1/P2P2P1/6K1/8 w - - 2 41";

    // pseudo pawn wall: unwinnable, ok
    // final var fen = "8/8/8/1p2p2k/pP1pP1p1/P2P2P1/6K1/8 w - - 2 50";

    // two opposite pawns which can possibly promote: possibly winnable, ok
    // final var fen = "8/8/3p4/4p2k/4P3/3P4/6K1/8 b - - 2 41";

    // first half-move line has depth 9 and checkmate of winner on last half-move
    // final var fen = "7k/p6P/7K/r5P1/5B2/8/8/1q6 b - - 4 66";

    // norgaard, not winnable but not detected
    // no semi-open files: possibly winnable (intruders), check
    // final var fen = "1k6/p1p1p1p1/P1P1P1P1/p1p1p1p1/8/8/P1P1P1P1/4K3 w - - 10 100";

    // semi-open files: possibly winnable, ok
    // final var fen = "6k1/p1p1p1P1/6PB/6P1/6p1/6pb/P1P1P1p1/6K1 w - - 0 100";

    // no semi-open files: unwinnable, ok
    // final var fen = "8/p1p1p1p1/6Pk/6pB/6Pb/6pK/P1P1P1P1/8 w - - 0 100";

    final var fen = "7k/8/2R3K1/8/8/8/8/8 b - - 149 100";

    final Board board = new Board(fen);

    assertEquals(UnwinnableQuick.UNWINNABLE, UnwinnableQuickAnalyzer.unwinnableQuick(board, Side.WHITE));
    assertEquals(UnwinnableQuick.UNWINNABLE, UnwinnableQuickAnalyzer.unwinnableQuick(board, Side.BLACK));
  }

  @SuppressWarnings("static-method")
  // @Test
  void testPgnFileValue() {
    final var pgnFileName = "unwinnable_fivefold_inevitable.pgn";

    final PgnTest pgnTest = PgnExpectedValue.findPgnFileBelongingPgnTestNotHavingTestValuesAlready(pgnFileName);
    final PgnFile pgnFile = PgnReader.readPgn(pgnTest.getFolderPath(), pgnFileName);
    final ApiBoard board = GeneralUtility.calculateBoard(pgnFile);
    logger.info(pgnFileName);

    assertEquals(UnwinnableQuick.POSSIBLY_WINNABLE,
        UnwinnableQuickAnalyzer.unwinnableQuick(board, board.getHavingMove().getOppositeSide()));

    assertEquals(UnwinnableQuick.UNWINNABLE,
        UnwinnableQuickAnalyzer.unwinnableQuick(board, board.getHavingMove().getOppositeSide()));
  }

  @SuppressWarnings("static-method")
  @Test
  void testPgnFileExpected() {
    final PgnFileTestCase pgnFileTestCase = PgnExpectedValue
        .findPgnFileBelongingPgnTestCase("25_black_capture_king_pawn.pgn");
    final ApiBoard board = new Board(pgnFileTestCase.fen());
    logger.info(pgnFileTestCase.pgnFileName());

    final UnwinnableQuick unwinnableQuickResultWhite = UnwinnableQuickAnalyzer.unwinnableQuick(board, Side.WHITE);
    assertEquals(pgnFileTestCase.unwinnableQuickWhite(), unwinnableQuickResultWhite);

    final UnwinnableQuick unwinnableQuickResultBlack = UnwinnableQuickAnalyzer.unwinnableQuick(board, Side.BLACK);
    assertEquals(pgnFileTestCase.unwinnableQuickBlack(), unwinnableQuickResultBlack);

  }

  @SuppressWarnings("static-method")
  // @Test
  void testFolder() throws Exception {
    final List<Long> milliSecondsList = new ArrayList<>();

    final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(PgnTest.UNFAIR_AMBRONA);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      final ApiBoard board = new Board(testCase.fen());
      logger.info(testCase.pgnFileName());

      {
        final var beforeMilliSeconds = System.currentTimeMillis();
        final UnwinnableQuick unwinnableQuickWhite = UnwinnableQuickAnalyzer.unwinnableQuick(board, Side.WHITE);
        milliSecondsList.add(System.currentTimeMillis() - beforeMilliSeconds);
        assertEquals(testCase.unwinnableQuickWhite(), unwinnableQuickWhite);
      }

      {
        final var beforeMilliSeconds = System.currentTimeMillis();
        final UnwinnableQuick unwinnableQuickBlack = UnwinnableQuickAnalyzer.unwinnableQuick(board, Side.BLACK);
        milliSecondsList.add(System.currentTimeMillis() - beforeMilliSeconds);
        assertEquals(testCase.unwinnableQuickBlack(), unwinnableQuickBlack);
      }

    }
    PrintDuration.printDuration(milliSecondsList, logger);
  }
}
