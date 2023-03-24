package com.dlb.chess.unwinnability.mobility.model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.utility.BasicUtility;
import com.dlb.chess.common.utility.GeneralUtility;
import com.dlb.chess.unwinnability.mobility.enums.VariableState;

public class Reachability {

  private final EnumMap<Side, EnumMap<Square, VariableState>> reachabilityMap = NonNullWrapperCommon
      .newEnumMap(Side.class);

  public void put(Side side, Square toSquare, VariableState reachable) {
    EnumMap<Square, VariableState> enumMap;
    if (!reachabilityMap.containsKey(side)) {
      enumMap = NonNullWrapperCommon.newEnumMap(Square.class);
      reachabilityMap.put(side, enumMap);
    } else {
      enumMap = NonNullWrapperCommon.get(reachabilityMap, side);
    }

    enumMap.put(toSquare, reachable);
  }

  public VariableState get(Side side, Square toSquare) {
    if (!reachabilityMap.containsKey(side)) {
      throw new IllegalArgumentException("Value is not set for color " + side);
    }

    final Map<Square, VariableState> map = NonNullWrapperCommon.get(reachabilityMap, side);
    if (!map.containsKey(toSquare)) {
      throw new IllegalArgumentException("Value is not set for square " + toSquare);
    }

    return NonNullWrapperCommon.get(map, toSquare);
  }

  // TODO unwinnability - use values two times, that is much easier
  public int calculateVariableCountSetToOne() {
    var count = 0;
    for (final Entry<Side, EnumMap<Square, VariableState>> mapEntryMap : reachabilityMap.entrySet()) {
      final EnumMap<Square, VariableState> mapEntry = NonNullWrapperCommon.get(reachabilityMap, mapEntryMap.getKey());
      for (final Entry<Square, VariableState> entry : mapEntry.entrySet()) {
        if (entry.getValue() == VariableState.ONE) {
          count++;
        }
      }
    }
    return count;
  }

  public List<ReachabilityVariable> calculateEntriesWithValueZero() {
    return calculateEntries(VariableState.ZERO);
  }

  public List<ReachabilityVariable> calculateEntriesWithValueOne() {
    return calculateEntries(VariableState.ONE);
  }

  private List<ReachabilityVariable> calculateEntries(VariableState reachable) {
    final List<ReachabilityVariable> result = new ArrayList<>();
    for (final Entry<Side, EnumMap<Square, VariableState>> mapEntryMap : reachabilityMap.entrySet()) {
      final EnumMap<Square, VariableState> mapEntry = NonNullWrapperCommon.get(reachabilityMap, mapEntryMap.getKey());
      for (final Entry<Square, VariableState> entry : mapEntry.entrySet()) {
        if (entry.getValue() == reachable) {
          result.add(new ReachabilityVariable(mapEntryMap.getKey(), entry.getKey()));
        }
      }
    }
    return result;
  }

  public String print() {

    final List<String> lineList = new ArrayList<>();

    lineList.add("");
    lineList.add("Reachability:");

    final List<ReachabilityVariable> entriesWithValueOneList = calculateEntriesWithValueOne();

    final Set<Square> reachableSquareSetWhite = calculateSquareSet(Side.WHITE, entriesWithValueOneList);
    final String squareListWhite = GeneralUtility.calculateSquareList(reachableSquareSetWhite);
    lineList.add(Side.WHITE.getName() + ": " + squareListWhite);

    final Set<Square> reachableSquareSetBlack = calculateSquareSet(Side.BLACK, entriesWithValueOneList);
    final String squareListBlack = GeneralUtility.calculateSquareList(reachableSquareSetBlack);
    lineList.add(Side.BLACK.getName() + ": " + squareListBlack);

    return BasicUtility.convertToString(lineList);
  }

  private static Set<Square> calculateSquareSet(Side side, List<ReachabilityVariable> entryList) {
    final Set<Square> squareSet = new TreeSet<>();
    for (final ReachabilityVariable reachabilityVariable : entryList) {
      if (reachabilityVariable.sideWhichCanReach() == side) {
        squareSet.add(reachabilityVariable.toSquare());
      }
    }
    return squareSet;
  }

  @Override
  public String toString() {
    return print();
  }
}
