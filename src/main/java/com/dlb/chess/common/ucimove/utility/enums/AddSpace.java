package com.dlb.chess.common.ucimove.utility.enums;

public enum AddSpace {

  YES(" "),
  NO("");

  private final String value;

  AddSpace(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

}
