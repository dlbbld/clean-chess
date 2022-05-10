package com.dlb.chess.squares.to.threaten;

import java.util.Set;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.squares.emptyboard.KingNonCastlingEmptyBoardSquares;

public class KingNonCastlingThreatenSquares extends AbstractThreatenSquares {

  public static Set<Square> calculateKingNonCastlingThreatenSquares(StaticPosition staticPosition, Square fromSquare,
      Side havingMove) {

    checkPiece(staticPosition, havingMove, fromSquare, KING);

    return KingNonCastlingEmptyBoardSquares.getKingSquares(fromSquare);
  }

}
