package com.dlb.chess.common.utility;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.board.enums.SquareType;
import com.dlb.chess.common.constants.EnumConstants;

public class MaterialUtility implements EnumConstants {

  public static boolean calculateHasPieceType(Side side, PieceType pieceType, StaticPosition staticPosition) {
    for (final Square boardSquare : Square.BOARD_SQUARE_LIST) {
      final Piece pieceOnSquare = staticPosition.get(boardSquare);
      if (MaterialUtility.calculateIsOwnPieceButNotKing(side, pieceOnSquare)
          && pieceOnSquare.getPieceType() == pieceType) {
        return true;
      }
    }
    return false;
  }

  public static boolean calculateHasPieceType(PieceType pieceType, StaticPosition staticPosition) {
    return calculateHasPieceType(Side.WHITE, pieceType, staticPosition)
        || calculateHasPieceType(Side.BLACK, pieceType, staticPosition);
  }

  public static boolean calculateHasRook(StaticPosition staticPosition) {
    return calculateHasPieceType(PieceType.ROOK, staticPosition);
  }

  public static boolean calculateHasKnight(StaticPosition staticPosition) {
    return calculateHasPieceType(PieceType.KNIGHT, staticPosition);
  }

  public static boolean calculateHasBishop(StaticPosition staticPosition) {
    return calculateHasPieceType(PieceType.BISHOP, staticPosition);
  }

  public static boolean calculateHasQueen(StaticPosition staticPosition) {
    return calculateHasPieceType(PieceType.QUEEN, staticPosition);
  }

  public static boolean calculateHasPawn(StaticPosition staticPosition) {
    return calculateHasPieceType(PieceType.PAWN, staticPosition);
  }

  public static boolean calculateIsOwnPieceButNotKing(Side side, Piece pieceOnSquare) {
    return calculateIsOwnPiece(side, pieceOnSquare) && pieceOnSquare.getPieceType() != KING;
  }

  public static boolean calculateIsOwnPiece(Side side, Piece pieceOnSquare) {
    return pieceOnSquare != Piece.NONE && pieceOnSquare.getSide() == side;
  }

  public static boolean calculateIsOwnPieceType(Side side, Piece pieceOnSquare, PieceType pieceType) {
    return calculateIsOwnPiece(side, pieceOnSquare) && pieceOnSquare.getPieceType() == pieceType;
  }

  public static boolean calculateIsKingOnly(Side side, StaticPosition staticPosition) {
    for (final Square boardSquare : Square.BOARD_SQUARE_LIST) {
      final Piece pieceOnSquare = staticPosition.get(boardSquare);
      if (calculateIsOwnPieceButNotKing(side, pieceOnSquare)) {
        return false;
      }
    }
    return true;
  }

  public static boolean calculateIsKingAndKnightOnly(Side side, StaticPosition staticPosition) {
    return calculateNumberOfPieces(side, staticPosition, ROOK) == 0
        && calculateNumberOfPieces(side, staticPosition, KNIGHT) == 1
        && calculateNumberOfPieces(side, staticPosition, BISHOP) == 0
        && calculateNumberOfPieces(side, staticPosition, QUEEN) == 0
        && calculateNumberOfPieces(side, staticPosition, KING) == 1
        && calculateNumberOfPieces(side, staticPosition, PAWN) == 0;
  }

  public static boolean calculateIsKingAndBishopsOnly(Side side, StaticPosition staticPosition, SquareType squareType) {
    return calculateNumberOfPieces(side, staticPosition, ROOK) == 0
        && calculateNumberOfPieces(side, staticPosition, KNIGHT) == 0
        && calculateNumberOfBishops(side, staticPosition, squareType) >= 1
        && calculateNumberOfPieces(side, staticPosition, QUEEN) == 0
        && calculateNumberOfPieces(side, staticPosition, KING) == 1
        && calculateNumberOfPieces(side, staticPosition, PAWN) == 0;
  }

  // TODO unwinnability - what if no pawns and queens
  public static boolean calculateIsKingAndPawnsOrQueensOnly(Side side, StaticPosition staticPosition) {
    return calculateNumberOfPieces(side, staticPosition, ROOK) == 0
        && calculateNumberOfPieces(side, staticPosition, KNIGHT) == 0
        && calculateNumberOfPieces(side, staticPosition, BISHOP) == 0
        && calculateNumberOfPieces(side, staticPosition, KING) == 1
        && (calculateNumberOfPieces(side, staticPosition, PAWN) >= 1
            || calculateNumberOfPieces(side, staticPosition, QUEEN) >= 1);
  }

  public static int calculateNumberOfPieces(Side side, StaticPosition staticPosition, PieceType pieceType) {
    var total = 0;
    for (final Square boardSquare : Square.BOARD_SQUARE_LIST) {
      final Piece pieceOnSquare = staticPosition.get(boardSquare);
      if (calculateIsOwnPieceType(side, pieceOnSquare, pieceType)) {
        total++;
      }
    }
    return total;
  }

  public static int calculateNumberOfBishops(Side side, StaticPosition staticPosition, SquareType squareType) {
    var total = 0;
    for (final Square boardSquare : Square.BOARD_SQUARE_LIST) {
      final Piece pieceOnSquare = staticPosition.get(boardSquare);
      if (calculateIsOwnPieceType(side, pieceOnSquare, PieceType.BISHOP) && boardSquare.getSquareType() == squareType) {
        total++;
      }
    }
    return total;
  }
}
