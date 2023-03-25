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

public class KingNonCastlingEmptyBoardSquares extends AbstractEmptyBoardSquares implements EnumConstants {

  // BEGIN generated code

  private static void addA1(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B1, A2, B2);
    map.put(A1, squareSet);
  }

  private static void addB1(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(A1, C1, A2, B2, C2);
    map.put(B1, squareSet);
  }

  private static void addC1(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B1, D1, B2, C2, D2);
    map.put(C1, squareSet);
  }

  private static void addD1(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(C1, E1, C2, D2, E2);
    map.put(D1, squareSet);
  }

  private static void addE1(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(D1, F1, D2, E2, F2);
    map.put(E1, squareSet);
  }

  private static void addF1(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(E1, G1, E2, F2, G2);
    map.put(F1, squareSet);
  }

  private static void addG1(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(F1, H1, F2, G2, H2);
    map.put(G1, squareSet);
  }

  private static void addH1(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(G1, G2, H2);
    map.put(H1, squareSet);
  }

  private static void addA2(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(A1, B1, B2, A3, B3);
    map.put(A2, squareSet);
  }

  private static void addB2(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(A1, B1, C1, A2, C2, A3, B3, C3);
    map.put(B2, squareSet);
  }

  private static void addC2(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B1, C1, D1, B2, D2, B3, C3, D3);
    map.put(C2, squareSet);
  }

  private static void addD2(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(C1, D1, E1, C2, E2, C3, D3, E3);
    map.put(D2, squareSet);
  }

  private static void addE2(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(D1, E1, F1, D2, F2, D3, E3, F3);
    map.put(E2, squareSet);
  }

  private static void addF2(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(E1, F1, G1, E2, G2, E3, F3, G3);
    map.put(F2, squareSet);
  }

  private static void addG2(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(F1, G1, H1, F2, H2, F3, G3, H3);
    map.put(G2, squareSet);
  }

  private static void addH2(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(G1, H1, G2, G3, H3);
    map.put(H2, squareSet);
  }

  private static void addA3(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(A2, B2, B3, A4, B4);
    map.put(A3, squareSet);
  }

  private static void addB3(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(A2, B2, C2, A3, C3, A4, B4, C4);
    map.put(B3, squareSet);
  }

  private static void addC3(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B2, C2, D2, B3, D3, B4, C4, D4);
    map.put(C3, squareSet);
  }

  private static void addD3(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(C2, D2, E2, C3, E3, C4, D4, E4);
    map.put(D3, squareSet);
  }

  private static void addE3(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(D2, E2, F2, D3, F3, D4, E4, F4);
    map.put(E3, squareSet);
  }

  private static void addF3(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(E2, F2, G2, E3, G3, E4, F4, G4);
    map.put(F3, squareSet);
  }

  private static void addG3(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(F2, G2, H2, F3, H3, F4, G4, H4);
    map.put(G3, squareSet);
  }

  private static void addH3(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(G2, H2, G3, G4, H4);
    map.put(H3, squareSet);
  }

  private static void addA4(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(A3, B3, B4, A5, B5);
    map.put(A4, squareSet);
  }

  private static void addB4(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(A3, B3, C3, A4, C4, A5, B5, C5);
    map.put(B4, squareSet);
  }

  private static void addC4(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B3, C3, D3, B4, D4, B5, C5, D5);
    map.put(C4, squareSet);
  }

  private static void addD4(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(C3, D3, E3, C4, E4, C5, D5, E5);
    map.put(D4, squareSet);
  }

  private static void addE4(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(D3, E3, F3, D4, F4, D5, E5, F5);
    map.put(E4, squareSet);
  }

  private static void addF4(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(E3, F3, G3, E4, G4, E5, F5, G5);
    map.put(F4, squareSet);
  }

  private static void addG4(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(F3, G3, H3, F4, H4, F5, G5, H5);
    map.put(G4, squareSet);
  }

  private static void addH4(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(G3, H3, G4, G5, H5);
    map.put(H4, squareSet);
  }

  private static void addA5(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(A4, B4, B5, A6, B6);
    map.put(A5, squareSet);
  }

  private static void addB5(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(A4, B4, C4, A5, C5, A6, B6, C6);
    map.put(B5, squareSet);
  }

  private static void addC5(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B4, C4, D4, B5, D5, B6, C6, D6);
    map.put(C5, squareSet);
  }

  private static void addD5(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(C4, D4, E4, C5, E5, C6, D6, E6);
    map.put(D5, squareSet);
  }

  private static void addE5(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(D4, E4, F4, D5, F5, D6, E6, F6);
    map.put(E5, squareSet);
  }

  private static void addF5(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(E4, F4, G4, E5, G5, E6, F6, G6);
    map.put(F5, squareSet);
  }

  private static void addG5(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(F4, G4, H4, F5, H5, F6, G6, H6);
    map.put(G5, squareSet);
  }

  private static void addH5(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(G4, H4, G5, G6, H6);
    map.put(H5, squareSet);
  }

  private static void addA6(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(A5, B5, B6, A7, B7);
    map.put(A6, squareSet);
  }

  private static void addB6(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(A5, B5, C5, A6, C6, A7, B7, C7);
    map.put(B6, squareSet);
  }

  private static void addC6(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B5, C5, D5, B6, D6, B7, C7, D7);
    map.put(C6, squareSet);
  }

  private static void addD6(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(C5, D5, E5, C6, E6, C7, D7, E7);
    map.put(D6, squareSet);
  }

  private static void addE6(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(D5, E5, F5, D6, F6, D7, E7, F7);
    map.put(E6, squareSet);
  }

  private static void addF6(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(E5, F5, G5, E6, G6, E7, F7, G7);
    map.put(F6, squareSet);
  }

  private static void addG6(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(F5, G5, H5, F6, H6, F7, G7, H7);
    map.put(G6, squareSet);
  }

  private static void addH6(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(G5, H5, G6, G7, H7);
    map.put(H6, squareSet);
  }

  private static void addA7(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(A6, B6, B7, A8, B8);
    map.put(A7, squareSet);
  }

  private static void addB7(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(A6, B6, C6, A7, C7, A8, B8, C8);
    map.put(B7, squareSet);
  }

  private static void addC7(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B6, C6, D6, B7, D7, B8, C8, D8);
    map.put(C7, squareSet);
  }

  private static void addD7(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(C6, D6, E6, C7, E7, C8, D8, E8);
    map.put(D7, squareSet);
  }

  private static void addE7(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(D6, E6, F6, D7, F7, D8, E8, F8);
    map.put(E7, squareSet);
  }

  private static void addF7(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(E6, F6, G6, E7, G7, E8, F8, G8);
    map.put(F7, squareSet);
  }

  private static void addG7(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(F6, G6, H6, F7, H7, F8, G8, H8);
    map.put(G7, squareSet);
  }

  private static void addH7(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(G6, H6, G7, G8, H8);
    map.put(H7, squareSet);
  }

  private static void addA8(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(A7, B7, B8);
    map.put(A8, squareSet);
  }

  private static void addB8(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(A7, B7, C7, A8, C8);
    map.put(B8, squareSet);
  }

  private static void addC8(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B7, C7, D7, B8, D8);
    map.put(C8, squareSet);
  }

  private static void addD8(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(C7, D7, E7, C8, E8);
    map.put(D8, squareSet);
  }

  private static void addE8(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(D7, E7, F7, D8, F8);
    map.put(E8, squareSet);
  }

  private static void addF8(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(E7, F7, G7, E8, G8);
    map.put(F8, squareSet);
  }

  private static void addG8(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(F7, G7, H7, F8, H8);
    map.put(G8, squareSet);
  }

  private static void addH8(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(G7, H7, G8);
    map.put(H8, squareSet);
  }

  private static final ImmutableMap<Square, ImmutableSet<Square>> KING_SQUARES_MAP;

  static {
    final EnumMap<Square, ImmutableSet<Square>> kingSquaresMap = NonNullWrapperCommon.newEnumMap(Square.class);
    addA1(kingSquaresMap);
    addB1(kingSquaresMap);
    addC1(kingSquaresMap);
    addD1(kingSquaresMap);
    addE1(kingSquaresMap);
    addF1(kingSquaresMap);
    addG1(kingSquaresMap);
    addH1(kingSquaresMap);
    addA2(kingSquaresMap);
    addB2(kingSquaresMap);
    addC2(kingSquaresMap);
    addD2(kingSquaresMap);
    addE2(kingSquaresMap);
    addF2(kingSquaresMap);
    addG2(kingSquaresMap);
    addH2(kingSquaresMap);
    addA3(kingSquaresMap);
    addB3(kingSquaresMap);
    addC3(kingSquaresMap);
    addD3(kingSquaresMap);
    addE3(kingSquaresMap);
    addF3(kingSquaresMap);
    addG3(kingSquaresMap);
    addH3(kingSquaresMap);
    addA4(kingSquaresMap);
    addB4(kingSquaresMap);
    addC4(kingSquaresMap);
    addD4(kingSquaresMap);
    addE4(kingSquaresMap);
    addF4(kingSquaresMap);
    addG4(kingSquaresMap);
    addH4(kingSquaresMap);
    addA5(kingSquaresMap);
    addB5(kingSquaresMap);
    addC5(kingSquaresMap);
    addD5(kingSquaresMap);
    addE5(kingSquaresMap);
    addF5(kingSquaresMap);
    addG5(kingSquaresMap);
    addH5(kingSquaresMap);
    addA6(kingSquaresMap);
    addB6(kingSquaresMap);
    addC6(kingSquaresMap);
    addD6(kingSquaresMap);
    addE6(kingSquaresMap);
    addF6(kingSquaresMap);
    addG6(kingSquaresMap);
    addH6(kingSquaresMap);
    addA7(kingSquaresMap);
    addB7(kingSquaresMap);
    addC7(kingSquaresMap);
    addD7(kingSquaresMap);
    addE7(kingSquaresMap);
    addF7(kingSquaresMap);
    addG7(kingSquaresMap);
    addH7(kingSquaresMap);
    addA8(kingSquaresMap);
    addB8(kingSquaresMap);
    addC8(kingSquaresMap);
    addD8(kingSquaresMap);
    addE8(kingSquaresMap);
    addF8(kingSquaresMap);
    addG8(kingSquaresMap);
    addH8(kingSquaresMap);

    KING_SQUARES_MAP = NonNullWrapperCommon.copyOfMap(kingSquaresMap);

    ValidateMoveNumberUtility.validateMapOfSet(KING_SQUARES_MAP, 420);

  }

  // END generated code

  static {
    ValidateMoveNumberUtility.validateMapOfSet(KING_SQUARES_MAP, 420);
  }

  public static Set<Square> getKingSquares(Square fromSquare) {
    return NonNullWrapperCommon.get(KING_SQUARES_MAP, fromSquare);
  }

}
