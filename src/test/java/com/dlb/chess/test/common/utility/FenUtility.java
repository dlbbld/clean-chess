package com.dlb.chess.test.common.utility;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.NonNullWrapperCommon;

public abstract class FenUtility {

  public static String createDummyFenForPiecePlacement(String piecePlacement, Side side) {
    final StringBuilder fen = new StringBuilder();

    fen.append(piecePlacement);
    fen.append(" ");
    fen.append(side.getFenLetter());
    fen.append(" - - 0 100");

    return NonNullWrapperCommon.toString(fen);

  }
}
