package com.dlb.chess.test.unwinnability.oracle;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.Nulls;
import com.dlb.chess.common.utility.GeneralUtility;
import com.dlb.chess.pgn.LenientPgnParser;
import com.dlb.chess.pgn.PgnFile;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgn.setup.CreatePgnTestCases;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.test.unwinnability.oracle.enums.LimitedUnwinnabilityVerdict;
import com.dlb.chess.unwinnability.UnwinnabilityQuickVerdict;

class TestLimitedUnwinnabilityOracle {

  private static final Logger logger = Nulls.getLogger(TestLimitedUnwinnabilityOracle.class);

  @SuppressWarnings("static-method")
  @Test
  void testStartPosition() {
    final Board board = new Board();

    assertEquals(LimitedUnwinnabilityVerdict.UNKNOWN,
        LimitedUnwinnabilityOracle.calculateUnwinnability(board, Side.WHITE));
    assertEquals(LimitedUnwinnabilityVerdict.UNKNOWN,
        LimitedUnwinnabilityOracle.calculateUnwinnability(board, Side.BLACK));
  }

  @SuppressWarnings("static-method")
  @Test
  void testFen() {
    final var fen = "rnbq1bnr/pppp2pp/PN6/R4k2/4pp2/5N2/1PPPPPPP/2BQKB1R b K - 5 8";
    final Board board = new Board(fen);

    assertEquals(LimitedUnwinnabilityVerdict.WINNABLE,
        LimitedUnwinnabilityOracle.calculateUnwinnability(board, Side.WHITE));
    assertEquals(LimitedUnwinnabilityVerdict.UNKNOWN,
        LimitedUnwinnabilityOracle.calculateUnwinnability(board, Side.BLACK));
  }

  @SuppressWarnings("static-method")
  @Test
  void testPgnFileValue() {
    final var pgnFileName = "pawn_wall_ambrona_real_game.pgn";

    final PgnTest pgnTest = CreatePgnTestCases.findPgnTestPgnNotListed(pgnFileName);
    final PgnFile pgnFile = LenientPgnParser.parse(pgnTest.getFolderPath(), pgnFileName);
    final Board board = GeneralUtility.calculateBoard(pgnFile);
    logger.info(pgnFileName);

    assertEquals(LimitedUnwinnabilityVerdict.UNWINNABLE,
        LimitedUnwinnabilityOracle.calculateUnwinnability(board, Side.WHITE));
    assertEquals(LimitedUnwinnabilityVerdict.UNWINNABLE,
        LimitedUnwinnabilityOracle.calculateUnwinnability(board, Side.BLACK));
  }

  @SuppressWarnings("static-method")
  // TODO not workin
  // @Test
  void testFolder() throws Exception {
    final PgnFileTestCaseList testCaseList = CreatePgnTestCases.getTestList(PgnTest.CHA_AMBRONA);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      final Board board = new Board(testCase.fen());
      logger.info(testCase.pgnFileName());

      check(testCase.unwinnableQuickWhite(), Side.WHITE, board);
      check(testCase.unwinnableQuickBlack(), Side.WHITE, board);
    }
  }

  private static void check(UnwinnabilityQuickVerdict unwinnableQuickSide, Side side, Board board) {
    final LimitedUnwinnabilityVerdict verdict = LimitedUnwinnabilityOracle.calculateUnwinnability(board, side);
    switch (unwinnableQuickSide) {
      case UNWINNABLE -> assertEquals(LimitedUnwinnabilityVerdict.UNWINNABLE, verdict);
      case WINNABLE -> assertEquals(LimitedUnwinnabilityVerdict.WINNABLE, verdict);
      case POSSIBLY_WINNABLE -> assertEquals(LimitedUnwinnabilityVerdict.UNKNOWN, verdict);
      default -> throw new IllegalArgumentException();
    }
  }
}
