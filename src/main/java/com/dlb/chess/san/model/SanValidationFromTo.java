package com.dlb.chess.san.model;

import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Square;

public record SanValidationFromTo(File fromFile, Rank fromRank, Square toSquare) {
}
