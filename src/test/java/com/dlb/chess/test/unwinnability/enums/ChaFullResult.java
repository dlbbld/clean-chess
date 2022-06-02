package com.dlb.chess.test.unwinnability.enums;

import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;

public enum ChaFullResult {
  WINNABLE("winnable", UnwinnableFull.WINNABLE),
  UNWINNABLE("unwinnable", UnwinnableFull.UNWINNABLE),
  UNDETERMINED("undetermined", UnwinnableFull.UNDETERMINED);

  private final String identifier;
  private final UnwinnableFull unwinnableFull;

  ChaFullResult(String identifier, UnwinnableFull unwinnableFull) {
    this.identifier = identifier;
    this.unwinnableFull = unwinnableFull;
  }

  public String getIdentifier() {
    return identifier;
  }

  public UnwinnableFull getUnwinnableFull() {
    return unwinnableFull;
  }

  public static boolean exists(String identifier) {
    for (final ChaFullResult mode : values()) {
      if (mode.getIdentifier().equals(identifier)) {
        return true;
      }
    }
    return false;
  }

  public static ChaFullResult calculate(String identifier) {
    if (!exists(identifier)) {
      throw new IllegalArgumentException("No mode for this letter identifier");
    }
    for (final ChaFullResult mode : values()) {
      if (mode.getIdentifier().equals(identifier)) {
        return mode;
      }
    }
    // not possible to come here
    throw new ProgrammingMistakeException();
  }

}
