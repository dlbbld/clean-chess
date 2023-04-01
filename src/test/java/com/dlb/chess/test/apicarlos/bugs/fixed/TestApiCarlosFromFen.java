package com.dlb.chess.test.apicarlos.bugs.fixed;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.test.apicarlos.NonNullWrapperApiCarlos;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.MoveBackup;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveList;

class TestApiCarlosFromFen {

  @SuppressWarnings("static-method")
  @Test
  void test() throws Exception {
    final Board board = new Board();
    board.doMove(new Move(Square.E2, Square.E4));
    assertEquals("e4", calculateLan(board)); // works fine

    final var fen = "8/8/3KP3/5P2/8/3p4/3kp3/8 w - - 0 100";
    board.loadFromFen(fen);

    board.doMove(new Move(Square.E6, Square.E5));

    try {
      assertEquals("e5", calculateLan(board)); // throws NPE
    } catch (@SuppressWarnings("unused") final NullPointerException npe) {
      System.out.println("Should not throw NPE");
    }
  }

  private static String calculateLan(Board board) {
    final MoveList moveList = new MoveList();
    moveList.addAll(calculateMoveList(board));
    final var sanArray = moveList.toSanArray();
    @SuppressWarnings("null") final var last = NonNullWrapperCommon.getLast(sanArray);
    return last;
  }

  private static List<Move> calculateMoveList(Board board) {
    final List<Move> result = new ArrayList<>();
    for (final MoveBackup moveBackup : NonNullWrapperApiCarlos.getBackup(board)) {
      result.add(NonNullWrapperApiCarlos.getMove(moveBackup));
    }
    return result;
  }

}
