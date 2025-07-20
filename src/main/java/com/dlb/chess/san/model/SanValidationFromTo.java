package com.dlb.chess.san.model;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Square;

public record SanValidationFromTo(@NonNull File fromFile, @NonNull Rank fromRank, @NonNull Square toSquare) {
}
