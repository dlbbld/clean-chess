package com.dlb.chess.test.unwinnability;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.Nulls;
import com.dlb.chess.pgn.LenientPgnParser;
import com.dlb.chess.pgn.PgnFile;
import com.dlb.chess.pgn.PgnFileUtility;
import com.dlb.chess.test.common.utility.PgnExtensionUtility;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgn.setup.CreatePgnTestCases;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.unwinnability.UnwinnableFull;
import com.dlb.chess.unwinnability.UnwinnableFullAnalyzer;

class TestUnwinnableFullForLichessGamesHavingHelpMate {

  private static final Logger logger = Nulls.getLogger(TestUnwinnableFullForLichessGamesHavingHelpMate.class);

  @SuppressWarnings("static-method")
  @Test
  void testFolder() throws Exception {
    final PgnFileTestCaseList testCaseHavingHelpmateList = CreatePgnTestCases
        .getTestList(PgnTest.CHA_LICHESS_QUICK_NOT_DEPTH_THREE_HELPMATE);
    for (final PgnFileTestCase testCaseHavingHelpmate : testCaseHavingHelpmateList.list()) {
      logger.info(testCaseHavingHelpmate.pgnFileName());

      final String lichessGame = calculateCorrespondingLichessGame(testCaseHavingHelpmate.pgnFileName());
      final PgnTest pgnTestLichessGame = CreatePgnTestCases.findPgnTest(lichessGame);
      final PgnFile pgnFileLichessGame = LenientPgnParser.parse(pgnTestLichessGame.getFolderPath(), lichessGame);

      final Board board = PgnFileUtility.calculateBoardPerLastMove(pgnFileLichessGame);
      final UnwinnableFull unwinnableFullHavingMove = UnwinnableFullAnalyzer
          .unwinnableFull(board, board.getHavingMove()).unwinnableFull();

      assertEquals(UnwinnableFull.WINNABLE, unwinnableFullHavingMove);
    }
  }

  private static String calculateCorrespondingLichessGame(String lichessGameHelpmate) {
    final var lichessGameHelpmateWithoutExtension = PgnExtensionUtility.removePgnFileExtension(lichessGameHelpmate);
    final var lichessGameWithoutExtension = lichessGameHelpmateWithoutExtension.replace("_helpmate", "");
    return PgnExtensionUtility.addPgnFileExtension(lichessGameWithoutExtension);
  }
}
