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

  private static final ImmutableMap<Square, RookRange> ROOK_SQUARES;

  static {
    final EnumMap<Square, RookRange> rookMap = NonNullWrapperCommon.newEnumMap(Square.class);

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(A2, A3, A4, A5, A6, A7, A8);
      final ImmutableList<Square> squareListEast = constructListSquare(B1, C1, D1, E1, F1, G1, H1);
      final ImmutableList<Square> squareListSouth = constructListSquare();
      final ImmutableList<Square> squareListWest = constructListSquare();
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(A1, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(B2, B3, B4, B5, B6, B7, B8);
      final ImmutableList<Square> squareListEast = constructListSquare(C1, D1, E1, F1, G1, H1);
      final ImmutableList<Square> squareListSouth = constructListSquare();
      final ImmutableList<Square> squareListWest = constructListSquare(A1);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(B1, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(C2, C3, C4, C5, C6, C7, C8);
      final ImmutableList<Square> squareListEast = constructListSquare(D1, E1, F1, G1, H1);
      final ImmutableList<Square> squareListSouth = constructListSquare();
      final ImmutableList<Square> squareListWest = constructListSquare(B1, A1);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(C1, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(D2, D3, D4, D5, D6, D7, D8);
      final ImmutableList<Square> squareListEast = constructListSquare(E1, F1, G1, H1);
      final ImmutableList<Square> squareListSouth = constructListSquare();
      final ImmutableList<Square> squareListWest = constructListSquare(C1, B1, A1);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(D1, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(E2, E3, E4, E5, E6, E7, E8);
      final ImmutableList<Square> squareListEast = constructListSquare(F1, G1, H1);
      final ImmutableList<Square> squareListSouth = constructListSquare();
      final ImmutableList<Square> squareListWest = constructListSquare(D1, C1, B1, A1);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(E1, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(F2, F3, F4, F5, F6, F7, F8);
      final ImmutableList<Square> squareListEast = constructListSquare(G1, H1);
      final ImmutableList<Square> squareListSouth = constructListSquare();
      final ImmutableList<Square> squareListWest = constructListSquare(E1, D1, C1, B1, A1);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(F1, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(G2, G3, G4, G5, G6, G7, G8);
      final ImmutableList<Square> squareListEast = constructListSquare(H1);
      final ImmutableList<Square> squareListSouth = constructListSquare();
      final ImmutableList<Square> squareListWest = constructListSquare(F1, E1, D1, C1, B1, A1);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(G1, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(H2, H3, H4, H5, H6, H7, H8);
      final ImmutableList<Square> squareListEast = constructListSquare();
      final ImmutableList<Square> squareListSouth = constructListSquare();
      final ImmutableList<Square> squareListWest = constructListSquare(G1, F1, E1, D1, C1, B1, A1);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(H1, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(A3, A4, A5, A6, A7, A8);
      final ImmutableList<Square> squareListEast = constructListSquare(B2, C2, D2, E2, F2, G2, H2);
      final ImmutableList<Square> squareListSouth = constructListSquare(A1);
      final ImmutableList<Square> squareListWest = constructListSquare();
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(A2, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(B3, B4, B5, B6, B7, B8);
      final ImmutableList<Square> squareListEast = constructListSquare(C2, D2, E2, F2, G2, H2);
      final ImmutableList<Square> squareListSouth = constructListSquare(B1);
      final ImmutableList<Square> squareListWest = constructListSquare(A2);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(B2, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(C3, C4, C5, C6, C7, C8);
      final ImmutableList<Square> squareListEast = constructListSquare(D2, E2, F2, G2, H2);
      final ImmutableList<Square> squareListSouth = constructListSquare(C1);
      final ImmutableList<Square> squareListWest = constructListSquare(B2, A2);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(C2, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(D3, D4, D5, D6, D7, D8);
      final ImmutableList<Square> squareListEast = constructListSquare(E2, F2, G2, H2);
      final ImmutableList<Square> squareListSouth = constructListSquare(D1);
      final ImmutableList<Square> squareListWest = constructListSquare(C2, B2, A2);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(D2, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(E3, E4, E5, E6, E7, E8);
      final ImmutableList<Square> squareListEast = constructListSquare(F2, G2, H2);
      final ImmutableList<Square> squareListSouth = constructListSquare(E1);
      final ImmutableList<Square> squareListWest = constructListSquare(D2, C2, B2, A2);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(E2, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(F3, F4, F5, F6, F7, F8);
      final ImmutableList<Square> squareListEast = constructListSquare(G2, H2);
      final ImmutableList<Square> squareListSouth = constructListSquare(F1);
      final ImmutableList<Square> squareListWest = constructListSquare(E2, D2, C2, B2, A2);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(F2, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(G3, G4, G5, G6, G7, G8);
      final ImmutableList<Square> squareListEast = constructListSquare(H2);
      final ImmutableList<Square> squareListSouth = constructListSquare(G1);
      final ImmutableList<Square> squareListWest = constructListSquare(F2, E2, D2, C2, B2, A2);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(G2, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(H3, H4, H5, H6, H7, H8);
      final ImmutableList<Square> squareListEast = constructListSquare();
      final ImmutableList<Square> squareListSouth = constructListSquare(H1);
      final ImmutableList<Square> squareListWest = constructListSquare(G2, F2, E2, D2, C2, B2, A2);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(H2, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(A4, A5, A6, A7, A8);
      final ImmutableList<Square> squareListEast = constructListSquare(B3, C3, D3, E3, F3, G3, H3);
      final ImmutableList<Square> squareListSouth = constructListSquare(A2, A1);
      final ImmutableList<Square> squareListWest = constructListSquare();
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(A3, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(B4, B5, B6, B7, B8);
      final ImmutableList<Square> squareListEast = constructListSquare(C3, D3, E3, F3, G3, H3);
      final ImmutableList<Square> squareListSouth = constructListSquare(B2, B1);
      final ImmutableList<Square> squareListWest = constructListSquare(A3);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(B3, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(C4, C5, C6, C7, C8);
      final ImmutableList<Square> squareListEast = constructListSquare(D3, E3, F3, G3, H3);
      final ImmutableList<Square> squareListSouth = constructListSquare(C2, C1);
      final ImmutableList<Square> squareListWest = constructListSquare(B3, A3);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(C3, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(D4, D5, D6, D7, D8);
      final ImmutableList<Square> squareListEast = constructListSquare(E3, F3, G3, H3);
      final ImmutableList<Square> squareListSouth = constructListSquare(D2, D1);
      final ImmutableList<Square> squareListWest = constructListSquare(C3, B3, A3);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(D3, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(E4, E5, E6, E7, E8);
      final ImmutableList<Square> squareListEast = constructListSquare(F3, G3, H3);
      final ImmutableList<Square> squareListSouth = constructListSquare(E2, E1);
      final ImmutableList<Square> squareListWest = constructListSquare(D3, C3, B3, A3);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(E3, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(F4, F5, F6, F7, F8);
      final ImmutableList<Square> squareListEast = constructListSquare(G3, H3);
      final ImmutableList<Square> squareListSouth = constructListSquare(F2, F1);
      final ImmutableList<Square> squareListWest = constructListSquare(E3, D3, C3, B3, A3);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(F3, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(G4, G5, G6, G7, G8);
      final ImmutableList<Square> squareListEast = constructListSquare(H3);
      final ImmutableList<Square> squareListSouth = constructListSquare(G2, G1);
      final ImmutableList<Square> squareListWest = constructListSquare(F3, E3, D3, C3, B3, A3);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(G3, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(H4, H5, H6, H7, H8);
      final ImmutableList<Square> squareListEast = constructListSquare();
      final ImmutableList<Square> squareListSouth = constructListSquare(H2, H1);
      final ImmutableList<Square> squareListWest = constructListSquare(G3, F3, E3, D3, C3, B3, A3);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(H3, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(A5, A6, A7, A8);
      final ImmutableList<Square> squareListEast = constructListSquare(B4, C4, D4, E4, F4, G4, H4);
      final ImmutableList<Square> squareListSouth = constructListSquare(A3, A2, A1);
      final ImmutableList<Square> squareListWest = constructListSquare();
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(A4, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(B5, B6, B7, B8);
      final ImmutableList<Square> squareListEast = constructListSquare(C4, D4, E4, F4, G4, H4);
      final ImmutableList<Square> squareListSouth = constructListSquare(B3, B2, B1);
      final ImmutableList<Square> squareListWest = constructListSquare(A4);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(B4, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(C5, C6, C7, C8);
      final ImmutableList<Square> squareListEast = constructListSquare(D4, E4, F4, G4, H4);
      final ImmutableList<Square> squareListSouth = constructListSquare(C3, C2, C1);
      final ImmutableList<Square> squareListWest = constructListSquare(B4, A4);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(C4, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(D5, D6, D7, D8);
      final ImmutableList<Square> squareListEast = constructListSquare(E4, F4, G4, H4);
      final ImmutableList<Square> squareListSouth = constructListSquare(D3, D2, D1);
      final ImmutableList<Square> squareListWest = constructListSquare(C4, B4, A4);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(D4, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(E5, E6, E7, E8);
      final ImmutableList<Square> squareListEast = constructListSquare(F4, G4, H4);
      final ImmutableList<Square> squareListSouth = constructListSquare(E3, E2, E1);
      final ImmutableList<Square> squareListWest = constructListSquare(D4, C4, B4, A4);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(E4, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(F5, F6, F7, F8);
      final ImmutableList<Square> squareListEast = constructListSquare(G4, H4);
      final ImmutableList<Square> squareListSouth = constructListSquare(F3, F2, F1);
      final ImmutableList<Square> squareListWest = constructListSquare(E4, D4, C4, B4, A4);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(F4, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(G5, G6, G7, G8);
      final ImmutableList<Square> squareListEast = constructListSquare(H4);
      final ImmutableList<Square> squareListSouth = constructListSquare(G3, G2, G1);
      final ImmutableList<Square> squareListWest = constructListSquare(F4, E4, D4, C4, B4, A4);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(G4, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(H5, H6, H7, H8);
      final ImmutableList<Square> squareListEast = constructListSquare();
      final ImmutableList<Square> squareListSouth = constructListSquare(H3, H2, H1);
      final ImmutableList<Square> squareListWest = constructListSquare(G4, F4, E4, D4, C4, B4, A4);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(H4, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(A6, A7, A8);
      final ImmutableList<Square> squareListEast = constructListSquare(B5, C5, D5, E5, F5, G5, H5);
      final ImmutableList<Square> squareListSouth = constructListSquare(A4, A3, A2, A1);
      final ImmutableList<Square> squareListWest = constructListSquare();
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(A5, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(B6, B7, B8);
      final ImmutableList<Square> squareListEast = constructListSquare(C5, D5, E5, F5, G5, H5);
      final ImmutableList<Square> squareListSouth = constructListSquare(B4, B3, B2, B1);
      final ImmutableList<Square> squareListWest = constructListSquare(A5);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(B5, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(C6, C7, C8);
      final ImmutableList<Square> squareListEast = constructListSquare(D5, E5, F5, G5, H5);
      final ImmutableList<Square> squareListSouth = constructListSquare(C4, C3, C2, C1);
      final ImmutableList<Square> squareListWest = constructListSquare(B5, A5);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(C5, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(D6, D7, D8);
      final ImmutableList<Square> squareListEast = constructListSquare(E5, F5, G5, H5);
      final ImmutableList<Square> squareListSouth = constructListSquare(D4, D3, D2, D1);
      final ImmutableList<Square> squareListWest = constructListSquare(C5, B5, A5);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(D5, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(E6, E7, E8);
      final ImmutableList<Square> squareListEast = constructListSquare(F5, G5, H5);
      final ImmutableList<Square> squareListSouth = constructListSquare(E4, E3, E2, E1);
      final ImmutableList<Square> squareListWest = constructListSquare(D5, C5, B5, A5);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(E5, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(F6, F7, F8);
      final ImmutableList<Square> squareListEast = constructListSquare(G5, H5);
      final ImmutableList<Square> squareListSouth = constructListSquare(F4, F3, F2, F1);
      final ImmutableList<Square> squareListWest = constructListSquare(E5, D5, C5, B5, A5);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(F5, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(G6, G7, G8);
      final ImmutableList<Square> squareListEast = constructListSquare(H5);
      final ImmutableList<Square> squareListSouth = constructListSquare(G4, G3, G2, G1);
      final ImmutableList<Square> squareListWest = constructListSquare(F5, E5, D5, C5, B5, A5);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(G5, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(H6, H7, H8);
      final ImmutableList<Square> squareListEast = constructListSquare();
      final ImmutableList<Square> squareListSouth = constructListSquare(H4, H3, H2, H1);
      final ImmutableList<Square> squareListWest = constructListSquare(G5, F5, E5, D5, C5, B5, A5);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(H5, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(A7, A8);
      final ImmutableList<Square> squareListEast = constructListSquare(B6, C6, D6, E6, F6, G6, H6);
      final ImmutableList<Square> squareListSouth = constructListSquare(A5, A4, A3, A2, A1);
      final ImmutableList<Square> squareListWest = constructListSquare();
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(A6, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(B7, B8);
      final ImmutableList<Square> squareListEast = constructListSquare(C6, D6, E6, F6, G6, H6);
      final ImmutableList<Square> squareListSouth = constructListSquare(B5, B4, B3, B2, B1);
      final ImmutableList<Square> squareListWest = constructListSquare(A6);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(B6, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(C7, C8);
      final ImmutableList<Square> squareListEast = constructListSquare(D6, E6, F6, G6, H6);
      final ImmutableList<Square> squareListSouth = constructListSquare(C5, C4, C3, C2, C1);
      final ImmutableList<Square> squareListWest = constructListSquare(B6, A6);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(C6, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(D7, D8);
      final ImmutableList<Square> squareListEast = constructListSquare(E6, F6, G6, H6);
      final ImmutableList<Square> squareListSouth = constructListSquare(D5, D4, D3, D2, D1);
      final ImmutableList<Square> squareListWest = constructListSquare(C6, B6, A6);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(D6, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(E7, E8);
      final ImmutableList<Square> squareListEast = constructListSquare(F6, G6, H6);
      final ImmutableList<Square> squareListSouth = constructListSquare(E5, E4, E3, E2, E1);
      final ImmutableList<Square> squareListWest = constructListSquare(D6, C6, B6, A6);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(E6, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(F7, F8);
      final ImmutableList<Square> squareListEast = constructListSquare(G6, H6);
      final ImmutableList<Square> squareListSouth = constructListSquare(F5, F4, F3, F2, F1);
      final ImmutableList<Square> squareListWest = constructListSquare(E6, D6, C6, B6, A6);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(F6, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(G7, G8);
      final ImmutableList<Square> squareListEast = constructListSquare(H6);
      final ImmutableList<Square> squareListSouth = constructListSquare(G5, G4, G3, G2, G1);
      final ImmutableList<Square> squareListWest = constructListSquare(F6, E6, D6, C6, B6, A6);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(G6, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(H7, H8);
      final ImmutableList<Square> squareListEast = constructListSquare();
      final ImmutableList<Square> squareListSouth = constructListSquare(H5, H4, H3, H2, H1);
      final ImmutableList<Square> squareListWest = constructListSquare(G6, F6, E6, D6, C6, B6, A6);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(H6, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(A8);
      final ImmutableList<Square> squareListEast = constructListSquare(B7, C7, D7, E7, F7, G7, H7);
      final ImmutableList<Square> squareListSouth = constructListSquare(A6, A5, A4, A3, A2, A1);
      final ImmutableList<Square> squareListWest = constructListSquare();
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(A7, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(B8);
      final ImmutableList<Square> squareListEast = constructListSquare(C7, D7, E7, F7, G7, H7);
      final ImmutableList<Square> squareListSouth = constructListSquare(B6, B5, B4, B3, B2, B1);
      final ImmutableList<Square> squareListWest = constructListSquare(A7);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(B7, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(C8);
      final ImmutableList<Square> squareListEast = constructListSquare(D7, E7, F7, G7, H7);
      final ImmutableList<Square> squareListSouth = constructListSquare(C6, C5, C4, C3, C2, C1);
      final ImmutableList<Square> squareListWest = constructListSquare(B7, A7);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(C7, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(D8);
      final ImmutableList<Square> squareListEast = constructListSquare(E7, F7, G7, H7);
      final ImmutableList<Square> squareListSouth = constructListSquare(D6, D5, D4, D3, D2, D1);
      final ImmutableList<Square> squareListWest = constructListSquare(C7, B7, A7);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(D7, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(E8);
      final ImmutableList<Square> squareListEast = constructListSquare(F7, G7, H7);
      final ImmutableList<Square> squareListSouth = constructListSquare(E6, E5, E4, E3, E2, E1);
      final ImmutableList<Square> squareListWest = constructListSquare(D7, C7, B7, A7);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(E7, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(F8);
      final ImmutableList<Square> squareListEast = constructListSquare(G7, H7);
      final ImmutableList<Square> squareListSouth = constructListSquare(F6, F5, F4, F3, F2, F1);
      final ImmutableList<Square> squareListWest = constructListSquare(E7, D7, C7, B7, A7);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(F7, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(G8);
      final ImmutableList<Square> squareListEast = constructListSquare(H7);
      final ImmutableList<Square> squareListSouth = constructListSquare(G6, G5, G4, G3, G2, G1);
      final ImmutableList<Square> squareListWest = constructListSquare(F7, E7, D7, C7, B7, A7);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(G7, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(H8);
      final ImmutableList<Square> squareListEast = constructListSquare();
      final ImmutableList<Square> squareListSouth = constructListSquare(H6, H5, H4, H3, H2, H1);
      final ImmutableList<Square> squareListWest = constructListSquare(G7, F7, E7, D7, C7, B7, A7);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(H7, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare();
      final ImmutableList<Square> squareListEast = constructListSquare(B8, C8, D8, E8, F8, G8, H8);
      final ImmutableList<Square> squareListSouth = constructListSquare(A7, A6, A5, A4, A3, A2, A1);
      final ImmutableList<Square> squareListWest = constructListSquare();
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(A8, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare();
      final ImmutableList<Square> squareListEast = constructListSquare(C8, D8, E8, F8, G8, H8);
      final ImmutableList<Square> squareListSouth = constructListSquare(B7, B6, B5, B4, B3, B2, B1);
      final ImmutableList<Square> squareListWest = constructListSquare(A8);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(B8, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare();
      final ImmutableList<Square> squareListEast = constructListSquare(D8, E8, F8, G8, H8);
      final ImmutableList<Square> squareListSouth = constructListSquare(C7, C6, C5, C4, C3, C2, C1);
      final ImmutableList<Square> squareListWest = constructListSquare(B8, A8);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(C8, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare();
      final ImmutableList<Square> squareListEast = constructListSquare(E8, F8, G8, H8);
      final ImmutableList<Square> squareListSouth = constructListSquare(D7, D6, D5, D4, D3, D2, D1);
      final ImmutableList<Square> squareListWest = constructListSquare(C8, B8, A8);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(D8, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare();
      final ImmutableList<Square> squareListEast = constructListSquare(F8, G8, H8);
      final ImmutableList<Square> squareListSouth = constructListSquare(E7, E6, E5, E4, E3, E2, E1);
      final ImmutableList<Square> squareListWest = constructListSquare(D8, C8, B8, A8);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(E8, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare();
      final ImmutableList<Square> squareListEast = constructListSquare(G8, H8);
      final ImmutableList<Square> squareListSouth = constructListSquare(F7, F6, F5, F4, F3, F2, F1);
      final ImmutableList<Square> squareListWest = constructListSquare(E8, D8, C8, B8, A8);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(F8, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare();
      final ImmutableList<Square> squareListEast = constructListSquare(H8);
      final ImmutableList<Square> squareListSouth = constructListSquare(G7, G6, G5, G4, G3, G2, G1);
      final ImmutableList<Square> squareListWest = constructListSquare(F8, E8, D8, C8, B8, A8);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(G8, rookRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare();
      final ImmutableList<Square> squareListEast = constructListSquare();
      final ImmutableList<Square> squareListSouth = constructListSquare(H7, H6, H5, H4, H3, H2, H1);
      final ImmutableList<Square> squareListWest = constructListSquare(G8, F8, E8, D8, C8, B8, A8);
      final RookRange rookRange = new RookRange(squareListNorth, squareListEast, squareListSouth, squareListWest);
      rookMap.put(H8, rookRange);
    }

    ROOK_SQUARES = NonNullWrapperCommon.copyOfMap(rookMap);

    ValidateMoveNumberUtility.validateOrthogonalMoveNumber(ROOK_SQUARES, 896);

  }

  // END generated code

  public static RookRange getRookSquares(Square fromSquare) {
    return NonNullWrapperCommon.get(ROOK_SQUARES, fromSquare);
  }

}
