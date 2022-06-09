package com.dlb.chess.unwinnability.quick.enums;

import com.dlb.chess.common.exceptions.ProgrammingMistakeException;

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

  public static UnwinnableQuick calculate(String identifier) {
    if (!exists(identifier)) {
      throw new IllegalArgumentException("No mode for this letter identifier");
    }
    for (final UnwinnableQuick mode : values()) {
      if (mode.getIdentifier().equals(identifier)) {
        return mode;
      }
    }
    // not possible to come here
    throw new ProgrammingMistakeException();
  }

}
