package com.dlb.chess.test.san.format;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.san.validate.SanValidateFormat;

class TestSanValidateFormatDetailed {

  // --- Pawn format: second character ---

  @SuppressWarnings("static-method")
  @Test
  void testPawnSecondCharacter() {
    // valid: d3 (digit), dxe5 (x)
    checkValid("d3");
    checkValid("dxe5");

    // invalid: "dz" — 'z' is not a valid SAN character
    checkException("dz", SanValidationProblem.FORMAT_INVALID_CHARACTER);
    checkException("dR", SanValidationProblem.FORMAT_PAWN_SECOND_CHARACTER);
  }

  @SuppressWarnings("static-method")
  @Test
  void testPawnSecondCharacterLength4() {
    // length 4 pawn, second char neither x nor equals at position 2
    // "dae5" — countFiles > 2 (d, a, e)
    checkException("dae5", SanValidationProblem.FORMAT);
  }

  // --- Pawn format: capture destination ---

  @SuppressWarnings("static-method")
  @Test
  void testPawnCaptureToFile() {
    // "dx95" — '9' is not a valid SAN character
    checkException("dx95", SanValidationProblem.FORMAT_INVALID_CHARACTER);
  }

  @SuppressWarnings("static-method")
  @Test
  void testPawnCaptureToRank() {
    // "dxea" — countFiles > 2 (d, e, a)
    checkException("dxea", SanValidationProblem.FORMAT);
  }

  // --- Pawn format: promotion ---

  @SuppressWarnings("static-method")
  @Test
  void testPawnPromotionRank() {
    // "da=Q" — second char 'a' is not a rank digit (non-capturing promotion path)
    checkException("da=Q", SanValidationProblem.FORMAT_PAWN_PROMOTION_RANK);
  }

  @SuppressWarnings("static-method")
  @Test
  void testPawnPromotionCaptureStructure() {
    // "dabcde" — countFiles > 2 (d,a,b,c,d,e)
    checkException("dabcde", SanValidationProblem.FORMAT);
  }

  @SuppressWarnings("static-method")
  @Test
  void testPawnPromotionCaptureToFile() {
    // "dx9e=Q" — '9' is not a valid SAN character
    checkException("dx9e=Q", SanValidationProblem.FORMAT_INVALID_CHARACTER);
  }

  @SuppressWarnings("static-method")
  @Test
  void testPawnPromotionCaptureToRank() {
    // "dxea=Q" — countFiles > 2 (d, e, a)
    checkException("dxea=Q", SanValidationProblem.FORMAT);
  }

  // --- Pawn format: missing promotion on rank 1/8 ---

  @SuppressWarnings("static-method")
  @Test
  void testPawnMissingPromotionNonCapturing() {
    // rank 8 without =piece
    checkException("d8", SanValidationProblem.FORMAT_PAWN_MISSING_PROMOTION);
    checkException("a8", SanValidationProblem.FORMAT_PAWN_MISSING_PROMOTION);
    // rank 1 without =piece
    checkException("d1", SanValidationProblem.FORMAT_PAWN_MISSING_PROMOTION);
    checkException("a1", SanValidationProblem.FORMAT_PAWN_MISSING_PROMOTION);
  }

  @SuppressWarnings("static-method")
  @Test
  void testPawnMissingPromotionCapturing() {
    // capture to rank 8 without =piece
    checkException("dxe8", SanValidationProblem.FORMAT_PAWN_MISSING_PROMOTION);
    checkException("axb8", SanValidationProblem.FORMAT_PAWN_MISSING_PROMOTION);
    // capture to rank 1 without =piece
    checkException("dxe1", SanValidationProblem.FORMAT_PAWN_MISSING_PROMOTION);
    checkException("axb1", SanValidationProblem.FORMAT_PAWN_MISSING_PROMOTION);
  }

  // --- Pawn format: promotion on wrong rank (treated as length error) ---

  @SuppressWarnings("static-method")
  @Test
  void testPawnPromotionMiddleOfBoardNonCapturing() {
    // =piece on non-promotion rank
    checkException("d3=Q", SanValidationProblem.FORMAT_PAWN_LENGTH);
    checkException("d4=Q", SanValidationProblem.FORMAT_PAWN_LENGTH);
    checkException("d7=Q", SanValidationProblem.FORMAT_PAWN_LENGTH);
  }

  @SuppressWarnings("static-method")
  @Test
  void testPawnPromotionMiddleOfBoardCapturing() {
    // capture with =piece on non-promotion rank
    checkException("dxe5=Q", SanValidationProblem.FORMAT_PAWN_LENGTH);
    checkException("dxe3=Q", SanValidationProblem.FORMAT_PAWN_LENGTH);
  }

  // --- Pawn format: valid promotion (should not throw) ---

  @SuppressWarnings("static-method")
  @Test
  void testPawnPromotionValid() {
    checkValid("d8=Q");
    checkValid("d1=Q");
    checkValid("dxe8=Q");
    checkValid("dxe1=N");
  }

  // --- Pawn format: length ---

  @SuppressWarnings("static-method")
  @Test
  void testPawnLength() {
    // length 3 starting with file — not a valid pawn length
    checkException("d3e", SanValidationProblem.FORMAT_PAWN_LENGTH);
    // length 5 starting with file — not a valid pawn length
    checkException("dxe5Q", SanValidationProblem.FORMAT_PAWN_LENGTH);
  }

  // --- King format ---

  @SuppressWarnings("static-method")
  @Test
  void testKingDestination() {
    // "K9e" — '9' is not a valid SAN character
    checkException("K9e", SanValidationProblem.FORMAT_INVALID_CHARACTER);
    // "KR5" — 'R' is not a file letter
    checkException("KR5", SanValidationProblem.FORMAT_KING_DESTINATION);
  }

  @SuppressWarnings("static-method")
  @Test
  void testKingSecondCharacter() {
    // "Kae5" — length 4 king, looks like file disambiguation which is never valid for king
    checkException("Kae5", SanValidationProblem.FORMAT_KING_FILE_SPECIFIED);
  }

  @SuppressWarnings("static-method")
  @Test
  void testKingCaptureDestination() {
    // "Kx9e" — '9' is not a valid SAN character
    checkException("Kx9e", SanValidationProblem.FORMAT_INVALID_CHARACTER);
  }

  @SuppressWarnings("static-method")
  @Test
  void testKingLength() {
    // "Ke" — too short
    checkException("Ke", SanValidationProblem.FORMAT_KING_LENGTH);
    // "Kxe5a" — too long
    checkException("Kxe5a", SanValidationProblem.FORMAT_KING_LENGTH);
  }

  // --- Piece format (R, N, B, Q) ---

  @SuppressWarnings("static-method")
  @Test
  void testPieceDestination() {
    // "QeR" — Q + R = countRbnq > 1
    checkException("QeR", SanValidationProblem.FORMAT);
  }

  @SuppressWarnings("static-method")
  @Test
  void testPieceLength() {
    // "Qe" — length 2, too short for a piece move
    checkException("Qe", SanValidationProblem.FORMAT_PIECE_LENGTH);
  }

  @SuppressWarnings("static-method")
  @Test
  void testPieceMiddle1() {
    // "Q=e5" — middle char '=' not a file/rank/x
    checkException("Q=e5", SanValidationProblem.FORMAT_PIECE_MIDDLE);
  }

  @SuppressWarnings("static-method")
  @Test
  void testPieceMiddle2() {
    // "Qabe5" — countFiles > 2 (a, b, e)
    checkException("Qabe5", SanValidationProblem.FORMAT);
  }

  @SuppressWarnings("static-method")
  @Test
  void testPieceMiddle3() {
    // "Qa3ae5" — countFiles > 2 (a, a, e)
    checkException("Qa3ae5", SanValidationProblem.FORMAT);
  }

  // --- Castling format ---

  @SuppressWarnings("static-method")
  @Test
  void testCastling() {
    // valid
    checkValid("O-O");
    checkValid("O-O-O");

    // invalid: "O-O-" — starts with O but is neither O-O nor O-O-O
    checkException("O-O-", SanValidationProblem.FORMAT_CASTLING);
    // "O-" — too short
    checkException("O-", SanValidationProblem.FORMAT_CASTLING);
    // "O" — just O
    checkException("O", SanValidationProblem.FORMAT_CASTLING);
  }

  // --- Helpers ---

  private static void checkException(String san, SanValidationProblem expectedProblem) {
    boolean isException;
    try {
      SanValidateFormat.validateFormat(san);
      isException = false;
    } catch (final SanValidationException e) {
      isException = true;
      System.out.println("SAN: " + san + " -> " + e.getMessage());
      assertEquals(expectedProblem, e.getSanValidationProblem());
    }
    assertTrue(isException);
  }

  private static void checkValid(String san) {
    var isException = false;
    try {
      SanValidateFormat.validateFormat(san);
    } catch (@SuppressWarnings("unused") final SanValidationException e) {
      isException = true;
    }
    assertFalse(isException);
  }

}
