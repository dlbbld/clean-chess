package com.dlb.chess.test.unwinnability;

import org.apache.logging.log4j.Logger;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.test.winnable.WinnableAnalyzer;
import com.dlb.chess.test.winnable.enums.Winnable;
import com.dlb.chess.unwinnability.full.UnwinnableFullAnalyzer;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;

public class TestUnwinnabilityFullAgainstWinnability {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestUnwinnabilityFullAgainstWinnability.class);

  private static final boolean IS_START_FROM_PGN_FILE = false;
  private static final String START_FROM_PGN_FILE_NAME = "02_white_rook_knight.pgn";

  @SuppressWarnings("static-method")
  // @Test
  void test() throws Exception {
    var hasFound = false;
    for (final PgnTest pgnTest : PgnTest.values()) {
      final PgnFileTestCaseList testCaseList = PgnExpectedValue.getTestList(pgnTest);
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        if (!hasFound) {
          if (IS_START_FROM_PGN_FILE) {
            if (START_FROM_PGN_FILE_NAME.equals(testCase.pgnFileName())) {
              hasFound = true;
            }
          } else {
            hasFound = true;
          }
        }
        if (!hasFound) {
          continue;
        }

        final ApiBoard board = new Board(testCase.fen());
        logger.info(testCase.pgnFileName());

        // not having move
        {
          final Winnable winnable = WinnableAnalyzer.calculateWinnable(board, board.getHavingMove().getOppositeSide());
          final UnwinnableFull unwinnableFull = UnwinnableFullAnalyzer
              .unwinnableFull(board, board.getHavingMove().getOppositeSide()).unwinnableFull();

          CheckFull.check(winnable, unwinnableFull);
        }

        // having move
        {
          final Winnable winnable = WinnableAnalyzer.calculateWinnable(board, board.getHavingMove());
          final UnwinnableFull unwinnableFull = UnwinnableFullAnalyzer.unwinnableFull(board, board.getHavingMove())
              .unwinnableFull();

          CheckFull.check(winnable, unwinnableFull);
        }
      }
    }
  }

}