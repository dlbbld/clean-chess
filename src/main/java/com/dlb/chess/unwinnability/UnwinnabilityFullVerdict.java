package com.dlb.chess.unwinnability;

import com.dlb.chess.common.exceptions.ProgrammingMistakeException;

public enum UnwinnabilityFullVerdict {
  WINNABLE("winnable"),
  UNWINNABLE("unwinnable"),
  UNDETERMINED("undetermined");

  private final String identifier;

  UnwinnabilityFullVerdict(String identifier) {
    this.identifier = identifier;
  }

  public String getIdentifier() {
    return identifier;
  }

  public static boolean exists(String identifier) {
    for (final UnwinnabilityFullVerdict mode : values()) {
      if (mode.getIdentifier().equals(identifier)) {
        return true;
      }
    }
    return false;
  }

  public static UnwinnabilityFullVerdict calculate(String identifier) {
    if (!exists(identifier)) {
      throw new IllegalArgumentException("No mode for this letter identifier");
    }
    for (final UnwinnabilityFullVerdict mode : values()) {
      if (mode.getIdentifier().equals(identifier)) {
        return mode;
      }
    }
    // not possible to come here
    throw new ProgrammingMistakeException();
  }
}
