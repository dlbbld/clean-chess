package com.dlb.chess.squares.emptyboard;

import static com.dlb.chess.common.utility.ImmutableUtility.constructListSquare;

import java.util.EnumMap;

import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.range.model.BishopRange;
import com.dlb.chess.utility.ValidateMoveNumberUtility;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class BishopEmptyBoardSquares extends AbstractEmptyBoardSquares implements EnumConstants {

  // BEGIN generated code

  private static void addA1(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(B2, C3, D4, E5, F6, G7, H8);
    final ImmutableList<Square> squareListSouthEast = constructListSquare();
    final ImmutableList<Square> squareListSouthWest = constructListSquare();
    final ImmutableList<Square> squareListNorthWest = constructListSquare();
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(A1, bishopRange);
  }

  private static void addB1(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(C2, D3, E4, F5, G6, H7);
    final ImmutableList<Square> squareListSouthEast = constructListSquare();
    final ImmutableList<Square> squareListSouthWest = constructListSquare();
    final ImmutableList<Square> squareListNorthWest = constructListSquare(A2);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(B1, bishopRange);
  }

  private static void addC1(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(D2, E3, F4, G5, H6);
    final ImmutableList<Square> squareListSouthEast = constructListSquare();
    final ImmutableList<Square> squareListSouthWest = constructListSquare();
    final ImmutableList<Square> squareListNorthWest = constructListSquare(B2, A3);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(C1, bishopRange);
  }

  private static void addD1(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(E2, F3, G4, H5);
    final ImmutableList<Square> squareListSouthEast = constructListSquare();
    final ImmutableList<Square> squareListSouthWest = constructListSquare();
    final ImmutableList<Square> squareListNorthWest = constructListSquare(C2, B3, A4);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(D1, bishopRange);
  }

  private static void addE1(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(F2, G3, H4);
    final ImmutableList<Square> squareListSouthEast = constructListSquare();
    final ImmutableList<Square> squareListSouthWest = constructListSquare();
    final ImmutableList<Square> squareListNorthWest = constructListSquare(D2, C3, B4, A5);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(E1, bishopRange);
  }

  private static void addF1(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(G2, H3);
    final ImmutableList<Square> squareListSouthEast = constructListSquare();
    final ImmutableList<Square> squareListSouthWest = constructListSquare();
    final ImmutableList<Square> squareListNorthWest = constructListSquare(E2, D3, C4, B5, A6);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(F1, bishopRange);
  }

  private static void addG1(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(H2);
    final ImmutableList<Square> squareListSouthEast = constructListSquare();
    final ImmutableList<Square> squareListSouthWest = constructListSquare();
    final ImmutableList<Square> squareListNorthWest = constructListSquare(F2, E3, D4, C5, B6, A7);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(G1, bishopRange);
  }

  private static void addH1(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare();
    final ImmutableList<Square> squareListSouthEast = constructListSquare();
    final ImmutableList<Square> squareListSouthWest = constructListSquare();
    final ImmutableList<Square> squareListNorthWest = constructListSquare(G2, F3, E4, D5, C6, B7, A8);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(H1, bishopRange);
  }

  private static void addA2(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(B3, C4, D5, E6, F7, G8);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(B1);
    final ImmutableList<Square> squareListSouthWest = constructListSquare();
    final ImmutableList<Square> squareListNorthWest = constructListSquare();
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(A2, bishopRange);
  }

  private static void addB2(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(C3, D4, E5, F6, G7, H8);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(C1);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(A1);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(A3);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(B2, bishopRange);
  }

  private static void addC2(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(D3, E4, F5, G6, H7);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(D1);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(B1);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(B3, A4);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(C2, bishopRange);
  }

  private static void addD2(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(E3, F4, G5, H6);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(E1);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(C1);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(C3, B4, A5);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(D2, bishopRange);
  }

  private static void addE2(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(F3, G4, H5);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(F1);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(D1);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(D3, C4, B5, A6);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(E2, bishopRange);
  }

  private static void addF2(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(G3, H4);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(G1);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(E1);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(E3, D4, C5, B6, A7);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(F2, bishopRange);
  }

  private static void addG2(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(H3);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(H1);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(F1);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(F3, E4, D5, C6, B7, A8);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(G2, bishopRange);
  }

  private static void addH2(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare();
    final ImmutableList<Square> squareListSouthEast = constructListSquare();
    final ImmutableList<Square> squareListSouthWest = constructListSquare(G1);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(G3, F4, E5, D6, C7, B8);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(H2, bishopRange);
  }

  private static void addA3(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(B4, C5, D6, E7, F8);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(B2, C1);
    final ImmutableList<Square> squareListSouthWest = constructListSquare();
    final ImmutableList<Square> squareListNorthWest = constructListSquare();
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(A3, bishopRange);
  }

  private static void addB3(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(C4, D5, E6, F7, G8);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(C2, D1);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(A2);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(A4);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(B3, bishopRange);
  }

  private static void addC3(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(D4, E5, F6, G7, H8);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(D2, E1);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(B2, A1);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(B4, A5);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(C3, bishopRange);
  }

  private static void addD3(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(E4, F5, G6, H7);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(E2, F1);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(C2, B1);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(C4, B5, A6);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(D3, bishopRange);
  }

  private static void addE3(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(F4, G5, H6);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(F2, G1);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(D2, C1);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(D4, C5, B6, A7);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(E3, bishopRange);
  }

  private static void addF3(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(G4, H5);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(G2, H1);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(E2, D1);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(E4, D5, C6, B7, A8);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(F3, bishopRange);
  }

  private static void addG3(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(H4);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(H2);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(F2, E1);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(F4, E5, D6, C7, B8);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(G3, bishopRange);
  }

  private static void addH3(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare();
    final ImmutableList<Square> squareListSouthEast = constructListSquare();
    final ImmutableList<Square> squareListSouthWest = constructListSquare(G2, F1);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(G4, F5, E6, D7, C8);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(H3, bishopRange);
  }

  private static void addA4(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(B5, C6, D7, E8);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(B3, C2, D1);
    final ImmutableList<Square> squareListSouthWest = constructListSquare();
    final ImmutableList<Square> squareListNorthWest = constructListSquare();
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(A4, bishopRange);
  }

  private static void addB4(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(C5, D6, E7, F8);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(C3, D2, E1);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(A3);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(A5);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(B4, bishopRange);
  }

  private static void addC4(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(D5, E6, F7, G8);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(D3, E2, F1);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(B3, A2);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(B5, A6);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(C4, bishopRange);
  }

  private static void addD4(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(E5, F6, G7, H8);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(E3, F2, G1);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(C3, B2, A1);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(C5, B6, A7);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(D4, bishopRange);
  }

  private static void addE4(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(F5, G6, H7);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(F3, G2, H1);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(D3, C2, B1);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(D5, C6, B7, A8);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(E4, bishopRange);
  }

  private static void addF4(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(G5, H6);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(G3, H2);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(E3, D2, C1);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(E5, D6, C7, B8);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(F4, bishopRange);
  }

  private static void addG4(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(H5);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(H3);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(F3, E2, D1);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(F5, E6, D7, C8);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(G4, bishopRange);
  }

  private static void addH4(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare();
    final ImmutableList<Square> squareListSouthEast = constructListSquare();
    final ImmutableList<Square> squareListSouthWest = constructListSquare(G3, F2, E1);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(G5, F6, E7, D8);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(H4, bishopRange);
  }

  private static void addA5(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(B6, C7, D8);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(B4, C3, D2, E1);
    final ImmutableList<Square> squareListSouthWest = constructListSquare();
    final ImmutableList<Square> squareListNorthWest = constructListSquare();
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(A5, bishopRange);
  }

  private static void addB5(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(C6, D7, E8);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(C4, D3, E2, F1);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(A4);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(A6);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(B5, bishopRange);
  }

  private static void addC5(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(D6, E7, F8);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(D4, E3, F2, G1);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(B4, A3);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(B6, A7);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(C5, bishopRange);
  }

  private static void addD5(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(E6, F7, G8);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(E4, F3, G2, H1);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(C4, B3, A2);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(C6, B7, A8);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(D5, bishopRange);
  }

  private static void addE5(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(F6, G7, H8);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(F4, G3, H2);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(D4, C3, B2, A1);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(D6, C7, B8);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(E5, bishopRange);
  }

  private static void addF5(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(G6, H7);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(G4, H3);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(E4, D3, C2, B1);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(E6, D7, C8);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(F5, bishopRange);
  }

  private static void addG5(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(H6);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(H4);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(F4, E3, D2, C1);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(F6, E7, D8);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(G5, bishopRange);
  }

  private static void addH5(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare();
    final ImmutableList<Square> squareListSouthEast = constructListSquare();
    final ImmutableList<Square> squareListSouthWest = constructListSquare(G4, F3, E2, D1);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(G6, F7, E8);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(H5, bishopRange);
  }

  private static void addA6(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(B7, C8);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(B5, C4, D3, E2, F1);
    final ImmutableList<Square> squareListSouthWest = constructListSquare();
    final ImmutableList<Square> squareListNorthWest = constructListSquare();
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(A6, bishopRange);
  }

  private static void addB6(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(C7, D8);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(C5, D4, E3, F2, G1);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(A5);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(A7);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(B6, bishopRange);
  }

  private static void addC6(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(D7, E8);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(D5, E4, F3, G2, H1);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(B5, A4);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(B7, A8);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(C6, bishopRange);
  }

  private static void addD6(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(E7, F8);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(E5, F4, G3, H2);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(C5, B4, A3);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(C7, B8);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(D6, bishopRange);
  }

  private static void addE6(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(F7, G8);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(F5, G4, H3);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(D5, C4, B3, A2);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(D7, C8);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(E6, bishopRange);
  }

  private static void addF6(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(G7, H8);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(G5, H4);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(E5, D4, C3, B2, A1);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(E7, D8);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(F6, bishopRange);
  }

  private static void addG6(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(H7);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(H5);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(F5, E4, D3, C2, B1);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(F7, E8);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(G6, bishopRange);
  }

  private static void addH6(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare();
    final ImmutableList<Square> squareListSouthEast = constructListSquare();
    final ImmutableList<Square> squareListSouthWest = constructListSquare(G5, F4, E3, D2, C1);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(G7, F8);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(H6, bishopRange);
  }

  private static void addA7(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(B8);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(B6, C5, D4, E3, F2, G1);
    final ImmutableList<Square> squareListSouthWest = constructListSquare();
    final ImmutableList<Square> squareListNorthWest = constructListSquare();
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(A7, bishopRange);
  }

  private static void addB7(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(C8);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(C6, D5, E4, F3, G2, H1);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(A6);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(A8);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(B7, bishopRange);
  }

  private static void addC7(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(D8);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(D6, E5, F4, G3, H2);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(B6, A5);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(B8);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(C7, bishopRange);
  }

  private static void addD7(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(E8);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(E6, F5, G4, H3);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(C6, B5, A4);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(C8);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(D7, bishopRange);
  }

  private static void addE7(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(F8);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(F6, G5, H4);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(D6, C5, B4, A3);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(D8);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(E7, bishopRange);
  }

  private static void addF7(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(G8);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(G6, H5);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(E6, D5, C4, B3, A2);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(E8);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(F7, bishopRange);
  }

  private static void addG7(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare(H8);
    final ImmutableList<Square> squareListSouthEast = constructListSquare(H6);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(F6, E5, D4, C3, B2, A1);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(F8);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(G7, bishopRange);
  }

  private static void addH7(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare();
    final ImmutableList<Square> squareListSouthEast = constructListSquare();
    final ImmutableList<Square> squareListSouthWest = constructListSquare(G6, F5, E4, D3, C2, B1);
    final ImmutableList<Square> squareListNorthWest = constructListSquare(G8);
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(H7, bishopRange);
  }

  private static void addA8(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare();
    final ImmutableList<Square> squareListSouthEast = constructListSquare(B7, C6, D5, E4, F3, G2, H1);
    final ImmutableList<Square> squareListSouthWest = constructListSquare();
    final ImmutableList<Square> squareListNorthWest = constructListSquare();
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(A8, bishopRange);
  }

  private static void addB8(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare();
    final ImmutableList<Square> squareListSouthEast = constructListSquare(C7, D6, E5, F4, G3, H2);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(A7);
    final ImmutableList<Square> squareListNorthWest = constructListSquare();
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(B8, bishopRange);
  }

  private static void addC8(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare();
    final ImmutableList<Square> squareListSouthEast = constructListSquare(D7, E6, F5, G4, H3);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(B7, A6);
    final ImmutableList<Square> squareListNorthWest = constructListSquare();
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(C8, bishopRange);
  }

  private static void addD8(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare();
    final ImmutableList<Square> squareListSouthEast = constructListSquare(E7, F6, G5, H4);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(C7, B6, A5);
    final ImmutableList<Square> squareListNorthWest = constructListSquare();
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(D8, bishopRange);
  }

  private static void addE8(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare();
    final ImmutableList<Square> squareListSouthEast = constructListSquare(F7, G6, H5);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(D7, C6, B5, A4);
    final ImmutableList<Square> squareListNorthWest = constructListSquare();
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(E8, bishopRange);
  }

  private static void addF8(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare();
    final ImmutableList<Square> squareListSouthEast = constructListSquare(G7, H6);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(E7, D6, C5, B4, A3);
    final ImmutableList<Square> squareListNorthWest = constructListSquare();
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(F8, bishopRange);
  }

  private static void addG8(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare();
    final ImmutableList<Square> squareListSouthEast = constructListSquare(H7);
    final ImmutableList<Square> squareListSouthWest = constructListSquare(F7, E6, D5, C4, B3, A2);
    final ImmutableList<Square> squareListNorthWest = constructListSquare();
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(G8, bishopRange);
  }

  private static void addH8(EnumMap<Square, BishopRange> map) {
    final ImmutableList<Square> squareListNorthEast = constructListSquare();
    final ImmutableList<Square> squareListSouthEast = constructListSquare();
    final ImmutableList<Square> squareListSouthWest = constructListSquare(G7, F6, E5, D4, C3, B2, A1);
    final ImmutableList<Square> squareListNorthWest = constructListSquare();
    final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
        squareListNorthWest);
    map.put(H8, bishopRange);
  }

  private static final ImmutableMap<Square, BishopRange> BISHOP_SQUARES_MAP;

  static {
    final EnumMap<Square, BishopRange> bishopSquaresMap = NonNullWrapperCommon.newEnumMap(Square.class);

    addA1(bishopSquaresMap);
    addB1(bishopSquaresMap);
    addC1(bishopSquaresMap);
    addD1(bishopSquaresMap);
    addE1(bishopSquaresMap);
    addF1(bishopSquaresMap);
    addG1(bishopSquaresMap);
    addH1(bishopSquaresMap);
    addA2(bishopSquaresMap);
    addB2(bishopSquaresMap);
    addC2(bishopSquaresMap);
    addD2(bishopSquaresMap);
    addE2(bishopSquaresMap);
    addF2(bishopSquaresMap);
    addG2(bishopSquaresMap);
    addH2(bishopSquaresMap);
    addA3(bishopSquaresMap);
    addB3(bishopSquaresMap);
    addC3(bishopSquaresMap);
    addD3(bishopSquaresMap);
    addE3(bishopSquaresMap);
    addF3(bishopSquaresMap);
    addG3(bishopSquaresMap);
    addH3(bishopSquaresMap);
    addA4(bishopSquaresMap);
    addB4(bishopSquaresMap);
    addC4(bishopSquaresMap);
    addD4(bishopSquaresMap);
    addE4(bishopSquaresMap);
    addF4(bishopSquaresMap);
    addG4(bishopSquaresMap);
    addH4(bishopSquaresMap);
    addA5(bishopSquaresMap);
    addB5(bishopSquaresMap);
    addC5(bishopSquaresMap);
    addD5(bishopSquaresMap);
    addE5(bishopSquaresMap);
    addF5(bishopSquaresMap);
    addG5(bishopSquaresMap);
    addH5(bishopSquaresMap);
    addA6(bishopSquaresMap);
    addB6(bishopSquaresMap);
    addC6(bishopSquaresMap);
    addD6(bishopSquaresMap);
    addE6(bishopSquaresMap);
    addF6(bishopSquaresMap);
    addG6(bishopSquaresMap);
    addH6(bishopSquaresMap);
    addA7(bishopSquaresMap);
    addB7(bishopSquaresMap);
    addC7(bishopSquaresMap);
    addD7(bishopSquaresMap);
    addE7(bishopSquaresMap);
    addF7(bishopSquaresMap);
    addG7(bishopSquaresMap);
    addH7(bishopSquaresMap);
    addA8(bishopSquaresMap);
    addB8(bishopSquaresMap);
    addC8(bishopSquaresMap);
    addD8(bishopSquaresMap);
    addE8(bishopSquaresMap);
    addF8(bishopSquaresMap);
    addG8(bishopSquaresMap);
    addH8(bishopSquaresMap);

    BISHOP_SQUARES_MAP = NonNullWrapperCommon.copyOfMap(bishopSquaresMap);

    ValidateMoveNumberUtility.validateDiagonalMovesNumber(BISHOP_SQUARES_MAP, 560);

  }

  // END generated code

  public static BishopRange getBishopSquares(Square fromSquare) {
    return NonNullWrapperCommon.get(BISHOP_SQUARES_MAP, fromSquare);
  }

}
