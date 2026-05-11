package com.dlb.chess.test.unwinnability;

import org.apache.logging.log4j.Logger;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgn.setup.CreatePgnTestCases;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.test.winnable.WinnableAnalyzer;
import com.dlb.chess.test.winnable.enums.Winnable;
import com.dlb.chess.unwinnability.UnwinnableFull;
import com.dlb.chess.unwinnability.UnwinnableFullAnalyzer;

class TestUnwinnabilityFullAgainstWinnability {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestUnwinnabilityFullAgainstWinnability.class);

  @SuppressWarnings("static-method")
  // TODO test cases
  // known ambrona_10.pgn not working
  // but lichess examples also not working
  // @Test
  void test() throws Exception {

    final PgnFileTestCaseList testCaseList = CreatePgnTestCases.getTestList(PgnTest.CHA_LICHESS_QUICK_NOT_DEPTH_THREE);
    for (final PgnFileTestCase testCase : testCaseList.list()) {
      test(testCase);
    }
  }

  private static void test(PgnFileTestCase testCase) {
    final Board board = new Board(testCase.fen());
    logger.info(testCase.pgnFileName());

    final Winnable winnableWhite = WinnableAnalyzer.calculateWinnable(board, Side.WHITE);
    final UnwinnableFull unwinnableFullWhite = UnwinnableFullAnalyzer.unwinnableFull(board, Side.WHITE)
        .unwinnableFull();
    CheckFull.check(winnableWhite, unwinnableFullWhite);

    final Winnable winnableBlack = WinnableAnalyzer.calculateWinnable(board, Side.BLACK);
    final UnwinnableFull unwinnableFullBlack = UnwinnableFullAnalyzer.unwinnableFull(board, Side.BLACK)
        .unwinnableFull();
    CheckFull.check(winnableBlack, unwinnableFullBlack);

  }
}