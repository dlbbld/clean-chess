package com.dlb.chess.board.enums;

import com.dlb.chess.common.exceptions.NonePointerException;

public enum PromotionPieceType {
  ROOK(PieceType.ROOK),
  KNIGHT(PieceType.KNIGHT),
  BISHOP(PieceType.BISHOP),
  QUEEN(PieceType.QUEEN),
  NONE(PieceType.NONE);

  private final PieceType pieceType;

  PromotionPieceType(PieceType pieceType) {
    this.pieceType = pieceType;
  }

  public PieceType getPieceType() {
    check();
    return pieceType;
  }

  public static Piece calculate(Side havingMove, PromotionPieceType pieceType) {
    return switch (havingMove) {
      case BLACK -> switch (pieceType) {
        case BISHOP -> Piece.BLACK_BISHOP;
        case KNIGHT -> Piece.BLACK_KNIGHT;
        case QUEEN -> Piece.BLACK_QUEEN;
        case ROOK -> Piece.BLACK_ROOK;
        case NONE -> throw new IllegalArgumentException();
        default -> throw new IllegalArgumentException();
      };
      case WHITE -> switch (pieceType) {
        case BISHOP -> Piece.WHITE_BISHOP;
        case KNIGHT -> Piece.WHITE_KNIGHT;
        case QUEEN -> Piece.WHITE_QUEEN;
        case ROOK -> Piece.WHITE_ROOK;
        case NONE -> throw new IllegalArgumentException();
        default -> throw new IllegalArgumentException();
      };
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  private void check() {
    if (this == NONE) {
      throw new NonePointerException();
    }
  }
}
