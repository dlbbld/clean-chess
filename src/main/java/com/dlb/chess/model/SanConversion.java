package com.dlb.chess.model;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.san.enums.CheckmateOrCheck;

@SuppressWarnings("null")
public record SanConversion(@NonNull File fromFile, @NonNull Rank fromRank, @NonNull Square toSquare,
    @NonNull PromotionPieceType promotionPieceType, @NonNull CheckmateOrCheck checkmateOrCheck) {

  public static final SanConversion EMPTY = new SanConversion(File.NONE, Rank.NONE, Square.NONE,
      PromotionPieceType.NONE, CheckmateOrCheck.NONE);

}
