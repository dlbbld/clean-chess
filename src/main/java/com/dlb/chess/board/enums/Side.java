package com.dlb.chess.board.enums;

import com.dlb.chess.common.exceptions.NonePointerException;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.internationalization.Message;

public enum Side {
  WHITE(true, false, Message.getString("color.white.name"), "w"),
  BLACK(false, true, Message.getString("color.black.name"), "b"),
  NONE(false, false, "", " ");

  private final boolean isWhite;
  private final boolean isBlack;
  private final String name;
  private final String fenLetter;

  Side(boolean isWhite, boolean isBlack, String name, String fenLetter) {
    this.isWhite = isWhite;
    this.isBlack = isBlack;
    this.name = name;
    this.fenLetter = fenLetter;
  }

  public boolean getIsWhite() {
    check();
    return isWhite;
  }

  public boolean getIsBlack() {
    check();
    return isBlack;
  }

  public String getName() {
    check();
    return name;
  }

  public String getFenLetter() {
    check();
    return fenLetter;
  }

  // cannot define in constructor as cannot reference an enum befor it is defined
  public Side getOppositeSide() {
    switch (this) {
      case WHITE:
        return BLACK;
      case BLACK:
        return WHITE;
      case NONE:
      default:
        throw new ProgrammingMistakeException("The non side has no opposite side");
    }
  }

  private void check() {
    if (this == NONE) {
      throw new NonePointerException();
    }
  }
}
