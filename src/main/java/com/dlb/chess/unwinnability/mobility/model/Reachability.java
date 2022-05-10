package com.dlb.chess.unwinnability.mobility.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
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
    final List<ReachabilityVariable> result = new ArrayList<>();
    for (final Entry<Side, Map<Square, VariableState>> mapEntryMap : values.entrySet()) {
      final Map<Square, VariableState> mapEntry = NonNullWrapperCommon.get(values, mapEntryMap.getKey());
      for (final Entry<Square, VariableState> entry : mapEntry.entrySet()) {
        if (entry.getValue() == VariableState.ZERO) {
          result.add(new ReachabilityVariable(mapEntryMap.getKey(), entry.getKey()));
        }
      }
    }
    return result;
  }
}
