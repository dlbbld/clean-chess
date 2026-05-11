package com.dlb.chess.report;

import java.util.ArrayList;
import java.util.List;

import com.dlb.chess.board.HalfMoveUtility;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.utility.BasicUtility;

class NoProgressPrint {

  public static List<String> calculateOutputNoProgressMoveListList(List<List<NoProgressHalfMove>> listList) {
    final List<String> resultList = new ArrayList<>();
    for (final List<NoProgressHalfMove> list : listList) {
      resultList.add(calculateOutputNoProgressMoveList(list));
    }
    return resultList;
  }

  private static String calculateOutputNoProgressMoveList(List<NoProgressHalfMove> list) {
    if (list.isEmpty()) {
      return "";
    }
    final List<String> result = new ArrayList<>();
    for (var i = 0; i < list.size(); i++) {
      final NoProgressHalfMove listItem = NonNullWrapperCommon.get(list, i);
      result.add(calculateOutputNoProgressMoveIncludingSequenceLength(listItem));
    }
    return BasicUtility.calculateSpaceSeparatedList(result);
  }

  private static String calculateOutputNoProgressMove(NoProgressHalfMove noProgressHalfMove) {
    return HalfMoveUtility.calculateFullMoveNumberInitialWithoutSpace(noProgressHalfMove.fullMoveNumber(),
        noProgressHalfMove.sideMoved()) + noProgressHalfMove.san();
  }

  private static String calculateOutputNoProgressMoveIncludingSequenceLength(NoProgressHalfMove noProgressHalfMove) {
    final var sequenceLengthInHalfMoves = noProgressHalfMove.sequenceLength();
    String sequenceLengthInFullMovesStr;
    if (sequenceLengthInHalfMoves % 2 == 0) {
      final var sequenceLengthInFullMoves = sequenceLengthInHalfMoves / 2;
      sequenceLengthInFullMovesStr = String.valueOf(sequenceLengthInFullMoves);
    } else {
      final var sequenceLengthInFullMoves = sequenceLengthInHalfMoves / 2.0;
      sequenceLengthInFullMovesStr = String.valueOf(sequenceLengthInFullMoves);

    }
    return calculateOutputNoProgressMove(noProgressHalfMove) + " (" + sequenceLengthInFullMovesStr + ")";
  }

}
