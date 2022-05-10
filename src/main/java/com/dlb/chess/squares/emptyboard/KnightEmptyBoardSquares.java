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

  private static final ImmutableMap<Square, ImmutableSet<Square>> KNIGHT_SQUARES;

  static {
    final EnumMap<Square, ImmutableSet<Square>> knightMap = NonNullWrapperCommon.newEnumMap(Square.class);

    {
      final ImmutableSet<Square> squareSet = constructSet(C2, B3);
      knightMap.put(A1, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(D2, A3, C3);
      knightMap.put(B1, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(A2, E2, B3, D3);
      knightMap.put(C1, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B2, F2, C3, E3);
      knightMap.put(D1, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(C2, G2, D3, F3);
      knightMap.put(E1, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(D2, H2, E3, G3);
      knightMap.put(F1, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(E2, F3, H3);
      knightMap.put(G1, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(F2, G3);
      knightMap.put(H1, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(C1, C3, B4);
      knightMap.put(A2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(D1, D3, A4, C4);
      knightMap.put(B2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(A1, E1, A3, E3, B4, D4);
      knightMap.put(C2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B1, F1, B3, F3, C4, E4);
      knightMap.put(D2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(C1, G1, C3, G3, D4, F4);
      knightMap.put(E2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(D1, H1, D3, H3, E4, G4);
      knightMap.put(F2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(E1, E3, F4, H4);
      knightMap.put(G2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(F1, F3, G4);
      knightMap.put(H2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B1, C2, C4, B5);
      knightMap.put(A3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(A1, C1, D2, D4, A5, C5);
      knightMap.put(B3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B1, D1, A2, E2, A4, E4, B5, D5);
      knightMap.put(C3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(C1, E1, B2, F2, B4, F4, C5, E5);
      knightMap.put(D3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(D1, F1, C2, G2, C4, G4, D5, F5);
      knightMap.put(E3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(E1, G1, D2, H2, D4, H4, E5, G5);
      knightMap.put(F3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(F1, H1, E2, E4, F5, H5);
      knightMap.put(G3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(G1, F2, F4, G5);
      knightMap.put(H3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B2, C3, C5, B6);
      knightMap.put(A4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(A2, C2, D3, D5, A6, C6);
      knightMap.put(B4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B2, D2, A3, E3, A5, E5, B6, D6);
      knightMap.put(C4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(C2, E2, B3, F3, B5, F5, C6, E6);
      knightMap.put(D4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(D2, F2, C3, G3, C5, G5, D6, F6);
      knightMap.put(E4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(E2, G2, D3, H3, D5, H5, E6, G6);
      knightMap.put(F4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(F2, H2, E3, E5, F6, H6);
      knightMap.put(G4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(G2, F3, F5, G6);
      knightMap.put(H4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B3, C4, C6, B7);
      knightMap.put(A5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(A3, C3, D4, D6, A7, C7);
      knightMap.put(B5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B3, D3, A4, E4, A6, E6, B7, D7);
      knightMap.put(C5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(C3, E3, B4, F4, B6, F6, C7, E7);
      knightMap.put(D5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(D3, F3, C4, G4, C6, G6, D7, F7);
      knightMap.put(E5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(E3, G3, D4, H4, D6, H6, E7, G7);
      knightMap.put(F5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(F3, H3, E4, E6, F7, H7);
      knightMap.put(G5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(G3, F4, F6, G7);
      knightMap.put(H5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B4, C5, C7, B8);
      knightMap.put(A6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(A4, C4, D5, D7, A8, C8);
      knightMap.put(B6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B4, D4, A5, E5, A7, E7, B8, D8);
      knightMap.put(C6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(C4, E4, B5, F5, B7, F7, C8, E8);
      knightMap.put(D6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(D4, F4, C5, G5, C7, G7, D8, F8);
      knightMap.put(E6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(E4, G4, D5, H5, D7, H7, E8, G8);
      knightMap.put(F6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(F4, H4, E5, E7, F8, H8);
      knightMap.put(G6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(G4, F5, F7, G8);
      knightMap.put(H6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B5, C6, C8);
      knightMap.put(A7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(A5, C5, D6, D8);
      knightMap.put(B7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B5, D5, A6, E6, A8, E8);
      knightMap.put(C7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(C5, E5, B6, F6, B8, F8);
      knightMap.put(D7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(D5, F5, C6, G6, C8, G8);
      knightMap.put(E7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(E5, G5, D6, H6, D8, H8);
      knightMap.put(F7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(F5, H5, E6, E8);
      knightMap.put(G7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(G5, F6, F8);
      knightMap.put(H7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B6, C7);
      knightMap.put(A8, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(A6, C6, D7);
      knightMap.put(B8, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B6, D6, A7, E7);
      knightMap.put(C8, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(C6, E6, B7, F7);
      knightMap.put(D8, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(D6, F6, C7, G7);
      knightMap.put(E8, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(E6, G6, D7, H7);
      knightMap.put(F8, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(F6, H6, E7);
      knightMap.put(G8, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(G6, F7);
      knightMap.put(H8, squareSet);
    }

    KNIGHT_SQUARES = NonNullWrapperCommon.copyOfMap(knightMap);

    ValidateMoveNumberUtility.validateMapOfSet(KNIGHT_SQUARES, 336);

  }

  // END generated code

  public static Set<Square> getKnightSquares(Square fromSquare) {
    return NonNullWrapperCommon.get(KNIGHT_SQUARES, fromSquare);
  }

}
