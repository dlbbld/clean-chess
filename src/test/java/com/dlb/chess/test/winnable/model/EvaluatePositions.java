package com.dlb.chess.test.winnable.model;

import java.util.Objects;
import java.util.Set;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.common.enums.GameStatus;

public record EvaluatePositions(Set<GameStatus> gameStatus, int evaluatedPositions) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (EvaluatePositions) obj;
    return evaluatedPositions == other.evaluatedPositions && Objects.equals(gameStatus, other.gameStatus);
  }

  public Set<GameStatus> gameStatus() {
    return gameStatus;
  }

  public int evaluatedPositions() {
    return evaluatedPositions;
  }

}
