package com.dlb.chess.test.pgnall;

import java.nio.file.Path;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.model.PgnHalfMove;
import com.dlb.chess.pgn.parser.model.PgnFile;
import com.dlb.chess.test.RestrictTestConstants;
import com.dlb.chess.test.custom.AbstractTestFenParser;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgn.parser.PgnCacheForStrictPgnParserTestCases;
import com.dlb.chess.test.pgntest.PgnExpectedValue;

class TestFenParserAll extends AbstractTestFenParser {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestFenParserAll.class);

  @SuppressWarnings("static-method")
  @Test
  void testPgnFiles() throws Exception {

    for (final PgnFileTestCaseList testCaseList : PgnExpectedValue.getRestrictedTestListList()) {
      if (RestrictTestConstants.IS_RESTRICT_PGN_FEN_PARSER_ALL_TEST) {
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
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        checkFromFen(testCaseList.pgnTest().getFolderPath(), testCase.pgnFileName());
      }
    }
  }

  private static void checkFromFen(Path folderPath, String pgnFileName) throws Exception {

    logger.info(pgnFileName);

    final PgnFile pgnFile = PgnCacheForStrictPgnParserTestCases.getPgn(folderPath, pgnFileName);

    final ApiBoard board = new Board(pgnFile.startFen());
    for (final PgnHalfMove halfMove : pgnFile.halfMoveList()) {
      board.performMove(halfMove.san());
    }
    final List<MoveSpecification> moveList = board.getPerformedMoveSpecificationList();
    checkGames(pgnFile.startFen().fen(), moveList, false);
  }

}
