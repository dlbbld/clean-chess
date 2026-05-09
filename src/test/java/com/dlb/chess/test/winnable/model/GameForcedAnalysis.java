package com.dlb.chess.test.winnable.model;

import com.dlb.chess.test.winnable.enums.GameStatusAnalysis;

public record GameForcedAnalysis(GameStatusAnalysis gameStatus, int numberOfForcedHalfMoves) {
}
