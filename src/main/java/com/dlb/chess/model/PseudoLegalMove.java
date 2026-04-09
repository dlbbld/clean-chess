package com.dlb.chess.model;

import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.model.MoveSpecification;

public record PseudoLegalMove(MoveSpecification moveSpecification, Piece movingPiece, Piece pieceCaptured)
    implements Comparable<PseudoLegalMove>, EnumConstants {

  @Override
  public int compareTo(PseudoLegalMove pseudoLegalMove) {
    return this.moveSpecification().compareTo(pseudoLegalMove.moveSpecification());
  }

}
