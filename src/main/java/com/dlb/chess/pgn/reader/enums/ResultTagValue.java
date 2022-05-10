package com.dlb.chess.pgn.reader.enums;

import java.util.ArrayList;
import java.util.List;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.utility.BasicUtility;

public enum ResultTagValue {
  WHITE_WON("1-0", Side.WHITE),
  BLACK_WON("0-1", Side.BLACK),
  DRAW("1/2-1/2", Side.NONE),
  ONGOING("*", Side.NONE);

  private final String value;
  private final Side sideWon;

  ResultTagValue(String value, Side sideWon) {
    this.value = value;
    this.sideWon = sideWon;
  }

  public String getValue() {
    return value;
  }

  public Side getSideWon() {
    return sideWon;
  }

  public static String calculateList() {
    final List<String> list = new ArrayList<>();
    for (final ResultTagValue resultTagValue : ResultTagValue.values()) {
      list.add(resultTagValue.getValue());
    }
    return BasicUtility.calculateCommaSeparatedList(list);
  }

  public static boolean exists(String value) {
    for (final ResultTagValue resultTagValue : ResultTagValue.values()) {
      if (resultTagValue.getValue().equals(value)) {
        return true;
      }
    }
    return false;
  }

  public static ResultTagValue calculate(String value) {
    if (!exists(value)) {
      throw new IllegalArgumentException();
    }
    for (final ResultTagValue resultTagValue : ResultTagValue.values()) {
      if (resultTagValue.getValue().equals(value)) {
        return resultTagValue;
      }
    }
    throw new ProgrammingMistakeException();
  }

}
