package com.dlb.chess.test.unwinnability.enums;

import com.dlb.chess.common.exceptions.ProgrammingMistakeException;

public enum ChaMode {
  FULL("full"),
  QUICK("quick");

  private final String identifier;

  ChaMode(String identifier) {
    this.identifier = identifier;
  }

  public String getIdentifier() {
    return identifier;
  }

  public static boolean exists(String identifier) {
    for (final ChaMode mode : values()) {
      if (mode.getIdentifier().equals(identifier)) {
        return true;
      }
    }
    return false;
  }

  public static ChaMode calculate(String identifier) {
    if (!exists(identifier)) {
      throw new IllegalArgumentException("No mode for this letter identifier");
    }
    for (final ChaMode mode : values()) {
      if (mode.getIdentifier().equals(identifier)) {
        return mode;
      }
    }
    // not possible to come here
    throw new ProgrammingMistakeException();
  }

}
