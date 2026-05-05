package com.dlb.chess.test.pgn.parser.commentary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.pgn.parser.StrictPgnParser;
import com.dlb.chess.pgn.parser.enums.StrictPgnParserValidationProblem;
import com.dlb.chess.pgn.parser.exceptions.StrictPgnParserValidationException;
import com.dlb.chess.pgn.parser.model.PgnFile;

/**
 * Commentary brace validation for {@link StrictPgnParser}. Covers the four rules agreed on for commentary:
 *
 * <ul>
 * <li>R1 — every {@code {} must have a matching {@code }}.
 * <li>R2 — a {@code {} inside an already-open commentary is an error (no nesting).
 *   
<li>R3 — a {@code }} outside any open commentary is an error (stray close).
 * <li>R4 — a brace token where a SAN half-move is expected is an error.
 * </ul>
 *
 * <p>
 * Also covers the positive path: where commentary is legal (leading slot, trailing-after-half-move slot) and how the
 * parser populates {@link PgnFile#leadingCommentary()} and each {@code PgnHalfMove.commentary()}. These are the first
 * dedicated tests for commentary in the codebase — previously commentary was only exercised incidentally by a handful
 * of game fixtures.
 */
class TestCommentaryStrict {

  // -------------------------------------------------------------------------------------------------
  // Valid cases — structural acceptance and commentary-text assertions
  // -------------------------------------------------------------------------------------------------

  @SuppressWarnings("static-method")
  @Test
  void v1_leadingCommentaryOnly() {
    final PgnFile file = StrictPgnParser.parseText(header("*") + "{opening remark} 1. e4 e5 *\n\n");
    assertEquals("opening remark", file.leadingCommentary());
    assertEquals(2, file.halfMoveList().size());
    assertEquals("", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary());
    assertEquals("", NonNullWrapperCommon.get(file.halfMoveList(), 1).commentary());
  }

  @SuppressWarnings("static-method")
  @Test
  void v2_trailingCommentaryAfterWhiteMove() {
    final PgnFile file = StrictPgnParser.parseText(header("*") + "1. e4 {good opening} e5 *\n\n");
    assertEquals("", file.leadingCommentary());
    assertEquals("good opening", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary());
    assertEquals("", NonNullWrapperCommon.get(file.halfMoveList(), 1).commentary());
  }

  @SuppressWarnings("static-method")
  @Test
  void v3_trailingCommentaryAfterBlackMove() {
    final PgnFile file = StrictPgnParser.parseText(header("*") + "1. e4 e5 {symmetric} 2. Nf3 Nc6 *\n\n");
    assertEquals("", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary());
    assertEquals("symmetric", NonNullWrapperCommon.get(file.halfMoveList(), 1).commentary());
    assertEquals("", NonNullWrapperCommon.get(file.halfMoveList(), 2).commentary());
    assertEquals("", NonNullWrapperCommon.get(file.halfMoveList(), 3).commentary());
  }

  @SuppressWarnings("static-method")
  @Test
  void v4_commentaryAfterEveryHalfMove() {
    final PgnFile file = StrictPgnParser.parseText(header("*") + "1. e4 {a} e5 {b} 2. Nf3 {c} Nc6 {d} *\n\n");
    assertEquals("a", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary());
    assertEquals("b", NonNullWrapperCommon.get(file.halfMoveList(), 1).commentary());
    assertEquals("c", NonNullWrapperCommon.get(file.halfMoveList(), 2).commentary());
    assertEquals("d", NonNullWrapperCommon.get(file.halfMoveList(), 3).commentary());
  }

  @SuppressWarnings("static-method")
  @Test
  void v5_leadingAndTrailingCommentary() {
    final PgnFile file = StrictPgnParser.parseText(header("*") + "{intro} 1. e4 {after-1-white} e5 *\n\n");
    assertEquals("intro", file.leadingCommentary());
    assertEquals("after-1-white", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary());
    assertEquals("", NonNullWrapperCommon.get(file.halfMoveList(), 1).commentary());
  }

  @SuppressWarnings("static-method")
  @Test
  void v6_emptyCommentary() {
    final PgnFile file = StrictPgnParser.parseText(header("*") + "1. e4 {} e5 *\n\n");
    assertEquals("", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary());
    assertEquals("", NonNullWrapperCommon.get(file.halfMoveList(), 1).commentary());
  }

  @SuppressWarnings("static-method")
  @Test
  void v7_commentaryWithPunctuationButNoBraces() {
    final PgnFile file = StrictPgnParser.parseText(header("*") + "1. e4 {special chars !? + # - / .} e5 *\n\n");
    assertEquals("special chars !? + # - / .", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary());
  }

  @SuppressWarnings("static-method")
  @Test
  void v8_multilineCommentary() {
    final PgnFile file = StrictPgnParser.parseText(header("*") + "1. e4 {line one\nline two} e5 *\n\n");
    assertEquals("line one\nline two", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary());
  }

  @SuppressWarnings("static-method")
  @Test
  void v9_commentaryAfterSuffixAnnotation() {
    final PgnFile file = StrictPgnParser.parseText(header("*") + "1. e4!? {spicy} e5 *\n\n");
    assertEquals("spicy", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary());
    assertEquals(com.dlb.chess.enums.MoveSuffixAnnotation.INTERESTING_MOVE,
        NonNullWrapperCommon.get(file.halfMoveList(), 0).moveSuffixAnnotation());
  }

  @SuppressWarnings("static-method")
  @Test
  void v10_zeroMoveGameWithLeadingCommentaryOnly() {
    final PgnFile file = StrictPgnParser.parseText(header("*") + "{no moves played} *\n\n");
    assertEquals("no moves played", file.leadingCommentary());
    assertEquals(0, file.halfMoveList().size());
  }

  // -------------------------------------------------------------------------------------------------
  // R1 — unclosed commentary (start brace without matching close)
  // -------------------------------------------------------------------------------------------------

  @SuppressWarnings("static-method")
  @Test
  void r1_unclosedAtEof() {
    expectError(header("*") + "1. e4 {unclosed\n\n",
        StrictPgnParserValidationProblem.MOVETEXT_COMMENTARY_START_BRACE_NOT_FOLLOWED_BY_END_BRACE);
  }

  @SuppressWarnings("static-method")
  @Test
  void r1_unclosedLeadingCommentary() {
    expectError(header("*") + "{unclosed leading 1. e4 e5 *\n\n",
        StrictPgnParserValidationProblem.MOVETEXT_COMMENTARY_START_BRACE_NOT_FOLLOWED_BY_END_BRACE);
  }

  // -------------------------------------------------------------------------------------------------
  // R2 — nested commentary (start brace inside an open commentary)
  // -------------------------------------------------------------------------------------------------

  @SuppressWarnings("static-method")
  @Test
  void r2_nestedInLeadingCommentary() {
    expectError(header("*") + "{outer {inner}} 1. e4 e5 *\n\n",
        StrictPgnParserValidationProblem.MOVETEXT_COMMENTARY_CONTAINS_START_BRACE);
  }

  @SuppressWarnings("static-method")
  @Test
  void r2_nestedInTrailingCommentary() {
    expectError(header("*") + "1. e4 {outer {inner}} e5 *\n\n",
        StrictPgnParserValidationProblem.MOVETEXT_COMMENTARY_CONTAINS_START_BRACE);
  }

  @SuppressWarnings("static-method")
  @Test
  void r2_nestedImmediatelyAtStartOfInnerComment() {
    expectError(header("*") + "1. e4 {{nested right away} e5 *\n\n",
        StrictPgnParserValidationProblem.MOVETEXT_COMMENTARY_CONTAINS_START_BRACE);
  }

  // -------------------------------------------------------------------------------------------------
  // R3 — stray closing brace outside any open commentary
  // -------------------------------------------------------------------------------------------------

  @SuppressWarnings("static-method")
  @Test
  void r3_strayCloseInLeadingPosition() {
    expectError(header("*") + "} 1. e4 e5 *\n\n",
        StrictPgnParserValidationProblem.MOVETEXT_COMMENTARY_END_BRACE_WITHOUT_START_BRACE);
  }

  @SuppressWarnings("static-method")
  @Test
  void r3_strayCloseInTrailingSlot() {
    expectError(header("*") + "1. e4 } e5 *\n\n",
        StrictPgnParserValidationProblem.MOVETEXT_COMMENTARY_END_BRACE_WITHOUT_START_BRACE);
  }

  @SuppressWarnings("static-method")
  @Test
  void r3_strayCloseAfterClosedCommentary() {
    expectError(header("*") + "1. e4 {ok}} e5 *\n\n",
        StrictPgnParserValidationProblem.MOVETEXT_COMMENTARY_END_BRACE_WITHOUT_START_BRACE);
  }

  // -------------------------------------------------------------------------------------------------
  // R4 — brace at SAN-expected position
  // -------------------------------------------------------------------------------------------------

  @SuppressWarnings("static-method")
  @Test
  void r4_braceBeforeFirstSan() {
    expectError(header("*") + "1. {comment} e4 e5 *\n\n",
        StrictPgnParserValidationProblem.MOVETEXT_COMMENTARY_NOT_ALLOWED_IN_SAN);
  }

  @SuppressWarnings("static-method")
  @Test
  void r4_braceBeforeBlackSan() {
    expectError(header("*") + "1. e4 {c1} {c2} e5 *\n\n",
        StrictPgnParserValidationProblem.MOVETEXT_COMMENTARY_NOT_ALLOWED_IN_SAN);
  }

  @SuppressWarnings("static-method")
  @Test
  void r3_strayCloseAtSanExpectedPosition() {
    // Broken brace variants (stray close, unclosed, nested) always surface with their specific lexical category,
    // regardless of whether the position would otherwise be a SAN-expected slot. R4 is reserved for well-formed
    // braces that appear in the wrong position.
    expectError(header("*") + "1. } e4 e5 *\n\n",
        StrictPgnParserValidationProblem.MOVETEXT_COMMENTARY_END_BRACE_WITHOUT_START_BRACE);
  }

  // -------------------------------------------------------------------------------------------------
  // Post-termination content — commentary may not appear after the game termination marker
  // -------------------------------------------------------------------------------------------------

  @SuppressWarnings("static-method")
  @Test
  void postTermination_wellFormedBraceIsRejected() {
    expectError(header("*") + "1. e4 e5 * {after result}\n\n",
        StrictPgnParserValidationProblem.MOVETEXT_CONTENT_AFTER_TERMINATION);
  }

  @SuppressWarnings("static-method")
  @Test
  void postTermination_strayCloseUsesSpecificCategory() {
    // Broken-brace lexical errors fire regardless of position, including after the termination marker.
    expectError(header("*") + "1. e4 e5 * }\n\n",
        StrictPgnParserValidationProblem.MOVETEXT_COMMENTARY_END_BRACE_WITHOUT_START_BRACE);
  }

  @SuppressWarnings("static-method")
  @Test
  void postTermination_unclosedBraceUsesSpecificCategory() {
    expectError(header("*") + "1. e4 e5 * {no closing\n\n",
        StrictPgnParserValidationProblem.MOVETEXT_COMMENTARY_START_BRACE_NOT_FOLLOWED_BY_END_BRACE);
  }

  @SuppressWarnings("static-method")
  @Test
  void postTermination_randomSymbolIsRejected() {
    expectError(header("*") + "1. e4 e5 * garbage\n\n",
        StrictPgnParserValidationProblem.MOVETEXT_CONTENT_AFTER_TERMINATION);
  }

  // -------------------------------------------------------------------------------------------------
  // Helpers
  // -------------------------------------------------------------------------------------------------

  /** Minimal seven-tag-roster header ending with the blank line that separates tags from movetext. */
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

  private static void expectError(String pgnText, StrictPgnParserValidationProblem expected) {
    var isException = false;
    try {
      StrictPgnParser.parseText(pgnText);
    } catch (final StrictPgnParserValidationException e) {
      isException = true;
      assertEquals(expected, e.getStrictPgnParserValidationProblem(),
          "Wrong problem category; message was: " + e.getMessage());
    }
    assertTrue(isException, "Expected " + expected + " but parser accepted the input");
  }
}
