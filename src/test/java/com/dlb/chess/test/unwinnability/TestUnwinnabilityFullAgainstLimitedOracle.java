package com.dlb.chess.test.unwinnability;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.Nulls;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgn.setup.CreatePgnTestCases;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.test.unwinnability.oracle.LimitedUnwinnabilityOracle;
import com.dlb.chess.test.unwinnability.oracle.enums.LimitedUnwinnabilityVerdict;
import com.dlb.chess.unwinnability.UnwinnabilityFullVerdict;
import com.dlb.chess.unwinnability.UnwinnableFullAnalyzer;

class TestUnwinnabilityFullAgainstLimitedOracle {

  private static final Logger logger = Nulls.getLogger(TestUnwinnabilityFullAgainstLimitedOracle.class);

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {

    final PgnFileTestCaseList testCaseList = CreatePgnTestCases.getTestList(PgnTest.CHA_LICHESS_QUICK_NOT_DEPTH_THREE);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      test(testCase);
    }
  }

  private static void test(PgnFileTestCase testCase) {
    final Board board = new Board(testCase.fen());
    logger.info(testCase.pgnFileName());

    final LimitedUnwinnabilityVerdict verdictWhite = LimitedUnwinnabilityOracle.calculateUnwinnability(board,
        Side.WHITE);
    final UnwinnabilityFullVerdict unwinnableFullWhite = UnwinnableFullAnalyzer.unwinnableFull(board, Side.WHITE)
        .verdict();
    check(verdictWhite, unwinnableFullWhite);

    final LimitedUnwinnabilityVerdict verdictBlack = LimitedUnwinnabilityOracle.calculateUnwinnability(board,
        Side.BLACK);
    final UnwinnabilityFullVerdict unwinnableFullBlack = UnwinnableFullAnalyzer.unwinnableFull(board, Side.BLACK)
        .verdict();
    check(verdictBlack, unwinnableFullBlack);

  }

  private static void check(LimitedUnwinnabilityVerdict verdict, UnwinnabilityFullVerdict unwinnableFull) {
    switch (verdict) {
      case UNWINNABLE:
        assertEquals(UnwinnabilityFullVerdict.UNWINNABLE, unwinnableFull);
        break;
      case WINNABLE:
        assertEquals(UnwinnabilityFullVerdict.WINNABLE, unwinnableFull);
        break;
      case UNKNOWN:
        break;
      default:
        throw new IllegalArgumentException();
    }

    switch (unwinnableFull) {
      case WINNABLE -> assertNotEquals(LimitedUnwinnabilityVerdict.UNWINNABLE, verdict);
      case UNWINNABLE -> {
        final var isIncomplete = verdict == LimitedUnwinnabilityVerdict.UNWINNABLE
            || verdict == LimitedUnwinnabilityVerdict.UNKNOWN;
        assertTrue(isIncomplete);
      }
      case UNDETERMINED -> assertEquals(LimitedUnwinnabilityVerdict.UNKNOWN, verdict);
      default -> throw new IllegalArgumentException();
    }
  }

}
