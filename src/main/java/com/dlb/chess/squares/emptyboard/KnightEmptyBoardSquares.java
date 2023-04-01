package com.dlb.chess.squares.emptyboard;

import static com.dlb.chess.common.utility.ImmutableUtility.constructSet;

import java.util.EnumMap;
import java.util.Set;

import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.utility.ValidateMoveNumberUtility;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

public class KnightEmptyBoardSquares extends AbstractEmptyBoardSquares implements EnumConstants {

  // BEGIN generated code

  private static void addA1(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(C2, B3);
    map.put(A1, squareSet);
  }

  private static void addB1(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(D2, A3, C3);
    map.put(B1, squareSet);
  }

  private static void addC1(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(A2, E2, B3, D3);
    map.put(C1, squareSet);
  }

  private static void addD1(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B2, F2, C3, E3);
    map.put(D1, squareSet);
  }

  private static void addE1(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(C2, G2, D3, F3);
    map.put(E1, squareSet);
  }

  private static void addF1(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(D2, H2, E3, G3);
    map.put(F1, squareSet);
  }

  private static void addG1(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(E2, F3, H3);
    map.put(G1, squareSet);
  }

  private static void addH1(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(F2, G3);
    map.put(H1, squareSet);
  }

  private static void addA2(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(C1, C3, B4);
    map.put(A2, squareSet);
  }

  private static void addB2(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(D1, D3, A4, C4);
    map.put(B2, squareSet);
  }

  private static void addC2(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(A1, E1, A3, E3, B4, D4);
    map.put(C2, squareSet);
  }

  private static void addD2(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B1, F1, B3, F3, C4, E4);
    map.put(D2, squareSet);
  }

  private static void addE2(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(C1, G1, C3, G3, D4, F4);
    map.put(E2, squareSet);
  }

  private static void addF2(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(D1, H1, D3, H3, E4, G4);
    map.put(F2, squareSet);
  }

  private static void addG2(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(E1, E3, F4, H4);
    map.put(G2, squareSet);
  }

  private static void addH2(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(F1, F3, G4);
    map.put(H2, squareSet);
  }

  private static void addA3(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B1, C2, C4, B5);
    map.put(A3, squareSet);
  }

  private static void addB3(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(A1, C1, D2, D4, A5, C5);
    map.put(B3, squareSet);
  }

  private static void addC3(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B1, D1, A2, E2, A4, E4, B5, D5);
    map.put(C3, squareSet);
  }

  private static void addD3(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(C1, E1, B2, F2, B4, F4, C5, E5);
    map.put(D3, squareSet);
  }

  private static void addE3(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(D1, F1, C2, G2, C4, G4, D5, F5);
    map.put(E3, squareSet);
  }

  private static void addF3(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(E1, G1, D2, H2, D4, H4, E5, G5);
    map.put(F3, squareSet);
  }

  private static void addG3(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(F1, H1, E2, E4, F5, H5);
    map.put(G3, squareSet);
  }

  private static void addH3(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(G1, F2, F4, G5);
    map.put(H3, squareSet);
  }

  private static void addA4(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B2, C3, C5, B6);
    map.put(A4, squareSet);
  }

  private static void addB4(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(A2, C2, D3, D5, A6, C6);
    map.put(B4, squareSet);
  }

  private static void addC4(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B2, D2, A3, E3, A5, E5, B6, D6);
    map.put(C4, squareSet);
  }

  private static void addD4(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(C2, E2, B3, F3, B5, F5, C6, E6);
    map.put(D4, squareSet);
  }

  private static void addE4(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(D2, F2, C3, G3, C5, G5, D6, F6);
    map.put(E4, squareSet);
  }

  private static void addF4(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(E2, G2, D3, H3, D5, H5, E6, G6);
    map.put(F4, squareSet);
  }

  private static void addG4(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(F2, H2, E3, E5, F6, H6);
    map.put(G4, squareSet);
  }

  private static void addH4(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(G2, F3, F5, G6);
    map.put(H4, squareSet);
  }

  private static void addA5(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B3, C4, C6, B7);
    map.put(A5, squareSet);
  }

  private static void addB5(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(A3, C3, D4, D6, A7, C7);
    map.put(B5, squareSet);
  }

  private static void addC5(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B3, D3, A4, E4, A6, E6, B7, D7);
    map.put(C5, squareSet);
  }

  private static void addD5(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(C3, E3, B4, F4, B6, F6, C7, E7);
    map.put(D5, squareSet);
  }

  private static void addE5(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(D3, F3, C4, G4, C6, G6, D7, F7);
    map.put(E5, squareSet);
  }

  private static void addF5(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(E3, G3, D4, H4, D6, H6, E7, G7);
    map.put(F5, squareSet);
  }

  private static void addG5(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(F3, H3, E4, E6, F7, H7);
    map.put(G5, squareSet);
  }

  private static void addH5(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(G3, F4, F6, G7);
    map.put(H5, squareSet);
  }

  private static void addA6(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B4, C5, C7, B8);
    map.put(A6, squareSet);
  }

  private static void addB6(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(A4, C4, D5, D7, A8, C8);
    map.put(B6, squareSet);
  }

  private static void addC6(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B4, D4, A5, E5, A7, E7, B8, D8);
    map.put(C6, squareSet);
  }

  private static void addD6(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(C4, E4, B5, F5, B7, F7, C8, E8);
    map.put(D6, squareSet);
  }

  private static void addE6(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(D4, F4, C5, G5, C7, G7, D8, F8);
    map.put(E6, squareSet);
  }

  private static void addF6(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(E4, G4, D5, H5, D7, H7, E8, G8);
    map.put(F6, squareSet);
  }

  private static void addG6(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(F4, H4, E5, E7, F8, H8);
    map.put(G6, squareSet);
  }

  private static void addH6(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(G4, F5, F7, G8);
    map.put(H6, squareSet);
  }

  private static void addA7(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B5, C6, C8);
    map.put(A7, squareSet);
  }

  private static void addB7(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(A5, C5, D6, D8);
    map.put(B7, squareSet);
  }

  private static void addC7(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B5, D5, A6, E6, A8, E8);
    map.put(C7, squareSet);
  }

  private static void addD7(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(C5, E5, B6, F6, B8, F8);
    map.put(D7, squareSet);
  }

  private static void addE7(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(D5, F5, C6, G6, C8, G8);
    map.put(E7, squareSet);
  }

  private static void addF7(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(E5, G5, D6, H6, D8, H8);
    map.put(F7, squareSet);
  }

  private static void addG7(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(F5, H5, E6, E8);
    map.put(G7, squareSet);
  }

  private static void addH7(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(G5, F6, F8);
    map.put(H7, squareSet);
  }

  private static void addA8(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B6, C7);
    map.put(A8, squareSet);
  }

  private static void addB8(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(A6, C6, D7);
    map.put(B8, squareSet);
  }

  private static void addC8(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B6, D6, A7, E7);
    map.put(C8, squareSet);
  }

  private static void addD8(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(C6, E6, B7, F7);
    map.put(D8, squareSet);
  }

  private static void addE8(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(D6, F6, C7, G7);
    map.put(E8, squareSet);
  }

  private static void addF8(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(E6, G6, D7, H7);
    map.put(F8, squareSet);
  }

  private static void addG8(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(F6, H6, E7);
    map.put(G8, squareSet);
  }

  private static void addH8(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(G6, F7);
    map.put(H8, squareSet);
  }

  private static final ImmutableMap<Square, ImmutableSet<Square>> KNIGHT_SQUARES_MAP;

  static {
    final EnumMap<Square, ImmutableSet<Square>> knightSquaresMap = NonNullWrapperCommon.newEnumMap(Square.class);
    addA1(knightSquaresMap);
    addB1(knightSquaresMap);
    addC1(knightSquaresMap);
    addD1(knightSquaresMap);
    addE1(knightSquaresMap);
    addF1(knightSquaresMap);
    addG1(knightSquaresMap);
    addH1(knightSquaresMap);
    addA2(knightSquaresMap);
    addB2(knightSquaresMap);
    addC2(knightSquaresMap);
    addD2(knightSquaresMap);
    addE2(knightSquaresMap);
    addF2(knightSquaresMap);
    addG2(knightSquaresMap);
    addH2(knightSquaresMap);
    addA3(knightSquaresMap);
    addB3(knightSquaresMap);
    addC3(knightSquaresMap);
    addD3(knightSquaresMap);
    addE3(knightSquaresMap);
    addF3(knightSquaresMap);
    addG3(knightSquaresMap);
    addH3(knightSquaresMap);
    addA4(knightSquaresMap);
    addB4(knightSquaresMap);
    addC4(knightSquaresMap);
    addD4(knightSquaresMap);
    addE4(knightSquaresMap);
    addF4(knightSquaresMap);
    addG4(knightSquaresMap);
    addH4(knightSquaresMap);
    addA5(knightSquaresMap);
    addB5(knightSquaresMap);
    addC5(knightSquaresMap);
    addD5(knightSquaresMap);
    addE5(knightSquaresMap);
    addF5(knightSquaresMap);
    addG5(knightSquaresMap);
    addH5(knightSquaresMap);
    addA6(knightSquaresMap);
    addB6(knightSquaresMap);
    addC6(knightSquaresMap);
    addD6(knightSquaresMap);
    addE6(knightSquaresMap);
    addF6(knightSquaresMap);
    addG6(knightSquaresMap);
    addH6(knightSquaresMap);
    addA7(knightSquaresMap);
    addB7(knightSquaresMap);
    addC7(knightSquaresMap);
    addD7(knightSquaresMap);
    addE7(knightSquaresMap);
    addF7(knightSquaresMap);
    addG7(knightSquaresMap);
    addH7(knightSquaresMap);
    addA8(knightSquaresMap);
    addB8(knightSquaresMap);
    addC8(knightSquaresMap);
    addD8(knightSquaresMap);
    addE8(knightSquaresMap);
    addF8(knightSquaresMap);
    addG8(knightSquaresMap);
    addH8(knightSquaresMap);

    KNIGHT_SQUARES_MAP = NonNullWrapperCommon.copyOfMap(knightSquaresMap);

    ValidateMoveNumberUtility.validateMapOfSet(KNIGHT_SQUARES_MAP, 336);

  }

  // END generated code

  public static Set<Square> getKnightSquares(Square fromSquare) {
    return NonNullWrapperCommon.get(KNIGHT_SQUARES_MAP, fromSquare);
  }

}
