package com.dlb.chess.enums;

public enum MoveSuffixAnnotationLetter {
  QUESTION_MARK("?"),
  EXCLAMATION_MARK("!");

  private final String letter;

  MoveSuffixAnnotationLetter(String letter) {
    this.letter = letter;
  }

  public String getLetter() {
    return letter;
  }

  public static boolean exists(String letter) {
    for (final MoveSuffixAnnotationLetter letterEnum : MoveSuffixAnnotationLetter.values()) {
      if (letterEnum.getLetter().equals(letter)) {
        return true;
      }
    }
    return false;
  }
}
