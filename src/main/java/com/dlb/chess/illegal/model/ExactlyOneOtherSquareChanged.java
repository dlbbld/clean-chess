package com.dlb.chess.illegal.model;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.Square;

public record ExactlyOneOtherSquareChanged(boolean isHappened, Square toSquare) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (ExactlyOneOtherSquareChanged) obj;
    return isHappened == other.isHappened && toSquare == other.toSquare;
  }
}
