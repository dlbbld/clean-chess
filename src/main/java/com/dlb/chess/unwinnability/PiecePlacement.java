package com.dlb.chess.unwinnability;

import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;

record PiecePlacement(PieceType pieceType, Side side, Square squareOriginal)
    implements Comparable<PiecePlacement> {

  @Override
  public int compareTo(PiecePlacement o) {
    if (this.side != o.side) {
      return this.side.compareTo(o.side);
    }
    return this.squareOriginal.compareTo(o.squareOriginal);
  }

  @Override
  public String toString() {
    return side.getName() + " " + pieceType.getName() + " on " + squareOriginal.getName();
  }

}
