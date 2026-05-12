package com.dlb.chess.common.utility;

import java.util.ArrayList;
import java.util.Set;

import com.dlb.chess.common.Nulls;

public abstract class SetUtility {

  public static <E> E getOnly(Set<E> set) {
    if (set.size() != 1) {
      throw new IllegalArgumentException("Expected exactly one element but found " + set.size());
    }
    return Nulls.getFirst(new ArrayList<>(set));
  }

}
