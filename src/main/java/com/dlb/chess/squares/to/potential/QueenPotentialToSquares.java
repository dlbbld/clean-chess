package com.dlb.chess.squares.to.potential;

import java.util.Set;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.squares.to.range.QueenRangeSquares;

public class QueenPotentialToSquares extends AbstractPotentialToSquares {

  public static Set<Square> calculateQueenPotentialToSquares(StaticPosition staticPosition, Square fromSquare,
      Side havingMove) {

    checkPiece(staticPosition, havingMove, fromSquare, QUEEN);

    return QueenRangeSquares.calculateQueenRangeSquares(staticPosition, fromSquare, havingMove, false);
  }

}
