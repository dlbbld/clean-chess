package com.dlb.chess.pgn.reader.enums;

import java.util.ArrayList;
import java.util.List;

import com.dlb.chess.common.exceptions.NonePointerException;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.utility.BasicUtility;

public enum SetUpTagValue {
  START_FROM_INITIAL_POSITION("0"),
  START_FROM_SETUP_POSITION("1"),
  NONE("");

  private final String value;

  SetUpTagValue(String value) {
    this.value = value;
  }

  public String getValue() {
    check();
    return value;
  }

  public static String calculateList() {
    final List<String> list = new ArrayList<>();
    for (final SetUpTagValue tagValue : SetUpTagValue.values()) {
      if (tagValue == NONE) {
        continue;
      }
      list.add(tagValue.getValue());
    }
    return BasicUtility.calculateCommaSeparatedList(list);
  }

  public static boolean exists(String value) {
    for (final SetUpTagValue tagValue : SetUpTagValue.values()) {
      if (tagValue == NONE) {
        continue;
      }
      if (tagValue.getValue().equals(value)) {
        return true;
      }
    }
    return false;
  }

  public static SetUpTagValue calculate(String value) {
    if (!exists(value)) {
      throw new IllegalArgumentException();
    }
    for (final SetUpTagValue tagValue : SetUpTagValue.values()) {
      if (tagValue == NONE) {
        continue;
      }
      if (tagValue.getValue().equals(value)) {
        return tagValue;
      }
    }
    throw new ProgrammingMistakeException();
  }

  private void check() {
    if (this == NONE) {
      throw new NonePointerException();
    }
  }
}
