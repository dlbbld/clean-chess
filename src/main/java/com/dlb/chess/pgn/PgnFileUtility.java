package com.dlb.chess.pgn;

import com.dlb.chess.board.Board;
import com.dlb.chess.model.PgnHalfMove;

public abstract class PgnFileUtility {

  public static Board calculateBoardPerLastMove(PgnFile pgnFile) {
    final Board board = new Board(pgnFile.startFen());
    for (final PgnHalfMove pgnHalfMove : pgnFile.halfMoveList()) {
      board.moveStrict(pgnHalfMove.san());
    }
    return board;
  }

}
