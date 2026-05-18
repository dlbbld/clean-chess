package com.dlb.chess.bitboard;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Side;
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

  public long occupied(Side side) {
    return switch (side) {
      case WHITE -> whitePawns | whiteRooks | whiteKnights | whiteBishops | whiteQueens | whiteKings;
      case BLACK -> blackPawns | blackRooks | blackKnights | blackBishops | blackQueens | blackKings;
      case NONE -> throw new IllegalArgumentException("Side.NONE has no occupancy");
      default -> throw new IllegalArgumentException();
    };
  }

  /**
   * Union of all squares attacked / defended by {@code side}'s pieces, in the same "isAllowOwnPiece = true" sense
   * the reference uses: includes squares occupied by own pieces (those are defended). Differential-tested against
   * {@code AbstractAttackedSquares.calculateAttackedSquares}.
   */
  public long attackedSquares(Side side) {
    return attackedSquares(side, occupied());
  }

  /**
   * Same as {@link #attackedSquares(Side)} but computed against a caller-supplied occupancy mask rather than the
   * record's own {@link #occupied()}. Used by king-safety calculations in legal-move generation, which need to ask
   * "what would the opponent attack if my king were not on the board" so a slider's ray correctly projects through
   * the king's current square onto squares the king might move to.
   *
   * <p>
   * Only sliding-piece attacks (bishop, rook, queen) consult {@code occupiedOverride}; non-sliders (knight, king,
   * pawn) ignore it. Callers wanting "no king" semantics should pass {@code occupied() ^ kingBits}.
   */
  public long attackedSquares(Side side, long occupiedOverride) {
    if (side != Side.WHITE && side != Side.BLACK) {
      throw new IllegalArgumentException("attackedSquares requires Side.WHITE or Side.BLACK, got " + side);
    }
    final boolean white = side == Side.WHITE;
    final long pawns = white ? whitePawns : blackPawns;
    final long knights = white ? whiteKnights : blackKnights;
    final long bishops = white ? whiteBishops : blackBishops;
    final long rooks = white ? whiteRooks : blackRooks;
    final long queens = white ? whiteQueens : blackQueens;
    final long kings = white ? whiteKings : blackKings;

    long attacks = 0L;

    long remaining = pawns;
    while (remaining != 0L) {
      attacks |= PawnAttacks.attacks(Square.REAL.get(Long.numberOfTrailingZeros(remaining)), side);
      remaining &= remaining - 1L;
    }

    remaining = knights;
    while (remaining != 0L) {
      attacks |= KnightAttacks.attacks(Square.REAL.get(Long.numberOfTrailingZeros(remaining)));
      remaining &= remaining - 1L;
    }

    remaining = bishops;
    while (remaining != 0L) {
      attacks |= BishopAttacks.attacks(Long.numberOfTrailingZeros(remaining), occupiedOverride);
      remaining &= remaining - 1L;
    }

    remaining = rooks;
    while (remaining != 0L) {
      attacks |= RookAttacks.attacks(Long.numberOfTrailingZeros(remaining), occupiedOverride);
      remaining &= remaining - 1L;
    }

    remaining = queens;
    while (remaining != 0L) {
      attacks |= QueenAttacks.attacks(Long.numberOfTrailingZeros(remaining), occupiedOverride);
      remaining &= remaining - 1L;
    }

    remaining = kings;
    while (remaining != 0L) {
      attacks |= KingAttacks.attacks(Square.REAL.get(Long.numberOfTrailingZeros(remaining)));
      remaining &= remaining - 1L;
    }

    return attacks;
  }

  /**
   * Returns {@code true} if {@code side}'s king is attacked by any of the opposite side's pieces. A position with no
   * king of the queried side returns {@code false} (no king to be in check) — the reference's
   * {@code StaticPositionUtility.calculateIsCheck} throws in that case, so callers comparing against the reference
   * should restrict comparisons to positions where the king of the queried side exists.
   */
  public boolean isInCheck(Side side) {
    if (side != Side.WHITE && side != Side.BLACK) {
      throw new IllegalArgumentException("isInCheck requires Side.WHITE or Side.BLACK, got " + side);
    }
    final long ownKings = (side == Side.WHITE) ? whiteKings : blackKings;
    if (ownKings == 0L) {
      return false;
    }
    return (attackedSquares(side.getOppositeSide()) & ownKings) != 0L;
  }

  /**
   * Bitboard of {@code side}'s pieces that attack {@code square}. Uses the "reverse attack" identity: a piece of
   * colour C attacks X iff X is in that piece's attack set. By the symmetry of all non-pawn piece-type attacks, that
   * is equivalent to "the piece sits on a square in the attack set of X" — and pawns are handled by querying the
   * opposite-colour pawn attack set from X (a white pawn at S attacks X iff a black pawn at X would attack S).
   *
   * <p>
   * Used in Phase 6 for pin detection. No direct production counterpart; the differential test derives the reference
   * by enumerating own pieces and asking each whether its attack set contains the target.
   */
  /**
   * Bitboard of legal non-castling king target squares for {@code side}. A target is legal iff (a) it is a
   * pseudo-legal target — surrounding square not occupied by own piece — and (b) it is not attacked by the opposite
   * side after the king vacates its current square. The "king vacates" part is essential: a slider that the king
   * was blocking would, post-move, project its ray through the king's old square onto squares the king might try
   * to move to (the XRAY case). The implementation removes own kings from the occupied mask before computing
   * opponent attacks.
   *
   * <p>
   * Captures of opponent pieces are included when the captured piece's square is not defended by another opponent
   * piece. Standard chess assumes one king per side; the implementation tolerates multiple kings (taking the union
   * of legal targets from each) for the differential test's robustness.
   */
  public long legalKingTargets(Side side) {
    if (side != Side.WHITE && side != Side.BLACK) {
      throw new IllegalArgumentException("legalKingTargets requires Side.WHITE or Side.BLACK, got " + side);
    }
    final long ownKings = side == Side.WHITE ? whiteKings : blackKings;
    if (ownKings == 0L) {
      return 0L;
    }
    final long ownPieces = occupied(side);
    final long occupiedWithoutOwnKings = occupied() ^ ownKings;
    final long opponentAttacks = attackedSquares(side.getOppositeSide(), occupiedWithoutOwnKings);

    long legalTargets = 0L;
    long remaining = ownKings;
    while (remaining != 0L) {
      final Square kingSquare = Square.REAL.get(Long.numberOfTrailingZeros(remaining));
      legalTargets |= KingMoves.targets(kingSquare, ownPieces) & ~opponentAttacks;
      remaining &= remaining - 1L;
    }
    return legalTargets;
  }

  /**
   * Pin ray for the piece on {@code pinnedSquare} relative to {@code side}'s king: the squares from king (exclusive)
   * to pinner (inclusive) along the line through {@code pinnedSquare}. Returns {@code 0L} if the piece is not
   * pinned. The pinned piece's legal-move filter is {@code pseudoLegal & pinRay(pinnedSquare, side)} — the piece may
   * move along the pin line (capturing the pinner is allowed; vacating the line is not).
   *
   * <p>
   * Pinning requires: pinned piece on a file / rank / diagonal with own king, no other piece between king and the
   * pinned piece, and the first piece beyond the pinned piece (in the same direction) is an opposite-side slider
   * whose move type matches the direction (bishop / queen on a diagonal; rook / queen on a file or rank). Standard
   * chess one-king-per-side assumed; with no own king of {@code side}, returns {@code 0L}.
   */
  public long pinRay(Square pinnedSquare, Side side) {
    if (pinnedSquare == Square.NONE) {
      throw new IllegalArgumentException("The NONE square does not belong to the board");
    }
    if (side != Side.WHITE && side != Side.BLACK) {
      throw new IllegalArgumentException("pinRay requires Side.WHITE or Side.BLACK, got " + side);
    }
    final long ownKings = side == Side.WHITE ? whiteKings : blackKings;
    if (ownKings == 0L) {
      return 0L;
    }
    final int kingOrdinal = Long.numberOfTrailingZeros(ownKings);
    final int pinnedOrdinal = pinnedSquare.ordinal();
    final int kingFile = kingOrdinal % 8;
    final int kingRank = kingOrdinal / 8;
    final int pinnedFile = pinnedOrdinal % 8;
    final int pinnedRank = pinnedOrdinal / 8;
    final int fileDiff = pinnedFile - kingFile;
    final int rankDiff = pinnedRank - kingRank;
    if (fileDiff == 0 && rankDiff == 0) {
      return 0L;
    }
    final boolean onFile = fileDiff == 0;
    final boolean onRank = rankDiff == 0;
    final boolean onDiagonal = !onFile && !onRank && Math.abs(fileDiff) == Math.abs(rankDiff);
    if (!onFile && !onRank && !onDiagonal) {
      return 0L;
    }

    final int fileStep = Integer.signum(fileDiff);
    final int rankStep = Integer.signum(rankDiff);
    final long occ = occupied();

    int file = kingFile + fileStep;
    int rank = kingRank + rankStep;
    while (file != pinnedFile || rank != pinnedRank) {
      if ((occ & (1L << (rank * 8 + file))) != 0L) {
        return 0L;
      }
      file += fileStep;
      rank += rankStep;
    }

    file = pinnedFile + fileStep;
    rank = pinnedRank + rankStep;
    while (file >= 0 && file < 8 && rank >= 0 && rank < 8) {
      final int beyondOrdinal = rank * 8 + file;
      if ((occ & (1L << beyondOrdinal)) != 0L) {
        final Piece beyondPiece = get(Square.REAL.get(beyondOrdinal));
        if (beyondPiece.getSide() == side) {
          return 0L;
        }
        final PieceType beyondPieceType = beyondPiece.getPieceType();
        final boolean diagonalMover = beyondPieceType == PieceType.BISHOP || beyondPieceType == PieceType.QUEEN;
        final boolean orthogonalMover = beyondPieceType == PieceType.ROOK || beyondPieceType == PieceType.QUEEN;
        if ((onDiagonal && diagonalMover) || ((onFile || onRank) && orthogonalMover)) {
          return inclusiveRayFromKing(kingOrdinal, beyondOrdinal);
        }
        return 0L;
      }
      file += fileStep;
      rank += rankStep;
    }
    return 0L;
  }

  /**
   * Bitboard of own pieces of {@code side} that are pinned to their king. A piece is pinned iff
   * {@link #pinRay(Square, Side)} returns a non-zero ray for that square.
   */
  public long pinnedPieces(Side side) {
    if (side != Side.WHITE && side != Side.BLACK) {
      throw new IllegalArgumentException("pinnedPieces requires Side.WHITE or Side.BLACK, got " + side);
    }
    final long ownKings = side == Side.WHITE ? whiteKings : blackKings;
    if (ownKings == 0L) {
      return 0L;
    }
    final long ownNonKings = occupied(side) & ~ownKings;
    long pinned = 0L;
    long remaining = ownNonKings;
    while (remaining != 0L) {
      final long pieceBit = Long.lowestOneBit(remaining);
      final Square pieceSquare = Square.REAL.get(Long.numberOfTrailingZeros(pieceBit));
      if (pinRay(pieceSquare, side) != 0L) {
        pinned |= pieceBit;
      }
      remaining &= ~pieceBit;
    }
    return pinned;
  }

  private static long inclusiveRayFromKing(int kingOrdinal, int pinnerOrdinal) {
    final int kingFile = kingOrdinal % 8;
    final int kingRank = kingOrdinal / 8;
    final int pinnerFile = pinnerOrdinal % 8;
    final int pinnerRank = pinnerOrdinal / 8;
    final int fileStep = Integer.signum(pinnerFile - kingFile);
    final int rankStep = Integer.signum(pinnerRank - kingRank);
    long result = 0L;
    int file = kingFile + fileStep;
    int rank = kingRank + rankStep;
    while (file != pinnerFile || rank != pinnerRank) {
      result |= 1L << (rank * 8 + file);
      file += fileStep;
      rank += rankStep;
    }
    result |= 1L << pinnerOrdinal;
    return result;
  }

  public long attackersTo(Square square, Side side) {
    if (square == Square.NONE) {
      throw new IllegalArgumentException("The NONE square does not belong to the board");
    }
    if (side != Side.WHITE && side != Side.BLACK) {
      throw new IllegalArgumentException("attackersTo requires Side.WHITE or Side.BLACK, got " + side);
    }
    final int squareOrdinal = square.ordinal();
    final long occ = occupied();

    final boolean white = side == Side.WHITE;
    final long pawns = white ? whitePawns : blackPawns;
    final long knights = white ? whiteKnights : blackKnights;
    final long bishops = white ? whiteBishops : blackBishops;
    final long rooks = white ? whiteRooks : blackRooks;
    final long queens = white ? whiteQueens : blackQueens;
    final long kings = white ? whiteKings : blackKings;

    long attackers = 0L;
    attackers |= pawns & PawnAttacks.attacks(square, side.getOppositeSide());
    attackers |= knights & KnightAttacks.attacks(square);
    attackers |= bishops & BishopAttacks.attacks(squareOrdinal, occ);
    attackers |= rooks & RookAttacks.attacks(squareOrdinal, occ);
    attackers |= queens & QueenAttacks.attacks(squareOrdinal, occ);
    attackers |= kings & KingAttacks.attacks(square);
    return attackers;
  }

  private static long bitFor(Square square) {
    if (square == Square.NONE) {
      throw new IllegalArgumentException("The NONE square does not belong to the board");
    }
    return 1L << square.ordinal();
  }
}
