package com.dlb.chess.squares;

import java.util.EnumMap;

import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.EnumConstants;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

class BishopEmptyBoardSquares extends AbstractEmptyBoardSquares implements EnumConstants {

  private static final ImmutableMap<Square, BishopRange> BISHOP_SQUARES_MAP;

  static {
    final EnumMap<Square, BishopRange> map = NonNullWrapperCommon.newEnumMap(Square.class);
    for (final Square from : Square.REAL) {
      final int file = from.getFile().getNumber();
      final int rank = from.getRank().getNumber();
      final ImmutableList<Square> northEast = RayUtility.ray(file, rank, 1, 1);
      final ImmutableList<Square> southEast = RayUtility.ray(file, rank, 1, -1);
      final ImmutableList<Square> southWest = RayUtility.ray(file, rank, -1, -1);
      final ImmutableList<Square> northWest = RayUtility.ray(file, rank, -1, 1);
      map.put(from, new BishopRange(northEast, southEast, southWest, northWest));
    }
    BISHOP_SQUARES_MAP = NonNullWrapperCommon.copyOfMap(map);
    ValidateMoveNumberUtility.validateDiagonalMovesNumber(BISHOP_SQUARES_MAP, 560);
  }

  public static BishopRange getBishopSquares(Square fromSquare) {
    return NonNullWrapperCommon.get(BISHOP_SQUARES_MAP, fromSquare);
  }

}
