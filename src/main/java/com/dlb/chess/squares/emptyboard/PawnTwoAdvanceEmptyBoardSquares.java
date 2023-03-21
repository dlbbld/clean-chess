package com.dlb.chess.squares.emptyboard;

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

public class PawnTwoAdvanceEmptyBoardSquares extends AbstractEmptyBoardSquares implements EnumConstants {

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
      final ImmutableSet<Square> squareSet = constructSet(A4);
      pawnWhiteMap.put(A2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B4);
      pawnWhiteMap.put(B2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(C4);
      pawnWhiteMap.put(C2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(D4);
      pawnWhiteMap.put(D2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(E4);
      pawnWhiteMap.put(E2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(F4);
      pawnWhiteMap.put(F2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(G4);
      pawnWhiteMap.put(G2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(H4);
      pawnWhiteMap.put(H2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(A3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(B3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(C3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(D3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(E3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(F3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(G3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(H3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(A4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(B4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(C4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(D4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(E4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(F4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(G4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(H4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(A5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(B5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(C5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(D5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(E5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(F5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(G5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(H5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(A6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(B6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(C6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(D6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(E6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(F6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(G6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(H6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(A7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(B7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(C7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(D7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(E7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(F7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnWhiteMap.put(G7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
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

    ValidateMoveNumberUtility.validateMapOfSet(PAWN_WHITE_SQUARES, 8);

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
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(A2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(B2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(C2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(D2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(E2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(F2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(G2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(H2, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(A3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(B3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(C3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(D3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(E3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(F3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(G3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(H3, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(A4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(B4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(C4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(D4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(E4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(F4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(G4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(H4, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(A5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(B5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(C5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(D5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(E5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(F5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(G5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(H5, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(A6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(B6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(C6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(D6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(E6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(F6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(G6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet();
      pawnBlackMap.put(H6, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(A5);
      pawnBlackMap.put(A7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(B5);
      pawnBlackMap.put(B7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(C5);
      pawnBlackMap.put(C7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(D5);
      pawnBlackMap.put(D7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(E5);
      pawnBlackMap.put(E7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(F5);
      pawnBlackMap.put(F7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(G5);
      pawnBlackMap.put(G7, squareSet);
    }

    {
      final ImmutableSet<Square> squareSet = constructSet(H5);
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

    ValidateMoveNumberUtility.validateMapOfSet(PAWN_BLACK_SQUARES, 8);

  }

  // END generated code

  public static Set<Square> getPawnSquares(Side havingMove, Square fromSquare) {
    return switch (havingMove) {
      case BLACK -> NonNullWrapperCommon.get(PAWN_BLACK_SQUARES, fromSquare);
      case WHITE -> NonNullWrapperCommon.get(PAWN_WHITE_SQUARES, fromSquare);
      case NONE -> throw new IllegalArgumentException();
      default -> throw new IllegalArgumentException();
    };
  }

}
