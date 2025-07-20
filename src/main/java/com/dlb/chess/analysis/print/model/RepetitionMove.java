package com.dlb.chess.analysis.print.model;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.common.model.HalfMove;

public record RepetitionMove(int positionId, int fold, @NonNull HalfMove halfMove)
    implements Comparable<RepetitionMove> {

  @Override
  public int compareTo(RepetitionMove o) {
    return Integer.compare(this.halfMove.halfMoveCount(), o.halfMove.halfMoveCount());
  }

}
