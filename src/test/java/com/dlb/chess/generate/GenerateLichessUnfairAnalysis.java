package com.dlb.chess.generate;

import org.apache.logging.log4j.Logger;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.utility.GeneralUtility;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.winnable.WinnableUtility;
import com.dlb.chess.winnable.enums.Winnable;
import com.dlb.chess.test.model.PgnFileTestCase;

public class GenerateLichessUnfairAnalysis {

  private static final Logger logger = NonNullWrapperCommon.getLogger(GenerateLichessUnfairAnalysis.class);

  public static void main(String[] args) throws Exception {
    generate(PgnExpectedValue.getTestList(PgnTest.LICHESS_UNFAIR));
  }

  private static void generate(PgnFileTestCaseList testCaseList) throws Exception {

    for (final PgnFileTestCase testCase : testCaseList.list()) {
      final String pgnFileName = testCase.pgnFileName();
      final String folderPath = testCaseList.pgnTest().getFolderPath();

      final var isPawnWallAmbrona = true;
      if (isPawnWallAmbrona) {
        logger.info(pgnFileName + ";" + "pawn wall");
      } else {
        final ApiBoard board = GeneralUtility.calculateChessBoard(folderPath, pgnFileName);
        final Winnable winnableWhite = WinnableUtility.calculateWinnable(board, Side.WHITE);
        final Winnable winnableBlack = WinnableUtility.calculateWinnable(board, Side.BLACK);

        logger.info("winnable white;" + winnableWhite);
        logger.info("winnable black;" + winnableBlack);
      }
    }
  }

}
