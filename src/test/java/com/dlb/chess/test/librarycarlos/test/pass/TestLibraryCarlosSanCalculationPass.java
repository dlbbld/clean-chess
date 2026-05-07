package com.dlb.chess.test.librarycarlos.test.pass;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.test.librarycarlos.NonNullWrapperLibraryCarlos;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.MoveBackup;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveList;

class TestLibraryCarlosSanCalculationPass {

  @SuppressWarnings("static-method")
  @Test
  void testWithoutFen() throws Exception {
    final Board board = new Board();
    board.doMove(new Move(Square.E2, Square.E4));
    assertEquals("e4", calculateSan(board)); // works fine
  }

  private static String calculateSan(Board board) {
    final MoveList moveList = new MoveList();
    moveList.addAll(calculateMoveList(board));
    final var sanArray = moveList.toSanArray();
    @SuppressWarnings("null") final var last = NonNullWrapperCommon.getLast(sanArray);
    return last;
  }

  private static List<Move> calculateMoveList(Board board) {
    final List<Move> result = new ArrayList<>();
    for (final MoveBackup moveBackup : NonNullWrapperLibraryCarlos.getBackup(board)) {
      result.add(NonNullWrapperLibraryCarlos.getMove(moveBackup));
    }
    return result;
  }

}
