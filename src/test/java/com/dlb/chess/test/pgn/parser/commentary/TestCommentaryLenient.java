package com.dlb.chess.test.pgn.parser.commentary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.pgn.create.PgnCreate;
import com.dlb.chess.pgn.parser.LenientPgnParser;
import com.dlb.chess.pgn.parser.enums.LenientPgnParserValidationProblem;
import com.dlb.chess.pgn.parser.exceptions.LenientPgnParserValidationException;
import com.dlb.chess.pgn.parser.model.PgnFile;
import com.dlb.chess.test.PgnTestHelper;

/**
 * Commentary brace validation for {@link LenientPgnParser}. The lenient parser is strict-equivalent on all four
 * commentary rules — continuing past malformed commentary produces unreliable downstream results, so the tolerance that
 * used to fold unclosed braces into accepted comment text has been removed.
 *
 * @see TestCommentaryStrict for the matching rule set documented there.
 */
class TestCommentaryLenient {

  // -------------------------------------------------------------------------------------------------
  // Valid cases — structural acceptance and commentary-text assertions
  // -------------------------------------------------------------------------------------------------

  @SuppressWarnings("static-method")
  @Test
  void v01_pregameCommentaryOnly() {
    final PgnFile file = LenientPgnParser.parseText(PgnTestHelper.header("*") + "{opening remark} 1. e4 e5 *\n\n");
    assertEquals("opening remark", file.pregameCommentary().value());
    assertEquals(2, file.halfMoveList().size());
  }

  @SuppressWarnings("static-method")
  @Test
  void v01_pregameCommentaryLongNoLinebreaks() {
    final var pregameCommentary = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
    assertTrue(pregameCommentary.length() > PgnCreate.MAX_LINE_LENGTH);
    final PgnFile file = LenientPgnParser
        .parseText(PgnTestHelper.header("*") + "{" + pregameCommentary + "} 1. e4 e5 *\n\n");
    assertEquals(pregameCommentary, file.pregameCommentary().value());
    assertEquals(2, file.halfMoveList().size());
  }

  /**
   * Per the commentary contract, lenient preserves source bytes verbatim — the embedded newlines from the Java
   * text-block stay in the model exactly as written. No trim, no substitution.
   */
  @SuppressWarnings("static-method")
  @Test
  void v01_pregameCommentaryLongWithLinebreaks() {
    final var pregameCommentary = """
        Lorem ipsum dolor sit amet,
        consectetur adipiscing elit,
        sed do eiusmod
        tempor incididunt ut labore et dolore magna aliqua.
        Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.
        Duis aute irure dolor in reprehenderit in
        voluptate velit esse cillum dolore eu fugiat nulla pariatur.
        Excepteur sint occaecat cupidatat non proident,
        sunt in culpa qui officia deserunt mollit anim id est laborum.
            """;
    assertTrue(pregameCommentary.length() > PgnCreate.MAX_LINE_LENGTH);
    final PgnFile file = LenientPgnParser
        .parseText(PgnTestHelper.header("*") + "{" + pregameCommentary + "} 1. e4 e5 *\n\n");
    assertEquals(pregameCommentary, file.pregameCommentary().value());
    assertEquals(2, file.halfMoveList().size());
  }

  @SuppressWarnings("static-method")
  @Test
  void v01_pregameCommentaryOnlySpaces() {
    final var pregameCommentary = "   ";
    final PgnFile file = LenientPgnParser
        .parseText(PgnTestHelper.header("*") + "{" + pregameCommentary + "} 1. e4 e5 *\n\n");
    assertEquals(pregameCommentary, file.pregameCommentary().value());
    assertEquals(2, file.halfMoveList().size());
  }

  @SuppressWarnings("static-method")
  @Test
  void v02_trailingCommentaryAfterWhiteMove() {
    final PgnFile file = LenientPgnParser.parseText(PgnTestHelper.header("*") + "1. e4 {good opening} e5 *\n\n");
    assertEquals("good opening", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary().value());
  }

  @SuppressWarnings("static-method")
  @Test
  void v03_trailingCommentaryAfterBlackMove() {
    final PgnFile file = LenientPgnParser
        .parseText(PgnTestHelper.header("*") + "1. e4 e5 {symmetric} 2. Nf3 Nc6 *\n\n");
    assertEquals("symmetric", NonNullWrapperCommon.get(file.halfMoveList(), 1).commentary().value());
  }

  @SuppressWarnings("static-method")
  @Test
  void v04_commentaryAfterEveryHalfMove() {
    final PgnFile file = LenientPgnParser
        .parseText(PgnTestHelper.header("*") + "1. e4 {a} e5 {b} 2. Nf3 {c} Nc6 {d} *\n\n");
    assertEquals("a", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary().value());
    assertEquals("b", NonNullWrapperCommon.get(file.halfMoveList(), 1).commentary().value());
    assertEquals("c", NonNullWrapperCommon.get(file.halfMoveList(), 2).commentary().value());
    assertEquals("d", NonNullWrapperCommon.get(file.halfMoveList(), 3).commentary().value());
  }

  @SuppressWarnings("static-method")
  @Test
  void v05_leadingAndTrailingCommentary() {
    final PgnFile file = LenientPgnParser
        .parseText(PgnTestHelper.header("*") + "{intro} 1. e4 {after-1-white} e5 *\n\n");
    assertEquals("intro", file.pregameCommentary().value());
    assertEquals("after-1-white", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary().value());
  }

  @SuppressWarnings("static-method")
  @Test
  void v06_emptyCommentary() {
    final PgnFile file = LenientPgnParser.parseText(PgnTestHelper.header("*") + "1. e4 {} e5 *\n\n");
    assertEquals("", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary().value());
  }

  @SuppressWarnings("static-method")
  @Test
  void v07_commentaryWithPunctuationButNoBraces() {
    final PgnFile file = LenientPgnParser
        .parseText(PgnTestHelper.header("*") + "1. e4 {special chars !? + # - / .} e5 *\n\n");
    assertEquals("special chars !? + # - / .", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary().value());
  }

  /**
   * Per the commentary contract, the PGN spec allows newlines inside {@code {...}} commentary. Lenient (like strict)
   * preserves source bytes verbatim. The previous lenient-side substitution of newline-to-space has been removed —
   * tabs and line breaks are valid commentary content per spec, not whitespace to be normalised.
   */
  @SuppressWarnings("static-method")
  @Test
  void v08_multilineCommentaryPreservedVerbatim() {
    final PgnFile file = LenientPgnParser.parseText(PgnTestHelper.header("*") + "1. e4 {line one\nline two} e5 *\n\n");
    assertEquals("line one\nline two", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary().value());
  }

  @SuppressWarnings("static-method")
  @Test
  void v09_commentaryAfterSuffixAnnotation() {
    final PgnFile file = LenientPgnParser.parseText(PgnTestHelper.header("*") + "1. e4!? {spicy} e5 *\n\n");
    assertEquals("spicy", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary().value());
    assertEquals(com.dlb.chess.enums.MoveSuffixAnnotation.INTERESTING_MOVE,
        NonNullWrapperCommon.get(file.halfMoveList(), 0).moveSuffixAnnotation());
  }

  @SuppressWarnings("static-method")
  @Test
  void v10_zeroMoveGameWithPreGameCommentaryOnly() {
    final PgnFile file = LenientPgnParser.parseText(PgnTestHelper.header("*") + "{no moves played} *\n\n");
    assertEquals("no moves played", file.pregameCommentary().value());
    assertEquals(0, file.halfMoveList().size());
  }

  // -------------------------------------------------------------------------------------------------
  // R1 — unclosed commentary
  // -------------------------------------------------------------------------------------------------

  @SuppressWarnings("static-method")
  @Test
  void r1_unclosedAtEof() {
    expectError(PgnTestHelper.header("*") + "1. e4 {unclosed\n\n",
        LenientPgnParserValidationProblem.MOVETEXT_COMMENTARY_START_BRACE_NOT_FOLLOWED_BY_END_BRACE);
  }

  @SuppressWarnings("static-method")
  @Test
  void r1_unclosedPreGameCommentary() {
    expectError(PgnTestHelper.header("*") + "{unclosed leading 1. e4 e5 *\n\n",
        LenientPgnParserValidationProblem.MOVETEXT_COMMENTARY_START_BRACE_NOT_FOLLOWED_BY_END_BRACE);
  }

  // -------------------------------------------------------------------------------------------------
  // R2 — nested commentary
  // -------------------------------------------------------------------------------------------------

  @SuppressWarnings("static-method")
  @Test
  void r2_nestedInPreGameCommentary() {
    expectError(PgnTestHelper.header("*") + "{outer {inner}} 1. e4 e5 *\n\n",
        LenientPgnParserValidationProblem.MOVETEXT_COMMENTARY_CONTAINS_START_BRACE);
  }

  @SuppressWarnings("static-method")
  @Test
  void r2_nestedInTrailingCommentary() {
    expectError(PgnTestHelper.header("*") + "1. e4 {outer {inner}} e5 *\n\n",
        LenientPgnParserValidationProblem.MOVETEXT_COMMENTARY_CONTAINS_START_BRACE);
  }

  @SuppressWarnings("static-method")
  @Test
  void r2_nestedImmediatelyAtStartOfInnerComment() {
    expectError(PgnTestHelper.header("*") + "1. e4 {{nested right away} e5 *\n\n",
        LenientPgnParserValidationProblem.MOVETEXT_COMMENTARY_CONTAINS_START_BRACE);
  }

  // -------------------------------------------------------------------------------------------------
  // R3 — stray closing brace
  // -------------------------------------------------------------------------------------------------

  @SuppressWarnings("static-method")
  @Test
  void r3_strayCloseInLeadingPosition() {
    expectError(PgnTestHelper.header("*") + "} 1. e4 e5 *\n\n",
        LenientPgnParserValidationProblem.MOVETEXT_COMMENTARY_END_BRACE_WITHOUT_START_BRACE);
  }

  @SuppressWarnings("static-method")
  @Test
  void r3_strayCloseInTrailingSlot() {
    expectError(PgnTestHelper.header("*") + "1. e4 } e5 *\n\n",
        LenientPgnParserValidationProblem.MOVETEXT_COMMENTARY_END_BRACE_WITHOUT_START_BRACE);
  }

  @SuppressWarnings("static-method")
  @Test
  void r3_strayCloseAfterClosedCommentary() {
    expectError(PgnTestHelper.header("*") + "1. e4 {ok}} e5 *\n\n",
        LenientPgnParserValidationProblem.MOVETEXT_COMMENTARY_END_BRACE_WITHOUT_START_BRACE);
  }

  // -------------------------------------------------------------------------------------------------
  // R4 — brace at SAN-expected position
  // -------------------------------------------------------------------------------------------------

  @SuppressWarnings("static-method")
  @Test
  void r4_braceBeforeFirstSan() {
    expectError(PgnTestHelper.header("*") + "1. {comment} e4 e5 *\n\n",
        LenientPgnParserValidationProblem.MOVETEXT_COMMENTARY_NOT_ALLOWED_IN_SAN);
  }

  @SuppressWarnings("static-method")
  @Test
  void r4_braceBeforeBlackSan() {
    expectError(PgnTestHelper.header("*") + "1. e4 {c1} {c2} e5 *\n\n",
        LenientPgnParserValidationProblem.MOVETEXT_COMMENTARY_NOT_ALLOWED_IN_SAN);
  }

  @SuppressWarnings("static-method")
  @Test
  void r3_strayCloseAtSanExpectedPosition() {
    // Broken brace variants always surface with their specific category, even at SAN-expected positions — see
    // TestCommentaryStrict for the rationale.
    expectError(PgnTestHelper.header("*") + "1. } e4 e5 *\n\n",
        LenientPgnParserValidationProblem.MOVETEXT_COMMENTARY_END_BRACE_WITHOUT_START_BRACE);
  }

  // -------------------------------------------------------------------------------------------------
  // Post-termination content — same rule as strict
  // -------------------------------------------------------------------------------------------------

  @SuppressWarnings("static-method")
  @Test
  void postTermination_wellFormedBraceIsRejected() {
    expectError(PgnTestHelper.header("*") + "1. e4 e5 * {after result}\n\n",
        LenientPgnParserValidationProblem.MOVETEXT_CONTENT_AFTER_TERMINATION);
  }

  @SuppressWarnings("static-method")
  @Test
  void postTermination_strayCloseUsesSpecificCategory() {
    expectError(PgnTestHelper.header("*") + "1. e4 e5 * }\n\n",
        LenientPgnParserValidationProblem.MOVETEXT_COMMENTARY_END_BRACE_WITHOUT_START_BRACE);
  }

  @SuppressWarnings("static-method")
  @Test
  void postTermination_unclosedBraceUsesSpecificCategory() {
    expectError(PgnTestHelper.header("*") + "1. e4 e5 * {no closing\n\n",
        LenientPgnParserValidationProblem.MOVETEXT_COMMENTARY_START_BRACE_NOT_FOLLOWED_BY_END_BRACE);
  }

  @SuppressWarnings("static-method")
  @Test
  void postTermination_randomSymbolIsRejected() {
    expectError(PgnTestHelper.header("*") + "1. e4 e5 * garbage\n\n",
        LenientPgnParserValidationProblem.MOVETEXT_CONTENT_AFTER_TERMINATION);
  }

  // -------------------------------------------------------------------------------------------------
  // Whitespace and control-character handling — per the commentary contract, lenient (like strict) preserves
  // source bytes verbatim. The PGN spec restricts non-printing characters from string tokens, NOT from {...}
  // commentary. Tabs, line breaks, and other control characters all round-trip unchanged.
  // -------------------------------------------------------------------------------------------------

  @SuppressWarnings("static-method")
  @Test
  void tabInMoveCommentaryIsPreservedVerbatim() {
    final PgnFile file = LenientPgnParser
        .parseText(PgnTestHelper.header("*") + "1. e4 {a\tb} e5 *\n\n");
    assertEquals("a\tb", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary().value());
  }

  @SuppressWarnings("static-method")
  @Test
  void carriageReturnInMoveCommentaryIsPreservedVerbatim() {
    final PgnFile file = LenientPgnParser
        .parseText(PgnTestHelper.header("*") + "1. e4 {a\rb} e5 *\n\n");
    assertEquals("a\rb", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary().value());
  }

  @SuppressWarnings("static-method")
  @Test
  void crlfInMoveCommentaryIsPreservedVerbatim() {
    final PgnFile file = LenientPgnParser
        .parseText(PgnTestHelper.header("*") + "1. e4 {a\r\nb} e5 *\n\n");
    assertEquals("a\r\nb", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary().value());
  }

  @SuppressWarnings("static-method")
  @Test
  void consecutiveTabsArePreservedVerbatim() {
    final PgnFile file = LenientPgnParser
        .parseText(PgnTestHelper.header("*") + "1. e4 {a\t\tb} e5 *\n\n");
    assertEquals("a\t\tb", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary().value());
  }

  @SuppressWarnings("static-method")
  @Test
  void tabInPreGameCommentaryIsPreservedVerbatim() {
    final PgnFile file = LenientPgnParser
        .parseText(PgnTestHelper.header("*") + "{intro\tnote} 1. e4 e5 *\n\n");
    assertEquals("intro\tnote", file.pregameCommentary().value());
  }

  @SuppressWarnings("static-method")
  @Test
  void doubleSpacesInSourceArePreservedAsIs() {
    final PgnFile file = LenientPgnParser
        .parseText(PgnTestHelper.header("*") + "1. e4 {a  b} e5 *\n\n");
    assertEquals("a  b", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary().value());
  }

  @SuppressWarnings("static-method")
  @Test
  void otherControlCharInCommentaryIsRejected() {
    // Bell (U+0007), Cc category (other than \t \n \r) — rejected per the Unicode contract.
    expectError(PgnTestHelper.header("*") + "1. e4 {ab} e5 *\n\n",
        LenientPgnParserValidationProblem.MOVETEXT_COMMENTARY_CONTAINS_FORBIDDEN_CHARACTER);
  }

  // -------------------------------------------------------------------------------------------------
  // T-002 — move-number indicator after intervening commentary
  //
  // Lenient must accept BOTH forms: with the "N..." indicator (canonical, what the strict parser requires) and
  // without (real-world PGN that omits the indicator). The strict-parser companion test in TestCommentaryStrict
  // covers the rejection side.
  // -------------------------------------------------------------------------------------------------

  @SuppressWarnings("static-method")
  @Test
  void t002_acceptCanonicalMoveNumberAfterCommentaryOnWhite() {
    final PgnFile file = LenientPgnParser
        .parseText(PgnTestHelper.header("*") + "1. e4 {after-white} 1... e5 *\n\n");
    assertEquals("after-white", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary().value());
    assertEquals("e5", NonNullWrapperCommon.get(file.halfMoveList(), 1).san());
  }

  @SuppressWarnings("static-method")
  @Test
  void t002_acceptMissingMoveNumberAfterCommentaryOnWhite() {
    // Without "1..." indicator — strict rejects, lenient accepts. Real-world PGN sources frequently omit the
    // indicator since many tools do not enforce it.
    final PgnFile file = LenientPgnParser
        .parseText(PgnTestHelper.header("*") + "1. e4 {after-white} e5 *\n\n");
    assertEquals("after-white", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary().value());
    assertEquals("e5", NonNullWrapperCommon.get(file.halfMoveList(), 1).san());
  }

  @SuppressWarnings("static-method")
  @Test
  void t002_acceptMissingMoveNumberAfterCommentaryAtHigherFullMoveNumber() {
    final PgnFile file = LenientPgnParser
        .parseText(PgnTestHelper.header("*") + "1. e4 e5 2. Nf3 {after-white-2} Nc6 *\n\n");
    assertEquals("after-white-2", NonNullWrapperCommon.get(file.halfMoveList(), 2).commentary().value());
    assertEquals("Nc6", NonNullWrapperCommon.get(file.halfMoveList(), 3).san());
  }

  // -------------------------------------------------------------------------------------------------
  // Helpers
  // -------------------------------------------------------------------------------------------------

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

  @SuppressWarnings("static-method")
  @Test
  void v11_commentaryWithLinebreaks() {
    final PgnFile file = LenientPgnParser
        .parseText(PgnTestHelper.header("*") + "1. e4!? {spicy\n" + "groovy}" + " e5 *\n\n");
    // Per the commentary contract, lenient preserves the embedded \n verbatim.
    assertEquals("spicy\ngroovy", NonNullWrapperCommon.get(file.halfMoveList(), 0).commentary().value());
    assertEquals(com.dlb.chess.enums.MoveSuffixAnnotation.INTERESTING_MOVE,
        NonNullWrapperCommon.get(file.halfMoveList(), 0).moveSuffixAnnotation());
  }

}
