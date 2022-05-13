package com.dlb.chess.unwinnability.findhelpmate.exhaust.enums;

public enum ScoreResult {

  NORMAL(0),
  REWARD(1),
  PUNISH(-2);

  private final int increment;

  ScoreResult(int increment) {
    this.increment = increment;
  }

  public int getIncrement() {
    return increment;
  }

}
