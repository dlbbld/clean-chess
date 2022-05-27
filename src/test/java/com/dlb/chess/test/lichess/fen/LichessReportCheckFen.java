package com.dlb.chess.test.lichess.fen;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.utility.FileUtility;
import com.dlb.chess.test.unwinnability.model.ValidateBothResult;
import com.dlb.chess.test.unwinnability.validate.ValidateAllTestCase;

public class LichessReportCheckFen extends AbstractLichessCheckFen {

  private static final Logger logger = NonNullWrapperCommon.getLogger(LichessReportCheckFen.class);

  private static final String FEN_FILE_NAME_AMBRONA_RESULT = "lichess-ambrona-out.txt";

  public static void main(String[] args) throws Exception {

    final var fenFilePathAmbronaResult = FEN_FOLDER_PATH + "\\" + FEN_FILE_NAME_AMBRONA_RESULT;

    final ValidateBothResult bothResult = ValidateAllTestCase.readChaResultList(fenFilePathAmbronaResult);
    // ValidateAllTestCase.validateBothTestResult(bothResult);

    reportDifferences();
  }

  private static void reportDifferences() throws IOException {

    final var fenFilePathMineResult = FEN_FOLDER_PATH + "\\" + FEN_FILE_NAME_MINE_RESULT;

    final List<String> fenFileListResult = FileUtility.readFileLines(fenFilePathMineResult);

    logger.printf(Level.INFO, "Total results: %d", fenFileListResult.size());

    for (final String line : fenFileListResult) {
      final var lineArr = line.split(";");
      final var fen = lineArr[0];
      final var lichessGameId = lineArr[1];

    }

  }

}
