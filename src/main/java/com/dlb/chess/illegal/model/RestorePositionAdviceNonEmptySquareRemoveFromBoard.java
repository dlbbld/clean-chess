package com.dlb.chess.illegal.model;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.Square;

@SuppressWarnings("null")
public record RestorePositionAdviceNonEmptySquareRemoveFromBoard(@NonNull Square square, @NonNull Piece pieceOnSquare)
    implements RestorePositionAdvice {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (RestorePositionAdviceNonEmptySquareRemoveFromBoard) obj;
    return pieceOnSquare == other.pieceOnSquare && square == other.square;
  }
}
