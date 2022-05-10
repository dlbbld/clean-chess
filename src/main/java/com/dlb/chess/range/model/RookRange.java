package com.dlb.chess.range.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.Square;
import com.dlb.chess.range.OrthogonalRange;
import com.google.common.collect.ImmutableList;

public record RookRange(ImmutableList<Square> squareListNorth, ImmutableList<Square> squareListEast,
    ImmutableList<Square> squareListSouth, ImmutableList<Square> squareListWest) implements OrthogonalRange {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (RookRange) obj;
    return Objects.equals(squareListEast, other.squareListEast)
        && Objects.equals(squareListNorth, other.squareListNorth)
        && Objects.equals(squareListSouth, other.squareListSouth)
        && Objects.equals(squareListWest, other.squareListWest);
  }

}
