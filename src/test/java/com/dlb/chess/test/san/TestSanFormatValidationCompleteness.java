package com.dlb.chess.test.san;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.dlb.chess.san.SanValidateFormat;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.san.validate.statically.format.calculate.SanValidateStaticallyFormat;

class TestSanFormatValidationCompleteness {

  private static final char[] ALPHABET = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', '1', '2', '3', '4', '5', '6', '7',
      '8', 'N', 'B', 'Q', 'K', 'R', 'x', '=' };

  private static final int MAX_LENGTH = 6;
  private static final int PRINT_INTERVAL = 100_000;

  // category indices
  private static final int CAT_FILE = 0;
  private static final int CAT_DIGIT = 1;
  private static final int CAT_RNBQ = 2;
  private static final int CAT_K = 3;
  private static final int CAT_X = 4;
  private static final int CAT_EQUALS = 5;
  private static final int NUM_CATS = 6;

  // maximum allowed occurrences per category
  private static final int[] CAT_MAX = { 2, 2, 1, 1, 1, 1 };

  // ASCII lookup table: character → category index
  private static final int[] CHAR_CATEGORY = new int[128];

  static {
    Arrays.fill(CHAR_CATEGORY, -1);
    for (final char c : new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' }) {
      CHAR_CATEGORY[c] = CAT_FILE;
    }
    for (final char c : new char[] { '1', '2', '3', '4', '5', '6', '7', '8' }) {
      CHAR_CATEGORY[c] = CAT_DIGIT;
    }
    for (final char c : new char[] { 'R', 'N', 'B', 'Q' }) {
      CHAR_CATEGORY[c] = CAT_RNBQ;
    }
    CHAR_CATEGORY['K'] = CAT_K;
    CHAR_CATEGORY['x'] = CAT_X;
    CHAR_CATEGORY['='] = CAT_EQUALS;
  }

  @SuppressWarnings("static-method")
  @Test
  void testCompleteness() {
    final Set<String> staticKeySet = SanValidateStaticallyFormat.getSanValidationMap().keySet();
    final var startTime = System.currentTimeMillis();
    final long[] count = { 0 };

    generate(new char[MAX_LENGTH], 0, new int[NUM_CATS], staticKeySet, count, startTime);

    final var totalElapsed = System.currentTimeMillis() - startTime;
    System.out
        .println("Completeness test done. Total checks: " + count[0] + ", total elapsed: " + totalElapsed + " ms");
  }

  private static void generate(char[] buf, int pos, int[] cats, Set<String> staticKeySet, long[] count,
      long startTime) {
    if (pos > 0) {
      check(new String(buf, 0, pos), staticKeySet, count, startTime);
    }
    if (pos == MAX_LENGTH) {
      return;
    }
    for (final char c : ALPHABET) {
      if (pos == 0 && CHAR_CATEGORY[c] != CAT_FILE && CHAR_CATEGORY[c] != CAT_RNBQ && CHAR_CATEGORY[c] != CAT_K) {
        continue;
      }
      final var cat = CHAR_CATEGORY[c];
      if (cats[cat] < CAT_MAX[cat]) {
        buf[pos] = c;
        cats[cat]++;
        generate(buf, pos + 1, cats, staticKeySet, count, startTime);
        cats[cat]--;
      }
    }
  }

  private static void check(String san, Set<String> staticKeySet, long[] count, long startTime) {
    boolean isValid;
    try {
      SanValidateFormat.validateFormat(san);
      isValid = true;
    } catch (@SuppressWarnings("unused") final SanValidationException e) {
      isValid = false;
    }

    if (isValid) {
      assertTrue(staticKeySet.contains(san));
    } else {
      assertFalse(staticKeySet.contains(san));
    }

    count[0]++;
    if (count[0] % PRINT_INTERVAL == 0) {
      final var elapsed = System.currentTimeMillis() - startTime;
      System.out.println("Processed: " + count[0] + ", elapsed: " + elapsed + " ms - current SAN: " + san);
    }
  }

}
