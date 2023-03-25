package com.dlb.chess.analysis.model;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.Side;

@SuppressWarnings("null")
public record YawnHalfMove(int performedHalfMoveCount, int fullMoveNumber, @NonNull String san, @NonNull Side sideMoved,
    int sequenceLength) implements Comparable<YawnHalfMove> {

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
