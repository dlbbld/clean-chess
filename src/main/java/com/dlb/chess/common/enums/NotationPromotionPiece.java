package com.dlb.chess.common.enums;

import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;

public enum NotationPromotionPiece {
  ROOK(PromotionPieceType.ROOK),
  KNIGHT(PromotionPieceType.KNIGHT),
  BISHOP(PromotionPieceType.BISHOP),
  QUEEN(PromotionPieceType.QUEEN);

  private final PromotionPieceType promotionPieceType;

  NotationPromotionPiece(PromotionPieceType promotionPieceType) {
    this.promotionPieceType = promotionPieceType;
  }

  public PromotionPieceType getPromotionPieceType() {
    return promotionPieceType;
  }

  public static boolean exists(String promotionPieceLetter) {
    for (final NotationPromotionPiece option : values()) {
      if (option.getPromotionPieceType().getPieceType().getLetter().equals(promotionPieceLetter)) {
        return true;
      }
    }
    return false;
  }

  public static boolean existsIgnoreCase(String promotionPieceLetter) {
    final var promotionPieceLetterUpperCase = NonNullWrapperCommon.toUpperCase(promotionPieceLetter);
    return exists(promotionPieceLetterUpperCase);
  }

  public static NotationPromotionPiece calculate(String promotionPieceLetter) {
    if (!exists(promotionPieceLetter)) {
      throw new IllegalArgumentException("For this letter not ignoring case no corresponding enum exists");
    }
    for (final NotationPromotionPiece option : values()) {
      if (option.getPromotionPieceType().getPieceType().getLetter().equals(promotionPieceLetter)) {
        return option;
      }
    }
    throw new ProgrammingMistakeException();
  }

  public static NotationPromotionPiece calculateIgnoreCase(String promotionPieceLetter) {
    if (!existsIgnoreCase(promotionPieceLetter)) {
      throw new IllegalArgumentException("For this letter ignoring case no corresponding enum exists");
    }
    final var promotionPieceLetterUpperCase = NonNullWrapperCommon.toUpperCase(promotionPieceLetter);
    return calculate(promotionPieceLetterUpperCase);
  }
}
