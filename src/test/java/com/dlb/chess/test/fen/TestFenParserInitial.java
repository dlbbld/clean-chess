package com.dlb.chess.test.fen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.dlb.chess.fen.FenParserAdvanced;
import com.dlb.chess.fen.constants.FenConstants;
import com.dlb.chess.fen.model.Fen;

class TestFenParserInitial {

  @SuppressWarnings("static-method")
  @Test
  void testInitial() {
    final Fen fen = FenParserAdvanced.parseFenAdvanced(FenConstants.FEN_INITIAL_STR);
    assertEquals(FenConstants.FEN_INITIAL_STR, fen.fen());
  }

}
