package com.dlb.chess.test.pgn.export;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
   * Per the commentary contract, strict rejects newlines (and tabs / CR / control characters) in commentary content.
   * Round-trip with linebreaks is the lenient parser's domain — see {@link #roundTripLeadingCommentaryWithNewline()}.
   */
  @SuppressWarnings("static-method")
  @Test
  void testFromImportStrictWithLinebreakIsRejected() {

    final var leadingCommentary = "This is the leading\ncommentary.";

    final var thrown = org.junit.jupiter.api.Assertions.assertThrows(
        com.dlb.chess.pgn.parser.exceptions.StrictPgnParserValidationException.class,
        () -> StrictPgnParser.parseText(PgnTestHelper.header("*") + "{" + leadingCommentary + "}" + " 1. e4 e5 *\n\n"));
    assertEquals(
        com.dlb.chess.pgn.parser.enums.StrictPgnParserValidationProblem.MOVETEXT_COMMENTARY_CONTAINS_FORBIDDEN_CHARACTER,
        thrown.getStrictPgnParserValidationProblem());

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
  // Round-trip property — semantic preservation of weird-whitespace commentary through lenient
  // parser + export. Strict can't appear here (it rejects the inputs); lenient must produce a model
  // that re-parses to the same model after export.
  //
  //   parse(export(parse(text))) ≡ parse(text)
  //
  // Plus an export-side guarantee: the exported string contains no tab/newline/CR.
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
   * Asserts the lenient round-trip property: parsing the input, exporting, and re-parsing yields a model equal to the
   * first parse. Also asserts the exported text is well-formed (no tab / newline / carriage-return inside any commentary).
   */
  private static void assertRoundTripStable(String inputPgn) {
    final PgnFile p1 = LenientPgnParser.parseText(inputPgn);
    final String t1 = PgnCreate.createPgnFileString(p1);
    final PgnFile p2 = LenientPgnParser.parseText(t1);

    // Semantic preservation: the model after a round-trip equals the model from the first parse.
    assertEquals(p1, p2, "Round-trip changed the parsed model");

    // Export-side well-formedness: no forbidden whitespace survives export.
    // We only check the brace-content regions to avoid false positives on the trailing newline that ends the file.
    assertExportContainsNoForbiddenInsideBraces(t1);
  }

  private static void assertExportContainsNoForbiddenInsideBraces(String exported) {
    var insideBrace = false;
    for (int i = 0; i < exported.length(); i++) {
      final char c = exported.charAt(i);
      if (c == '{') {
        insideBrace = true;
      } else if (c == '}') {
        insideBrace = false;
      } else if (insideBrace) {
        assertFalse(c == '\t' || c == '\n' || c == '\r',
            "Exported commentary contained forbidden whitespace at index " + i + ": U+" + String.format("%04X", (int) c));
      }
    }
  }

}
