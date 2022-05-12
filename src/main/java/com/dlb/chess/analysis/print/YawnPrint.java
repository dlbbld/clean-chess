package com.dlb.chess.analysis.print;

import java.util.ArrayList;
import java.util.List;

import com.dlb.chess.analysis.model.YawnHalfMove;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.utility.BasicUtility;
import com.dlb.chess.common.utility.HalfMoveUtility;

public class YawnPrint {

  public static List<String> calculateOutputYawnMoveListList(List<List<YawnHalfMove>> listList) {
    final List<String> resultList = new ArrayList<>();
    for (final List<YawnHalfMove> list : listList) {
      resultList.add(calculateOutputYawnMoveList(list));
    }
    return resultList;
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
    final var sequenceLengthInHalfMoves = yawnHalfMove.sequenceLength();
    String sequenceLengthInFullMovesStr;
    if (sequenceLengthInHalfMoves % 2 == 0) {
      final var sequenceLengthInFullMoves = sequenceLengthInHalfMoves / 2;
      sequenceLengthInFullMovesStr = String.valueOf(sequenceLengthInFullMoves);
    } else {
      final var sequenceLengthInFullMoves = sequenceLengthInHalfMoves / 2.0;
      sequenceLengthInFullMovesStr = String.valueOf(sequenceLengthInFullMoves);

    }
    return calculateOutputYawnMove(yawnHalfMove) + " (" + sequenceLengthInFullMovesStr + ")";
  }

}
