package com.dlb.chess.illegal.model;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.Square;

public record RestorePositionAdviceEmptySquarePutSparePiece(Square square, Piece sparePiece)
    implements RestorePositionAdvice {

  public Square square() {
    return square;
  }

  public Piece sparePiece() {
    return sparePiece;
  }

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
