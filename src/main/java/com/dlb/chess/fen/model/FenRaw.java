package com.dlb.chess.fen.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

public record FenRaw(String piecePlacement, String havingMove, String castlingRightBothStr,
    String enPassantCaptureTargetSquare, String halfMoveClock, String fullMoveNumber) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (FenRaw) obj;
    return Objects.equals(castlingRightBothStr, other.castlingRightBothStr)
        && Objects.equals(enPassantCaptureTargetSquare, other.enPassantCaptureTargetSquare)
        && Objects.equals(fullMoveNumber, other.fullMoveNumber) && Objects.equals(halfMoveClock, other.halfMoveClock)
        && Objects.equals(havingMove, other.havingMove) && Objects.equals(piecePlacement, other.piecePlacement);
  }

}
