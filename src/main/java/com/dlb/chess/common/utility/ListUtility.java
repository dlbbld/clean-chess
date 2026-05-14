package com.dlb.chess.common.utility;

import java.util.List;

import com.dlb.chess.common.Nulls;

public abstract class ListUtility {

  public static <E> E getOnly(List<E> list) {
    if (list.size() != 1) {
      throw new IllegalArgumentException("Expected exactly one element but found " + list.size());
    }
    return Nulls.getFirst(list);
  }

}
