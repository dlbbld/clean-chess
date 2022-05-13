package com.dlb.chess.unwinnability.quick.enums;

public enum UnwinnableQuick {
  UNWINNABLE("unwinnable"),
  WINNABLE("winnable"),
  POSSIBLY_WINNABLE("possibly winnable");

  private final String description;

  UnwinnableQuick(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

}
