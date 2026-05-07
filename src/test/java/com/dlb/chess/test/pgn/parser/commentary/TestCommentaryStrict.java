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
 * parser populates {@link PgnFile#pregameCommentary()} and each {@code PgnHalfMove.commentary()}. These are the first
 * dedicated tests for commentary in the codebase — previously commentary was only exercised incidentally by a handful
 * of game fixtures.
 */
class TestCommentaryStrict {

  // -------------------------------------------------------------------------------------------------
  // Valid cases — structural acceptance and commentary-text assertions
  // -------------------------------------------------------------------------------------------------

  @SuppressWarnings("static-method")
  @Test
  void v1_pregameCommentaryOnly() {
    final PgnFile file = StrictPgnParser.parseText(header("*") + "{opening remark} 1. e4 e5 *\n\n");
    assertEquals("opening remark", file.pregameCommentary().value());
    assertEquals(2, file.halfMoveList().size());
    assertEquals("", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary().value());
    assertEquals("", NonNullWrapperCommon.get(file.halfMoveList(), 1).commentary().value());
  }

  @SuppressWarnings("static-method")
  @Test
  void v2_trailingCommentaryAfterWhiteMove() {
    final PgnFile file = StrictPgnParser.parseText(header("*") + "1. e4 {good opening} 1... e5 *\n\n");
    assertEquals("", file.pregameCommentary().value());
    assertEquals("good opening", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary().value());
    assertEquals("", NonNullWrapperCommon.get(file.halfMoveList(), 1).commentary().value());
  }

  @SuppressWarnings("static-method")
  @Test
  void v3_trailingCommentaryAfterBlackMove() {
    final PgnFile file = StrictPgnParser.parseText(header("*") + "1. e4 e5 {symmetric} 2. Nf3 Nc6 *\n\n");
    assertEquals("", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary().value());
    assertEquals("symmetric", NonNullWrapperCommon.get(file.halfMoveList(), 1).commentary().value());
    assertEquals("", NonNullWrapperCommon.get(file.halfMoveList(), 2).commentary().value());
    assertEquals("", NonNullWrapperCommon.get(file.halfMoveList(), 3).commentary().value());
  }

  @SuppressWarnings("static-method")
  @Test
  void v4_commentaryAfterEveryHalfMove() {
    final PgnFile file = StrictPgnParser.parseText(header("*") + "1. e4 {a} 1... e5 {b} 2. Nf3 {c} 2... Nc6 {d} *\n\n");
    assertEquals("a", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary().value());
    assertEquals("b", NonNullWrapperCommon.get(file.halfMoveList(), 1).commentary().value());
    assertEquals("c", NonNullWrapperCommon.get(file.halfMoveList(), 2).commentary().value());
    assertEquals("d", NonNullWrapperCommon.get(file.halfMoveList(), 3).commentary().value());
  }

  @SuppressWarnings("static-method")
  @Test
  void v5_leadingAndTrailingCommentary() {
    final PgnFile file = StrictPgnParser.parseText(header("*") + "{intro} 1. e4 {after-1-white} 1... e5 *\n\n");
    assertEquals("intro", file.pregameCommentary().value());
    assertEquals("after-1-white", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary().value());
    assertEquals("", NonNullWrapperCommon.get(file.halfMoveList(), 1).commentary().value());
  }

  @SuppressWarnings("static-method")
  @Test
  void v6_emptyCommentary() {
    final PgnFile file = StrictPgnParser.parseText(header("*") + "1. e4 {} 1... e5 *\n\n");
    assertEquals("", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary().value());
    assertEquals("", NonNullWrapperCommon.get(file.halfMoveList(), 1).commentary().value());
  }

  @SuppressWarnings("static-method")
  @Test
  void v7_commentaryWithPunctuationButNoBraces() {
    final PgnFile file = StrictPgnParser.parseText(header("*") + "1. e4 {special chars !? + # - / .} 1... e5 *\n\n");
    assertEquals("special chars !? + # - / .", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary().value());
  }

  /**
   * Per the commentary contract, the PGN spec allows newlines inside {@code {...}} commentary (the spec's prohibition
   * on non-printing characters applies only to <em>string tokens</em>, i.e. tag values, not to commentary). Strict
   * preserves the source bytes verbatim, including embedded newlines.
   */
  @SuppressWarnings("static-method")
  @Test
  void v8_multilineCommentaryPreservedVerbatim() {
    final PgnFile file = StrictPgnParser.parseText(header("*") + "1. e4 {line one\nline two} 1... e5 *\n\n");
    assertEquals("line one\nline two", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary().value());
  }

  @SuppressWarnings("static-method")
  @Test
  void v9_commentaryAfterSuffixAnnotation() {
    final PgnFile file = StrictPgnParser.parseText(header("*") + "1. e4!? {spicy} 1... e5 *\n\n");
    assertEquals("spicy", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary().value());
    assertEquals(com.dlb.chess.enums.MoveSuffixAnnotation.INTERESTING_MOVE,
        NonNullWrapperCommon.get(file.halfMoveList(), 0).moveSuffixAnnotation());
  }

  @SuppressWarnings("static-method")
  @Test
  void v10_zeroMoveGameWithPreGameCommentaryOnly() {
    final PgnFile file = StrictPgnParser.parseText(header("*") + "{no moves played} *\n\n");
    assertEquals("no moves played", file.pregameCommentary().value());
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
  void r1_unclosedPreGameCommentary() {
    expectError(header("*") + "{unclosed leading 1. e4 e5 *\n\n",
        StrictPgnParserValidationProblem.MOVETEXT_COMMENTARY_START_BRACE_NOT_FOLLOWED_BY_END_BRACE);
  }

  // -------------------------------------------------------------------------------------------------
  // T-003 — left brace inside an open brace comment is content, not a nested-comment error.
  //
  // Per PGN spec §8.2.5: "a left brace character appearing in a brace comment loses its special meaning". Both
  // parsers consume `{` as a literal content character — the comment is closed only by the next `}`. The previous
  // R2 rule (rejection of any `{` inside an open comment) has been removed, and the corresponding problem code
  // MOVETEXT_COMMENTARY_CONTAINS_START_BRACE is gone. A `}` after the comment closes is still R3 (stray close).
  // -------------------------------------------------------------------------------------------------

  @SuppressWarnings("static-method")
  @Test
  void t003_openBraceInPreGameCommentaryIsContent() {
    final PgnFile file = StrictPgnParser.parseText(header("*") + "{outer {inner} 1. e4 e5 *\n\n");
    assertEquals("outer {inner", file.pregameCommentary().value());
  }

  @SuppressWarnings("static-method")
  @Test
  void t003_openBraceInTrailingCommentaryIsContent() {
    final PgnFile file = StrictPgnParser.parseText(header("*") + "1. e4 {outer {inner} 1... e5 *\n\n");
    assertEquals("outer {inner", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary().value());
  }

  @SuppressWarnings("static-method")
  @Test
  void t003_openBraceImmediatelyAtStartOfContent() {
    final PgnFile file = StrictPgnParser.parseText(header("*") + "1. e4 {{nested right away} 1... e5 *\n\n");
    assertEquals("{nested right away", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary().value());
  }

  @SuppressWarnings("static-method")
  @Test
  void t003_strayCloseAfterClosedCommentaryWithInnerOpenBraceIsR3() {
    // The well-formed `{outer {inner}` closes at the first `}` (its content is "outer {inner"). The trailing `}`
    // is then a stray close — categorized as R3. T-003 changes the comment-internal handling, not the stray-close
    // rule.
    expectError(header("*") + "1. e4 {outer {inner}} e5 *\n\n",
        StrictPgnParserValidationProblem.MOVETEXT_COMMENTARY_END_BRACE_WITHOUT_START_BRACE);
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
    expectError(header("*") + "1. e4 {c1} 1... {c2} e5 *\n\n",
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
  // Whitespace and control-character handling — per the commentary contract, the PGN spec restricts non-printing
  // characters from string tokens, NOT from {...} commentary. Both strict and lenient preserve source bytes
  // verbatim. These tests cover acceptance and round-trip-faithful storage.
  // -------------------------------------------------------------------------------------------------

  @SuppressWarnings("static-method")
  @Test
  void tabInPreGameCommentaryIsPreserved() {
    final PgnFile file = StrictPgnParser.parseText(header("*") + "{a\tb} 1. e4 e5 *\n\n");
    assertEquals("a\tb", file.pregameCommentary().value());
  }

  @SuppressWarnings("static-method")
  @Test
  void tabInMoveCommentaryIsPreserved() {
    final PgnFile file = StrictPgnParser.parseText(header("*") + "1. e4 {a\tb} 1... e5 *\n\n");
    assertEquals("a\tb", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary().value());
  }

  @SuppressWarnings("static-method")
  @Test
  void carriageReturnInPreGameCommentaryIsNormalisedToLf() {
    // T-005: CRLF and lone CR are normalised to LF at the parser input boundary. The model never sees `\r`.
    final PgnFile file = StrictPgnParser.parseText(header("*") + "{a\rb} 1. e4 e5 *\n\n");
    assertEquals("a\nb", file.pregameCommentary().value());
  }

  @SuppressWarnings("static-method")
  @Test
  void crlfInPreGameCommentaryIsNormalisedToLf() {
    final PgnFile file = StrictPgnParser.parseText(header("*") + "{a\r\nb} 1. e4 e5 *\n\n");
    assertEquals("a\nb", file.pregameCommentary().value());
  }

  @SuppressWarnings("static-method")
  @Test
  void crlfInMoveCommentaryIsNormalisedToLf() {
    final PgnFile file = StrictPgnParser.parseText(header("*") + "1. e4 {a\r\nb} 1... e5 *\n\n");
    assertEquals("a\nb", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary().value());
  }

  @SuppressWarnings("static-method")
  @Test
  void otherControlCharInCommentaryIsRejected() {
    // Bell character (U+0007), Cc category (other than \t \n \r) — rejected per the Unicode contract.
    expectError(header("*") + "1. e4 {ab} e5 *\n\n",
        StrictPgnParserValidationProblem.MOVETEXT_COMMENTARY_CONTAINS_FORBIDDEN_CHARACTER);
  }

  @SuppressWarnings("static-method")
  @Test
  void doubleSpacesInCommentaryArePreservedAsIs() {
    final PgnFile file = StrictPgnParser.parseText(header("*") + "1. e4 {a  b} 1... e5 *\n\n");
    assertEquals("a  b", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary().value());
  }

  @SuppressWarnings("static-method")
  @Test
  void doubleSpacesInPreGameCommentaryArePreservedAsIs() {
    final PgnFile file = StrictPgnParser.parseText(header("*") + "{line one  line two} 1. e4 e5 *\n\n");
    assertEquals("line one  line two", file.pregameCommentary().value());
  }

  // -------------------------------------------------------------------------------------------------
  // T-002 — move-number indicator after intervening commentary (PGN spec §8.2.2 case 1)
  //
  // When commentary intervenes between the previous White move and the next Black move, the strict parser REQUIRES
  // an explicit "N..." move-number indicator before the Black move. This mirrors python-chess's `force_movenumber`
  // export behavior and the spec's literal phrasing. The lenient parser accepts both forms (with and without the
  // indicator) — see TestCommentaryLenient.
  // -------------------------------------------------------------------------------------------------

  @SuppressWarnings("static-method")
  @Test
  void t002_rejectMissingMoveNumberAfterCommentaryOnWhite() {
    // Canonical violation: commentary on White's move, then Black's move with no "1..." indicator. Strict must
    // reject this with the dedicated category; the previous (non-T-002) categorization would have classified
    // this differently or accepted it.
    expectError(header("*") + "1. e4 {after-white} e5 *\n\n",
        StrictPgnParserValidationProblem.MOVETEXT_MOVE_NUMBER_REQUIRED_AFTER_COMMENTARY);
  }

  @SuppressWarnings("static-method")
  @Test
  void t002_rejectMissingMoveNumberAfterCommentaryOnWhiteHigherFullMoveNumber() {
    // The required indicator is the current full-move number followed by "...". Verify it's not hardcoded to "1".
    expectError(header("*") + "1. e4 e5 2. Nf3 {after-white-2} Nc6 *\n\n",
        StrictPgnParserValidationProblem.MOVETEXT_MOVE_NUMBER_REQUIRED_AFTER_COMMENTARY);
  }

  @SuppressWarnings("static-method")
  @Test
  void t002_acceptCanonicalMoveNumberAfterCommentaryOnWhite() {
    // Canonical acceptance: "1..." indicator present after intervening commentary.
    final PgnFile file = StrictPgnParser.parseText(header("*") + "1. e4 {after-white} 1... e5 *\n\n");
    assertEquals("after-white", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary().value());
    assertEquals("e5", NonNullWrapperCommon.get(file.halfMoveList(), 1).san());
  }

  @SuppressWarnings("static-method")
  @Test
  void t002_rejectWrongMoveNumberAfterCommentaryOnWhite() {
    // Indicator present but wrong number — strict must reject. Surfaces with the same dedicated category as
    // a missing indicator (same defect class: "the required indicator is not the one we found").
    expectError(header("*") + "1. e4 {after-white} 2... e5 *\n\n",
        StrictPgnParserValidationProblem.MOVETEXT_MOVE_NUMBER_REQUIRED_AFTER_COMMENTARY);
  }

  @SuppressWarnings("static-method")
  @Test
  void t002_noIndicatorRequiredWhenNoCommentaryIntervenes() {
    // Sanity check that T-002 only fires when commentary actually intervened. A plain "1. e4 e5" must still parse
    // — the indicator must NOT be required when there's no intervening commentary.
    final PgnFile file = StrictPgnParser.parseText(header("*") + "1. e4 e5 *\n\n");
    assertEquals(2, file.halfMoveList().size());
  }

  @SuppressWarnings("static-method")
  @Test
  void t002_indicatorNotRequiredWhenCommentaryIsOnBlackMove() {
    // Commentary on Black's move does not trigger T-002 — the next move is a White move and already requires
    // its own move number for an unrelated reason.
    final PgnFile file = StrictPgnParser.parseText(header("*") + "1. e4 e5 {after-black} 2. Nf3 *\n\n");
    assertEquals("after-black", NonNullWrapperCommon.get(file.halfMoveList(), 1).commentary().value());
    assertEquals("Nf3", NonNullWrapperCommon.get(file.halfMoveList(), 2).san());
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
