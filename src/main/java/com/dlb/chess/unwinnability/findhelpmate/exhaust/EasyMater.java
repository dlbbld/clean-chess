package com.dlb.chess.unwinnability.findhelpmate.exhaust;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.utility.MaterialUtility;

public class EasyMater {

  public static boolean isClassicalCheckmatePositionMatingSide(StaticPosition staticPosition, Side sideCheckmating) {
    return MaterialUtility.calculateHasKingAndRookOnly(sideCheckmating, staticPosition)
        || MaterialUtility.calculateHasKingAndKnightAndBishopOnly(sideCheckmating, staticPosition)
        || MaterialUtility.calculateHasKingAndOppositeSquaresBishopOnly(sideCheckmating, staticPosition)
        || MaterialUtility.calculateHasKingAndQueenOnly(sideCheckmating, staticPosition);
  }

  public static boolean isClassicalCheckmatePosition(StaticPosition staticPosition, Side sideCheckmating) {
    return isClassicalCheckmatePositionMatingSide(staticPosition, sideCheckmating)
        && MaterialUtility.calculateHasKingOnly(sideCheckmating.getOppositeSide(), staticPosition);
  }

}
