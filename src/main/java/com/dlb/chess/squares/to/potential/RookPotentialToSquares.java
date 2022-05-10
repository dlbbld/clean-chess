package com.dlb.chess.squares.to.potential;

import java.util.Set;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.squares.to.range.RookRangeSquares;

public class RookPotentialToSquares extends AbstractPotentialToSquares {

  public static Set<Square> calculateRookPotentialToSquares(StaticPosition staticPosition, Square fromSquare,
      Side havingMove) {

    checkPiece(staticPosition, havingMove, fromSquare, ROOK);

    return RookRangeSquares.calculateRookRangeSquares(staticPosition, fromSquare, havingMove, false);
  }

}
