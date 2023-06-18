package com.dlb.chess.common.model;

import org.eclipse.jdt.annotation.NonNull;

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

}
