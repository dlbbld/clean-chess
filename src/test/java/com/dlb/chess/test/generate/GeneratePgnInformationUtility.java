package com.dlb.chess.test.generate;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.common.Nulls;
import com.dlb.chess.common.constants.ConfigurationConstants;
import com.dlb.chess.common.exceptions.FileSystemAccessException;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.pgn.PgnGame;
import com.dlb.chess.pgn.StandardTag;
import com.dlb.chess.pgn.TagUtility;
import com.dlb.chess.test.common.utility.FileUtility;
import com.dlb.chess.test.pgn.parser.PgnCacheForStrictPgnParserTestCases;

public abstract class GeneratePgnInformationUtility {

  private static final Logger logger = Nulls.getLogger(GeneratePgnInformationUtility.class);

  public static void main(String[] args) throws Exception {
    createInformation(Nulls.pathResolve(ConfigurationConstants.TEMP_FOLDER_PATH, "candidates"),
        ConfigurationConstants.TEMP_FOLDER_PATH, "candidates_information.txt");
  }

  private static void createInformation(Path inputFolderPath, Path outputFolderPath, String outputFileName)
      throws Exception {

    final var inputFolder = inputFolderPath.toFile();
    if (!inputFolder.isDirectory()) {
      throw new IllegalArgumentException("\"" + inputFolderPath + "\" is not a directory");
    }

    final var filesList = inputFolder.listFiles();
    if (filesList == null) {
      throw new FileSystemAccessException("File list retrieval for \"" + inputFolderPath + "\" failed");
    }

    final List<String> lineList = new ArrayList<>();
    final StringBuilder headerLine = new StringBuilder();
    headerLine.append("PGN").append(";");
    headerLine.append("White Last name").append(";");
    headerLine.append("Black Last name").append(";");
    headerLine.append("Date").append(";");
    headerLine.append("Site").append(";");
    headerLine.append("Event").append(";");
    headerLine.append("Result");
    lineList.add(Nulls.toString(headerLine));

    for (final File file : filesList) {
      if (file == null) {
        throw new ProgrammingMistakeException("Wrong assumption about API behaviour");
      }
      @SuppressWarnings("null") @NonNull final String fileName = file.getName();
      logger.info("Processing " + fileName);

      final PgnGame pgnGame = PgnCacheForStrictPgnParserTestCases.getPgn(inputFolderPath, fileName);
      final StringBuilder newLine = new StringBuilder();

      newLine.append(fileName).append(";");

      newLine.append(calculateWhiteLastName(pgnGame)).append(";");
      newLine.append(calculateBlackLastName(pgnGame)).append(";");

      final String date = TagUtility.calculateTagValue(pgnGame, StandardTag.DATE);
      newLine.append(date).append(";");

      final String site = TagUtility.calculateTagValue(pgnGame, StandardTag.SITE);
      newLine.append(site).append(";");

      final String event = TagUtility.calculateTagValue(pgnGame, "Event");
      newLine.append(event).append(";");

      final String result = TagUtility.calculateTagValue(pgnGame, StandardTag.RESULT);
      newLine.append(result);

      lineList.add(Nulls.toString(newLine));
    }

    FileUtility.deleteFile(outputFolderPath, outputFileName);
    FileUtility.writeFile(outputFolderPath, outputFileName, lineList);
  }

  private static String calculateWhiteLastName(PgnGame pgnGame) {
    final String playerInformation = TagUtility.calculateTagValue(pgnGame, StandardTag.WHITE);
    return calculateLastNameFromChessBasePlayerInformation(playerInformation);
  }

  private static String calculateBlackLastName(PgnGame pgnGame) {
    final String playerInformation = TagUtility.calculateTagValue(pgnGame, StandardTag.BLACK);
    return calculateLastNameFromChessBasePlayerInformation(playerInformation);
  }

  private static String calculateLastNameFromChessBasePlayerInformation(String playerInformation) {
    if (!calculateIsValidPlayerInformation(playerInformation)) {
      throw new IllegalArgumentException("Invalid player information format");
    }
    final var player = playerInformation.split(", ");
    @SuppressWarnings("null") @NonNull final String lastName = player[0];
    return lastName;
  }

  private static boolean calculateIsValidPlayerInformation(String playerInformation) {
    if (playerInformation.isBlank()) {
      return false;
    }
    final var indexFirstCommaOccurrence = playerInformation.indexOf(",");

    // multiple commas
    // comma not followed by space
    // no value for first name
    if (indexFirstCommaOccurrence == -1 || playerInformation.lastIndexOf(",") != indexFirstCommaOccurrence
        || playerInformation.indexOf(", ") == -1 || indexFirstCommaOccurrence == playerInformation.length() - 2) {
      return false;
    }
    return true;
  }
}
