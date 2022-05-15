package com.dlb.chess.unwinnability.findhelpmate.exhaust;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.utility.MaterialUtility;

public class EasyMater {

  public static boolean isClassicalCheckmatePosition(StaticPosition staticPosition, Side sideCheckmating) {
    return MaterialUtility.calculateIsKingAndRookOnly(sideCheckmating, staticPosition);
  }

}
