package com.dlb.chess.model;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.san.enums.CheckmateOrCheck;

public record SanConversion(File fromFile, Rank fromRank, Square toSquare, PromotionPieceType promotionPieceType,
    CheckmateOrCheck checkmateOrCheck) {

  public static final SanConversion EMPTY = new SanConversion(File.NONE, Rank.NONE, Square.NONE,
      PromotionPieceType.NONE, CheckmateOrCheck.NONE);

  public File fromFile() {
    return fromFile;
  }

  public Rank fromRank() {
    return fromRank;
  }

  public Square toSquare() {
    return toSquare;
  }

  public PromotionPieceType promotionPieceType() {
    return promotionPieceType;
  }

  public CheckmateOrCheck checkmateOrCheck() {
    return checkmateOrCheck;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (SanConversion) obj;
    return fromFile == other.fromFile && fromRank == other.fromRank && promotionPieceType == other.promotionPieceType
        && checkmateOrCheck == other.checkmateOrCheck && toSquare == other.toSquare;
  }

}
