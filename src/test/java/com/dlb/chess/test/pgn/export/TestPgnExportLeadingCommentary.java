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
    assertEquals(leadingCommentary, fileImport.leadingCommentary());

    final String fileString = PgnCreate.createPgnFileString(fileImport);

    final PgnFile fileExport = StrictPgnParser.parseText(fileString);

    assertEquals(leadingCommentary, fileExport.leadingCommentary());

  }

  @SuppressWarnings("static-method")
  @Test
  void testFromImportStrictHasNoMoves() {

    final var leadingCommentary = "This is the leading commentary.";

    final PgnFile fileImport = StrictPgnParser
        .parseText(PgnTestHelper.header("*") + "{" + leadingCommentary + "}" + " *\n\n");
    assertEquals(leadingCommentary, fileImport.leadingCommentary());

    final String fileString = PgnCreate.createPgnFileString(fileImport);

    final PgnFile fileExport = StrictPgnParser.parseText(fileString);

    assertEquals(leadingCommentary, fileExport.leadingCommentary());

  }

  @SuppressWarnings("static-method")
  @Test
  void testFromImportStrictLong() {

    final var leadingCommentary = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
    assertTrue(leadingCommentary.length() > PgnCreate.MAX_LINE_LENGTH);

    final PgnFile fileImport = StrictPgnParser
        .parseText(PgnTestHelper.header("*") + "{" + leadingCommentary + "}" + " *\n\n");
    assertEquals(leadingCommentary, fileImport.leadingCommentary());

    final String fileString = PgnCreate.createPgnFileString(fileImport);

    final PgnFile fileExport = StrictPgnParser.parseText(fileString);

    assertEquals(leadingCommentary, fileExport.leadingCommentary());

  }

  @SuppressWarnings("static-method")
  @Test
  void testFromImportStrictHasLinebreaks() {

    final var leadingCommentary = "This is the leading\ncommentary.";

    final PgnFile fileImport = StrictPgnParser
        .parseText(PgnTestHelper.header("*") + "{" + leadingCommentary + "}" + " 1. e4 e5 *\n\n");
    assertEquals(leadingCommentary, fileImport.leadingCommentary());

    final String fileString = PgnCreate.createPgnFileString(fileImport);

    final PgnFile fileExport = StrictPgnParser.parseText(fileString);

    assertEquals(leadingCommentary, fileExport.leadingCommentary());

  }

  @SuppressWarnings("static-method")
  @Test
  void testFromImportLenientHasMoves() {

    final var leadingCommentary = "This is the leading commentary.";

    final PgnFile fileImport = LenientPgnParser
        .parseText(PgnTestHelper.header("*") + "{" + leadingCommentary + "}" + " 1. e4 e5 *\n\n");
    assertEquals(leadingCommentary, fileImport.leadingCommentary());

    final String fileString = PgnCreate.createPgnFileString(fileImport);

    final PgnFile fileExport = LenientPgnParser.parseText(fileString);

    assertEquals(leadingCommentary, fileExport.leadingCommentary());

  }

  @SuppressWarnings("static-method")
  @Test
  void testFromImportLenientHasNoMoves() {

    final var leadingCommentary = "This is the leading commentary.";

    final PgnFile fileImport = LenientPgnParser
        .parseText(PgnTestHelper.header("*") + "{" + leadingCommentary + "}" + " *\n\n");
    assertEquals(leadingCommentary, fileImport.leadingCommentary());

    final String fileString = PgnCreate.createPgnFileString(fileImport);

    final PgnFile fileExport = LenientPgnParser.parseText(fileString);

    assertEquals(leadingCommentary, fileExport.leadingCommentary());

  }

  @SuppressWarnings("static-method")
  @Test
  void testFromImportLenientLong() {

    final var leadingCommentary = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
    assertTrue(leadingCommentary.length() > PgnCreate.MAX_LINE_LENGTH);

    final PgnFile fileImport = LenientPgnParser
        .parseText(PgnTestHelper.header("*") + "{" + leadingCommentary + "}" + " *\n\n");
    assertEquals(leadingCommentary, fileImport.leadingCommentary());

    final String fileString = PgnCreate.createPgnFileString(fileImport);

    final PgnFile fileExport = LenientPgnParser.parseText(fileString);

    assertEquals(leadingCommentary, fileExport.leadingCommentary());

  }

}
