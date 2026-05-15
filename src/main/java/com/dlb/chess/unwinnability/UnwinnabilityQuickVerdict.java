package com.dlb.chess.unwinnability;

public enum UnwinnabilityQuickVerdict {
  WINNABLE("winnable"),
  UNWINNABLE("unwinnable"),
  POSSIBLY_WINNABLE("undetermined");

  private final String identifier;

  UnwinnabilityQuickVerdict(String identifier) {
    this.identifier = identifier;
  }

  public String getIdentifier() {
    return identifier;
  }

  public static boolean exists(String identifier) {
    for (final UnwinnabilityQuickVerdict mode : values()) {
      if (mode.getIdentifier().equals(identifier)) {
        return true;
      }
    }
    return false;
  }

}
