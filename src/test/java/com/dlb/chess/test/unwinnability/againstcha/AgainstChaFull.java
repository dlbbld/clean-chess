package com.dlb.chess.test.unwinnability.againstcha;

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
import com.dlb.chess.unwinnability.full.model.UnwinnableFullAnalysis;

// rnbqkbnr/1ppppppp/8/p7/8/P7/RPPPPPPP/1NBQKBNR b Kkq - 1 2; UNDETERMINED; WINNABLE
// rnbqkbnr/pppppppp/8/8/8/2N5/PPPPPPPP/R1BQKBNR b KQkq - 1 1; UNDETERMINED; WINNABLE
public class AgainstChaFull extends AbstractAgainstCha {

  private static final Logger logger = NonNullWrapperCommon.getLogger(AgainstChaFull.class);

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
      final UnwinnableFullAnalysis fullMineAnalysis = UnwinnableFullAnalyzer.unwinnableFull(board,
          fen.havingMove().getOppositeSide());

      if (fullCha != fullMineAnalysis.unwinnableFull()) {
        counterDifferences++;
        System.out.println(fenStr + "; " + fullMineAnalysis.unwinnableFull() + "; " + fullCha);
      }

    }
    logger.printf(Level.INFO, "%d differences found", counterDifferences);
  }

}
