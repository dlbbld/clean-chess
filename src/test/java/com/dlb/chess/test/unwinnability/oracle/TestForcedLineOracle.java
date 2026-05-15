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

/**
 * Exercises {@link ForcedLineOracle} against the BASIC_FORCED corpus: positions where the unique-legal-move chain
 * from the root leads to a terminal status (checkmate / stalemate / insufficient material). Each fixture's
 * expected per-side verdict is encoded in the {@code unwinnableQuickWhite}/{@code unwinnableQuickBlack} columns of
 * {@link PgnFileTestCase}; the convert helper folds Ambrona's three-valued POSSIBLY_WINNABLE into the oracle's
 * UNKNOWN.
 */
class TestForcedLineOracle {

  @SuppressWarnings("static-method")
  @Test
  void testStartPosition() {
    final Board board = new Board(false);

    assertEquals(LimitedUnwinnabilityVerdict.UNKNOWN,
        ForcedLineOracle.calculateUnwinnability(board, Side.WHITE));
    assertEquals(LimitedUnwinnabilityVerdict.UNKNOWN,
        ForcedLineOracle.calculateUnwinnability(board, Side.BLACK));
  }

  @SuppressWarnings("static-method")
  @Test
  void testBasicForcedCorpus() {
    final List<PgnFileTestCase> fixtures = CreatePgnTestCases.getTestList(PgnTest.BASIC_FORCED).list();

    for (final PgnFileTestCase testCase : fixtures) {
      final Board board = new Board(testCase.fen(), false);

      assertEquals(convert(testCase.unwinnableQuickWhite()),
          ForcedLineOracle.calculateUnwinnability(board, Side.WHITE), testCase.pgnFileName());
      assertEquals(convert(testCase.unwinnableQuickBlack()),
          ForcedLineOracle.calculateUnwinnability(board, Side.BLACK), testCase.pgnFileName());
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
