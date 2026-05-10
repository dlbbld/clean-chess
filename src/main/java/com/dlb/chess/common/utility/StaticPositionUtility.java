package com.dlb.chess.common.utility;

import java.util.List;
import java.util.Set;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.board.model.UpdateSquare;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.moves.utility.CastlingUtility;
import com.dlb.chess.moves.utility.EnPassantCaptureUtility;
import com.dlb.chess.moves.utility.PromotionUtility;
import com.dlb.chess.moves.utility.StandardMoveUtility;
import com.dlb.chess.squares.to.attacked.AbstractAttackedSquares;

public abstract class StaticPositionUtility implements EnumConstants {

  public static boolean calculateIsCheck(StaticPosition staticPosition, Side havingMove) {
    final Set<Square> attackedSquares = AbstractAttackedSquares.calculateAttackedSquares(staticPosition,
        havingMove.getOppositeSide());
    final Square kingSquareHavingMove = StaticPositionUtility.calculateKingSquare(staticPosition, havingMove);
    return attackedSquares.contains(kingSquareHavingMove);
  }

  public static Square calculateKingSquare(StaticPosition staticPosition, Side side) {
    for (final Square square : Square.REAL) {
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

  public static boolean calculateIsKingAttackedAfterMove(StaticPosition staticPosition, Side havingMove,
      MoveSpecification moveSpecification) {
    final StaticPosition staticPositionEvaluateAfterMove = createPositionAfterMove(staticPosition, havingMove,
        moveSpecification);
    return calculateIsCheck(staticPositionEvaluateAfterMove, havingMove);
  }

  public static StaticPosition createPositionAfterMove(StaticPosition staticPosition, Side havingMove,
      MoveSpecification moveSpecification) {

    final List<UpdateSquare> updateSquareList = calculateUpdateSquareList(staticPosition, havingMove,
        moveSpecification);
    return staticPosition.createChangedPosition(updateSquareList);
  }

  private static List<UpdateSquare> calculateUpdateSquareList(StaticPosition staticPosition, Side havingMove,
      MoveSpecification moveSpecification) {

    if (EnPassantCaptureUtility.calculateIsEnPassantCaptureNewMove(staticPosition, moveSpecification)) {
      return EnPassantCaptureUtility.performEnPassantCaptureMovements(staticPosition, havingMove, moveSpecification);
    }
    if (CastlingUtility.calculateIsCastlingMove(moveSpecification)) {
      return CastlingUtility.performCastlingMovements(havingMove, moveSpecification);
    }
    if (PromotionUtility.calculateIsPromotionNewMove(moveSpecification)) {
      return PromotionUtility.performPromotionMovements(havingMove, moveSpecification);
    }
    return StandardMoveUtility.performStandardMovements(staticPosition, moveSpecification);
  }

}
