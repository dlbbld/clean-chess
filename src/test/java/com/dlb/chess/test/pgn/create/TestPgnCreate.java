package com.dlb.chess.test.pgn.create;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.dlb.chess.pgn.create.PgnCreate;
import com.dlb.chess.pgn.parser.LenientPgnParser;
import com.dlb.chess.pgn.parser.model.PgnFile;
import com.dlb.chess.test.PgnTestHelper;

class TestPgnCreate {

  @SuppressWarnings("static-method")
  @Test
  void test() {

    final var expectedString = PgnTestHelper.header("*") + "1. e4 e5 *\n\n";

    final PgnFile fileImport = LenientPgnParser.parseText(expectedString);

    final String actualString = PgnCreate.createPgnFileString(fileImport);

    assertEquals(expectedString, actualString);

  }

}
