package com.dlb.chess.squares.to.range;

import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.range.model.QueenRange;
import com.dlb.chess.squares.emptyboard.QueenEmptyBoardSquares;

public class QueenRangeSquares extends AbstractRangeSquares {

  public static Set<Square> calculateQueenRangeSquares(StaticPosition staticPosition, Square fromSquare,
      Side havingMove, boolean isAllowOwnPiece) {

    final QueenRange emptyBoardRange = QueenEmptyBoardSquares.getQueenSquares(fromSquare);

    final Set<Square> result = new TreeSet<>(calculateOrthogonalRangeSquare(staticPosition, havingMove, fromSquare,
        QUEEN, emptyBoardRange, isAllowOwnPiece));
    result.addAll(
        calculateDiagonalRangeSquare(staticPosition, havingMove, fromSquare, QUEEN, emptyBoardRange, isAllowOwnPiece));

    return result;
  }

}
