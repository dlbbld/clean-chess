package com.dlb.chess.test.apicarlos.bugs;

import org.junit.jupiter.api.Test;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Square;

class TestApiCarlosEnPassantCapture {

  @SuppressWarnings("static-method")
  @Test
  void testEnPassantCapture() throws Exception {
    final Board board = new Board();
    board.doMove("e4");
    board.doMove("Nf6");
    board.doMove("e5");
    board.doMove("d5");

    // fails, is D5
    System.out.println("Expected " + Square.D6.name() + " but is " + board.getEnPassantTarget());

    // fails, is NONE
    System.out.println("Expected " + Square.D6.name() + " but is " + board.getBackup().getLast().getEnPassantTarget());

    // fails, is false
    board.doMove("exd6");
    System.out.println("Expected true but is " + board.getBackup().getLast().isEnPassantMove());
  }

}
