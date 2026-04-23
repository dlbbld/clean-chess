package com.dlb.chess.test.pgn.parser.commentary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.pgn.parser.LenientPgnParser;
import com.dlb.chess.pgn.parser.enums.LenientPgnParserValidationProblem;
import com.dlb.chess.pgn.parser.exceptions.LenientPgnParserValidationException;
import com.dlb.chess.pgn.parser.model.PgnFile;

/**
 * Commentary brace validation for {@link LenientPgnParser}. The lenient parser is strict-equivalent on all four
 * commentary rules — continuing past malformed commentary produces unreliable downstream results, so the tolerance
 * that used to fold unclosed braces into accepted comment text has been removed.
 *
 * @see TestCommentaryStrict for the matching rule set documented there.
 */
class TestCommentaryLenient {

  // -------------------------------------------------------------------------------------------------
  // Valid cases — structural acceptance and commentary-text assertions
  // -------------------------------------------------------------------------------------------------

  @SuppressWarnings("static-method")
  @Test
  void v1_leadingCommentaryOnly() {
    final PgnFile file = LenientPgnParser.parseText(header("*") + "{opening remark} 1. e4 e5 *\n\n");
    assertEquals("opening remark", file.leadingCommentary());
    assertEquals(2, file.halfMoveList().size());
  }

  @SuppressWarnings("static-method")
  @Test
  void v2_trailingCommentaryAfterWhiteMove() {
    final PgnFile file = LenientPgnParser.parseText(header("*") + "1. e4 {good opening} e5 *\n\n");
    assertEquals("good opening", file.halfMoveList().get(0).commentary());
  }

  @SuppressWarnings("static-method")
  @Test
  void v3_trailingCommentaryAfterBlackMove() {
    final PgnFile file = LenientPgnParser.parseText(
        header("*") + "1. e4 e5 {symmetric} 2. Nf3 Nc6 *\n\n");
    assertEquals("symmetric", file.halfMoveList().get(1).commentary());
  }

  @SuppressWarnings("static-method")
  @Test
  void v4_commentaryAfterEveryHalfMove() {
    final PgnFile file = LenientPgnParser.parseText(
        header("*") + "1. e4 {a} e5 {b} 2. Nf3 {c} Nc6 {d} *\n\n");
    assertEquals("a", file.halfMoveList().get(0).commentary());
    assertEquals("b", file.halfMoveList().get(1).commentary());
    assertEquals("c", file.halfMoveList().get(2).commentary());
    assertEquals("d", file.halfMoveList().get(3).commentary());
  }

  @SuppressWarnings("static-method")
  @Test
  void v5_leadingAndTrailingCommentary() {
    final PgnFile file = LenientPgnParser.parseText(
        header("*") + "{intro} 1. e4 {after-1-white} e5 *\n\n");
    assertEquals("intro", file.leadingCommentary());
    assertEquals("after-1-white", file.halfMoveList().get(0).commentary());
  }

  @SuppressWarnings("static-method")
  @Test
  void v6_emptyCommentary() {
    final PgnFile file = LenientPgnParser.parseText(header("*") + "1. e4 {} e5 *\n\n");
    assertEquals("", file.halfMoveList().get(0).commentary());
  }

  @SuppressWarnings("static-method")
  @Test
  void v7_commentaryWithPunctuationButNoBraces() {
    final PgnFile file = LenientPgnParser.parseText(
        header("*") + "1. e4 {special chars !? + # - / .} e5 *\n\n");
    assertEquals("special chars !? + # - / .", file.halfMoveList().get(0).commentary());
  }

  @SuppressWarnings("static-method")
  @Test
  void v8_multilineCommentary() {
    final PgnFile file = LenientPgnParser.parseText(
        header("*") + "1. e4 {line one\nline two} e5 *\n\n");
    assertEquals("line one\nline two", file.halfMoveList().get(0).commentary());
  }

  @SuppressWarnings("static-method")
  @Test
  void v9_commentaryAfterSuffixAnnotation() {
    final PgnFile file = LenientPgnParser.parseText(
        header("*") + "1. e4!? {spicy} e5 *\n\n");
    assertEquals("spicy", file.halfMoveList().get(0).commentary());
    assertEquals(com.dlb.chess.enums.MoveSuffixAnnotation.INTERESTING_MOVE,
        file.halfMoveList().get(0).moveSuffixAnnotation());
  }

  @SuppressWarnings("static-method")
  @Test
  void v10_zeroMoveGameWithLeadingCommentaryOnly() {
    final PgnFile file = LenientPgnParser.parseText(header("*") + "{no moves played} *\n\n");
    assertEquals("no moves played", file.leadingCommentary());
    assertEquals(0, file.halfMoveList().size());
  }

  // -------------------------------------------------------------------------------------------------
  // R1 — unclosed commentary
  // -------------------------------------------------------------------------------------------------

  @SuppressWarnings("static-method")
  @Test
  void r1_unclosedAtEof() {
    expectError(header("*") + "1. e4 {unclosed\n\n",
        LenientPgnParserValidationProblem.MOVETEXT_COMMENTARY_START_BRACE_NOT_FOLLOWED_BY_END_BRACE);
  }

  @SuppressWarnings("static-method")
  @Test
  void r1_unclosedLeadingCommentary() {
    expectError(header("*") + "{unclosed leading 1. e4 e5 *\n\n",
        LenientPgnParserValidationProblem.MOVETEXT_COMMENTARY_START_BRACE_NOT_FOLLOWED_BY_END_BRACE);
  }

  // -------------------------------------------------------------------------------------------------
  // R2 — nested commentary
  // -------------------------------------------------------------------------------------------------

  @SuppressWarnings("static-method")
  @Test
  void r2_nestedInLeadingCommentary() {
    expectError(header("*") + "{outer {inner}} 1. e4 e5 *\n\n",
        LenientPgnParserValidationProblem.MOVETEXT_COMMENTARY_CONTAINS_START_BRACE);
  }

  @SuppressWarnings("static-method")
  @Test
  void r2_nestedInTrailingCommentary() {
    expectError(header("*") + "1. e4 {outer {inner}} e5 *\n\n",
        LenientPgnParserValidationProblem.MOVETEXT_COMMENTARY_CONTAINS_START_BRACE);
  }

  @SuppressWarnings("static-method")
  @Test
  void r2_nestedImmediatelyAtStartOfInnerComment() {
    expectError(header("*") + "1. e4 {{nested right away} e5 *\n\n",
        LenientPgnParserValidationProblem.MOVETEXT_COMMENTARY_CONTAINS_START_BRACE);
  }

  // -------------------------------------------------------------------------------------------------
  // R3 — stray closing brace
  // -------------------------------------------------------------------------------------------------

  @SuppressWarnings("static-method")
  @Test
  void r3_strayCloseInLeadingPosition() {
    expectError(header("*") + "} 1. e4 e5 *\n\n",
        LenientPgnParserValidationProblem.MOVETEXT_COMMENTARY_END_BRACE_WITHOUT_START_BRACE);
  }

  @SuppressWarnings("static-method")
  @Test
  void r3_strayCloseInTrailingSlot() {
    expectError(header("*") + "1. e4 } e5 *\n\n",
        LenientPgnParserValidationProblem.MOVETEXT_COMMENTARY_END_BRACE_WITHOUT_START_BRACE);
  }

  @SuppressWarnings("static-method")
  @Test
  void r3_strayCloseAfterClosedCommentary() {
    expectError(header("*") + "1. e4 {ok}} e5 *\n\n",
        LenientPgnParserValidationProblem.MOVETEXT_COMMENTARY_END_BRACE_WITHOUT_START_BRACE);
  }

  // -------------------------------------------------------------------------------------------------
  // R4 — brace at SAN-expected position
  // -------------------------------------------------------------------------------------------------

  @SuppressWarnings("static-method")
  @Test
  void r4_braceBeforeFirstSan() {
    expectError(header("*") + "1. {comment} e4 e5 *\n\n",
        LenientPgnParserValidationProblem.MOVETEXT_COMMENTARY_NOT_ALLOWED_IN_SAN);
  }

  @SuppressWarnings("static-method")
  @Test
  void r4_braceBeforeBlackSan() {
    expectError(header("*") + "1. e4 {c1} {c2} e5 *\n\n",
        LenientPgnParserValidationProblem.MOVETEXT_COMMENTARY_NOT_ALLOWED_IN_SAN);
  }

  @SuppressWarnings("static-method")
  @Test
  void r3_strayCloseAtSanExpectedPosition() {
    // Broken brace variants always surface with their specific category, even at SAN-expected positions — see
    // TestCommentaryStrict for the rationale.
    expectError(header("*") + "1. } e4 e5 *\n\n",
        LenientPgnParserValidationProblem.MOVETEXT_COMMENTARY_END_BRACE_WITHOUT_START_BRACE);
  }

  // -------------------------------------------------------------------------------------------------
  // Helpers
  // -------------------------------------------------------------------------------------------------

  /**
   * Uses the strict-style seven-tag-roster header. Lenient accepts this form without complaint and makes the tests
   * directly parallel to {@link TestCommentaryStrict}.
   */
  private static String header(String result) {
    return """
        [Event "?"]
        [Site "?"]
        [Date "?"]
        [Round "?"]
        [White "?"]
        [Black "?"]
        [Result \"""" + result + "\"]\n\n";
  }

  private static void expectError(String pgnText, LenientPgnParserValidationProblem expected) {
    var isException = false;
    try {
      LenientPgnParser.parseText(pgnText);
    } catch (final LenientPgnParserValidationException e) {
      isException = true;
      assertEquals(expected, e.getLenientPgnParserValidationProblem(),
          "Wrong problem category; message was: " + e.getMessage());
    }
    assertTrue(isException, "Expected " + expected + " but parser accepted the input");
  }
}
