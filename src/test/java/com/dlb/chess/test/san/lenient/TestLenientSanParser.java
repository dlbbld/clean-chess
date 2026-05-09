package com.dlb.chess.test.san.lenient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.ucimove.utility.UciMoveUtility;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.model.UciMove;
import com.dlb.chess.san.enums.LenientSanValidationProblem;
import com.dlb.chess.san.exceptions.LenientSanParserValidationException;
import com.dlb.chess.san.model.ForgivenItem;
import com.dlb.chess.san.model.LenientSanParserValidationResult;

@SuppressWarnings("static-method")
class TestLenientSanParser implements EnumConstants {

  // Italian-game opening that exercises pawn pushes, knight/bishop development, and castling.
  // Castling is the only move whose canonical SAN, LAN, and UCI representations all differ — the rest
  // give us pawn vs piece coverage in each notation form.
  private static final String[] ITALIAN_OPENING_SAN = { "e4", "e5", "Nf3", "Nc6", "Bc4", "Bc5", "O-O", "Nf6", "d3",
      "d6" };

  // ---------------------------------------------------------------------------
  // Three full-game tests (canonical SAN / UCI / LAN end-to-end).
  // ---------------------------------------------------------------------------

  @Test
  void testCanonicalSanGameProducesNoForgivenItems() {
    final Board board = new Board();
    for (final String san : ITALIAN_OPENING_SAN) {
      final LenientSanParserValidationResult result = board.performMoveLenient(san);
      assertTrue(result.forgivenItems().isEmpty(), "Expected no forgiven items for canonical SAN move: " + san
          + " (got " + result.forgivenItems() + ")");
    }
  }

  @Test
  void testGameInUciNotation() {
    final String[] uciMoves = computeUciForms(ITALIAN_OPENING_SAN);
    final Board board = new Board();
    var sawUciCode = false;
    for (var i = 0; i < uciMoves.length; i++) {
      final String uci = uciMoves[i];
      final LenientSanParserValidationResult result = board.performMoveLenient(uci);
      if (containsCode(result, LenientSanValidationProblem.UCI_NOTATION)) {
        sawUciCode = true;
      }
    }
    assertTrue(sawUciCode, "Expected at least one UCI_NOTATION forgiven item across the UCI-form game");
  }

  @Test
  void testGameInLanNotation() {
    // Use the project's getLan() output, then ensure pawn-forward moves get a hyphen so they're
    // unambiguously LAN (not UCI-equivalent).
    final String[] sanMoves = ITALIAN_OPENING_SAN;
    final Board ref = new Board();
    final String[] lanMoves = new String[sanMoves.length];
    for (var i = 0; i < sanMoves.length; i++) {
      ref.performMove(sanMoves[i]);
      lanMoves[i] = toUnambiguousLan(ref.getLan());
    }

    final Board board = new Board();
    var sawLongAlgebraic = false;
    for (var i = 0; i < lanMoves.length; i++) {
      final LenientSanParserValidationResult result = board.performMoveLenient(lanMoves[i]);
      if (containsCode(result, LenientSanValidationProblem.LONG_ALGEBRAIC_NOTATION)) {
        sawLongAlgebraic = true;
      }
    }
    assertTrue(sawLongAlgebraic, "Expected at least one LONG_ALGEBRAIC_NOTATION forgiven item across the LAN-form game");
  }

  // ---------------------------------------------------------------------------
  // Per-code tests (one per LenientSanValidationProblem value).
  // ---------------------------------------------------------------------------

  @Test
  void testMissingCheckSuffix() {
    // After 1.e4 e5 2.Bc4 Nc6, white's Bxf7+ is check (not mate).
    final Board board = new Board();
    board.performMove("e4");
    board.performMove("e5");
    board.performMove("Bc4");
    board.performMove("Nc6");
    final LenientSanParserValidationResult result = board.performMoveLenient("Bxf7");
    assertExactlyOneCode(result, LenientSanValidationProblem.MISSING_CHECK_SUFFIX);
    assertEquals("Bxf7+", canonical(result));
  }

  @Test
  void testMissingCheckmateSuffix() {
    // Back-rank mate: white rook to a8 mates black king on g8.
    final Board board = new Board("6k1/5ppp/8/8/8/8/8/R6K w - - 0 1");
    final LenientSanParserValidationResult result = board.performMoveLenient("Ra8");
    assertExactlyOneCode(result, LenientSanValidationProblem.MISSING_CHECKMATE_SUFFIX);
    assertEquals("Ra8#", canonical(result));
  }

  @Test
  void testSpuriousCheckSuffix() {
    // 1.e4+ — pawn push that's not a check.
    final Board board = new Board();
    final LenientSanParserValidationResult result = board.performMoveLenient("e4+");
    assertExactlyOneCode(result, LenientSanValidationProblem.SPURIOUS_CHECK_SUFFIX);
    assertEquals("e4", canonical(result));
  }

  @Test
  void testSpuriousCheckmateSuffix() {
    // 1.e4# — pawn push that's not mate.
    final Board board = new Board();
    final LenientSanParserValidationResult result = board.performMoveLenient("e4#");
    assertExactlyOneCode(result, LenientSanValidationProblem.SPURIOUS_CHECKMATE_SUFFIX);
    assertEquals("e4", canonical(result));
  }

  @Test
  void testWrongCheckSuffixForCheckmate() {
    // Back-rank mate written with + instead of #.
    final Board board = new Board("6k1/5ppp/8/8/8/8/8/R6K w - - 0 1");
    final LenientSanParserValidationResult result = board.performMoveLenient("Ra8+");
    assertExactlyOneCode(result, LenientSanValidationProblem.WRONG_CHECK_SUFFIX_FOR_CHECKMATE);
    assertEquals("Ra8#", canonical(result));
  }

  @Test
  void testWrongCheckmateSuffixForCheck() {
    // Bxf7 in Italian is check, not mate. Written with # instead of +.
    final Board board = new Board();
    board.performMove("e4");
    board.performMove("e5");
    board.performMove("Bc4");
    board.performMove("Nc6");
    final LenientSanParserValidationResult result = board.performMoveLenient("Bxf7#");
    assertExactlyOneCode(result, LenientSanValidationProblem.WRONG_CHECKMATE_SUFFIX_FOR_CHECK);
    assertEquals("Bxf7+", canonical(result));
  }

  @Test
  void testMissingCaptureMarker() {
    // After 1.e4 e5 2.Nf3 d6 3.Nxe5 — knight capture on e5. Lenient: "Ne5" without x.
    final Board board = new Board();
    board.performMove("e4");
    board.performMove("e5");
    board.performMove("Nf3");
    board.performMove("d6");
    final LenientSanParserValidationResult result = board.performMoveLenient("Ne5");
    assertExactlyOneCode(result, LenientSanValidationProblem.MISSING_CAPTURE_MARKER);
    assertEquals("Nxe5", canonical(result));
  }

  @Test
  void testSpuriousCaptureMarker() {
    // 1.e4 e5 2.Bxc4 — bishop to c4 is not a capture (c4 is empty).
    final Board board = new Board();
    board.performMove("e4");
    board.performMove("e5");
    final LenientSanParserValidationResult result = board.performMoveLenient("Bxc4");
    assertExactlyOneCode(result, LenientSanValidationProblem.SPURIOUS_CAPTURE_MARKER);
    assertEquals("Bc4", canonical(result));
  }

  @Test
  void testOverspecifiedFileDisambiguation() {
    // After 1.e4, black plays "Nbc6" — only Nb8 can reach c6, file disambig is unnecessary.
    final Board board = new Board();
    board.performMove("e4");
    final LenientSanParserValidationResult result = board.performMoveLenient("Nbc6");
    assertExactlyOneCode(result, LenientSanValidationProblem.OVERSPECIFIED_FILE_DISAMBIGUATION);
    assertEquals("Nc6", canonical(result));
  }

  @Test
  void testOverspecifiedRankDisambiguation() {
    // Single white knight on d5; "N5e7" is rank-disambiguated but rank not necessary.
    final Board board = new Board("4k3/8/8/3N4/8/8/P7/4K3 w - - 0 1");
    final LenientSanParserValidationResult result = board.performMoveLenient("N5e7");
    assertExactlyOneCode(result, LenientSanValidationProblem.OVERSPECIFIED_RANK_DISAMBIGUATION);
    assertEquals("Ne7", canonical(result));
  }

  @Test
  void testNonStandardRankDisambiguation() {
    // Two white rooks (d1 and a4); both can reach a1, but only the d1 rook is on rank 1.
    // Canonical SAN: "Rda1" (file disambig). User input "R1a1" (rank disambig) uniquely identifies the move
    // but is non-canonical — strict throws NON_STANDARD_SPECIFIED_RNBQ_RANK_INSTEAD_OF_FILE; lenient resolves to
    // "Rda1" by board lookup.
    final Board board = new Board("7k/8/8/8/R7/8/8/3R3K w - - 0 1");
    final LenientSanParserValidationResult result = board.performMoveLenient("R1a1");
    assertExactlyOneCode(result, LenientSanValidationProblem.NON_STANDARD_RANK_DISAMBIGUATION);
    assertEquals("Rda1", canonical(result));
  }

  @Test
  void testOverspecifiedSquareDisambiguation() {
    // Single white knight on d5; "Nd5e7" is square-disambiguated (both file and rank unnecessary).
    final Board board = new Board("4k3/8/8/3N4/8/8/P7/4K3 w - - 0 1");
    final LenientSanParserValidationResult result = board.performMoveLenient("Nd5e7");
    assertExactlyOneCode(result, LenientSanValidationProblem.OVERSPECIFIED_SQUARE_DISAMBIGUATION);
    assertEquals("Ne7", canonical(result));
  }

  @Test
  void testLongAlgebraicNotation() {
    // 1.e2-e4 — pawn move with explicit from-square and hyphen.
    final Board board = new Board();
    final LenientSanParserValidationResult result = board.performMoveLenient("e2-e4");
    assertContainsCode(result, LenientSanValidationProblem.LONG_ALGEBRAIC_NOTATION);
    assertEquals("e4", canonical(result));
  }

  @Test
  void testUciNotation() {
    // 1.e2e4 — pawn UCI form (no hyphen, no piece letter).
    final Board board = new Board();
    final LenientSanParserValidationResult result = board.performMoveLenient("e2e4");
    assertExactlyOneCode(result, LenientSanValidationProblem.UCI_NOTATION);
    assertEquals("e4", canonical(result));
  }

  @Test
  void testZeroInsteadOfOCastling() {
    // Set up castle-eligible position and play 0-0.
    final Board board = new Board();
    board.performMove("e4");
    board.performMove("e5");
    board.performMove("Nf3");
    board.performMove("Nc6");
    board.performMove("Bc4");
    board.performMove("Bc5");
    final LenientSanParserValidationResult result = board.performMoveLenient("0-0");
    assertExactlyOneCode(result, LenientSanValidationProblem.ZERO_INSTEAD_OF_O_CASTLING);
    assertEquals("O-O", canonical(result));
  }

  @Test
  void testExplicitPawnLetter() {
    // 1.Pe4 — explicit pawn letter prefix.
    final Board board = new Board();
    final LenientSanParserValidationResult result = board.performMoveLenient("Pe4");
    assertExactlyOneCode(result, LenientSanValidationProblem.EXPLICIT_PAWN_LETTER);
    assertEquals("e4", canonical(result));
  }

  @Test
  void testMissingPromotionEquals() {
    // White pawn on a7, plays a8Q (no = symbol).
    final Board board = new Board("8/P7/1k6/8/8/8/8/4K3 w - - 0 1");
    final LenientSanParserValidationResult result = board.performMoveLenient("a8Q");
    assertExactlyOneCode(result, LenientSanValidationProblem.MISSING_PROMOTION_EQUALS);
    assertEquals("a8=Q", canonical(result));
  }

  @Test
  void testLowercasePieceLetter() {
    // 1.nf3 — lowercase knight letter.
    final Board board = new Board();
    final LenientSanParserValidationResult result = board.performMoveLenient("nf3");
    assertExactlyOneCode(result, LenientSanValidationProblem.LOWERCASE_PIECE_LETTER);
    assertEquals("Nf3", canonical(result));
  }

  @Test
  void testUppercaseFileLetter() {
    // 1.NF3 — uppercase file letter.
    final Board board = new Board();
    final LenientSanParserValidationResult result = board.performMoveLenient("NF3");
    assertExactlyOneCode(result, LenientSanValidationProblem.UPPERCASE_FILE_LETTER);
    assertEquals("Nf3", canonical(result));
  }

  @Test
  void testUppercaseCaptureMarker() {
    // 1.e4 e5 2.Nf3 d6 3.NXe5 — uppercase X.
    final Board board = new Board();
    board.performMove("e4");
    board.performMove("e5");
    board.performMove("Nf3");
    board.performMove("d6");
    final LenientSanParserValidationResult result = board.performMoveLenient("NXe5");
    assertExactlyOneCode(result, LenientSanValidationProblem.UPPERCASE_CAPTURE_MARKER);
    assertEquals("Nxe5", canonical(result));
  }

  @Test
  void testLowercasePromotionPiece() {
    // White pawn on a7, plays a8=q (lowercase q).
    final Board board = new Board("8/P7/1k6/8/8/8/8/4K3 w - - 0 1");
    final LenientSanParserValidationResult result = board.performMoveLenient("a8=q");
    assertExactlyOneCode(result, LenientSanValidationProblem.LOWERCASE_PROMOTION_PIECE);
    assertEquals("a8=Q", canonical(result));
  }

  // ---------------------------------------------------------------------------
  // Combination tests — multiple codes on one move.
  // ---------------------------------------------------------------------------

  @Test
  void testCombinationLowercasePieceAndOverspecifiedFile() {
    // After 1.e4, black plays "nbc6" — lowercase n + unnecessary file disambig.
    final Board board = new Board();
    board.performMove("e4");
    final LenientSanParserValidationResult result = board.performMoveLenient("nbc6");
    assertContainsCode(result, LenientSanValidationProblem.LOWERCASE_PIECE_LETTER);
    assertContainsCode(result, LenientSanValidationProblem.OVERSPECIFIED_FILE_DISAMBIGUATION);
    assertEquals("Nc6", canonical(result));
  }

  @Test
  void testCombinationZeroCastlingAndCheckSuffix() {
    // White castles kingside; the move isn't check, but written 0-0+ tests both forgiveness paths.
    final Board board = new Board();
    board.performMove("e4");
    board.performMove("e5");
    board.performMove("Nf3");
    board.performMove("Nc6");
    board.performMove("Bc4");
    board.performMove("Bc5");
    final LenientSanParserValidationResult result = board.performMoveLenient("0-0+");
    assertContainsCode(result, LenientSanValidationProblem.ZERO_INSTEAD_OF_O_CASTLING);
    assertContainsCode(result, LenientSanValidationProblem.SPURIOUS_CHECK_SUFFIX);
    assertEquals("O-O", canonical(result));
  }

  @Test
  void testCombinationUciAndOverspecifiedSquare() {
    // 1.g1f3 — UCI knight move. After UCI translation gives "Ng1f3"; Phase 2 strips overspec to "Nf3".
    final Board board = new Board();
    final LenientSanParserValidationResult result = board.performMoveLenient("g1f3");
    assertContainsCode(result, LenientSanValidationProblem.UCI_NOTATION);
    assertContainsCode(result, LenientSanValidationProblem.OVERSPECIFIED_SQUARE_DISAMBIGUATION);
    assertEquals("Nf3", canonical(result));
  }

  // ---------------------------------------------------------------------------
  // Hard rejection tests.
  // ---------------------------------------------------------------------------

  @Test
  void testRejectMixedZeroAndOCastling() {
    final Board board = new Board();
    board.performMove("e4");
    board.performMove("e5");
    board.performMove("Nf3");
    board.performMove("Nc6");
    board.performMove("Bc4");
    board.performMove("Bc5");
    assertThrows(LenientSanParserValidationException.class, () -> board.performMoveLenient("0-O"));
  }

  @Test
  void testRejectGarbageInput() {
    final Board board = new Board();
    assertThrows(LenientSanParserValidationException.class, () -> board.performMoveLenient("xyz"));
  }

  @Test
  void testRejectIllegalMove() {
    // From initial position, white cannot play Nf6 (no knight can reach f6 in one move).
    final Board board = new Board();
    assertThrows(LenientSanParserValidationException.class, () -> board.performMoveLenient("Nf6"));
  }

  @Test
  void testRejectBlankInput() {
    final Board board = new Board();
    assertThrows(LenientSanParserValidationException.class, () -> board.performMoveLenient(""));
  }

  // ---------------------------------------------------------------------------
  // Helpers.
  // ---------------------------------------------------------------------------

  private static String[] computeUciForms(String[] sanMoves) {
    final Board ref = new Board();
    final String[] uci = new String[sanMoves.length];
    for (var i = 0; i < sanMoves.length; i++) {
      ref.performMove(sanMoves[i]);
      final LegalMove last = ref.getLastMove();
      final UciMove uciMove = UciMoveUtility.convertMoveSpecificationToUci(last.havingMove(), last.moveSpecification());
      uci[i] = uciMove.text();
    }
    return uci;
  }

  private static String toUnambiguousLan(String lanForm) {
    // Castling: same in LAN and SAN.
    if (lanForm.startsWith("O")) {
      return lanForm;
    }
    // Pawn forward (length 4, file/rank/file/rank) is identical to UCI; insert a hyphen to disambiguate.
    if (lanForm.length() == 4 && isFileLetter(lanForm.charAt(0)) && isRankDigit(lanForm.charAt(1))
        && isFileLetter(lanForm.charAt(2)) && isRankDigit(lanForm.charAt(3))) {
      return lanForm.substring(0, 2) + "-" + lanForm.substring(2);
    }
    // Other LAN forms (piece letter present, or pawn capture with x) are already distinct from UCI.
    return lanForm;
  }

  private static boolean isFileLetter(char c) {
    return c >= 'a' && c <= 'h';
  }

  private static boolean isRankDigit(char c) {
    return c >= '1' && c <= '8';
  }

  private static boolean containsCode(LenientSanParserValidationResult result, LenientSanValidationProblem code) {
    return result.forgivenItems().stream().anyMatch(item -> item.code() == code);
  }

  private static void assertContainsCode(LenientSanParserValidationResult result, LenientSanValidationProblem code) {
    assertTrue(containsCode(result, code),
        "Expected forgiven items to contain " + code + " but got " + result.forgivenItems());
  }

  private static void assertExactlyOneCode(LenientSanParserValidationResult result,
      LenientSanValidationProblem expectedCode) {
    assertEquals(1, result.forgivenItems().size(),
        "Expected exactly one forgiven item with code " + expectedCode + " but got " + result.forgivenItems());
    final ForgivenItem item = result.forgivenItems().get(0);
    assertEquals(expectedCode, item.code(),
        "Expected forgiven code " + expectedCode + " but got " + item.code());
  }

  private static String canonical(LenientSanParserValidationResult result) {
    assertFalse(result.forgivenItems().isEmpty(),
        "Cannot extract canonical SAN from a result with no forgiven items");
    return result.forgivenItems().get(0).canonicalSan();
  }
}
