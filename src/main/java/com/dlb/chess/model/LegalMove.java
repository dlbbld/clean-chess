package com.dlb.chess.model;

import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.model.MoveSpecification;

public record LegalMove(MoveSpecification moveSpecification, Piece movingPiece, Piece pieceCaptured)
    implements Comparable<LegalMove>, EnumConstants {

  public LegalMove(MoveSpecification moveSpecification) {
    this(moveSpecification, Piece.NONE, Piece.NONE);
  }

  @Override
  public int compareTo(LegalMove legalMove) {
    return this.moveSpecification().compareTo(legalMove.moveSpecification());
  }

}
