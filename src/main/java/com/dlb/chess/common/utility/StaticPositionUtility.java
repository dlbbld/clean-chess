package com.dlb.chess.common.utility;

import java.util.Set;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.squares.to.threaten.AbstractThreatenSquares;

public class StaticPositionUtility implements EnumConstants {

  public static boolean calculateIsCheck(StaticPosition staticPosition, Side havingMove) {
    final Set<Square> threatenedSquares = AbstractThreatenSquares.calculateThreatenedSquares(staticPosition,
        havingMove.getOppositeSide());
    return calculateIsKingAttacked(staticPosition, havingMove, threatenedSquares);
  }

  private static boolean calculateIsKingAttacked(StaticPosition staticPosition, Side havingMove,
      Set<Square> threatenedSquares) {
    final Square kingSquareHavingMove = StaticPositionUtility.calculateKingSquare(staticPosition, havingMove);
    return threatenedSquares.contains(kingSquareHavingMove);
  }

  public static Square calculateKingSquare(StaticPosition staticPosition, Side side) {
    for (final Square square : Square.BOARD_SQUARE_LIST) {
      if (!staticPosition.isEmpty(square)) {
        final Piece piece = staticPosition.get(square);
        if (piece.getPieceType() == KING && piece.getSide() == side) {
          return square;
        }
      }
    }
    throw new ProgrammingMistakeException(
        "There must be a king on the board each, the fun support for no kings is yet to be implemented ....");
  }

  // here we allow king off the board
  public static Square checkKingSquare(StaticPosition staticPosition, Side side) {
    for (final Square square : Square.BOARD_SQUARE_LIST) {
      if (staticPosition.isOwnKing(square, side)) {
        return square;
      }
    }
    return Square.NONE;
  }

  public static String calculatePiecePlacement(StaticPosition staticPosition) {
    final StringBuilder piecePlacement = new StringBuilder();
    for (var rankNumber = 8; rankNumber >= 1; rankNumber--) {
      var consecutiveEmptySquares = 0;
      for (var fileNumber = 1; fileNumber <= 8; fileNumber++) {
        final Square square = Square.calculate(fileNumber, rankNumber);
        final Piece pieceOnSquare = staticPosition.get(square);
        final var isEmptySquare = pieceOnSquare == Piece.NONE;
        if (isEmptySquare) {
          consecutiveEmptySquares++;
          if (fileNumber == 8) {
            // last square in the rank
            piecePlacement.append(consecutiveEmptySquares);
          }
        } else {
          if (consecutiveEmptySquares > 0) {
            piecePlacement.append(consecutiveEmptySquares);
            consecutiveEmptySquares = 0;
          }
          piecePlacement.append(pieceOnSquare.getLetter());
        }
      }
      if (rankNumber != 1) {
        piecePlacement.append("/");
      }
    }
    return NonNullWrapperCommon.toString(piecePlacement);
  }

  public static boolean calculateIsEvaluateAttackingKing(StaticPosition staticPosition,
      MoveSpecification moveSpecification) {
    final StaticPosition staticPositionEvaluateAfterMove = Board.createPositionAfterMove(staticPosition,
        moveSpecification);
    return calculateIsEvaluateAttackingKing(staticPositionEvaluateAfterMove,
        moveSpecification.havingMove().getOppositeSide());
  }

  public static boolean calculateIsEvaluateAttackingKing(StaticPosition staticPositionEvaluateAfterMove,
      Side havingMove) {
    final Set<Square> threatenedSquares = AbstractThreatenSquares
        .calculateThreatenedSquares(staticPositionEvaluateAfterMove, havingMove);
    final Square kingSquareNotHavingMove = calculateKingSquare(staticPositionEvaluateAfterMove,
        havingMove.getOppositeSide());
    return threatenedSquares.contains(kingSquareNotHavingMove);
  }
}
