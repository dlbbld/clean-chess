package com.dlb.chess.test.winnable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.Nulls;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.utility.BasicUtility;
import com.dlb.chess.common.utility.StaticPositionUtility;

public class PawnWall {

  // TODO own pawns outside pawn wall line
  private static final boolean IS_IGNORE_PAWN_OWN_PAWN_OUTSIDE_PAWN_WALL_LINE = true;

  /**
   * Tri-state classifier: returns {@link PawnWallVerdict#YES} when the geometric pawn-wall check accepts the position
   * as a sound permanent barrier, {@link PawnWallVerdict#UNKNOWN} otherwise. See {@code pawn-wall-soundness.md} for the
   * full design, including the BFS test oracle that verifies the geometric verdict on a fixture corpus.
   *
   * <p>
   * The production geometric check is the predicate body of {@link #calculateHasPawnWall(Board)} — the chain-finder
   * across orthogonally-adjacent barrier squares, plus the precheck stack (no R/N/Q, locked pawns, bishop reach).
   * The {@code calculate} method is the recommended entry point; the boolean predicate is retained for source
   * compatibility with existing callers and tests.
   */
  public static PawnWallVerdict calculate(Board board) {
    return calculateHasPawnWall(board) ? PawnWallVerdict.YES : PawnWallVerdict.UNKNOWN;
  }

  public static boolean calculateHasPawnWall(Board board) {

    final StaticPosition staticPosition = board.getStaticPosition();

    // 1) we do not consider positions with rooks, knights or queens
    // 2) we need at least a pawn for a pawn wall (the minimum is most likely much higher, but not having a better
    // assessment for now)
    if (hasAnyPieceType(staticPosition, PieceType.ROOK) || hasAnyPieceType(staticPosition, PieceType.KNIGHT)
        || hasAnyPieceType(staticPosition, PieceType.QUEEN) || !hasAnyPieceType(staticPosition, PieceType.PAWN)) {
      return false;
    }

    // here we branch between having bishops and no bishops
    if (hasAnyPieceType(staticPosition, PieceType.BISHOP)) {
      // we only look at the case if no pawn can move
      if (!calculateIsAllPawnsBlocked(board)) {
        return false;
      }
      // A bishop only threatens the wall if it can actually reach an opponent pawn through a
      // sequence of diagonal moves. Same-square-colour is necessary but not sufficient: a bishop
      // trapped behind its own pawns (e.g. h2 with f4 and d4 of the same side on its diagonals)
      // cannot reach any opponent pawn no matter what colour the opponent pawns sit on.
      for (final Side side : Side.REAL) {
        if (calculateAnyBishopCanReachOpponentPawn(staticPosition, side)) {
          return false;
        }
      }
      return calculateIsHasPawnWallAfterPrecheck(board);
    }

    // from here we only have kings and pawns
    if (!calculateIsAllPawnsBlocked(board)) {
      // 1) if one reason for not blocked is capturing, we give up the analysis, too complex

      // 2) first we check that the non blocked pawns
      // 2a) have another pawn in front of any color
      // 2b) when moving can never capture another pawn

      // 1) and 2a)
      if (!calculateIsAllPawnsCannotCapture(board) || !calculateIsAllPawnsCanReachPawnAhead(board)) {
        return false;
      }

      // 2b)
      // For this we calculate all squares where the pawns as is can capture plus the squares resulting in moving per
      // side. When the intersection is empty we think we hold the promise.
      // TODO Potentially this check is too strong.
      // When there are forced moves so a pawn cannot visit all empty squares
      // ahead, for opponent pawn having to move.
      final Set<Square> attackingSquaresWhite = calculateAttackingSquares(board, Side.WHITE);
      final Set<Square> pawnSquaresBlack = calculatePawnSquares(board, Side.BLACK);
      if (!BasicUtility.calculateIsDisjoint(attackingSquaresWhite, pawnSquaresBlack)) {
        return false;
      }

      final Set<Square> attackingSquaresBlack = calculateAttackingSquares(board, Side.BLACK);
      final Set<Square> pawnSquaresWhite = calculatePawnSquares(board, Side.WHITE);
      if (!BasicUtility.calculateIsDisjoint(attackingSquaresBlack, pawnSquaresWhite)) {
        return false;
      }
    }

    // from here we have all pawns blocked
    return calculateIsHasPawnWallAfterPrecheck(board);

  }

  private static boolean calculateIsHasPawnWallAfterPrecheck(Board board) {
    // check pawn wall line
    final var hasPawnWallWhite = calculateHasPawnWallLine(board, Side.WHITE);
    if (!hasPawnWallWhite) {
      return false;
    }

    final var hasPawnWallBlack = calculateHasPawnWallLine(board, Side.BLACK);
    if (!hasPawnWallBlack) {
      return false;
    }

    // check if both kings are on their side of the board
    final var isKingBehindPawnWallWhite = calculateIsKingBehindPawnWall(board, Side.WHITE);
    if (!isKingBehindPawnWallWhite) {
      return false;
    }

    return calculateIsKingBehindPawnWall(board, Side.BLACK);
  }

  // calculate capturing squares
  protected static Set<Square> calculateAttackingSquares(Board board, Side side) {
    final Set<Square> attackingSquares = new TreeSet<>(calculateAttackingSquareAsIs(board, side));
    attackingSquares.addAll(calculateAttackingSquareAfterMoving(board, side));
    return attackingSquares;
  }

  private static Set<Square> calculateAttackingSquareAsIs(Board board, Side side) {
    final Set<Square> attackingSquaresAsIs = new TreeSet<>();
    for (final Square square : Square.REAL) {
      if (board.getStaticPosition().isOwnPawn(square, side)) {
        if (Square.calculateHasLeftDiagonalSquare(side, square)) {
          final Square squareLeftDiagonal = Square.calculateLeftDiagonalSquare(side, square);
          attackingSquaresAsIs.add(squareLeftDiagonal);
        }
        if (Square.calculateHasRightDiagonalSquare(side, square)) {
          final Square squareRightDiagonal = Square.calculateRightDiagonalSquare(side, square);
          attackingSquaresAsIs.add(squareRightDiagonal);
        }
      }
    }
    return attackingSquaresAsIs;
  }

  private static Set<Square> calculateAttackingSquareAfterMoving(Board board, Side side) {
    final Set<Square> attackingSquaresAll = new TreeSet<>();

    final StaticPosition staticPosition = board.getStaticPosition();
    for (final Square square : Square.REAL) {
      if (board.getStaticPosition().isOwnPawn(square, side)) {
        final Set<Square> attackingSquaresAfterMoving = new TreeSet<>();
        addAttackingSquaresAfterMovingForFile(attackingSquaresAfterMoving, square, staticPosition, side);
        attackingSquaresAll.addAll(attackingSquaresAfterMoving);
      }
    }
    return attackingSquaresAll;
  }

  private static void addAttackingSquaresAfterMovingForFile(Set<Square> foundSquares, Square squareLookAhead,
      StaticPosition staticPosition, Side side) {
    if (Square.calculateHasAheadSquare(side, squareLookAhead)) {
      final Square squareAhead = Square.calculateAheadSquare(side, squareLookAhead);
      if (Rank.calculateIsPromotionRank(side, squareAhead.getRank())) {
        return;
      }
      // now we assume the square is empty
      if (!staticPosition.isEmpty(squareAhead)) {
        final Square ownKingSquare = StaticPositionUtility.calculateKingSquare(staticPosition, side);
        if (squareAhead != ownKingSquare) {
          return;
        }
      }

      if (Square.calculateHasLeftDiagonalSquare(side, squareAhead)) {
        final Square squareLeftDiagonal = Square.calculateLeftDiagonalSquare(side, squareAhead);
        foundSquares.add(squareLeftDiagonal);
      }
      if (Square.calculateHasRightDiagonalSquare(side, squareAhead)) {
        final Square squareRightDiagonal = Square.calculateRightDiagonalSquare(side, squareAhead);
        foundSquares.add(squareRightDiagonal);
      }

      // check if can potentially further advance

      addAttackingSquaresAfterMovingForFile(foundSquares, squareAhead, staticPosition, side);
    }
  }

  // same for pawn squares
  protected static Set<Square> calculatePawnSquares(Board board, Side side) {
    final Set<Square> pawnSquares = new TreeSet<>(calculatePawnSquareAsIs(board, side));
    pawnSquares.addAll(calculatePawnSquareAfterMoving(board, side));
    return pawnSquares;
  }

  private static Set<Square> calculatePawnSquareAsIs(Board board, Side side) {
    final Set<Square> pawnSquaresAsIs = new TreeSet<>();
    for (final Square square : Square.REAL) {
      if (board.getStaticPosition().isOwnPawn(square, side)) {
        pawnSquaresAsIs.add(square);
      }
    }
    return pawnSquaresAsIs;
  }

  private static Set<Square> calculatePawnSquareAfterMoving(Board board, Side side) {
    final Set<Square> pawnSquaresAll = new TreeSet<>();

    final StaticPosition staticPosition = board.getStaticPosition();
    for (final Square square : Square.REAL) {
      if (board.getStaticPosition().isOwnPawn(square, side)) {
        final Set<Square> pawnSquaresAfterMoving = new TreeSet<>();
        addPawnSquaresAfterMovingForFile(pawnSquaresAfterMoving, square, staticPosition, side);
        pawnSquaresAll.addAll(pawnSquaresAfterMoving);
      }
    }
    return pawnSquaresAll;
  }

  private static void addPawnSquaresAfterMovingForFile(Set<Square> foundSquares, Square squareLookAhead,
      StaticPosition staticPosition, Side side) {
    if (Square.calculateHasAheadSquare(side, squareLookAhead)) {
      final Square squareAhead = Square.calculateAheadSquare(side, squareLookAhead);
      if (Rank.calculateIsPromotionRank(side, squareAhead.getRank())) {
        return;
      }
      // now we assume the square is empty
      if (!staticPosition.isEmpty(squareAhead)) {
        final Square ownKingSquare = StaticPositionUtility.calculateKingSquare(staticPosition, side);
        if (squareAhead != ownKingSquare) {
          return;
        }
      }

      foundSquares.add(squareAhead);

      // check if can potentially further advance
      addPawnSquaresAfterMovingForFile(foundSquares, squareAhead, staticPosition, side);
    }
  }

  private static boolean calculateIsKingBehindPawnWall(Board board, Side side) {
    final StaticPosition blockedSquares = calculateBlockedSquares(board, side);
    final Square kingSquare = StaticPositionUtility.calculateKingSquare(board.getStaticPosition(), side);
    return calculateIsKingBehindPawnWall(blockedSquares, kingSquare, side);
  }

  private static boolean calculateIsKingBehindPawnWall(StaticPosition blockedSquares, Square kingSquare, Side side) {
    for (final Square square : Square.REAL) {
      if (square.getFile() == kingSquare.getFile() && blockedSquares.isPawn(square)) {
        switch (side) {
          case WHITE:
            if (square.getRank().getNumber() < kingSquare.getRank().getNumber()) {
              return false;
            }
            break;
          case BLACK:
            if (square.getRank().getNumber() > kingSquare.getRank().getNumber()) {
              return false;
            }
            break;
          case NONE:
          default:
            throw new IllegalArgumentException();
        }
      }
    }
    return true;
  }

  protected static boolean calculateIsAllPawnsHavePawnAhead(Board board) {
    // loop over all square and check each pawn
    final StaticPosition staticPosition = board.getStaticPosition();
    for (final Square square : Square.REAL) {
      if (staticPosition.isPawn(square)) {
        final Piece piece = staticPosition.get(square);
        if (!Square.calculateHasAheadSquare(piece.getSide(), square)) {
          throw new ProgrammingMistakeException(
              "We are not expecting to find pawn on promotion rank, as we only allow legal positions");
        }
        final Square squareAhead = Square.calculateAheadSquare(piece.getSide(), square);
        if (staticPosition.isEmpty(squareAhead)) {
          return false;
        }
        final Piece pieceAhead = staticPosition.get(squareAhead);
        // we only require a piece ahead, no matter which side
        if (pieceAhead.getPieceType() != PieceType.PAWN) {
          return false;
        }
      }
    }
    return true;
  }

  protected static boolean calculateIsAllPawnsCanReachPawnAhead(Board board) {
    // loop over all square and check each pawn
    final StaticPosition staticPosition = board.getStaticPosition();
    for (final Square square : Square.REAL) {
      if (!staticPosition.isEmpty(square)) {
        final Piece piece = staticPosition.get(square);
        if (piece.getPieceType() == PieceType.PAWN && !calculateHasPawnAhead(staticPosition, square, piece.getSide())) {
          return false;
        }
      }
    }
    return true;
  }

  private static boolean calculateHasPawnAhead(StaticPosition staticPosition, Square square, Side side) {

    if (!Square.calculateHasAheadSquare(side, square)) {
      throw new ProgrammingMistakeException("We are not expecting this to happen, as we only allow legal positions");
    }
    final Square squareAhead = Square.calculateAheadSquare(side, square);

    if (Rank.calculateIsPromotionRank(side, squareAhead.getRank())) {
      // the pawn is one square before the promotion rank, there can be no other pawn,
      // for an own pawn would have to be promoted, an opponent pawn cannot start on the opponent's first rank
      return false;
    }
    if (staticPosition.isPawn(squareAhead)) {
      // we only require a piece ahead, no matter which side
      return true;
    }

    // TODO replace unbounded recursion with primitive recursion
    return calculateHasPawnAhead(staticPosition, squareAhead, side);
  }

  protected static boolean calculateIsAllPawnsCannotCapture(Board board) {
    // we must check the en passant capture as not seen by following static checks
    if (board.isEnPassantCapturePossible()) {
      return false;
    }

    // loop over all square and check each pawn
    final StaticPosition staticPosition = board.getStaticPosition();
    for (final Square square : Square.REAL) {
      if (staticPosition.isPawn(square)) {
        final Piece piece = staticPosition.get(square);
        if (Square.calculateHasLeftDiagonalSquare(piece.getSide(), square)) {
          final Square squareLeftDiagonal = Square.calculateLeftDiagonalSquare(piece.getSide(), square);
          if (!staticPosition.isEmpty(squareLeftDiagonal)) {
            // if opponent piece not ok
            final Piece pieceLeftDiagonal = staticPosition.get(squareLeftDiagonal);
            if (pieceLeftDiagonal.getSide() != piece.getSide()) {
              return false;
            }
          }
        }
        if (Square.calculateHasRightDiagonalSquare(piece.getSide(), square)) {
          final Square squareRightDiagonal = Square.calculateRightDiagonalSquare(piece.getSide(), square);
          if (!staticPosition.isEmpty(squareRightDiagonal)) {
            // if opponent piece not ok
            final Piece pieceRightDiagonal = staticPosition.get(squareRightDiagonal);
            if (pieceRightDiagonal.getSide() != piece.getSide()) {
              return false;
            }
          }
        }
      }
    }
    return true;
  }

  protected static boolean calculateIsAllPawnsBlocked(Board board) {
    return calculateIsAllPawnsHavePawnAhead(board) && calculateIsAllPawnsCannotCapture(board);
  }

  private static StaticPosition calculateBlockedSquares(Board board, Side side) {
    final StaticPosition staticPosition = board.getStaticPosition();
    final Square ownKingSquare = StaticPositionUtility.calculateKingSquare(staticPosition, side);

    // we make a board full of white pawns to mark all blocked squares
    StaticPosition calculateBlockedSquares = StaticPosition.EMPTY_POSITION;
    for (final Square square : Square.REAL) {
      if (staticPosition.isOwnPawn(square, side)) {
        // we ignore pawn with empty squares ahead, but such pawns must be checked prior calling to have a pawn
        // ahead
        if (Square.calculateHasAheadSquare(side, square)) {
          final Square squareAhead = Square.calculateAheadSquare(side, square);
          // now we assume the square is empty
          if (staticPosition.isEmpty(squareAhead) || squareAhead == ownKingSquare) {
            // we ignore pawns which can still move forward
            continue;
          }
          calculateBlockedSquares = createChangedPositionIfChange(calculateBlockedSquares, square);
        }
      } else if (staticPosition.isOpponentPawn(square, side)) {
        if (Square.calculateHasLeftDiagonalSquare(side.getOppositeSide(), square)) {
          final Square squareLeftDiagonal = Square.calculateLeftDiagonalSquare(side.getOppositeSide(), square);
          calculateBlockedSquares = createChangedPositionIfChange(calculateBlockedSquares, squareLeftDiagonal);
        }
        if (Square.calculateHasRightDiagonalSquare(side.getOppositeSide(), square)) {
          final Square squareRightDiagonal = Square.calculateRightDiagonalSquare(side.getOppositeSide(), square);
          // TODO how comes we are trying to add a pawn on a square already holding a pawn
          calculateBlockedSquares = createChangedPositionIfChange(calculateBlockedSquares, squareRightDiagonal);
        }
      }
    }
    return calculateBlockedSquares;
  }

  private static StaticPosition createChangedPositionIfChange(StaticPosition staticPosition, Square square) {
    // TODO how comes we are trying to add a pawn on a square already holding a pawn
    if (staticPosition.get(square) != Piece.WHITE_PAWN) {
      return staticPosition.createChangedPosition(square, Piece.WHITE_PAWN);
    }
    return staticPosition;
  }

  protected static boolean calculateHasPawnWallLine(Board board, Side side) {

    final StaticPosition blockedSquares = calculateBlockedSquares(board, side);

    final List<Square> startCandidates = new ArrayList<>();
    final List<Square> leftMostFile = leftmostFile(side);

    for (final Square squareLeftMostFile : leftMostFile) {
      if (!blockedSquares.isEmpty(squareLeftMostFile)) {
        startCandidates.add(squareLeftMostFile);
      }
    }

    final List<Square> currentLine = new ArrayList<>();
    final List<List<Square>> resultList = new ArrayList<>();
    for (final Square squareCandidate : startCandidates) {
      currentLine.add(squareCandidate);
      calculatePawnWallLines(blockedSquares, squareCandidate, true, side, currentLine, resultList);
      currentLine.remove(currentLine.size() - 1);
    }
    return calculateHasPawnWallLine(board.getStaticPosition(), resultList, side);
  }

  private static boolean calculateHasPawnWallLine(StaticPosition staticPosition, List<List<Square>> resultList,
      Side side) {
    // we want all own pawns behind the pawn line for the one example
    for (final List<Square> pawnWallLine : resultList) {
      // we check to find one such line
      if (calculateHasAllPawnsBehindLine(staticPosition, pawnWallLine, side)) {
        return true;
      }
    }
    // no such line found
    return false;
  }

  private static boolean calculateHasAllPawnsBehindLine(StaticPosition staticPosition, List<Square> pawnWallLine,
      Side side) {
    if (IS_IGNORE_PAWN_OWN_PAWN_OUTSIDE_PAWN_WALL_LINE) {
      return true;
    }
    for (final Square pawnWallSquare : pawnWallLine) {
      // we found one element with own pawns not behind, so returning false
      if (!calculateHasAllPawnsBehindSquare(staticPosition, pawnWallSquare, side)) {
        return false;
      }
    }
    // all own pawns behind the line
    return true;
  }

  private static boolean calculateHasAllPawnsBehindSquare(StaticPosition staticPosition, Square squareToCheck,
      Side side) {
    // we want all own pawns behind the pawn line for the one example
    if (Square.calculateHasAheadSquare(side, squareToCheck)) {
      final Square squareAhead = Square.calculateAheadSquare(side, squareToCheck);
      final Piece pieceOnSquareAhead = staticPosition.get(squareAhead);
      if (pieceOnSquareAhead.getPieceType() == PieceType.PAWN && pieceOnSquareAhead.getSide() == side) {
        // found a own pawn outside the pawn wall line
        return false;
      }
      return calculateHasAllPawnsBehindSquare(staticPosition, squareAhead, side);

    }
    return true;
  }

  protected static void calculatePawnWallLines(StaticPosition blockedSquares, Square squareCandidate,
      boolean isNeedNeighbor, Side side, List<Square> currentLine, List<List<Square>> resultList) {
    if (isNeedNeighbor) {
      final Square squareNeighbor = Square.calculateRightSquare(side, squareCandidate);
      if (!blockedSquares.isEmpty(squareNeighbor)) {
        currentLine.add(squareNeighbor);
        if (Square.calculateIsRightMostFile(squareNeighbor, side)) {
          final List<Square> foundPawnWallLine = new ArrayList<>(currentLine);
          resultList.add(foundPawnWallLine);
          return;
        }
        calculatePawnWallLines(blockedSquares, squareNeighbor, false, side, currentLine, resultList);
        currentLine.remove(currentLine.size() - 1);
      }
      return;
    }

    // check all candidates to continue pawn wall

    // first - square behind
    if (Square.calculateHasBehindSquare(side, squareCandidate)) {
      final Square squareBehind = Square.calculateBehindSquare(side, squareCandidate);
      if (!blockedSquares.isEmpty(squareBehind)) {
        currentLine.add(squareBehind);
        calculatePawnWallLines(blockedSquares, squareBehind, true, side, currentLine, resultList);
        currentLine.remove(currentLine.size() - 1);
      }
    }

    // second - square right
    if (Square.calculateHasRightSquare(side, squareCandidate)) {
      final Square squareRight = Square.calculateRightSquare(side, squareCandidate);
      if (!blockedSquares.isEmpty(squareRight)) {
        currentLine.add(squareRight);
        if (Square.calculateIsRightMostFile(squareRight, side)) {
          final List<Square> foundPawnWallLine = new ArrayList<>(currentLine);
          resultList.add(foundPawnWallLine);
          return;
        }
        calculatePawnWallLines(blockedSquares, squareRight, false, side, currentLine, resultList);
        currentLine.remove(currentLine.size() - 1);
      }
    }

    // first - square ahead
    if (Square.calculateHasAheadSquare(side, squareCandidate)) {
      final Square squareAhead = Square.calculateAheadSquare(side, squareCandidate);
      if (!blockedSquares.isEmpty(squareAhead)) {
        currentLine.add(squareAhead);
        calculatePawnWallLines(blockedSquares, squareAhead, true, side, currentLine, resultList);
        currentLine.remove(currentLine.size() - 1);
      }
    }
  }

  /**
   * Returns {@code true} iff any bishop of {@code side} has at least one opponent pawn in its diagonal-reachability set
   * on the current static position. Reachability is computed by BFS from each bishop along all four diagonal
   * directions: empty squares are passed through, opponent pieces can be captured (terminate that ray), own pieces
   * block the bishop entirely.
   *
   * <p>
   * Used by the pawn-wall heuristic: a bishop can only threaten the wall if it can actually capture a pawn that's part
   * of the wall. A bishop trapped behind its own pawns is harmless even if opponent pawns share its square colour.
   */
  private static boolean calculateAnyBishopCanReachOpponentPawn(StaticPosition staticPosition, Side side) {
    for (final Square bishopSquare : Square.REAL) {
      final Piece piece = staticPosition.get(bishopSquare);
      if (piece == Piece.NONE || piece.getSide() != side || piece.getPieceType() != PieceType.BISHOP) {
        continue;
      }
      final Set<Square> reach = calculateBishopReach(staticPosition, bishopSquare, side);
      for (final Square reachSquare : reach) {
        final Piece pieceOnReach = staticPosition.get(reachSquare);
        if (pieceOnReach != Piece.NONE && pieceOnReach.getSide() != side
            && pieceOnReach.getPieceType() == PieceType.PAWN) {
          return true;
        }
      }
    }
    return false;
  }

  private static final int[][] BISHOP_DIAGONALS = { { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 } };

  /**
   * BFS the bishop's transitive diagonal reach from {@code bishopSquare} on the current position. A square is in the
   * returned set if the bishop can stand on it after some sequence of legal bishop moves (sliding through empty squares
   * only; opponent pieces can be the terminal square — captured; own pieces block the ray).
   */
  private static Set<Square> calculateBishopReach(StaticPosition staticPosition, Square bishopSquare, Side side) {
    final Set<Square> reachable = new TreeSet<>();
    final Set<Square> visited = new HashSet<>();
    final Deque<Square> queue = new ArrayDeque<>();
    queue.add(bishopSquare);
    visited.add(bishopSquare);

    while (!queue.isEmpty()) {
      @SuppressWarnings("null") final Square current = queue.poll();
      for (final int[] dir : BISHOP_DIAGONALS) {
        var file = current.getFile().getNumber();
        var rank = current.getRank().getNumber();
        while (true) {
          file += dir[0];
          rank += dir[1];
          if (file < 1 || file > 8 || rank < 1 || rank > 8) {
            break;
          }
          final Square next = Square.calculate(file, rank);
          final Piece pieceOnNext = staticPosition.get(next);
          if (pieceOnNext == Piece.NONE) {
            reachable.add(next);
            if (visited.add(next)) {
              queue.add(next);
            }
          } else {
            if (pieceOnNext.getSide() != side) {
              // opponent piece — bishop can capture (terminal); ray stops here
              reachable.add(next);
            }
            break;
          }
        }
      }
    }
    return reachable;
  }

  private static final List<Square> LEFTMOST_FILE_WHITE = Nulls.listOf(Square.A1, Square.A2, Square.A3, Square.A4,
      Square.A5, Square.A6, Square.A7, Square.A8);

  private static final List<Square> LEFTMOST_FILE_BLACK = Nulls.listOf(Square.H8, Square.H7, Square.H6, Square.H5,
      Square.H4, Square.H3, Square.H2, Square.H1);

  // Local material checks. Inlined from the former public MaterialUtility so that
  // material arithmetic is not re-exposed on the public API surface for this test helper.
  private static boolean hasAnyPieceType(StaticPosition staticPosition, PieceType pieceType) {
    for (final Square boardSquare : Square.REAL) {
      final Piece pieceOnSquare = staticPosition.get(boardSquare);
      if (pieceOnSquare != Piece.NONE && pieceOnSquare.getPieceType() == pieceType) {
        return true;
      }
    }
    return false;
  }

  private static List<Square> leftmostFile(Side side) {
    return switch (side) {
      case WHITE -> LEFTMOST_FILE_WHITE;
      case BLACK -> LEFTMOST_FILE_BLACK;
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }
}
