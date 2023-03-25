package com.dlb.chess.common.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.model.CastlingRightBoth;

@SuppressWarnings("null")
public record DynamicPosition(@NonNull Side havingMove, @NonNull StaticPosition staticPosition,
    boolean isEnPassantCapturePossible, @NonNull CastlingRightBoth castlingRightBoth) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (DynamicPosition) obj;
    return Objects.equals(castlingRightBoth, other.castlingRightBoth) && havingMove == other.havingMove
        && isEnPassantCapturePossible == other.isEnPassantCapturePossible
        && Objects.equals(staticPosition, other.staticPosition);
  }

}
