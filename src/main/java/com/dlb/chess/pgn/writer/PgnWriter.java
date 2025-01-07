package com.dlb.chess.pgn.writer;

import java.nio.file.Path;
import java.util.List;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.utility.FileUtility;
import com.dlb.chess.pgn.create.PgnCreate;
import com.dlb.chess.pgn.reader.model.PgnFile;
import com.dlb.chess.pgn.reader.model.Tag;

public class PgnWriter {

  public static void writePgnFile(PgnFile pgnFile, Path folderPath, String pgnFileName) {
    final Path filePath = FileUtility.calculateFilePath(folderPath, pgnFileName);
    writePgnFile(pgnFile, filePath);
  }

  public static void writePgnFile(PgnFile pgnFile, Path filePath) {
    final List<String> fileLines = PgnCreate.createPgnFileLines(pgnFile);

    FileUtility.deleteIfExists(filePath);
    FileUtility.writeFile(filePath, fileLines);
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
