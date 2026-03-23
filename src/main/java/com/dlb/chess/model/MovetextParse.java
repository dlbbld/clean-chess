package com.dlb.chess.model;

import java.util.List;

public record MovetextParse(boolean hasMovesAndIsLastMoveBlack, List<PgnHalfMove> whiteHalfMoveList,
    List<PgnHalfMove> blackHalfMoveList) {
}
