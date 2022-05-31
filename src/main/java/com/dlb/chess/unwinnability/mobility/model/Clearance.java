package com.dlb.chess.unwinnability.mobility.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.unwinnability.mobility.enums.VariableState;
import com.dlb.chess.unwinnability.model.PiecePlacement;

public class Clearance {

  private final Map<PiecePlacement, VariableState> map = new HashMap<>();

  public void put(PiecePlacement piecePlacement, VariableState binary) {
    map.put(piecePlacement, binary);
  }

  public VariableState get(PiecePlacement piecePlacement) {
    if (!map.containsKey(piecePlacement)) {
      throw new IllegalArgumentException("Value is not set for piece placement " + piecePlacement);
    }
    return NonNullWrapperCommon.get(map, piecePlacement);
  }

  // TODO unwinnability - use values two times, that is much easier
  public int calculateVariableCountSetToOne() {
    var count = 0;
    for (final Entry<PiecePlacement, VariableState> entry : map.entrySet()) {
      if (entry.getValue() == VariableState.ONE) {
        count++;
      }
    }
    return count;
  }

  public List<PiecePlacement> calculateEntriesWithValueZero() {
    return calculateEntries(VariableState.ZERO);
  }

  public List<PiecePlacement> calculateEntriesWithValueOne() {
    return calculateEntries(VariableState.ONE);
  }

  public List<PiecePlacement> calculateEntries(VariableState variableState) {
    final List<PiecePlacement> result = new ArrayList<>();
    for (final Entry<PiecePlacement, VariableState> entry : map.entrySet()) {
      if (entry.getValue() == variableState) {
        result.add(entry.getKey());
      }
    }
    return result;
  }

  public void debug() {
    System.out.println("");
    System.out.println("Clearance:");

    // TreeSet for ordering
    for (final PiecePlacement piecePlacement : new TreeSet<>(map.keySet())) {
      final VariableState variableState = NonNullWrapperCommon.get(map, piecePlacement);
      final var pieceDescription = new StringBuilder();
      pieceDescription.append(piecePlacement.toString());
      pieceDescription.append(": ");
      pieceDescription.append(variableState.getDescription());
      System.out.println(pieceDescription.toString());
    }
  }

}
