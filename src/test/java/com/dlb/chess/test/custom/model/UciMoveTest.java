package com.dlb.chess.test.custom.model;

public record UciMoveTest(String san, String uciMoveStr) {

  public String san() {
    return san;
  }

  public String uciMoveStr() {
    return uciMoveStr;
  }
}
