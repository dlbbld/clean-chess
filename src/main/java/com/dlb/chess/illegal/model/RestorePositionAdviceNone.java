package com.dlb.chess.illegal.model;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.Square;

public record RestorePositionAdviceNone(Square square) implements RestorePositionAdvice {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (RestorePositionAdviceNone) obj;
    return square == other.square;
  }

}
