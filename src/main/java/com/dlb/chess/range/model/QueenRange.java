package com.dlb.chess.range.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.Square;
import com.dlb.chess.range.DiagonalRange;
import com.dlb.chess.range.OrthogonalRange;
import com.google.common.collect.ImmutableList;

@SuppressWarnings("null")
public record QueenRange(@NonNull ImmutableList<Square> squareListNorth, @NonNull ImmutableList<Square> squareListEast,
    @NonNull ImmutableList<Square> squareListSouth, @NonNull ImmutableList<Square> squareListWest,
    @NonNull ImmutableList<Square> squareListNorthEast, @NonNull ImmutableList<Square> squareListSouthEast,
    @NonNull ImmutableList<Square> squareListSouthWest, @NonNull ImmutableList<Square> squareListNorthWest)
    implements DiagonalRange, OrthogonalRange {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (QueenRange) obj;
    return Objects.equals(squareListEast, other.squareListEast)
        && Objects.equals(squareListNorth, other.squareListNorth)
        && Objects.equals(squareListNorthEast, other.squareListNorthEast)
        && Objects.equals(squareListNorthWest, other.squareListNorthWest)
        && Objects.equals(squareListSouth, other.squareListSouth)
        && Objects.equals(squareListSouthEast, other.squareListSouthEast)
        && Objects.equals(squareListSouthWest, other.squareListSouthWest)
        && Objects.equals(squareListWest, other.squareListWest);
  }

}
