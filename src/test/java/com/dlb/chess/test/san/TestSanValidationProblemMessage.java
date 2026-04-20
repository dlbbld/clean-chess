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

    // king non castling non capture — destination file (second character when not 'x')
    checkException("K", SanValidationProblem.FORMAT_KING_NON_CASTLING_NON_CAPTURE_NO_DESTINATION_FILE,
        "For a king non-castling non-capturing move, after the king letter a file letter (a-h) is expected for the destination file, but was not provided.");

    checkException("K=", SanValidationProblem.FORMAT_KING_NON_CASTLING_NON_CAPTURE_WRONG_DESTINATION_FILE,
        "For a king non-castling non-capturing move, after the king letter a file letter (a-h) is expected for the destination file, but is '='.");

    // rank-disambiguation attempts like "K2e5" collapse into NON_CAPTURE_WRONG_DESTINATION_FILE
    checkException("K2e5", SanValidationProblem.FORMAT_KING_NON_CASTLING_NON_CAPTURE_WRONG_DESTINATION_FILE,
        "For a king non-castling non-capturing move, after the king letter a file letter (a-h) is expected for the destination file, but is '2'.");

    // king non castling non capture — destination rank
    checkException("Ke", SanValidationProblem.FORMAT_KING_NON_CASTLING_NON_CAPTURE_NO_DESTINATION_RANK,
        "For a king non-castling non-capturing move, after the destination file a rank digit (1-8) is expected for the destination rank, but was not provided.");

    checkException("KeR", SanValidationProblem.FORMAT_KING_NON_CASTLING_NON_CAPTURE_WRONG_DESTINATION_RANK,
        "For a king non-castling non-capturing move, after the destination file a rank digit (1-8) is expected for the destination rank, but is 'R'.");

    // file-disambiguation attempts like "Kae5" collapse into WRONG_DESTINATION_RANK (third char 'e' is a file, not a
    // rank)
    checkException("Kae5", SanValidationProblem.FORMAT_KING_NON_CASTLING_NON_CAPTURE_WRONG_DESTINATION_RANK,
        "For a king non-castling non-capturing move, after the destination file a rank digit (1-8) is expected for the destination rank, but is 'e'.");

    checkException("Ke5R", SanValidationProblem.FORMAT_KING_NON_CASTLING_NON_CAPTURE_OVERLENGTH,
        "A king non-castling non-capturing move must have exactly 3 characters (excluding check/checkmate symbol).");

    // square-disambiguation attempts like "Ka2b3" collapse into OVERLENGTH_NON_CAPTURE
    checkException("Ka2b3", SanValidationProblem.FORMAT_KING_NON_CASTLING_NON_CAPTURE_OVERLENGTH,
        "A king non-castling non-capturing move must have exactly 3 characters (excluding check/checkmate symbol).");

    // king non castling capture
    checkException("Kx", SanValidationProblem.FORMAT_KING_NON_CASTLING_CAPTURE_NO_DESTINATION_FILE,
        "For a king non-castling capturing move, after the capture symbol a file letter (a-h) is expected for the destination file, but was not provided.");

    checkException("KxR", SanValidationProblem.FORMAT_KING_NON_CASTLING_CAPTURE_WRONG_DESTINATION_FILE,
        "For a king non-castling capturing move, after the capture symbol a file letter (a-h) is expected for the destination file, but is 'R'.");

    checkException("Kxe", SanValidationProblem.FORMAT_KING_NON_CASTLING_CAPTURE_NO_DESTINATION_RANK,
        "For a king non-castling capturing move, after the destination file a rank digit (1-8) is expected for the destination rank, but was not provided.");

    checkException("KxeR", SanValidationProblem.FORMAT_KING_NON_CASTLING_CAPTURE_WRONG_DESTINATION_RANK,
        "For a king non-castling capturing move, after the destination file a rank digit (1-8) is expected for the destination rank, but is 'R'.");

    checkException("Kxe5a", SanValidationProblem.FORMAT_KING_NON_CASTLING_CAPTURE_OVERLENGTH,
        "A king non-castling capturing move must have exactly 4 characters (excluding check/checkmate symbol).");

  }

  @SuppressWarnings("static-method")
  @Test
  void testFormatRnbq() {

    // Cases listed in sequential parse order. The parser branches at each ambiguity point and commits to a
    // disambiguation only when the next character disambiguates (or when the SAN ends and the shorter valid form
    // is complete).

    // --- pos 1: second character ---

    checkException("Q", SanValidationProblem.FORMAT_RNBQ_NO_SECOND_CHARACTER,
        "For a piece move (R, N, B, Q), the piece letter must be followed by a file letter (a-h),"
            + " a rank digit (1-8), or the capture symbol (x).");

    checkException("Q=", SanValidationProblem.FORMAT_RNBQ_WRONG_SECOND_CHARACTER,
        "For a piece move (R, N, B, Q), the second character must be a file letter (a-h),"
            + " a rank digit (1-8), or the capture symbol (x), but is '='.");

    // --- Rx... — capture, no disambiguation ---

    checkException("Qx", SanValidationProblem.FORMAT_RNBQ_CAPTURE_NO_DESTINATION_FILE,
        "For a piece move with no disambiguation and capturing, after the capture symbol a file letter (a-h)"
            + " is expected for the destination file, but was not provided.");

    checkException("Qx=", SanValidationProblem.FORMAT_RNBQ_CAPTURE_WRONG_DESTINATION_FILE,
        "For a piece move with no disambiguation and capturing, after the capture symbol a file letter (a-h)"
            + " is expected for the destination file, but is '='.");

    checkException("Qxe", SanValidationProblem.FORMAT_RNBQ_CAPTURE_NO_DESTINATION_RANK,
        "For a piece move with no disambiguation and capturing, after the destination file a rank digit (1-8)"
            + " is expected for the destination rank, but was not provided.");

    checkException("Qxe=", SanValidationProblem.FORMAT_RNBQ_CAPTURE_WRONG_DESTINATION_RANK,
        "For a piece move with no disambiguation and capturing, after the destination file a rank digit (1-8)"
            + " is expected for the destination rank, but is '='.");

    checkException("Qxe5y", SanValidationProblem.FORMAT_RNBQ_CAPTURE_OVERLENGTH,
        "A piece move with no disambiguation and capturing must have exactly 4 characters"
            + " (excluding check/checkmate symbol).");

    // --- R[rank]... — rank branch (pos 2 expects 'x' or file letter) ---

    checkException("Q2", SanValidationProblem.FORMAT_RNBQ_RANK_NO_THIRD_CHARACTER,
        "After the source-rank digit, a file letter (destination file for a non-capturing move)"
            + " or the capture symbol (x) is expected, but was not provided.");

    checkException("Q2=", SanValidationProblem.FORMAT_RNBQ_RANK_WRONG_THIRD_CHARACTER,
        "After the source-rank digit, a file letter (destination file for a non-capturing move)"
            + " or the capture symbol (x) is expected, but is '='.");

    // R[rank][toFile][toRank] — non-capture rank disambiguation
    checkException("Q2a", SanValidationProblem.FORMAT_RNBQ_NON_CAPTURE_RANK_NO_DESTINATION_RANK,
        "For a piece move with rank disambiguation and non-capturing, after the destination file a rank digit"
            + " (1-8) is expected for the destination rank, but was not provided.");

    checkException("Q2a=", SanValidationProblem.FORMAT_RNBQ_NON_CAPTURE_RANK_WRONG_DESTINATION_RANK,
        "For a piece move with rank disambiguation and non-capturing, after the destination file a rank digit"
            + " (1-8) is expected for the destination rank, but is '='.");

    checkException("Q2a1y", SanValidationProblem.FORMAT_RNBQ_NON_CAPTURE_RANK_OVERLENGTH,
        "A piece move with rank disambiguation and non-capturing must have exactly 4 characters"
            + " (excluding check/checkmate symbol).");

    // R[rank]x[toFile][toRank] — capture rank disambiguation
    checkException("Q2x", SanValidationProblem.FORMAT_RNBQ_CAPTURE_RANK_NO_DESTINATION_FILE,
        "For a piece move with rank disambiguation and capturing, after the capture symbol a file letter (a-h)"
            + " is expected for the destination file, but was not provided.");

    checkException("Q2x=", SanValidationProblem.FORMAT_RNBQ_CAPTURE_RANK_WRONG_DESTINATION_FILE,
        "For a piece move with rank disambiguation and capturing, after the capture symbol a file letter (a-h)"
            + " is expected for the destination file, but is '='.");

    checkException("Q2xa", SanValidationProblem.FORMAT_RNBQ_CAPTURE_RANK_NO_DESTINATION_RANK,
        "For a piece move with rank disambiguation and capturing, after the destination file a rank digit (1-8)"
            + " is expected for the destination rank, but was not provided.");

    checkException("Q2xa=", SanValidationProblem.FORMAT_RNBQ_CAPTURE_RANK_WRONG_DESTINATION_RANK,
        "For a piece move with rank disambiguation and capturing, after the destination file a rank digit (1-8)"
            + " is expected for the destination rank, but is '='.");

    checkException("Q2xa1y", SanValidationProblem.FORMAT_RNBQ_CAPTURE_RANK_OVERLENGTH,
        "A piece move with rank disambiguation and capturing must have exactly 5 characters"
            + " (excluding check/checkmate symbol).");

    // --- R[file]... — file branch (3-way ambiguity at pos 2) ---

    checkException("Qa", SanValidationProblem.FORMAT_RNBQ_FILE_NO_THIRD_CHARACTER,
        "After the file letter, a rank digit (destination rank), another file letter (destination file when"
            + " a source file is being specified), or the capture symbol (x) is expected, but was not provided.");

    checkException("Qa=", SanValidationProblem.FORMAT_RNBQ_FILE_WRONG_THIRD_CHARACTER,
        "After the file letter, a rank digit (destination rank), another file letter (destination file when"
            + " a source file is being specified), or the capture symbol (x) is expected, but is '='.");

    // R[fromFile][toFile][toRank] — non-capture file disambiguation
    checkException("Qab", SanValidationProblem.FORMAT_RNBQ_NON_CAPTURE_FILE_NO_DESTINATION_RANK,
        "For a piece move with file disambiguation and non-capturing, after the destination file a rank digit"
            + " (1-8) is expected for the destination rank, but was not provided.");

    checkException("Qab=", SanValidationProblem.FORMAT_RNBQ_NON_CAPTURE_FILE_WRONG_DESTINATION_RANK,
        "For a piece move with file disambiguation and non-capturing, after the destination file a rank digit"
            + " (1-8) is expected for the destination rank, but is '='.");

    checkException("Qab1y", SanValidationProblem.FORMAT_RNBQ_NON_CAPTURE_FILE_OVERLENGTH,
        "A piece move with file disambiguation and non-capturing must have exactly 4 characters"
            + " (excluding check/checkmate symbol).");

    // R[fromFile]x[toFile][toRank] — capture file disambiguation
    checkException("Qax", SanValidationProblem.FORMAT_RNBQ_CAPTURE_FILE_NO_DESTINATION_FILE,
        "For a piece move with file disambiguation and capturing, after the capture symbol a file letter (a-h)"
            + " is expected for the destination file, but was not provided.");

    checkException("Qax=", SanValidationProblem.FORMAT_RNBQ_CAPTURE_FILE_WRONG_DESTINATION_FILE,
        "For a piece move with file disambiguation and capturing, after the capture symbol a file letter (a-h)"
            + " is expected for the destination file, but is '='.");

    checkException("Qaxb", SanValidationProblem.FORMAT_RNBQ_CAPTURE_FILE_NO_DESTINATION_RANK,
        "For a piece move with file disambiguation and capturing, after the destination file a rank digit (1-8)"
            + " is expected for the destination rank, but was not provided.");

    checkException("Qaxb=", SanValidationProblem.FORMAT_RNBQ_CAPTURE_FILE_WRONG_DESTINATION_RANK,
        "For a piece move with file disambiguation and capturing, after the destination file a rank digit (1-8)"
            + " is expected for the destination rank, but is '='.");

    checkException("Qaxb1y", SanValidationProblem.FORMAT_RNBQ_CAPTURE_FILE_OVERLENGTH,
        "A piece move with file disambiguation and capturing must have exactly 5 characters"
            + " (excluding check/checkmate symbol).");

    // --- R[file][rank]... — ambiguous: Ra1 (length 3, valid) or source-square prefix (length > 3) ---

    // When a fourth char is present we commit to source-square. It must be 'x' (capture) or a file letter.
    checkException("Qa1=", SanValidationProblem.FORMAT_RNBQ_SQUARE_WRONG_THIRD_CHARACTER,
        "After the source square, a file letter (destination file) or the capture symbol (x) is expected,"
            + " but is '='.");

    // R[fromFile][fromRank][toFile][toRank] — non-capture square disambiguation
    checkException("Qa1b", SanValidationProblem.FORMAT_RNBQ_NON_CAPTURE_SQUARE_NO_DESTINATION_RANK,
        "For a piece move with square disambiguation and non-capturing, after the destination file a rank digit"
            + " (1-8) is expected for the destination rank, but was not provided.");

    checkException("Qa1b=", SanValidationProblem.FORMAT_RNBQ_NON_CAPTURE_SQUARE_WRONG_DESTINATION_RANK,
        "For a piece move with square disambiguation and non-capturing, after the destination file a rank digit"
            + " (1-8) is expected for the destination rank, but is '='.");

    checkException("Qa1b2y", SanValidationProblem.FORMAT_RNBQ_NON_CAPTURE_SQUARE_OVERLENGTH,
        "A piece move with square disambiguation and non-capturing must have exactly 5 characters"
            + " (excluding check/checkmate symbol).");

    // R[fromFile][fromRank]x[toFile][toRank] — capture square disambiguation
    checkException("Qa1x", SanValidationProblem.FORMAT_RNBQ_CAPTURE_SQUARE_NO_DESTINATION_FILE,
        "For a piece move with square disambiguation and capturing, after the capture symbol a file letter (a-h)"
            + " is expected for the destination file, but was not provided.");

    checkException("Qa1x=", SanValidationProblem.FORMAT_RNBQ_CAPTURE_SQUARE_WRONG_DESTINATION_FILE,
        "For a piece move with square disambiguation and capturing, after the capture symbol a file letter (a-h)"
            + " is expected for the destination file, but is '='.");

    checkException("Qa1xb", SanValidationProblem.FORMAT_RNBQ_CAPTURE_SQUARE_NO_DESTINATION_RANK,
        "For a piece move with square disambiguation and capturing, after the destination file a rank digit"
            + " (1-8) is expected for the destination rank, but was not provided.");

    checkException("Qa1xb=", SanValidationProblem.FORMAT_RNBQ_CAPTURE_SQUARE_WRONG_DESTINATION_RANK,
        "For a piece move with square disambiguation and capturing, after the destination file a rank digit"
            + " (1-8) is expected for the destination rank, but is '='.");

    checkException("Qa1xb2y", SanValidationProblem.FORMAT_RNBQ_CAPTURE_SQUARE_OVERLENGTH,
        "A piece move with square disambiguation and capturing must have exactly 6 characters"
            + " (excluding check/checkmate symbol).");

  }

  @SuppressWarnings("static-method")
  @Test
  void testNonMovementRnbq() {
    checkException("Rd4d4", SanValidationProblem.NON_MOVEMENT_RNBQ_SOURCE_SQUARE_EQUALS_DESTINATION_SQUARE,
        "For a movement to be valid, the source square cannot be the same as the destination square.");

  }

  @SuppressWarnings("static-method")
  @Test
  void testMovementPawn() {
    checkException("d2", new Board(), SanValidationProblem.MOVEMENT_PAWN_FORWARD_BACKWARDS,
        "A white pawn can never move to rank 2 or 1 as pawns cannot move backwards.");

    checkException("fxh4", new Board(), SanValidationProblem.MOVEMENT_PAWN_CAPTURE_NON_ADJACENT_FILE,
        "A pawn can never capture on a non-adjacent file, only on adjacent files.");

  }

  @SuppressWarnings("static-method")
  @Test
  void testMovementRnbq() {
    checkException("Baa1", SanValidationProblem.MOVEMENT_RNBQ_FROM_FILE,
        "A bishop can never move from file a to square a1. A bishop can only move diagonally.");

    checkException("N2a2", SanValidationProblem.MOVEMENT_RNBQ_FROM_RANK,
        "A knight can never move from rank 2 to square a2. A knight can only move in an L-shape.");

    checkException("Qa1c2", SanValidationProblem.MOVEMENT_RNBQ_FROM_SQUARE,
        "A queen can never move from square a1 to square c2. A queen can only move orthogonally or diagonally.");

  }

  @SuppressWarnings("static-method")
  @Test
  void testExistsPawn() {
    final Board board = new Board();
    board.performMoves("a4", "h6", "a5", "h5", "a6", "h4", "axb7", "h3");
    checkException("a4", board, SanValidationProblem.EXISTS_PAWN, "There is no white pawn on file a.");
  }

  @SuppressWarnings("static-method")
  @Test
  void testExistsRnbq() {
    final Board board = new Board();
    board.performMoves("b3", "g6", "g3", "Bg7", "Na3", "Bxa1", "Nb1", "b6", "Na3", "Bb7", "Nb1", "Bxh1");
    checkException("Ra2", board, SanValidationProblem.EXISTS_RNBQ_NEITHER, "There is no white rook on the board.");
    checkException("Nac3", SanValidationProblem.EXISTS_RNBQ_FILE, "There is no white knight on file a.");
    checkException("Q3d4", SanValidationProblem.EXISTS_RNBQ_RANK, "There is no white queen on rank 3.");
    checkException("Bh5xf7+", SanValidationProblem.EXISTS_RNBQ_SQUARE, "There is no white bishop on square h5.");
  }

  @SuppressWarnings("static-method")
  @Test
  void testDestinationPawn() {
    // DESTINATION_PAWN_FORWARD_OWN_PIECE: after Nf3 Nf6, white's f-pawn tries to advance to f3 blocked by own knight
    {
      final Board board = new Board();
      board.performMoves("Nf3", "Nf6");
      checkException("f3", board, SanValidationProblem.DESTINATION_PAWN_FORWARD_OWN_PIECE,
          "The pawn cannot move forward to square f3 because it is occupied by an own piece.");
    }

    // DESTINATION_PAWN_FORWARD_OPPONENT_PIECE_KING: white pawn e4, black king e5, white tries e5
    {
      final ApiBoard board = new Board("8/8/8/4k3/4P3/8/8/K7 w - - 0 1");
      checkException("e5", board, SanValidationProblem.DESTINATION_PAWN_FORWARD_OPPONENT_PIECE_KING,
          "The pawn cannot move forward to square e5 because it is occupied by the opponent king (the king cannot be captured, and pawns cannot capture by moving forward).");
    }

    // DESTINATION_PAWN_FORWARD_OPPONENT_PIECE_NOT_KING: after d4 d5, white's d-pawn tries d5 blocked by black pawn
    {
      final Board board = new Board();
      board.performMoves("d4", "d5");
      checkException("d5", board, SanValidationProblem.DESTINATION_PAWN_FORWARD_OPPONENT_PIECE_NOT_KING,
          "The pawn cannot move forward to square d5 because it is occupied by an opponent piece; pawns cannot capture by moving forward.");
    }

    // DESTINATION_PAWN_CAPTURE_OWN_PIECE: after Nc3 a6, white's b-pawn tries bxc3 onto own knight
    {
      final Board board = new Board();
      board.performMoves("Nc3", "a6");
      checkException("bxc3", board, SanValidationProblem.DESTINATION_PAWN_CAPTURE_OWN_PIECE,
          "The pawn cannot capture on square c3 because it is occupied by an own piece.");
    }

    // DESTINATION_PAWN_CAPTURE_KING: legal position — white pawn e2 doesn't threaten d5; SAN "exd5" would target
    // d5 where the black king sits. Movement layer passes (file change adjacent, rank progression OK), exists check
    // passes (pawn on e-file), destination check fires because d5 has opponent king.
    {
      final ApiBoard board = new Board("7K/8/8/3k4/8/8/4P3/8 w - - 0 1");
      checkException("exd5", board, SanValidationProblem.DESTINATION_PAWN_CAPTURE_KING,
          "The pawn cannot capture the opponent king on d5; the king cannot be captured.");
    }

    // DESTINATION_PAWN_CAPTURE_EMPTY_NOT_EN_PASSANT: from initial position, dxe3 with e3 empty and no EP target
    checkException("dxe3", SanValidationProblem.DESTINATION_PAWN_CAPTURE_EMPTY_NOT_EN_PASSANT,
        "A pawn diagonal capture requires an opponent piece on the destination square (or a valid en passant capture target); the destination square e3 is empty and no en passant capture applies.");
  }

  @SuppressWarnings("static-method")
  @Test
  void testDestinationRnbqkOwnPiece() {
    checkException("Na1", SanValidationProblem.DESTINATION_RNBQK_OWN_PIECE_NON_CAPTURING,
        "The move to square a1 is not possible because it is occupied by an own piece.");

    checkException("Nxa1", SanValidationProblem.DESTINATION_RNBQK_OWN_PIECE_CAPTURING,
        "Capturing on square a1 is not possible because it is occupied by an own piece.");

  }

  @SuppressWarnings("static-method")
  @Test
  void testDestinationRnbqkKing() {
    checkException("Re8", SanValidationProblem.DESTINATION_RNBQK_OPPONENT_KING_NON_CAPTURING,
        "The move to square e8 is not possible because it is occupied by the opponent king.");

    checkException("Rxe8", SanValidationProblem.DESTINATION_RNBQK_OPPONENT_KING_CAPTURING,
        "The opponent king can never be captured.");

  }

  @SuppressWarnings("static-method")
  @Test
  void testCaptureSymbol() {

    {
      final Board board = new Board();
      board.performMoves("Nc3", "e6", "Nb5", "e5");

      checkException("Na7", board,
          SanValidationProblem.DESTINATION_RNBQK_OPPONENT_NON_KING_NO_CAPTURE_SYMBOL,
          "The move captures an opponent piece on square a7 but has not capture symbol.");
    }

    checkException("Nxc3", SanValidationProblem.DESTINATION_RNBQK_EMPTY_CAPTURE_SYMBOL,
        "The move is designated as a capture by the capture symbol, but the destination square c3 is empty.");
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
