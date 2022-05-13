package com.dlb.chess.unwinnability.quick.enums;

public enum UnwinnableQuick {
  YES("unwinnable"),
  NO("winnable"),
  MOST_LIKELY_WINNABLE("possibly winnable");

  private final String description;

  UnwinnableQuick(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

}
