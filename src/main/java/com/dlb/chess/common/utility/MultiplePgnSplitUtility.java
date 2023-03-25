package com.dlb.chess.common.utility;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.ConfigurationConstants;

//manual to create multiple pgn file
//1) open database in chessbase
//2) filter games
//3) copy search result to  new database
//4) remove annotations by using Tools in new database
//5) Open new database and select all games
//6) Export to text as pgn
//7) Open the exported PGN in Notepad2 and change encoding to ANSI, save
//8) This resulting PGN can be used here to be splitted

public abstract class MultiplePgnSplitUtility {
  private static final String MULTIPLE_PGN_FILE_PATH = ConfigurationConstants.TEMP_FOLDER_PATH
      + "\\otherdb\\mb-3.45\\mb-3.45.pgn";
  private static final String OUTPUT_FOLDER_PATH = ConfigurationConstants.TEMP_FOLDER_PATH
      + "\\otherdb\\mb-3.45\\split";

  private static final Logger logger = NonNullWrapperCommon.getLogger(MultiplePgnSplitUtility.class);

  public static void main(String[] args) {
    createSinglePgnFilesFromPgnMultipleFile(MULTIPLE_PGN_FILE_PATH, OUTPUT_FOLDER_PATH);
  }

  private static void createSinglePgnFilesFromPgnMultipleFile(String multiplePgnFilePath, String outputFolderPath) {
    FileUtility.deleteFilesInDirectory(outputFolderPath);

    logger.printf(Level.INFO, "Processing file %s", multiplePgnFilePath);

    var writtenFileCounter = 0;
    var chess960Counter = 0;
    final var fenCounter = 0;
    var blankLineCounter = 0;
    var isChess960 = false;
    var isFen = false;
    List<String> currentFileLines = new ArrayList<>();

    final File file = new File(multiplePgnFilePath);
    if (!file.isFile()) {
      throw new IllegalArgumentException("\"" + multiplePgnFilePath + "\" is not a file");
    }
    try (final Scanner myReader = new Scanner(file, StandardCharsets.ISO_8859_1);) {
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

            // last line must be emmpty
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
    } catch (final IOException e) {
      throw new RuntimeException(e);
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
