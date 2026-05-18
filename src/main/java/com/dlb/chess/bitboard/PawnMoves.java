package com.dlb.chess.bitboard;

import com.dlb.chess.board.enums.Side;

/**
 * Pseudo-legal pawn target squares — pushes only at this stage. Single-push lands one rank forward iff that square
 * is empty; double-push lands two ranks forward iff the pawn is on its starting rank and both intermediate and
 * target squares are empty. Promotion targets are included (the bitboard does not distinguish — promotion is the
 * caller's concern). Captures (diagonal, including en-passant) come in {@code PawnCaptures} (Phase 5.3).
 *
 * <p>
 * Side parameter is required: a "pawn" with no side has no defined forward direction.
 */
public final class PawnMoves {

  private PawnMoves() {
  }

  public static long pushes(int squareOrdinal, long occupied, Side side) {
    if (squareOrdinal < 0 || squareOrdinal >= 64) {
      throw new IllegalArgumentException("squareOrdinal out of range: " + squareOrdinal);
    }
    if (side != Side.WHITE && side != Side.BLACK) {
      throw new IllegalArgumentException("pushes requires Side.WHITE or Side.BLACK, got " + side);
    }

    final int forwardOrdinal = side == Side.WHITE ? squareOrdinal + 8 : squareOrdinal - 8;
    if (forwardOrdinal < 0 || forwardOrdinal >= 64) {
      return 0L;
    }
    final long forwardBit = 1L << forwardOrdinal;
    if ((forwardBit & occupied) != 0L) {
      return 0L;
    }

    long result = forwardBit;

    final int fromRank = squareOrdinal / 8;
    final boolean onStartingRank = side == Side.WHITE ? fromRank == 1 : fromRank == 6;
    if (onStartingRank) {
      final int doubleOrdinal = side == Side.WHITE ? squareOrdinal + 16 : squareOrdinal - 16;
      final long doubleBit = 1L << doubleOrdinal;
      if ((doubleBit & occupied) == 0L) {
        result |= doubleBit;
      }
    }

    return result;
  }
}
