package com.dlb.chess.board.enums;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.exceptions.NonePointerException;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;

public enum File {
  FILE_A('a', 1, true),
  FILE_B('b', 2, false),
  FILE_C('c', 3, false),
  FILE_D('d', 4, false),
  FILE_E('e', 5, false),
  FILE_F('f', 6, false),
  FILE_G('g', 7, false),
  FILE_H('h', 8, true),
  NONE('\0', 0, false);

  private final char letter;
  private final String letterString;

  private final int number;
  private final boolean isBorderFile;

  File(char letter, int number, boolean isBorderFile) {
    this.letter = letter;
    this.letterString = NonNullWrapperCommon.valueOf(letter);
    this.number = number;
    this.isBorderFile = isBorderFile;
  }

  public char getLetter() {
    check();
    return letter;
  }

  public String getLetterString() {
    check();
    return letterString;
  }

  public int getNumber() {
    check();
    return number;
  }

  public boolean getIsBorderFile() {
    check();
    return isBorderFile;
  }

  public static boolean exists(char letter) {
    for (final File file : values()) {
      if (file == NONE) {
        continue;
      }
      if (file.getLetter() == letter) {
        return true;
      }
    }
    return false;
  }

  public static File calculateFile(char letter) {
    if (!exists(letter)) {
      throw new IllegalArgumentException("For this letter no corresponding non dummy File exists");
    }
    for (final File file : values()) {
      if (file == NONE) {
        continue;
      }
      if (file.getLetter() == letter) {
        return file;
      }
    }
    throw new ProgrammingMistakeException("The code for calculating the file by letter is wrong");
  }

  private static File calculateLeftFileWhiteView(File file) {
    return switch (file) {
      case FILE_A -> throw new IllegalArgumentException();
      case FILE_B -> FILE_A;
      case FILE_C -> FILE_B;
      case FILE_D -> FILE_C;
      case FILE_E -> FILE_D;
      case FILE_F -> FILE_E;
      case FILE_G -> FILE_F;
      case FILE_H -> FILE_G;
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  private static File calculateRightFileWhiteView(File file) {
    return switch (file) {
      case FILE_A -> FILE_B;
      case FILE_B -> FILE_C;
      case FILE_C -> FILE_D;
      case FILE_D -> FILE_E;
      case FILE_E -> FILE_F;
      case FILE_F -> FILE_G;
      case FILE_G -> FILE_H;
      case FILE_H -> throw new IllegalArgumentException();
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  public static final boolean calculateHasLeftFile(final Side havingMove, final File file) {
    if (havingMove == Side.NONE || file == NONE) {
      throw new IllegalArgumentException();
    }

    return switch (havingMove) {
      case BLACK -> file != FILE_H;
      case WHITE -> file != FILE_A;
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  public static final boolean calculateHasLeftLeftFile(final Side havingMove, final File file) {
    if (havingMove == Side.NONE || file == NONE) {
      throw new IllegalArgumentException();
    }

    if (!calculateHasLeftFile(havingMove, file)) {
      return false;
    }
    final File leftFile = calculateLeftFile(havingMove, file);
    return calculateHasLeftFile(havingMove, leftFile);
  }

  public static final boolean calculateHasRightFile(final Side havingMove, final File file) {
    if (havingMove == Side.NONE || file == NONE) {
      throw new IllegalArgumentException();
    }

    return switch (havingMove) {
      case BLACK -> file != FILE_A;
      case WHITE -> file != FILE_H;
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  public static final boolean calculateHasRightRightFile(final Side havingMove, final File file) {
    if (havingMove == Side.NONE || file == NONE) {
      throw new IllegalArgumentException();
    }

    if (!calculateHasRightFile(havingMove, file)) {
      return false;
    }
    final File rightFile = calculateRightFile(havingMove, file);
    return calculateHasRightFile(havingMove, rightFile);
  }

  public static final File calculateLeftFile(final Side havingMove, final File file) {
    if (havingMove == Side.NONE || file == NONE) {
      throw new IllegalArgumentException();
    }

    if (!calculateHasLeftFile(havingMove, file)) {
      throw new IllegalArgumentException("No left file");
    }
    return switch (havingMove) {
      case BLACK -> calculateRightFileWhiteView(file);
      case WHITE -> calculateLeftFileWhiteView(file);
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  public static File calculateRightFile(Side havingMove, final File file) {
    if (havingMove == Side.NONE || file == NONE) {
      throw new IllegalArgumentException();
    }

    if (!calculateHasRightFile(havingMove, file)) {
      throw new IllegalArgumentException("No right file");
    }
    return switch (havingMove) {
      case BLACK -> calculateLeftFileWhiteView(file);
      case WHITE -> calculateRightFileWhiteView(file);
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

  public static final File calculateLeftLeftFile(final Side havingMove, final File file) {
    if (havingMove == Side.NONE || file == NONE) {
      throw new IllegalArgumentException();
    }

    if (!calculateHasLeftLeftFile(havingMove, file)) {
      throw new IllegalArgumentException("No left file");
    }
    final File leftFile = calculateLeftFile(havingMove, file);
    return calculateLeftFile(havingMove, leftFile);
  }

  public static final File calculateRightRightFile(final Side havingMove, final File file) {
    if (havingMove == Side.NONE || file == NONE) {
      throw new IllegalArgumentException();
    }

    if (!calculateHasRightRightFile(havingMove, file)) {
      throw new IllegalArgumentException("No right file");
    }
    final File rightFile = calculateRightFile(havingMove, file);
    return calculateRightFile(havingMove, rightFile);
  }

  private void check() {
    if (this == NONE) {
      throw new NonePointerException();
    }
  }
}
