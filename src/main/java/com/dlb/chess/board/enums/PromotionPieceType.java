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
    switch (havingMove) {
      case BLACK:
        switch (pieceType) {
          case BISHOP:
            return Piece.BLACK_BISHOP;
          case KNIGHT:
            return Piece.BLACK_KNIGHT;
          case QUEEN:
            return Piece.BLACK_QUEEN;
          case ROOK:
            return Piece.BLACK_ROOK;
          case NONE:
          default:
            throw new IllegalArgumentException();
        }
      case WHITE:
        switch (pieceType) {
          case BISHOP:
            return Piece.WHITE_BISHOP;
          case KNIGHT:
            return Piece.WHITE_KNIGHT;
          case QUEEN:
            return Piece.WHITE_QUEEN;
          case ROOK:
            return Piece.WHITE_ROOK;
          case NONE:
          default:
            throw new IllegalArgumentException();
        }
      case NONE:
      default:
        throw new IllegalArgumentException();
    }
  }

  private void check() {
    if (this == NONE) {
      throw new NonePointerException();
    }
  }
}
