package com.dlb.chess.test.basic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.constants.ChessConstants;
import com.dlb.chess.common.constants.EnumConstants;

class TestBasicFile implements EnumConstants {

  @SuppressWarnings("static-method")
  @Test
  void testCount() throws Exception {
    var totalFiles = 0;
    for (final File file : File.values()) {
      if (file != File.NONE) {
        totalFiles++;
      }
    }
    assertEquals(8, totalFiles);
  }

  @SuppressWarnings("static-method")
  @Test
  void testMethodsDirect() throws Exception {
    assertTrue(File.exists(ChessConstants.FILE_A_LETTER));
    assertTrue(File.exists(ChessConstants.FILE_B_LETTER));
    assertTrue(File.exists(ChessConstants.FILE_C_LETTER));
    assertTrue(File.exists(ChessConstants.FILE_D_LETTER));
    assertTrue(File.exists(ChessConstants.FILE_E_LETTER));
    assertTrue(File.exists(ChessConstants.FILE_F_LETTER));
    assertTrue(File.exists(ChessConstants.FILE_G_LETTER));
    assertTrue(File.exists(ChessConstants.FILE_H_LETTER));

    assertFalse(File.exists("i"));
    assertFalse(File.exists("j"));
    assertFalse(File.exists("k"));
    assertFalse(File.exists("1"));
    assertFalse(File.exists("2"));
    assertFalse(File.exists("3"));
    assertFalse(File.exists("-1"));
    assertFalse(File.exists("0"));
    assertFalse(File.exists("9"));

    assertEquals(File.FILE_A, File.calculateFile(ChessConstants.FILE_A_LETTER));
    assertEquals(File.FILE_B, File.calculateFile(ChessConstants.FILE_B_LETTER));
    assertEquals(File.FILE_C, File.calculateFile(ChessConstants.FILE_C_LETTER));
    assertEquals(File.FILE_D, File.calculateFile(ChessConstants.FILE_D_LETTER));
    assertEquals(File.FILE_E, File.calculateFile(ChessConstants.FILE_E_LETTER));
    assertEquals(File.FILE_F, File.calculateFile(ChessConstants.FILE_F_LETTER));
    assertEquals(File.FILE_G, File.calculateFile(ChessConstants.FILE_G_LETTER));
    assertEquals(File.FILE_H, File.calculateFile(ChessConstants.FILE_H_LETTER));

    checkException("i");
    checkException("j");
    checkException("k");
    checkException("1");
    checkException("2");
    checkException("3");
    checkException("-1");
    checkException("0");
    checkException("9");
  }

  @SuppressWarnings("static-method")
  @Test
  void testMethodsAdjacent() throws Exception {

    // white existence
    assertFalse(File.calculateHasLeftFile(WHITE, File.FILE_A));
    assertTrue(File.calculateHasLeftFile(WHITE, File.FILE_B));
    assertTrue(File.calculateHasLeftFile(WHITE, File.FILE_C));
    assertTrue(File.calculateHasLeftFile(WHITE, File.FILE_D));
    assertTrue(File.calculateHasLeftFile(WHITE, File.FILE_E));
    assertTrue(File.calculateHasLeftFile(WHITE, File.FILE_F));
    assertTrue(File.calculateHasLeftFile(WHITE, File.FILE_G));
    assertTrue(File.calculateHasLeftFile(WHITE, File.FILE_H));

    assertTrue(File.calculateHasRightFile(WHITE, File.FILE_A));
    assertTrue(File.calculateHasRightFile(WHITE, File.FILE_B));
    assertTrue(File.calculateHasRightFile(WHITE, File.FILE_C));
    assertTrue(File.calculateHasRightFile(WHITE, File.FILE_D));
    assertTrue(File.calculateHasRightFile(WHITE, File.FILE_E));
    assertTrue(File.calculateHasRightFile(WHITE, File.FILE_F));
    assertTrue(File.calculateHasRightFile(WHITE, File.FILE_G));
    assertFalse(File.calculateHasRightFile(WHITE, File.FILE_H));

    // black existence
    assertTrue(File.calculateHasLeftFile(BLACK, File.FILE_A));
    assertTrue(File.calculateHasLeftFile(BLACK, File.FILE_B));
    assertTrue(File.calculateHasLeftFile(BLACK, File.FILE_C));
    assertTrue(File.calculateHasLeftFile(BLACK, File.FILE_D));
    assertTrue(File.calculateHasLeftFile(BLACK, File.FILE_E));
    assertTrue(File.calculateHasLeftFile(BLACK, File.FILE_F));
    assertTrue(File.calculateHasLeftFile(BLACK, File.FILE_G));
    assertFalse(File.calculateHasLeftFile(BLACK, File.FILE_H));

    assertFalse(File.calculateHasRightFile(BLACK, File.FILE_A));
    assertTrue(File.calculateHasRightFile(BLACK, File.FILE_B));
    assertTrue(File.calculateHasRightFile(BLACK, File.FILE_C));
    assertTrue(File.calculateHasRightFile(BLACK, File.FILE_D));
    assertTrue(File.calculateHasRightFile(BLACK, File.FILE_E));
    assertTrue(File.calculateHasRightFile(BLACK, File.FILE_F));
    assertTrue(File.calculateHasRightFile(BLACK, File.FILE_G));
    assertTrue(File.calculateHasRightFile(BLACK, File.FILE_H));

    // white value
    checkExceptionLeft(WHITE, File.FILE_A);
    assertEquals(File.FILE_A, File.calculateLeftFile(WHITE, File.FILE_B));
    assertEquals(File.FILE_B, File.calculateLeftFile(WHITE, File.FILE_C));
    assertEquals(File.FILE_C, File.calculateLeftFile(WHITE, File.FILE_D));
    assertEquals(File.FILE_D, File.calculateLeftFile(WHITE, File.FILE_E));
    assertEquals(File.FILE_E, File.calculateLeftFile(WHITE, File.FILE_F));
    assertEquals(File.FILE_F, File.calculateLeftFile(WHITE, File.FILE_G));

    assertEquals(File.FILE_B, File.calculateRightFile(WHITE, File.FILE_A));
    assertEquals(File.FILE_C, File.calculateRightFile(WHITE, File.FILE_B));
    assertEquals(File.FILE_D, File.calculateRightFile(WHITE, File.FILE_C));
    assertEquals(File.FILE_E, File.calculateRightFile(WHITE, File.FILE_D));
    assertEquals(File.FILE_F, File.calculateRightFile(WHITE, File.FILE_E));
    assertEquals(File.FILE_G, File.calculateRightFile(WHITE, File.FILE_F));
    assertEquals(File.FILE_H, File.calculateRightFile(WHITE, File.FILE_G));
    checkExceptionRight(WHITE, File.FILE_H);

    // black value
    assertEquals(File.FILE_B, File.calculateLeftFile(BLACK, File.FILE_A));
    assertEquals(File.FILE_C, File.calculateLeftFile(BLACK, File.FILE_B));
    assertEquals(File.FILE_D, File.calculateLeftFile(BLACK, File.FILE_C));
    assertEquals(File.FILE_E, File.calculateLeftFile(BLACK, File.FILE_D));
    assertEquals(File.FILE_F, File.calculateLeftFile(BLACK, File.FILE_E));
    assertEquals(File.FILE_G, File.calculateLeftFile(BLACK, File.FILE_F));
    assertEquals(File.FILE_H, File.calculateLeftFile(BLACK, File.FILE_G));
    checkExceptionLeft(BLACK, File.FILE_H);

    checkExceptionRight(BLACK, File.FILE_A);
    assertEquals(File.FILE_A, File.calculateRightFile(BLACK, File.FILE_B));
    assertEquals(File.FILE_B, File.calculateRightFile(BLACK, File.FILE_C));
    assertEquals(File.FILE_C, File.calculateRightFile(BLACK, File.FILE_D));
    assertEquals(File.FILE_D, File.calculateRightFile(BLACK, File.FILE_E));
    assertEquals(File.FILE_E, File.calculateRightFile(BLACK, File.FILE_F));
    assertEquals(File.FILE_F, File.calculateRightFile(BLACK, File.FILE_G));
    assertEquals(File.FILE_G, File.calculateRightFile(BLACK, File.FILE_H));
  }

  @SuppressWarnings("static-method")
  @Test
  void testMethodsAdjacentAdjacent() throws Exception {
    // white existence
    assertFalse(File.calculateHasLeftLeftFile(WHITE, File.FILE_A));
    assertFalse(File.calculateHasLeftLeftFile(WHITE, File.FILE_B));
    assertTrue(File.calculateHasLeftLeftFile(WHITE, File.FILE_C));
    assertTrue(File.calculateHasLeftLeftFile(WHITE, File.FILE_D));
    assertTrue(File.calculateHasLeftLeftFile(WHITE, File.FILE_E));
    assertTrue(File.calculateHasLeftLeftFile(WHITE, File.FILE_F));
    assertTrue(File.calculateHasLeftLeftFile(WHITE, File.FILE_G));
    assertTrue(File.calculateHasLeftLeftFile(WHITE, File.FILE_H));

    assertTrue(File.calculateHasRightRightFile(WHITE, File.FILE_A));
    assertTrue(File.calculateHasRightRightFile(WHITE, File.FILE_B));
    assertTrue(File.calculateHasRightRightFile(WHITE, File.FILE_C));
    assertTrue(File.calculateHasRightRightFile(WHITE, File.FILE_D));
    assertTrue(File.calculateHasRightRightFile(WHITE, File.FILE_E));
    assertTrue(File.calculateHasRightRightFile(WHITE, File.FILE_F));
    assertFalse(File.calculateHasRightRightFile(WHITE, File.FILE_G));
    assertFalse(File.calculateHasRightRightFile(WHITE, File.FILE_H));

    // black existence
    assertTrue(File.calculateHasLeftLeftFile(BLACK, File.FILE_A));
    assertTrue(File.calculateHasLeftLeftFile(BLACK, File.FILE_B));
    assertTrue(File.calculateHasLeftLeftFile(BLACK, File.FILE_C));
    assertTrue(File.calculateHasLeftLeftFile(BLACK, File.FILE_D));
    assertTrue(File.calculateHasLeftLeftFile(BLACK, File.FILE_E));
    assertTrue(File.calculateHasLeftLeftFile(BLACK, File.FILE_F));
    assertFalse(File.calculateHasLeftLeftFile(BLACK, File.FILE_G));
    assertFalse(File.calculateHasLeftLeftFile(BLACK, File.FILE_H));

    assertFalse(File.calculateHasRightRightFile(BLACK, File.FILE_A));
    assertFalse(File.calculateHasRightRightFile(BLACK, File.FILE_B));
    assertTrue(File.calculateHasRightRightFile(BLACK, File.FILE_C));
    assertTrue(File.calculateHasRightRightFile(BLACK, File.FILE_D));
    assertTrue(File.calculateHasRightRightFile(BLACK, File.FILE_E));
    assertTrue(File.calculateHasRightRightFile(BLACK, File.FILE_F));
    assertTrue(File.calculateHasRightRightFile(BLACK, File.FILE_G));
    assertTrue(File.calculateHasRightRightFile(BLACK, File.FILE_H));

    // white values
    checkExceptionLeftLeft(WHITE, File.FILE_A);
    checkExceptionLeftLeft(WHITE, File.FILE_B);
    assertEquals(File.FILE_A, File.calculateLeftLeftFile(WHITE, File.FILE_C));
    assertEquals(File.FILE_B, File.calculateLeftLeftFile(WHITE, File.FILE_D));
    assertEquals(File.FILE_C, File.calculateLeftLeftFile(WHITE, File.FILE_E));
    assertEquals(File.FILE_D, File.calculateLeftLeftFile(WHITE, File.FILE_F));
    assertEquals(File.FILE_E, File.calculateLeftLeftFile(WHITE, File.FILE_G));
    assertEquals(File.FILE_F, File.calculateLeftLeftFile(WHITE, File.FILE_H));

    assertEquals(File.FILE_C, File.calculateRightRightFile(WHITE, File.FILE_A));
    assertEquals(File.FILE_D, File.calculateRightRightFile(WHITE, File.FILE_B));
    assertEquals(File.FILE_E, File.calculateRightRightFile(WHITE, File.FILE_C));
    assertEquals(File.FILE_F, File.calculateRightRightFile(WHITE, File.FILE_D));
    assertEquals(File.FILE_G, File.calculateRightRightFile(WHITE, File.FILE_E));
    assertEquals(File.FILE_H, File.calculateRightRightFile(WHITE, File.FILE_F));
    checkExceptionRightRight(WHITE, File.FILE_G);
    checkExceptionRightRight(WHITE, File.FILE_H);

    // black values
    assertEquals(File.FILE_C, File.calculateLeftLeftFile(BLACK, File.FILE_A));
    assertEquals(File.FILE_D, File.calculateLeftLeftFile(BLACK, File.FILE_B));
    assertEquals(File.FILE_E, File.calculateLeftLeftFile(BLACK, File.FILE_C));
    assertEquals(File.FILE_F, File.calculateLeftLeftFile(BLACK, File.FILE_D));
    assertEquals(File.FILE_G, File.calculateLeftLeftFile(BLACK, File.FILE_E));
    assertEquals(File.FILE_H, File.calculateLeftLeftFile(BLACK, File.FILE_F));
    checkExceptionLeftLeft(BLACK, File.FILE_G);
    checkExceptionLeftLeft(BLACK, File.FILE_H);

    checkExceptionRightRight(BLACK, File.FILE_A);
    checkExceptionRightRight(BLACK, File.FILE_B);
    assertEquals(File.FILE_A, File.calculateRightRightFile(BLACK, File.FILE_C));
    assertEquals(File.FILE_B, File.calculateRightRightFile(BLACK, File.FILE_D));
    assertEquals(File.FILE_C, File.calculateRightRightFile(BLACK, File.FILE_E));
    assertEquals(File.FILE_D, File.calculateRightRightFile(BLACK, File.FILE_F));
    assertEquals(File.FILE_E, File.calculateRightRightFile(BLACK, File.FILE_G));
    assertEquals(File.FILE_F, File.calculateRightRightFile(BLACK, File.FILE_H));

  }

  private static void checkException(String fileLetter) {
    boolean isException;
    try {
      File.calculateFile(fileLetter);
      isException = false;
    } catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
      isException = true;
    }
    assertTrue(isException);
  }

  private static void checkExceptionLeft(Side side, File file) {
    boolean isException;
    try {
      File.calculateLeftFile(side, file);
      isException = false;
    } catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
      isException = true;
    }
    assertTrue(isException);
  }

  private static void checkExceptionRight(Side side, File file) {
    boolean isException;
    try {
      File.calculateRightFile(side, file);
      isException = false;
    } catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
      isException = true;
    }
    assertTrue(isException);
  }

  private static void checkExceptionLeftLeft(Side side, File file) {
    boolean isException;
    try {
      File.calculateLeftLeftFile(side, file);
      isException = false;
    } catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
      isException = true;
    }
    assertTrue(isException);
  }

  private static void checkExceptionRightRight(Side side, File file) {
    boolean isException;
    try {
      File.calculateRightRightFile(side, file);
      isException = false;
    } catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
      isException = true;
    }
    assertTrue(isException);
  }
}
