package com.dlb.chess.common.utility;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.fen.FenParserRaw;

public abstract class FenUtility {

  public static Side calculateSideHavingMoveForFen(String fen) {
    return BasicChessUtility.calculateSideHavingMoveForSide(FenParserRaw.parseHavingMove(fen));
  }

  public static String createDummyFenForPiecePlacement(String piecePlacement, Side side) {
    final StringBuilder fen = new StringBuilder();
  
    fen.append(piecePlacement);
    fen.append(" ");
    fen.append(side.getFenLetter());
    fen.append(" - - 0 100");
  
    return NonNullWrapperCommon.toString(fen);
  
  }
}
