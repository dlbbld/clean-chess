package com.dlb.chess.test.unwinnability;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
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
import com.dlb.chess.unwinnability.quick.UnwinnableQuickCalculator;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuick;

public class TestUnwinnabilityQuick {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestUnwinnabilityQuick.class);

  @SuppressWarnings("static-method")
  // @Test
  void testStartPosition() {
    final Board board = new Board();
    assertEquals(UnwinnableQuick.WINNABLE,
        UnwinnableQuickCalculator.unwinnableQuick(board, board.getHavingMove().getOppositeSide()));
  }

  @SuppressWarnings("static-method")
  // @Test
  void testFen() {
    final var fen = "rnbq1bnr/pppp2pp/PN6/R4k2/4pp2/5N2/1PPPPPPP/2BQKB1R b K - 5 8";
    final Board board = new Board(fen);
    assertEquals(UnwinnableQuick.WINNABLE,
        UnwinnableQuickCalculator.unwinnableQuick(board, board.getHavingMove().getOppositeSide()));
  }

  @SuppressWarnings("static-method")
  // @Test
  void testPgnFileValue() {
    final var pgnFileName = "ae_16.pgn";

    final PgnTest pgnTest = PgnExpectedValue.findPgnFileBelongingPgnTestNotHavingTestValuesAlready(pgnFileName);
    final PgnFile pgnFile = PgnReader.readPgn(pgnTest.getFolderPath(), pgnFileName);
    final ApiBoard board = GeneralUtility.calculateChessBoard(pgnFile);
    logger.info(pgnFileName);

    assertEquals(UnwinnableQuick.UNWINNABLE,
        UnwinnableQuickCalculator.unwinnableQuick(board, board.getHavingMove().getOppositeSide()));
  }

  @SuppressWarnings("static-method")
  // @Test
  void testPgnFileExpected() {
    final PgnFileTestCase pgnFileTestCase = PgnExpectedValue.findPgnFileBelongingPgnTestCase("25_black_king_pawn.pgn");
    final ApiBoard board = new Board(pgnFileTestCase.fen());
    logger.info(pgnFileTestCase.pgnFileName());

    final UnwinnableQuick unwinnableQuickResult = UnwinnableQuickCalculator.unwinnableQuick(board,
        board.getHavingMove().getOppositeSide());

    CheckQuick.check(pgnFileTestCase.unwinnableFullResultTest(), unwinnableQuickResult);
  }

  @SuppressWarnings("static-method")
  @Test
  void testFolder() throws Exception {
    final List<Long> milliSecondsList = new ArrayList<>();

    final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(PgnTest.UNFAIR_AMBRONA_EXAMPLES);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      final ApiBoard board = new Board(testCase.fen());
      logger.info(testCase.pgnFileName());

      final var beforeMilliSeconds = System.currentTimeMillis();
      final UnwinnableQuick unwinnableQuick = UnwinnableQuickCalculator.unwinnableQuick(board,
          board.getHavingMove().getOppositeSide());
      milliSecondsList.add(System.currentTimeMillis() - beforeMilliSeconds);

      CheckQuick.check(testCase.unwinnableFullResultTest(), unwinnableQuick);
    }
    PrintDuration.printDuration(milliSecondsList, logger);
  }
}
