package com.dlb.chess.utility;

import com.dlb.chess.board.Board;
import com.dlb.chess.model.PgnHalfMove;
import com.dlb.chess.pgn.reader.model.PgnFile;

public abstract class PgnUtility {

  public static Board calculateBoardPerLastMove(PgnFile pgnFile) {
    final Board board = new Board(pgnFile.startFen());
    for (final PgnHalfMove pgnHalfMove : pgnFile.halfMoveList()) {
      board.performMove(pgnHalfMove.san());
    }
    return board;
  }

}
