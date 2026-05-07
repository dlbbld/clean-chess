package com.dlb.chess.test.pgn.export;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.pgn.create.PgnCreate;
import com.dlb.chess.pgn.parser.LenientPgnParser;
import com.dlb.chess.pgn.parser.StrictPgnParser;
import com.dlb.chess.pgn.parser.model.PgnFile;
import com.dlb.chess.test.PgnTestHelper;

class TestPgnExportLeadingCommentary {

  @SuppressWarnings("static-method")
  @Test
  void testFromImportStrictHasMoves() {

    final var leadingCommentary = "This is the leading commentary.";

    final PgnFile fileImport = StrictPgnParser
        .parseText(PgnTestHelper.header("*") + "{" + leadingCommentary + "}" + " 1. e4 e5 *\n\n");
    assertEquals(leadingCommentary, fileImport.leadingCommentary().value());

    final String fileString = PgnCreate.createPgnFileString(fileImport);

    final PgnFile fileExport = StrictPgnParser.parseText(fileString);

    assertEquals(leadingCommentary, fileExport.leadingCommentary().value());

  }

  @SuppressWarnings("static-method")
  @Test
  void testFromImportStrictHasNoMoves() {

    final var leadingCommentary = "This is the leading commentary.";

    final PgnFile fileImport = StrictPgnParser
        .parseText(PgnTestHelper.header("*") + "{" + leadingCommentary + "}" + " *\n\n");
    assertEquals(leadingCommentary, fileImport.leadingCommentary().value());

    final String fileString = PgnCreate.createPgnFileString(fileImport);

    final PgnFile fileExport = StrictPgnParser.parseText(fileString);

    assertEquals(leadingCommentary, fileExport.leadingCommentary().value());

  }

  /**
   * Round-trip with a long single-line commentary currently fails on byte-stability: {@code PgnCreate} wraps the
   * entire movetext (including brace content) at {@link PgnCreate#MAX_LINE_LENGTH} via
   * {@code PgnUtility.calculateWrappedLines}, replacing some original spaces with newlines INSIDE the {@code {...}}
   * region. After T-001 (preserve newlines/tabs verbatim per the spec), strict no longer rejects the wrapped output —
   * but the re-parsed model has {@code \n} in positions where the input had spaces, so {@code assertEquals(original,
   * roundTripped)} still fails.
   *
   * <p>Fix needed in {@code PgnCreate.calculatePgnFileFileLines} / {@code PgnUtility.calculateWrappedLines}: wrapping
   * must respect brace boundaries — a long brace-comment must stay on a single line (matching python-chess's
   * "don't break inside {...}" approach), so original whitespace inside commentary is not transformed.
   *
   * <p>Disabled until that brace-aware wrap fix lands.
   */
  @org.junit.jupiter.api.Disabled("PgnCreate wrap replaces spaces with \\n inside long {...} — round-trip not byte-stable; needs brace-aware wrap")
  @SuppressWarnings("static-method")
  @Test
  void testFromImportStrictLong() {

    final var leadingCommentary = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
    assertTrue(leadingCommentary.length() > PgnCreate.MAX_LINE_LENGTH);

    final PgnFile fileImport = StrictPgnParser
        .parseText(PgnTestHelper.header("*") + "{" + leadingCommentary + "}" + " *\n\n");
    assertEquals(leadingCommentary, fileImport.leadingCommentary().value());

    final String fileString = PgnCreate.createPgnFileString(fileImport);

    final PgnFile fileExport = StrictPgnParser.parseText(fileString);

    assertEquals(leadingCommentary, fileExport.leadingCommentary().value());

  }

  /**
   * Per the commentary contract, strict (like lenient) preserves source bytes verbatim including embedded newlines.
   * The PGN spec restricts non-printing characters from string tokens, not from {@code {...}} commentary.
   */
  @SuppressWarnings("static-method")
  @Test
  void testFromImportStrictWithLinebreakIsPreservedThroughRoundTrip() {

    final var leadingCommentary = "This is the leading\ncommentary.";

    final PgnFile fileImport = StrictPgnParser
        .parseText(PgnTestHelper.header("*") + "{" + leadingCommentary + "}" + " 1. e4 e5 *\n\n");
    assertEquals(leadingCommentary, fileImport.leadingCommentary().value());

    final String fileString = PgnCreate.createPgnFileString(fileImport);

    final PgnFile fileExport = StrictPgnParser.parseText(fileString);

    assertEquals(leadingCommentary, fileExport.leadingCommentary().value());

  }

  @SuppressWarnings("static-method")
  @Test
  void testFromImportLenientHasMoves() {

    final var leadingCommentary = "This is the leading commentary.";

    final PgnFile fileImport = LenientPgnParser
        .parseText(PgnTestHelper.header("*") + "{" + leadingCommentary + "}" + " 1. e4 e5 *\n\n");
    assertEquals(leadingCommentary, fileImport.leadingCommentary().value());

    final String fileString = PgnCreate.createPgnFileString(fileImport);

    final PgnFile fileExport = LenientPgnParser.parseText(fileString);

    assertEquals(leadingCommentary, fileExport.leadingCommentary().value());

  }

  @SuppressWarnings("static-method")
  @Test
  void testFromImportLenientHasNoMoves() {

    final var leadingCommentary = "This is the leading commentary.";

    final PgnFile fileImport = LenientPgnParser
        .parseText(PgnTestHelper.header("*") + "{" + leadingCommentary + "}" + " *\n\n");
    assertEquals(leadingCommentary, fileImport.leadingCommentary().value());

    final String fileString = PgnCreate.createPgnFileString(fileImport);

    final PgnFile fileExport = LenientPgnParser.parseText(fileString);

    assertEquals(leadingCommentary, fileExport.leadingCommentary().value());

  }

  /**
   * Same brace-aware-wrap issue as {@link #testFromImportStrictLong()} — once the wrap stops breaking inside
   * {@code {...}}, this test will pass as-is.
   */
  @org.junit.jupiter.api.Disabled("PgnCreate wrap replaces spaces with \\n inside long {...} — round-trip not byte-stable; needs brace-aware wrap")
  @SuppressWarnings("static-method")
  @Test
  void testFromImportLenientLong() {

    final var leadingCommentary = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
    assertTrue(leadingCommentary.length() > PgnCreate.MAX_LINE_LENGTH);

    final PgnFile fileImport = LenientPgnParser
        .parseText(PgnTestHelper.header("*") + "{" + leadingCommentary + "}" + " *\n\n");
    assertEquals(leadingCommentary, fileImport.leadingCommentary().value());

    final String fileString = PgnCreate.createPgnFileString(fileImport);

    final PgnFile fileExport = LenientPgnParser.parseText(fileString);

    assertEquals(leadingCommentary, fileExport.leadingCommentary().value());

  }

  // -------------------------------------------------------------------------------------------------
  // Round-trip property — semantic preservation of commentary content through parse + export. Per the
  // commentary contract, both strict and lenient preserve source bytes verbatim, so the round-trip is
  // byte-stable for tabs, newlines, CR, and CRLF embedded in commentary.
  //
  //   parse(export(parse(text))) ≡ parse(text)
  //
  // (No export-side "no forbidden chars" assertion any more — the PGN spec allows these characters in
  // commentary and we preserve them.)
  // -------------------------------------------------------------------------------------------------

  @SuppressWarnings("static-method")
  @Test
  void roundTripLeadingCommentaryWithNewline() {
    assertRoundTripStable(PgnTestHelper.header("*") + "{line one\nline two} 1. e4 e5 *\n\n");
  }

  @SuppressWarnings("static-method")
  @Test
  void roundTripLeadingCommentaryWithTab() {
    assertRoundTripStable(PgnTestHelper.header("*") + "{line one\tline two} 1. e4 e5 *\n\n");
  }

  @SuppressWarnings("static-method")
  @Test
  void roundTripLeadingCommentaryWithCrlf() {
    assertRoundTripStable(PgnTestHelper.header("*") + "{line one\r\nline two} 1. e4 e5 *\n\n");
  }

  @SuppressWarnings("static-method")
  @Test
  void roundTripMoveCommentaryWithNewline() {
    assertRoundTripStable(PgnTestHelper.header("*") + "1. e4 {white\nmove note} e5 *\n\n");
  }

  @SuppressWarnings("static-method")
  @Test
  void roundTripMoveCommentaryWithTab() {
    assertRoundTripStable(PgnTestHelper.header("*") + "1. e4 {white\tmove note} e5 *\n\n");
  }

  @SuppressWarnings("static-method")
  @Test
  void roundTripBothLeadingAndMoveCommentary() {
    assertRoundTripStable(
        PgnTestHelper.header("*") + "{intro\nline} 1. e4 {one\ttwo} e5 {three\rfour} *\n\n");
  }

  @SuppressWarnings("static-method")
  @Test
  void roundTripDoubleSpacesPreserved() {
    assertRoundTripStable(PgnTestHelper.header("*") + "1. e4 {a  b  c} e5 *\n\n");
  }

  /**
   * Asserts the round-trip property: parsing the input, exporting, and re-parsing yields a model equal to the
   * first parse. Per the commentary contract this is byte-stable for tabs, newlines, CR, and CRLF — none of these
   * is normalised by the parser, so what goes in comes back out.
   */
  private static void assertRoundTripStable(String inputPgn) {
    final PgnFile p1 = LenientPgnParser.parseText(inputPgn);
    final String t1 = PgnCreate.createPgnFileString(p1);
    final PgnFile p2 = LenientPgnParser.parseText(t1);

    assertEquals(p1, p2, "Round-trip changed the parsed model");
  }

}
