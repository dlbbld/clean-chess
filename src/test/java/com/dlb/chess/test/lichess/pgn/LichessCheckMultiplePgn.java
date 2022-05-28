package com.dlb.chess.test.lichess.pgn;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.pgn.reader.PgnReaderStrict;
import com.dlb.chess.pgn.reader.model.PgnFile;
import com.dlb.chess.utility.TagUtility;

//manual to create multiple pgn file
//1) open database in chessbase
//2) filter games
//3) copy search result to  new database
//4) remove annotations by using Tools in new database
//5) Open new database and select all games
//6) Export to text as pgn
//7) Open the exported PGN in Notepad2 and change encoding to ANSI, save
//8) This resulting PGN can be used here to be splitted

public class LichessCheckMultiplePgn extends AbstractLichessCheck {

  private static final int RESUME_FROM_PGN_NUMBER = 550000;

  // 2020 - March - 13.9 GB - 55,544,817 games
  private static final String MULTIPLE_PGN_FILE_PATH = "L:\\Lichess\\lichess_db_standard_rated_2020-03.pgn";

  private static final Logger logger = NonNullWrapperCommon.getLogger(LichessCheckMultiplePgn.class);

  public static void main(String[] args) {
    doTheCheck(MULTIPLE_PGN_FILE_PATH);
  }

  private static void doTheCheck(String multiplePgnFilePath) {
    logger.printf(Level.INFO, "Processing file %s", multiplePgnFilePath);
    logger.printf(Level.INFO, "Resuming from PGN number %s", RESUME_FROM_PGN_NUMBER);

    var readFileCounter = 0;
    var chess960Counter = 0;
    final var fenCounter = 0;
    var blankLineCounter = 0;
    var isChess960 = false;
    var isFen = false;
    List<String> currentPgnFileLines = new ArrayList<>();
    StringBuilder currentPgnFileString = new StringBuilder();

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
            readFileCounter++;

            // last line must be emmpty
            currentPgnFileLines.add("");
            currentPgnFileString.append("");

            // we check the tags on the string value as this is much faster than creating the PGN only for this
            // purpose
            if (readFileCounter >= RESUME_FROM_PGN_NUMBER
                && currentPgnFileString.indexOf("[Termination \"Time forfeit\"]") != -1
                && (currentPgnFileString.indexOf("[Result \"1-0\"]") != -1
                    || currentPgnFileString.indexOf("[Result \"0-1\"]") != -1)) {
              final PgnFile pgnFile = PgnReaderStrict.readPgn(currentPgnFileLines);
              if (!AbstractLichessCheck.calculateIsTimeForfeitCandidate(pgnFile)) {
                final String siteValue = TagUtility.calculateTagValue(pgnFile, "Site");
                logger.printf(Level.INFO, "Invalid parsing of %s", siteValue);
              } else if (AbstractLichessCheck.calculateIsIncorrectResult(pgnFile)) {
                final String siteValue = TagUtility.calculateTagValue(pgnFile, "Site");
                logger.printf(Level.INFO, "Time forfeit - incorrect result;%s", siteValue);
              } else {
                // logger.info("Time forfeit - possibly correct result - " + file.getName());
              }
            }

          } else {
            chess960Counter++;
          }
          blankLineCounter = 0;
          isChess960 = false;
          isFen = false;
          currentPgnFileLines = new ArrayList<>();
          currentPgnFileString = new StringBuilder();
          if (readFileCounter == RESUME_FROM_PGN_NUMBER) {
            logger.info("Read until resume");
          } else if (readFileCounter > RESUME_FROM_PGN_NUMBER) {
            final var processedFileCounter = readFileCounter - RESUME_FROM_PGN_NUMBER;
            if (processedFileCounter % 10000 == 0) {
              logger.printf(Level.INFO, "Analyzed %s PGN's", processedFileCounter);
            }
          }
          continue;
        }
        currentPgnFileLines.add(line);
        currentPgnFileString.append(line);
      }
    } catch (

    final IOException e) {
      throw new RuntimeException(e);
    }

    logger.printf(Level.INFO, "Created %s files", readFileCounter);
    logger.printf(Level.INFO, "Ignored %s chess960 games", chess960Counter);
    logger.printf(Level.INFO, "Ignored %s games continuing from FEN", fenCounter);
  }

}
