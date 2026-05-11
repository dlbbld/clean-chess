package com.dlb.chess.unwinnability;

public enum UnwinnableQuick {
  WINNABLE("winnable"),
  UNWINNABLE("unwinnable"),
  POSSIBLY_WINNABLE("undetermined");

  private final String identifier;

  UnwinnableQuick(String identifier) {
    this.identifier = identifier;
  }

  public String getIdentifier() {
    return identifier;
  }

  public static boolean exists(String identifier) {
    for (final UnwinnableQuick mode : values()) {
      if (mode.getIdentifier().equals(identifier)) {
        return true;
      }
    }
    return false;
  }

}
