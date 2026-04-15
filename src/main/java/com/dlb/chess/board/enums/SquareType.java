package com.dlb.chess.board.enums;

import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.google.common.collect.ImmutableList;

public enum SquareType {
  LIGHT_SQUARE,
  DARK_SQUARE,
  NONE;

  @SuppressWarnings("null")
  public static final ImmutableList<SquareType> REAL = ImmutableList.of(LIGHT_SQUARE, DARK_SQUARE);

  // cannot define in constructor as cannot reference an enum befor it is defined
  public SquareType getOppositeSquareType() {
    return switch (this) {
      case LIGHT_SQUARE -> DARK_SQUARE;
      case DARK_SQUARE -> LIGHT_SQUARE;
      case NONE -> throw new ProgrammingMistakeException("The non square type has no opposite");
      default -> throw new ProgrammingMistakeException("The non square type has no opposite");
    };
  }

}
