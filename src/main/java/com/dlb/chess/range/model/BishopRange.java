package com.dlb.chess.range.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.Square;
import com.dlb.chess.range.DiagonalRange;
import com.google.common.collect.ImmutableList;

public record BishopRange(ImmutableList<Square> squareListNorthEast, ImmutableList<Square> squareListSouthEast,
    ImmutableList<Square> squareListSouthWest, ImmutableList<Square> squareListNorthWest) implements DiagonalRange {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (BishopRange) obj;
    return Objects.equals(squareListNorthEast, other.squareListNorthEast)
        && Objects.equals(squareListNorthWest, other.squareListNorthWest)
        && Objects.equals(squareListSouthEast, other.squareListSouthEast)
        && Objects.equals(squareListSouthWest, other.squareListSouthWest);
  }

}