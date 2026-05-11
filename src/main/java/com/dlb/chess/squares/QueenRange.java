package com.dlb.chess.squares;

import com.dlb.chess.board.enums.Square;
import com.google.common.collect.ImmutableList;

public record QueenRange(ImmutableList<Square> squareListNorth, ImmutableList<Square> squareListEast,
    ImmutableList<Square> squareListSouth, ImmutableList<Square> squareListWest,
    ImmutableList<Square> squareListNorthEast, ImmutableList<Square> squareListSouthEast,
    ImmutableList<Square> squareListSouthWest, ImmutableList<Square> squareListNorthWest)
    implements DiagonalRange, OrthogonalRange {

}
