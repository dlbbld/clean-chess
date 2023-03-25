package com.dlb.chess.illegal.model;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.Square;

@SuppressWarnings("null")
public record RestorePositionAdviceEmptySquarePutSparePiece(@NonNull Square square, @NonNull Piece sparePiece)
    implements RestorePositionAdvice {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (RestorePositionAdviceEmptySquarePutSparePiece) obj;
    return sparePiece == other.sparePiece && square == other.square;
  }

}
