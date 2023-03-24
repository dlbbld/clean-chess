package com.dlb.chess.squares.pawn.diagonal;

import static com.dlb.chess.common.utility.ImmutableUtility.constructSet;

import java.util.EnumMap;
import java.util.Set;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.utility.ValidateMoveNumberUtility;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

public class PawnDiagonalSquares implements EnumConstants {

  // BEGIN generated code

  private static final ImmutableMap<Square, ImmutableSet<Square>> PAWN_WHITE_SQUARES;

  static {

    final EnumMap<Square, ImmutableSet<Square>> pawnWhiteMap = NonNullWrapperCommon.newEnumMap(Square.class);

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(A1, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(B1, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(C1, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(D1, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(E1, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(F1, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(G1, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(H1, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B3);
      pawnWhiteMap.put(A2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(A3, C3);
      pawnWhiteMap.put(B2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B3, D3);
      pawnWhiteMap.put(C2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(C3, E3);
      pawnWhiteMap.put(D2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(D3, F3);
      pawnWhiteMap.put(E2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(E3, G3);
      pawnWhiteMap.put(F2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(F3, H3);
      pawnWhiteMap.put(G2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(G3);
      pawnWhiteMap.put(H2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B4);
      pawnWhiteMap.put(A3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(A4, C4);
      pawnWhiteMap.put(B3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B4, D4);
      pawnWhiteMap.put(C3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(C4, E4);
      pawnWhiteMap.put(D3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(D4, F4);
      pawnWhiteMap.put(E3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(E4, G4);
      pawnWhiteMap.put(F3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(F4, H4);
      pawnWhiteMap.put(G3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(G4);
      pawnWhiteMap.put(H3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B5);
      pawnWhiteMap.put(A4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(A5, C5);
      pawnWhiteMap.put(B4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B5, D5);
      pawnWhiteMap.put(C4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(C5, E5);
      pawnWhiteMap.put(D4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(D5, F5);
      pawnWhiteMap.put(E4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(E5, G5);
      pawnWhiteMap.put(F4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(F5, H5);
      pawnWhiteMap.put(G4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(G5);
      pawnWhiteMap.put(H4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B6);
      pawnWhiteMap.put(A5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(A6, C6);
      pawnWhiteMap.put(B5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B6, D6);
      pawnWhiteMap.put(C5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(C6, E6);
      pawnWhiteMap.put(D5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(D6, F6);
      pawnWhiteMap.put(E5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(E6, G6);
      pawnWhiteMap.put(F5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(F6, H6);
      pawnWhiteMap.put(G5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(G6);
      pawnWhiteMap.put(H5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B7);
      pawnWhiteMap.put(A6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(A7, C7);
      pawnWhiteMap.put(B6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B7, D7);
      pawnWhiteMap.put(C6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(C7, E7);
      pawnWhiteMap.put(D6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(D7, F7);
      pawnWhiteMap.put(E6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(E7, G7);
      pawnWhiteMap.put(F6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(F7, H7);
      pawnWhiteMap.put(G6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(G7);
      pawnWhiteMap.put(H6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B8);
      pawnWhiteMap.put(A7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(A8, C8);
      pawnWhiteMap.put(B7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B8, D8);
      pawnWhiteMap.put(C7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(C8, E8);
      pawnWhiteMap.put(D7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(D8, F8);
      pawnWhiteMap.put(E7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(E8, G8);
      pawnWhiteMap.put(F7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(F8, H8);
      pawnWhiteMap.put(G7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(G8);
      pawnWhiteMap.put(H7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(A8, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(B8, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(C8, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(D8, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(E8, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(F8, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(G8, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(H8, squareSet);
    }

    PAWN_WHITE_SQUARES = NonNullWrapperCommon.copyOfMap(pawnWhiteMap);

  }

  // END generated code

  // BEGIN generated code

  private static final ImmutableMap<Square, ImmutableSet<Square>> PAWN_BLACK_SQUARES;

  static {
    final EnumMap<Square, ImmutableSet<Square>> pawnBlackMap = NonNullWrapperCommon.newEnumMap(Square.class);

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(A1, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(B1, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(C1, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(D1, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(E1, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(F1, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(G1, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(H1, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B1);
      pawnBlackMap.put(A2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(A1, C1);
      pawnBlackMap.put(B2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B1, D1);
      pawnBlackMap.put(C2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(C1, E1);
      pawnBlackMap.put(D2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(D1, F1);
      pawnBlackMap.put(E2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(E1, G1);
      pawnBlackMap.put(F2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(F1, H1);
      pawnBlackMap.put(G2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(G1);
      pawnBlackMap.put(H2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B2);
      pawnBlackMap.put(A3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(A2, C2);
      pawnBlackMap.put(B3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B2, D2);
      pawnBlackMap.put(C3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(C2, E2);
      pawnBlackMap.put(D3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(D2, F2);
      pawnBlackMap.put(E3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(E2, G2);
      pawnBlackMap.put(F3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(F2, H2);
      pawnBlackMap.put(G3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(G2);
      pawnBlackMap.put(H3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B3);
      pawnBlackMap.put(A4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(A3, C3);
      pawnBlackMap.put(B4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B3, D3);
      pawnBlackMap.put(C4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(C3, E3);
      pawnBlackMap.put(D4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(D3, F3);
      pawnBlackMap.put(E4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(E3, G3);
      pawnBlackMap.put(F4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(F3, H3);
      pawnBlackMap.put(G4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(G3);
      pawnBlackMap.put(H4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B4);
      pawnBlackMap.put(A5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(A4, C4);
      pawnBlackMap.put(B5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B4, D4);
      pawnBlackMap.put(C5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(C4, E4);
      pawnBlackMap.put(D5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(D4, F4);
      pawnBlackMap.put(E5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(E4, G4);
      pawnBlackMap.put(F5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(F4, H4);
      pawnBlackMap.put(G5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(G4);
      pawnBlackMap.put(H5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B5);
      pawnBlackMap.put(A6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(A5, C5);
      pawnBlackMap.put(B6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B5, D5);
      pawnBlackMap.put(C6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(C5, E5);
      pawnBlackMap.put(D6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(D5, F5);
      pawnBlackMap.put(E6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(E5, G5);
      pawnBlackMap.put(F6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(F5, H5);
      pawnBlackMap.put(G6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(G5);
      pawnBlackMap.put(H6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B6);
      pawnBlackMap.put(A7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(A6, C6);
      pawnBlackMap.put(B7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B6, D6);
      pawnBlackMap.put(C7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(C6, E6);
      pawnBlackMap.put(D7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(D6, F6);
      pawnBlackMap.put(E7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(E6, G6);
      pawnBlackMap.put(F7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(F6, H6);
      pawnBlackMap.put(G7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(G6);
      pawnBlackMap.put(H7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(A8, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(B8, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(C8, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(D8, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(E8, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(F8, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(G8, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(H8, squareSet);
    }

    PAWN_BLACK_SQUARES = NonNullWrapperCommon.copyOfMap(pawnBlackMap);

  }

  static {
    ValidateMoveNumberUtility.validateMapOfSet(PAWN_WHITE_SQUARES, 84);
    ValidateMoveNumberUtility.validateMapOfSet(PAWN_BLACK_SQUARES, 84);
  }

  // END generated code

  public static Set<Square> getPawnDiagonalSquares(Side havingMove, Square fromSquare) {
    return switch (havingMove) {
      case BLACK -> NonNullWrapperCommon.get(PAWN_BLACK_SQUARES, fromSquare);
      case WHITE -> NonNullWrapperCommon.get(PAWN_WHITE_SQUARES, fromSquare);
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

}
