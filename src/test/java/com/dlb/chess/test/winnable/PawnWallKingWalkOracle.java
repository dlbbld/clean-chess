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
 * king-walk BFS asking whether the side's king can reach the opposing king's square through passable squares — i.e.
 * whether the wall is a genuine separator between the two kings.
 *
 * <p>
 * <b>Passable squares</b>: empty squares; squares occupied by same-side non-pawn pieces (they can move out of the way
 * in a future move, opponent cooperating); squares occupied by undefended opposing pawns (the king captures them and
 * continues from the captured square); squares occupied by opposing non-pawn pieces (king captures). The opposing
 * king's square is treated as the BFS target — if the king's reach extends to it, the wall isn't a separator.
 *
 * <p>
 * <b>Impassable squares</b>: squares occupied by same-side pawns (locked in a wall configuration); squares attacked
 * by any opposing pawn. The "attacked by opposing pawn" check covers both empty attacked squares (king can't enter
 * check) and opposing pawns defended by another opposing pawn (the defender's attack also hits the defended pawn's
 * square — capture would be re-captured).
 *
 * <p>
 * The BFS uses a static attack model: opposing-pawn attacks don't update when the king captures an opposing pawn.
 * This is conservative — squares previously attacked by a now-captured pawn remain marked impassable. Sound for the
 * "king trapped" claim; may declare some positions trapped that a dynamic search would clear.
 *
 * <p>
 * See {@code pawn-wall-soundness.md} for the full design and the asymmetric agreement contract
 * ({@code geometric_YES ⟹ BFS_YES}).
 */
final class PawnWallKingWalkOracle {

  private PawnWallKingWalkOracle() {
  }

  /**
   * Returns {@code true} iff the king of {@code side} cannot reach the opposing king's square through passable
   * squares — i.e. the wall topologically separates the two kings under the helpmate-cooperation movement model.
   */
  static boolean isKingTrappedBehindPermanentBarrier(Board board, Side side) {
    final StaticPosition position = board.getStaticPosition();
    final Side opponent = side.getOppositeSide();
    final Set<Square> opposingPawnAttacks = calculatePawnAttacks(position, opponent);

    final Square ownKingSquare = StaticPositionUtility.calculateKingSquare(position, side);
    final Square opposingKingSquare = StaticPositionUtility.calculateKingSquare(position, opponent);

    final Set<Square> visited = EnumSet.noneOf(Square.class);
    final Deque<Square> queue = new ArrayDeque<>();
    visited.add(ownKingSquare);
    queue.add(ownKingSquare);

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
        // Impassable: same-side pawn (locked in a wall configuration).
        if (piece != Piece.NONE && piece.getSide() == side && piece.getPieceType() == PieceType.PAWN) {
          continue;
        }
        // Impassable: attacked by an opposing pawn. Covers both empty attacked squares (king can't enter check) and
        // opposing pawns defended by another opposing pawn (the defender's attack also hits the defended pawn's
        // square - capture would be re-captured).
        if (opposingPawnAttacks.contains(neighbour)) {
          continue;
        }
        // Passable: everything else (empty; own non-pawn piece; undefended opposing pawn; opposing non-pawn piece;
        // opposing king's square). Captures are modelled by continuing BFS from the captured-piece's square.
        visited.add(neighbour);
        queue.add(neighbour);
      }
    }
    // King is trapped iff the BFS reach does not include the opposing king's square.
    return !visited.contains(opposingKingSquare);
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
