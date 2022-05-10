package com.dlb.chess.illegal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.illegal.model.RestorePositionAdvice;
import com.dlb.chess.illegal.model.RestorePositionAdviceEmptySquarePutPieceFromOtherSquare;
import com.dlb.chess.illegal.model.RestorePositionAdviceEmptySquarePutSparePiece;
import com.dlb.chess.illegal.model.RestorePositionAdviceNonEmptySquareMoveToOtherSquare;
import com.dlb.chess.illegal.model.RestorePositionAdviceNonEmptySquareRemoveFromBoard;
import com.dlb.chess.illegal.model.RestorePositionAdviceNonEmptySquareSwapPiece;
import com.dlb.chess.illegal.model.RestorePositionAdviceNone;

public class PositionRestoreAdvisor {

  /**
   * We calculate the first square having a difference in the given order A1, B1, ..., H1, A2, B2, ...
   */
  public static RestorePositionAdvice calculatePositionAdvice(StaticPosition correctPosition,
      StaticPosition incorrectPosition) {

    for (final Square checkSquare : Square.BOARD_SQUARE_LIST) {
      if (correctPosition.get(checkSquare) != incorrectPosition.get(checkSquare)) {
        if (incorrectPosition.isEmpty(checkSquare)) {
          // we must put something on the square
          final List<Square> orderedFromSquareList = calculateOrderedFromSquareList(checkSquare, correctPosition,
              incorrectPosition);
          final Piece pieceRequiredOnSquare = correctPosition.get(checkSquare);
          if (!orderedFromSquareList.isEmpty()) {
            return new RestorePositionAdviceEmptySquarePutPieceFromOtherSquare(checkSquare, pieceRequiredOnSquare,
                orderedFromSquareList);
          }
          return new RestorePositionAdviceEmptySquarePutSparePiece(checkSquare, pieceRequiredOnSquare);
        }
        if (correctPosition.isEmpty(checkSquare)) {
          // we must clear the square
          final Piece pieceOnSquare = incorrectPosition.get(checkSquare);
          final List<Square> orderedToSquareList = calculateMoveOrderedToSquareList(checkSquare, correctPosition,
              incorrectPosition);
          if (!orderedToSquareList.isEmpty()) {
            return new RestorePositionAdviceNonEmptySquareMoveToOtherSquare(checkSquare, pieceOnSquare,
                orderedToSquareList);
          }
          return new RestorePositionAdviceNonEmptySquareRemoveFromBoard(checkSquare, pieceOnSquare);
        }
        // different pieces
        // we must do something creative
        // let's say we have a white knight but need a black queen
        // first we move the white knight off the board or to another square
        // then we put the black queen as a spare piece or from another square
        // we perform the first operation, and then let the system recall this method
        // by the logic it will produced the second message, as now the square is empty but needs a black queen!

        // first we check swap
        final List<Square> swapSquareList = calculateSwapOrderedSwapSquareList(checkSquare, correctPosition,
            incorrectPosition);
        if (!swapSquareList.isEmpty()) {
          final Piece pieceSourceSquare = incorrectPosition.get(checkSquare);
          final Square firstSwapSquare = NonNullWrapperCommon.getFirst(swapSquareList);
          final Piece pieceDestinationSquare = incorrectPosition.get(firstSwapSquare);
          return new RestorePositionAdviceNonEmptySquareSwapPiece(checkSquare, pieceSourceSquare,
              pieceDestinationSquare, swapSquareList);

        }

        // then we check move to
        final List<Square> toSquareList = calculateMoveOrderedToSquareList(checkSquare, correctPosition,
            incorrectPosition);
        final Piece pieceOnSquare = incorrectPosition.get(checkSquare);
        if (!toSquareList.isEmpty()) {
          return new RestorePositionAdviceNonEmptySquareMoveToOtherSquare(checkSquare, pieceOnSquare, toSquareList);
        }
        return new RestorePositionAdviceNonEmptySquareRemoveFromBoard(checkSquare, pieceOnSquare);
      }
    }
    return new RestorePositionAdviceNone(Square.NONE);
  }

  private static List<Square> calculateOrderedFromSquareList(Square checkSquare, StaticPosition correctPosition,
      StaticPosition incorrectPosition) {

    final List<Square> orderedFromSquareList = new ArrayList<>();
    for (final Square fromSquareCandidate : Square.BOARD_SQUARE_LIST) {
      if (calculateIsFromSquareCandidate(checkSquare, fromSquareCandidate, correctPosition, incorrectPosition)) {
        orderedFromSquareList.add(fromSquareCandidate);
      }
    }
    Collections.sort(orderedFromSquareList, new SquareDistance(checkSquare));
    return orderedFromSquareList;
  }

  private static boolean calculateIsFromSquareCandidate(Square checkSquare, Square fromSquareCandidate,
      StaticPosition correctPosition, StaticPosition incorrectPosition) {

    final Piece pieceRequiredOnSquare = correctPosition.get(checkSquare);

    // for example
    // the square a1 is empty in the incorrect position but has a white rook in the correct position
    // we have white rooks on a5 and b6 for incorrect position and empty such square for correct position
    // we can move any of these white rooks for correction
    return incorrectPosition.get(fromSquareCandidate) == pieceRequiredOnSquare
        && correctPosition.isEmpty(fromSquareCandidate);
  }

  private static List<Square> calculateSwapOrderedSwapSquareList(Square checkSquare, StaticPosition correctPosition,
      StaticPosition incorrectPosition) {

    final List<Square> orderedSwapSquareList = new ArrayList<>();
    for (final Square swapSquareCandidate : Square.BOARD_SQUARE_LIST) {
      if (calculateIsSwapCandidate(checkSquare, swapSquareCandidate, correctPosition, incorrectPosition)) {
        orderedSwapSquareList.add(swapSquareCandidate);
      }
    }

    Collections.sort(orderedSwapSquareList, new SquareDistance(checkSquare));
    return orderedSwapSquareList;
  }

  private static boolean calculateIsSwapCandidate(Square checkSquare, Square swapSquareCandidate,
      StaticPosition correctPosition, StaticPosition incorrectPosition) {

    final Piece pieceIncorrectPositionMoveAway = incorrectPosition.get(checkSquare);
    final Piece pieceCorrectPositionDestination = correctPosition.get(swapSquareCandidate);

    if (pieceIncorrectPositionMoveAway != pieceCorrectPositionDestination) {
      return false;
    }

    final Piece pieceIncorrectPositionDestination = incorrectPosition.get(swapSquareCandidate);
    final Piece pieceCorrectPositionRequired = correctPosition.get(checkSquare);

    return pieceIncorrectPositionDestination == pieceCorrectPositionRequired;

  }

  private static List<Square> calculateMoveOrderedToSquareList(Square checkSquare, StaticPosition correctPosition,
      StaticPosition incorrectPosition) {

    final List<Square> orderedToSquareList = new ArrayList<>();
    for (final Square toSquareCandidate : Square.BOARD_SQUARE_LIST) {
      if (calculateIsToSquareCandidate(checkSquare, toSquareCandidate, correctPosition, incorrectPosition)) {
        orderedToSquareList.add(toSquareCandidate);
      }
    }

    Collections.sort(orderedToSquareList, new SquareDistance(checkSquare));
    return orderedToSquareList;
  }

  private static boolean calculateIsToSquareCandidate(Square checkSquare, Square toSquareCandidate,
      StaticPosition correctPosition, StaticPosition incorrectPosition) {

    final Piece pieceWhichMustBeMovedAwayFromSquare = incorrectPosition.get(checkSquare);

    // for example
    // the square c5 has a black knight in the incorrect position but is empty in the correct position
    // we have black knights on d7 and c2 in the correct position and empty such square for incorrect position
    // we can move the black knight to any of these square for correction
    return correctPosition.get(toSquareCandidate) == pieceWhichMustBeMovedAwayFromSquare
        && incorrectPosition.isEmpty(toSquareCandidate);
  }
}
