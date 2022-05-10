package com.dlb.chess.analysis.model;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.Side;

public record YawnHalfMove(int performedHalfMoveCount, int fullMoveNumber, String san, Side sideMoved,
    int sequenceLength) implements Comparable<YawnHalfMove> {

  public int performedHalfMoveCount() {
    return performedHalfMoveCount;
  }

  public int fullMoveNumber() {
    return fullMoveNumber;
  }

  public String san() {
    return san;
  }

  public Side sideMoved() {
    return sideMoved;
  }

  public int sequenceLength() {
    return sequenceLength;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (YawnHalfMove) obj;
    if (fullMoveNumber != other.fullMoveNumber || performedHalfMoveCount != other.performedHalfMoveCount
        || !san.equals(other.san) || sequenceLength != other.sequenceLength || sideMoved != other.sideMoved) {
      return false;
    }

    return true;
  }

  @Override
  public int compareTo(YawnHalfMove o) {
    return Integer.compare(this.performedHalfMoveCount, o.performedHalfMoveCount);
  }

}
