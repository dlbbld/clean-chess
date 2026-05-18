package com.dlb.chess.bitboard;

import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.CastlingMove;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.model.MoveSpecification;

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

  /**
   * Full legal non-castling move generation. Returns the set of legal {@link MoveSpecification}s for {@code side}'s
   * pieces excluding castling — castling lives on {@link com.dlb.chess.board.Board} together with the castling-
   * rights state. The {@code enPassantBit} parameter is the single-bit bitboard of the en-passant target square
   * (or {@code 0L} if no EP is available to {@code side}); the bitboard layer is stateless about whose turn it is.
   *
   * <p>
   * Algorithm: generate the king's legal targets via {@link #legalKingTargets}; if double check, only king moves
   * are legal. Otherwise compute the check-evasion mask (the squares non-king pieces must land on: the checker
   * square plus the squares between king and a sliding checker). For each own non-king piece, take its pseudo-legal
   * targets, intersect with the check-evasion mask and the pin ray (if pinned), and emit
   * {@link MoveSpecification}s. Pawn promotion expands each rank-1/rank-8 target into four moves; en-passant is
   * special-cased for the rank-pin edge case where capturing the EP pawn could expose own king to a rook or queen
   * along the rank.
   */
  public Set<MoveSpecification> legalMoves(Side side, long enPassantBit) {
    if (side != Side.WHITE && side != Side.BLACK) {
      throw new IllegalArgumentException("legalMoves requires Side.WHITE or Side.BLACK, got " + side);
    }
    final Set<MoveSpecification> moves = new TreeSet<>();

    final long ownKings = side == Side.WHITE ? whiteKings : blackKings;
    if (ownKings == 0L) {
      return moves;
    }
    final int kingOrdinal = Long.numberOfTrailingZeros(ownKings);
    final Square kingSquare = Square.REAL.get(kingOrdinal);

    final long kingTargets = legalKingTargets(side);
    addTargetsAsMoves(moves, kingSquare, kingTargets);

    final long checkers = attackersTo(kingSquare, side.getOppositeSide());
    final int checkerCount = Long.bitCount(checkers);
    if (checkerCount >= 2) {
      return moves;
    }

    final long checkEvasionMask;
    if (checkerCount == 1) {
      final int checkerOrdinal = Long.numberOfTrailingZeros(checkers);
      final Piece checker = get(Square.REAL.get(checkerOrdinal));
      final long betweenMask = isSlider(checker.getPieceType())
          ? squaresBetween(kingOrdinal, checkerOrdinal) : 0L;
      checkEvasionMask = checkers | betweenMask;
    } else {
      checkEvasionMask = -1L;
    }

    final long ownPieces = occupied(side);
    final long ownNonKings = ownPieces & ~ownKings;
    final long occ = occupied();
    final long opponentPieces = occupied(side.getOppositeSide());

    long remaining = ownNonKings;
    while (remaining != 0L) {
      final int fromOrdinal = Long.numberOfTrailingZeros(remaining);
      final Square fromSquare = Square.REAL.get(fromOrdinal);
      final Piece piece = get(fromSquare);
      final long pinRay = pinRay(fromSquare, side);
      final long pinFilter = pinRay == 0L ? -1L : pinRay;
      final long combinedMask = checkEvasionMask & pinFilter;

      switch (piece.getPieceType()) {
        case KNIGHT -> addTargetsAsMoves(moves, fromSquare,
            KnightMoves.targets(fromSquare, ownPieces) & combinedMask);
        case BISHOP -> addTargetsAsMoves(moves, fromSquare,
            BishopMoves.targets(fromOrdinal, occ, ownPieces) & combinedMask);
        case ROOK -> addTargetsAsMoves(moves, fromSquare,
            RookMoves.targets(fromOrdinal, occ, ownPieces) & combinedMask);
        case QUEEN -> addTargetsAsMoves(moves, fromSquare,
            QueenMoves.targets(fromOrdinal, occ, ownPieces) & combinedMask);
        case PAWN -> addPawnMoves(moves, fromSquare, fromOrdinal, side, occ, opponentPieces, enPassantBit,
            combinedMask, checkers, checkerCount, kingOrdinal, pinFilter);
        default -> throw new IllegalArgumentException();
      }
      remaining &= remaining - 1L;
    }
    return moves;
  }

  private void addPawnMoves(Set<MoveSpecification> moves, Square fromSquare, int fromOrdinal, Side side, long occ,
      long opponentPieces, long enPassantBit, long combinedMask, long checkers, int checkerCount, int kingOrdinal,
      long pinFilter) {
    final long pushTargets = PawnMoves.pushes(fromOrdinal, occ, side) & combinedMask;
    final long regularCaptureTargets = PawnMoves.captures(fromOrdinal, opponentPieces, 0L, side) & combinedMask;

    long epCaptureTarget = 0L;
    if (enPassantBit != 0L) {
      final long pawnDiagonalAttacks = PawnAttacks.attacks(fromSquare, side);
      if ((pawnDiagonalAttacks & enPassantBit) != 0L) {
        final long capturedPawnBit = side == Side.WHITE ? (enPassantBit >>> 8) : (enPassantBit << 8);
        final boolean epEvadesCheck = checkerCount == 0 || (enPassantBit & combinedMask) != 0L
            || (checkerCount == 1 && capturedPawnBit == checkers);
        final boolean epOnPinRay = (enPassantBit & pinFilter) != 0L;
        if (epEvadesCheck && epOnPinRay
            && !epExposesKing(fromOrdinal, enPassantBit, capturedPawnBit, kingOrdinal, side)) {
          epCaptureTarget = enPassantBit;
        }
      }
    }

    addPawnTargetsWithPromotion(moves, fromSquare, pushTargets);
    addPawnTargetsWithPromotion(moves, fromSquare, regularCaptureTargets | epCaptureTarget);
  }

  private boolean epExposesKing(int fromOrdinal, long enPassantBit, long capturedPawnBit, int kingOrdinal, Side side) {
    final long fromBit = 1L << fromOrdinal;
    final long occAfterEp = (occupied() & ~fromBit & ~capturedPawnBit) | enPassantBit;

    final Side opp = side.getOppositeSide();
    final long oppPawns = (opp == Side.WHITE ? whitePawns : blackPawns) & ~capturedPawnBit;
    final long oppKnights = opp == Side.WHITE ? whiteKnights : blackKnights;
    final long oppBishops = opp == Side.WHITE ? whiteBishops : blackBishops;
    final long oppRooks = opp == Side.WHITE ? whiteRooks : blackRooks;
    final long oppQueens = opp == Side.WHITE ? whiteQueens : blackQueens;
    final long oppKings = opp == Side.WHITE ? whiteKings : blackKings;
    final Square kingSquare = Square.REAL.get(kingOrdinal);

    if ((oppPawns & PawnAttacks.attacks(kingSquare, side)) != 0L) {
      return true;
    }
    if ((oppKnights & KnightAttacks.attacks(kingSquare)) != 0L) {
      return true;
    }
    if (((oppBishops | oppQueens) & BishopAttacks.attacks(kingOrdinal, occAfterEp)) != 0L) {
      return true;
    }
    if (((oppRooks | oppQueens) & RookAttacks.attacks(kingOrdinal, occAfterEp)) != 0L) {
      return true;
    }
    if ((oppKings & KingAttacks.attacks(kingSquare)) != 0L) {
      return true;
    }
    return false;
  }

  private static void addTargetsAsMoves(Set<MoveSpecification> moves, Square fromSquare, long targets) {
    long remaining = targets;
    while (remaining != 0L) {
      final Square toSquare = Square.REAL.get(Long.numberOfTrailingZeros(remaining));
      moves.add(new MoveSpecification(fromSquare, toSquare));
      remaining &= remaining - 1L;
    }
  }

  private static void addPawnTargetsWithPromotion(Set<MoveSpecification> moves, Square fromSquare, long targets) {
    long remaining = targets;
    while (remaining != 0L) {
      final int toOrdinal = Long.numberOfTrailingZeros(remaining);
      final Square toSquare = Square.REAL.get(toOrdinal);
      final int toRank = toOrdinal / 8;
      if (toRank == 0 || toRank == 7) {
        for (final PromotionPieceType promotion : PromotionPieceType.REAL) {
          moves.add(new MoveSpecification(fromSquare, toSquare, promotion));
        }
      } else {
        moves.add(new MoveSpecification(fromSquare, toSquare));
      }
      remaining &= remaining - 1L;
    }
  }

  private static boolean isSlider(PieceType pieceType) {
    return pieceType == PieceType.BISHOP || pieceType == PieceType.ROOK || pieceType == PieceType.QUEEN;
  }

  private static long squaresBetween(int sq1, int sq2) {
    final int file1 = sq1 % 8;
    final int rank1 = sq1 / 8;
    final int file2 = sq2 % 8;
    final int rank2 = sq2 / 8;
    final int fileDiff = file2 - file1;
    final int rankDiff = rank2 - rank1;
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
    long result = 0L;
    int file = file1 + fileStep;
    int rank = rank1 + rankStep;
    while (file != file2 || rank != rank2) {
      result |= 1L << (rank * 8 + file);
      file += fileStep;
      rank += rankStep;
    }
    return result;
  }

  /**
   * Immutable make-move: returns the {@code BitboardPosition} that results from applying {@code moveSpec} to this
   * position with {@code movingSide} as the moving side. Handles regular moves, captures, en-passant capture,
   * promotions (the destination piece becomes {@code moveSpec.promotionPieceType()} fixed to {@code movingSide}),
   * and the piece-movement part of castling (king and rook both move).
   *
   * <p>
   * Castling rights, en-passant target square, side-to-move, and the halfmove / fullmove counters are intentionally
   * NOT updated here — they live on {@link com.dlb.chess.board.Board} / {@link com.dlb.chess.board.DynamicPosition}.
   * This is the piece-placement-only equivalent of
   * {@code StaticPositionUtility.createPositionAfterMove}.
   *
   * <p>
   * The bitboard layer is intentionally stateless about whose turn it is. Callers pass {@code movingSide} explicitly
   * — for castling, this determines which king/rook pair moves; for non-castling moves, it determines the
   * promotion piece's side and the direction of en-passant capture.
   */
  public BitboardPosition afterMove(MoveSpecification moveSpec, Side movingSide) {
    if (movingSide != Side.WHITE && movingSide != Side.BLACK) {
      throw new IllegalArgumentException("afterMove requires Side.WHITE or Side.BLACK, got " + movingSide);
    }
    if (moveSpec.castlingMove() != CastlingMove.NONE) {
      return afterCastling(moveSpec.castlingMove(), movingSide);
    }
    final Square from = moveSpec.fromSquare();
    final Square to = moveSpec.toSquare();
    final Piece movingPiece = get(from);
    final long fromBit = 1L << from.ordinal();
    final long toBit = 1L << to.ordinal();

    final Piece capturedPiece;
    final long capturedBit;
    if ((toBit & occupied()) != 0L) {
      capturedPiece = get(to);
      capturedBit = toBit;
    } else if (movingPiece.getPieceType() == PieceType.PAWN && from.getFile() != to.getFile()) {
      // En-passant: captured pawn sits on the same rank as the capturing pawn, file matching `to`.
      final int capturedOrdinal = movingSide == Side.WHITE ? to.ordinal() - 8 : to.ordinal() + 8;
      capturedBit = 1L << capturedOrdinal;
      capturedPiece = movingSide == Side.WHITE ? Piece.BLACK_PAWN : Piece.WHITE_PAWN;
    } else {
      capturedPiece = Piece.NONE;
      capturedBit = 0L;
    }

    final PromotionPieceType promotion = moveSpec.promotionPieceType();
    final Piece destPiece = promotion == PromotionPieceType.NONE ? movingPiece
        : Piece.calculate(movingSide, promotion.getPieceType());

    final long[] pieces = currentPieceBitboards();
    toggleBit(pieces, movingPiece, fromBit);
    toggleBit(pieces, capturedPiece, capturedBit);
    toggleBit(pieces, destPiece, toBit);
    return fromPieceBitboards(pieces);
  }

  private BitboardPosition afterCastling(CastlingMove castlingMove, Side movingSide) {
    final int kingFromOrdinal;
    final int kingToOrdinal;
    final int rookFromOrdinal;
    final int rookToOrdinal;
    if (movingSide == Side.WHITE) {
      if (castlingMove == CastlingMove.KING_SIDE) {
        kingFromOrdinal = Square.E1.ordinal();
        kingToOrdinal = Square.G1.ordinal();
        rookFromOrdinal = Square.H1.ordinal();
        rookToOrdinal = Square.F1.ordinal();
      } else {
        kingFromOrdinal = Square.E1.ordinal();
        kingToOrdinal = Square.C1.ordinal();
        rookFromOrdinal = Square.A1.ordinal();
        rookToOrdinal = Square.D1.ordinal();
      }
    } else {
      if (castlingMove == CastlingMove.KING_SIDE) {
        kingFromOrdinal = Square.E8.ordinal();
        kingToOrdinal = Square.G8.ordinal();
        rookFromOrdinal = Square.H8.ordinal();
        rookToOrdinal = Square.F8.ordinal();
      } else {
        kingFromOrdinal = Square.E8.ordinal();
        kingToOrdinal = Square.C8.ordinal();
        rookFromOrdinal = Square.A8.ordinal();
        rookToOrdinal = Square.D8.ordinal();
      }
    }
    final Piece kingPiece = movingSide == Side.WHITE ? Piece.WHITE_KING : Piece.BLACK_KING;
    final Piece rookPiece = movingSide == Side.WHITE ? Piece.WHITE_ROOK : Piece.BLACK_ROOK;

    final long[] pieces = currentPieceBitboards();
    toggleBit(pieces, kingPiece, 1L << kingFromOrdinal);
    toggleBit(pieces, kingPiece, 1L << kingToOrdinal);
    toggleBit(pieces, rookPiece, 1L << rookFromOrdinal);
    toggleBit(pieces, rookPiece, 1L << rookToOrdinal);
    return fromPieceBitboards(pieces);
  }

  private long[] currentPieceBitboards() {
    return new long[] { whitePawns, whiteRooks, whiteKnights, whiteBishops, whiteQueens, whiteKings, blackPawns,
        blackRooks, blackKnights, blackBishops, blackQueens, blackKings };
  }

  private static BitboardPosition fromPieceBitboards(long[] pieces) {
    return new BitboardPosition(pieces[0], pieces[1], pieces[2], pieces[3], pieces[4], pieces[5], pieces[6], pieces[7],
        pieces[8], pieces[9], pieces[10], pieces[11]);
  }

  private static void toggleBit(long[] pieces, Piece piece, long bit) {
    final int index = pieceIndex(piece);
    if (index >= 0) {
      pieces[index] ^= bit;
    }
  }

  private static int pieceIndex(Piece piece) {
    return switch (piece) {
      case WHITE_PAWN -> 0;
      case WHITE_ROOK -> 1;
      case WHITE_KNIGHT -> 2;
      case WHITE_BISHOP -> 3;
      case WHITE_QUEEN -> 4;
      case WHITE_KING -> 5;
      case BLACK_PAWN -> 6;
      case BLACK_ROOK -> 7;
      case BLACK_KNIGHT -> 8;
      case BLACK_BISHOP -> 9;
      case BLACK_QUEEN -> 10;
      case BLACK_KING -> 11;
      case NONE -> -1;
      default -> throw new IllegalArgumentException();
    };
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
