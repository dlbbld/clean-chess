package com.dlb.chess.unwinnability.findhelpmate.exhaust.classicalcheckmate;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.utility.MaterialUtility;
import com.dlb.chess.unwinnability.findhelpmate.exhaust.classicalcheckmate.enums.ClassicalCheckmateSituation;

public class ClassicalCheckmate {

  public static boolean isClassicalCheckmatePosition(Side sideCheckmating, StaticPosition staticPosition) {
    return isClassicalCheckmateMaterialMatingSide(sideCheckmating, staticPosition)
        && MaterialUtility.calculateHasKingOnly(sideCheckmating.getOppositeSide(), staticPosition);
  }

  private static boolean isClassicalCheckmateMaterialMatingSide(Side sideCheckmating, StaticPosition staticPosition) {
    return MaterialUtility.calculateHasKingAndRookOnly(sideCheckmating, staticPosition)
        || MaterialUtility.calculateHasKingAndKnightAndBishopOnly(sideCheckmating, staticPosition)
        || MaterialUtility.calculateHasKingAndOppositeSquaresBishopOnly(sideCheckmating, staticPosition)
        || MaterialUtility.calculateHasKingAndQueenOnly(sideCheckmating, staticPosition);
  }

  public static ClassicalCheckmateSituation calculateAboveClassicalCheckmateMaterial(Side sideCheckmating,
      StaticPosition staticPosition) {

    if (isClassicalCheckmatePosition(sideCheckmating, staticPosition)) {
      throw new ProgrammingMistakeException(
          "The method is only designed for the situation when no classical checkmate position is on the board");
    }
    // return in order preferred for the algorithm, not listing existence
    if (MaterialUtility.calculateHasQueen(sideCheckmating, staticPosition)) {
      return ClassicalCheckmateSituation.ABOVE_KING_AND_QUEEN;
    }
    if (MaterialUtility.calculateHasRook(sideCheckmating, staticPosition)) {
      return ClassicalCheckmateSituation.ABOVE_KING_AND_ROOK;
    }
    if (MaterialUtility.calculateHasKingAndOppositeSquaresBishop(sideCheckmating, staticPosition)) {
      return ClassicalCheckmateSituation.ABOVE_KING_AND_OPPOSITE_SQUARES_BISHOP;
    }
    if (MaterialUtility.calculateHasKingAndKnightAndBishop(sideCheckmating, staticPosition)) {
      return ClassicalCheckmateSituation.ABOVE_KING_AND_KNIGHT_AND_BISHOP;
    }
    if (MaterialUtility.calculateHasPawn(sideCheckmating, staticPosition)) {
      return ClassicalCheckmateSituation.NO_HAVING_PAWN;
    }
    return ClassicalCheckmateSituation.NO_NOT_HAVING_PAWN;
  }

}
