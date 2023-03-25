package com.dlb.chess.illegal.model;

import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.Square;

@SuppressWarnings("null")
public record RestorePositionAdviceNonEmptySquareSwapPiece(@NonNull Square square, @NonNull Piece pieceSourceSquare,
    @NonNull Piece pieceDestinationSquare, @NonNull List<Square> orderedSwapSquareList)
    implements RestorePositionAdvice {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (RestorePositionAdviceNonEmptySquareSwapPiece) obj;
    return Objects.equals(orderedSwapSquareList, other.orderedSwapSquareList)
        && pieceDestinationSquare == other.pieceDestinationSquare && pieceSourceSquare == other.pieceSourceSquare
        && square == other.square;
  }
}
