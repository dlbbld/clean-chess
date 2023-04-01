package com.dlb.chess.squares.emptyboard;

import static com.dlb.chess.common.utility.ImmutableUtility.constructListSquare;

import java.util.EnumMap;

import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.range.model.RookRange;
import com.dlb.chess.utility.ValidateMoveNumberUtility;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class RookEmptyBoardSquares extends AbstractEmptyBoardSquares implements EnumConstants {

  // BEGIN generated code

  private static void addA1(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(A2, A3, A4, A5, A6, A7, A8);
    final ImmutableList<Square> squareListEast = constructListSquare(B1, C1, D1, E1, F1, G1, H1);
    final ImmutableList<Square> squareListSouth = constructListSquare();
    final ImmutableList<Square> squareListWest = constructListSquare();
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(A1, rookRange);
  }

  private static void addB1(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(B2, B3, B4, B5, B6, B7, B8);
    final ImmutableList<Square> squareListEast = constructListSquare(C1, D1, E1, F1, G1, H1);
    final ImmutableList<Square> squareListSouth = constructListSquare();
    final ImmutableList<Square> squareListWest = constructListSquare(A1);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(B1, rookRange);
  }

  private static void addC1(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(C2, C3, C4, C5, C6, C7, C8);
    final ImmutableList<Square> squareListEast = constructListSquare(D1, E1, F1, G1, H1);
    final ImmutableList<Square> squareListSouth = constructListSquare();
    final ImmutableList<Square> squareListWest = constructListSquare(B1, A1);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(C1, rookRange);
  }

  private static void addD1(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(D2, D3, D4, D5, D6, D7, D8);
    final ImmutableList<Square> squareListEast = constructListSquare(E1, F1, G1, H1);
    final ImmutableList<Square> squareListSouth = constructListSquare();
    final ImmutableList<Square> squareListWest = constructListSquare(C1, B1, A1);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(D1, rookRange);
  }

  private static void addE1(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(E2, E3, E4, E5, E6, E7, E8);
    final ImmutableList<Square> squareListEast = constructListSquare(F1, G1, H1);
    final ImmutableList<Square> squareListSouth = constructListSquare();
    final ImmutableList<Square> squareListWest = constructListSquare(D1, C1, B1, A1);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(E1, rookRange);
  }

  private static void addF1(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(F2, F3, F4, F5, F6, F7, F8);
    final ImmutableList<Square> squareListEast = constructListSquare(G1, H1);
    final ImmutableList<Square> squareListSouth = constructListSquare();
    final ImmutableList<Square> squareListWest = constructListSquare(E1, D1, C1, B1, A1);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(F1, rookRange);
  }

  private static void addG1(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(G2, G3, G4, G5, G6, G7, G8);
    final ImmutableList<Square> squareListEast = constructListSquare(H1);
    final ImmutableList<Square> squareListSouth = constructListSquare();
    final ImmutableList<Square> squareListWest = constructListSquare(F1, E1, D1, C1, B1, A1);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(G1, rookRange);
  }

  private static void addH1(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(H2, H3, H4, H5, H6, H7, H8);
    final ImmutableList<Square> squareListEast = constructListSquare();
    final ImmutableList<Square> squareListSouth = constructListSquare();
    final ImmutableList<Square> squareListWest = constructListSquare(G1, F1, E1, D1, C1, B1, A1);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(H1, rookRange);
  }

  private static void addA2(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(A3, A4, A5, A6, A7, A8);
    final ImmutableList<Square> squareListEast = constructListSquare(B2, C2, D2, E2, F2, G2, H2);
    final ImmutableList<Square> squareListSouth = constructListSquare(A1);
    final ImmutableList<Square> squareListWest = constructListSquare();
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(A2, rookRange);
  }

  private static void addB2(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(B3, B4, B5, B6, B7, B8);
    final ImmutableList<Square> squareListEast = constructListSquare(C2, D2, E2, F2, G2, H2);
    final ImmutableList<Square> squareListSouth = constructListSquare(B1);
    final ImmutableList<Square> squareListWest = constructListSquare(A2);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(B2, rookRange);
  }

  private static void addC2(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(C3, C4, C5, C6, C7, C8);
    final ImmutableList<Square> squareListEast = constructListSquare(D2, E2, F2, G2, H2);
    final ImmutableList<Square> squareListSouth = constructListSquare(C1);
    final ImmutableList<Square> squareListWest = constructListSquare(B2, A2);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(C2, rookRange);
  }

  private static void addD2(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(D3, D4, D5, D6, D7, D8);
    final ImmutableList<Square> squareListEast = constructListSquare(E2, F2, G2, H2);
    final ImmutableList<Square> squareListSouth = constructListSquare(D1);
    final ImmutableList<Square> squareListWest = constructListSquare(C2, B2, A2);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(D2, rookRange);
  }

  private static void addE2(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(E3, E4, E5, E6, E7, E8);
    final ImmutableList<Square> squareListEast = constructListSquare(F2, G2, H2);
    final ImmutableList<Square> squareListSouth = constructListSquare(E1);
    final ImmutableList<Square> squareListWest = constructListSquare(D2, C2, B2, A2);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(E2, rookRange);
  }

  private static void addF2(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(F3, F4, F5, F6, F7, F8);
    final ImmutableList<Square> squareListEast = constructListSquare(G2, H2);
    final ImmutableList<Square> squareListSouth = constructListSquare(F1);
    final ImmutableList<Square> squareListWest = constructListSquare(E2, D2, C2, B2, A2);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(F2, rookRange);
  }

  private static void addG2(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(G3, G4, G5, G6, G7, G8);
    final ImmutableList<Square> squareListEast = constructListSquare(H2);
    final ImmutableList<Square> squareListSouth = constructListSquare(G1);
    final ImmutableList<Square> squareListWest = constructListSquare(F2, E2, D2, C2, B2, A2);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(G2, rookRange);
  }

  private static void addH2(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(H3, H4, H5, H6, H7, H8);
    final ImmutableList<Square> squareListEast = constructListSquare();
    final ImmutableList<Square> squareListSouth = constructListSquare(H1);
    final ImmutableList<Square> squareListWest = constructListSquare(G2, F2, E2, D2, C2, B2, A2);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(H2, rookRange);
  }

  private static void addA3(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(A4, A5, A6, A7, A8);
    final ImmutableList<Square> squareListEast = constructListSquare(B3, C3, D3, E3, F3, G3, H3);
    final ImmutableList<Square> squareListSouth = constructListSquare(A2, A1);
    final ImmutableList<Square> squareListWest = constructListSquare();
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(A3, rookRange);
  }

  private static void addB3(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(B4, B5, B6, B7, B8);
    final ImmutableList<Square> squareListEast = constructListSquare(C3, D3, E3, F3, G3, H3);
    final ImmutableList<Square> squareListSouth = constructListSquare(B2, B1);
    final ImmutableList<Square> squareListWest = constructListSquare(A3);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(B3, rookRange);
  }

  private static void addC3(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(C4, C5, C6, C7, C8);
    final ImmutableList<Square> squareListEast = constructListSquare(D3, E3, F3, G3, H3);
    final ImmutableList<Square> squareListSouth = constructListSquare(C2, C1);
    final ImmutableList<Square> squareListWest = constructListSquare(B3, A3);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(C3, rookRange);
  }

  private static void addD3(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(D4, D5, D6, D7, D8);
    final ImmutableList<Square> squareListEast = constructListSquare(E3, F3, G3, H3);
    final ImmutableList<Square> squareListSouth = constructListSquare(D2, D1);
    final ImmutableList<Square> squareListWest = constructListSquare(C3, B3, A3);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(D3, rookRange);
  }

  private static void addE3(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(E4, E5, E6, E7, E8);
    final ImmutableList<Square> squareListEast = constructListSquare(F3, G3, H3);
    final ImmutableList<Square> squareListSouth = constructListSquare(E2, E1);
    final ImmutableList<Square> squareListWest = constructListSquare(D3, C3, B3, A3);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(E3, rookRange);
  }

  private static void addF3(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(F4, F5, F6, F7, F8);
    final ImmutableList<Square> squareListEast = constructListSquare(G3, H3);
    final ImmutableList<Square> squareListSouth = constructListSquare(F2, F1);
    final ImmutableList<Square> squareListWest = constructListSquare(E3, D3, C3, B3, A3);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(F3, rookRange);
  }

  private static void addG3(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(G4, G5, G6, G7, G8);
    final ImmutableList<Square> squareListEast = constructListSquare(H3);
    final ImmutableList<Square> squareListSouth = constructListSquare(G2, G1);
    final ImmutableList<Square> squareListWest = constructListSquare(F3, E3, D3, C3, B3, A3);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(G3, rookRange);
  }

  private static void addH3(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(H4, H5, H6, H7, H8);
    final ImmutableList<Square> squareListEast = constructListSquare();
    final ImmutableList<Square> squareListSouth = constructListSquare(H2, H1);
    final ImmutableList<Square> squareListWest = constructListSquare(G3, F3, E3, D3, C3, B3, A3);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(H3, rookRange);
  }

  private static void addA4(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(A5, A6, A7, A8);
    final ImmutableList<Square> squareListEast = constructListSquare(B4, C4, D4, E4, F4, G4, H4);
    final ImmutableList<Square> squareListSouth = constructListSquare(A3, A2, A1);
    final ImmutableList<Square> squareListWest = constructListSquare();
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(A4, rookRange);
  }

  private static void addB4(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(B5, B6, B7, B8);
    final ImmutableList<Square> squareListEast = constructListSquare(C4, D4, E4, F4, G4, H4);
    final ImmutableList<Square> squareListSouth = constructListSquare(B3, B2, B1);
    final ImmutableList<Square> squareListWest = constructListSquare(A4);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(B4, rookRange);
  }

  private static void addC4(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(C5, C6, C7, C8);
    final ImmutableList<Square> squareListEast = constructListSquare(D4, E4, F4, G4, H4);
    final ImmutableList<Square> squareListSouth = constructListSquare(C3, C2, C1);
    final ImmutableList<Square> squareListWest = constructListSquare(B4, A4);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(C4, rookRange);
  }

  private static void addD4(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(D5, D6, D7, D8);
    final ImmutableList<Square> squareListEast = constructListSquare(E4, F4, G4, H4);
    final ImmutableList<Square> squareListSouth = constructListSquare(D3, D2, D1);
    final ImmutableList<Square> squareListWest = constructListSquare(C4, B4, A4);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(D4, rookRange);
  }

  private static void addE4(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(E5, E6, E7, E8);
    final ImmutableList<Square> squareListEast = constructListSquare(F4, G4, H4);
    final ImmutableList<Square> squareListSouth = constructListSquare(E3, E2, E1);
    final ImmutableList<Square> squareListWest = constructListSquare(D4, C4, B4, A4);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(E4, rookRange);
  }

  private static void addF4(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(F5, F6, F7, F8);
    final ImmutableList<Square> squareListEast = constructListSquare(G4, H4);
    final ImmutableList<Square> squareListSouth = constructListSquare(F3, F2, F1);
    final ImmutableList<Square> squareListWest = constructListSquare(E4, D4, C4, B4, A4);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(F4, rookRange);
  }

  private static void addG4(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(G5, G6, G7, G8);
    final ImmutableList<Square> squareListEast = constructListSquare(H4);
    final ImmutableList<Square> squareListSouth = constructListSquare(G3, G2, G1);
    final ImmutableList<Square> squareListWest = constructListSquare(F4, E4, D4, C4, B4, A4);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(G4, rookRange);
  }

  private static void addH4(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(H5, H6, H7, H8);
    final ImmutableList<Square> squareListEast = constructListSquare();
    final ImmutableList<Square> squareListSouth = constructListSquare(H3, H2, H1);
    final ImmutableList<Square> squareListWest = constructListSquare(G4, F4, E4, D4, C4, B4, A4);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(H4, rookRange);
  }

  private static void addA5(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(A6, A7, A8);
    final ImmutableList<Square> squareListEast = constructListSquare(B5, C5, D5, E5, F5, G5, H5);
    final ImmutableList<Square> squareListSouth = constructListSquare(A4, A3, A2, A1);
    final ImmutableList<Square> squareListWest = constructListSquare();
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(A5, rookRange);
  }

  private static void addB5(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(B6, B7, B8);
    final ImmutableList<Square> squareListEast = constructListSquare(C5, D5, E5, F5, G5, H5);
    final ImmutableList<Square> squareListSouth = constructListSquare(B4, B3, B2, B1);
    final ImmutableList<Square> squareListWest = constructListSquare(A5);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(B5, rookRange);
  }

  private static void addC5(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(C6, C7, C8);
    final ImmutableList<Square> squareListEast = constructListSquare(D5, E5, F5, G5, H5);
    final ImmutableList<Square> squareListSouth = constructListSquare(C4, C3, C2, C1);
    final ImmutableList<Square> squareListWest = constructListSquare(B5, A5);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(C5, rookRange);
  }

  private static void addD5(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(D6, D7, D8);
    final ImmutableList<Square> squareListEast = constructListSquare(E5, F5, G5, H5);
    final ImmutableList<Square> squareListSouth = constructListSquare(D4, D3, D2, D1);
    final ImmutableList<Square> squareListWest = constructListSquare(C5, B5, A5);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(D5, rookRange);
  }

  private static void addE5(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(E6, E7, E8);
    final ImmutableList<Square> squareListEast = constructListSquare(F5, G5, H5);
    final ImmutableList<Square> squareListSouth = constructListSquare(E4, E3, E2, E1);
    final ImmutableList<Square> squareListWest = constructListSquare(D5, C5, B5, A5);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(E5, rookRange);
  }

  private static void addF5(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(F6, F7, F8);
    final ImmutableList<Square> squareListEast = constructListSquare(G5, H5);
    final ImmutableList<Square> squareListSouth = constructListSquare(F4, F3, F2, F1);
    final ImmutableList<Square> squareListWest = constructListSquare(E5, D5, C5, B5, A5);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(F5, rookRange);
  }

  private static void addG5(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(G6, G7, G8);
    final ImmutableList<Square> squareListEast = constructListSquare(H5);
    final ImmutableList<Square> squareListSouth = constructListSquare(G4, G3, G2, G1);
    final ImmutableList<Square> squareListWest = constructListSquare(F5, E5, D5, C5, B5, A5);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(G5, rookRange);
  }

  private static void addH5(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(H6, H7, H8);
    final ImmutableList<Square> squareListEast = constructListSquare();
    final ImmutableList<Square> squareListSouth = constructListSquare(H4, H3, H2, H1);
    final ImmutableList<Square> squareListWest = constructListSquare(G5, F5, E5, D5, C5, B5, A5);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(H5, rookRange);
  }

  private static void addA6(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(A7, A8);
    final ImmutableList<Square> squareListEast = constructListSquare(B6, C6, D6, E6, F6, G6, H6);
    final ImmutableList<Square> squareListSouth = constructListSquare(A5, A4, A3, A2, A1);
    final ImmutableList<Square> squareListWest = constructListSquare();
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(A6, rookRange);
  }

  private static void addB6(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(B7, B8);
    final ImmutableList<Square> squareListEast = constructListSquare(C6, D6, E6, F6, G6, H6);
    final ImmutableList<Square> squareListSouth = constructListSquare(B5, B4, B3, B2, B1);
    final ImmutableList<Square> squareListWest = constructListSquare(A6);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(B6, rookRange);
  }

  private static void addC6(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(C7, C8);
    final ImmutableList<Square> squareListEast = constructListSquare(D6, E6, F6, G6, H6);
    final ImmutableList<Square> squareListSouth = constructListSquare(C5, C4, C3, C2, C1);
    final ImmutableList<Square> squareListWest = constructListSquare(B6, A6);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(C6, rookRange);
  }

  private static void addD6(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(D7, D8);
    final ImmutableList<Square> squareListEast = constructListSquare(E6, F6, G6, H6);
    final ImmutableList<Square> squareListSouth = constructListSquare(D5, D4, D3, D2, D1);
    final ImmutableList<Square> squareListWest = constructListSquare(C6, B6, A6);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(D6, rookRange);
  }

  private static void addE6(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(E7, E8);
    final ImmutableList<Square> squareListEast = constructListSquare(F6, G6, H6);
    final ImmutableList<Square> squareListSouth = constructListSquare(E5, E4, E3, E2, E1);
    final ImmutableList<Square> squareListWest = constructListSquare(D6, C6, B6, A6);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(E6, rookRange);
  }

  private static void addF6(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(F7, F8);
    final ImmutableList<Square> squareListEast = constructListSquare(G6, H6);
    final ImmutableList<Square> squareListSouth = constructListSquare(F5, F4, F3, F2, F1);
    final ImmutableList<Square> squareListWest = constructListSquare(E6, D6, C6, B6, A6);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(F6, rookRange);
  }

  private static void addG6(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(G7, G8);
    final ImmutableList<Square> squareListEast = constructListSquare(H6);
    final ImmutableList<Square> squareListSouth = constructListSquare(G5, G4, G3, G2, G1);
    final ImmutableList<Square> squareListWest = constructListSquare(F6, E6, D6, C6, B6, A6);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(G6, rookRange);
  }

  private static void addH6(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(H7, H8);
    final ImmutableList<Square> squareListEast = constructListSquare();
    final ImmutableList<Square> squareListSouth = constructListSquare(H5, H4, H3, H2, H1);
    final ImmutableList<Square> squareListWest = constructListSquare(G6, F6, E6, D6, C6, B6, A6);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(H6, rookRange);
  }

  private static void addA7(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(A8);
    final ImmutableList<Square> squareListEast = constructListSquare(B7, C7, D7, E7, F7, G7, H7);
    final ImmutableList<Square> squareListSouth = constructListSquare(A6, A5, A4, A3, A2, A1);
    final ImmutableList<Square> squareListWest = constructListSquare();
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(A7, rookRange);
  }

  private static void addB7(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(B8);
    final ImmutableList<Square> squareListEast = constructListSquare(C7, D7, E7, F7, G7, H7);
    final ImmutableList<Square> squareListSouth = constructListSquare(B6, B5, B4, B3, B2, B1);
    final ImmutableList<Square> squareListWest = constructListSquare(A7);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(B7, rookRange);
  }

  private static void addC7(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(C8);
    final ImmutableList<Square> squareListEast = constructListSquare(D7, E7, F7, G7, H7);
    final ImmutableList<Square> squareListSouth = constructListSquare(C6, C5, C4, C3, C2, C1);
    final ImmutableList<Square> squareListWest = constructListSquare(B7, A7);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(C7, rookRange);
  }

  private static void addD7(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(D8);
    final ImmutableList<Square> squareListEast = constructListSquare(E7, F7, G7, H7);
    final ImmutableList<Square> squareListSouth = constructListSquare(D6, D5, D4, D3, D2, D1);
    final ImmutableList<Square> squareListWest = constructListSquare(C7, B7, A7);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(D7, rookRange);
  }

  private static void addE7(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(E8);
    final ImmutableList<Square> squareListEast = constructListSquare(F7, G7, H7);
    final ImmutableList<Square> squareListSouth = constructListSquare(E6, E5, E4, E3, E2, E1);
    final ImmutableList<Square> squareListWest = constructListSquare(D7, C7, B7, A7);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(E7, rookRange);
  }

  private static void addF7(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(F8);
    final ImmutableList<Square> squareListEast = constructListSquare(G7, H7);
    final ImmutableList<Square> squareListSouth = constructListSquare(F6, F5, F4, F3, F2, F1);
    final ImmutableList<Square> squareListWest = constructListSquare(E7, D7, C7, B7, A7);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(F7, rookRange);
  }

  private static void addG7(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(G8);
    final ImmutableList<Square> squareListEast = constructListSquare(H7);
    final ImmutableList<Square> squareListSouth = constructListSquare(G6, G5, G4, G3, G2, G1);
    final ImmutableList<Square> squareListWest = constructListSquare(F7, E7, D7, C7, B7, A7);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(G7, rookRange);
  }

  private static void addH7(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare(H8);
    final ImmutableList<Square> squareListEast = constructListSquare();
    final ImmutableList<Square> squareListSouth = constructListSquare(H6, H5, H4, H3, H2, H1);
    final ImmutableList<Square> squareListWest = constructListSquare(G7, F7, E7, D7, C7, B7, A7);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(H7, rookRange);
  }

  private static void addA8(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare();
    final ImmutableList<Square> squareListEast = constructListSquare(B8, C8, D8, E8, F8, G8, H8);
    final ImmutableList<Square> squareListSouth = constructListSquare(A7, A6, A5, A4, A3, A2, A1);
    final ImmutableList<Square> squareListWest = constructListSquare();
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(A8, rookRange);
  }

  private static void addB8(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare();
    final ImmutableList<Square> squareListEast = constructListSquare(C8, D8, E8, F8, G8, H8);
    final ImmutableList<Square> squareListSouth = constructListSquare(B7, B6, B5, B4, B3, B2, B1);
    final ImmutableList<Square> squareListWest = constructListSquare(A8);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(B8, rookRange);
  }

  private static void addC8(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare();
    final ImmutableList<Square> squareListEast = constructListSquare(D8, E8, F8, G8, H8);
    final ImmutableList<Square> squareListSouth = constructListSquare(C7, C6, C5, C4, C3, C2, C1);
    final ImmutableList<Square> squareListWest = constructListSquare(B8, A8);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(C8, rookRange);
  }

  private static void addD8(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare();
    final ImmutableList<Square> squareListEast = constructListSquare(E8, F8, G8, H8);
    final ImmutableList<Square> squareListSouth = constructListSquare(D7, D6, D5, D4, D3, D2, D1);
    final ImmutableList<Square> squareListWest = constructListSquare(C8, B8, A8);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(D8, rookRange);
  }

  private static void addE8(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare();
    final ImmutableList<Square> squareListEast = constructListSquare(F8, G8, H8);
    final ImmutableList<Square> squareListSouth = constructListSquare(E7, E6, E5, E4, E3, E2, E1);
    final ImmutableList<Square> squareListWest = constructListSquare(D8, C8, B8, A8);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(E8, rookRange);
  }

  private static void addF8(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare();
    final ImmutableList<Square> squareListEast = constructListSquare(G8, H8);
    final ImmutableList<Square> squareListSouth = constructListSquare(F7, F6, F5, F4, F3, F2, F1);
    final ImmutableList<Square> squareListWest = constructListSquare(E8, D8, C8, B8, A8);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(F8, rookRange);
  }

  private static void addG8(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare();
    final ImmutableList<Square> squareListEast = constructListSquare(H8);
    final ImmutableList<Square> squareListSouth = constructListSquare(G7, G6, G5, G4, G3, G2, G1);
    final ImmutableList<Square> squareListWest = constructListSquare(F8, E8, D8, C8, B8, A8);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(G8, rookRange);
  }

  private static void addH8(EnumMap<Square, RookRange> map) {
    final ImmutableList<Square> squareListNorth = constructListSquare();
    final ImmutableList<Square> squareListEast = constructListSquare();
    final ImmutableList<Square> squareListSouth = constructListSquare(H7, H6, H5, H4, H3, H2, H1);
    final ImmutableList<Square> squareListWest = constructListSquare(G8, F8, E8, D8, C8, B8, A8);
    final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
    map.put(H8, rookRange);
  }

  private static final ImmutableMap<Square, RookRange> ROOK_SQUARES_MAP;

  static {
    final EnumMap<Square, RookRange> rookSquaresMap = NonNullWrapperCommon.newEnumMap(Square.class);

    addA1(rookSquaresMap);
    addB1(rookSquaresMap);
    addC1(rookSquaresMap);
    addD1(rookSquaresMap);
    addE1(rookSquaresMap);
    addF1(rookSquaresMap);
    addG1(rookSquaresMap);
    addH1(rookSquaresMap);
    addA2(rookSquaresMap);
    addB2(rookSquaresMap);
    addC2(rookSquaresMap);
    addD2(rookSquaresMap);
    addE2(rookSquaresMap);
    addF2(rookSquaresMap);
    addG2(rookSquaresMap);
    addH2(rookSquaresMap);
    addA3(rookSquaresMap);
    addB3(rookSquaresMap);
    addC3(rookSquaresMap);
    addD3(rookSquaresMap);
    addE3(rookSquaresMap);
    addF3(rookSquaresMap);
    addG3(rookSquaresMap);
    addH3(rookSquaresMap);
    addA4(rookSquaresMap);
    addB4(rookSquaresMap);
    addC4(rookSquaresMap);
    addD4(rookSquaresMap);
    addE4(rookSquaresMap);
    addF4(rookSquaresMap);
    addG4(rookSquaresMap);
    addH4(rookSquaresMap);
    addA5(rookSquaresMap);
    addB5(rookSquaresMap);
    addC5(rookSquaresMap);
    addD5(rookSquaresMap);
    addE5(rookSquaresMap);
    addF5(rookSquaresMap);
    addG5(rookSquaresMap);
    addH5(rookSquaresMap);
    addA6(rookSquaresMap);
    addB6(rookSquaresMap);
    addC6(rookSquaresMap);
    addD6(rookSquaresMap);
    addE6(rookSquaresMap);
    addF6(rookSquaresMap);
    addG6(rookSquaresMap);
    addH6(rookSquaresMap);
    addA7(rookSquaresMap);
    addB7(rookSquaresMap);
    addC7(rookSquaresMap);
    addD7(rookSquaresMap);
    addE7(rookSquaresMap);
    addF7(rookSquaresMap);
    addG7(rookSquaresMap);
    addH7(rookSquaresMap);
    addA8(rookSquaresMap);
    addB8(rookSquaresMap);
    addC8(rookSquaresMap);
    addD8(rookSquaresMap);
    addE8(rookSquaresMap);
    addF8(rookSquaresMap);
    addG8(rookSquaresMap);
    addH8(rookSquaresMap);

    ROOK_SQUARES_MAP = NonNullWrapperCommon.copyOfMap(rookSquaresMap);

    ValidateMoveNumberUtility.validateOrthogonalMoveNumber(ROOK_SQUARES_MAP, 896);

  }

  // END generated code

  public static RookRange getRookSquares(Square fromSquare) {
    return NonNullWrapperCommon.get(ROOK_SQUARES_MAP, fromSquare);
  }

}
