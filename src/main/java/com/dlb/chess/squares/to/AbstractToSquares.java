package com.dlb.chess.squares.to;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.enums.SquareOccupation;

public abstract class AbstractToSquares implements EnumConstants {

  protected static void checkPiece(StaticPosition staticPosition, Side havingMove, Square sourceSquare,
      PieceType expectedPieceType) throws IllegalArgumentException {
    if (!staticPosition.isOwnPiece(sourceSquare, havingMove, expectedPieceType)) {
      throw new IllegalArgumentException(
          "The source square must be occupied by a " + havingMove + " " + expectedPieceType);
    }
  }

  public static SquareOccupation calculateSquareOccupation(StaticPosition staticPosition, Side havingMove,
      Square square) {
    final Piece piece = staticPosition.get(square);
    if (piece == Piece.NONE) {
      return SquareOccupation.NONE;
    }
    switch (piece.getSide()) {
      case BLACK:
        switch (havingMove) {
          case BLACK:
            return SquareOccupation.OWN_PIECE;
          case WHITE:
            return SquareOccupation.OPPONENT_PIECE;
          case NONE:
          default:
            throw new IllegalArgumentException();
        }
      case WHITE:
        switch (havingMove) {
          case BLACK:
            return SquareOccupation.OPPONENT_PIECE;
          case WHITE:
            return SquareOccupation.OWN_PIECE;
          case NONE:
          default:
            throw new IllegalArgumentException();
        }
      case NONE:
        // we filtered this case before
        throw new ProgrammingMistakeException();
      default:
        throw new IllegalArgumentException();
    }
  }
}
