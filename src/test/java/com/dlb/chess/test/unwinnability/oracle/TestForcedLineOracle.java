package com.dlb.chess.test.unwinnability.oracle;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.test.model.PgnTestCase;
import com.dlb.chess.test.pgn.setup.PgnTestCaseCatalog;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.test.unwinnability.againstcha.AmbronaUnwinnabilityOracle;
import com.dlb.chess.test.unwinnability.oracle.enums.LimitedUnwinnabilityVerdict;
import com.dlb.chess.unwinnability.UnwinnabilityQuickVerdict;

/**
 * Exercises {@link ForcedLineOracle} against the BASIC_FORCED corpus: positions where the unique-legal-move chain from
 * the root leads to a terminal status (checkmate / stalemate / insufficient material). The convert helper folds
 * Ambrona's three-valued POSSIBLY_WINNABLE into the oracle's UNKNOWN.
 */
class TestForcedLineOracle {

  @SuppressWarnings("static-method")
  @Test
  void testStartPosition() {
    final Board board = new Board(false);

    assertEquals(LimitedUnwinnabilityVerdict.UNKNOWN, ForcedLineOracle.calculateUnwinnability(board, Side.WHITE));
    assertEquals(LimitedUnwinnabilityVerdict.UNKNOWN, ForcedLineOracle.calculateUnwinnability(board, Side.BLACK));
  }

  @SuppressWarnings("static-method")
  @Test
  void testBasicForcedCorpus() {
    final List<PgnTestCase> fixtures = PgnTestCaseCatalog.getTestList(PgnTest.BASIC_FORCED).list();

    for (final PgnTestCase testCase : fixtures) {
      final Board board = testCase.finalPosition();

      assertEquals(convert(AmbronaUnwinnabilityOracle.get(testCase.finalFen()).quickWhite()),
          ForcedLineOracle.calculateUnwinnability(board, Side.WHITE), testCase.pgnFileName());
      assertEquals(convert(AmbronaUnwinnabilityOracle.get(testCase.finalFen()).quickBlack()),
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
