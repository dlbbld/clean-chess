package com.dlb.chess.test.common.utility;

import java.util.ArrayList;
import java.util.List;

import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.enums.MoveSuffixAnnotation;
import com.dlb.chess.model.PgnHalfMove;

public abstract class AbstractTestMovetextUtility implements EnumConstants {

  public static List<String> calculateSanList(List<PgnHalfMove> halfMoveList) {
    final List<String> sanList = new ArrayList<>();
    for (final PgnHalfMove halfMove : halfMoveList) {
      sanList.add(halfMove.san());
    }
    return sanList;
  }

  public static List<MoveSuffixAnnotation> calculateMoveSuffixAnnotationList(List<PgnHalfMove> halfMoveList) {
    final List<MoveSuffixAnnotation> moveSuffixAnnotationList = new ArrayList<>();
    for (final PgnHalfMove halfMove : halfMoveList) {
      moveSuffixAnnotationList.add(halfMove.moveSuffixAnnotation());
    }
    return moveSuffixAnnotationList;
  }

  public static List<String> calculateCommentaryList(List<PgnHalfMove> halfMoveList) {
    final List<String> commentaryList = new ArrayList<>();
    for (final PgnHalfMove halfMove : halfMoveList) {
      commentaryList.add(halfMove.commentary());
    }
    return commentaryList;
  }
}
