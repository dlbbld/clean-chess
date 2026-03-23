package com.dlb.chess.range.model;

import com.dlb.chess.board.enums.Square;
import com.dlb.chess.range.DiagonalRange;
import com.google.common.collect.ImmutableList;

public record BishopRange(ImmutableList<Square> squareListNorthEast,
    ImmutableList<Square> squareListSouthEast, ImmutableList<Square> squareListSouthWest,
    ImmutableList<Square> squareListNorthWest) implements DiagonalRange {

}