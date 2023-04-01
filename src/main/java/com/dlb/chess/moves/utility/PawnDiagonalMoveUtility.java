package com.dlb.chess.moves.utility;

import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.model.PawnDiagonalBoardMove;
import com.dlb.chess.squares.pawn.diagonal.PawnDiagonalSquares;

public abstract class PawnDiagonalMoveUtility {
  public static Set<PawnDiagonalBoardMove> calculatePawnDiagonalMoves(Side side) {
    final Set<PawnDiagonalBoardMove> diagonalBoardMoveSetSide = new TreeSet<>();

    for (final Square fromSquare : Square.BOARD_SQUARE_LIST) {
      final Set<Square> diagonalSquareSet = PawnDiagonalSquares.getPawnDiagonalSquares(side, fromSquare);
      for (final Square diagonalSquare : diagonalSquareSet) {
        diagonalBoardMoveSetSide.add(new PawnDiagonalBoardMove(fromSquare, diagonalSquare));
      }
    }
    return diagonalBoardMoveSetSide;
  }

  public static boolean calculateIsPawnDiagonalMove(Side side, Square fromSquare, Square toSquare) {
    final Set<Square> diagonalToSquareSet = PawnDiagonalSquares.getPawnDiagonalSquares(side, fromSquare);
    return diagonalToSquareSet.contains(toSquare);
  }
}
