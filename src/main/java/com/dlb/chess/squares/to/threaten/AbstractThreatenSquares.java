package com.dlb.chess.squares.to.threaten;

import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.squares.to.AbstractToSquares;

/**
 * Potentially to defined as all squares the piece can move to, being empty or occupied by an opponent piece, in the
 * case of rook, bishop or queen additionally additionally requiring that the square is "visible", that is the squares
 * between the from and to squares are all empty and in the case of pawns excluding the diagonal moves (for the reason,
 * that these moves requires additional conditions, contrary to the other pieces).
 *
 *
 */
public abstract class AbstractThreatenSquares extends AbstractToSquares {
  public static Set<Square> calculateThreatenedSquares(StaticPosition staticPosition, Side havingMove) {

    final Set<Square> squareSet = new TreeSet<>();

    for (final Square fromSquare : Square.BOARD_SQUARE_LIST) {
      if (staticPosition.isOwnPiece(fromSquare, havingMove)) {
        final Piece piece = staticPosition.get(fromSquare);
        switch (piece.getPieceType()) {
          case BISHOP:
            squareSet
                .addAll(BishopThreatenSquares.calculateBishopThreatenSquares(staticPosition, fromSquare, havingMove));
            break;
          case KING:
            squareSet.addAll(KingNonCastlingThreatenSquares.calculateKingNonCastlingThreatenSquares(staticPosition,
                fromSquare, havingMove));
            break;
          case KNIGHT:
            squareSet
                .addAll(KnightThreatenSquares.calculateKnightThreatenSquares(staticPosition, fromSquare, havingMove));
            break;
          case PAWN:
            squareSet.addAll(PawnThreatenSquares.calculatePawnThreatenSquares(staticPosition, fromSquare, havingMove));
            break;
          case QUEEN:
            squareSet
                .addAll(QueenThreatenSquares.calculateQueenThreatenSquares(staticPosition, fromSquare, havingMove));
            break;
          case ROOK:
            squareSet.addAll(RookThreatenSquares.calculateRookThreatenSquares(staticPosition, fromSquare, havingMove));
            break;
          case NONE:
          default:
            throw new IllegalArgumentException();
        }
      }
    }

    return squareSet;
  }

}
