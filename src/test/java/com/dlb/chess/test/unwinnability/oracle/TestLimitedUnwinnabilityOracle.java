package com.dlb.chess.test.unwinnability.oracle;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.Nulls;
import com.dlb.chess.pgn.LenientPgnParser;
import com.dlb.chess.pgn.PgnGame;
import com.dlb.chess.pgn.PgnUtility;
import com.dlb.chess.test.pgn.setup.PgnTestCaseCatalog;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.test.unwinnability.oracle.enums.LimitedUnwinnabilityVerdict;

class TestLimitedUnwinnabilityOracle {

  private static final Logger logger = Nulls.getLogger(TestLimitedUnwinnabilityOracle.class);

  @SuppressWarnings("static-method")
  @Test
  void testHelpmate() {
    final var pgnFileName = "07_helpmate3_white_to_move.pgn";

    final PgnTest pgnTest = PgnTestCaseCatalog.findPgnTestPgnNotListed(pgnFileName);
    final PgnGame pgnGame = LenientPgnParser.parse(pgnTest.getFolderPath(), pgnFileName);
    // PGN replay must NOT auto-detect DEAD_POSITION_UNWINNABLE_QUICK here — the recorded game intentionally passes
    // through a pawn-wall position that the quick analyzer classifies as dead before the PGN's final move. The test
    // itself then asserts the oracle behaviour on the resulting position.
    final Board board = PgnUtility.calculateBoard(pgnGame, false);
    logger.info(pgnFileName);

    assertEquals(LimitedUnwinnabilityVerdict.WINNABLE,
        LimitedUnwinnabilityOracle.calculateUnwinnability(board, Side.WHITE));
    assertEquals(LimitedUnwinnabilityVerdict.UNWINNABLE,
        LimitedUnwinnabilityOracle.calculateUnwinnability(board, Side.BLACK));
  }

  @SuppressWarnings("static-method")
  @Test
  void testForced() {
    final var pgnFileName = "01_forced_checkmate.pgn";

    final PgnTest pgnTest = PgnTestCaseCatalog.findPgnTestPgnNotListed(pgnFileName);
    final PgnGame pgnGame = LenientPgnParser.parse(pgnTest.getFolderPath(), pgnFileName);
    // PGN replay must NOT auto-detect DEAD_POSITION_UNWINNABLE_QUICK here — the recorded game intentionally passes
    // through a pawn-wall position that the quick analyzer classifies as dead before the PGN's final move. The test
    // itself then asserts the oracle behaviour on the resulting position.
    final Board board = PgnUtility.calculateBoard(pgnGame, false);
    logger.info(pgnFileName);

    assertEquals(LimitedUnwinnabilityVerdict.WINNABLE,
        LimitedUnwinnabilityOracle.calculateUnwinnability(board, Side.WHITE));
    assertEquals(LimitedUnwinnabilityVerdict.UNWINNABLE,
        LimitedUnwinnabilityOracle.calculateUnwinnability(board, Side.BLACK));
  }

  @SuppressWarnings("static-method")
  @Test
  void testPawnWall() {
    final var pgnFileName = "pawn_wall_ambrona_real_game.pgn";

    final PgnTest pgnTest = PgnTestCaseCatalog.findPgnTestPgnNotListed(pgnFileName);
    final PgnGame pgnGame = LenientPgnParser.parse(pgnTest.getFolderPath(), pgnFileName);
    // PGN replay must NOT auto-detect DEAD_POSITION_UNWINNABLE_QUICK here — the recorded game intentionally passes
    // through a pawn-wall position that the quick analyzer classifies as dead before the PGN's final move. The test
    // itself then asserts the oracle behaviour on the resulting position.
    final Board board = PgnUtility.calculateBoard(pgnGame, false);
    logger.info(pgnFileName);

    assertEquals(LimitedUnwinnabilityVerdict.UNWINNABLE,
        LimitedUnwinnabilityOracle.calculateUnwinnability(board, Side.WHITE));
    assertEquals(LimitedUnwinnabilityVerdict.UNWINNABLE,
        LimitedUnwinnabilityOracle.calculateUnwinnability(board, Side.BLACK));
  }

}
