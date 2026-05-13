package com.dlb.chess.common;

import java.util.Comparator;
import java.util.List;

import com.dlb.chess.common.model.HalfMove;

public class HalfMoveListListComparator implements Comparator<List<HalfMove>> {

  public static final HalfMoveListListComparator COMPARATOR = new HalfMoveListListComparator();

  @Override
  public int compare(List<HalfMove> firstList, List<HalfMove> secondList) {

    final HalfMove firstHalfMoveFirstList = Nulls.getFirst(firstList);
    final HalfMove firstHalfMoveSecondList = Nulls.getFirst(secondList);

    return Integer.compare(firstHalfMoveFirstList.halfMoveCount(), firstHalfMoveSecondList.halfMoveCount());
  }
}
