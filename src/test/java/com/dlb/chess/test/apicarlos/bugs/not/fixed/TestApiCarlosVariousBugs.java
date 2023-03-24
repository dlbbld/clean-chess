package com.dlb.chess.test.apicarlos.bugs.not.fixed;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.bhlangonijr.chesslib.Board;

class TestApiCarlosVariousBugs {

  @SuppressWarnings("static-method")
  void testInsufficientMaterial() throws Exception {
    final Board board = new Board();

    // expected false but is true
    board.loadFromFen("8/8/4K3/8/1n6/8/5k1N/8 w - - 0 50"); // KNvKN
    assertTrue(board.isInsufficientMaterial());

    // expected false but is true
    board.loadFromFen("8/k2b4/8/8/2K5/8/8/1N6 w - - 0 50"); // KNvKB, bishop light squares
    assertTrue(board.isInsufficientMaterial());

    // expected false but is true
    board.loadFromFen("5b2/8/3k4/8/8/4K3/8/1N6 w - - 0 50"); // KNvKB, bishop dark squares
    assertTrue(board.isInsufficientMaterial());

    // expected false but is true
    board.loadFromFen("1n6/8/8/2k1K3/8/8/8/5B2 w - - 0 50"); // KBvKN, bishop light squares
    assertTrue(board.isInsufficientMaterial());

    // expected false but is true
    board.loadFromFen("1n6/8/8/8/4K3/1k6/1B6/8 w - - 0 50"); // KBvKN, bishop dark squares
    assertTrue(board.isInsufficientMaterial());
  }

}
