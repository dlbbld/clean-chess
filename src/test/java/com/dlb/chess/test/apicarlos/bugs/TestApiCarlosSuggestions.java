package com.dlb.chess.test.apicarlos.bugs;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveList;

class TestApiCarlosSuggestions {
  private static final boolean IS_WORKING = false;

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
    if (IS_WORKING) {
      runSuggestionDoMoveSan();
      runSuggestionBoardSan();
    }
  }

  private static void runSuggestionDoMoveSan() throws Exception {
    final MoveList moveList = new MoveList();
    moveList.loadFromSan("1. e4 Nf6 2. e5 d5 3. Bc4 Nc6 4. Bf1 Nb8 5. Bc4 Nc6 6. Bf1 Nb8");

    final Board board = new Board();
    @SuppressWarnings("null") final Iterator<Move> moves = moveList.iterator();
    while (moves.hasNext()) {
      board.doMove(moves.next());
    }

    board.doMove(new Move("d2d4", Side.WHITE)); // ok pseudo LAN works
    board.undoMove();
    try {
      board.doMove(new Move("d4", Side.WHITE)); // throws exception
    } catch (@SuppressWarnings("unused") final Exception e) {
      System.out.println("SAN notation cannot be used - but would be fine!");
    }
  }

  private static void runSuggestionBoardSan() throws Exception {
    final Board board = new Board();
    board.loadFromFen("4k3/8/8/8/1b6/2N5/8/4K1N1 w - - 0 1");
    board.doMove(new Move("Nge2", Side.WHITE)); // don't allow
    board.undoMove();
    board.doMove(new Move("Ne2", Side.WHITE)); // ok
  }
}
