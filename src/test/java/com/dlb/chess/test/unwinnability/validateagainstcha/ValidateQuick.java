package com.dlb.chess.test.unwinnability.validateagainstcha;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.fen.FenParser;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.test.unwinnability.model.ValidateBothResult;
import com.dlb.chess.unwinnability.quick.UnwinnableQuickAnalyzer;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuick;

public class ValidateQuick extends AbstractValidateAgainstCha {

  private static final Logger logger = NonNullWrapperCommon.getLogger(ValidateQuick.class);

  private static final boolean IS_START_FROM_PGN_FILE = false;
  private static final String START_FROM_PGN_FILE_NAME = "no_move_half_move_clock_99_black_to_move.pgn";

  public static void main(String[] args) throws Exception {

    final ValidateBothResult bothResult = readChaResultList(FEN_CHA_ANALYSIS_BOTH_FILE_PATH);
    // validateBothTestResult(bothResult); //ok
    checkMineQuickAgainstChaQuick(bothResult);
  }

  private static void checkMineQuickAgainstChaQuick(ValidateBothResult bothResult) throws Exception {

    System.out.println("fen;mine;cha");

    var counterDifferences = 0;

    var hasFound = false;
    for (final PgnTest pgnTest : PgnTest.values()) {
      if (pgnTest != PgnTest.UNFAIR_LICHESS_ANALYSIS_GAMES) {
        continue;
      }
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

        // logger.info(testCase.pgnFileName());

        final Fen fen = FenParser.parseAdvancedFen(testCase.fen());

        final UnwinnableQuick quickCha = readQuickTestResult(fen, fen.havingMove().getOppositeSide(),
            bothResult.quickResultList());

        final Board board = new Board(fen);
        final UnwinnableQuick quickMine = UnwinnableQuickAnalyzer.unwinnableQuick(board,
            fen.havingMove().getOppositeSide());

        if (quickCha != quickMine) {
          counterDifferences++;
          System.out.println(testCase.fen() + "; " + quickMine + "; " + quickCha);
        }
      }
    }

    logger.printf(Level.INFO, "%d differences found", counterDifferences);
  }

}
