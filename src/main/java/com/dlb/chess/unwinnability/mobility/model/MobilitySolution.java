package com.dlb.chess.unwinnability.mobility.model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.utility.BasicUtility;
import com.dlb.chess.common.utility.GeneralUtility;
import com.dlb.chess.unwinnability.mobility.enums.VariableState;
import com.dlb.chess.unwinnability.model.PiecePlacement;

public class MobilitySolution {

  private final Map<PiecePlacement, EnumMap<Square, VariableState>> mobilityMap = new HashMap<>();

  public void put(PiecePlacement piecePlacement, Square toSquare, VariableState mobility) {
    EnumMap<Square, VariableState> enumMap;
    if (!mobilityMap.containsKey(piecePlacement)) {
      enumMap = NonNullWrapperCommon.newEnumMap(Square.class);
      mobilityMap.put(piecePlacement, enumMap);
    } else {
      enumMap = NonNullWrapperCommon.get(mobilityMap, piecePlacement);
    }

    enumMap.put(toSquare, mobility);
  }

  public VariableState get(PiecePlacement piecePlacement, Square toSquare) {
    if (!mobilityMap.containsKey(piecePlacement)) {
      throw new IllegalArgumentException("Value is not set for piece placement " + piecePlacement);
    }

    final Map<Square, VariableState> map = NonNullWrapperCommon.get(mobilityMap, piecePlacement);
    if (!map.containsKey(toSquare)) {
      throw new IllegalArgumentException("Value is not set for square " + toSquare);
    }

    return NonNullWrapperCommon.get(map, toSquare);
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

    if (!mobilityMap.containsKey(piecePlacement)) {
      throw new IllegalArgumentException("No such piece placement");
    }
    final Map<Square, VariableState> map = NonNullWrapperCommon.get(mobilityMap, piecePlacement);

    for (final Square squareCandidate : map.keySet()) {
      if (NonNullWrapperCommon.get(map, squareCandidate) == VariableState.ONE) {
        squareSet.add(squareCandidate);
      }
    }
    return squareSet;
  }

  public List<MobilitySolutionVariable> calculateEntries(VariableState mobility) {
    final List<MobilitySolutionVariable> result = new ArrayList<>();
    for (final Entry<PiecePlacement, EnumMap<Square, VariableState>> mapEntryMap : mobilityMap.entrySet()) {
      final EnumMap<Square, VariableState> mapEntry = NonNullWrapperCommon.get(mobilityMap, mapEntryMap.getKey());
      for (final Entry<Square, VariableState> entry : mapEntry.entrySet()) {
        if (entry.getValue() == mobility) {
          result.add(new MobilitySolutionVariable(mapEntryMap.getKey(), entry.getKey()));
        }
      }
    }
    return result;
  }

  public Set<PiecePlacement> getPiecePlacementSet() {
    @SuppressWarnings("null") @NonNull final Set<PiecePlacement> keySet = mobilityMap.keySet();
    // treeset for ordering
    return new TreeSet<>(keySet);
  }

  public String print() {

    final List<String> lineList = new ArrayList<>();

    lineList.add("");
    lineList.add("Mobility:");

    for (final PiecePlacement piecePlacement : new TreeSet<>(mobilityMap.keySet())) {
      final Map<Square, VariableState> valuePlacement = NonNullWrapperCommon.get(mobilityMap, piecePlacement);
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
