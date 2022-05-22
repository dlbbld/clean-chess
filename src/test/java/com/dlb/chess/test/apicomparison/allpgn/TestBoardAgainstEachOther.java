package com.dlb.chess.test.apicomparison.allpgn;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.fen.constants.FenConstants;
import com.dlb.chess.model.PgnHalfMove;
import com.dlb.chess.pgn.reader.model.PgnFile;
import com.dlb.chess.test.apicarlos.board.ApiCarlosBoard;
import com.dlb.chess.test.apicomparison.utility.CommonTestUtility;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgn.reader.PgnStrictCacheForTestCases;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.PgnTestConstants;

class TestBoardAgainstEachOther {

  // Leave empty to test all games, put a game name to only test this game.
  // private static final String ONLY_TEST_GAME = "threefold_castling_white_both_sides_lost";

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestBoardAgainstEachOther.class);

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
    for (final PgnFileTestCaseList testCaseList : PgnExpectedValue.getRestrictedTestListList()) {
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        // takes 50 minutes with all test cases
        if (PgnTestConstants.IS_RESTRICT_BOARD_API_AGAINST_EACH_OTHER_TEST) {
          switch (testCaseList.pgnTest()) {
            case BASIC_CHECK_WHITE:
            case BASIC_CHECK_BLACK:
            case BASIC_CHECKMATE_WHITE:
            case BASIC_CHECKMATE_BLACK:
            case BASIC_STALEMATE:
            case BASIC_FROM_FEN:
              break;
            // $CASES-OMITTED$
            default:
              continue;
          }
        }

        final String pgnFileName = testCase.pgnFileName();
        logger.info(pgnFileName);

        final PgnFile pgnFile = PgnStrictCacheForTestCases.getPgn(testCaseList.pgnTest().getFolderPath(), pgnFileName);

        if (pgnFile.startFen() != FenConstants.FEN_INITIAL) {
          logger.warn("Skipping PGN as starting from non-initital position:" + pgnFileName);
          continue;
        }

        final ApiBoard board = new Board();
        final ApiBoard carlosBoard = new ApiCarlosBoard();

        for (final PgnHalfMove pgnFileHalfMove : pgnFile.halfMoveList()) {

          final String san = pgnFileHalfMove.san();
          board.performMove(san);
          carlosBoard.performMove(san);

          CommonTestUtility.checkBoardsAgainstEachOtherAll(board, carlosBoard);

        }
      }
    }
  }

}
