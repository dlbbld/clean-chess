package com.dlb.chess.illegal.model;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.CastlingMove;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.Square;

// TODO promotion

public record DetectMovement(boolean isDetected, Piece movingPiece, Square fromSquare, Square toSquare,
    Piece pieceCaptured, CastlingMove castlingMove, boolean isEnPassantCapture) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (DetectMovement) obj;
    return castlingMove == other.castlingMove && fromSquare == other.fromSquare && isDetected == other.isDetected
        && isEnPassantCapture == other.isEnPassantCapture && movingPiece == other.movingPiece
        && pieceCaptured == other.pieceCaptured && toSquare == other.toSquare;
  }

}
