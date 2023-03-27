package com.dlb.chess.test.pgnall;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.pgn.reader.AbstractPgnReader;
import com.dlb.chess.pgn.reader.model.PgnFile;
import com.dlb.chess.test.custom.AbstractTestFenParser;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgn.reader.PgnStrictCacheForTestCases;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.PgnTestConstants;

class TestFenParserAll extends AbstractTestFenParser {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestFenParserAll.class);

  @SuppressWarnings("static-method")
  @Test
  void testPgnFiles() throws Exception {

    for (final PgnFileTestCaseList testCaseList : PgnExpectedValue.getRestrictedTestListList()) {
      if (PgnTestConstants.IS_RESTRICT_FEN_PARSER_ALL_TEST) {
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

  private static void checkFromFen(String folderPath, String pgnFileName) throws Exception {

    logger.info(pgnFileName);

    final PgnFile pgnFile = PgnStrictCacheForTestCases.getPgn(folderPath, pgnFileName);

    final List<MoveSpecification> moveList = AbstractPgnReader.calculateMoveSpecificationList(pgnFile);
    checkGames(pgnFile.startFen().fen(), moveList, false);
  }

}
