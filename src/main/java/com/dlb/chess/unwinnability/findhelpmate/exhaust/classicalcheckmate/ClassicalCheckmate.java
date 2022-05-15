package com.dlb.chess.unwinnability.findhelpmate.exhaust.classicalcheckmate;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.utility.MaterialUtility;
import com.dlb.chess.unwinnability.findhelpmate.exhaust.classicalcheckmate.enums.ClassicalCheckmateType;

public class ClassicalCheckmate {

  public static boolean isClassicalCheckmateMaterialMatingSide(Side sideCheckmating, StaticPosition staticPosition) {
    return MaterialUtility.calculateHasKingAndRookOnly(sideCheckmating, staticPosition)
        || MaterialUtility.calculateHasKingAndKnightAndBishopOnly(sideCheckmating, staticPosition)
        || MaterialUtility.calculateHasKingAndOppositeSquaresBishopOnly(sideCheckmating, staticPosition)
        || MaterialUtility.calculateHasKingAndQueenOnly(sideCheckmating, staticPosition);
  }

  public static ClassicalCheckmateType calculateClassicalCheckmateMaterialMatingSide(Side sideCheckmating,
      StaticPosition staticPosition) {
    // return in order preferred for the algorithm, not listing existence
    if (MaterialUtility.calculateHasQueen(sideCheckmating, staticPosition)) {
      return ClassicalCheckmateType.KING_AND_QUEEN;
    }
    if (MaterialUtility.calculateHasRook(sideCheckmating, staticPosition)) {
      return ClassicalCheckmateType.KING_AND_ROOK;
    }
    if (MaterialUtility.calculateHasKingAndOppositeSquaresBishopOnly(sideCheckmating, staticPosition)) {
      return ClassicalCheckmateType.KING_AND_OPPOSITE_SQUARES_BISHOP;
    }
    if (MaterialUtility.calculateHasKingAndKnightAndBishopOnly(sideCheckmating, staticPosition)) {
      return ClassicalCheckmateType.KING_AND_KNIGHT_AND_BISHOP;
    }
    return ClassicalCheckmateType.NONE;
  }

  public static boolean isClassicalCheckmateMaterial(Side sideCheckmating, StaticPosition staticPosition) {
    return isClassicalCheckmateMaterialMatingSide(sideCheckmating, staticPosition)
        && MaterialUtility.calculateHasKingOnly(sideCheckmating.getOppositeSide(), staticPosition);
  }

}
