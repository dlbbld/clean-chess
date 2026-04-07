package com.dlb.chess.common.utility;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.board.enums.SquareType;
import com.dlb.chess.common.constants.EnumConstants;

public abstract class MaterialUtility implements EnumConstants {

  public static boolean calculateHasPieceType(Side side, PieceType pieceType, StaticPosition staticPosition) {
    for (final Square boardSquare : Square.BOARD_SQUARE_LIST) {
      final Piece pieceOnSquare = staticPosition.get(boardSquare);
      if (MaterialUtility.calculateIsOwnPiece(side, pieceOnSquare) && pieceOnSquare.getPieceType() == pieceType) {
        return true;
      }
    }
    return false;
  }

  public static boolean calculateHasPieceType(Side side, PieceType pieceType, StaticPosition staticPosition,
      File file) {
    for (final Square boardSquare : Square.BOARD_SQUARE_LIST) {
      if (boardSquare.getFile() != file) {
        continue;
      }
      final Piece pieceOnSquare = staticPosition.get(boardSquare);
      if (MaterialUtility.calculateIsOwnPiece(side, pieceOnSquare) && pieceOnSquare.getPieceType() == pieceType) {
        return true;
      }
    }
    return false;
  }

  public static boolean calculateHasPieceType(Side side, PieceType pieceType, StaticPosition staticPosition,
      Rank rank) {
    for (final Square boardSquare : Square.BOARD_SQUARE_LIST) {
      if (boardSquare.getRank() != rank) {
        continue;
      }
      final Piece pieceOnSquare = staticPosition.get(boardSquare);
      if (MaterialUtility.calculateIsOwnPiece(side, pieceOnSquare) && pieceOnSquare.getPieceType() == pieceType) {
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

  public static boolean calculateHasRook(Side side, StaticPosition staticPosition) {
    return calculateHasPieceType(side, PieceType.ROOK, staticPosition);
  }

  public static boolean calculateHasKnight(StaticPosition staticPosition) {
    return calculateHasPieceType(PieceType.KNIGHT, staticPosition);
  }

  public static boolean calculateHasKnight(Side side, StaticPosition staticPosition) {
    return calculateHasPieceType(side, PieceType.KNIGHT, staticPosition);
  }

  public static boolean calculateHasBishop(StaticPosition staticPosition) {
    return calculateHasPieceType(PieceType.BISHOP, staticPosition);
  }

  public static boolean calculateHasBishop(Side side, StaticPosition staticPosition) {
    return calculateHasPieceType(side, PieceType.BISHOP, staticPosition);
  }

  public static boolean calculateHasQueen(StaticPosition staticPosition) {
    return calculateHasPieceType(PieceType.QUEEN, staticPosition);
  }

  public static boolean calculateHasQueen(Side side, StaticPosition staticPosition) {
    return calculateHasPieceType(side, PieceType.QUEEN, staticPosition);
  }

  public static boolean calculateHasPawn(StaticPosition staticPosition) {
    return calculateHasPieceType(PieceType.PAWN, staticPosition);
  }

  public static boolean calculateHasPawn(Side side, StaticPosition staticPosition) {
    return calculateHasPieceType(side, PieceType.PAWN, staticPosition);
  }

  public static boolean calculateHasKingAndKnightAndBishop(Side side, StaticPosition staticPosition) {
    return calculateNumberOfPieces(side, staticPosition, KNIGHT) >= 1
        && calculateNumberOfPieces(side, staticPosition, BISHOP) >= 1
        && calculateNumberOfPieces(side, staticPosition, KING) == 1;
  }

  public static boolean calculateHasKingAndOppositeSquaresBishop(Side side, StaticPosition staticPosition) {
    return calculateNumberOfBishops(side, staticPosition, SquareType.LIGHT_SQUARE) >= 1
        && calculateNumberOfBishops(side, staticPosition, SquareType.DARK_SQUARE) >= 1
        && calculateNumberOfPieces(side, staticPosition, KING) == 1;
  }

  public static boolean calculateIsOwnPiece(Side side, Piece pieceOnSquare) {
    return pieceOnSquare != Piece.NONE && pieceOnSquare.getSide() == side;
  }

  public static boolean calculateIsOwnPieceType(Side side, Piece pieceOnSquare, PieceType pieceType) {
    return calculateIsOwnPiece(side, pieceOnSquare) && pieceOnSquare.getPieceType() == pieceType;
  }

  public static boolean calculateHasKingOnly(Side side, StaticPosition staticPosition) {
    var countKing = 0;
    for (final Square boardSquare : Square.BOARD_SQUARE_LIST) {
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

  public static boolean calculateHasKingAndBishopsOnly(Side side, StaticPosition staticPosition,
      SquareType squareType) {
    return calculateNumberOfPieces(side, staticPosition, ROOK) == 0
        && calculateNumberOfPieces(side, staticPosition, KNIGHT) == 0
        && calculateNumberOfBishops(side, staticPosition, squareType) >= 1
        && calculateNumberOfBishops(side, staticPosition, squareType.getOppositeSquareType()) == 0
        && calculateNumberOfPieces(side, staticPosition, QUEEN) == 0
        && calculateNumberOfPieces(side, staticPosition, KING) == 1
        && calculateNumberOfPieces(side, staticPosition, PAWN) == 0;
  }

  public static boolean calculateHasKingAndPawnsOrQueensOnly(Side side, StaticPosition staticPosition) {
    return calculateNumberOfPieces(side, staticPosition, ROOK) == 0
        && calculateNumberOfPieces(side, staticPosition, KNIGHT) == 0
        && calculateNumberOfPieces(side, staticPosition, BISHOP) == 0
        && calculateNumberOfPieces(side, staticPosition, KING) == 1
        && (calculateNumberOfPieces(side, staticPosition, PAWN) >= 1
            || calculateNumberOfPieces(side, staticPosition, QUEEN) >= 1);
  }

  public static int calculateNumberOfRooks(Side side, StaticPosition staticPosition) {
    return calculateNumberOfPieces(side, staticPosition, ROOK);
  }

  public static int calculateNumberOfKnights(Side side, StaticPosition staticPosition) {
    return calculateNumberOfPieces(side, staticPosition, KNIGHT);
  }

  public static int calculateNumberOfBishops(Side side, StaticPosition staticPosition) {
    return calculateNumberOfPieces(side, staticPosition, BISHOP);
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

  public static int calculateNumberOfQueens(Side side, StaticPosition staticPosition) {
    return calculateNumberOfPieces(side, staticPosition, QUEEN);
  }

  public static int calculateNumberOfPawns(Side side, StaticPosition staticPosition) {
    return calculateNumberOfPieces(side, staticPosition, PAWN);
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

  public static boolean calculateHasNoRooks(Side side, StaticPosition staticPosition) {
    return calculateNumberOfPieces(side, staticPosition, PieceType.ROOK) == 0;
  }

  public static boolean calculateHasNoKnights(Side side, StaticPosition staticPosition) {
    return calculateNumberOfPieces(side, staticPosition, PieceType.KNIGHT) == 0;
  }

  public static boolean calculateHasNoBishops(Side side, StaticPosition staticPosition) {
    return calculateNumberOfPieces(side, staticPosition, PieceType.BISHOP) == 0;
  }

  public static boolean calculateHasNoBishops(Side side, StaticPosition staticPosition, SquareType squareType) {
    return calculateNumberOfBishops(side, staticPosition, squareType) == 0;
  }

  public static boolean calculateHasLightSquareBishops(Side side, StaticPosition staticPosition) {
    return calculateNumberOfBishops(side, staticPosition, SquareType.LIGHT_SQUARE) > 0;
  }

  public static boolean calculateHasNoLightSquareBishops(Side side, StaticPosition staticPosition) {
    return !calculateHasLightSquareBishops(side, staticPosition);
  }

  public static boolean calculateHasDarkSquareBishops(Side side, StaticPosition staticPosition) {
    return calculateNumberOfBishops(side, staticPosition, SquareType.DARK_SQUARE) > 0;
  }

  public static boolean calculateHasNoDarkSquareBishops(Side side, StaticPosition staticPosition) {
    return !calculateHasDarkSquareBishops(side, staticPosition);
  }

  public static boolean calculateHasNoQueens(Side side, StaticPosition staticPosition) {
    return calculateNumberOfPieces(side, staticPosition, PieceType.QUEEN) == 0;
  }

  public static boolean calculateHasNoPawns(Side side, StaticPosition staticPosition) {
    return calculateNumberOfPieces(side, staticPosition, PieceType.PAWN) == 0;
  }

  public static boolean calculateHasKingAndRookOnly(Side side, StaticPosition staticPosition) {
    return calculateHasKingAndAnotherPieceOnly(side, ROOK, staticPosition);
  }

  public static boolean calculateHasKingAndKnightOnly(Side side, StaticPosition staticPosition) {
    return calculateHasKingAndAnotherPieceOnly(side, KNIGHT, staticPosition);
  }

  public static boolean calculateHasKingAndBishopOnly(Side side, StaticPosition staticPosition) {
    return calculateHasKingAndAnotherPieceOnly(side, BISHOP, staticPosition);
  }

  public static boolean calculateHasKingAndQueenOnly(Side side, StaticPosition staticPosition) {
    return calculateHasKingAndAnotherPieceOnly(side, QUEEN, staticPosition);
  }

  public static boolean calculateHasKingAndKnightAndBishopOnly(Side side, StaticPosition staticPosition) {
    return calculateNumberOfPieces(side, staticPosition, ROOK) == 0
        && calculateNumberOfPieces(side, staticPosition, KNIGHT) == 1
        && calculateNumberOfPieces(side, staticPosition, BISHOP) == 1
        && calculateNumberOfPieces(side, staticPosition, QUEEN) == 0
        && calculateNumberOfPieces(side, staticPosition, KING) == 1
        && calculateNumberOfPieces(side, staticPosition, PAWN) == 0;
  }

  public static boolean calculateHasKingAndOppositeSquaresBishopOnly(Side side, StaticPosition staticPosition) {
    return calculateNumberOfPieces(side, staticPosition, ROOK) == 0
        && calculateNumberOfPieces(side, staticPosition, KNIGHT) == 0
        && calculateNumberOfBishops(side, staticPosition, SquareType.LIGHT_SQUARE) == 1
        && calculateNumberOfBishops(side, staticPosition, SquareType.DARK_SQUARE) == 1
        && calculateNumberOfPieces(side, staticPosition, QUEEN) == 0
        && calculateNumberOfPieces(side, staticPosition, KING) == 1
        && calculateNumberOfPieces(side, staticPosition, PAWN) == 0;
  }

  private static boolean calculateHasKingAndAnotherPieceOnly(Side side, PieceType anotherPieceType,
      StaticPosition staticPosition) {

    var countKing = 0;
    var countAnotherPieces = 0;
    for (final Square boardSquare : Square.BOARD_SQUARE_LIST) {
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
