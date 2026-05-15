package com.dlb.chess.test.unwinnability.oracle;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.pgn.setup.CreatePgnTestCases;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.test.unwinnability.oracle.enums.LimitedUnwinnabilityVerdict;
import com.dlb.chess.unwinnability.UnwinnabilityQuickVerdict;

class TestShallowTerminationOracle {

  @SuppressWarnings("static-method")
  @Test
  void testStartPosition() {
    final Board board = new Board();

    assertEquals(LimitedUnwinnabilityVerdict.UNKNOWN,
        ShallowTerminationOracle.calculateUnwinnability(board, Side.WHITE));
    assertEquals(LimitedUnwinnabilityVerdict.UNKNOWN,
        ShallowTerminationOracle.calculateUnwinnability(board, Side.BLACK));
  }

  @SuppressWarnings("static-method")
  @Test
  void testFen() {
    final var fen = "rnbq1bnr/pppp2pp/PN6/R4k2/4pp2/5N2/1PPPPPPP/2BQKB1R b K - 5 8";
    final Board board = new Board(fen);

    assertEquals(LimitedUnwinnabilityVerdict.UNKNOWN,
        ShallowTerminationOracle.calculateUnwinnability(board, Side.WHITE));
    assertEquals(LimitedUnwinnabilityVerdict.UNKNOWN,
        ShallowTerminationOracle.calculateUnwinnability(board, Side.BLACK));
  }

  @SuppressWarnings("static-method")
  @Test
  void testChaLichessDepthThreeFixtures() {
    final List<PgnFileTestCase> fixtures = CreatePgnTestCases.getTestList(PgnTest.CHA_LICHESS_QUICK_DEPTH_THREE).list();

    for (final PgnFileTestCase testCase : fixtures) {
      final Board board = new Board(testCase.fen());

      assertEquals(convert(testCase.unwinnableQuickWhite()),
          ShallowTerminationOracle.calculateUnwinnability(board, Side.WHITE), testCase.pgnFileName());
      assertEquals(convert(testCase.unwinnableQuickBlack()),
          ShallowTerminationOracle.calculateUnwinnability(board, Side.BLACK), testCase.pgnFileName());
    }
  }

  /**
   * Corpus designed to exercise each of the oracle's three ply-depths independently. Each fixture is a position
   * where the first terminal status appears at a specific depth (1, 2, or 3) for one side, plus a control where
   * no termination is reachable within 3 plies. Both White-to-move and Black-to-move variants are included; the
   * positions are independent (not just mirrors). See {@code createTestCasesShallowTermination} for the
   * fixture-by-fixture rationale.
   */
  @SuppressWarnings("static-method")
  @Test
  void testShallowTerminationCorpus() {
    final List<PgnFileTestCase> fixtures = CreatePgnTestCases.getTestList(PgnTest.CHA_SHALLOW_TERMINATION).list();

    for (final PgnFileTestCase testCase : fixtures) {
      final Board board = new Board(testCase.fen());

      assertEquals(convert(testCase.unwinnableQuickWhite()),
          ShallowTerminationOracle.calculateUnwinnability(board, Side.WHITE), testCase.pgnFileName());
      assertEquals(convert(testCase.unwinnableQuickBlack()),
          ShallowTerminationOracle.calculateUnwinnability(board, Side.BLACK), testCase.pgnFileName());
    }
  }

  private static LimitedUnwinnabilityVerdict convert(UnwinnabilityQuickVerdict verdict) {
    return switch (verdict) {
      case UNWINNABLE -> LimitedUnwinnabilityVerdict.UNWINNABLE;
      case WINNABLE -> LimitedUnwinnabilityVerdict.WINNABLE;
      case POSSIBLY_WINNABLE -> LimitedUnwinnabilityVerdict.UNKNOWN;
      default -> throw new IllegalArgumentException();
    };
  }
}
