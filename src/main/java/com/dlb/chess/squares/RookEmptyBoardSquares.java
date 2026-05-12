package com.dlb.chess.squares;

import java.util.EnumMap;

import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.EnumConstants;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

class RookEmptyBoardSquares extends AbstractEmptyBoardSquares implements EnumConstants {

  private static final ImmutableMap<Square, RookRange> ROOK_SQUARES_MAP;

  static {
    final EnumMap<Square, RookRange> map = NonNullWrapperCommon.newEnumMap(Square.class);
    for (final Square from : Square.REAL) {
      final int file = from.getFile().getNumber();
      final int rank = from.getRank().getNumber();
      final ImmutableList<Square> north = RayUtility.ray(file, rank, 0, 1);
      final ImmutableList<Square> east = RayUtility.ray(file, rank, 1, 0);
      final ImmutableList<Square> south = RayUtility.ray(file, rank, 0, -1);
      final ImmutableList<Square> west = RayUtility.ray(file, rank, -1, 0);
      map.put(from, new RookRange(north, east, south, west));
    }
    ROOK_SQUARES_MAP = NonNullWrapperCommon.copyOfMap(map);
    ValidateMoveNumberUtility.validateOrthogonalMoveNumber(ROOK_SQUARES_MAP, 896);
  }

  public static RookRange getRookSquares(Square fromSquare) {
    return NonNullWrapperCommon.get(ROOK_SQUARES_MAP, fromSquare);
  }

}
