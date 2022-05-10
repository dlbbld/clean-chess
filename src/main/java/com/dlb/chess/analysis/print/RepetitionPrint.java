package com.dlb.chess.analysis.print;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.dlb.chess.analysis.print.model.RepetitionMove;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.model.HalfMove;
import com.dlb.chess.common.utility.BasicUtility;
import com.dlb.chess.common.utility.HalfMoveUtility;
import com.dlb.chess.internationalization.Message;

public class RepetitionPrint {

  public static List<String> calculateOutputRepetition(List<List<HalfMove>> repetitionList) {
    final List<String> resultList = new ArrayList<>();
    for (final List<HalfMove> list : repetitionList) {
      final var fold = list.size();
      final String foldStr = Message.getString("analysis.threefold.fold", fold);

      final String repetition = BasicUtility.calculateSpaceSeparatedList(calculateMoveNumberAndSanList(list));

      final var repetionDescription = foldStr + ": " + repetition;

      resultList.add(repetionDescription);
    }
    return resultList;
  }

  public static String calculateOutputRepetitionChronlogically(List<List<HalfMove>> repetitionList) {

    final List<RepetitionMove> modelList = calculateOutputRepetitionChronlogicallyModelList(repetitionList);

    final List<String> result = new ArrayList<>();
    for (final RepetitionMove repetitionMove : modelList) {
      result.add(calculatePrint(repetitionMove));
    }

    return BasicUtility.calculateSpaceSeparatedList(result);
  }

  private static String calculatePrint(RepetitionMove repetitionMove) {

    final StringBuilder result = new StringBuilder();

    result.append(HalfMoveUtility.calculateMoveNumberAndSanWithoutSpace(repetitionMove.halfMove()));

    result.append(" (");

    final var positionId = Message.getString("analysis.threefold.dynamicPosition.prefix")
        + repetitionMove.positionId();
    result.append(positionId);
    result.append(", ");
    result.append(repetitionMove.halfMove().countRepetition());
    result.append("/");
    result.append(repetitionMove.fold());
    result.append(")");

    return NonNullWrapperCommon.toString(result);
  }

  private static List<RepetitionMove> calculateOutputRepetitionChronlogicallyModelList(
      List<List<HalfMove>> repetitionList) {

    final List<RepetitionMove> resultList = new ArrayList<>();
    var positionId = 0;
    for (final List<HalfMove> list : repetitionList) {
      positionId++;
      final var fold = list.size();
      for (final HalfMove halfMove : list) {
        resultList.add(new RepetitionMove(positionId, fold, halfMove));
      }
    }

    Collections.sort(resultList);
    return resultList;
  }

  private static List<String> calculateMoveNumberAndSanList(List<HalfMove> halfMoveList) {
    final List<String> result = new ArrayList<>();
    for (final HalfMove halfMove : halfMoveList) {
      result.add(HalfMoveUtility.calculateMoveNumberAndSanWithoutSpace(halfMove));
    }
    return result;
  }

}
