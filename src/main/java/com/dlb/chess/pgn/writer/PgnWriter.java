package com.dlb.chess.pgn.writer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.exceptions.FileSystemAccessException;
import com.dlb.chess.pgn.create.PgnCreate;
import com.dlb.chess.pgn.parser.model.PgnFile;
import com.dlb.chess.pgn.parser.model.Tag;

public class PgnWriter {

  public static void writePgnFile(PgnFile pgnFile, String pgnFilePath) {
    writePgnFile(pgnFile, NonNullWrapperCommon.pathOf(pgnFilePath));
  }

  public static void writePgnFile(PgnFile pgnFile, Path folderPath, String pgnFileName) {
    final Path filePath = NonNullWrapperCommon.pathResolve(folderPath, pgnFileName);
    writePgnFile(pgnFile, filePath);
  }

  public static void writePgnFile(PgnFile pgnFile, Path filePath) {
    final List<String> fileLines = PgnCreate.createPgnFileLines(pgnFile);
    writeLinesReplacing(filePath, fileLines);
  }

  private static void writeLinesReplacing(Path filePath, List<String> lineList) {
    try {
      Files.deleteIfExists(filePath);
    } catch (final IOException ioe) {
      throw new FileSystemAccessException("Deleting existing file \"" + filePath + "\" failed.", ioe);
    }
    try (var writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
      for (final String line : lineList) {
        writer.write(line);
        writer.write("\n");
      }
    } catch (final IOException ioe) {
      throw new FileSystemAccessException("Writing file \"" + filePath + "\" failed.", ioe);
    }
  }

  public static void writePgnFile(Board board, List<Tag> tagList, Path folderPath, String pgnFileName) {
    final PgnFile pgnFile = PgnCreate.createPgnFile(board, tagList);
    writePgnFile(pgnFile, folderPath, pgnFileName);
  }

  public static void writePgnFile(Board board, Path folderPath, String pgnFileName) {
    final PgnFile pgnFile = PgnCreate.createPgnFile(board);
    writePgnFile(pgnFile, folderPath, pgnFileName);
  }

}
