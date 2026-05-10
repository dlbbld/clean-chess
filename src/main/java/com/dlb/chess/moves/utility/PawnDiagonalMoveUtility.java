package com.dlb.chess.moves.utility;

import java.util.Set;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.squares.pawn.diagonal.PawnDiagonalSquares;

public abstract class PawnDiagonalMoveUtility {

  public static boolean calculateIsPawnDiagonalMove(Side side, Square fromSquare, Square toSquare) {
    final Set<Square> diagonalToSquareSet = PawnDiagonalSquares.getPawnDiagonalSquares(side, fromSquare);
    return diagonalToSquareSet.contains(toSquare);
  }
}
