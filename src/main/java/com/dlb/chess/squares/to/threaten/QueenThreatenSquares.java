package com.dlb.chess.squares.to.threaten;

import java.util.Set;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.squares.to.range.QueenRangeSquares;

public class QueenThreatenSquares extends AbstractThreatenSquares {

  public static Set<Square> calculateQueenThreatenSquares(StaticPosition staticPosition, Square fromSquare,
      Side havingMove) {

    checkPiece(staticPosition, havingMove, fromSquare, QUEEN);

    return QueenRangeSquares.calculateQueenRangeSquares(staticPosition, fromSquare, havingMove, true);
  }

}
