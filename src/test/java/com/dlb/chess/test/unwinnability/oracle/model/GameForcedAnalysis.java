package com.dlb.chess.test.unwinnability.oracle.model;

import com.dlb.chess.test.unwinnability.oracle.enums.GameStatusAnalysis;

public record GameForcedAnalysis(GameStatusAnalysis gameStatus, int numberOfForcedHalfMoves) {
}
