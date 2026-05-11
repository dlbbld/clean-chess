package com.dlb.chess.model;

import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.san.SanTerminalMarker;

public record SanConversion(PieceType movingPieceType, File fromFile, Rank fromRank, Square toSquare,
    PromotionPieceType promotionPieceType, SanTerminalMarker sanTerminalMarker) {

  public static final SanConversion EMPTY = new SanConversion(PieceType.NONE, File.NONE, Rank.NONE, Square.NONE,
      PromotionPieceType.NONE, SanTerminalMarker.NONE);

}
