package com.dlb.chess.common.enums;

import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;

public enum NotationMovingPiece {
  ROOK(PieceType.ROOK),
  KNIGHT(PieceType.KNIGHT),
  BISHOP(PieceType.BISHOP),
  QUEEN(PieceType.QUEEN),
  KING(PieceType.KING);

  private final PieceType pieceType;

  public PieceType getPieceType() {
    return pieceType;
  }

  NotationMovingPiece(PieceType pieceType) {
    this.pieceType = pieceType;
  }

  public static boolean exists(String movingPieceLetter) {
    for (final NotationMovingPiece movingPiece : values()) {
      if (movingPiece.getPieceType().getLetter().equals(movingPieceLetter)) {
        return true;
      }
    }
    return false;
  }

  public static NotationMovingPiece calculate(String movingPieceLetter) {
    if (!exists(movingPieceLetter)) {
      throw new IllegalArgumentException("For this letter no corresponding moving piece exists");
    }
    for (final NotationMovingPiece movingPiece : values()) {
      if (movingPiece.getPieceType().getLetter().equals(movingPieceLetter)) {
        return movingPiece;
      }
    }
    throw new ProgrammingMistakeException();
  }
}
