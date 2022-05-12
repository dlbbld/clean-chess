package com.dlb.chess.winnable.enums;

public enum Winnable {

  NO("no"),
  YES("no"),
  NO_AMBRONA("no ambrona"),
  YES_AMBRONA("yes ambrona"),
  UNKNOWN("unknown");

  private final String description;

  Winnable(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
