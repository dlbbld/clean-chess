package com.dlb.chess.test.unwinnability.oracle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.Nulls;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.pgn.setup.CreatePgnTestCases;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.test.unwinnability.oracle.enums.LimitedUnwinnabilityVerdict;
import com.dlb.chess.unwinnability.UnwinnabilityQuickVerdict;

class TestShallowTerminationOracle {

  private static final Logger logger = Nulls.getLogger(TestShallowTerminationOracle.class);

  @SuppressWarnings("static-method")
  @Test
  void testStartPosition() {
    final Board board = new Board(false);

    assertEquals(LimitedUnwinnabilityVerdict.UNKNOWN,
        ShallowTerminationOracle.calculateUnwinnability(board, Side.WHITE));
    assertEquals(LimitedUnwinnabilityVerdict.UNKNOWN,
        ShallowTerminationOracle.calculateUnwinnability(board, Side.BLACK));
  }

  @SuppressWarnings("static-method")
  @Test
  void testFen() {
    final var fen = "rnbq1bnr/pppp2pp/PN6/R4k2/4pp2/5N2/1PPPPPPP/2BQKB1R b K - 5 8";
    final Board board = new Board(fen, false);

    assertEquals(LimitedUnwinnabilityVerdict.WINNABLE,
        ShallowTerminationOracle.calculateUnwinnability(board, Side.WHITE));
    assertEquals(LimitedUnwinnabilityVerdict.UNKNOWN,
        ShallowTerminationOracle.calculateUnwinnability(board, Side.BLACK));
  }

  @SuppressWarnings("static-method")
  @Test
  void testChaLichessDepthThreeFixtures() {
    final List<PgnFileTestCase> fixtures = CreatePgnTestCases.getTestList(PgnTest.CHA_LICHESS_QUICK_DEPTH_THREE).list();

    for (final PgnFileTestCase testCase : fixtures) {
      logger.info(testCase.pgnFileName());

      final Board board = testCase.finalPosition();

      check(testCase.unwinnableQuickWhite(), ShallowTerminationOracle.calculateUnwinnability(board, Side.WHITE));
      check(testCase.unwinnableQuickBlack(), ShallowTerminationOracle.calculateUnwinnability(board, Side.BLACK));
    }
  }

  /**
   * Corpus designed to exercise each of the oracle's three ply-depths independently. Each fixture is a position where
   * the first terminal status appears at a specific depth (1, 2, or 3) for one side, plus a control where no
   * termination is reachable within 3 plies. Both White-to-move and Black-to-move variants are included; the positions
   * are independent (not just mirrors). See {@code createTestCasesShallowTermination} for the fixture-by-fixture
   * rationale.
   */
  @SuppressWarnings("static-method")
  @Test
  void testShallowTerminationCorpus() {
    final List<PgnFileTestCase> fixtures = CreatePgnTestCases.getTestList(PgnTest.CHA_SHALLOW_TERMINATION).list();

    for (final PgnFileTestCase testCase : fixtures) {
      logger.info(testCase.pgnFileName());

      final Board board = testCase.finalPosition();

      check(testCase.unwinnableQuickWhite(), ShallowTerminationOracle.calculateUnwinnability(board, Side.WHITE));
      check(testCase.unwinnableQuickBlack(), ShallowTerminationOracle.calculateUnwinnability(board, Side.BLACK));
    }
  }

  private static void check(UnwinnabilityQuickVerdict verdictTestCase, LimitedUnwinnabilityVerdict verdictCalculation) {
    switch (verdictTestCase) {
      case UNWINNABLE:
        assertTrue(verdictCalculation == LimitedUnwinnabilityVerdict.UNWINNABLE
            || verdictCalculation == LimitedUnwinnabilityVerdict.UNKNOWN);
        break;
      case WINNABLE:
        assertTrue(verdictCalculation == LimitedUnwinnabilityVerdict.WINNABLE
            || verdictCalculation == LimitedUnwinnabilityVerdict.UNKNOWN);
        break;
      case POSSIBLY_WINNABLE:
        // anything goes
        break;
      default:
        throw new IllegalArgumentException();
    }
  }
}
