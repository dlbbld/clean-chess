package com.dlb.chess.analysis.model;

import com.dlb.chess.board.enums.Side;

public record YawnHalfMove(int performedHalfMoveCount, int fullMoveNumber, String san, Side sideMoved,
    int sequenceLength) implements Comparable<YawnHalfMove> {

  @Override
  public int compareTo(YawnHalfMove o) {
    return Integer.compare(this.performedHalfMoveCount, o.performedHalfMoveCount);
  }

}
