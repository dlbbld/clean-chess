package com.dlb.chess.test.unwinnability;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.Nulls;
import com.dlb.chess.pgn.LenientPgnParser;
import com.dlb.chess.pgn.PgnGame;
import com.dlb.chess.pgn.PgnUtility;
import com.dlb.chess.test.PrintDuration;
import com.dlb.chess.test.model.PgnTestCase;
import com.dlb.chess.test.model.PgnTestCaseList;
import com.dlb.chess.test.pgn.setup.PgnTestCaseCatalog;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.test.unwinnability.againstcha.AmbronaUnwinnabilityOracle;
import com.dlb.chess.unwinnability.UnwinnabilityQuickVerdict;
import com.dlb.chess.unwinnability.UnwinnableQuickAnalyzer;

class TestUnwinnabilityQuick {

  private static final Logger logger = Nulls.getLogger(TestUnwinnabilityQuick.class);

  @SuppressWarnings("static-method")
  @Test
  void testStartPosition() {
    final Board board = new Board(false);
    assertEquals(UnwinnabilityQuickVerdict.POSSIBLY_WINNABLE,
        UnwinnableQuickAnalyzer.unwinnableQuick(board, board.getHavingMove().getOppositeSide()));
  }

  @SuppressWarnings("static-method")
  @Test
  void testFen() {
    // pseudo pawn wall: possiby winnable, ok
    // final var fen = "8/8/3p4/1p2p2k/pP1pP1p1/P2P2P1/6K1/8 w - - 2 41";

    // pseudo pawn wall: unwinnable, ok
    // final var fen = "8/8/8/1p2p2k/pP1pP1p1/P2P2P1/6K1/8 w - - 2 50";

    // two opposite pawns which can possibly promote: possibly winnable, ok
    // final var fen = "8/8/3p4/4p2k/4P3/3P4/6K1/8 b - - 2 41";

    // first half-move line has depth 9 and checkmate of winner on last half-move
    // final var fen = "7k/p6P/7K/r5P1/5B2/8/8/1q6 b - - 4 66";

    // norgaard, not winnable but not detected
    // no semi-open files: possibly winnable (intruders), check
    // final var fen = "1k6/p1p1p1p1/P1P1P1P1/p1p1p1p1/8/8/P1P1P1P1/4K3 w - - 10 100";

    // semi-open files: possibly winnable, ok
    // final var fen = "6k1/p1p1p1P1/6PB/6P1/6p1/6pb/P1P1P1p1/6K1 w - - 0 100";

    // no semi-open files: unwinnable, ok
    // final var fen = "8/p1p1p1p1/6Pk/6pB/6Pb/6pK/P1P1P1P1/8 w - - 0 100";

    final var fen = "7k/8/2R3K1/8/8/8/8/8 b - - 149 100";

    final Board board = new Board(fen, false);

    // Quick deliberately does not enforce the 75-move rule per the CHA paper / Ambrona issue thread.
    // White can mate via 1...Kg8 2.Rc8# but Quick's bounded helpmate search misses it (the first root
    // move at depth 0 isn't Rc8 and the INTERRUPTED early-bail returns before Rc8 is tried). Matches
    // the Ambrona reference (TSV: quickWhite=POSSIBLY_WINNABLE for this FEN). Black has just the
    // king — insufficient material, UNWINNABLE.
    assertEquals(UnwinnabilityQuickVerdict.POSSIBLY_WINNABLE,
        UnwinnableQuickAnalyzer.unwinnableQuick(board, Side.WHITE));
    assertEquals(UnwinnabilityQuickVerdict.UNWINNABLE, UnwinnableQuickAnalyzer.unwinnableQuick(board, Side.BLACK));
  }

  @SuppressWarnings("static-method")
  @Test
  void testMateInOneWhiteToMove() {
    final Board board = new Board("6k1/5ppp/8/8/8/8/5PPP/R3K3 w Q - 0 1", false);

    assertEquals(UnwinnabilityQuickVerdict.POSSIBLY_WINNABLE,
        UnwinnableQuickAnalyzer.unwinnableQuick(board, Side.WHITE));
  }

  @SuppressWarnings("static-method")
  @Test
  void testMateInOneBlackToMove() {
    final Board board = new Board("1q6/8/8/P7/8/n2k4/8/3K4 b - - 0 1", false);

    assertEquals(UnwinnabilityQuickVerdict.POSSIBLY_WINNABLE,
        UnwinnableQuickAnalyzer.unwinnableQuick(board, Side.BLACK));
  }

  @SuppressWarnings("static-method")
  @Test
  void testPgnNotListed() {
    final var pgnName = "01_beyond_seventy_five.pgn";

    final PgnTest pgnTest = PgnTestCaseCatalog.findPgnTestPgnNotListed(pgnName);
    final PgnGame pgnGame = LenientPgnParser.parse(pgnTest.getFolderPath(), pgnName);
    final Board board = PgnUtility.calculateBoard(pgnGame, false);
    logger.info(pgnName);

    // Quick no longer enforces 75-move in the helpmate search (paper / Ambrona issue thread). On this
    // fixture (halfmove=148) both sides come back POSSIBLY_WINNABLE — matches the Ambrona TSV row for
    // this FEN.
    assertEquals(UnwinnabilityQuickVerdict.POSSIBLY_WINNABLE,
        UnwinnableQuickAnalyzer.unwinnableQuick(board, board.getHavingMove()));

    assertEquals(UnwinnabilityQuickVerdict.POSSIBLY_WINNABLE,
        UnwinnableQuickAnalyzer.unwinnableQuick(board, board.getHavingMove().getOppositeSide()));
  }

  @SuppressWarnings("static-method")
  @Test
  void testPgnAgainstTestCase() {
    final PgnTestCase pgnTestCase = PgnTestCaseCatalog.findTestCase("25_black_capture_king_pawn.pgn");
    final Board board = pgnTestCase.finalPosition();
    logger.info(pgnTestCase.pgnName());

    final UnwinnabilityQuickVerdict unwinnableQuickResultWhite = UnwinnableQuickAnalyzer.unwinnableQuick(board,
        Side.WHITE);
    assertEquals(AmbronaUnwinnabilityOracle.get(pgnTestCase.finalFen()).quickWhite(), unwinnableQuickResultWhite);

    final UnwinnabilityQuickVerdict unwinnableQuickResultBlack = UnwinnableQuickAnalyzer.unwinnableQuick(board,
        Side.BLACK);
    assertEquals(AmbronaUnwinnabilityOracle.get(pgnTestCase.finalFen()).quickBlack(), unwinnableQuickResultBlack);

  }

  @SuppressWarnings("static-method")
  @Test
  void testFolder() throws Exception {
    final List<Long> milliSecondsList = new ArrayList<>();

    final PgnTestCaseList testCaseList = PgnTestCaseCatalog.getTestList(PgnTest.CHA_AMBRONA);
    for (final PgnTestCase testCase : testCaseList.list()) {
      final Board board = testCase.finalPosition();
      logger.info(testCase.pgnName());

      {
        final var beforeMilliSeconds = System.currentTimeMillis();
        final UnwinnabilityQuickVerdict unwinnableQuickWhite = UnwinnableQuickAnalyzer.unwinnableQuick(board,
            Side.WHITE);
        milliSecondsList.add(System.currentTimeMillis() - beforeMilliSeconds);
        assertEquals(AmbronaUnwinnabilityOracle.get(testCase.finalFen()).quickWhite(), unwinnableQuickWhite);
      }

      {
        final var beforeMilliSeconds = System.currentTimeMillis();
        final UnwinnabilityQuickVerdict unwinnableQuickBlack = UnwinnableQuickAnalyzer.unwinnableQuick(board,
            Side.BLACK);
        milliSecondsList.add(System.currentTimeMillis() - beforeMilliSeconds);
        assertEquals(AmbronaUnwinnabilityOracle.get(testCase.finalFen()).quickBlack(), unwinnableQuickBlack);
      }

    }
    PrintDuration.printDuration(milliSecondsList, logger);
  }
}
