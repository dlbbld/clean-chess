package com.dlb.chess.test.unwinnability;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.test.pgntest.enums.UnwinnableFullResultTest;
import com.dlb.chess.unwinnability.full.UnwinnableFullCalculator;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;

abstract class TestAmbronaExamplesHavingMoveFull extends AbstractTestAmbronaExamplesHavingMove {

  @SuppressWarnings("static-method")
  @Test
  void testFull() throws Exception {
    final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(PgnTest.UNFAIR_AMBRONA_EXAMPLES);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      final ApiBoard board = new Board(testCase.fen());

      final UnwinnableFullResultTest unwinnableFullResultTest = NonNullWrapperCommon.get(map, testCase.pgnFileName());
      final UnwinnableFull unwinnableFull = UnwinnableFullCalculator.unwinnableFull(board, board.getHavingMove());

      CheckFull.check(unwinnableFullResultTest, unwinnableFull);
    }
  }
}
