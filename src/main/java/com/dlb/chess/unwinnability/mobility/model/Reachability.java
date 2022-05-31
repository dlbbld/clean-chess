package com.dlb.chess.unwinnability.mobility.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.utility.GeneralUtility;
import com.dlb.chess.unwinnability.mobility.enums.VariableState;

public class Reachability {

  private final Map<Side, Map<Square, VariableState>> values = new HashMap<>();

  public void put(Side c, Square s, VariableState binary) {
    Map<Square, VariableState> map;
    if (!values.containsKey(c)) {
      map = new HashMap<>();
      values.put(c, map);
    } else {
      map = NonNullWrapperCommon.get(values, c);
    }

    map.put(s, binary);
  }

  public VariableState get(Side c, Square s) {
    if (!values.containsKey(c)) {
      throw new IllegalArgumentException("Value is not set for color " + c);
    }

    final Map<Square, VariableState> map = NonNullWrapperCommon.get(values, c);
    if (!map.containsKey(s)) {
      throw new IllegalArgumentException("Value is not set for square " + s);
    }

    return NonNullWrapperCommon.get(map, s);
  }

  // TODO unwinnability - use values two times, that is much easier
  public int calculateVariableCountSetToOne() {
    var count = 0;
    for (final Entry<Side, Map<Square, VariableState>> mapEntryMap : values.entrySet()) {
      final Map<Square, VariableState> mapEntry = NonNullWrapperCommon.get(values, mapEntryMap.getKey());
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

  private List<ReachabilityVariable> calculateEntries(VariableState variableState) {
    final List<ReachabilityVariable> result = new ArrayList<>();
    for (final Entry<Side, Map<Square, VariableState>> mapEntryMap : values.entrySet()) {
      final Map<Square, VariableState> mapEntry = NonNullWrapperCommon.get(values, mapEntryMap.getKey());
      for (final Entry<Square, VariableState> entry : mapEntry.entrySet()) {
        if (entry.getValue() == variableState) {
          result.add(new ReachabilityVariable(mapEntryMap.getKey(), entry.getKey()));
        }
      }
    }
    return result;
  }

  public void debug() {
    final List<ReachabilityVariable> entriesWithValueOneList = calculateEntriesWithValueOne();

    System.out.println("");
    System.out.println("Reachability:");

    final Set<Square> reachableSquareSetWhite = calculateSquareSet(Side.WHITE, entriesWithValueOneList);
    final String squareListWhite = GeneralUtility.calculateSquareList(reachableSquareSetWhite);
    System.out.println(Side.WHITE.getName() + ": " + squareListWhite);

    final Set<Square> reachableSquareSetBlack = calculateSquareSet(Side.BLACK, entriesWithValueOneList);
    final String squareListBlack = GeneralUtility.calculateSquareList(reachableSquareSetBlack);
    System.out.println(Side.BLACK.getName() + ": " + squareListBlack);
  }

  private static Set<Square> calculateSquareSet(Side side, List<ReachabilityVariable> entryList) {
    final Set<Square> squareSet = new TreeSet<>();
    for (final ReachabilityVariable reachabilityVariable : entryList) {
      if (reachabilityVariable.sideWhichCanReach() == side) {
        squareSet.add(reachabilityVariable.reachableSquare());
      }
    }
    return squareSet;
  }
}
