package com.dlb.chess.illegal.model;

import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.Square;

public record RestorePositionAdviceNonEmptySquareSwapPiece(Square square, Piece pieceSourceSquare,
    Piece pieceDestinationSquare, List<Square> orderedSwapSquareList) implements RestorePositionAdvice {

  public Square square() {
    return square;
  }

  public Piece pieceSourceSquare() {
    return pieceSourceSquare;
  }

  public Piece pieceDestinationSquare() {
    return pieceDestinationSquare;
  }

  public List<Square> orderedSwapSquareList() {
    return orderedSwapSquareList;
  }

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
