package com.dlb.chess.unwinnability.mobility.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.utility.GeneralUtility;
import com.dlb.chess.unwinnability.mobility.enums.VariableState;
import com.dlb.chess.unwinnability.model.PiecePlacement;

public class MobilitySolution {

  private final Map<PiecePlacement, Map<Square, VariableState>> values = new HashMap<>();

  public void put(PiecePlacement piecePlacement, Square s, VariableState binary) {
    Map<Square, VariableState> map;
    if (!values.containsKey(piecePlacement)) {
      map = new HashMap<>();
      values.put(piecePlacement, map);
    } else {
      map = NonNullWrapperCommon.get(values, piecePlacement);
    }

    map.put(s, binary);
  }

  public VariableState get(PiecePlacement piecePlacement, Square s) {
    if (!values.containsKey(piecePlacement)) {
      throw new IllegalArgumentException("Value is not set for piece placement " + piecePlacement);
    }

    final Map<Square, VariableState> map = NonNullWrapperCommon.get(values, piecePlacement);
    if (!map.containsKey(s)) {
      throw new IllegalArgumentException("Value is not set for square " + s);
    }

    return NonNullWrapperCommon.get(map, s);
  }

  public int calculateVariableCountSetToOne() {
    return calculateEntries(VariableState.ONE).size();
  }

  public List<MobilitySolutionVariable> calculateEntriesWithValueZero() {
    return calculateEntries(VariableState.ZERO);
  }

  public List<MobilitySolutionVariable> calculateEntriesWithValueOne() {
    return calculateEntries(VariableState.ONE);
  }

  public Set<Square> calculateSquaresWithValueOne(PiecePlacement piecePlacement) {
    final Set<Square> squareSet = new TreeSet<>();

    if (!values.containsKey(piecePlacement)) {
      throw new IllegalArgumentException("No such piece placement");
    }
    final Map<Square, VariableState> map = NonNullWrapperCommon.get(values, piecePlacement);

    for (final Square squareCandidate : map.keySet()) {
      if (NonNullWrapperCommon.get(map, squareCandidate) == VariableState.ONE) {
        squareSet.add(squareCandidate);
      }
    }
    return squareSet;
  }

  public List<MobilitySolutionVariable> calculateEntries(VariableState binaryValue) {
    final List<MobilitySolutionVariable> result = new ArrayList<>();
    for (final Entry<PiecePlacement, Map<Square, VariableState>> mapEntryMap : values.entrySet()) {
      final Map<Square, VariableState> mapEntry = NonNullWrapperCommon.get(values, mapEntryMap.getKey());
      for (final Entry<Square, VariableState> entry : mapEntry.entrySet()) {
        if (entry.getValue() == binaryValue) {
          result.add(new MobilitySolutionVariable(mapEntryMap.getKey(), entry.getKey()));
        }
      }
    }
    return result;
  }

  public Set<PiecePlacement> getPiecePlacementSet() {
    @SuppressWarnings("null") @NonNull final Set<PiecePlacement> keySet = values.keySet();
    // treeset for ordering
    return new TreeSet<>(keySet);
  }

  public void debug() {

    System.out.println("");
    System.out.println("Mobility:");

    for (final PiecePlacement piecePlacement : new TreeSet<>(values.keySet())) {
      final Map<Square, VariableState> valuePlacement = NonNullWrapperCommon.get(values, piecePlacement);
      final Set<Square> reachable = new TreeSet<>();
      for (final Square square : valuePlacement.keySet()) {
        final VariableState stateSquare = NonNullWrapperCommon.get(valuePlacement, square);
        if (stateSquare == VariableState.ONE) {
          reachable.add(square);
        }
      }
      final var pieceDescription = new StringBuilder();
      pieceDescription.append(piecePlacement.toString());
      final String squareList = GeneralUtility.calculateSquareList(reachable);
      pieceDescription.append(": ");
      pieceDescription.append(squareList);
      System.out.println(pieceDescription.toString());
    }
  }
}
