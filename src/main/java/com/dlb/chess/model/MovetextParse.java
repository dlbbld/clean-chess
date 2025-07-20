package com.dlb.chess.model;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

public record MovetextParse(boolean hasMovesAndIsLastMoveBlack, @NonNull List<PgnHalfMove> whiteHalfMoveList,
    @NonNull List<PgnHalfMove> blackHalfMoveList) {
}
