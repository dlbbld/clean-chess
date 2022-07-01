package com.dlb.chess.unwinnability.full.enums;

import com.dlb.chess.common.exceptions.ProgrammingMistakeException;

public enum UnwinnableFull {
  WINNABLE("winnable"),
  UNWINNABLE("unwinnable"),
  UNDETERMINED("undetermined");

  private final String identifier;

  UnwinnableFull(String identifier) {
    this.identifier = identifier;
  }

  public String getIdentifier() {
    return identifier;
  }

  public static boolean exists(String identifier) {
    for (final UnwinnableFull mode : values()) {
      if (mode.getIdentifier().equals(identifier)) {
        return true;
      }
    }
    return false;
  }

  public static UnwinnableFull calculate(String identifier) {
    if (!exists(identifier)) {
      throw new IllegalArgumentException("No mode for this letter identifier");
    }
    for (final UnwinnableFull mode : values()) {
      if (mode.getIdentifier().equals(identifier)) {
        return mode;
      }
    }
    // not possible to come here
    throw new ProgrammingMistakeException();
  }
}
