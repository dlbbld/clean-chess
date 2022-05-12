package com.dlb.chess.winnable.enums;

public enum Winnable {

  NO("no"),
  YES("no"),
  UNKNOWN("unknown");

  private final String description;

  Winnable(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
