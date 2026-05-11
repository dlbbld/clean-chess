package com.dlb.chess.board;

import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.constants.EnumConstants;

/**
 * Internal material checks needed by the board package's rule helpers
 * (currently {@link InsufficientMaterialUtility}). Kept narrow and
 * package-private: the public API does not expose material arithmetic.
 */
abstract class BoardMaterial implements EnumConstants {

  static boolean calculateIsOwnPiece(Side side, Piece pieceOnSquare) {
    return pieceOnSquare != Piece.NONE && pieceOnSquare.getSide() == side;
  }

  static boolean calculateHasPieceType(Side side, PieceType pieceType, StaticPosition staticPosition) {
    final Piece piece = Piece.calculate(side, pieceType);
    for (final Square boardSquare : Square.REAL) {
      if (staticPosition.get(boardSquare) == piece) {
        return true;
      }
    }
    return false;
  }

  static boolean calculateHasKingOnly(Side side, StaticPosition staticPosition) {
    var countKing = 0;
    for (final Square boardSquare : Square.REAL) {
      final Piece pieceOnSquare = staticPosition.get(boardSquare);
      if (pieceOnSquare == Piece.NONE || pieceOnSquare.getSide() != side) {
        continue;
      }
      if (pieceOnSquare.getPieceType() == KING) {
        countKing++;
        continue;
      }
      return false;
    }
    return countKing == 1;
  }

  static boolean calculateHasKingAndKnightOnly(Side side, StaticPosition staticPosition) {
    return calculateHasKingAndAnotherPieceOnly(side, KNIGHT, staticPosition);
  }

  static boolean calculateHasKingAndBishopOnly(Side side, StaticPosition staticPosition) {
    return calculateHasKingAndAnotherPieceOnly(side, BISHOP, staticPosition);
  }

  private static boolean calculateHasKingAndAnotherPieceOnly(Side side, PieceType anotherPieceType,
      StaticPosition staticPosition) {
    var countKing = 0;
    var countAnotherPieces = 0;
    for (final Square boardSquare : Square.REAL) {
      final Piece pieceOnSquare = staticPosition.get(boardSquare);
      if (pieceOnSquare == Piece.NONE || pieceOnSquare.getSide() != side) {
        continue;
      }
      if (pieceOnSquare.getPieceType() == KING) {
        countKing++;
        continue;
      }
      if (pieceOnSquare.getPieceType() != anotherPieceType) {
        return false;
      }
      countAnotherPieces++;
      if (countAnotherPieces > 1) {
        return false;
      }
    }
    return countKing == 1 && countAnotherPieces == 1;
  }

}
