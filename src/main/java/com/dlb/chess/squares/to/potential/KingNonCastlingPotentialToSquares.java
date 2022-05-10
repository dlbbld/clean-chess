package com.dlb.chess.squares.to.potential;

import java.util.Set;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.squares.emptyboard.KingNonCastlingEmptyBoardSquares;

public class KingNonCastlingPotentialToSquares extends AbstractPotentialToSquares {

  public static Set<Square> calculateKingNonCastlingPotentialToSquares(StaticPosition staticPosition, Square fromSquare,
      Side havingMove) {

    final Set<Square> emptyBoardSquareSet = KingNonCastlingEmptyBoardSquares.getKingSquares(fromSquare);

    return calculateNonRangeNonPawnPotentialToSquares(staticPosition, fromSquare, KING, emptyBoardSquareSet,
        havingMove);
  }

}
