package com.dlb.chess.common.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.model.PossibleMove;
import com.dlb.chess.model.EmptyBoardMove;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

public class ImmutableUtility implements EnumConstants {

  private static final ImmutableSet<Square> EMPTY_UNMODIFIABLE_SET;

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

  public static ImmutableMap<Side, ImmutableMap<Square, ImmutableMap<Square, PossibleMove>>> calculateMapForSet(
      PieceType pieceType, Set<EmptyBoardMove> emptyBoardMoveSetWhite, Set<EmptyBoardMove> emptyBoardMoveSetBlack) {
    final ImmutableMap<Square, ImmutableMap<Square, PossibleMove>> mapWhite = calculateMapForSetSide(WHITE, pieceType,
        emptyBoardMoveSetWhite);
    final ImmutableMap<Square, ImmutableMap<Square, PossibleMove>> mapBlack = calculateMapForSetSide(BLACK, pieceType,
        emptyBoardMoveSetBlack);

    final EnumMap<Side, ImmutableMap<Square, ImmutableMap<Square, PossibleMove>>> possibleMoveMap = NonNullWrapperCommon
        .newEnumMap(Side.class);
    possibleMoveMap.put(WHITE, mapWhite);
    possibleMoveMap.put(BLACK, mapBlack);

    return NonNullWrapperCommon.copyOfMap(possibleMoveMap);
  }

  private static ImmutableMap<Square, ImmutableMap<Square, PossibleMove>> calculateMapForSetSide(Side side,
      PieceType pieceType, Set<EmptyBoardMove> emptyBoardMoveSetSide) {

    final EnumMap<Square, ImmutableMap<Square, PossibleMove>> mapSide = NonNullWrapperCommon.newEnumMap(Square.class);

    for (final Square fromSquare : Square.BOARD_SQUARE_LIST) {
      final EnumMap<Square, PossibleMove> squareFromToMapSide = NonNullWrapperCommon.newEnumMap(Square.class);
      for (final EmptyBoardMove emptyBoardMove : emptyBoardMoveSetSide) {
        final PossibleMove possibleMoveSide = new PossibleMove(side, pieceType, emptyBoardMove.fromSquare(),
            emptyBoardMove.toSquare());
        squareFromToMapSide.put(fromSquare, possibleMoveSide);

      }
      @NonNull final ImmutableMap<Square, PossibleMove> squareFromToMapSideUnmodifiableMap = NonNullWrapperCommon
          .immutableEnumMap(squareFromToMapSide);
      mapSide.put(fromSquare, squareFromToMapSideUnmodifiableMap);
    }

    @NonNull final ImmutableMap<Square, ImmutableMap<Square, PossibleMove>> mapSideUnmodifiableMap = NonNullWrapperCommon
        .immutableEnumMap(mapSide);

    return mapSideUnmodifiableMap;
  }
}
