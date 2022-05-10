package com.dlb.chess.squares.to.threaten;

import java.util.Set;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.squares.to.range.BishopRangeSquares;

public class BishopThreatenSquares extends AbstractThreatenSquares {

  public static Set<Square> calculateBishopThreatenSquares(StaticPosition staticPosition, Square fromSquare,
      Side havingMove) {

    checkPiece(staticPosition, havingMove, fromSquare, BISHOP);

    return BishopRangeSquares.calculateBishopRangeSquares(staticPosition, fromSquare, havingMove, true);

  }

}
