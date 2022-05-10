package com.dlb.chess.unwinnability.findhelpmate.enums;

public enum ScoreResult {

  NORMAL(0),
  REWARD(1),
  PUNISH(-2);

  private final int changeIncrement;

  ScoreResult(int changeIncrement) {
    this.changeIncrement = changeIncrement;
  }

  public int getChangeIncrement() {
    return changeIncrement;
  }

}
