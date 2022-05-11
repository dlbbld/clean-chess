package com.dlb.chess.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.moves.utility.CastlingUtility;

public record LegalMove(MoveSpecification moveSpecification, Piece movingPiece, Piece pieceCaptured)
    implements Comparable<LegalMove>, EnumConstants {

  public LegalMove(MoveSpecification moveSpecification) {
    this(moveSpecification, Piece.NONE, Piece.NONE);
  }

  public MoveSpecification moveSpecification() {
    return moveSpecification;
  }

  public Piece movingPiece() {
    return movingPiece;
  }

  public Piece pieceCaptured() {
    return pieceCaptured;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (LegalMove) obj;
    return Objects.equals(moveSpecification, other.moveSpecification) && movingPiece == other.movingPiece
        && pieceCaptured == other.pieceCaptured;
  }

  @Override
  public int compareTo(LegalMove legalMove) {
    if (!CastlingUtility.calculateIsCastlingMove(this.moveSpecification)
        && !CastlingUtility.calculateIsCastlingMove(legalMove.moveSpecification)) {
      if (this.movingPiece().getPieceType() == PAWN && legalMove.movingPiece().getPieceType() != PAWN) {
        return -1;
      }
      if (this.movingPiece().getPieceType() != PAWN && legalMove.movingPiece().getPieceType() == PAWN) {
        return 1;
      }
      if (this.pieceCaptured() != Piece.NONE && legalMove.pieceCaptured() == Piece.NONE
          || this.pieceCaptured() == Piece.NONE && legalMove.pieceCaptured() != Piece.NONE) {
        return -1;
      }
      if (this.pieceCaptured() == Piece.NONE && legalMove.pieceCaptured() != Piece.NONE
          || this.pieceCaptured() == Piece.NONE && legalMove.pieceCaptured() != Piece.NONE) {
        return 1;
      }
    }
    return this.moveSpecification().compareTo(legalMove.moveSpecification());
  }

}
