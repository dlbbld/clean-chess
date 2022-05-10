package com.dlb.chess.unwinnability.model;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;

public record PiecePlacement(PieceType type, Side side, Square sq) implements Comparable<PiecePlacement> {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (PiecePlacement) obj;
    return side == other.side && sq == other.sq && type == other.type;
  }

  @Override
  public int compareTo(PiecePlacement o) {
    return this.sq.getName().compareTo(o.sq.getName());
  }

}
