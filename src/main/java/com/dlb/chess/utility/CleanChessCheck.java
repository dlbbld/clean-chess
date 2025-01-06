package com.dlb.chess.utility;

import java.nio.file.Paths;

import com.dlb.chess.board.Board;
import com.dlb.chess.pgn.create.PgnCreate;
import com.dlb.chess.pgn.writer.PgnWriter;


public class CleanChessCheck {

  public static void main(String[] args) {
    final var board = new Board();  
    board.performMoves("e4", "e5", "Nf3", "Nf6", "Bc4", "Bc5");

    final var pgnFile = PgnCreate.createPgnFile(board);
    System.out.println(PgnCreate.createPgnFileString(pgnFile));

    PgnWriter.writePgnFile(pgnFile, Paths.get("/tmp"), "test2.pgn");
  }
 
}
