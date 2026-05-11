package com.dlb.chess.test.report.representation;

import java.util.ArrayList;
import java.util.List;

import com.dlb.chess.board.HalfMoveUtility;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.enums.EnPassantCaptureRuleThreefold;
import com.dlb.chess.common.model.HalfMove;
import com.dlb.chess.common.utility.RepetitionUtility;
import com.dlb.chess.report.Report;

public class RepetitionRepresentation {

  private static String calculateRepresentationRepetitionList(List<HalfMove> halfMoveList,
      EnPassantCaptureRuleThreefold enPassantCaptureRule) {
    final StringBuilder result = new StringBuilder();
    // the moves must be sorted ascending, as this is expected in the result
    // so the last entry has the repetition count of the sequence in the repetition count for the half-move
    final HalfMove lastHalfMove = NonNullWrapperCommon.getLast(halfMoveList);

    final var countRepetition = RepetitionUtility.getCountRepetition(lastHalfMove, enPassantCaptureRule);

    result.append("repPos=").append(countRepetition).append(": ");
    for (var i = 0; i < halfMoveList.size(); i++) {
      final HalfMove halfMove = NonNullWrapperCommon.get(halfMoveList, i);
      result.append(HalfMoveUtility.calculateMoveNumberAndSanWithoutSpace(halfMove));
      if (i <= halfMoveList.size() - 2) {
        result.append(" ");
      }
    }
    return NonNullWrapperCommon.toString(result);
  }

  public static String calculateRepresentationRepetitionReport(Report report,
      EnPassantCaptureRuleThreefold enPassantCaptureRule) {

    final var repetitionList = switch (enPassantCaptureRule) {
      case DO_IGNORE -> report.repetitionListListInitialEnPassantCapture();
      case DO_NOT_IGNORE -> report.repetitionListList();
      default -> throw new IllegalArgumentException();
    };
    return calculateRepresentationRepetitionListList(repetitionList, enPassantCaptureRule);
  }

  public static String calculateRepresentationRepetitionListList(List<List<HalfMove>> repetitionList,
      EnPassantCaptureRuleThreefold enPassantCaptureRule) {

    final List<String> resultList = new ArrayList<>();
    final var listLength = repetitionList.size();
    for (var i = 0; i < listLength; i++) {
      final List<HalfMove> repeatingPosition = NonNullWrapperCommon.get(repetitionList, i);

      resultList.add(calculateRepresentationRepetitionList(repeatingPosition, enPassantCaptureRule));
    }
    return NonNullWrapperCommon.join("; ", resultList);
  }

}
