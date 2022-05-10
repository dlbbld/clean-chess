package com.dlb.chess.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.enums.UciValidateHelper;

public record UciMove(UciValidateHelper uciMove, Square fromSquare, Square toSquare, String text, boolean isPromotion,
    PromotionPieceType promotionPieceType) {

  public UciValidateHelper uciMove() {
    return uciMove;
  }

  public Square fromSquare() {
    return fromSquare;
  }

  public Square toSquare() {
    return toSquare;
  }

  public String text() {
    return text;
  }

  public boolean isPromotion() {
    return isPromotion;
  }

  public PromotionPieceType promotionPieceType() {
    return promotionPieceType;
  }

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
