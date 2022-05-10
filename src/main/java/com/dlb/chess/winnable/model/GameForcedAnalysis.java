package com.dlb.chess.winnable.model;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.common.enums.GameStatusAnalysis;

public record GameForcedAnalysis(GameStatusAnalysis gameStatus, int numberOfForcedHalfMoves) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (GameForcedAnalysis) obj;
    return gameStatus == other.gameStatus && numberOfForcedHalfMoves == other.numberOfForcedHalfMoves;
  }
}
