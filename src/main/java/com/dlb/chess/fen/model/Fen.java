package com.dlb.chess.fen.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.model.CastlingRightBoth;

@SuppressWarnings("null")
public record Fen(@NonNull String fen, @NonNull StaticPosition staticPosition, @NonNull Side havingMove,
    @NonNull CastlingRightBoth castlingRightBoth, @NonNull Square enPassantCaptureTargetSquare, int halfMoveClock,
    int fullMoveNumber) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (Fen) obj;
    return Objects.equals(castlingRightBoth, other.castlingRightBoth)
        && enPassantCaptureTargetSquare == other.enPassantCaptureTargetSquare && Objects.equals(fen, other.fen)
        && fullMoveNumber == other.fullMoveNumber && halfMoveClock == other.halfMoveClock
        && havingMove == other.havingMove && Objects.equals(staticPosition, other.staticPosition);
  }
}
