package com.dlb.chess.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.model.MoveSpecification;

@SuppressWarnings("null")
public record LegalMove(@NonNull MoveSpecification moveSpecification, @NonNull Piece movingPiece,
    @NonNull Piece pieceCaptured) implements Comparable<LegalMove>, EnumConstants {

  public LegalMove(MoveSpecification moveSpecification) {
    this(moveSpecification, Piece.NONE, Piece.NONE);
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
    return this.moveSpecification().compareTo(legalMove.moveSpecification());
  }

}
