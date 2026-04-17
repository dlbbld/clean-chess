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
  void testFormatGeneral() {
    checkException("", SanValidationProblem.FORMAT_BLANK, "The value cannot be blank.");

    checkException("Z", SanValidationProblem.FORMAT_FIRST_CHARACTER,
        "A SAN move must start with a file letter (a-h), a piece letter (R, N, B, Q, K), or O for castling (letter O not digit 0), but starts with 'Z'.");

  }

  @SuppressWarnings("static-method")
  @Test
  void testFormatPawn() {

    // pawn
    checkException("a", SanValidationProblem.FORMAT_PAWN_NO_SECOND_CHARACTER,
        "For a pawn move the file must be followed by a rank for a forward move or by a 'x' for a capture move.");

    checkException("aa", SanValidationProblem.FORMAT_PAWN_WRONG_SECOND_CHARACTER,
        "For a pawn move, the second character must be a rank digit (1-8) or the capture symbol (x), but is 'a'.");

    // pawn forward non promotion
    checkException("e7=", SanValidationProblem.FORMAT_PAWN_FORWARD_NON_PROMOTION_OVERLENGTH,
        "A non promoting pawn forward move must have exactly 2 characters (excluding check/checkmate symbol).");

    // pawn forward promotion
    checkException("e8", SanValidationProblem.FORMAT_PAWN_FORWARD_PROMOTION_NO_PROMOTION_SYMBOL,
        "For a pawn promotion, the promotion symbol '=' is expected after the promotion rank.");

    checkException("e8x", SanValidationProblem.FORMAT_PAWN_FORWARD_PROMOTION_WRONG_PROMOTION_SYMBOL,
        "For a pawn promotion, the promotion symbol '=' is expected after the promotion rank, but is 'x'.");

    checkException("e8=", SanValidationProblem.FORMAT_PAWN_FORWARD_PROMOTION_NO_PROMOTION_PIECE,
        "For a pawn promotion, a promotion piece is required after the promotion symbol '='.");

    checkException("e8=P", SanValidationProblem.FORMAT_PAWN_FORWARD_PROMOTION_WRONG_PROMOTION_PIECE,
        "For a pawn promotion, the promotion piece must be R, N, B, or Q, but is 'P'.");

    checkException("e8=Qx", SanValidationProblem.FORMAT_PAWN_FORWARD_PROMOTION_OVERLENGTH,
        "A promoting pawn forward move must have exactly 4 characters (excluding check/checkmate symbol).");

    // pawn capture
    checkException("ax", SanValidationProblem.FORMAT_PAWN_CAPTURE_NO_FILE,
        "For a pawn capture, a file letter (a-h) is expected after the capture symbol for the destination file, but was not provided.");

    checkException("axQ", SanValidationProblem.FORMAT_PAWN_CAPTURE_WRONG_FILE,
        "For a pawn capture, a file letter (a-h) is expected after the capture symbol for the destination file, but is 'Q'.");

    checkException("axb", SanValidationProblem.FORMAT_PAWN_CAPTURE_NO_RANK,
        "For a pawn capture, a rank digit (1-8) is expected after the destination file for the destination rank, but was not provided.");

    checkException("axbb", SanValidationProblem.FORMAT_PAWN_CAPTURE_WRONG_RANK,
        "For a pawn capture, a rank digit (1-8) is expected after the destination file for the destination rank, but is 'b'.");

    // pawn capture non promotion
    checkException("axb7=", SanValidationProblem.FORMAT_PAWN_CAPTURE_NON_PROMOTION_OVERLENGTH,
        "A non promoting pawn capturing move must have exactly 4 characters (excluding check/checkmate symbol).");

    // pawn capture promotion
    checkException("axb8", SanValidationProblem.FORMAT_PAWN_CAPTURE_PROMOTION_NO_PROMOTION_SYMBOL,
        "For a pawn promotion, the promotion symbol '=' is expected after the promotion rank.");

    checkException("axb8x", SanValidationProblem.FORMAT_PAWN_CAPTURE_PROMOTION_WRONG_PROMOTION_SYMBOL,
        "For a pawn promotion, the promotion symbol '=' is expected after the promotion rank, but is 'x'.");

    checkException("axb8=", SanValidationProblem.FORMAT_PAWN_CAPTURE_PROMOTION_NO_PROMOTION_PIECE,
        "For a pawn promotion, a promotion piece is required after the promotion symbol '='.");

    checkException("axb8=P", SanValidationProblem.FORMAT_PAWN_CAPTURE_PROMOTION_WRONG_PROMOTION_PIECE,
        "For a pawn promotion, the promotion piece must be R, N, B, or Q, but is 'P'.");

    checkException("axb8=Qx", SanValidationProblem.FORMAT_PAWN_CAPTURE_PROMOTION_OVERLENGTH,
        "A promoting pawn capturing move must have exactly 6 characters (excluding check/checkmate symbol).");

  }

  @SuppressWarnings("static-method")
  @Test
  void testFormatKing() {

    // king
    // king castling
    checkException("O-", SanValidationProblem.FORMAT_KING_CASTLING,
        "When the value starts with 'O', it must be either castling king-side (O-O) or castling queen-side (O-O-O).");

    // king non castling
    checkException("K", SanValidationProblem.FORMAT_KING_NON_CASTLING_NO_SECOND_CHARACTER,
        "For a king non-castling move, the king letter must be followed by a file letter (a-h) for a non-capturing move or by the capture symbol (x) for a capturing move.");

    checkException("K=", SanValidationProblem.FORMAT_KING_NON_CASTLING_WRONG_SECOND_CHARACTER,
        "For a king non-castling move, the second character must be a file letter (a-h) or the capture symbol (x), but is '='.");

    // rank-disambiguation attempts like "K2e5" collapse into WRONG_SECOND_CHARACTER
    checkException("K2e5", SanValidationProblem.FORMAT_KING_NON_CASTLING_WRONG_SECOND_CHARACTER,
        "For a king non-castling move, the second character must be a file letter (a-h) or the capture symbol (x), but is '2'.");

    // king non castling non capture
    checkException("Ke", SanValidationProblem.FORMAT_KING_NON_CASTLING_NO_DESTINATION_RANK,
        "For a king non-castling non-capturing move, after the destination file a rank digit (1-8) is expected for the destination rank, but was not provided.");

    checkException("KeR", SanValidationProblem.FORMAT_KING_NON_CASTLING_WRONG_DESTINATION_RANK,
        "For a king non-castling non-capturing move, after the destination file a rank digit (1-8) is expected for the destination rank, but is 'R'.");

    // file-disambiguation attempts like "Kae5" collapse into WRONG_DESTINATION_RANK (third char 'e' is a file, not a
    // rank)
    checkException("Kae5", SanValidationProblem.FORMAT_KING_NON_CASTLING_WRONG_DESTINATION_RANK,
        "For a king non-castling non-capturing move, after the destination file a rank digit (1-8) is expected for the destination rank, but is 'e'.");

    checkException("Ke5R", SanValidationProblem.FORMAT_KING_NON_CASTLING_OVERLENGTH_NON_CAPTURE,
        "A king non-castling non-capturing move must have exactly 3 characters (excluding check/checkmate symbol).");

    // square-disambiguation attempts like "Ka2b3" collapse into OVERLENGTH_NON_CAPTURE
    checkException("Ka2b3", SanValidationProblem.FORMAT_KING_NON_CASTLING_OVERLENGTH_NON_CAPTURE,
        "A king non-castling non-capturing move must have exactly 3 characters (excluding check/checkmate symbol).");

    // king non castling capture
    checkException("Kx", SanValidationProblem.FORMAT_KING_NON_CASTLING_NO_CAPTURE_FILE,
        "For a king non-castling capturing move, after the capture symbol a file letter (a-h) is expected for the destination file, but was not provided.");

    checkException("KxR", SanValidationProblem.FORMAT_KING_NON_CASTLING_WRONG_CAPTURE_FILE,
        "For a king non-castling capturing move, after the capture symbol a file letter (a-h) is expected for the destination file, but is 'R'.");

    checkException("Kxe", SanValidationProblem.FORMAT_KING_NON_CASTLING_NO_CAPTURE_RANK,
        "For a king non-castling capturing move, after the destination file a rank digit (1-8) is expected for the destination rank, but was not provided.");

    checkException("KxeR", SanValidationProblem.FORMAT_KING_NON_CASTLING_WRONG_CAPTURE_RANK,
        "For a king non-castling capturing move, after the destination file a rank digit (1-8) is expected for the destination rank, but is 'R'.");

    checkException("Kxe5a", SanValidationProblem.FORMAT_KING_NON_CASTLING_OVERLENGTH_CAPTURE,
        "A king non-castling capturing move must have exactly 4 characters (excluding check/checkmate symbol).");

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
