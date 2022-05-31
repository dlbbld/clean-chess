package com.dlb.chess.unwinnability.model;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;

public record PiecePlacement(PieceType pieceType, Side side, Square squareOriginal)
    implements Comparable<PiecePlacement> {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (PiecePlacement) obj;
    return side == other.side && squareOriginal == other.squareOriginal && pieceType == other.pieceType;
  }

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
