package com.dlb.chess.unwinnability.mobility.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.unwinnability.mobility.enums.VariableState;
import com.dlb.chess.unwinnability.model.PiecePlacement;

public class Clearance {

  private final Map<PiecePlacement, VariableState> values = new HashMap<>();

  public void put(PiecePlacement piecePlacement, VariableState binary) {
    values.put(piecePlacement, binary);
  }

  public VariableState get(PiecePlacement piecePlacement) {
    if (!values.containsKey(piecePlacement)) {
      throw new IllegalArgumentException("Value is not set for piece placement " + piecePlacement);
    }
    return NonNullWrapperCommon.get(values, piecePlacement);
  }

  // TODO unwinnability - use values two times, that is much easier
  public int calculateVariableCountSetToOne() {
    var count = 0;
    for (final Entry<PiecePlacement, VariableState> entry : values.entrySet()) {
      if (entry.getValue() == VariableState.ONE) {
        count++;
      }
    }
    return count;
  }

  public List<PiecePlacement> calculateEntriesWithValueZero() {
    final List<PiecePlacement> result = new ArrayList<>();
    for (final Entry<PiecePlacement, VariableState> entry : values.entrySet()) {
      if (entry.getValue() == VariableState.ZERO) {
        result.add(entry.getKey());
      }
    }
    return result;
  }
}
