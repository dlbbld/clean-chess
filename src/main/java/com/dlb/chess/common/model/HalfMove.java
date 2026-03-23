package com.dlb.chess.common.model;

import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.common.utility.HalfMoveUtility;

// only use half moves in lists, and then add them in order of play.
// not used in sets per this design, so we don't need sorting.
public record HalfMove(int index, int halfMoveCount, int fullMoveNumber, int halfMoveClock, boolean isCapture,
    String fen, DynamicPosition dynamicPosition, int countRepetition, int countRepetitionIgnoringEnPassantCapture,
    String san, Piece movingPiece, MoveSpecification moveSpecification) {

  @Override
  public String toString() {
    return HalfMoveUtility.calculateMoveNumberAndSanWithoutSpace(this);
  }

}
