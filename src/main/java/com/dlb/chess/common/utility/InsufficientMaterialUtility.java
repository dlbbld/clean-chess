package com.dlb.chess.common.utility;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.board.enums.SquareType;
import com.dlb.chess.common.constants.EnumConstants;

public class InsufficientMaterialUtility implements EnumConstants {

  public static boolean calculateIsInsufficientMaterial(Side side, StaticPosition staticPosition) {
    final Side oppositeSide = side.getOppositeSide();

    if (MaterialUtility.calculateIsKingOnly(side, staticPosition)) {
      return true;
    }
    if (MaterialUtility.calculateIsKingAndKnightOnly(side, staticPosition)) {
      return calculateHasZeroOrMultipleQueenOnly(oppositeSide, staticPosition);
    }
    if (calculateHasZeroOrMultipleLightSquareBishopOnly(side, staticPosition)) {
      return calculateHasNoPawnAndNoKnightAndNoDarkSquareBishop(oppositeSide, staticPosition);
    }
    if (calculateHasZeroOrMultipleDarkSquareBishopOnly(side, staticPosition)) {
      return calculateHasNoPawnAndNoKnightAndNoLightSquareBishop(oppositeSide, staticPosition);
    }

    return false;
  }

  private static boolean calculateHasZeroOrMultipleSquareBishopOnlyForSpecifiedColor(Side side,
      StaticPosition staticPosition, SquareType squareType) {
    for (final Square boardSquare : Square.BOARD_SQUARE_LIST) {
      final Piece pieceOnSquare = staticPosition.get(boardSquare);
      if (MaterialUtility.calculateIsOwnPieceButNotKing(side, pieceOnSquare)) {
        if (pieceOnSquare.getPieceType() == BISHOP && boardSquare.getSquareType() == squareType) {
          continue;
        }
        return false;
      }
    }
    return true;
  }

  public static boolean calculateHasZeroOrMultipleLightSquareBishopOnly(Side side, StaticPosition staticPosition) {
    return calculateHasZeroOrMultipleSquareBishopOnlyForSpecifiedColor(side, staticPosition, SquareType.LIGHT_SQUARE);
  }

  public static boolean calculateHasZeroOrMultipleDarkSquareBishopOnly(Side side, StaticPosition staticPosition) {
    return calculateHasZeroOrMultipleSquareBishopOnlyForSpecifiedColor(side, staticPosition, SquareType.DARK_SQUARE);
  }

  private static boolean calculateHasZeroOrMultipleQueenOnly(Side side, StaticPosition staticPosition) {
    for (final Square boardSquare : Square.BOARD_SQUARE_LIST) {
      final Piece pieceOnSquare = staticPosition.get(boardSquare);
      if (MaterialUtility.calculateIsOwnPieceButNotKing(side, pieceOnSquare)) {
        if (pieceOnSquare.getPieceType() == QUEEN) {
          continue;
        }
        return false;
      }
    }
    return true;
  }

  private static boolean calculateHasNoPawnAndNoKnightAndNoLightSquareBishop(Side side, StaticPosition staticPosition) {
    return !calculateHasPawn(side, staticPosition) && !calculateHasKnight(side, staticPosition)
        && !calculateHasBishopForSpecifiedColor(side, SquareType.LIGHT_SQUARE, staticPosition);
  }

  private static boolean calculateHasNoPawnAndNoKnightAndNoDarkSquareBishop(Side side, StaticPosition staticPosition) {
    return !calculateHasPawn(side, staticPosition) && !calculateHasKnight(side, staticPosition)
        && !calculateHasBishopForSpecifiedColor(side, SquareType.DARK_SQUARE, staticPosition);
  }

  private static boolean calculateHasPawn(Side side, StaticPosition staticPosition) {
    return MaterialUtility.calculateHasPieceType(side, PAWN, staticPosition);
  }

  private static boolean calculateHasKnight(Side side, StaticPosition staticPosition) {
    return MaterialUtility.calculateHasPieceType(side, KNIGHT, staticPosition);
  }

  private static boolean calculateHasBishopForSpecifiedColor(Side side, SquareType squareType,
      StaticPosition staticPosition) {
    for (final Square boardSquare : Square.BOARD_SQUARE_LIST) {
      final Piece pieceOnSquare = staticPosition.get(boardSquare);
      if (MaterialUtility.calculateIsOwnPieceButNotKing(side, pieceOnSquare) && pieceOnSquare.getPieceType() == BISHOP
          && boardSquare.getSquareType() == squareType) {
        return true;
      }
    }
    return false;
  }

}
