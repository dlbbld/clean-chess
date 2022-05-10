package com.dlb.chess.analysis.model;

import org.eclipse.jdt.annotation.Nullable;

public record YawnIndex(int beginPerformedIndex, int endPerformedIndex, int halfMoveClockEnd) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (YawnIndex) obj;
    if (beginPerformedIndex != other.beginPerformedIndex) {
      return false;
    }
    return endPerformedIndex == other.endPerformedIndex;
  }

}
