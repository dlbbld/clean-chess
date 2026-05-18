package com.dlb.chess.bitboard;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;

/**
 * Precomputed pawn-attack tables, one {@code long[64]} per side. {@link #attacks(Square, Side)} returns the diagonal
 * capture squares for a pawn of the given side on the given from-square.
 *
 * <p>
 * Mirrors {@code PawnDiagonalSquares} on the {@link com.dlb.chess.board.StaticPosition} side, including its convention
 * that pawns only legally exist on ranks 2–7: from-squares on rank 1 or rank 8 return {@code 0L}, matching the
 * reference's empty {@code Set<Square>}. Differential-tested bit-exact.
 */
public final class PawnAttacks {

  private static final long[] WHITE_ATTACKS = computeAttacks(Side.WHITE);
  private static final long[] BLACK_ATTACKS = computeAttacks(Side.BLACK);

  private static long[] computeAttacks(Side side) {
    final long[] table = new long[64];
    final int rankOffset = side == Side.WHITE ? 1 : -1;
    for (int squareOrdinal = 0; squareOrdinal < 64; squareOrdinal++) {
      final int fromFile = squareOrdinal % 8;
      final int fromRank = squareOrdinal / 8;
      // Pawns legally exist only on ranks 2-7 (= 0-indexed rank 1-6). Match the reference.
      if (fromRank < 1 || fromRank > 6) {
        continue;
      }
      final int toRank = fromRank + rankOffset;
      long attacks = 0L;
      if (fromFile - 1 >= 0) {
        attacks |= 1L << (toRank * 8 + (fromFile - 1));
      }
      if (fromFile + 1 <= 7) {
        attacks |= 1L << (toRank * 8 + (fromFile + 1));
      }
      table[squareOrdinal] = attacks;
    }
    return table;
  }

  private PawnAttacks() {
  }

  public static long attacks(Square square, Side side) {
    if (square == Square.NONE) {
      throw new IllegalArgumentException("The NONE square does not belong to the board");
    }
    return switch (side) {
      case WHITE -> WHITE_ATTACKS[square.ordinal()];
      case BLACK -> BLACK_ATTACKS[square.ordinal()];
      case NONE -> throw new IllegalArgumentException("Side.NONE does not have pawn attacks");
      default -> throw new IllegalArgumentException();
    };
  }
}
