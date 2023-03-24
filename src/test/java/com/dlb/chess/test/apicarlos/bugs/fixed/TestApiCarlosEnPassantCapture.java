package com.dlb.chess.test.apicarlos.bugs.fixed;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Square;

class TestApiCarlosEnPassantCapture {

  @SuppressWarnings("static-method")
  @Test
  void testEnPassantCaptureExpectedBugs() throws Exception {
    final Board board = new Board();
    board.doMove("e4");
    board.doMove("Nf6");
    board.doMove("e5");
    board.doMove("d5");

    // expected D6 but results is D5
    assertEquals(Square.D5, board.getEnPassantTarget());

    // expected D6 but is NONE
    assertEquals(Square.NONE, board.getBackup().getLast().getEnPassantTarget());

    // expected true but is false
    board.doMove("exd6");
    assertFalse(board.getBackup().getLast().isEnPassantMove());
  }

}
