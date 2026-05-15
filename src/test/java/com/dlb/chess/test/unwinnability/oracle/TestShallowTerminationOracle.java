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

  private static final int EXPECTED_DEPTH_THREE_FIXTURE_COUNT = 4;

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

    assertEquals(LimitedUnwinnabilityVerdict.WINNABLE,
        ShallowTerminationOracle.calculateUnwinnability(board, Side.WHITE));
    assertEquals(LimitedUnwinnabilityVerdict.UNKNOWN,
        ShallowTerminationOracle.calculateUnwinnability(board, Side.BLACK));
  }

  @SuppressWarnings("static-method")
  @Test
  void testChaLichessDepthThreeFixtures() {
    final List<PgnFileTestCase> fixtures = CreatePgnTestCases.getTestList(PgnTest.CHA_LICHESS_QUICK_DEPTH_THREE)
        .list();
    assertEquals(EXPECTED_DEPTH_THREE_FIXTURE_COUNT, fixtures.size());

    for (final PgnFileTestCase testCase : fixtures) {
      final Board board = new Board(testCase.fen());
      if ("lichess_mf4MFw9v.pgn".equals(testCase.pgnFileName())) {
        assertEquals(LimitedUnwinnabilityVerdict.UNWINNABLE,
            ShallowTerminationOracle.calculateUnwinnability(board, Side.WHITE), testCase.pgnFileName());
        assertEquals(LimitedUnwinnabilityVerdict.UNKNOWN,
            ShallowTerminationOracle.calculateUnwinnability(board, Side.BLACK), testCase.pgnFileName());
        continue;
      }

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
