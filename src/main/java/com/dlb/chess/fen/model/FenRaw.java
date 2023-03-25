package com.dlb.chess.fen.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

@SuppressWarnings("null")
public record FenRaw(@NonNull String piecePlacement, @NonNull String havingMove, @NonNull String castlingRightBothStr,
    @NonNull String enPassantCaptureTargetSquare, @NonNull String halfMoveClock, @NonNull String fullMoveNumber) {

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
