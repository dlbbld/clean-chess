package com.dlb.chess.squares;

import java.util.EnumMap;

import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.EnumConstants;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

class QueenEmptyBoardSquares extends AbstractEmptyBoardSquares implements EnumConstants {

  private static final ImmutableMap<Square, QueenRange> QUEEN_SQUARES_MAP;

  static {
    final EnumMap<Square, QueenRange> map = NonNullWrapperCommon.newEnumMap(Square.class);
    for (final Square from : Square.REAL) {
      final int file = from.getFile().getNumber();
      final int rank = from.getRank().getNumber();
      final ImmutableList<Square> north = RayUtility.ray(file, rank, 0, 1);
      final ImmutableList<Square> east = RayUtility.ray(file, rank, 1, 0);
      final ImmutableList<Square> south = RayUtility.ray(file, rank, 0, -1);
      final ImmutableList<Square> west = RayUtility.ray(file, rank, -1, 0);
      final ImmutableList<Square> northEast = RayUtility.ray(file, rank, 1, 1);
      final ImmutableList<Square> southEast = RayUtility.ray(file, rank, 1, -1);
      final ImmutableList<Square> southWest = RayUtility.ray(file, rank, -1, -1);
      final ImmutableList<Square> northWest = RayUtility.ray(file, rank, -1, 1);
      map.put(from, new QueenRange(north, east, south, west, northEast, southEast, southWest, northWest));
    }
    QUEEN_SQUARES_MAP = NonNullWrapperCommon.copyOfMap(map);
    ValidateMoveNumberUtility.validateOrthogonalMoveNumber(QUEEN_SQUARES_MAP, 896);
    ValidateMoveNumberUtility.validateDiagonalMovesNumber(QUEEN_SQUARES_MAP, 560);
  }

  public static QueenRange getQueenSquares(Square fromSquare) {
    return NonNullWrapperCommon.get(QUEEN_SQUARES_MAP, fromSquare);
  }

}
