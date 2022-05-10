package com.dlb.chess.board.enums;

import com.dlb.chess.common.exceptions.ProgrammingMistakeException;

public enum SquareType {
  LIGHT_SQUARE,
  DARK_SQUARE,
  NONE;

  // cannot define in constructor as cannot reference an enum befor it is defined
  public SquareType getOppositeSquareType() {
    switch (this) {
      case LIGHT_SQUARE:
        return DARK_SQUARE;
      case DARK_SQUARE:
        return LIGHT_SQUARE;
      case NONE:
      default:
        throw new ProgrammingMistakeException("The non square type has no opposite");
    }
  }

}
