package com.dlb.chess.report.model;

import com.dlb.chess.board.enums.Side;

public record NoProgressHalfMove(int performedHalfMoveCount, int fullMoveNumber, String san, Side sideMoved,
    int sequenceLength) implements Comparable<NoProgressHalfMove> {

  @Override
  public int compareTo(NoProgressHalfMove o) {
    return Integer.compare(this.performedHalfMoveCount, o.performedHalfMoveCount);
  }

}
