package com.dlb.chess.bitboard;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.Square;

/**
 * Twelve-bitboard piece-placement representation: one {@code long} per real
 * {@link com.dlb.chess.board.enums.Piece} value, each bit indexed by
 * {@link com.dlb.chess.board.enums.Square#ordinal()} (little-endian rank-file:
 * {@code A1 = 0, B1 = 1, …, H8 = 63}). Field order matches the
 * {@link com.dlb.chess.board.enums.Piece#REAL} enum order.
 *
 * <p>
 * <b>Construction invariant:</b> the twelve piece bitboards are pairwise disjoint — no square may carry two pieces.
 * The compact constructor enforces this, so any reachable {@code BitboardPosition} is guaranteed consistent under
 * every query method. Attempting to construct one with overlapping bitboards is rejected with
 * {@link IllegalArgumentException}.
 *
 * <p>
 * Built alongside {@link com.dlb.chess.board.StaticPosition} and verified bit-exact against it via differential
 * testing across the full PGN/FEN corpus. See {@code tasks.md} and the package-level Javadoc for the governing
 * invariant: the bitboard backend release is purely additive; the {@code StaticPosition} reference implementation
 * remains the project's correctness ground truth.
 */
public record BitboardPosition(long whitePawns, long whiteRooks, long whiteKnights, long whiteBishops, long whiteQueens,
    long whiteKings, long blackPawns, long blackRooks, long blackKnights, long blackBishops, long blackQueens,
    long blackKings) {

  public BitboardPosition {
    final long union = whitePawns | whiteRooks | whiteKnights | whiteBishops | whiteQueens | whiteKings | blackPawns
        | blackRooks | blackKnights | blackBishops | blackQueens | blackKings;
    final int sumOfBitCounts = Long.bitCount(whitePawns) + Long.bitCount(whiteRooks) + Long.bitCount(whiteKnights)
        + Long.bitCount(whiteBishops) + Long.bitCount(whiteQueens) + Long.bitCount(whiteKings)
        + Long.bitCount(blackPawns) + Long.bitCount(blackRooks) + Long.bitCount(blackKnights)
        + Long.bitCount(blackBishops) + Long.bitCount(blackQueens) + Long.bitCount(blackKings);
    if (sumOfBitCounts != Long.bitCount(union)) {
      throw new IllegalArgumentException("Piece bitboards must be pairwise disjoint (no square may carry two pieces)");
    }
  }

  public static final BitboardPosition INITIAL_POSITION = BitboardPositionUtility
      .fromStaticPosition(StaticPosition.INITIAL_POSITION);

  public static final BitboardPosition EMPTY_POSITION = BitboardPositionUtility
      .fromStaticPosition(StaticPosition.EMPTY_POSITION);

  public Piece get(Square square) {
    final long bit = bitFor(square);
    if ((whitePawns & bit) != 0L) {
      return Piece.WHITE_PAWN;
    }
    if ((whiteRooks & bit) != 0L) {
      return Piece.WHITE_ROOK;
    }
    if ((whiteKnights & bit) != 0L) {
      return Piece.WHITE_KNIGHT;
    }
    if ((whiteBishops & bit) != 0L) {
      return Piece.WHITE_BISHOP;
    }
    if ((whiteQueens & bit) != 0L) {
      return Piece.WHITE_QUEEN;
    }
    if ((whiteKings & bit) != 0L) {
      return Piece.WHITE_KING;
    }
    if ((blackPawns & bit) != 0L) {
      return Piece.BLACK_PAWN;
    }
    if ((blackRooks & bit) != 0L) {
      return Piece.BLACK_ROOK;
    }
    if ((blackKnights & bit) != 0L) {
      return Piece.BLACK_KNIGHT;
    }
    if ((blackBishops & bit) != 0L) {
      return Piece.BLACK_BISHOP;
    }
    if ((blackQueens & bit) != 0L) {
      return Piece.BLACK_QUEEN;
    }
    if ((blackKings & bit) != 0L) {
      return Piece.BLACK_KING;
    }
    return Piece.NONE;
  }

  public boolean isEmpty(Square square) {
    return get(square) == Piece.NONE;
  }

  public long occupied() {
    return whitePawns | whiteRooks | whiteKnights | whiteBishops | whiteQueens | whiteKings | blackPawns | blackRooks
        | blackKnights | blackBishops | blackQueens | blackKings;
  }

  private static long bitFor(Square square) {
    if (square == Square.NONE) {
      throw new IllegalArgumentException("The NONE square does not belong to the board");
    }
    return 1L << square.ordinal();
  }
}
