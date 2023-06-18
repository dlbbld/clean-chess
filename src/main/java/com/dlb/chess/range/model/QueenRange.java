package com.dlb.chess.range.model;

import org.eclipse.jdt.annotation.NonNull;

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

}
