package com.dlb.chess.test.san;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.san.SanValidateFormat;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.san.model.SanParse;
import com.dlb.chess.san.validate.statically.format.calculate.SanValidateStaticallyFormat;

/**
 * Exhaustive completeness test: enumerates every string that can plausibly be a SAN move (drawn from a restricted
 * alphabet, up to MAX_LENGTH characters, with per-category occurrence limits) and asserts that
 * {@link SanValidateFormat#validateFormat} and the pre-built static SAN map
 * ({@link SanValidateStaticallyFormat#getSanValidationMap()}) agree on whether each string is valid.
 *
 * <p>
 * Performance note: this test processes millions of candidates. All hot-path data structures are primitive arrays for
 * O(1) lookup. The generation uses in-place backtracking to avoid allocating intermediate strings. Do not replace these
 * structures with higher-level abstractions.
 */
class TestSanFormatValidationCompleteness {

  // ---------------------------------------------------------------------------
  // Alphabet: every character that may appear in a SAN string (check/checkmate
  // symbols +/# are deliberately excluded because the static map does not
  // include those variants).
  // ---------------------------------------------------------------------------
  private static final char[] ALPHABET = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', // file letters
      '1', '2', '3', '4', '5', '6', '7', '8', // rank digits
      'N', 'B', 'Q', 'K', 'R', // piece letters
      'x', '=' // capture / promotion symbols
  };

  /** Longest SAN string to consider (e.g. "Qc3xe5" = 6 chars). */
  private static final int MAX_LENGTH = 6;

  /** How often to print a progress line to stdout. */
  private static final int PRINT_INTERVAL = 100_000;

  // ---------------------------------------------------------------------------
  // Character categories — every character in ALPHABET belongs to exactly one
  // category. A category has a maximum number of occurrences allowed in any
  // single SAN string; the generator prunes branches that would exceed it.
  // ---------------------------------------------------------------------------
  private static final int CAT_FILE = 0; // a–h (file letters, max 2: from-file + to-file)
  private static final int CAT_DIGIT = 1; // 1–8 (rank digits, max 2: from-rank + to-rank)
  private static final int CAT_RNBQ = 2; // R,N,B,Q (non-king piece letter, max 1)
  private static final int CAT_K = 3; // K (king letter, max 1)
  private static final int CAT_X = 4; // x (capture symbol, max 1)
  private static final int CAT_EQUALS = 5; // = (promotion symbol, max 1)
  private static final int NUM_CATS = 6;

  /** Maximum occurrences per category; indexed by the CAT_* constants above. */
  private static final int[] CAT_MAX = { 2, 2, 1, 1, 1, 1 };

  // ---------------------------------------------------------------------------
  // Fast lookup tables indexed by char value (ASCII only).
  // CHAR_CATEGORY maps a character to its category index (-1 = uncategorised).
  // VALID_FIRST_CHAR marks which characters may legally open a SAN string
  // (file letters a–h, or piece letters R/N/B/Q/K).
  // ---------------------------------------------------------------------------

  /** Maps ASCII char value → category index; -1 for characters not in ALPHABET. */
  private static final int[] CHAR_CATEGORY = new int[128];

  /**
   * {@code true} if the character is a legal first character of a SAN string. SAN strings must begin with a file letter
   * (pawn move) or a piece letter.
   */
  private static final boolean[] VALID_FIRST_CHAR = new boolean[128];

  static {
    Arrays.fill(CHAR_CATEGORY, -1);

    for (final char c : new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' }) {
      CHAR_CATEGORY[c] = CAT_FILE;
      VALID_FIRST_CHAR[c] = true; // pawn move starts with a file letter
    }
    for (final char c : new char[] { '1', '2', '3', '4', '5', '6', '7', '8' }) {
      CHAR_CATEGORY[c] = CAT_DIGIT;
    }
    for (final char c : new char[] { 'R', 'N', 'B', 'Q' }) {
      CHAR_CATEGORY[c] = CAT_RNBQ;
      VALID_FIRST_CHAR[c] = true; // piece move starts with the piece letter
    }
    CHAR_CATEGORY['K'] = CAT_K;
    VALID_FIRST_CHAR['K'] = true; // king move starts with K
    CHAR_CATEGORY['x'] = CAT_X;
    CHAR_CATEGORY['='] = CAT_EQUALS;
  }

  // ---------------------------------------------------------------------------
  // Test entry point
  // ---------------------------------------------------------------------------

  /**
   * For every entry in the static SAN map, asserts that {@link SanValidateFormat#validateFormat} returns the identical
   * {@link SanParse} that the map pre-computed.
   *
   * <p>
   * This uses the static map as an oracle: if the new parser produces a different result for any known-valid SAN string
   * (wrong {@link com.dlb.chess.san.enums.SanType}, wrong from-square, wrong promotion piece, etc.) the test will catch
   * it immediately without having to enumerate strings manually.
   */
  @SuppressWarnings("static-method")
  @Test
  void testConsistencyWithStaticMap() {
    for (final Map.Entry<String, SanParse> entry : NonNullWrapperCommon
        .entrySet(SanValidateStaticallyFormat.getSanValidationMap())) {
      final String san = NonNullWrapperCommon.getKey(entry);
      final SanParse expected = NonNullWrapperCommon.getValue(entry);
      final SanParse actual = SanValidateFormat.validateFormat(san);
      assertEquals(expected, actual, "validateFormat result differs from static map for SAN: \"" + san + "\"");
    }
  }

  // Completeness test done. Total checks: 9737473, total elapsed: 38461 ms
  // TODO currently runs, reenable when tests stable again
  @SuppressWarnings("static-method")
  // @Test
  void testCompleteness() {
    final Set<String> validSanSet = NonNullWrapperCommon.keySet(SanValidateStaticallyFormat.getSanValidationMap());
    final var startTime = System.currentTimeMillis();

    // Single-element array: the idiomatic Java way to hold a mutable long counter
    // that is updated deep inside a recursive method without using instance state.
    final long[] totalChecked = { 0 };

    generateAndCheck(new char[MAX_LENGTH], 0, new int[NUM_CATS], validSanSet, totalChecked, startTime);

    final var totalElapsed = System.currentTimeMillis() - startTime;
    System.out.println(
        "Completeness test done. Total checks: " + totalChecked[0] + ", total elapsed: " + totalElapsed + " ms");
  }

  // ---------------------------------------------------------------------------
  // Core generation + checking
  // ---------------------------------------------------------------------------

  /**
   * Recursively builds all candidate SAN strings by appending one character at a time, pruning branches early when a
   * per-category occurrence limit would be exceeded.
   *
   * <p>
   * The algorithm is a depth-first search with backtracking:
   * <ol>
   * <li>Check the current prefix (if non-empty).</li>
   * <li>For each character in the alphabet, skip it if:
   * <ul>
   * <li>we are at position 0 and the character cannot start a SAN string, or</li>
   * <li>appending it would exceed that character's category limit.</li>
   * </ul>
   * </li>
   * <li>Append the character, recurse, then undo the append (backtrack).</li>
   * </ol>
   *
   * <p>
   * The {@code buffer} and {@code categoryCounts} arrays are mutated in-place to avoid allocating new objects on every
   * recursive call.
   */
  private static void generateAndCheck(final char[] buffer, final int length, final int[] categoryCounts,
      final Set<String> validSanSet, final long[] totalChecked, final long startTime) {

    if (length > 0) {
      checkCandidate(new String(buffer, 0, length), validSanSet, totalChecked, startTime);
    }
    if (length == MAX_LENGTH) {
      return;
    }

    for (final char c : ALPHABET) {
      // SAN strings must start with a file letter (pawn) or a piece letter (R/N/B/Q/K)
      if (length == 0 && !VALID_FIRST_CHAR[c]) {
        continue;
      }
      // Prune: do not exceed the maximum allowed occurrences for this character's category.
      // Note: every character in ALPHABET has a valid category (CHAR_CATEGORY != -1).
      final var category = CHAR_CATEGORY[c];
      if (categoryCounts[category] < CAT_MAX[category]) {
        buffer[length] = c;
        categoryCounts[category]++;
        generateAndCheck(buffer, length + 1, categoryCounts, validSanSet, totalChecked, startTime);
        categoryCounts[category]--; // backtrack
      }
    }
  }

  /**
   * Asserts that {@link SanValidateFormat#validateFormat} and the static SAN map agree on whether {@code san} is a
   * valid SAN string, then updates the progress counter.
   */
  private static void checkCandidate(final String san, final Set<String> validSanSet, final long[] totalChecked,
      final long startTime) {

    boolean isValid;
    try {
      SanValidateFormat.validateFormat(san);
      isValid = true;
    } catch (@SuppressWarnings("unused") final SanValidationException e) {
      isValid = false;
    }

    if (isValid) {
      assertTrue(validSanSet.contains(san),
          "validateFormat accepted \"" + san + "\" but it is absent from the static SAN map");
    } else {
      assertFalse(validSanSet.contains(san),
          "validateFormat rejected \"" + san + "\" but it is present in the static SAN map");
    }

    totalChecked[0]++;
    if (totalChecked[0] % PRINT_INTERVAL == 0) {
      final var elapsed = System.currentTimeMillis() - startTime;
      System.out.println("Processed: " + totalChecked[0] + ", elapsed: " + elapsed + " ms - current SAN: " + san);
    }
  }

}
