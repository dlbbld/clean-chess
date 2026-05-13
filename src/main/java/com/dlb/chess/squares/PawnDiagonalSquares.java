package com.dlb.chess.squares;

import java.util.EnumMap;
import java.util.Set;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.Nulls;
import com.dlb.chess.common.constants.EnumConstants;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

public class PawnDiagonalSquares implements EnumConstants {

  private static final ImmutableMap<Square, ImmutableSet<Square>> PAWN_WHITE_SQUARES_MAP;
  private static final ImmutableMap<Square, ImmutableSet<Square>> PAWN_BLACK_SQUARES_MAP;

  static {
    PAWN_WHITE_SQUARES_MAP = build(Side.WHITE);
    ValidateMoveNumberUtility.validateMapOfSet(PAWN_WHITE_SQUARES_MAP, 84);

    PAWN_BLACK_SQUARES_MAP = build(Side.BLACK);
    ValidateMoveNumberUtility.validateMapOfSet(PAWN_BLACK_SQUARES_MAP, 84);
  }

  // The 0-2 diagonal capture squares reachable from each pawn position. Pawns only exist on ranks 2-7.
  private static ImmutableMap<Square, ImmutableSet<Square>> build(Side side) {
    final int rankOffset = side == Side.WHITE ? 1 : -1;
    final EnumMap<Square, ImmutableSet<Square>> map = Nulls.newEnumMap(Square.class);
    for (final Square from : Square.REAL) {
      final int fromFile = from.getFile().getNumber();
      final int fromRank = from.getRank().getNumber();
      if (fromRank < 2 || fromRank > 7) {
        map.put(from, ImmutableSet.of());
        continue;
      }
      final int toRank = fromRank + rankOffset;
      if (toRank < 1 || toRank > 8) {
        map.put(from, ImmutableSet.of());
        continue;
      }
      final ImmutableSet.Builder<Square> builder = ImmutableSet.builder();
      if (fromFile - 1 >= 1) {
        builder.add(Square.calculate(fromFile - 1, toRank));
      }
      if (fromFile + 1 <= 8) {
        builder.add(Square.calculate(fromFile + 1, toRank));
      }
      map.put(from, builder.build());
    }
    return Nulls.copyOfMap(map);
  }

  public static Set<Square> getPawnDiagonalSquares(Side havingMove, Square fromSquare) {
    return switch (havingMove) {
      case BLACK -> Nulls.get(PAWN_BLACK_SQUARES_MAP, fromSquare);
      case WHITE -> Nulls.get(PAWN_WHITE_SQUARES_MAP, fromSquare);
      case NONE -> throw new IllegalArgumentException();
    };
  }

}
