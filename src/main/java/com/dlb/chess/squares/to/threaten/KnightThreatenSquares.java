package com.dlb.chess.squares.to.threaten;

import java.util.Set;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.squares.emptyboard.KnightEmptyBoardSquares;

public class KnightThreatenSquares extends AbstractThreatenSquares {

  public static Set<Square> calculateKnightThreatenSquares(StaticPosition staticPosition, Square fromSquare,
      Side havingMove) {

    checkPiece(staticPosition, havingMove, fromSquare, KNIGHT);

    return KnightEmptyBoardSquares.getKnightSquares(fromSquare);
  }

}
