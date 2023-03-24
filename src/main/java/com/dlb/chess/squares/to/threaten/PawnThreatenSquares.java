package com.dlb.chess.squares.to.threaten;

import java.util.Set;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.squares.pawn.diagonal.PawnDiagonalSquares;

public class PawnThreatenSquares extends AbstractThreatenSquares {

  public static Set<Square> calculatePawnThreatenSquares(StaticPosition staticPosition, Square fromSquare,
      Side havingMove) {

    checkPiece(staticPosition, havingMove, fromSquare, PAWN);

    return PawnDiagonalSquares.getPawnDiagonalSquares(havingMove, fromSquare);
  }
}
