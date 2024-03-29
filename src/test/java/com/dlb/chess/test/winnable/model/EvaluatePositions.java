package com.dlb.chess.test.winnable.model;

import java.util.Set;

import com.dlb.chess.common.enums.GameStatus;

public record EvaluatePositions(Set<GameStatus> gameStatus, int evaluatedPositions) {

  public Set<GameStatus> gameStatus() {
    return gameStatus;
  }

  public int evaluatedPositions() {
    return evaluatedPositions;
  }

}
