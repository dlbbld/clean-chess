package com.dlb.chess.range.model;

import com.dlb.chess.board.enums.Square;
import com.dlb.chess.range.OrthogonalRange;
import com.google.common.collect.ImmutableList;

public record RookRange(ImmutableList<Square> squareListNorth, ImmutableList<Square> squareListEast,
    ImmutableList<Square> squareListSouth, ImmutableList<Square> squareListWest)
    implements OrthogonalRange {

}
