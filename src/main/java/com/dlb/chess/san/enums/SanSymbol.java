package com.dlb.chess.san.enums;

import com.dlb.chess.common.NonNullWrapperCommon;

public enum SanSymbol {
  CAPTURE('x'),
  PROMOTION('='),
  CHECK('+'),
  CHECKMATE('#');

  private final char symbol;
  private final String symbolString;

  SanSymbol(char symbol) {
    this.symbol = symbol;
    this.symbolString = NonNullWrapperCommon.valueOf(symbol);
  }

  public char getSymbol() {
    return symbol;
  }

  public String getSymbolString() {
    return symbolString;
  }

  public static boolean exists(char symbol) {
    for (final SanSymbol sanSymbol : SanSymbol.values()) {
      if (sanSymbol.getSymbol() == symbol) {
        return true;
      }
    }
    return false;
  }
}
