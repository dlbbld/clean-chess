package com.dlb.chess.range;

import com.dlb.chess.board.enums.Square;
import com.google.common.collect.ImmutableList;

public interface OrthogonalRange {

  ImmutableList<Square> squareListNorth();

  ImmutableList<Square> squareListEast();

  ImmutableList<Square> squareListSouth();

  ImmutableList<Square> squareListWest();
}
