package com.dlb.chess.test.pgn.export;

import org.junit.jupiter.api.Test;

import com.dlb.chess.pgn.parser.LenientPgnParser;
import com.dlb.chess.pgn.parser.model.PgnFile;
import com.dlb.chess.pgn.writer.PgnWriter;

class TestPgnExport {

  @SuppressWarnings("static-method")
  @Test
  void test() {

    final PgnFile pgnFileFromFileSystem = LenientPgnParser.parse(
        "C:\\Users\\danie\\git\\clean-chess\\src\\test\\resources\\pgn\\basic\\insufficientMaterial\\insufficient_material_K_K.pgn");
    PgnWriter.writePgnFile(pgnFileFromFileSystem, "c:\\temp\\formatted.pgn");
  }
}
