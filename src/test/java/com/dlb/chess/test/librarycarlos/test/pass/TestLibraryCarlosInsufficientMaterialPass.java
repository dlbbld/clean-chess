package com.dlb.chess.test.librarycarlos.test.pass;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import com.github.bhlangonijr.chesslib.Board;

class TestLibraryCarlosInsufficientMaterialPass {

  @SuppressWarnings("static-method")
  @Test
  void testExceptedFalseAndFalse() throws Exception {

    // KNvKN
    assertFalse(isInsufficientMaterial("8/8/4K3/8/1n6/8/5k1N/8 w - - 0 50"));

    // KNvKB, bishop light squares
    assertFalse(isInsufficientMaterial("8/k2b4/8/8/2K5/8/8/1N6 w - - 0 50"));

    // KNvKB, bishop dark squares
    assertFalse(isInsufficientMaterial("5b2/8/3k4/8/8/4K3/8/1N6 w - - 0 50"));

    // KBvKN, bishop light squares
    assertFalse(isInsufficientMaterial("1n6/8/8/2k1K3/8/8/8/5B2 w - - 0 50"));

    // KBvKN, bishop dark squares
    assertFalse(isInsufficientMaterial("1n6/8/8/8/4K3/1k6/1B6/8 w - - 0 50"));

    // KBvKB with bishop on different color field is not a draw
    final var bishopOnDifferentColorSquares = "8/8/8/4k3/5b2/3K4/2B5/8 w - - 0 1";
    assertFalse(isInsufficientMaterial(bishopOnDifferentColorSquares));

  }

  private static boolean isInsufficientMaterial(String fen) {
    final Board board = new Board();
    return board.isInsufficientMaterial();
  }
}
