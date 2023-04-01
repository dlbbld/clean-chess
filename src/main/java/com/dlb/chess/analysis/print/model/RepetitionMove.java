package com.dlb.chess.analysis.print.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.common.model.HalfMove;

@SuppressWarnings("null")
public record RepetitionMove(int positionId, int fold, @NonNull HalfMove halfMove)
    implements Comparable<RepetitionMove> {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (RepetitionMove) obj;
    return fold == other.fold && Objects.equals(halfMove, other.halfMove) && positionId == other.positionId;
  }

  @Override
  public int compareTo(RepetitionMove o) {
    return Integer.compare(this.halfMove.halfMoveCount(), o.halfMove.halfMoveCount());
  }

}
