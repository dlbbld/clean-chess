package com.dlb.chess.squares.to.potential;

import java.util.Set;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.squares.to.range.BishopRangeSquares;

public class BishopPotentialToSquares extends AbstractPotentialToSquares {

  public static Set<Square> calculateBishopPotentialToSquares(StaticPosition staticPosition, Square fromSquare,
      Side havingMove) {

    checkPiece(staticPosition, havingMove, fromSquare, BISHOP);

    return BishopRangeSquares.calculateBishopRangeSquares(staticPosition, fromSquare, havingMove, false);

  }

}
