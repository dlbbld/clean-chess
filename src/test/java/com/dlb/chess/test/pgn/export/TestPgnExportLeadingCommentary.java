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
  void testFromImportStrict() {

    final var leadingCommentary = "This is the leading commentary.";

    final PgnFile fileImport = StrictPgnParser
        .parseText(PgnTestHelper.header("*") + "{" + leadingCommentary + "}" + " 1. e4 e5 *\n\n");
    assertEquals(leadingCommentary, fileImport.leadingCommentary());

    final String fileString = PgnCreate.createPgnFileString(fileImport);

    assertTrue(fileString.contains(leadingCommentary));

    final PgnFile fileExport = StrictPgnParser.parseText(fileString);

    assertEquals(leadingCommentary, fileExport.leadingCommentary());

  }

  @SuppressWarnings("static-method")
  @Test
  void testFromImportLenient() {

    final var leadingCommentary = "This is the leading commentary.";

    final PgnFile fileImport = LenientPgnParser
        .parseText(PgnTestHelper.header("*") + "{" + leadingCommentary + "}" + " 1. e4 e5 *\n\n");
    assertEquals(leadingCommentary, fileImport.leadingCommentary());

    final String fileString = PgnCreate.createPgnFileString(fileImport);

    assertTrue(fileString.contains(leadingCommentary));

    final PgnFile fileExport = LenientPgnParser.parseText(fileString);

    assertEquals(leadingCommentary, fileExport.leadingCommentary());

  }

}
