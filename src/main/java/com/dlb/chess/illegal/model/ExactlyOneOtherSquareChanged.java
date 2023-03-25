package com.dlb.chess.illegal.model;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.Square;

@SuppressWarnings("null")
public record ExactlyOneOtherSquareChanged(boolean isHappened, @NonNull Square toSquare) {

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
