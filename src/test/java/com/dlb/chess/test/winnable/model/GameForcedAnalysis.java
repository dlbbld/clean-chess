package com.dlb.chess.test.winnable.model;

import com.dlb.chess.common.enums.GameStatusAnalysis;

public record GameForcedAnalysis(GameStatusAnalysis gameStatus, int numberOfForcedHalfMoves) {
}
