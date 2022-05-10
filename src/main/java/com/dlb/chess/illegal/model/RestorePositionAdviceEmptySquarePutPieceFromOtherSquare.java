package com.dlb.chess.illegal.model;

import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.Square;

public record RestorePositionAdviceEmptySquarePutPieceFromOtherSquare(Square square, Piece pieceFromOtherSquare,
    List<Square> orderedFromSquareList) implements RestorePositionAdvice {

  public Square square() {
    return square;
  }

  public Piece pieceFromOtherSquare() {
    return pieceFromOtherSquare;
  }

  public List<Square> orderedFromSquareList() {
    return orderedFromSquareList;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (RestorePositionAdviceEmptySquarePutPieceFromOtherSquare) obj;
    return Objects.equals(orderedFromSquareList, other.orderedFromSquareList)
        && pieceFromOtherSquare == other.pieceFromOtherSquare && square == other.square;
  }
}
