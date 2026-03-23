package com.dlb.chess.model;

import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.san.enums.CheckmateOrCheck;

public record SanConversion(File fromFile, Rank fromRank, Square toSquare, PromotionPieceType promotionPieceType,
    CheckmateOrCheck checkmateOrCheck) {

  public static final SanConversion EMPTY = new SanConversion(File.NONE, Rank.NONE, Square.NONE,
      PromotionPieceType.NONE, CheckmateOrCheck.NONE);

}
