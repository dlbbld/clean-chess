package com.dlb.chess.squares.to.range;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.enums.SquareOccupation;
import com.dlb.chess.range.DiagonalRange;
import com.dlb.chess.range.OrthogonalRange;
import com.dlb.chess.squares.to.AbstractToSquares;
import com.google.common.collect.ImmutableList;

/**
 * Calculated to defined as all squares the piece can move to, being empty, occupied by an opponent piece or by
 * parameterized allowing or disallowing an own piece, in the case of rook, bishop or queen additionally additionally
 * requiring that the square is "visible", that is the squares between the from and to squares are all empty and in the
 * case of pawns excluding the diagonal moves (for the reason, that these moves requires additional conditions, contrary
 * to the other pieces).
 */
public abstract class AbstractRangeSquares extends AbstractToSquares {

  public static Set<Square> calculateOrthogonalRangeSquare(StaticPosition staticPosition, Side havingMove,
      Square fromSquare, PieceType expectedSourcePieceType, OrthogonalRange orthogonalMoves, boolean isAllowOwnPiece) {

    final Set<Square> calculatedToSquareSet = new TreeSet<>(calculateRangeSquare(staticPosition, havingMove, fromSquare,
        expectedSourcePieceType, orthogonalMoves.squareListNorth(), isAllowOwnPiece));

    calculatedToSquareSet.addAll(calculateRangeSquare(staticPosition, havingMove, fromSquare, expectedSourcePieceType,
        orthogonalMoves.squareListEast(), isAllowOwnPiece));
    calculatedToSquareSet.addAll(calculateRangeSquare(staticPosition, havingMove, fromSquare, expectedSourcePieceType,
        orthogonalMoves.squareListSouth(), isAllowOwnPiece));
    calculatedToSquareSet.addAll(calculateRangeSquare(staticPosition, havingMove, fromSquare, expectedSourcePieceType,
        orthogonalMoves.squareListWest(), isAllowOwnPiece));
    return calculatedToSquareSet;
  }

  public static Set<Square> calculateDiagonalRangeSquare(StaticPosition staticPosition, Side havingMove,
      Square fromSquare, PieceType expectedSourcePieceType, DiagonalRange diagonalMoves, boolean isAllowOwnPiece) {

    final Set<Square> calculatedToSquareSet = new TreeSet<>(calculateRangeSquare(staticPosition, havingMove, fromSquare,
        expectedSourcePieceType, diagonalMoves.squareListNorthEast(), isAllowOwnPiece));

    calculatedToSquareSet.addAll(calculateRangeSquare(staticPosition, havingMove, fromSquare, expectedSourcePieceType,
        diagonalMoves.squareListSouthEast(), isAllowOwnPiece));
    calculatedToSquareSet.addAll(calculateRangeSquare(staticPosition, havingMove, fromSquare, expectedSourcePieceType,
        diagonalMoves.squareListSouthWest(), isAllowOwnPiece));
    calculatedToSquareSet.addAll(calculateRangeSquare(staticPosition, havingMove, fromSquare, expectedSourcePieceType,
        diagonalMoves.squareListNorthWest(), isAllowOwnPiece));
    return calculatedToSquareSet;
  }

  private static Set<Square> calculateRangeSquare(StaticPosition staticPosition, Side havingMove, Square fromSquare,
      PieceType expectedSourcePieceType, ImmutableList<Square> emptyBoardSquareList, boolean isAllowOwnPiece) {

    checkPiece(staticPosition, havingMove, fromSquare, expectedSourcePieceType);

    final Set<Square> calculatedToSquareSet = new TreeSet<>();

    final List<Square> calculatedToSquareList = calculateRangeSquareList(staticPosition, havingMove,
        emptyBoardSquareList, isAllowOwnPiece);
    calculatedToSquareSet.addAll(calculatedToSquareList);
    return calculatedToSquareSet;
  }

  public static Set<Square> calculateRangeSquare(StaticPosition staticPosition, Side havingMove, Square fromSquare,
      boolean isAllowOwnPiece) {

    final Piece piece = staticPosition.get(fromSquare);

    if (piece == Piece.NONE) {
      throw new IllegalArgumentException();
    }
    return switch (piece.getPieceType()) {
      case BISHOP -> BishopRangeSquares.calculateBishopRangeSquares(staticPosition, fromSquare, havingMove,
          isAllowOwnPiece);
      case QUEEN -> QueenRangeSquares.calculateQueenRangeSquares(staticPosition, fromSquare, havingMove,
          isAllowOwnPiece);
      case ROOK -> RookRangeSquares.calculateRookRangeSquares(staticPosition, fromSquare, havingMove, isAllowOwnPiece);
      case KING, KNIGHT, PAWN, NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };

  }

  private static List<Square> calculateRangeSquareList(StaticPosition staticPosition, Side havingMove,
      List<Square> emptyBoardSquareList, boolean isAllowOwnPiece) {

    final List<Square> calculatedToSquareList = new ArrayList<>();

    for (final Square toSquare : emptyBoardSquareList) {
      final SquareOccupation squareOccupation = calculateSquareOccupation(staticPosition, havingMove, toSquare);
      switch (squareOccupation) {
        case NONE:
          calculatedToSquareList.add(toSquare);
          continue;
        case OPPONENT_PIECE:
          calculatedToSquareList.add(toSquare);
          return calculatedToSquareList;
        case OWN_PIECE:
          if (isAllowOwnPiece) {
            calculatedToSquareList.add(toSquare);
          }
          return calculatedToSquareList;
        default:
          throw new IllegalArgumentException();
      }
    }
    return calculatedToSquareList;
  }

}
