package com.dlb.chess.pgn;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.Nulls;
import com.dlb.chess.common.exceptions.FileSystemAccessException;

/**
 * Serialises a {@link PgnGame} (or a {@link Board}) to a PGN file on disk. All overloads default to
 * {@link WriteMode#SEMANTIC} — honest preservation of what the parse model contains. Callers who need a PGN spec
 * section 8.1.1-conformant artifact pass {@link WriteMode#ARCHIVAL} explicitly.
 */
public class PgnWriter {

  // -------------------------------------------------------------------------------------------------
  // PgnGame entry points — semantic default, explicit-mode overloads
  // -------------------------------------------------------------------------------------------------

  public static void writePgnFile(PgnGame pgnGame, String pgnFilePath) {
    writePgnFile(pgnGame, Nulls.pathOf(pgnFilePath));
  }

  public static void writePgnFile(PgnGame pgnGame, String pgnFilePath, WriteMode writeMode) {
    writePgnFile(pgnGame, Nulls.pathOf(pgnFilePath), writeMode);
  }

  public static void writePgnFile(PgnGame pgnGame, Path folderPath, String pgnFileName) {
    writePgnFile(pgnGame, Nulls.pathResolve(folderPath, pgnFileName));
  }

  public static void writePgnFile(PgnGame pgnGame, Path folderPath, String pgnFileName, WriteMode writeMode) {
    writePgnFile(pgnGame, Nulls.pathResolve(folderPath, pgnFileName), writeMode);
  }

  public static void writePgnFile(PgnGame pgnGame, Path filePath) {
    writePgnFile(pgnGame, filePath, WriteMode.SEMANTIC);
  }

  public static void writePgnFile(PgnGame pgnGame, Path filePath, WriteMode writeMode) {
    final List<String> fileLines = PgnCreate.createPgnLines(pgnGame, writeMode);
    writeLinesReplacing(filePath, fileLines);
  }

  // -------------------------------------------------------------------------------------------------
  // Board entry points — semantic default, explicit-mode overloads
  // -------------------------------------------------------------------------------------------------

  public static void writePgnFile(Board board, List<Tag> tagList, Path folderPath, String pgnFileName) {
    writePgnFile(board, tagList, folderPath, pgnFileName, WriteMode.SEMANTIC);
  }

  public static void writePgnFile(Board board, List<Tag> tagList, Path folderPath, String pgnFileName,
      WriteMode writeMode) {
    final PgnGame pgnGame = PgnCreate.createPgnGame(board, tagList);
    writePgnFile(pgnGame, folderPath, pgnFileName, writeMode);
  }

  public static void writePgnFile(Board board, Path folderPath, String pgnFileName) {
    writePgnFile(board, folderPath, pgnFileName, WriteMode.SEMANTIC);
  }

  public static void writePgnFile(Board board, Path folderPath, String pgnFileName, WriteMode writeMode) {
    final PgnGame pgnGame = PgnCreate.createPgnGame(board);
    writePgnFile(pgnGame, folderPath, pgnFileName, writeMode);
  }

  // -------------------------------------------------------------------------------------------------
  // File I/O
  // -------------------------------------------------------------------------------------------------

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

}
