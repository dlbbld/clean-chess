package com.dlb.chess.illegal.model;

import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.Square;

@SuppressWarnings("null")
public record RestorePositionAdviceNonEmptySquareMoveToOtherSquare(@NonNull Square square, @NonNull Piece pieceOnSquare,
    @NonNull List<Square> orderedToSquareList) implements RestorePositionAdvice {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (RestorePositionAdviceNonEmptySquareMoveToOtherSquare) obj;
    return Objects.equals(orderedToSquareList, other.orderedToSquareList) && pieceOnSquare == other.pieceOnSquare
        && square == other.square;
  }
}
