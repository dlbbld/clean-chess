package com.dlb.chess.common.constants;

import com.dlb.chess.common.model.DynamicPosition;
import com.dlb.chess.fen.constants.FenConstants;

public class DynamicPositionConstants {

  public static final DynamicPosition INITIAL = new DynamicPosition(FenConstants.FEN_INITIAL.havingMove(),
      FenConstants.FEN_INITIAL.staticPosition(), false, FenConstants.FEN_INITIAL.castlingRightBoth());
}
