package com.dlb.chess.test.san;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.san.validate.SanValidation;

/**
 * Per-entry coverage for {@link SanValidationProblem}. For each enum constant that can be triggered from a SAN input,
 * at least one test case is provided that:
 *
 * <ol>
 * <li>exercises a SAN string (plus board position, where relevant) that produces that exact problem code, and</li>
 * <li>asserts the full exception message — both static messages and parameter-substituted messages (e.g.
 * {@code ''{0}''}) are verified against the expected final string, so message templates and their interpolation are
 * covered in one place.</li>
 * </ol>
 *
 * <p>
 * The two non-triggering values {@link SanValidationProblem#NONE} and {@link SanValidationProblem#UNKNOWN_ERROR} have
 * no test case.
 */
class TestSanValidationProblemMessage {

  /**
   * When {@code true}, each test asserts the exact full exception message (useful while messages.properties is being
   * built up, to lock down wording and parameter interpolation). When stable, flip to {@code false} — only the problem
   * code is then checked, so message wording can be freely edited in the resource bundle without breaking tests.
   */
  private static final boolean IS_CHECK_MESSAGE = true;

  @SuppressWarnings("static-method")
  @Test
  void testFormat() {
    checkException("", SanValidationProblem.FORMAT_BLANK, "The value cannot be blank.");

    checkException("Z", SanValidationProblem.FORMAT_FIRST_CHARACTER,
        "A SAN move must start with a file letter (a-h), a piece letter (R, N, B, Q, K), or O for castling, but starts with 'Z'.");

    checkException("aa", SanValidationProblem.FORMAT_PAWN_SECOND_CHARACTER,
        "For a pawn forward move, the second character must be a rank digit (1-8) or the capture symbol (x), but is 'a'.");

    checkException("axQ", SanValidationProblem.FORMAT_PAWN_CAPTURE_TO_FILE,
        "For a pawn capture, a file letter (a-h) is expected after the capture symbol for the destination file, but is 'Q'.");

    checkException("axbb", SanValidationProblem.FORMAT_PAWN_CAPTURE_TO_RANK,
        "For a pawn capture, a rank digit (1-8) is expected after the destination file for the destination rank, but is 'b'.");

  }

  /** Checks a SAN against the initial position. */
  private static void checkException(String san, SanValidationProblem expectedProblem, String expectedMessage) {
    checkException(san, new Board(), expectedProblem, expectedMessage);
  }

  /** Checks a SAN against the given board, asserting both the problem code and the full exception message. */
  private static void checkException(String san, ApiBoard board, SanValidationProblem expectedProblem,
      String expectedMessage) {
    boolean isException;
    try {
      SanValidation.validateSan(san, board);
      isException = false;
    } catch (final SanValidationException e) {
      isException = true;
      assertEquals(expectedProblem, e.getSanValidationProblem());
      if (IS_CHECK_MESSAGE) {
        assertEquals(expectedMessage, e.getMessage());
      }
    }
    assertTrue(isException);
  }

}
