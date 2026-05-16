package com.dlb.chess.test.unwinnability.oracle;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.Nulls;
import com.dlb.chess.pgn.LenientPgnParser;
import com.dlb.chess.pgn.PgnFile;
import com.dlb.chess.pgn.PgnUtility;
import com.dlb.chess.test.pgn.setup.CreatePgnTestCases;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.test.unwinnability.oracle.enums.LimitedUnwinnabilityVerdict;

class TestLimitedUnwinnabilityOracle {

  private static final Logger logger = Nulls.getLogger(TestLimitedUnwinnabilityOracle.class);

  @SuppressWarnings("static-method")
  @Test
  void testStartPosition() {
    final Board board = new Board(false);

    assertEquals(LimitedUnwinnabilityVerdict.UNKNOWN,
        LimitedUnwinnabilityOracle.calculateUnwinnability(board, Side.WHITE));
    assertEquals(LimitedUnwinnabilityVerdict.UNKNOWN,
        LimitedUnwinnabilityOracle.calculateUnwinnability(board, Side.BLACK));
  }

  @SuppressWarnings("static-method")
  @Test
  void testFen() {
    final var fen = "rnbq1bnr/pppp2pp/PN6/R4k2/4pp2/5N2/1PPPPPPP/2BQKB1R b K - 5 8";
    final Board board = new Board(fen, false);

    assertEquals(LimitedUnwinnabilityVerdict.UNKNOWN,
        LimitedUnwinnabilityOracle.calculateUnwinnability(board, Side.WHITE));
    assertEquals(LimitedUnwinnabilityVerdict.UNKNOWN,
        LimitedUnwinnabilityOracle.calculateUnwinnability(board, Side.BLACK));
  }

  @SuppressWarnings("static-method")
  @Test
  void testPawnWallCombinesAfterShallowTerminationUnknown() {
    final var pgnFileName = "pawn_wall_ambrona_real_game.pgn";

    final PgnTest pgnTest = CreatePgnTestCases.findPgnTestPgnNotListed(pgnFileName);
    final PgnFile pgnFile = LenientPgnParser.parse(pgnTest.getFolderPath(), pgnFileName);
    // PGN replay must NOT auto-detect DEAD_POSITION_UNWINNABLE_QUICK here — the recorded game intentionally passes
    // through a pawn-wall position that the quick analyzer classifies as dead before the PGN's final move. The test
    // itself then asserts the oracle behaviour on the resulting position.
    final Board board = PgnUtility.calculateBoardStrict(pgnFile, false);
    logger.info(pgnFileName);

    assertEquals(LimitedUnwinnabilityVerdict.UNKNOWN,
        ShallowTerminationOracle.calculateUnwinnability(board, Side.WHITE));
    assertEquals(LimitedUnwinnabilityVerdict.UNKNOWN,
        ShallowTerminationOracle.calculateUnwinnability(board, Side.BLACK));

    assertEquals(LimitedUnwinnabilityVerdict.UNWINNABLE,
        LimitedUnwinnabilityOracle.calculateUnwinnability(board, Side.WHITE));
    assertEquals(LimitedUnwinnabilityVerdict.UNWINNABLE,
        LimitedUnwinnabilityOracle.calculateUnwinnability(board, Side.BLACK));
  }

}
