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

public class ImmutableUtility implements EnumConstants {

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

  private static final ImmutableList<String> EMPTY_UNMODIFIABLE_LIST_STRING;

  static {
    final List<String> list = new ArrayList<>();
    EMPTY_UNMODIFIABLE_LIST_STRING = NonNullWrapperCommon.copyOfList(list);
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

  public static ImmutableList<String> constructListString(String... squareArray) {
    if (squareArray.length == 0) {
      return EMPTY_UNMODIFIABLE_LIST_STRING;
    }
    // the array is not constructed as null
    @SuppressWarnings("null") final List<String> list = NonNullWrapperCommon.asList(squareArray);
    return NonNullWrapperCommon.copyOfList(list);
  }

  public static ImmutableList<ImmutableList<Square>> constructListList(ImmutableList<Square> squareListNorthEast,
      ImmutableList<Square> squareListSouthEast, ImmutableList<Square> squareListSouthWest,
      ImmutableList<Square> squareListNorthWest) {
    // the array is not constructed as null
    final List<ImmutableList<Square>> listList = new ArrayList<>();
    listList.add(squareListNorthEast);
    listList.add(squareListSouthEast);
    listList.add(squareListSouthWest);
    listList.add(squareListNorthWest);
    return NonNullWrapperCommon.copyOfList(listList);
  }

  public static ImmutableList<ImmutableList<Square>> constructListList(ImmutableList<Square> squareListNorth,
      ImmutableList<Square> squareListEast, ImmutableList<Square> squareListSouth, ImmutableList<Square> squareListWest,
      ImmutableList<Square> squareListNorthEast, ImmutableList<Square> squareListSouthEast,
      ImmutableList<Square> squareListSouthWest, ImmutableList<Square> squareListNorthWest) {
    // the array is not constructed as null
    final List<ImmutableList<Square>> listList = new ArrayList<>();
    listList.add(squareListNorth);
    listList.add(squareListEast);
    listList.add(squareListSouth);
    listList.add(squareListWest);

    listList.add(squareListNorthEast);
    listList.add(squareListSouthEast);
    listList.add(squareListSouthWest);
    listList.add(squareListNorthWest);
    return NonNullWrapperCommon.copyOfList(listList);
  }

}
