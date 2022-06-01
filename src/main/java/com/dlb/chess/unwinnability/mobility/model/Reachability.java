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

  private final Map<Side, Map<Square, VariableState>> reachabilityMap = new HashMap<>();

  public void put(Side side, Square toSquare, VariableState reachable) {
    Map<Square, VariableState> map;
    if (!reachabilityMap.containsKey(side)) {
      map = new HashMap<>();
      reachabilityMap.put(side, map);
    } else {
      map = NonNullWrapperCommon.get(reachabilityMap, side);
    }

    map.put(toSquare, reachable);
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
    for (final Entry<Side, Map<Square, VariableState>> mapEntryMap : reachabilityMap.entrySet()) {
      final Map<Square, VariableState> mapEntry = NonNullWrapperCommon.get(reachabilityMap, mapEntryMap.getKey());
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
    for (final Entry<Side, Map<Square, VariableState>> mapEntryMap : reachabilityMap.entrySet()) {
      final Map<Square, VariableState> mapEntry = NonNullWrapperCommon.get(reachabilityMap, mapEntryMap.getKey());
      for (final Entry<Square, VariableState> entry : mapEntry.entrySet()) {
        if (entry.getValue() == reachable) {
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
        squareSet.add(reachabilityVariable.toSquare());
      }
    }
    return squareSet;
  }
}
