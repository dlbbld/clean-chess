package com.dlb.chess.bitboard;

/**
 * Twelve-bitboard piece-placement representation: one {@code long} per real
 * {@link com.dlb.chess.board.enums.Piece} value, each bit indexed by
 * {@link com.dlb.chess.board.enums.Square#ordinal()} (little-endian rank-file:
 * {@code A1 = 0, B1 = 1, …, H8 = 63}). Field order matches the
 * {@link com.dlb.chess.board.enums.Piece#REAL} enum order.
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
}
