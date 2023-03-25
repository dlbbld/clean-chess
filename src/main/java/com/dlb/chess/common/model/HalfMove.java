package com.dlb.chess.common.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.common.utility.HalfMoveUtility;

// only use half moves in lists, and then add them in order of play.
// not used in sets per this design, so we don't need sorting.
@SuppressWarnings("null")
public record HalfMove(int index, int halfMoveCount, int fullMoveNumber, int halfMoveClock, boolean isCapture,
    @NonNull String fen, @NonNull DynamicPosition dynamicPosition, int countRepetition,
    int countRepetitionIgnoringEnPassantCapture, @NonNull String san, @NonNull Piece movingPiece,
    @NonNull MoveSpecification moveSpecification) {

  @Override
  public String toString() {
    return HalfMoveUtility.calculateMoveNumberAndSanWithoutSpace(this);
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (HalfMove) obj;
    return countRepetition == other.countRepetition
        && countRepetitionIgnoringEnPassantCapture == other.countRepetitionIgnoringEnPassantCapture
        && Objects.equals(dynamicPosition, other.dynamicPosition) && Objects.equals(fen, other.fen)
        && halfMoveClock == other.halfMoveClock && halfMoveCount == other.halfMoveCount && index == other.index
        && isCapture == other.isCapture && fullMoveNumber == other.fullMoveNumber
        && Objects.equals(moveSpecification, other.moveSpecification) && movingPiece == other.movingPiece
        && Objects.equals(san, other.san);
  }

}
