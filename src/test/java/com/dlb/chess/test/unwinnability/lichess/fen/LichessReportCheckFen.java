package com.dlb.chess.test.unwinnability.lichess.fen;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.utility.FileUtility;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuick;

public class LichessReportCheckFen extends AbstractLichessCheckFen {

  private static final Logger logger = NonNullWrapperCommon.getLogger(LichessReportCheckFen.class);

  private static final String FEN_FILE_NAME_AMBRONA_RESULT_QUICK = "lichess-ambrona-out-quick.txt";
  private static final String FEN_FILE_NAME_MINE_RESULT_QUICK = "lichess-mine-out-quick-2.txt";

  private static final String FEN_FILE_NAME_COMPARE_QUICK = "lichess-compare-quick.txt";
  private static final String FEN_FILE_COMPARE_QUICK_HEADER = "fen;lichessGameId;mode;colour;cha;check";

  public static void main(String[] args) throws Exception {

    reportDifferencesQuick();
  }

  private static void reportDifferencesQuick() throws IOException {

    final var fenFilePathAmbronaResult = NonNullWrapperCommon.resolve(FEN_FOLDER_PATH,
        FEN_FILE_NAME_AMBRONA_RESULT_QUICK);
    final var fenFilePathMineResult = NonNullWrapperCommon.resolve(FEN_FOLDER_PATH, FEN_FILE_NAME_MINE_RESULT_QUICK);

    final var fenFilePathCompareQuick = NonNullWrapperCommon.resolve(FEN_FOLDER_PATH, FEN_FILE_NAME_COMPARE_QUICK);

    FileUtility.writeFile(fenFilePathCompareQuick, FEN_FILE_COMPARE_QUICK_HEADER);

    final var fileAmbrona = fenFilePathAmbronaResult.toFile();
    final var fileMine = fenFilePathMineResult.toFile();
    final var fileReport = fenFilePathCompareQuick.toFile();

    var counterAll = 0;
    var counterDifferences = 0;
    try (final Scanner readerAmbrona = new Scanner(fileAmbrona, StandardCharsets.ISO_8859_1);
        final Scanner readerMine = new Scanner(fileMine, StandardCharsets.ISO_8859_1);
        Writer writerReport = new OutputStreamWriter(new FileOutputStream(fileReport, true),
            StandardCharsets.UTF_8.name());
        PrintWriter printWriterReport = new PrintWriter(writerReport)) {
      while (readerAmbrona.hasNextLine()) {
        counterAll++;
        final String lineAmbrona = NonNullWrapperCommon.nextLine(readerAmbrona);
        final String lineMine = NonNullWrapperCommon.nextLine(readerMine);

        final var lineAmbronaArr = lineAmbrona.split(";");
        final var ambronaFen = lineAmbronaArr[0];
        final var ambronaGameId = lineAmbronaArr[1];
        final var ambronaMode = lineAmbronaArr[2];
        final var ambronaTestingSide = lineAmbronaArr[3];

        final var lineMineArr = lineMine.split(";");
        final var mineFen = lineMineArr[0];
        final var mineGameId = lineMineArr[1];
        final var mineMode = lineMineArr[2];
        final var mineTestingSide = lineMineArr[3];

        if (!ambronaFen.equals(mineFen) || !ambronaGameId.equals(mineGameId) || !ambronaMode.equals(mineMode)
            || !ambronaTestingSide.equals(mineTestingSide)) {
          throw new IllegalArgumentException("Test results are not as expected");
        }

        @SuppressWarnings("null") final @NonNull String ambronaResult = lineAmbronaArr[4];
        @SuppressWarnings("null") final @NonNull String mineResult = lineMineArr[4];

        if (calculateHasDifference(ambronaResult, mineResult)) {
          counterDifferences++;
          final StringBuilder outputLine = new StringBuilder();
          outputLine.append(ambronaFen).append(";");
          outputLine.append(ambronaGameId).append(";");
          outputLine.append(ambronaMode).append(";");
          outputLine.append(ambronaTestingSide).append(";");

          outputLine.append(ambronaResult).append(";");
          outputLine.append(mineResult).append(";");

          final String outputLineStr = NonNullWrapperCommon.toString(outputLine);
          printWriterReport.println(outputLineStr);
          // logger.info(outputLineStr);
        }

      }
      logger.printf(Level.INFO, "Processed FEN's: %d", counterAll);
      logger.printf(Level.INFO, "Differences: %d", counterDifferences);
    }
  }

  private static boolean calculateHasDifference(String ambronaResult, String mineResult) {
    switch (ambronaResult) {
      case "winnable":
      case "unwinnable":
        if (!mineResult.equals(ambronaResult)) {
          return true;
        }
        break;
      case "undetermined":
        if (!mineResult.equals(UnwinnableQuick.POSSIBLY_WINNABLE.getIdentifier())) {
          return true;
        }
        break;
      default:
        throw new IllegalArgumentException();
    }
    return false;

  }

}
