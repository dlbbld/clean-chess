package com.dlb.chess.test.common.utility;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.exceptions.ChessApiRuntimeException;
import com.dlb.chess.common.utility.FileUtility;

public final class MultiplePgnSplitUtility {

  private static final Logger logger = NonNullWrapperCommon.getLogger(MultiplePgnSplitUtility.class);

  private MultiplePgnSplitUtility() {
  }

  public static void createSinglePgnFilesFromPgnMultipleFile(Path multiplePgnFilePath, Path outputFolderPath) {
    FileUtility.deleteFilesInDirectory(outputFolderPath);

    logger.printf(Level.INFO, "Processing file %s", multiplePgnFilePath);

    var writtenFileCounter = 0;
    var chess960Counter = 0;
    final var fenCounter = 0;
    var blankLineCounter = 0;
    var isChess960 = false;
    var isFen = false;
    List<String> currentFileLines = new ArrayList<>();

    final var file = multiplePgnFilePath.toFile();
    if (!file.isFile()) {
      throw new IllegalArgumentException("\"" + multiplePgnFilePath + "\" is not a file");
    }
    try (final Scanner myReader = new Scanner(file, StandardCharsets.UTF_8)) {
      while (myReader.hasNextLine()) {
        final String line = NonNullWrapperCommon.nextLine(myReader);

        if (line.length() == 0) {
          blankLineCounter++;
        }
        if (!isChess960 && line.indexOf("chess 960") != -1) {
          isChess960 = true;
        }
        if (!isFen && line.toUpperCase().indexOf("[FEN") != -1) {
          isFen = true;
        }

        if (blankLineCounter == 2) {
          if (!isChess960 && !isFen) {
            writtenFileCounter++;

            currentFileLines.add("");

            final String pgnFileName = PgnExtensionUtility.addPgnFileExtension(padNumber(writtenFileCounter, -1));

            FileUtility.writeFile(outputFolderPath, pgnFileName, currentFileLines);
          } else {
            chess960Counter++;
          }
          blankLineCounter = 0;
          isChess960 = false;
          isFen = false;
          currentFileLines = new ArrayList<>();
          if (writtenFileCounter % 1000 == 0) {
            logger.printf(Level.INFO, "Created %s files", writtenFileCounter);
          }
          continue;
        }
        currentFileLines.add(line);
      }
    } catch (final IOException ioe) {
      @SuppressWarnings("null") @NonNull final String message = ioe.getMessage();
      throw new ChessApiRuntimeException(message);
    }

    logger.printf(Level.INFO, "Created %s files", writtenFileCounter);
    logger.printf(Level.INFO, "Ignored %s chess960 games", chess960Counter);
    logger.printf(Level.INFO, "Ignored %s games continuing from FEN", fenCounter);
  }

  private static String padNumber(int number, int totalFiles) {
    final var maxDigits = (int) StrictMath.floor(StrictMath.log10(totalFiles)) + 1;
    final StringBuilder padded = new StringBuilder();
    padded.append(number);
    while (padded.length() < maxDigits) {
      padded.insert(0, "0");
    }
    return NonNullWrapperCommon.toString(padded);
  }
}
