package com.dlb.chess.unwinnability.findhelpmate.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.StaticPosition;

public record TranspositionTable(StaticPosition staticPosition, int d) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }
    final var other = (TranspositionTable) obj;
    return d == other.d && Objects.equals(staticPosition, other.staticPosition);
  }

}
