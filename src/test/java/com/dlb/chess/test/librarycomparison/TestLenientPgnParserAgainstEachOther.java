package com.dlb.chess.test.librarycomparison;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.model.PgnHalfMove;
import com.dlb.chess.pgn.PgnFile;
import com.dlb.chess.test.RestrictTestConstants;
import com.dlb.chess.test.librarycarlos.pgn.parser.PgnParserLibraryCarlos;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgn.parser.PgnCacheForStrictPgnParserTestCases;
import com.dlb.chess.test.pgn.parser.model.PgnSan;
import com.dlb.chess.test.pgn.setup.CreatePgnTestCases;

class TestLenientPgnParserAgainstEachOther {

  // Leave empty to test all games, put a game name to only test this game.
  // private static final String ONLY_TEST_GAME = "threefold_castling_white_both_sides_lost";

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestLenientPgnParserAgainstEachOther.class);

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
    for (final PgnFileTestCaseList testCaseList : CreatePgnTestCases.getRestrictedTestListList()) {
      for (final PgnFileTestCase testCase : testCaseList.list()) {
        if (RestrictTestConstants.IS_RESTRICT_PGN_LENIENT_PARSER_API_AGAINST_EACH_OTHER_TEST) {
          switch (testCaseList.pgnTest()) {
            case BASIC_CHECK_WHITE:
            case BASIC_CHECK_BLACK:
            case BASIC_CHECKMATE_WHITE:
            case BASIC_CHECKMATE_BLACK:
            case BASIC_STALEMATE:
              break;
            // $CASES-OMITTED$
            default:
              continue;
          }
        }

        final String pgnFileName = testCase.pgnFileName();

        logger.info(pgnFileName);

        final PgnSan parsedPgn = parcePgnSan(testCaseList.pgnTest().getFolderPath(), pgnFileName);
        final PgnSan carlosParsedPgn = PgnParserLibraryCarlos.parsePgnSan(testCaseList.pgnTest().getFolderPath(),
            pgnFileName);

        assertEquals(parsedPgn, carlosParsedPgn);
      }
    }
  }

  // we extract some of the most important information from the PGN reader we can test against API carlos PGN reader
  // we cannot test the full information against API carlos PGN reader
  public static PgnSan parcePgnSan(Path pgnFolderPath, String pgnFileName) {
    final PgnFile pgnFile = PgnCacheForStrictPgnParserTestCases.getPgn(pgnFolderPath, pgnFileName);
    final Fen startFen = pgnFile.startFen();

    final List<String> sanList = new ArrayList<>();
    for (final PgnHalfMove pgnHalfMove : pgnFile.halfMoveList()) {
      sanList.add(pgnHalfMove.san());
    }
    return new PgnSan(startFen.fen(), sanList);
  }

}
