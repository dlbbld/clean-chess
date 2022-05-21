package com.dlb.chess.test.unwinnability.enums;

import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuick;

public enum ChaQuickResult {
  WINNABLE("winnable", UnwinnableQuick.WINNABLE),
  UNWINNABLE("unwinnable", UnwinnableQuick.UNWINNABLE),
  POSSIBLY_WINNABLE("undetermined", UnwinnableQuick.POSSIBLY_WINNABLE);

  private final String identifier;
  private final UnwinnableQuick unwinnableQuick;

  ChaQuickResult(String identifier, UnwinnableQuick unwinnableQuick) {
    this.identifier = identifier;
    this.unwinnableQuick = unwinnableQuick;
  }

  public String getIdentifier() {
    return identifier;
  }

  public UnwinnableQuick getUnwinnableQuick() {
    return unwinnableQuick;
  }

  public static boolean exists(String identifier) {
    for (final ChaQuickResult mode : values()) {
      if (mode.getIdentifier().equals(identifier)) {
        return true;
      }
    }
    return false;
  }

  public static ChaQuickResult calculate(String identifier) {
    if (!exists(identifier)) {
      throw new IllegalArgumentException("No mode for this letter identifier");
    }
    for (final ChaQuickResult mode : values()) {
      if (mode.getIdentifier().equals(identifier)) {
        return mode;
      }
    }
    // not possible to come here
    throw new ProgrammingMistakeException();
  }

}
