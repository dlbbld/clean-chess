package com.dlb.chess.model;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.CastlingRight;

public record CastlingRightBoth(CastlingRight castlingRightWhite, CastlingRight castlingRightBlack) {

  public CastlingRight castlingRightWhite() {
    return castlingRightWhite;
  }

  public CastlingRight castlingRightBlack() {
    return castlingRightBlack;
  }

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
