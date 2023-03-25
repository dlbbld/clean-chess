package com.dlb.chess.illegal.model;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.Square;

@SuppressWarnings("null")
public record ExactlyOnePieceVanished(boolean isHappened, @NonNull Square fromSquare) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (ExactlyOnePieceVanished) obj;
    return fromSquare == other.fromSquare && isHappened == other.isHappened;
  }

}
