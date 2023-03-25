package com.dlb.chess.test.common.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.dlb.chess.analysis.model.YawnHalfMove;
import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.constants.ChessConstants;
import com.dlb.chess.common.utility.YawnMoveUtility;

class TestYawnMoveUtility {

  @SuppressWarnings("static-method")
  @Test
  void testBasic() {

    final var fenRookEndGame = "8/5k2/4rP2/8/8/8/8/6KR b - - 0 100";
    final Board board = new Board(fenRookEndGame);

    board.performMoves("Kxf6", "Rh2", "Ke7", "Rh3", "Kd6", "Rh4", "Kc6", "Rh5", "Kd6", "Rh6", "Kc6", "Rh7", "Kd6",
        "Rh8", "Kc6", "Rg8", "Kd6", "Rg7", "Kc6", "Rg6", "Kd6", "Rg5", "Kc6", "Rg4", "Kd6", "Rg3", "Kc6", "Rg2", "Kd6",
        "Rf2", "Kc6", "Rf3", "Kd6", "Rf4", "Kc6", "Rf5", "Kd6", "Rf6", "Kc6", "Rf7", "Kd6", "Rf8", "Kc6", "Kf1", "Kd6",
        "Rf7", "Kc6", "Rf6", "Kd6", "Rf5", "Kc6", "Rf4", "Kd6", "Rf3", "Kc6", "Rf2", "Kd6", "Rg2", "Kc6", "Rg3", "Kd6",
        "Rg4", "Kc6", "Rg5", "Kd6", "Rg6", "Kc6", "Rg7", "Kd6", "Rg8", "Kc6", "Rh8", "Kd6", "Rh7", "Kc6", "Rh6", "Kd6",
        "Rh5", "Kc6", "Rh4", "Kd6", "Rh3", "Kc6", "Rh2", "Kd6", "Rh1", "Kc6", "Kf2", "Kd6", "Rh2", "Kc6", "Rh3", "Kd6",
        "Rh4", "Kc6", "Rh5", "Kd6", "Rh6", "Kc6", "Rh7", "Kd6");

    final List<List<YawnHalfMove>> actualListList = YawnMoveUtility.calculateYawnMoveRule(board,
        ChessConstants.FIFTY_MOVE_RULE_HALF_MOVE_CLOCK_THRESHOLD);

    final List<List<YawnHalfMove>> expectedListList = new ArrayList<>();

    final Board boardPlayAlong = new Board(fenRookEndGame);
    boardPlayAlong.performMoves("Kxf6", "Rh2");

    final List<YawnHalfMove> yawnHalfMoveList = new ArrayList<>();
    final YawnHalfMove firstYawnHalfMove = calculateYawnHalfMoveForLastMove(boardPlayAlong);
    yawnHalfMoveList.add(firstYawnHalfMove);

    boardPlayAlong.performMoves("Ke7", "Rh3", "Kd6", "Rh4", "Kc6", "Rh5", "Kd6", "Rh6", "Kc6", "Rh7", "Kd6", "Rh8",
        "Kc6", "Rg8", "Kd6", "Rg7", "Kc6", "Rg6", "Kd6", "Rg5", "Kc6", "Rg4", "Kd6", "Rg3", "Kc6", "Rg2", "Kd6", "Rf2",
        "Kc6", "Rf3", "Kd6", "Rf4", "Kc6", "Rf5", "Kd6", "Rf6", "Kc6", "Rf7", "Kd6", "Rf8", "Kc6", "Kf1", "Kd6", "Rf7",
        "Kc6", "Rf6", "Kd6", "Rf5", "Kc6", "Rf4", "Kd6", "Rf3", "Kc6", "Rf2", "Kd6", "Rg2", "Kc6", "Rg3", "Kd6", "Rg4",
        "Kc6", "Rg5", "Kd6", "Rg6", "Kc6", "Rg7", "Kd6", "Rg8", "Kc6", "Rh8", "Kd6", "Rh7", "Kc6", "Rh6", "Kd6", "Rh5",
        "Kc6", "Rh4", "Kd6", "Rh3", "Kc6", "Rh2", "Kd6", "Rh1", "Kc6", "Kf2", "Kd6", "Rh2", "Kc6", "Rh3", "Kd6", "Rh4",
        "Kc6", "Rh5", "Kd6", "Rh6", "Kc6", "Rh7", "Kd6");

    final YawnHalfMove lastYawnHalfMove = calculateYawnHalfMoveForLastMove(boardPlayAlong);
    yawnHalfMoveList.add(lastYawnHalfMove);

    expectedListList.add(yawnHalfMoveList);

    assertEquals(expectedListList, actualListList);
  }

  private static YawnHalfMove calculateYawnHalfMoveForLastMove(Board board) {
    final var performedHalfMoveCount = board.getPerformedHalfMoveCount();
    final var fullMoveNumber = board.getFullMoveNumber();
    final String san = board.getSan();
    final Side sideMoved = board.getHavingMove().getOppositeSide();
    final var halfMoveClock = board.getHalfMoveClock();
    return new YawnHalfMove(performedHalfMoveCount, fullMoveNumber, san, sideMoved, halfMoveClock);
  }
}
