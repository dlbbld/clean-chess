package com.dlb.chess.test.report.representation;

import java.util.ArrayList;
import java.util.List;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.utility.BasicUtility;
import com.dlb.chess.common.utility.HalfMoveUtility;
import com.dlb.chess.report.model.YawnHalfMove;

public class YawnRepresentation {

  public static String calculateRepresentationYawnMoveListList(List<List<YawnHalfMove>> listList) {
    final List<String> resultList = new ArrayList<>();
    for (final List<YawnHalfMove> list : listList) {
      resultList.add(calculateRepresentationYawnMoveList(list));
    }
    return BasicUtility.calculateSemicolonSeparatedList(resultList);
  }

  private static String calculateRepresentationYawnMoveList(List<YawnHalfMove> list) {
    if (list.isEmpty()) {
      return "";
    }
    final List<String> result = new ArrayList<>();
    for (var i = 0; i < list.size(); i++) {
      final YawnHalfMove listItem = NonNullWrapperCommon.get(list, i);
      result.add(calculateRepresentationYawnMoveIncludingSequenceLength(listItem));
    }
    return BasicUtility.calculateSpaceSeparatedList(result);
  }

  private static String calculateRepresentationYawnMove(YawnHalfMove yawnHalfMove) {
    return HalfMoveUtility.calculateFullMoveNumberInitialWithoutSpace(yawnHalfMove.fullMoveNumber(),
        yawnHalfMove.sideMoved()) + yawnHalfMove.san();
  }

  private static String calculateRepresentationYawnMoveIncludingSequenceLength(YawnHalfMove yawnHalfMove) {
    return calculateRepresentationYawnMove(yawnHalfMove) + " (" + yawnHalfMove.sequenceLength() + ")";
  }

}
