package com.dlb.chess.test.librarycarlos.test.notpass;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import com.github.bhlangonijr.chesslib.Board;

class TestLibraryCarlosInsufficientMaterialNotPass {

  @SuppressWarnings("static-method")
  @Test
  void testExceptedTrueButFalse() throws Exception {

    // KBvKB with bishop on same color field is a draw
    final var bishopOnSameColorSquares = "8/8/8/4k3/5b2/3K4/8/2B5 w - - 0 1";
    assertFalse(isInsufficientMaterial(bishopOnSameColorSquares));

    // KBBvK with bishop on same color field is a draw
    final var whiteTwoBishopsOnSameColorSquares = "B3k3/8/8/8/8/8/8/4KB2 w - - 0 1";
    assertFalse(isInsufficientMaterial(whiteTwoBishopsOnSameColorSquares));

    // KBBvKBBB with bishop on same color field is a draw
    assertFalse(isInsufficientMaterial("B1b1k3/3b4/4b3/8/8/8/8/4KB2 w - - 0 1"));

  }

  private static boolean isInsufficientMaterial(String fen) {
    final Board board = new Board();
    return board.isInsufficientMaterial();
  }
}
