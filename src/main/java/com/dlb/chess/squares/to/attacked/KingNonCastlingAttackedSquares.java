package com.dlb.chess.squares.to.attacked;

import java.util.Set;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.squares.emptyboard.KingNonCastlingEmptyBoardSquares;

class KingNonCastlingAttackedSquares extends AbstractAttackedSquares {

  public static Set<Square> calculateKingNonCastlingAttackedSquares(StaticPosition staticPosition, Square fromSquare,
      Side havingMove) {

    checkPiece(staticPosition, havingMove, fromSquare, KING);

    return KingNonCastlingEmptyBoardSquares.getKingSquares(fromSquare);
  }

}
