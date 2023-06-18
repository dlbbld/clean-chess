package com.dlb.chess.analysis.model;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.board.enums.Side;

@SuppressWarnings("null")
public record YawnHalfMove(int performedHalfMoveCount, int fullMoveNumber, @NonNull String san, @NonNull Side sideMoved,
    int sequenceLength) implements Comparable<YawnHalfMove> {

  @Override
  public int compareTo(YawnHalfMove o) {
    return Integer.compare(this.performedHalfMoveCount, o.performedHalfMoveCount);
  }

}
