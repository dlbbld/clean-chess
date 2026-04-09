package com.dlb.chess.test.san.format;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.san.validate.SanValidateFormat;

class TestSanValidateFormatBasic {

  // --- Invalid characters ---

  @SuppressWarnings("static-method")
  @Test
  void testInvalidLetters() {
    // 'i' is not a valid SAN character
    checkException("Q2xi5", "i");
    checkException("Ni3e5", "i");
    checkException("Ri5", "i");

    // 'P' is not allowed (pawn has no letter in SAN)
    checkException("Pe4", "P");

    // 'Z' is not a valid SAN character
    checkException("Ze4", "Z");

    // 'j' is not a valid SAN character
    checkException("j4", "j");
  }

  @SuppressWarnings("static-method")
  @Test
  void testInvalidDigitNine() {
    // '9' is not a valid rank digit (only 1-8)
    checkException("d9", "9");
    checkException("Qe9", "9");
    checkException("dxe9", "9");
  }

  @SuppressWarnings("static-method")
  @Test
  void testInvalidDigitZero() {
    // '0' is not a valid SAN character
    checkException("d0", "0");
  }

  @SuppressWarnings("static-method")
  @Test
  void testInvalidSpecialCharacters() {
    checkException("d.4", ".");
    checkException("d,4", ",");
    checkException("d/4", "/");
    checkException("d@4", "@");
  }

  // --- Valid characters (should not throw FORMAT_INVALID_CHARACTER) ---

  @SuppressWarnings("static-method")
  @Test
  void testValidCharacters() {
    // all allowed characters should pass validateFormatBasic
    checkValid("d3");
    checkValid("dxe5");
    checkValid("d8=Q");
    checkValid("dxe8=Q");
    checkValid("Nf3");
    checkValid("Nf3+");
    checkValid("Nf3#");
    checkValid("O-O");
    checkValid("O-O-O");
    checkValid("Ke2");
    checkValid("Kxe5");
  }

  private static void checkException(String san, String invalidChar) {
    boolean isException;
    try {
      SanValidateFormat.validateFormatBasic(san);
      isException = false;
    } catch (final SanValidationException e) {
      isException = true;
      System.out.println("SAN: " + san + " -> " + e.getMessage() + " (invalid char: '" + invalidChar + "')");
      assertEquals(SanValidationProblem.FORMAT_INVALID_CHARACTER, e.getSanValidationProblem());
    }
    assertTrue(isException);
  }

  private static void checkValid(String san) {
    var isException = false;
    try {
      SanValidateFormat.validateFormatBasic(san);
    } catch (@SuppressWarnings("unused") final SanValidationException e) {
      isException = true;
    }
    assertFalse(isException);
  }

}
