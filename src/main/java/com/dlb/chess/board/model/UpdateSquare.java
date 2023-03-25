package com.dlb.chess.board.model;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.Square;

@SuppressWarnings("null")
public record UpdateSquare(@NonNull Square square, @NonNull Piece piece) {

  public UpdateSquare(Square square) {
    this(square, Piece.NONE);
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (UpdateSquare) obj;
    return piece == other.piece && square == other.square;
  }

}
