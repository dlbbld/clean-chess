package com.dlb.chess.test.fen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.enums.FenAdvancedValidationProblem;
import com.dlb.chess.fen.ForgivenFenItem;
import com.dlb.chess.fen.ForgivenFenItemCode;
import com.dlb.chess.fen.LenientFenParser;
import com.dlb.chess.fen.LenientFenParserValidationProblem;
import com.dlb.chess.fen.LenientFenParserValidationResult;
import com.dlb.chess.fen.constants.FenConstants;
import com.google.common.collect.ImmutableList;

/**
 * One inline fixture per {@link ForgivenFenItemCode} value plus end-to-end and propagation cases. Each fixture
 * is a minimal FEN whose normalisation triggers exactly the named code; the lenient parser must accept the
 * fixture and surface that code (only). The {@code testCanonicalInput*} case asserts the absence of any
 * forgiven items on already-canonical input; the {@code testAdvancedInvalid*} case asserts that a semantically
 * invalid position (king missing) still fails — the lenient layer only forgives syntactic deviations.
 */
@SuppressWarnings("static-method")
class TestLenientFenParser {

  private static final String INITIAL_CANONICAL = FenConstants.FEN_INITIAL_STR;

  @Test
  void test01_canonicalInputEmitsNoForgivenItems() {
    final LenientFenParserValidationResult result = LenientFenParser.validateText(INITIAL_CANONICAL);
    assertTrue(result.isValid());
    assertTrue(result.forgivenItems().isEmpty());
    assertEquals(INITIAL_CANONICAL, result.fen().fen());
  }

  @Test
  void test02_leadingWhitespaceStripped() {
    assertExactlyOneCode("  " + INITIAL_CANONICAL, ForgivenFenItemCode.LEADING_WHITESPACE);
  }

  @Test
  void test03_trailingWhitespaceStripped() {
    assertExactlyOneCode(INITIAL_CANONICAL + "   ", ForgivenFenItemCode.TRAILING_WHITESPACE);
  }

  @Test
  void test04_extraWhitespaceBetweenFieldsCollapsed() {
    final String deviating = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR  w KQkq - 0 1";
    assertExactlyOneCode(deviating, ForgivenFenItemCode.EXTRA_WHITESPACE_BETWEEN_FIELDS);
  }

  @Test
  void test05_tabOrNewlineAsSeparator() {
    final String deviating = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR\tw\tKQkq\t-\t0\t1";
    final LenientFenParserValidationResult result = LenientFenParser.validateText(deviating);
    assertTrue(result.isValid());
    assertTrue(containsCode(result.forgivenItems(), ForgivenFenItemCode.TAB_OR_NEWLINE_AS_SEPARATOR));
  }

  @Test
  void test06_missingHalfmoveAndFullmove() {
    // Four-field FEN as Stockfish UCI emits.
    final String deviating = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq -";
    assertExactlyOneCode(deviating, ForgivenFenItemCode.MISSING_HALFMOVE_AND_FULLMOVE);
  }

  @Test
  void test07_missingFullmoveNumber() {
    // Five-field FEN — halfmove present, fullmove absent.
    final String deviating = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0";
    assertExactlyOneCode(deviating, ForgivenFenItemCode.MISSING_FULLMOVE_NUMBER);
  }

  @Test
  void test08_uppercaseSideToMove() {
    final String deviating = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR W KQkq - 0 1";
    assertExactlyOneCode(deviating, ForgivenFenItemCode.UPPERCASE_SIDE_TO_MOVE);
  }

  @Test
  void test09_castlingNonCanonicalOrder() {
    final String deviating = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w qkQK - 0 1";
    assertExactlyOneCode(deviating, ForgivenFenItemCode.CASTLING_NON_CANONICAL_ORDER);
  }

  @Test
  void test10_enPassantNonStandardDash() {
    // U+2014 EM DASH used for "no en-passant target."
    final String deviating = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq — 0 1";
    assertExactlyOneCode(deviating, ForgivenFenItemCode.EN_PASSANT_NON_STANDARD_DASH);
  }

  @Test
  void test11_enPassantUppercase() {
    // Position after 1. e4 (White's pawn-two-advance from e2 to e4): Black to move, en-passant target on e3.
    // The fixture writes the target as uppercase "E3" to exercise the lenient normalisation; the surrounding
    // piece placement satisfies the strict en-passant preconditions (pawn on e4, e2 empty, e3 empty).
    final String deviating = "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq E3 0 1";
    final LenientFenParserValidationResult result = LenientFenParser.validateText(deviating);
    assertTrue(result.isValid(), () -> "expected valid; got: " + result.message());
    assertTrue(containsCode(result.forgivenItems(), ForgivenFenItemCode.EN_PASSANT_UPPERCASE));
  }

  @Test
  void test12_trailingGarbageToken() {
    final String deviating = INITIAL_CANONICAL + " extra-token-after-fullmove";
    assertExactlyOneCode(deviating, ForgivenFenItemCode.TRAILING_GARBAGE_TOKEN);
  }

  @Test
  void test13_unrecoverableInsufficientFields() {
    // Three fields: cannot recover even with defaulting.
    final String deviating = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq";
    final LenientFenParserValidationResult result = LenientFenParser.validateText(deviating);
    assertFalse(result.isValid());
    assertEquals(LenientFenParserValidationProblem.UNRECOVERABLE, result.problem());
    assertNull(result.fen());
  }

  @Test
  void test14_advancedInvalidPropagates() {
    // Black king missing — strict-semantic failure, lenient does not forgive.
    final String deviating = "rnbq1bnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    final LenientFenParserValidationResult result = LenientFenParser.validateText(deviating);
    assertFalse(result.isValid());
    assertEquals(LenientFenParserValidationProblem.ADVANCED_INVALID, result.problem());
    // The underlying advanced-problem categorisation is carried so callers can switch without parsing message.
    assertFalse(result.fenAdvancedValidationProblem() == FenAdvancedValidationProblem.SUCCESS);
    assertNull(result.fen());
  }

  @Test
  void test15_halfMoveClockInconsistentWithFullMoveNumber() {
    // halfMoveClock=15, fullMoveNumber=1, white-to-move: physically impossible (max=0). Lenient parser bumps
    // fullMoveNumber up to the minimum consistent value (9 for halfMoveClock=15 white-to-move).
    final String deviating = "8/8/8/8/8/8/8/4K2k w - - 15 1";
    final LenientFenParserValidationResult result = LenientFenParser.validateText(deviating);
    assertTrue(result.isValid(), () -> "expected valid; got: " + result.message());
    assertTrue(containsCode(result.forgivenItems(),
        ForgivenFenItemCode.HALF_MOVE_CLOCK_INCONSISTENT_WITH_FULL_MOVE_NUMBER));
    assertEquals(9, result.fen().fullMoveNumber());
    assertEquals(15, result.fen().halfMoveClock());
  }

  @Test
  void test16_endToEndDeficientFen() {
    // Realistic engine-output deficient FEN: leading whitespace, tab separators, uppercase side, non-canonical
    // castling order, em-dash en-passant, missing fullmove (5-field).
    final String deviating = "  rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR\tW\tqkQK\t—\t0";
    final LenientFenParserValidationResult result = LenientFenParser.validateText(deviating);
    assertTrue(result.isValid(), () -> "expected valid; got: " + result.message());
    final ImmutableList<ForgivenFenItem> items = result.forgivenItems();
    assertTrue(containsCode(items, ForgivenFenItemCode.LEADING_WHITESPACE));
    assertTrue(containsCode(items, ForgivenFenItemCode.TAB_OR_NEWLINE_AS_SEPARATOR));
    assertTrue(containsCode(items, ForgivenFenItemCode.UPPERCASE_SIDE_TO_MOVE));
    assertTrue(containsCode(items, ForgivenFenItemCode.CASTLING_NON_CANONICAL_ORDER));
    assertTrue(containsCode(items, ForgivenFenItemCode.EN_PASSANT_NON_STANDARD_DASH));
    assertTrue(containsCode(items, ForgivenFenItemCode.MISSING_FULLMOVE_NUMBER));
    assertEquals(INITIAL_CANONICAL, result.fen().fen());
  }

  // -------------------------------------------------------------------------------------------------
  // Helpers
  // -------------------------------------------------------------------------------------------------

  private static void assertExactlyOneCode(String input, ForgivenFenItemCode expectedCode) {
    final LenientFenParserValidationResult result = LenientFenParser.validateText(input);
    assertTrue(result.isValid(), () -> "expected valid; got: " + result.message());
    final long count = result.forgivenItems().stream().filter(i -> i.code() == expectedCode).count();
    assertEquals(1, count, () -> "expected exactly one " + expectedCode + " item; got: " + result.forgivenItems());
  }

  private static boolean containsCode(ImmutableList<ForgivenFenItem> items, ForgivenFenItemCode code) {
    return items.stream().anyMatch(i -> i.code() == code);
  }
}
