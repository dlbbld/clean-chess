package com.dlb.chess.test.unwinnability.lichess.pgn;

import java.io.File;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.exceptions.FileSystemAccessException;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.pgn.reader.model.PgnFile;
import com.dlb.chess.test.pgn.reader.PgnStrictCacheForTestCases;
import com.dlb.chess.utility.TagUtility;

public class LichessCheckSinglePgn extends AbstractLichessCheck {

  private static final Logger logger = NonNullWrapperCommon.getLogger(LichessCheckSinglePgn.class);

  private static final String PGN_FOLDER_PATH = "D:\\Lichess\\split";

  public static void main(String[] args) {
    doTheCheck(PGN_FOLDER_PATH);
  }

  private static void doTheCheck(String pngOutFolderPath) {
    final File folder = new File(pngOutFolderPath);
    if (!folder.isDirectory()) {
      throw new IllegalArgumentException("\"" + pngOutFolderPath + "\" is not a directory");
    }

    final var filesList = folder.listFiles();
    if (filesList == null) {
      throw new FileSystemAccessException("File list retrieval for \"" + pngOutFolderPath + "\" failed");
    }

    var totalProcessed = 0;
    for (final File file : filesList) {
      if (file == null) {
        throw new ProgrammingMistakeException("Wrong assumption about API behaviour");
      }
      totalProcessed++;
      // logger.info("Processing " + file.getName());

      if (totalProcessed % 100 == 0) {
        logger.printf(Level.INFO, "Processing total %i", totalProcessed);
      }
      final PgnFile pgnFile;

      try {
        pgnFile = PgnStrictCacheForTestCases.getPgn(pngOutFolderPath, NonNullWrapperCommon.getName(file));
        if (calculateIsTimeForfeitCandidate(pgnFile)) {
          if (calculateIsIncorrectResult(pgnFile)) {
            final String siteValue = TagUtility.calculateTagValue(pgnFile, "Site");
            logger.printf(Level.INFO, "Time forfeit - incorrect result;%s;%s", file.getName(), siteValue);
          } else {
            // logger.info("Time forfeit - possibly correct result - " + file.getName());
          }
        }
      } catch (final Exception e) {
        logger.printf(Level.ERROR, "PGN parse error for %s because of %s", file.getName(), e.getMessage());
      }

    }
  }

}
