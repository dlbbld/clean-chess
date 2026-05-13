package com.dlb.chess.squares;

import com.dlb.chess.board.enums.Square;
import com.google.common.collect.ImmutableList;

/**
 * Shared helper for the empty-board sliding-piece (rook, bishop, queen) tables: walk from a starting (file, rank)
 * coordinate in a given direction (fileDelta, rankDelta) until off the board, collecting each square visited.
 */
abstract class RayUtility {

  static ImmutableList<Square> ray(int fromFile, int fromRank, int fileDelta, int rankDelta) {
    final ImmutableList.Builder<Square> builder = ImmutableList.builder();
    int f = fromFile + fileDelta;
    int r = fromRank + rankDelta;
    while (f >= 1 && f <= 8 && r >= 1 && r <= 8) {
      builder.add(Square.calculate(f, r));
      f += fileDelta;
      r += rankDelta;
    }
    return builder.build();
  }

}
