package com.dlb.chess.test.analysis.output;

import java.util.ArrayList;
import java.util.List;

import com.dlb.chess.analysis.model.Analysis;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.enums.EnPassantCaptureRuleThreefold;
import com.dlb.chess.common.model.HalfMove;
import com.dlb.chess.common.utility.BasicUtility;
import com.dlb.chess.common.utility.HalfMoveUtility;
import com.dlb.chess.common.utility.RepetitionUtility;

public class RepetitionOutput {

  private static String calculateOutputRepetitionList(List<HalfMove> halfMoveList,
      EnPassantCaptureRuleThreefold enPassantCaptureRule) {
    final StringBuilder result = new StringBuilder();
    // the moves must be sorted ascending, as this is expected in the result
    // so the last entry has the repetition count of the sequence in the repetition count for the half-move
    final HalfMove lastHalfMove = NonNullWrapperCommon.getLast(halfMoveList);

    final var countRepetition = RepetitionUtility.getCountRepetition(lastHalfMove, enPassantCaptureRule);

    result.append("repPos=" + countRepetition + ": ");
    for (var i = 0; i < halfMoveList.size(); i++) {
      final HalfMove halfMove = NonNullWrapperCommon.get(halfMoveList, i);
      result.append(HalfMoveUtility.calculateMoveNumberAndSanWithoutSpace(halfMove));
      if (i <= halfMoveList.size() - 2) {
        result.append(" ");
      }
    }
    return NonNullWrapperCommon.toString(result);
  }

  public static String calculateOutputRepetitionAnalysis(Analysis analysis,
      EnPassantCaptureRuleThreefold enPassantCaptureRule) {

    final List<List<HalfMove>> repetitionList = RepetitionUtility.getRepetitionListListType(analysis, enPassantCaptureRule);
    return calculateOutputRepetitionListList(repetitionList, enPassantCaptureRule);
  }

  public static String calculateOutputRepetitionListList(List<List<HalfMove>> repetitionList,
      EnPassantCaptureRuleThreefold enPassantCaptureRule) {

    final List<String> resultList = new ArrayList<>();
    final var listLength = repetitionList.size();
    for (var i = 0; i < listLength; i++) {
      final List<HalfMove> repeatingPosition = NonNullWrapperCommon.get(repetitionList, i);

      resultList.add(calculateOutputRepetitionList(repeatingPosition, enPassantCaptureRule));
    }
    return BasicUtility.calculateSemicolonSeparatedList(resultList);
  }

}
