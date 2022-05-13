package com.dlb.chess.test.unwinnability;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.utility.GeneralUtility;
import com.dlb.chess.pgn.reader.PgnReader;
import com.dlb.chess.pgn.reader.model.PgnFile;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.unwinnability.full.UnwinnableFullCalculator;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;

public class TestUnwinnabilityFull {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestUnwinnabilityFull.class);

  @SuppressWarnings("static-method")
  // @Test
  void testStartPosition() {
    final Board board = new Board();
    assertEquals(UnwinnableFull.WINNABLE, UnwinnableFullCalculator.unwinnableFull(board, Side.WHITE));
  }

  @SuppressWarnings("static-method")
  @Test
  void testFen() {
    final var fen = "8/8/4k3/3R4/2K5/8/8/8 w - - 0 50";
    final Board board = new Board(fen);
    assertEquals(UnwinnableFull.WINNABLE, UnwinnableFullCalculator.unwinnableFull(board, Side.WHITE));
  }

  @SuppressWarnings("static-method")
  // @Test
  void testPgnFileValue() {
    final var pgnFileName = "ae_16.pgn";

    final PgnTest pgnTest = PgnExpectedValue.findPgnFileBelongingPgnTestNotHavingTestValuesAlready(pgnFileName);
    final PgnFile pgnFile = PgnReader.readPgn(pgnTest.getFolderPath(), pgnFileName);
    final ApiBoard board = GeneralUtility.calculateChessBoard(pgnFile);
    logger.info(pgnFileName);

    assertEquals(UnwinnableFull.WINNABLE, UnwinnableFullCalculator.unwinnableFull(board, Side.WHITE));
  }

  @SuppressWarnings("static-method")
  // @Test
  void testPgnFileExpected() {
    final PgnFileTestCase pgnFileTestCase = PgnExpectedValue.findPgnFileBelongingPgnTestCase("ae_10.pgn");
    final ApiBoard board = new Board(pgnFileTestCase.fen());
    logger.info(pgnFileTestCase.pgnFileName());

    final UnwinnableFull unwinnableFull = UnwinnableFullCalculator.unwinnableFull(board,
        board.getHavingMove().getOppositeSide());

    CheckFull.check(pgnFileTestCase.unwinnableFullResultTest(), unwinnableFull);
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
    final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(PgnTest.UNFAIR_AMBRONA_EXAMPLES);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      final ApiBoard board = new Board(testCase.fen());

      logger.info(testCase.pgnFileName());

      final UnwinnableFull unwinnableFull = UnwinnableFullCalculator.unwinnableFull(board,
          board.getHavingMove().getOppositeSide());

      CheckFull.check(testCase.unwinnableFullResultTest(), unwinnableFull);
    }
  }
}
