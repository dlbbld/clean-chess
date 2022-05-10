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

  private static final ImmutableMap<Square, ImmutableSet<Square>> KING_SQUARES;

  static {
    final EnumMap<Square, ImmutableSet<Square>> kingMap = NonNullWrapperCommon.newEnumMap(Square.class);

    {
      final ImmutableSet<Square> squareSet = constructSet(B1, A2, B2);
      kingMap.put(A1, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(A1, C1, A2, B2, C2);
      kingMap.put(B1, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B1, D1, B2, C2, D2);
      kingMap.put(C1, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(C1, E1, C2, D2, E2);
      kingMap.put(D1, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(D1, F1, D2, E2, F2);
      kingMap.put(E1, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(E1, G1, E2, F2, G2);
      kingMap.put(F1, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(F1, H1, F2, G2, H2);
      kingMap.put(G1, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(G1, G2, H2);
      kingMap.put(H1, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(A1, B1, B2, A3, B3);
      kingMap.put(A2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(A1, B1, C1, A2, C2, A3, B3, C3);
      kingMap.put(B2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B1, C1, D1, B2, D2, B3, C3, D3);
      kingMap.put(C2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(C1, D1, E1, C2, E2, C3, D3, E3);
      kingMap.put(D2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(D1, E1, F1, D2, F2, D3, E3, F3);
      kingMap.put(E2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(E1, F1, G1, E2, G2, E3, F3, G3);
      kingMap.put(F2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(F1, G1, H1, F2, H2, F3, G3, H3);
      kingMap.put(G2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(G1, H1, G2, G3, H3);
      kingMap.put(H2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(A2, B2, B3, A4, B4);
      kingMap.put(A3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(A2, B2, C2, A3, C3, A4, B4, C4);
      kingMap.put(B3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B2, C2, D2, B3, D3, B4, C4, D4);
      kingMap.put(C3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(C2, D2, E2, C3, E3, C4, D4, E4);
      kingMap.put(D3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(D2, E2, F2, D3, F3, D4, E4, F4);
      kingMap.put(E3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(E2, F2, G2, E3, G3, E4, F4, G4);
      kingMap.put(F3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(F2, G2, H2, F3, H3, F4, G4, H4);
      kingMap.put(G3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(G2, H2, G3, G4, H4);
      kingMap.put(H3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(A3, B3, B4, A5, B5);
      kingMap.put(A4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(A3, B3, C3, A4, C4, A5, B5, C5);
      kingMap.put(B4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B3, C3, D3, B4, D4, B5, C5, D5);
      kingMap.put(C4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(C3, D3, E3, C4, E4, C5, D5, E5);
      kingMap.put(D4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(D3, E3, F3, D4, F4, D5, E5, F5);
      kingMap.put(E4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(E3, F3, G3, E4, G4, E5, F5, G5);
      kingMap.put(F4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(F3, G3, H3, F4, H4, F5, G5, H5);
      kingMap.put(G4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(G3, H3, G4, G5, H5);
      kingMap.put(H4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(A4, B4, B5, A6, B6);
      kingMap.put(A5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(A4, B4, C4, A5, C5, A6, B6, C6);
      kingMap.put(B5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B4, C4, D4, B5, D5, B6, C6, D6);
      kingMap.put(C5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(C4, D4, E4, C5, E5, C6, D6, E6);
      kingMap.put(D5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(D4, E4, F4, D5, F5, D6, E6, F6);
      kingMap.put(E5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(E4, F4, G4, E5, G5, E6, F6, G6);
      kingMap.put(F5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(F4, G4, H4, F5, H5, F6, G6, H6);
      kingMap.put(G5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(G4, H4, G5, G6, H6);
      kingMap.put(H5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(A5, B5, B6, A7, B7);
      kingMap.put(A6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(A5, B5, C5, A6, C6, A7, B7, C7);
      kingMap.put(B6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B5, C5, D5, B6, D6, B7, C7, D7);
      kingMap.put(C6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(C5, D5, E5, C6, E6, C7, D7, E7);
      kingMap.put(D6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(D5, E5, F5, D6, F6, D7, E7, F7);
      kingMap.put(E6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(E5, F5, G5, E6, G6, E7, F7, G7);
      kingMap.put(F6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(F5, G5, H5, F6, H6, F7, G7, H7);
      kingMap.put(G6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(G5, H5, G6, G7, H7);
      kingMap.put(H6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(A6, B6, B7, A8, B8);
      kingMap.put(A7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(A6, B6, C6, A7, C7, A8, B8, C8);
      kingMap.put(B7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B6, C6, D6, B7, D7, B8, C8, D8);
      kingMap.put(C7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(C6, D6, E6, C7, E7, C8, D8, E8);
      kingMap.put(D7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(D6, E6, F6, D7, F7, D8, E8, F8);
      kingMap.put(E7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(E6, F6, G6, E7, G7, E8, F8, G8);
      kingMap.put(F7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(F6, G6, H6, F7, H7, F8, G8, H8);
      kingMap.put(G7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(G6, H6, G7, G8, H8);
      kingMap.put(H7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(A7, B7, B8);
      kingMap.put(A8, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(A7, B7, C7, A8, C8);
      kingMap.put(B8, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B7, C7, D7, B8, D8);
      kingMap.put(C8, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(C7, D7, E7, C8, E8);
      kingMap.put(D8, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(D7, E7, F7, D8, F8);
      kingMap.put(E8, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(E7, F7, G7, E8, G8);
      kingMap.put(F8, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(F7, G7, H7, F8, H8);
      kingMap.put(G8, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(G7, H7, G8);
      kingMap.put(H8, squareSet);
    }

    KING_SQUARES = NonNullWrapperCommon.copyOfMap(kingMap);

    ValidateMoveNumberUtility.validateMapOfSet(KING_SQUARES, 420);

  }

  // END generated code

  static {
    ValidateMoveNumberUtility.validateMapOfSet(KING_SQUARES, 420);
  }

  public static Set<Square> getKingSquares(Square fromSquare) {
    return NonNullWrapperCommon.get(KING_SQUARES, fromSquare);
  }

}
