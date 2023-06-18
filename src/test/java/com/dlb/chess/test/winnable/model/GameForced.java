package com.dlb.chess.test.winnable.model;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.enums.GameStatus;

public record GameForced(GameStatus gameStatus, int evaluatedPositions, Side sideMadeLastMove) {
}
