package com.dlb.chess.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.enums.UciValidateHelper;

@SuppressWarnings("null")
public record UciMove(@NonNull UciValidateHelper uciMove, @NonNull Square fromSquare, @NonNull Square toSquare,
    @NonNull String text, boolean isPromotion, @NonNull PromotionPieceType promotionPieceType) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (UciMove) obj;
    return fromSquare == other.fromSquare && isPromotion == other.isPromotion
        && promotionPieceType == other.promotionPieceType && Objects.equals(text, other.text)
        && toSquare == other.toSquare && uciMove == other.uciMove;
  }

}
