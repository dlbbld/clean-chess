package com.dlb.chess.test.generate;

import java.nio.file.Path;

import org.apache.logging.log4j.Logger;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.board.Board;
import com.dlb.chess.common.utility.GeneralUtility;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgn.setup.CreatePgnTestCases;
import com.dlb.chess.test.pgntest.enums.PgnTest;
import com.dlb.chess.test.winnable.WinnableAnalyzer;
import com.dlb.chess.test.winnable.enums.Winnable;

public class GenerateChaLichessReport {

  private static final Logger logger = NonNullWrapperCommon.getLogger(GenerateChaLichessReport.class);

  public static void main(String[] args) throws Exception {
    generate(CreatePgnTestCases.getTestList(PgnTest.CHA_LICHESS_QUICK_NOT_DEPTH_THREE));
  }

  private static void generate(PgnFileTestCaseList testCaseList) throws Exception {

    for (final PgnFileTestCase testCase : testCaseList.list()) {
      final String pgnFileName = testCase.pgnFileName();
      final Path folderPath = testCaseList.pgnTest().getFolderPath();

      final var isPawnWallAmbrona = true;
      if (isPawnWallAmbrona) {
        logger.info(pgnFileName + ";" + "pawn wall");
      } else {
        final Board board = GeneralUtility.calculateBoard(folderPath, pgnFileName);
        final Winnable winnableWhite = WinnableAnalyzer.calculateWinnable(board, Side.WHITE);
        final Winnable winnableBlack = WinnableAnalyzer.calculateWinnable(board, Side.BLACK);

        logger.info("winnable white;" + winnableWhite);
        logger.info("winnable black;" + winnableBlack);
      }
    }
  }

}
