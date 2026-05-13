package com.dlb.chess.test.report.representation;

import java.util.ArrayList;
import java.util.List;

import com.dlb.chess.board.HalfMoveUtility;
import com.dlb.chess.common.Nulls;
import com.dlb.chess.common.utility.BasicUtility;
import com.dlb.chess.report.NoProgressHalfMove;

public class NoProgressRepresentation {

  public static String calculateRepresentationNoProgressMoveListList(List<List<NoProgressHalfMove>> listList) {
    final List<String> resultList = new ArrayList<>();
    for (final List<NoProgressHalfMove> list : listList) {
      resultList.add(calculateRepresentationNoProgressMoveList(list));
    }
    return Nulls.join("; ", resultList);
  }

  private static String calculateRepresentationNoProgressMoveList(List<NoProgressHalfMove> list) {
    if (list.isEmpty()) {
      return "";
    }
    final List<String> result = new ArrayList<>();
    for (var i = 0; i < list.size(); i++) {
      final NoProgressHalfMove listItem = Nulls.get(list, i);
      result.add(calculateRepresentationNoProgressMoveIncludingSequenceLength(listItem));
    }
    return BasicUtility.calculateSpaceSeparatedList(result);
  }

  private static String calculateRepresentationNoProgressMove(NoProgressHalfMove noProgressHalfMove) {
    return HalfMoveUtility.calculateFullMoveNumberInitialWithoutSpace(noProgressHalfMove.fullMoveNumber(),
        noProgressHalfMove.sideMoved()) + noProgressHalfMove.san();
  }

  private static String calculateRepresentationNoProgressMoveIncludingSequenceLength(
      NoProgressHalfMove noProgressHalfMove) {
    return calculateRepresentationNoProgressMove(noProgressHalfMove) + " (" + noProgressHalfMove.sequenceLength() + ")";
  }

}
