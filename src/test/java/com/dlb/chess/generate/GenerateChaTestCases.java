package com.dlb.chess.generate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import org.apache.logging.log4j.Logger;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.ConfigurationConstants;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.exceptions.FileSystemAccessException;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.utility.FileUtility;
import com.dlb.chess.model.PgnHalfMove;
import com.dlb.chess.pgn.reader.model.PgnFile;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgn.reader.PgnStrictCacheForTestCases;
import com.dlb.chess.test.pgntest.PgnExpectedValue;

public class GenerateChaTestCases implements EnumConstants {

  private static final Logger logger = NonNullWrapperCommon.getLogger(GenerateChaTestCases.class);

  private static final Path LIST_FOR_CHA_FILE_PATH = NonNullWrapperCommon
      .resolve(ConfigurationConstants.TEMP_FOLDER_PATH, "list_cha_2.txt");

  public static void main(String[] args) throws Exception {
    generateChaTestCases(LIST_FOR_CHA_FILE_PATH);
  }

  public static void generateChaTestCases(Path filePath) throws Exception {

    FileUtility.deleteIfExists(filePath);

    logger.info("BEGIN generating code");
    final var file = filePath.toFile();
    try (Writer w = new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8.name());
        PrintWriter pw = new PrintWriter(w)) {

      for (final PgnFileTestCaseList testCaseList : PgnExpectedValue.getRestrictedTestListList()) {
        final Path folderPath = testCaseList.pgnTest().getFolderPath();
        logger.info("Processing folder " + folderPath);

        for (final PgnFileTestCase testCase : testCaseList.list()) {
          // if (!testList.getFolder().equals(PgnTest.BASIC_CHECKMATE)) {
          // continue;
          // }
          logger.info("Processing game " + testCase.pgnFileName());

          final String pgnFileName = testCase.pgnFileName();

          final PgnFile pgnFile = PgnStrictCacheForTestCases.getPgn(folderPath, pgnFileName);

          final ApiBoard board = new Board(pgnFile.startFen());

          var halfMoveCounter = 0;
          pw.println(calculateLine(board, folderPath, pgnFileName, halfMoveCounter));

          for (final PgnHalfMove halfMove : pgnFile.halfMoveList()) {
            halfMoveCounter++;
            board.performMove(halfMove.san());

            pw.println(calculateLine(board, folderPath, pgnFileName, halfMoveCounter));

          }

        }
      }
    } catch (final IOException ioe) {
      throw new FileSystemAccessException("File appending to " + filePath + " failed", ioe);
    }
    logger.info("END generating code");
  }

  private static String calculateLine(ApiBoard board, Path folderPath, String game, int halfMoveCounter) {
    final StringBuilder line = new StringBuilder();
    line.append(board.getFen());

    line.append(";");
    line.append(folderPath);

    line.append(";");
    line.append(game);

    line.append(";");
    line.append(halfMoveCounter);

    line.append(";");
    line.append(board.getHavingMove());

    final var isLastMove = halfMoveCounter == board.getHalfMoveList().size();
    line.append(";");
    line.append(isLastMove);

    String result;
    if (board.isCheckmate()) {
      result = "checkmate";
    } else if (board.isStalemate()) {
      result = "stalemate";
    } else {
      result = "none";
    }
    line.append(";");
    line.append(result);

    final var isHavingMoveInsufficientMaterial = board.isInsufficientMaterial(board.getHavingMove().getOppositeSide());
    line.append(";");
    line.append(isHavingMoveInsufficientMaterial);

    final var isToReplyInsufficientMaterial = board.isInsufficientMaterial(board.getHavingMove().getOppositeSide());
    line.append(";");
    line.append(isToReplyInsufficientMaterial);

    return NonNullWrapperCommon.toString(line);
  }

}
