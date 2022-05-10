package com.dlb.chess.range;

import com.dlb.chess.board.enums.Square;
import com.google.common.collect.ImmutableList;

public interface DiagonalRange {
  ImmutableList<Square> squareListNorthEast();

  ImmutableList<Square> squareListSouthEast();

  ImmutableList<Square> squareListSouthWest();

  ImmutableList<Square> squareListNorthWest();
}
