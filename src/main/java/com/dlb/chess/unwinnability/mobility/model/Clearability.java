package com.dlb.chess.unwinnability.mobility.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.utility.BasicUtility;
import com.dlb.chess.unwinnability.mobility.enums.VariableState;
import com.dlb.chess.unwinnability.model.PiecePlacement;

public class Clearability {

  private final Map<PiecePlacement, VariableState> clearabilityMap = new HashMap<>();

  public void put(PiecePlacement piecePlacement, VariableState clearable) {
    clearabilityMap.put(piecePlacement, clearable);
  }

  public VariableState get(PiecePlacement piecePlacement) {
    if (!clearabilityMap.containsKey(piecePlacement)) {
      throw new IllegalArgumentException("Value is not set for piece placement " + piecePlacement);
    }
    return NonNullWrapperCommon.get(clearabilityMap, piecePlacement);
  }

  // TODO unwinnability - use values two times, that is much easier
  public int calculateVariableCountSetToOne() {
    var count = 0;
    for (final Entry<PiecePlacement, VariableState> entry : clearabilityMap.entrySet()) {
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
    for (final Entry<PiecePlacement, VariableState> entry : clearabilityMap.entrySet()) {
      if (entry.getValue() == variableState) {
        result.add(entry.getKey());
      }
    }
    return result;
  }

  public String print() {

    final List<String> lineList = new ArrayList<>();

    lineList.add("");
    lineList.add("Clearability:");

    // TreeSet for ordering
    for (final PiecePlacement piecePlacement : new TreeSet<>(clearabilityMap.keySet())) {
      final VariableState variableState = NonNullWrapperCommon.get(clearabilityMap, piecePlacement);
      final var pieceDescription = new StringBuilder();
      pieceDescription.append(piecePlacement.toString());
      pieceDescription.append(": ");
      pieceDescription.append(variableState.getDescription());
      @SuppressWarnings("null") @NonNull final String string = pieceDescription.toString();
      lineList.add(string);
    }

    return BasicUtility.convertToString(lineList);
  }

  @Override
  public String toString() {
    return print();
  }

}
