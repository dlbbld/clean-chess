package com.dlb.chess.model;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.CastlingRight;

@SuppressWarnings("null")
public record CastlingRightBoth(@NonNull CastlingRight castlingRightWhite, @NonNull CastlingRight castlingRightBlack) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (CastlingRightBoth) obj;
    return castlingRightBlack == other.castlingRightBlack && castlingRightWhite == other.castlingRightWhite;
  }

}
