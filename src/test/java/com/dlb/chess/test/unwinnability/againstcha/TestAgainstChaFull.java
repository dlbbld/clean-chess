package com.dlb.chess.test.unwinnability.againstcha;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.utility.FileUtility;
import com.dlb.chess.common.utility.GeneralUtility;
import com.dlb.chess.fen.FenParser;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.test.unwinnability.againstcha.model.ChaBothRead;
import com.dlb.chess.test.unwinnability.againstcha.model.ChaFullRead;
import com.dlb.chess.unwinnability.full.UnwinnableFullAnalyzer;
import com.dlb.chess.unwinnability.full.model.UnwinnableFullAnalysis;

public class TestAgainstChaFull extends AbstractAgainstCha {

  private static final Logger logger = NonNullWrapperCommon.getLogger(TestAgainstChaFull.class);

  private static final boolean IS_START_FROM_FEN = false;
  private static final String START_FROM_FEN = "8/1k6/8/2B5/8/8/8/1b4K1 b - - 100 196";

  public static void main(String[] args) throws Exception {

    final ChaBothRead bothResult = readChaResultList(FEN_CHA_ANALYSIS_FULL_FILE_PATH);
    // validateBothTestResult(bothResult); //ok
    checkMineQuickAgainstChaFull(bothResult);
  }

  private static void checkMineQuickAgainstChaFull(ChaBothRead bothResult) throws Exception {

    System.out.println("fen;mine;cha");

    var counterDifferences = 0;

    var hasFound = false;

    final var millisecondsBefore = System.currentTimeMillis();
    var testCounter = 0;
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
      testCounter++;

      // logger.info(testCase.pgnFileName());

      final Fen fen = FenParser.parseAdvancedFen(fenStr);

      final ChaFullRead fullChaAnalysis = readFullResult(fen, fen.havingMove().getOppositeSide(),
          bothResult.fullResultList());

      final Board board = new Board(fen);
      final UnwinnableFullAnalysis fullMineAnalysis = UnwinnableFullAnalyzer.unwinnableFull(board,
          fen.havingMove().getOppositeSide());

      final String chaCheckmateLine = fullChaAnalysis.mateLine();
      final String myCheckmateLine = GeneralUtility.composeCheckmateLine(fullMineAnalysis.mateLine());

      final var out = fenStr + "; " + fullMineAnalysis.unwinnableFull() + "; " + fullChaAnalysis.unwinnableFull() + ";"
          + myCheckmateLine + ";" + chaCheckmateLine;

      if (fullChaAnalysis.unwinnableFull() != fullMineAnalysis.unwinnableFull()) {
        counterDifferences++;
        System.out.println("Difference result: " + out);
      } else if (!chaCheckmateLine.equals(myCheckmateLine)) {
        System.out.println("Difference mateline : " + out);
      } else {
        System.out.println("Equal : " + out);
      }

    }
    final var totalMillisecondsBefore = System.currentTimeMillis() - millisecondsBefore;

    final var totalSeconds = totalMillisecondsBefore / 1000;

    final double secondsPerTest = totalSeconds / testCounter;

    logger.printf(Level.INFO, "%d differences found", counterDifferences);
    logger.printf(Level.INFO, "%d tests in %d seconds, %f seconds per test", testCounter, totalSeconds, secondsPerTest);
  }

}
