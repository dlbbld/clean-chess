package com.dlb.chess.unwinnability.quick.enums;

public enum UnwinnableQuickResult {
  UNWINNABLE("unwinnable"),
  WINNABLE("winnable"),
  POSSIBLY_WINNABLE("possibly winnable");

  private final String description;

  UnwinnableQuickResult(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

}
