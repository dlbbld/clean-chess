package com.dlb.chess.model;

import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.model.MoveSpecification;

public record LegalMove(MoveSpecification moveSpecification, Piece movingPiece, Piece pieceCaptured, LegalMoveKind kind)
    implements Comparable<LegalMove>, EnumConstants {

  public LegalMove {
    if (movingPiece == Piece.NONE) {
      throw new IllegalArgumentException("The moving piece cannot be the none piece");
    }
  }

  public Side havingMove() {
    return movingPiece.getSide();
  }

  @Override
  public int compareTo(LegalMove legalMove) {
    return this.moveSpecification().compareTo(legalMove.moveSpecification());
  }

}
