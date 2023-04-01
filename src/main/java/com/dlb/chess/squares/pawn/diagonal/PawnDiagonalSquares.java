package com.dlb.chess.squares.pawn.diagonal;

import static com.dlb.chess.common.utility.ImmutableUtility.constructSet;

import java.util.EnumMap;
import java.util.Set;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.utility.ImmutableUtility;
import com.dlb.chess.utility.ValidateMoveNumberUtility;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

public class PawnDiagonalSquares implements EnumConstants {

  // BEGIN generated code

  private static void addWhiteA1(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = ImmutableUtility.EMPTY_UNMODIFIABLE_SET;
    map.put(A1, squareSet);
  }

  private static void addWhiteB1(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = ImmutableUtility.EMPTY_UNMODIFIABLE_SET;
    map.put(B1, squareSet);
  }

  private static void addWhiteC1(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = ImmutableUtility.EMPTY_UNMODIFIABLE_SET;
    map.put(C1, squareSet);
  }

  private static void addWhiteD1(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = ImmutableUtility.EMPTY_UNMODIFIABLE_SET;
    map.put(D1, squareSet);
  }

  private static void addWhiteE1(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = ImmutableUtility.EMPTY_UNMODIFIABLE_SET;
    map.put(E1, squareSet);
  }

  private static void addWhiteF1(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = ImmutableUtility.EMPTY_UNMODIFIABLE_SET;
    map.put(F1, squareSet);
  }

  private static void addWhiteG1(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = ImmutableUtility.EMPTY_UNMODIFIABLE_SET;
    map.put(G1, squareSet);
  }

  private static void addWhiteH1(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = ImmutableUtility.EMPTY_UNMODIFIABLE_SET;
    map.put(H1, squareSet);
  }

  private static void addWhiteA2(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B3);
    map.put(A2, squareSet);
  }

  private static void addWhiteB2(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(A3, C3);
    map.put(B2, squareSet);
  }

  private static void addWhiteC2(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B3, D3);
    map.put(C2, squareSet);
  }

  private static void addWhiteD2(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(C3, E3);
    map.put(D2, squareSet);
  }

  private static void addWhiteE2(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(D3, F3);
    map.put(E2, squareSet);
  }

  private static void addWhiteF2(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(E3, G3);
    map.put(F2, squareSet);
  }

  private static void addWhiteG2(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(F3, H3);
    map.put(G2, squareSet);
  }

  private static void addWhiteH2(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(G3);
    map.put(H2, squareSet);
  }

  private static void addWhiteA3(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B4);
    map.put(A3, squareSet);
  }

  private static void addWhiteB3(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(A4, C4);
    map.put(B3, squareSet);
  }

  private static void addWhiteC3(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B4, D4);
    map.put(C3, squareSet);
  }

  private static void addWhiteD3(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(C4, E4);
    map.put(D3, squareSet);
  }

  private static void addWhiteE3(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(D4, F4);
    map.put(E3, squareSet);
  }

  private static void addWhiteF3(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(E4, G4);
    map.put(F3, squareSet);
  }

  private static void addWhiteG3(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(F4, H4);
    map.put(G3, squareSet);
  }

  private static void addWhiteH3(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(G4);
    map.put(H3, squareSet);
  }

  private static void addWhiteA4(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B5);
    map.put(A4, squareSet);
  }

  private static void addWhiteB4(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(A5, C5);
    map.put(B4, squareSet);
  }

  private static void addWhiteC4(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B5, D5);
    map.put(C4, squareSet);
  }

  private static void addWhiteD4(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(C5, E5);
    map.put(D4, squareSet);
  }

  private static void addWhiteE4(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(D5, F5);
    map.put(E4, squareSet);
  }

  private static void addWhiteF4(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(E5, G5);
    map.put(F4, squareSet);
  }

  private static void addWhiteG4(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(F5, H5);
    map.put(G4, squareSet);
  }

  private static void addWhiteH4(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(G5);
    map.put(H4, squareSet);
  }

  private static void addWhiteA5(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B6);
    map.put(A5, squareSet);
  }

  private static void addWhiteB5(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(A6, C6);
    map.put(B5, squareSet);
  }

  private static void addWhiteC5(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B6, D6);
    map.put(C5, squareSet);
  }

  private static void addWhiteD5(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(C6, E6);
    map.put(D5, squareSet);
  }

  private static void addWhiteE5(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(D6, F6);
    map.put(E5, squareSet);
  }

  private static void addWhiteF5(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(E6, G6);
    map.put(F5, squareSet);
  }

  private static void addWhiteG5(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(F6, H6);
    map.put(G5, squareSet);
  }

  private static void addWhiteH5(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(G6);
    map.put(H5, squareSet);
  }

  private static void addWhiteA6(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B7);
    map.put(A6, squareSet);
  }

  private static void addWhiteB6(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(A7, C7);
    map.put(B6, squareSet);
  }

  private static void addWhiteC6(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B7, D7);
    map.put(C6, squareSet);
  }

  private static void addWhiteD6(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(C7, E7);
    map.put(D6, squareSet);
  }

  private static void addWhiteE6(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(D7, F7);
    map.put(E6, squareSet);
  }

  private static void addWhiteF6(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(E7, G7);
    map.put(F6, squareSet);
  }

  private static void addWhiteG6(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(F7, H7);
    map.put(G6, squareSet);
  }

  private static void addWhiteH6(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(G7);
    map.put(H6, squareSet);
  }

  private static void addWhiteA7(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B8);
    map.put(A7, squareSet);
  }

  private static void addWhiteB7(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(A8, C8);
    map.put(B7, squareSet);
  }

  private static void addWhiteC7(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B8, D8);
    map.put(C7, squareSet);
  }

  private static void addWhiteD7(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(C8, E8);
    map.put(D7, squareSet);
  }

  private static void addWhiteE7(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(D8, F8);
    map.put(E7, squareSet);
  }

  private static void addWhiteF7(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(E8, G8);
    map.put(F7, squareSet);
  }

  private static void addWhiteG7(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(F8, H8);
    map.put(G7, squareSet);
  }

  private static void addWhiteH7(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(G8);
    map.put(H7, squareSet);
  }

  private static void addWhiteA8(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = ImmutableUtility.EMPTY_UNMODIFIABLE_SET;
    map.put(A8, squareSet);
  }

  private static void addWhiteB8(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = ImmutableUtility.EMPTY_UNMODIFIABLE_SET;
    map.put(B8, squareSet);
  }

  private static void addWhiteC8(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = ImmutableUtility.EMPTY_UNMODIFIABLE_SET;
    map.put(C8, squareSet);
  }

  private static void addWhiteD8(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = ImmutableUtility.EMPTY_UNMODIFIABLE_SET;
    map.put(D8, squareSet);
  }

  private static void addWhiteE8(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = ImmutableUtility.EMPTY_UNMODIFIABLE_SET;
    map.put(E8, squareSet);
  }

  private static void addWhiteF8(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = ImmutableUtility.EMPTY_UNMODIFIABLE_SET;
    map.put(F8, squareSet);
  }

  private static void addWhiteG8(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = ImmutableUtility.EMPTY_UNMODIFIABLE_SET;
    map.put(G8, squareSet);
  }

  private static void addWhiteH8(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = ImmutableUtility.EMPTY_UNMODIFIABLE_SET;
    map.put(H8, squareSet);
  }

  private static final ImmutableMap<Square, ImmutableSet<Square>> PAWN_WHITE_SQUARES_MAP;

  static {
    final EnumMap<Square, ImmutableSet<Square>> pawnWhiteSquaresMap = NonNullWrapperCommon.newEnumMap(Square.class);
    addWhiteA1(pawnWhiteSquaresMap);
    addWhiteB1(pawnWhiteSquaresMap);
    addWhiteC1(pawnWhiteSquaresMap);
    addWhiteD1(pawnWhiteSquaresMap);
    addWhiteE1(pawnWhiteSquaresMap);
    addWhiteF1(pawnWhiteSquaresMap);
    addWhiteG1(pawnWhiteSquaresMap);
    addWhiteH1(pawnWhiteSquaresMap);
    addWhiteA2(pawnWhiteSquaresMap);
    addWhiteB2(pawnWhiteSquaresMap);
    addWhiteC2(pawnWhiteSquaresMap);
    addWhiteD2(pawnWhiteSquaresMap);
    addWhiteE2(pawnWhiteSquaresMap);
    addWhiteF2(pawnWhiteSquaresMap);
    addWhiteG2(pawnWhiteSquaresMap);
    addWhiteH2(pawnWhiteSquaresMap);
    addWhiteA3(pawnWhiteSquaresMap);
    addWhiteB3(pawnWhiteSquaresMap);
    addWhiteC3(pawnWhiteSquaresMap);
    addWhiteD3(pawnWhiteSquaresMap);
    addWhiteE3(pawnWhiteSquaresMap);
    addWhiteF3(pawnWhiteSquaresMap);
    addWhiteG3(pawnWhiteSquaresMap);
    addWhiteH3(pawnWhiteSquaresMap);
    addWhiteA4(pawnWhiteSquaresMap);
    addWhiteB4(pawnWhiteSquaresMap);
    addWhiteC4(pawnWhiteSquaresMap);
    addWhiteD4(pawnWhiteSquaresMap);
    addWhiteE4(pawnWhiteSquaresMap);
    addWhiteF4(pawnWhiteSquaresMap);
    addWhiteG4(pawnWhiteSquaresMap);
    addWhiteH4(pawnWhiteSquaresMap);
    addWhiteA5(pawnWhiteSquaresMap);
    addWhiteB5(pawnWhiteSquaresMap);
    addWhiteC5(pawnWhiteSquaresMap);
    addWhiteD5(pawnWhiteSquaresMap);
    addWhiteE5(pawnWhiteSquaresMap);
    addWhiteF5(pawnWhiteSquaresMap);
    addWhiteG5(pawnWhiteSquaresMap);
    addWhiteH5(pawnWhiteSquaresMap);
    addWhiteA6(pawnWhiteSquaresMap);
    addWhiteB6(pawnWhiteSquaresMap);
    addWhiteC6(pawnWhiteSquaresMap);
    addWhiteD6(pawnWhiteSquaresMap);
    addWhiteE6(pawnWhiteSquaresMap);
    addWhiteF6(pawnWhiteSquaresMap);
    addWhiteG6(pawnWhiteSquaresMap);
    addWhiteH6(pawnWhiteSquaresMap);
    addWhiteA7(pawnWhiteSquaresMap);
    addWhiteB7(pawnWhiteSquaresMap);
    addWhiteC7(pawnWhiteSquaresMap);
    addWhiteD7(pawnWhiteSquaresMap);
    addWhiteE7(pawnWhiteSquaresMap);
    addWhiteF7(pawnWhiteSquaresMap);
    addWhiteG7(pawnWhiteSquaresMap);
    addWhiteH7(pawnWhiteSquaresMap);
    addWhiteA8(pawnWhiteSquaresMap);
    addWhiteB8(pawnWhiteSquaresMap);
    addWhiteC8(pawnWhiteSquaresMap);
    addWhiteD8(pawnWhiteSquaresMap);
    addWhiteE8(pawnWhiteSquaresMap);
    addWhiteF8(pawnWhiteSquaresMap);
    addWhiteG8(pawnWhiteSquaresMap);
    addWhiteH8(pawnWhiteSquaresMap);

    PAWN_WHITE_SQUARES_MAP = NonNullWrapperCommon.copyOfMap(pawnWhiteSquaresMap);

    ValidateMoveNumberUtility.validateMapOfSet(PAWN_WHITE_SQUARES_MAP, 84);

  }

  // END generated code

  // BEGIN generated code

  private static void addBlackA1(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = ImmutableUtility.EMPTY_UNMODIFIABLE_SET;
    map.put(A1, squareSet);
  }

  private static void addBlackB1(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = ImmutableUtility.EMPTY_UNMODIFIABLE_SET;
    map.put(B1, squareSet);
  }

  private static void addBlackC1(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = ImmutableUtility.EMPTY_UNMODIFIABLE_SET;
    map.put(C1, squareSet);
  }

  private static void addBlackD1(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = ImmutableUtility.EMPTY_UNMODIFIABLE_SET;
    map.put(D1, squareSet);
  }

  private static void addBlackE1(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = ImmutableUtility.EMPTY_UNMODIFIABLE_SET;
    map.put(E1, squareSet);
  }

  private static void addBlackF1(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = ImmutableUtility.EMPTY_UNMODIFIABLE_SET;
    map.put(F1, squareSet);
  }

  private static void addBlackG1(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = ImmutableUtility.EMPTY_UNMODIFIABLE_SET;
    map.put(G1, squareSet);
  }

  private static void addBlackH1(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = ImmutableUtility.EMPTY_UNMODIFIABLE_SET;
    map.put(H1, squareSet);
  }

  private static void addBlackA2(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B1);
    map.put(A2, squareSet);
  }

  private static void addBlackB2(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(A1, C1);
    map.put(B2, squareSet);
  }

  private static void addBlackC2(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B1, D1);
    map.put(C2, squareSet);
  }

  private static void addBlackD2(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(C1, E1);
    map.put(D2, squareSet);
  }

  private static void addBlackE2(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(D1, F1);
    map.put(E2, squareSet);
  }

  private static void addBlackF2(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(E1, G1);
    map.put(F2, squareSet);
  }

  private static void addBlackG2(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(F1, H1);
    map.put(G2, squareSet);
  }

  private static void addBlackH2(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(G1);
    map.put(H2, squareSet);
  }

  private static void addBlackA3(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B2);
    map.put(A3, squareSet);
  }

  private static void addBlackB3(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(A2, C2);
    map.put(B3, squareSet);
  }

  private static void addBlackC3(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B2, D2);
    map.put(C3, squareSet);
  }

  private static void addBlackD3(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(C2, E2);
    map.put(D3, squareSet);
  }

  private static void addBlackE3(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(D2, F2);
    map.put(E3, squareSet);
  }

  private static void addBlackF3(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(E2, G2);
    map.put(F3, squareSet);
  }

  private static void addBlackG3(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(F2, H2);
    map.put(G3, squareSet);
  }

  private static void addBlackH3(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(G2);
    map.put(H3, squareSet);
  }

  private static void addBlackA4(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B3);
    map.put(A4, squareSet);
  }

  private static void addBlackB4(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(A3, C3);
    map.put(B4, squareSet);
  }

  private static void addBlackC4(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B3, D3);
    map.put(C4, squareSet);
  }

  private static void addBlackD4(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(C3, E3);
    map.put(D4, squareSet);
  }

  private static void addBlackE4(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(D3, F3);
    map.put(E4, squareSet);
  }

  private static void addBlackF4(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(E3, G3);
    map.put(F4, squareSet);
  }

  private static void addBlackG4(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(F3, H3);
    map.put(G4, squareSet);
  }

  private static void addBlackH4(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(G3);
    map.put(H4, squareSet);
  }

  private static void addBlackA5(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B4);
    map.put(A5, squareSet);
  }

  private static void addBlackB5(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(A4, C4);
    map.put(B5, squareSet);
  }

  private static void addBlackC5(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B4, D4);
    map.put(C5, squareSet);
  }

  private static void addBlackD5(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(C4, E4);
    map.put(D5, squareSet);
  }

  private static void addBlackE5(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(D4, F4);
    map.put(E5, squareSet);
  }

  private static void addBlackF5(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(E4, G4);
    map.put(F5, squareSet);
  }

  private static void addBlackG5(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(F4, H4);
    map.put(G5, squareSet);
  }

  private static void addBlackH5(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(G4);
    map.put(H5, squareSet);
  }

  private static void addBlackA6(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B5);
    map.put(A6, squareSet);
  }

  private static void addBlackB6(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(A5, C5);
    map.put(B6, squareSet);
  }

  private static void addBlackC6(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B5, D5);
    map.put(C6, squareSet);
  }

  private static void addBlackD6(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(C5, E5);
    map.put(D6, squareSet);
  }

  private static void addBlackE6(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(D5, F5);
    map.put(E6, squareSet);
  }

  private static void addBlackF6(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(E5, G5);
    map.put(F6, squareSet);
  }

  private static void addBlackG6(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(F5, H5);
    map.put(G6, squareSet);
  }

  private static void addBlackH6(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(G5);
    map.put(H6, squareSet);
  }

  private static void addBlackA7(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B6);
    map.put(A7, squareSet);
  }

  private static void addBlackB7(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(A6, C6);
    map.put(B7, squareSet);
  }

  private static void addBlackC7(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(B6, D6);
    map.put(C7, squareSet);
  }

  private static void addBlackD7(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(C6, E6);
    map.put(D7, squareSet);
  }

  private static void addBlackE7(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(D6, F6);
    map.put(E7, squareSet);
  }

  private static void addBlackF7(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(E6, G6);
    map.put(F7, squareSet);
  }

  private static void addBlackG7(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(F6, H6);
    map.put(G7, squareSet);
  }

  private static void addBlackH7(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = constructSet(G6);
    map.put(H7, squareSet);
  }

  private static void addBlackA8(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = ImmutableUtility.EMPTY_UNMODIFIABLE_SET;
    map.put(A8, squareSet);
  }

  private static void addBlackB8(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = ImmutableUtility.EMPTY_UNMODIFIABLE_SET;
    map.put(B8, squareSet);
  }

  private static void addBlackC8(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = ImmutableUtility.EMPTY_UNMODIFIABLE_SET;
    map.put(C8, squareSet);
  }

  private static void addBlackD8(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = ImmutableUtility.EMPTY_UNMODIFIABLE_SET;
    map.put(D8, squareSet);
  }

  private static void addBlackE8(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = ImmutableUtility.EMPTY_UNMODIFIABLE_SET;
    map.put(E8, squareSet);
  }

  private static void addBlackF8(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = ImmutableUtility.EMPTY_UNMODIFIABLE_SET;
    map.put(F8, squareSet);
  }

  private static void addBlackG8(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = ImmutableUtility.EMPTY_UNMODIFIABLE_SET;
    map.put(G8, squareSet);
  }

  private static void addBlackH8(EnumMap<Square, ImmutableSet<Square>> map) {
    final ImmutableSet<Square> squareSet = ImmutableUtility.EMPTY_UNMODIFIABLE_SET;
    map.put(H8, squareSet);
  }

  private static final ImmutableMap<Square, ImmutableSet<Square>> PAWN_BLACK_SQUARES_MAP;

  static {
    final EnumMap<Square, ImmutableSet<Square>> pawnBlackSquaresMap = NonNullWrapperCommon.newEnumMap(Square.class);
    addBlackA1(pawnBlackSquaresMap);
    addBlackB1(pawnBlackSquaresMap);
    addBlackC1(pawnBlackSquaresMap);
    addBlackD1(pawnBlackSquaresMap);
    addBlackE1(pawnBlackSquaresMap);
    addBlackF1(pawnBlackSquaresMap);
    addBlackG1(pawnBlackSquaresMap);
    addBlackH1(pawnBlackSquaresMap);
    addBlackA2(pawnBlackSquaresMap);
    addBlackB2(pawnBlackSquaresMap);
    addBlackC2(pawnBlackSquaresMap);
    addBlackD2(pawnBlackSquaresMap);
    addBlackE2(pawnBlackSquaresMap);
    addBlackF2(pawnBlackSquaresMap);
    addBlackG2(pawnBlackSquaresMap);
    addBlackH2(pawnBlackSquaresMap);
    addBlackA3(pawnBlackSquaresMap);
    addBlackB3(pawnBlackSquaresMap);
    addBlackC3(pawnBlackSquaresMap);
    addBlackD3(pawnBlackSquaresMap);
    addBlackE3(pawnBlackSquaresMap);
    addBlackF3(pawnBlackSquaresMap);
    addBlackG3(pawnBlackSquaresMap);
    addBlackH3(pawnBlackSquaresMap);
    addBlackA4(pawnBlackSquaresMap);
    addBlackB4(pawnBlackSquaresMap);
    addBlackC4(pawnBlackSquaresMap);
    addBlackD4(pawnBlackSquaresMap);
    addBlackE4(pawnBlackSquaresMap);
    addBlackF4(pawnBlackSquaresMap);
    addBlackG4(pawnBlackSquaresMap);
    addBlackH4(pawnBlackSquaresMap);
    addBlackA5(pawnBlackSquaresMap);
    addBlackB5(pawnBlackSquaresMap);
    addBlackC5(pawnBlackSquaresMap);
    addBlackD5(pawnBlackSquaresMap);
    addBlackE5(pawnBlackSquaresMap);
    addBlackF5(pawnBlackSquaresMap);
    addBlackG5(pawnBlackSquaresMap);
    addBlackH5(pawnBlackSquaresMap);
    addBlackA6(pawnBlackSquaresMap);
    addBlackB6(pawnBlackSquaresMap);
    addBlackC6(pawnBlackSquaresMap);
    addBlackD6(pawnBlackSquaresMap);
    addBlackE6(pawnBlackSquaresMap);
    addBlackF6(pawnBlackSquaresMap);
    addBlackG6(pawnBlackSquaresMap);
    addBlackH6(pawnBlackSquaresMap);
    addBlackA7(pawnBlackSquaresMap);
    addBlackB7(pawnBlackSquaresMap);
    addBlackC7(pawnBlackSquaresMap);
    addBlackD7(pawnBlackSquaresMap);
    addBlackE7(pawnBlackSquaresMap);
    addBlackF7(pawnBlackSquaresMap);
    addBlackG7(pawnBlackSquaresMap);
    addBlackH7(pawnBlackSquaresMap);
    addBlackA8(pawnBlackSquaresMap);
    addBlackB8(pawnBlackSquaresMap);
    addBlackC8(pawnBlackSquaresMap);
    addBlackD8(pawnBlackSquaresMap);
    addBlackE8(pawnBlackSquaresMap);
    addBlackF8(pawnBlackSquaresMap);
    addBlackG8(pawnBlackSquaresMap);
    addBlackH8(pawnBlackSquaresMap);

    PAWN_BLACK_SQUARES_MAP = NonNullWrapperCommon.copyOfMap(pawnBlackSquaresMap);

    ValidateMoveNumberUtility.validateMapOfSet(PAWN_BLACK_SQUARES_MAP, 84);

  }

  // END generated code

  public static Set<Square> getPawnDiagonalSquares(Side havingMove, Square fromSquare) {
    return switch (havingMove) {
      case BLACK -> NonNullWrapperCommon.get(PAWN_BLACK_SQUARES_MAP, fromSquare);
      case WHITE -> NonNullWrapperCommon.get(PAWN_WHITE_SQUARES_MAP, fromSquare);
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

}
