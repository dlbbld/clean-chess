package com.dlb.chess.unwinnability;

import com.dlb.chess.board.enums.Square;

class KingDistance {

  // Chebyshev Distance
  // minimum king moves on empty board
  public static int distance(Square squareFrom, Square squareTo) {
    return Math.max(Math.abs(squareFrom.getFile().getNumber() - squareTo.getFile().getNumber()),
        Math.abs(squareFrom.getRank().getNumber() - squareTo.getRank().getNumber()));
  }

}
