package com.dlb.chess.test.winnable;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumSet;
import java.util.Set;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.utility.StaticPositionUtility;

/**
 * Test-only BFS oracle for the pawn-wall classifier.
 *
 * <p>
 * The production {@link PawnWall} predicate uses a geometric chain check. This oracle is the second opinion: a
 * king-walk BFS that asks whether the side's king can reach any opposing pawn through passable squares. If the
 * oracle reports the king can reach an opposing pawn while the geometric check returned {@link PawnWallVerdict#YES},
 * the geometric check has a false positive on that fixture — a test failure.
 *
 * <p>
 * <b>Passable squares</b>: empty squares; squares occupied by same-side non-pawn pieces (they can move out of the
 * way in a future move, opponent cooperating); squares occupied by undefended opposing pawns (the king captures them).
 *
 * <p>
 * <b>Impassable squares</b>: squares occupied by same-side pawns (locked in a wall configuration); squares attacked
 * by any opposing pawn (king cannot enter check); squares occupied by opposing pawns that are defended by another
 * opposing pawn.
 *
 * <p>
 * "Defended by another opposing pawn" specifically — defence by an opposing piece (bishop, etc.) does not count as
 * permanent because the piece can be moved away. Only pawn defence is structural in a locked-wall configuration.
 *
 * <p>
 * See {@code pawn-wall-soundness.md} for the full design and the asymmetric agreement contract
 * ({@code geometric_YES ⟹ BFS_YES}).
 */
final class PawnWallKingWalkOracle {

  private PawnWallKingWalkOracle() {
  }

  /**
   * Returns {@code true} iff the king of {@code side} cannot reach any opposing pawn through passable squares — i.e.
   * the king is trapped behind a permanent barrier under the helpmate-cooperation movement model.
   */
  static boolean isKingTrappedBehindPermanentBarrier(Board board, Side side) {
    final StaticPosition position = board.getStaticPosition();
    final Side opponent = side.getOppositeSide();
    final Set<Square> opposingPawnAttacks = calculatePawnAttacks(position, opponent);
    final Set<Square> opposingPawnDefendedSquares = calculatePawnAttacks(position, opponent);
    // Note: a pawn defends squares it attacks; opposingPawnAttacks and opposingPawnDefendedSquares are the same set.
    // The two names are kept for readability — one is used for "the king cannot enter this attacked square", the
    // other for "an opposing pawn on this square is defended."

    final Square kingSquare = StaticPositionUtility.calculateKingSquare(position, side);
    final Set<Square> visited = EnumSet.noneOf(Square.class);
    final Deque<Square> queue = new ArrayDeque<>();
    visited.add(kingSquare);
    queue.add(kingSquare);

    while (!queue.isEmpty()) {
      final Square current = queue.poll();
      if (current == null) {
        continue;
      }
      for (final Square neighbour : kingNeighbours(current)) {
        if (visited.contains(neighbour)) {
          continue;
        }
        final Piece piece = position.get(neighbour);
        // Same-side pawn — impassable.
        if (piece != Piece.NONE && piece.getSide() == side && piece.getPieceType() == PieceType.PAWN) {
          continue;
        }
        // Attacked by opposing pawn — king cannot enter check.
        if (opposingPawnAttacks.contains(neighbour)) {
          continue;
        }
        // Opposing pawn — passable iff undefended (king captures); impassable iff defended by another opposing pawn.
        if (piece != Piece.NONE && piece.getSide() == opponent && piece.getPieceType() == PieceType.PAWN) {
          if (opposingPawnDefendedSquares.contains(neighbour)) {
            // Defended by another opposing pawn — impassable.
            continue;
          }
          // Undefended opposing pawn — king reaches an opposing pawn. Wall breachable.
          return false;
        }
        // Empty, same-side non-pawn piece, or opposing non-pawn piece (king can capture) — passable.
        visited.add(neighbour);
        queue.add(neighbour);
      }
    }
    // BFS exhausted without reaching any opposing pawn — king is trapped.
    return true;
  }

  /**
   * Returns the set of squares attacked by pawns of {@code side}. A pawn at square {@code s} attacks the two squares
   * one rank forward (from {@code side}'s perspective) and one file left or right.
   */
  private static Set<Square> calculatePawnAttacks(StaticPosition position, Side side) {
    final Set<Square> attacks = EnumSet.noneOf(Square.class);
    for (final Square square : Square.REAL) {
      if (!position.isOwnPawn(square, side)) {
        continue;
      }
      if (Square.calculateHasLeftDiagonalSquare(side, square)) {
        attacks.add(Square.calculateLeftDiagonalSquare(side, square));
      }
      if (Square.calculateHasRightDiagonalSquare(side, square)) {
        attacks.add(Square.calculateRightDiagonalSquare(side, square));
      }
    }
    return attacks;
  }

  /**
   * Returns the up-to-eight king-move neighbours of {@code square} (all squares within one file and one rank).
   */
  private static EnumSet<Square> kingNeighbours(Square square) {
    final EnumSet<Square> neighbours = EnumSet.noneOf(Square.class);
    final int fileNumber = square.getFile().getNumber();
    final int rankNumber = square.getRank().getNumber();
    for (int df = -1; df <= 1; df++) {
      for (int dr = -1; dr <= 1; dr++) {
        if (df == 0 && dr == 0) {
          continue;
        }
        final int newFile = fileNumber + df;
        final int newRank = rankNumber + dr;
        if (newFile < 1 || newFile > 8 || newRank < 1 || newRank > 8) {
          continue;
        }
        neighbours.add(Square.calculate(newFile, newRank));
      }
    }
    return neighbours;
  }

}
