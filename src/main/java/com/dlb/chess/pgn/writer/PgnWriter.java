package com.dlb.chess.pgn.writer;

import java.util.List;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.utility.FileUtility;
import com.dlb.chess.pgn.create.PgnCreate;
import com.dlb.chess.pgn.reader.model.PgnFile;
import com.dlb.chess.pgn.reader.model.Tag;

public class PgnWriter {

  public static void writePgnFile(PgnFile pgnFile, String folderPath, String pgnFileName) {
    final List<String> fileLines = PgnCreate.createPgnFileLines(pgnFile);

    FileUtility.deleteIfExists(folderPath, pgnFileName);
    FileUtility.writeFile(folderPath, pgnFileName, fileLines);
  }

  public static void writePgnFile(Board board, List<Tag> tagList, String folderPath, String pgnFileName) {
    final PgnFile pgnFile = PgnCreate.createPgnFile(board, tagList);
    writePgnFile(pgnFile, folderPath, pgnFileName);
  }

  public static void writePgnFile(Board board, String folderPath, String pgnFileName) {
    final PgnFile pgnFile = PgnCreate.createPgnFile(board);
    writePgnFile(pgnFile, folderPath, pgnFileName);
  }

}
