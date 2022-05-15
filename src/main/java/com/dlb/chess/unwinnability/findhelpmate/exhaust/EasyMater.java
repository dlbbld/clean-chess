package com.dlb.chess.unwinnability.findhelpmate.exhaust;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.utility.MaterialUtility;

public class EasyMater {

  public static boolean isClassicalCheckmatePositionMatingSide(StaticPosition staticPosition, Side sideCheckmating) {
    return MaterialUtility.calculateIsKingAndRookOnly(sideCheckmating, staticPosition)
        || MaterialUtility.calculateIsKingAndKnightAndBishopOnly(sideCheckmating, staticPosition)
        || MaterialUtility.calculateIsKingAndOppositeSquaresBishopOnly(sideCheckmating, staticPosition)
        || MaterialUtility.calculateIsKingAndQueenOnly(sideCheckmating, staticPosition);
  }

  public static boolean isClassicalCheckmatePosition(StaticPosition staticPosition, Side sideCheckmating) {
    return isClassicalCheckmatePositionMatingSide(staticPosition, sideCheckmating)
        && MaterialUtility.calculateIsKingOnly(sideCheckmating.getOppositeSide(), staticPosition);
  }

}
