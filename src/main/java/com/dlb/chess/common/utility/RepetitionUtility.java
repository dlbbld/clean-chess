package com.dlb.chess.common.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.analysis.model.Analysis;
import com.dlb.chess.common.HalfMoveListListComparator;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.ChessConstants;
import com.dlb.chess.common.enums.EnPassantCaptureRuleThreefold;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.model.DynamicPosition;
import com.dlb.chess.common.model.HalfMove;
import com.dlb.chess.model.LegalMove;

public class RepetitionUtility {

  /**
   * We do not override equals because we need conditional equals method which we realize by always comparing explicitly
   * in the code when used and not using counts on set etc., which would use a non conditional comparions. This is
   * needed to count the repetition in two different ways (one as it should be, not ignoring the en passant condition,
   * one as it shouldn't be, ignoring the en passant condition, for finally finding such games where ignoring the
   * condition would have made a difference.
   */
  public static boolean equals(DynamicPosition dynamicPosition, @Nullable Object obj,
      EnPassantCaptureRuleThreefold enPassantCaptureRule) {
    switch (enPassantCaptureRule) {
      case DO_IGNORE:
        return equalsIgnoringEnPassantCapture(dynamicPosition, obj);
      case DO_NOT_IGNORE:
        return equalsNotIgnoringEnPassantCapture(dynamicPosition, obj);
      default:
        throw new IllegalArgumentException();
    }
  }

  private static boolean equalsIgnoringEnPassantCapture(DynamicPosition dynamicPosition, @Nullable Object obj) {
    if (dynamicPosition == obj) {
      return true;
    }
    if (obj == null || dynamicPosition.getClass() != obj.getClass()) {
      return false;
    }
    final var other = (DynamicPosition) obj;
    return dynamicPosition.castlingRightBoth() == other.castlingRightBoth()
        // && dynamicPosition.isEnPassantCapturePossible() == other.isEnPassantCapturePossible()
        && dynamicPosition.havingMove() == other.havingMove()
        && dynamicPosition.staticPosition().equals(other.staticPosition());
  }

  private static boolean equalsNotIgnoringEnPassantCapture(DynamicPosition dynamicPosition, @Nullable Object obj) {
    if (dynamicPosition == obj) {
      return true;
    }
    if (obj == null || dynamicPosition.getClass() != obj.getClass()) {
      return false;
    }
    final var other = (DynamicPosition) obj;
    return dynamicPosition.castlingRightBoth() == other.castlingRightBoth()
        && dynamicPosition.isEnPassantCapturePossible() == other.isEnPassantCapturePossible()
        && dynamicPosition.havingMove() == other.havingMove()
        && dynamicPosition.staticPosition().equals(other.staticPosition());
  }

  public static int getCountRepetition(HalfMove halfMove, EnPassantCaptureRuleThreefold enPassantCaptureRule) {
    switch (enPassantCaptureRule) {
      case DO_NOT_IGNORE:
        return halfMove.countRepetition();
      case DO_IGNORE:
        return halfMove.countRepetitionIgnoringEnPassantCapture();
      default:
        throw new IllegalArgumentException();
    }
  }

  public static int calculateCountRepetition(List<LegalMove> performedLegalMoveList,
      List<DynamicPosition> dynamicPositionList, DynamicPosition dynamicPosition,
      EnPassantCaptureRuleThreefold enPassantCaptureRule) {

    if (performedLegalMoveList.isEmpty()) {
      throw new ProgrammingMistakeException("Not to be called for no moves played");
    }
    // double-check because we are iterating over both lists
    if (performedLegalMoveList.size() != dynamicPositionList.size() - 1) {
      throw new ProgrammingMistakeException("Something went wrong with the list size");
    }

    var countRepetition = 1;

    // we use the same index for moves and position on purpose
    for (var i = performedLegalMoveList.size() - 1; i >= 0; i--) {
      final LegalMove lastLegalMove = NonNullWrapperCommon.get(performedLegalMoveList, i);
      if (BasicChessUtility.calculateIsResetHalfMoveClock(lastLegalMove)) {
        // if pawn move or capture the positions before cannot equal the current position
        // this is a property of the chess game with a basic mathematical proof
        // this is used often and increases performance
        return countRepetition;
      }
      final DynamicPosition previousDynamicPosition = NonNullWrapperCommon.get(dynamicPositionList, i);
      final var isEqual = equals(dynamicPosition, previousDynamicPosition, enPassantCaptureRule);
      if (isEqual) {
        countRepetition++;
      }
    }
    if (countRepetition < 1) {
      throw new ProgrammingMistakeException("The conditional repetition count cannot be below one");
    }
    return countRepetition;
  }

  public static List<List<HalfMove>> calculateRepetitionListList(List<HalfMove> halfMoveList,
      EnPassantCaptureRuleThreefold enPassantCaptureRule) {

    final List<List<HalfMove>> list = new ArrayList<>();
    final List<DynamicPosition> processed = new ArrayList<>();
    for (final HalfMove searchHalfMoveThreeFold : halfMoveList) {
      // we iterate over the move list
      final DynamicPosition searchDynamicPositionThreeFold = searchHalfMoveThreeFold.dynamicPosition();
      if (calculateIsContained(processed, searchDynamicPositionThreeFold, enPassantCaptureRule)) {
        continue;
      }
      final var countRepetition = getCountRepetition(searchHalfMoveThreeFold, enPassantCaptureRule);

      if (countRepetition == ChessConstants.THREEFOLD_REPETITION_RULE_THRESHOLD) {
        // if we found a half move which is equal or above the required count, we sample all previous half-moves with
        // the
        // same dynamic position
        final List<HalfMove> halfMoveSameDynamicPositionList = new ArrayList<>();
        for (final HalfMove searchHalfMoveSameDynamicPosition : halfMoveList) {
          final var isEqual = equals(searchDynamicPositionThreeFold,
              searchHalfMoveSameDynamicPosition.dynamicPosition(), enPassantCaptureRule);
          if (isEqual) {
            halfMoveSameDynamicPositionList.add(searchHalfMoveSameDynamicPosition);
          }
        }

        list.add(halfMoveSameDynamicPositionList);
        processed.add(searchDynamicPositionThreeFold);
      }
    }
    Collections.sort(list, HalfMoveListListComparator.COMPARATOR);
    return list;
  }

  private static boolean calculateIsContained(List<DynamicPosition> processedDynamicPositionList,
      DynamicPosition position, EnPassantCaptureRuleThreefold enPassantCaptureRule) {
    for (final DynamicPosition processedDynamicPosition : processedDynamicPositionList) {
      final var isEqual = equals(processedDynamicPosition, position, enPassantCaptureRule);
      if (isEqual) {
        return true;
      }
    }
    return false;
  }

  public static List<List<HalfMove>> getRepetitionListListType(Analysis analysis,
      EnPassantCaptureRuleThreefold enPassantCaptureRule) {
    switch (enPassantCaptureRule) {
      case DO_IGNORE:
        return analysis.repetitionListListInitialEnPassantCapture();
      case DO_NOT_IGNORE:
        return analysis.repetitionListList();
      default:
        throw new IllegalArgumentException();
    }
  }

}
