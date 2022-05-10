package com.dlb.chess.enums;

import java.util.ArrayList;
import java.util.List;

import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.utility.BasicUtility;

public enum MoveSuffixAnnotation {

  MISTAKE("?"),
  GOOD_MOVE("!"),
  BLUNDER("??"),
  DUBIOUS_MOVE("?!"),
  INTERESTING_MOVE("!?"),
  BRILLIANT_MOVE("!!"),
  NONE("");

  private final String suffix;

  MoveSuffixAnnotation(String suffix) {
    this.suffix = suffix;
  }

  public String getSuffix() {
    return suffix;
  }

  public static boolean exists(String suffix) {
    for (final MoveSuffixAnnotation suffixEnum : values()) {
      if (suffixEnum != NONE && suffixEnum.getSuffix().equals(suffix)) {
        return true;
      }
    }
    return false;
  }

  public static MoveSuffixAnnotation calculate(String suffix) {
    if (!exists(suffix)) {
      throw new IllegalArgumentException("No enum exists for this suffix");
    }
    for (final MoveSuffixAnnotation suffixEnum : values()) {
      if (suffixEnum != NONE && suffixEnum.getSuffix().equals(suffix)) {
        return suffixEnum;
      }
    }
    throw new ProgrammingMistakeException("The code for calculating the suffix enum is wrong");
  }

  public static String calculateValueList() {
    final List<String> list = new ArrayList<>();
    for (final MoveSuffixAnnotation suffixEnum : values()) {
      if (suffixEnum != NONE) {
        list.add(suffixEnum.getSuffix());
      }
    }
    return BasicUtility.calculateCommaSeparatedList(list);
  }
}
