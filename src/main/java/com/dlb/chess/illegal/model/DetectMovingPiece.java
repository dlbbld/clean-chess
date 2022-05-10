package com.dlb.chess.illegal.model;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.Square;

public record DetectMovingPiece(boolean isDetected, Piece movingPiece, Square fromSquare, Square toSquare,
    Piece pieceCaptured) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (DetectMovingPiece) obj;
    return fromSquare == other.fromSquare && isDetected == other.isDetected && movingPiece == other.movingPiece
        && pieceCaptured == other.pieceCaptured && toSquare == other.toSquare;
  }
}
