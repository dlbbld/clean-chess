package com.dlb.chess.test.unwinnability;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.Nulls;
import com.dlb.chess.common.ucimove.utility.UciMoveUtility;
import com.dlb.chess.model.UciMove;
import com.dlb.chess.pgn.LenientPgnParser;
import com.dlb.chess.pgn.PgnFile;
import com.dlb.chess.pgn.PgnUtility;
import com.dlb.chess.test.RestrictTestConstants;
import com.dlb.chess.test.common.utility.PgnExtensionUtility;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgn.setup.CreatePgnTestCases;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.unwinnability.UnwinnabilityFullAnalysis;
import com.dlb.chess.unwinnability.UnwinnabilityFullVerdict;
import com.dlb.chess.unwinnability.UnwinnableFullAnalyzer;

class TestUnwinnableFullForLichessGamesHavingHelpMate {

  private static final Logger logger = Nulls.getLogger(TestUnwinnableFullForLichessGamesHavingHelpMate.class);

  /** Cap on files tested when the smoke restriction is active. */
  private static final int MAX_FILES = 10;

  @SuppressWarnings("static-method")
  @Test
  void testFolder() throws Exception {
    final PgnFileTestCaseList testCaseHavingHelpmateList = CreatePgnTestCases
        .getTestList(PgnTest.CHA_LICHESS_QUICK_NOT_DEPTH_THREE_HELPMATE);

    var processedFilesInFolder = 0;
    for (final PgnFileTestCase testCaseHavingHelpmate : testCaseHavingHelpmateList.list()) {
      if (RestrictTestConstants.IS_RESTRICT_UNWINNABLE_FULL_FOR_LICHESS_HELPMATE_TEST
          && processedFilesInFolder >= MAX_FILES) {
        break;
      }
      logger.info(testCaseHavingHelpmate.pgnFileName());

      final String lichessGame = calculateCorrespondingLichessGame(testCaseHavingHelpmate.pgnFileName());
      final PgnTest pgnTestLichessGame = CreatePgnTestCases.findPgnTest(lichessGame);
      final PgnFile pgnFileLichessGame = LenientPgnParser.parse(pgnTestLichessGame.getFolderPath(), lichessGame);

      final Board board = PgnUtility.calculateBoardPerLastMove(pgnFileLichessGame);
      final String fen = board.getFen();
      final Side winner = board.getHavingMove();
      final UnwinnabilityFullAnalysis analysis = UnwinnableFullAnalyzer.unwinnableFull(board, winner);

      assertEquals(UnwinnabilityFullVerdict.WINNABLE, analysis.verdict());
      assertHelpmateLine(fen, winner, analysis.mateLine());

      processedFilesInFolder++;
    }
  }

  private static void assertHelpmateLine(String fen, Side winner, List<UciMove> mateLine) {
    final var board = new Board(fen, false);
    advanceForcedMoves(board);
    for (final UciMove uciMove : mateLine) {
      board.move(UciMoveUtility.convertUciMoveToMoveSpecification(board, uciMove));
    }
    assertEquals(winner.getOppositeSide(), board.getHavingMove());
    assertTrue(board.isCheckmate());
  }

  private static void advanceForcedMoves(Board board) {
    while (board.getLegalMoves().size() == 1 && !board.isFivefoldRepetition() && !board.isSeventyFiveMove()) {
      board.move(Nulls.getFirst(board.getLegalMoves()).moveSpecification());
    }
  }

  private static String calculateCorrespondingLichessGame(String lichessGameHelpmate) {
    final var lichessGameHelpmateWithoutExtension = PgnExtensionUtility.removePgnFileExtension(lichessGameHelpmate);
    final var lichessGameWithoutExtension = Nulls.replace(lichessGameHelpmateWithoutExtension, "_helpmate", "");
    return PgnExtensionUtility.addPgnFileExtension(lichessGameWithoutExtension);
  }
}
