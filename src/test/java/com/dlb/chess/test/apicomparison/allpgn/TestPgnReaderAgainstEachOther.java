package com.dlb.chess.test.apicomparison.allpgn;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.model.PgnHalfMove;
import com.dlb.chess.pgn.reader.model.PgnFile;
import com.dlb.chess.pgn.reader.model.PgnSan;
import com.dlb.chess.test.apicarlos.pgnreader.ApiCarlosPgnReader;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgn.reader.PgnStrictCacheForTestCases;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.PgnTestConstants;

class TestPgnReaderAgainstEachOther {

  // Leave empty to test all games, put a game name to only test this game.
  // private static final String ONLY_TEST_GAME = "threefold_castling_white_both_sides_lost";

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestPgnReaderAgainstEachOther.class);

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
    for (final PgnFileTestCaseList testCaseList : PgnExpectedValue.getRestrictedTestListList()) {
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        if (PgnTestConstants.IS_RESTRICT_PGN_READER_API_AGAINST_EACH_OTHER_TEST) {
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

        final PgnSan parsedPgn = readPgnSanList(testCaseList.pgnTest().getFolderPath(), pgnFileName);
        final PgnSan carlosParsedPgn = ApiCarlosPgnReader.readPgnSanList(testCaseList.pgnTest().getFolderPath(),
            pgnFileName);

        assertEquals(parsedPgn, carlosParsedPgn);
      }
    }
  }

  // we extract some of the most important information from the PGN reader we can test against API carlos PGN reader
  // we cannot test the full information against API carlos PGN reader
  public static PgnSan readPgnSanList(String pgnFolderPath, String pgnFileName) {
    final PgnFile pgnFile = PgnStrictCacheForTestCases.getPgn(pgnFolderPath, pgnFileName);
    final Fen startFen = pgnFile.startFen();

    final List<String> sanList = new ArrayList<>();
    for (final PgnHalfMove pgnHalfMove : pgnFile.halfMoveList()) {
      sanList.add(pgnHalfMove.san());
    }
    return new PgnSan(startFen.fen(), sanList);
  }

}
