package com.dlb.chess.test.unwinnability.validateagainstcha;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.utility.FileUtility;
import com.dlb.chess.fen.FenParser;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.test.unwinnability.model.ValidateBothResult;
import com.dlb.chess.unwinnability.full.UnwinnableFullAnalyzer;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;

public class ValidateFull extends AbstractValidateAgainstCha {

  private static final Logger logger = NonNullWrapperCommon.getLogger(ValidateFull.class);

  private static final boolean IS_START_FROM_FEN = false;
  private static final String START_FROM_FEN = "no_move_half_move_clock_99_black_to_move.pgn";

  public static void main(String[] args) throws Exception {

    final ValidateBothResult bothResult = readChaResultList(FEN_CHA_ANALYSIS_FULL_FILE_PATH);
    // validateBothTestResult(bothResult); //ok
    checkMineQuickAgainstChaFull(bothResult);
  }

  private static void checkMineQuickAgainstChaFull(ValidateBothResult bothResult) throws Exception {

    System.out.println("fen;mine;cha");

    var counterDifferences = 0;

    var hasFound = false;

    for (final String fenStr : FileUtility.readFileLines(FEN_MINE)) {
      if (!hasFound) {
        if (IS_START_FROM_FEN) {
          if (START_FROM_FEN.equals(fenStr)) {
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

      final Fen fen = FenParser.parseAdvancedFen(fenStr);

      final UnwinnableFull fullCha = readFullTestResult(fen, fen.havingMove().getOppositeSide(),
          bothResult.fullResultList());

      final Board board = new Board(fen);
      final UnwinnableFull fullMine = UnwinnableFullAnalyzer.unwinnableFull(board, fen.havingMove().getOppositeSide());

      if (fullCha != fullMine) {
        counterDifferences++;
        System.out.println(fenStr + "; " + fullMine + "; " + fullCha);
      }

      logger.printf(Level.INFO, "%d differences found", counterDifferences);
    }
  }

}
