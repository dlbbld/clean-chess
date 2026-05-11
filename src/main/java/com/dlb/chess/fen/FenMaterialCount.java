package com.dlb.chess.fen;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.board.enums.SquareType;

/**
 * Internal piece-count helpers used by FEN advanced validation. Not part of
 * the public API: the library exposes parsing and outcome reporting, not
 * material arithmetic.
 */
abstract class FenMaterialCount {

  static int calculateNumberOfPieces(Side side, StaticPosition staticPosition, PieceType pieceType) {
    final Piece piece = Piece.calculate(side, pieceType);
    var total = 0;
    for (final Square boardSquare : Square.REAL) {
      if (staticPosition.get(boardSquare) == piece) {
        total++;
      }
    }
    return total;
  }

  static int calculateNumberOfBishops(Side side, StaticPosition staticPosition, SquareType squareType) {
    final Piece bishop = Piece.calculate(side, PieceType.BISHOP);
    var total = 0;
    for (final Square boardSquare : Square.REAL) {
      if (staticPosition.get(boardSquare) == bishop && boardSquare.getSquareType() == squareType) {
        total++;
      }
    }
    return total;
  }
}
