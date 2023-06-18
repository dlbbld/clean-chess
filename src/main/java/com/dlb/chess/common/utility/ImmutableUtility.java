package com.dlb.chess.common.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.EnumConstants;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public abstract class ImmutableUtility implements EnumConstants {

  public static final ImmutableSet<Square> EMPTY_UNMODIFIABLE_SET;

  static {
    final List<Square> list = new ArrayList<>();
    final EnumSet<Square> enumSet = NonNullWrapperCommon.newEnumSet(list, Square.class);
    EMPTY_UNMODIFIABLE_SET = NonNullWrapperCommon.copyOfSet(enumSet);
  }

  private static final ImmutableList<Square> EMPTY_UNMODIFIABLE_LIST_SQUARE;

  static {
    final List<Square> list = new ArrayList<>();
    EMPTY_UNMODIFIABLE_LIST_SQUARE = NonNullWrapperCommon.copyOfList(list);
  }

  public static ImmutableSet<Square> constructSet(Square... squareArray) {
    if (squareArray.length == 0) {
      return EMPTY_UNMODIFIABLE_SET;
    }
    // the array is not constructed as null
    @SuppressWarnings("null") final List<Square> list = Arrays.asList(squareArray);
    // the enum set is not constructed as null
    @SuppressWarnings("null") final EnumSet<Square> enumSet = NonNullWrapperCommon.newEnumSet(list, Square.class);
    return NonNullWrapperCommon.copyOfSet(enumSet);
  }

  public static ImmutableList<Square> constructListSquare(Square... squareArray) {
    if (squareArray.length == 0) {
      return EMPTY_UNMODIFIABLE_LIST_SQUARE;
    }
    // the array is not constructed as null
    @SuppressWarnings("null") final List<Square> list = NonNullWrapperCommon.asList(squareArray);
    return NonNullWrapperCommon.copyOfList(list);
  }

}
