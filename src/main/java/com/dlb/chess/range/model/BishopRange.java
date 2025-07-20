package com.dlb.chess.range.model;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.board.enums.Square;
import com.dlb.chess.range.DiagonalRange;
import com.google.common.collect.ImmutableList;

public record BishopRange(@NonNull ImmutableList<Square> squareListNorthEast,
    @NonNull ImmutableList<Square> squareListSouthEast, @NonNull ImmutableList<Square> squareListSouthWest,
    @NonNull ImmutableList<Square> squareListNorthWest) implements DiagonalRange {

}