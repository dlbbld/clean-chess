package com.dlb.chess.test.analysis.output;

import java.util.ArrayList;
import java.util.List;

import com.dlb.chess.analysis.model.YawnHalfMove;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.utility.BasicUtility;
import com.dlb.chess.common.utility.HalfMoveUtility;

public class YawnOutput {

  public static String calculateOutputYawnMoveListList(List<List<YawnHalfMove>> listList) {
    final List<String> resultList = new ArrayList<>();
    for (final List<YawnHalfMove> list : listList) {
      resultList.add(calculateOutputYawnMoveList(list));
    }
    return BasicUtility.calculateSemicolonSeparatedList(resultList);
  }

  private static String calculateOutputYawnMoveList(List<YawnHalfMove> list) {
    if (list.isEmpty()) {
      return "";
    }
    final List<String> result = new ArrayList<>();
    for (var i = 0; i < list.size(); i++) {
      final YawnHalfMove listItem = NonNullWrapperCommon.get(list, i);
      result.add(calculateOutputYawnMoveIncludingSequenceLength(listItem));
    }
    return BasicUtility.calculateSpaceSeparatedList(result);
  }

  private static String calculateOutputYawnMove(YawnHalfMove yawnHalfMove) {
    return HalfMoveUtility.calculateFullMoveNumberInitialWithoutSpace(yawnHalfMove.fullMoveNumber(),
        yawnHalfMove.sideMoved()) + yawnHalfMove.san();
  }

  private static String calculateOutputYawnMoveIncludingSequenceLength(YawnHalfMove yawnHalfMove) {
    return calculateOutputYawnMove(yawnHalfMove) + " (" + yawnHalfMove.sequenceLength() + ")";
  }

}
