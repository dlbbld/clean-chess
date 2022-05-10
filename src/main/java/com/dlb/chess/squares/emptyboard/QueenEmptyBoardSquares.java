package com.dlb.chess.squares.emptyboard;

import static com.dlb.chess.common.utility.ImmutableUtility.constructListSquare;

import java.util.EnumMap;

import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.range.model.QueenRange;
import com.dlb.chess.utility.ValidateMoveNumberUtility;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class QueenEmptyBoardSquares extends AbstractEmptyBoardSquares implements EnumConstants {

  // BEGIN generated code

  private static final ImmutableMap<Square, QueenRange> QUEEN_SQUARES;

  static {
    final EnumMap<Square, QueenRange> queenMap = NonNullWrapperCommon.newEnumMap(Square.class);

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(A2, A3, A4, A5, A6, A7, A8);
      final ImmutableList<Square> squareListEast = constructListSquare(B1, C1, D1, E1, F1, G1, H1);
      final ImmutableList<Square> squareListSouth = constructListSquare();
      final ImmutableList<Square> squareListWest = constructListSquare();
      final ImmutableList<Square> squareListNorthEast = constructListSquare(B2, C3, D4, E5, F6, G7, H8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthWest = constructListSquare();
      final ImmutableList<Square> squareListNorthWest = constructListSquare();
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(A1, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(B2, B3, B4, B5, B6, B7, B8);
      final ImmutableList<Square> squareListEast = constructListSquare(C1, D1, E1, F1, G1, H1);
      final ImmutableList<Square> squareListSouth = constructListSquare();
      final ImmutableList<Square> squareListWest = constructListSquare(A1);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(C2, D3, E4, F5, G6, H7);
      final ImmutableList<Square> squareListSouthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthWest = constructListSquare();
      final ImmutableList<Square> squareListNorthWest = constructListSquare(A2);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(B1, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(C2, C3, C4, C5, C6, C7, C8);
      final ImmutableList<Square> squareListEast = constructListSquare(D1, E1, F1, G1, H1);
      final ImmutableList<Square> squareListSouth = constructListSquare();
      final ImmutableList<Square> squareListWest = constructListSquare(B1, A1);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(D2, E3, F4, G5, H6);
      final ImmutableList<Square> squareListSouthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthWest = constructListSquare();
      final ImmutableList<Square> squareListNorthWest = constructListSquare(B2, A3);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(C1, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(D2, D3, D4, D5, D6, D7, D8);
      final ImmutableList<Square> squareListEast = constructListSquare(E1, F1, G1, H1);
      final ImmutableList<Square> squareListSouth = constructListSquare();
      final ImmutableList<Square> squareListWest = constructListSquare(C1, B1, A1);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(E2, F3, G4, H5);
      final ImmutableList<Square> squareListSouthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthWest = constructListSquare();
      final ImmutableList<Square> squareListNorthWest = constructListSquare(C2, B3, A4);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(D1, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(E2, E3, E4, E5, E6, E7, E8);
      final ImmutableList<Square> squareListEast = constructListSquare(F1, G1, H1);
      final ImmutableList<Square> squareListSouth = constructListSquare();
      final ImmutableList<Square> squareListWest = constructListSquare(D1, C1, B1, A1);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(F2, G3, H4);
      final ImmutableList<Square> squareListSouthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthWest = constructListSquare();
      final ImmutableList<Square> squareListNorthWest = constructListSquare(D2, C3, B4, A5);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(E1, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(F2, F3, F4, F5, F6, F7, F8);
      final ImmutableList<Square> squareListEast = constructListSquare(G1, H1);
      final ImmutableList<Square> squareListSouth = constructListSquare();
      final ImmutableList<Square> squareListWest = constructListSquare(E1, D1, C1, B1, A1);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(G2, H3);
      final ImmutableList<Square> squareListSouthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthWest = constructListSquare();
      final ImmutableList<Square> squareListNorthWest = constructListSquare(E2, D3, C4, B5, A6);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(F1, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(G2, G3, G4, G5, G6, G7, G8);
      final ImmutableList<Square> squareListEast = constructListSquare(H1);
      final ImmutableList<Square> squareListSouth = constructListSquare();
      final ImmutableList<Square> squareListWest = constructListSquare(F1, E1, D1, C1, B1, A1);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(H2);
      final ImmutableList<Square> squareListSouthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthWest = constructListSquare();
      final ImmutableList<Square> squareListNorthWest = constructListSquare(F2, E3, D4, C5, B6, A7);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(G1, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(H2, H3, H4, H5, H6, H7, H8);
      final ImmutableList<Square> squareListEast = constructListSquare();
      final ImmutableList<Square> squareListSouth = constructListSquare();
      final ImmutableList<Square> squareListWest = constructListSquare(G1, F1, E1, D1, C1, B1, A1);
      final ImmutableList<Square> squareListNorthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthWest = constructListSquare();
      final ImmutableList<Square> squareListNorthWest = constructListSquare(G2, F3, E4, D5, C6, B7, A8);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(H1, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(A3, A4, A5, A6, A7, A8);
      final ImmutableList<Square> squareListEast = constructListSquare(B2, C2, D2, E2, F2, G2, H2);
      final ImmutableList<Square> squareListSouth = constructListSquare(A1);
      final ImmutableList<Square> squareListWest = constructListSquare();
      final ImmutableList<Square> squareListNorthEast = constructListSquare(B3, C4, D5, E6, F7, G8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(B1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare();
      final ImmutableList<Square> squareListNorthWest = constructListSquare();
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(A2, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(B3, B4, B5, B6, B7, B8);
      final ImmutableList<Square> squareListEast = constructListSquare(C2, D2, E2, F2, G2, H2);
      final ImmutableList<Square> squareListSouth = constructListSquare(B1);
      final ImmutableList<Square> squareListWest = constructListSquare(A2);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(C3, D4, E5, F6, G7, H8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(C1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(A1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(A3);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(B2, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(C3, C4, C5, C6, C7, C8);
      final ImmutableList<Square> squareListEast = constructListSquare(D2, E2, F2, G2, H2);
      final ImmutableList<Square> squareListSouth = constructListSquare(C1);
      final ImmutableList<Square> squareListWest = constructListSquare(B2, A2);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(D3, E4, F5, G6, H7);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(D1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(B1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(B3, A4);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(C2, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(D3, D4, D5, D6, D7, D8);
      final ImmutableList<Square> squareListEast = constructListSquare(E2, F2, G2, H2);
      final ImmutableList<Square> squareListSouth = constructListSquare(D1);
      final ImmutableList<Square> squareListWest = constructListSquare(C2, B2, A2);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(E3, F4, G5, H6);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(E1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(C1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(C3, B4, A5);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(D2, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(E3, E4, E5, E6, E7, E8);
      final ImmutableList<Square> squareListEast = constructListSquare(F2, G2, H2);
      final ImmutableList<Square> squareListSouth = constructListSquare(E1);
      final ImmutableList<Square> squareListWest = constructListSquare(D2, C2, B2, A2);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(F3, G4, H5);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(F1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(D1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(D3, C4, B5, A6);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(E2, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(F3, F4, F5, F6, F7, F8);
      final ImmutableList<Square> squareListEast = constructListSquare(G2, H2);
      final ImmutableList<Square> squareListSouth = constructListSquare(F1);
      final ImmutableList<Square> squareListWest = constructListSquare(E2, D2, C2, B2, A2);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(G3, H4);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(G1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(E1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(E3, D4, C5, B6, A7);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(F2, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(G3, G4, G5, G6, G7, G8);
      final ImmutableList<Square> squareListEast = constructListSquare(H2);
      final ImmutableList<Square> squareListSouth = constructListSquare(G1);
      final ImmutableList<Square> squareListWest = constructListSquare(F2, E2, D2, C2, B2, A2);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(H3);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(H1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(F1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(F3, E4, D5, C6, B7, A8);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(G2, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(H3, H4, H5, H6, H7, H8);
      final ImmutableList<Square> squareListEast = constructListSquare();
      final ImmutableList<Square> squareListSouth = constructListSquare(H1);
      final ImmutableList<Square> squareListWest = constructListSquare(G2, F2, E2, D2, C2, B2, A2);
      final ImmutableList<Square> squareListNorthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthWest = constructListSquare(G1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(G3, F4, E5, D6, C7, B8);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(H2, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(A4, A5, A6, A7, A8);
      final ImmutableList<Square> squareListEast = constructListSquare(B3, C3, D3, E3, F3, G3, H3);
      final ImmutableList<Square> squareListSouth = constructListSquare(A2, A1);
      final ImmutableList<Square> squareListWest = constructListSquare();
      final ImmutableList<Square> squareListNorthEast = constructListSquare(B4, C5, D6, E7, F8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(B2, C1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare();
      final ImmutableList<Square> squareListNorthWest = constructListSquare();
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(A3, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(B4, B5, B6, B7, B8);
      final ImmutableList<Square> squareListEast = constructListSquare(C3, D3, E3, F3, G3, H3);
      final ImmutableList<Square> squareListSouth = constructListSquare(B2, B1);
      final ImmutableList<Square> squareListWest = constructListSquare(A3);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(C4, D5, E6, F7, G8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(C2, D1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(A2);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(A4);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(B3, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(C4, C5, C6, C7, C8);
      final ImmutableList<Square> squareListEast = constructListSquare(D3, E3, F3, G3, H3);
      final ImmutableList<Square> squareListSouth = constructListSquare(C2, C1);
      final ImmutableList<Square> squareListWest = constructListSquare(B3, A3);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(D4, E5, F6, G7, H8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(D2, E1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(B2, A1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(B4, A5);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(C3, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(D4, D5, D6, D7, D8);
      final ImmutableList<Square> squareListEast = constructListSquare(E3, F3, G3, H3);
      final ImmutableList<Square> squareListSouth = constructListSquare(D2, D1);
      final ImmutableList<Square> squareListWest = constructListSquare(C3, B3, A3);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(E4, F5, G6, H7);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(E2, F1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(C2, B1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(C4, B5, A6);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(D3, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(E4, E5, E6, E7, E8);
      final ImmutableList<Square> squareListEast = constructListSquare(F3, G3, H3);
      final ImmutableList<Square> squareListSouth = constructListSquare(E2, E1);
      final ImmutableList<Square> squareListWest = constructListSquare(D3, C3, B3, A3);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(F4, G5, H6);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(F2, G1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(D2, C1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(D4, C5, B6, A7);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(E3, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(F4, F5, F6, F7, F8);
      final ImmutableList<Square> squareListEast = constructListSquare(G3, H3);
      final ImmutableList<Square> squareListSouth = constructListSquare(F2, F1);
      final ImmutableList<Square> squareListWest = constructListSquare(E3, D3, C3, B3, A3);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(G4, H5);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(G2, H1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(E2, D1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(E4, D5, C6, B7, A8);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(F3, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(G4, G5, G6, G7, G8);
      final ImmutableList<Square> squareListEast = constructListSquare(H3);
      final ImmutableList<Square> squareListSouth = constructListSquare(G2, G1);
      final ImmutableList<Square> squareListWest = constructListSquare(F3, E3, D3, C3, B3, A3);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(H4);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(H2);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(F2, E1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(F4, E5, D6, C7, B8);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(G3, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(H4, H5, H6, H7, H8);
      final ImmutableList<Square> squareListEast = constructListSquare();
      final ImmutableList<Square> squareListSouth = constructListSquare(H2, H1);
      final ImmutableList<Square> squareListWest = constructListSquare(G3, F3, E3, D3, C3, B3, A3);
      final ImmutableList<Square> squareListNorthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthWest = constructListSquare(G2, F1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(G4, F5, E6, D7, C8);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(H3, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(A5, A6, A7, A8);
      final ImmutableList<Square> squareListEast = constructListSquare(B4, C4, D4, E4, F4, G4, H4);
      final ImmutableList<Square> squareListSouth = constructListSquare(A3, A2, A1);
      final ImmutableList<Square> squareListWest = constructListSquare();
      final ImmutableList<Square> squareListNorthEast = constructListSquare(B5, C6, D7, E8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(B3, C2, D1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare();
      final ImmutableList<Square> squareListNorthWest = constructListSquare();
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(A4, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(B5, B6, B7, B8);
      final ImmutableList<Square> squareListEast = constructListSquare(C4, D4, E4, F4, G4, H4);
      final ImmutableList<Square> squareListSouth = constructListSquare(B3, B2, B1);
      final ImmutableList<Square> squareListWest = constructListSquare(A4);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(C5, D6, E7, F8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(C3, D2, E1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(A3);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(A5);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(B4, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(C5, C6, C7, C8);
      final ImmutableList<Square> squareListEast = constructListSquare(D4, E4, F4, G4, H4);
      final ImmutableList<Square> squareListSouth = constructListSquare(C3, C2, C1);
      final ImmutableList<Square> squareListWest = constructListSquare(B4, A4);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(D5, E6, F7, G8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(D3, E2, F1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(B3, A2);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(B5, A6);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(C4, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(D5, D6, D7, D8);
      final ImmutableList<Square> squareListEast = constructListSquare(E4, F4, G4, H4);
      final ImmutableList<Square> squareListSouth = constructListSquare(D3, D2, D1);
      final ImmutableList<Square> squareListWest = constructListSquare(C4, B4, A4);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(E5, F6, G7, H8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(E3, F2, G1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(C3, B2, A1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(C5, B6, A7);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(D4, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(E5, E6, E7, E8);
      final ImmutableList<Square> squareListEast = constructListSquare(F4, G4, H4);
      final ImmutableList<Square> squareListSouth = constructListSquare(E3, E2, E1);
      final ImmutableList<Square> squareListWest = constructListSquare(D4, C4, B4, A4);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(F5, G6, H7);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(F3, G2, H1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(D3, C2, B1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(D5, C6, B7, A8);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(E4, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(F5, F6, F7, F8);
      final ImmutableList<Square> squareListEast = constructListSquare(G4, H4);
      final ImmutableList<Square> squareListSouth = constructListSquare(F3, F2, F1);
      final ImmutableList<Square> squareListWest = constructListSquare(E4, D4, C4, B4, A4);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(G5, H6);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(G3, H2);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(E3, D2, C1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(E5, D6, C7, B8);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(F4, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(G5, G6, G7, G8);
      final ImmutableList<Square> squareListEast = constructListSquare(H4);
      final ImmutableList<Square> squareListSouth = constructListSquare(G3, G2, G1);
      final ImmutableList<Square> squareListWest = constructListSquare(F4, E4, D4, C4, B4, A4);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(H5);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(H3);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(F3, E2, D1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(F5, E6, D7, C8);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(G4, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(H5, H6, H7, H8);
      final ImmutableList<Square> squareListEast = constructListSquare();
      final ImmutableList<Square> squareListSouth = constructListSquare(H3, H2, H1);
      final ImmutableList<Square> squareListWest = constructListSquare(G4, F4, E4, D4, C4, B4, A4);
      final ImmutableList<Square> squareListNorthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthWest = constructListSquare(G3, F2, E1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(G5, F6, E7, D8);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(H4, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(A6, A7, A8);
      final ImmutableList<Square> squareListEast = constructListSquare(B5, C5, D5, E5, F5, G5, H5);
      final ImmutableList<Square> squareListSouth = constructListSquare(A4, A3, A2, A1);
      final ImmutableList<Square> squareListWest = constructListSquare();
      final ImmutableList<Square> squareListNorthEast = constructListSquare(B6, C7, D8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(B4, C3, D2, E1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare();
      final ImmutableList<Square> squareListNorthWest = constructListSquare();
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(A5, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(B6, B7, B8);
      final ImmutableList<Square> squareListEast = constructListSquare(C5, D5, E5, F5, G5, H5);
      final ImmutableList<Square> squareListSouth = constructListSquare(B4, B3, B2, B1);
      final ImmutableList<Square> squareListWest = constructListSquare(A5);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(C6, D7, E8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(C4, D3, E2, F1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(A4);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(A6);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(B5, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(C6, C7, C8);
      final ImmutableList<Square> squareListEast = constructListSquare(D5, E5, F5, G5, H5);
      final ImmutableList<Square> squareListSouth = constructListSquare(C4, C3, C2, C1);
      final ImmutableList<Square> squareListWest = constructListSquare(B5, A5);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(D6, E7, F8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(D4, E3, F2, G1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(B4, A3);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(B6, A7);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(C5, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(D6, D7, D8);
      final ImmutableList<Square> squareListEast = constructListSquare(E5, F5, G5, H5);
      final ImmutableList<Square> squareListSouth = constructListSquare(D4, D3, D2, D1);
      final ImmutableList<Square> squareListWest = constructListSquare(C5, B5, A5);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(E6, F7, G8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(E4, F3, G2, H1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(C4, B3, A2);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(C6, B7, A8);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(D5, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(E6, E7, E8);
      final ImmutableList<Square> squareListEast = constructListSquare(F5, G5, H5);
      final ImmutableList<Square> squareListSouth = constructListSquare(E4, E3, E2, E1);
      final ImmutableList<Square> squareListWest = constructListSquare(D5, C5, B5, A5);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(F6, G7, H8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(F4, G3, H2);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(D4, C3, B2, A1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(D6, C7, B8);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(E5, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(F6, F7, F8);
      final ImmutableList<Square> squareListEast = constructListSquare(G5, H5);
      final ImmutableList<Square> squareListSouth = constructListSquare(F4, F3, F2, F1);
      final ImmutableList<Square> squareListWest = constructListSquare(E5, D5, C5, B5, A5);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(G6, H7);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(G4, H3);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(E4, D3, C2, B1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(E6, D7, C8);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(F5, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(G6, G7, G8);
      final ImmutableList<Square> squareListEast = constructListSquare(H5);
      final ImmutableList<Square> squareListSouth = constructListSquare(G4, G3, G2, G1);
      final ImmutableList<Square> squareListWest = constructListSquare(F5, E5, D5, C5, B5, A5);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(H6);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(H4);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(F4, E3, D2, C1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(F6, E7, D8);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(G5, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(H6, H7, H8);
      final ImmutableList<Square> squareListEast = constructListSquare();
      final ImmutableList<Square> squareListSouth = constructListSquare(H4, H3, H2, H1);
      final ImmutableList<Square> squareListWest = constructListSquare(G5, F5, E5, D5, C5, B5, A5);
      final ImmutableList<Square> squareListNorthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthWest = constructListSquare(G4, F3, E2, D1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(G6, F7, E8);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(H5, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(A7, A8);
      final ImmutableList<Square> squareListEast = constructListSquare(B6, C6, D6, E6, F6, G6, H6);
      final ImmutableList<Square> squareListSouth = constructListSquare(A5, A4, A3, A2, A1);
      final ImmutableList<Square> squareListWest = constructListSquare();
      final ImmutableList<Square> squareListNorthEast = constructListSquare(B7, C8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(B5, C4, D3, E2, F1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare();
      final ImmutableList<Square> squareListNorthWest = constructListSquare();
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(A6, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(B7, B8);
      final ImmutableList<Square> squareListEast = constructListSquare(C6, D6, E6, F6, G6, H6);
      final ImmutableList<Square> squareListSouth = constructListSquare(B5, B4, B3, B2, B1);
      final ImmutableList<Square> squareListWest = constructListSquare(A6);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(C7, D8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(C5, D4, E3, F2, G1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(A5);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(A7);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(B6, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(C7, C8);
      final ImmutableList<Square> squareListEast = constructListSquare(D6, E6, F6, G6, H6);
      final ImmutableList<Square> squareListSouth = constructListSquare(C5, C4, C3, C2, C1);
      final ImmutableList<Square> squareListWest = constructListSquare(B6, A6);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(D7, E8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(D5, E4, F3, G2, H1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(B5, A4);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(B7, A8);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(C6, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(D7, D8);
      final ImmutableList<Square> squareListEast = constructListSquare(E6, F6, G6, H6);
      final ImmutableList<Square> squareListSouth = constructListSquare(D5, D4, D3, D2, D1);
      final ImmutableList<Square> squareListWest = constructListSquare(C6, B6, A6);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(E7, F8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(E5, F4, G3, H2);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(C5, B4, A3);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(C7, B8);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(D6, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(E7, E8);
      final ImmutableList<Square> squareListEast = constructListSquare(F6, G6, H6);
      final ImmutableList<Square> squareListSouth = constructListSquare(E5, E4, E3, E2, E1);
      final ImmutableList<Square> squareListWest = constructListSquare(D6, C6, B6, A6);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(F7, G8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(F5, G4, H3);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(D5, C4, B3, A2);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(D7, C8);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(E6, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(F7, F8);
      final ImmutableList<Square> squareListEast = constructListSquare(G6, H6);
      final ImmutableList<Square> squareListSouth = constructListSquare(F5, F4, F3, F2, F1);
      final ImmutableList<Square> squareListWest = constructListSquare(E6, D6, C6, B6, A6);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(G7, H8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(G5, H4);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(E5, D4, C3, B2, A1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(E7, D8);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(F6, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(G7, G8);
      final ImmutableList<Square> squareListEast = constructListSquare(H6);
      final ImmutableList<Square> squareListSouth = constructListSquare(G5, G4, G3, G2, G1);
      final ImmutableList<Square> squareListWest = constructListSquare(F6, E6, D6, C6, B6, A6);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(H7);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(H5);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(F5, E4, D3, C2, B1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(F7, E8);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(G6, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(H7, H8);
      final ImmutableList<Square> squareListEast = constructListSquare();
      final ImmutableList<Square> squareListSouth = constructListSquare(H5, H4, H3, H2, H1);
      final ImmutableList<Square> squareListWest = constructListSquare(G6, F6, E6, D6, C6, B6, A6);
      final ImmutableList<Square> squareListNorthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthWest = constructListSquare(G5, F4, E3, D2, C1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(G7, F8);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(H6, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(A8);
      final ImmutableList<Square> squareListEast = constructListSquare(B7, C7, D7, E7, F7, G7, H7);
      final ImmutableList<Square> squareListSouth = constructListSquare(A6, A5, A4, A3, A2, A1);
      final ImmutableList<Square> squareListWest = constructListSquare();
      final ImmutableList<Square> squareListNorthEast = constructListSquare(B8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(B6, C5, D4, E3, F2, G1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare();
      final ImmutableList<Square> squareListNorthWest = constructListSquare();
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(A7, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(B8);
      final ImmutableList<Square> squareListEast = constructListSquare(C7, D7, E7, F7, G7, H7);
      final ImmutableList<Square> squareListSouth = constructListSquare(B6, B5, B4, B3, B2, B1);
      final ImmutableList<Square> squareListWest = constructListSquare(A7);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(C8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(C6, D5, E4, F3, G2, H1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(A6);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(A8);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(B7, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(C8);
      final ImmutableList<Square> squareListEast = constructListSquare(D7, E7, F7, G7, H7);
      final ImmutableList<Square> squareListSouth = constructListSquare(C6, C5, C4, C3, C2, C1);
      final ImmutableList<Square> squareListWest = constructListSquare(B7, A7);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(D8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(D6, E5, F4, G3, H2);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(B6, A5);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(B8);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(C7, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(D8);
      final ImmutableList<Square> squareListEast = constructListSquare(E7, F7, G7, H7);
      final ImmutableList<Square> squareListSouth = constructListSquare(D6, D5, D4, D3, D2, D1);
      final ImmutableList<Square> squareListWest = constructListSquare(C7, B7, A7);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(E8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(E6, F5, G4, H3);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(C6, B5, A4);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(C8);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(D7, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(E8);
      final ImmutableList<Square> squareListEast = constructListSquare(F7, G7, H7);
      final ImmutableList<Square> squareListSouth = constructListSquare(E6, E5, E4, E3, E2, E1);
      final ImmutableList<Square> squareListWest = constructListSquare(D7, C7, B7, A7);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(F8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(F6, G5, H4);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(D6, C5, B4, A3);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(D8);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(E7, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(F8);
      final ImmutableList<Square> squareListEast = constructListSquare(G7, H7);
      final ImmutableList<Square> squareListSouth = constructListSquare(F6, F5, F4, F3, F2, F1);
      final ImmutableList<Square> squareListWest = constructListSquare(E7, D7, C7, B7, A7);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(G8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(G6, H5);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(E6, D5, C4, B3, A2);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(E8);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(F7, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(G8);
      final ImmutableList<Square> squareListEast = constructListSquare(H7);
      final ImmutableList<Square> squareListSouth = constructListSquare(G6, G5, G4, G3, G2, G1);
      final ImmutableList<Square> squareListWest = constructListSquare(F7, E7, D7, C7, B7, A7);
      final ImmutableList<Square> squareListNorthEast = constructListSquare(H8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(H6);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(F6, E5, D4, C3, B2, A1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(F8);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(G7, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare(H8);
      final ImmutableList<Square> squareListEast = constructListSquare();
      final ImmutableList<Square> squareListSouth = constructListSquare(H6, H5, H4, H3, H2, H1);
      final ImmutableList<Square> squareListWest = constructListSquare(G7, F7, E7, D7, C7, B7, A7);
      final ImmutableList<Square> squareListNorthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthWest = constructListSquare(G6, F5, E4, D3, C2, B1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(G8);
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(H7, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare();
      final ImmutableList<Square> squareListEast = constructListSquare(B8, C8, D8, E8, F8, G8, H8);
      final ImmutableList<Square> squareListSouth = constructListSquare(A7, A6, A5, A4, A3, A2, A1);
      final ImmutableList<Square> squareListWest = constructListSquare();
      final ImmutableList<Square> squareListNorthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthEast = constructListSquare(B7, C6, D5, E4, F3, G2, H1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare();
      final ImmutableList<Square> squareListNorthWest = constructListSquare();
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(A8, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare();
      final ImmutableList<Square> squareListEast = constructListSquare(C8, D8, E8, F8, G8, H8);
      final ImmutableList<Square> squareListSouth = constructListSquare(B7, B6, B5, B4, B3, B2, B1);
      final ImmutableList<Square> squareListWest = constructListSquare(A8);
      final ImmutableList<Square> squareListNorthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthEast = constructListSquare(C7, D6, E5, F4, G3, H2);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(A7);
      final ImmutableList<Square> squareListNorthWest = constructListSquare();
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(B8, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare();
      final ImmutableList<Square> squareListEast = constructListSquare(D8, E8, F8, G8, H8);
      final ImmutableList<Square> squareListSouth = constructListSquare(C7, C6, C5, C4, C3, C2, C1);
      final ImmutableList<Square> squareListWest = constructListSquare(B8, A8);
      final ImmutableList<Square> squareListNorthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthEast = constructListSquare(D7, E6, F5, G4, H3);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(B7, A6);
      final ImmutableList<Square> squareListNorthWest = constructListSquare();
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(C8, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare();
      final ImmutableList<Square> squareListEast = constructListSquare(E8, F8, G8, H8);
      final ImmutableList<Square> squareListSouth = constructListSquare(D7, D6, D5, D4, D3, D2, D1);
      final ImmutableList<Square> squareListWest = constructListSquare(C8, B8, A8);
      final ImmutableList<Square> squareListNorthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthEast = constructListSquare(E7, F6, G5, H4);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(C7, B6, A5);
      final ImmutableList<Square> squareListNorthWest = constructListSquare();
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(D8, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare();
      final ImmutableList<Square> squareListEast = constructListSquare(F8, G8, H8);
      final ImmutableList<Square> squareListSouth = constructListSquare(E7, E6, E5, E4, E3, E2, E1);
      final ImmutableList<Square> squareListWest = constructListSquare(D8, C8, B8, A8);
      final ImmutableList<Square> squareListNorthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthEast = constructListSquare(F7, G6, H5);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(D7, C6, B5, A4);
      final ImmutableList<Square> squareListNorthWest = constructListSquare();
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(E8, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare();
      final ImmutableList<Square> squareListEast = constructListSquare(G8, H8);
      final ImmutableList<Square> squareListSouth = constructListSquare(F7, F6, F5, F4, F3, F2, F1);
      final ImmutableList<Square> squareListWest = constructListSquare(E8, D8, C8, B8, A8);
      final ImmutableList<Square> squareListNorthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthEast = constructListSquare(G7, H6);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(E7, D6, C5, B4, A3);
      final ImmutableList<Square> squareListNorthWest = constructListSquare();
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(F8, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare();
      final ImmutableList<Square> squareListEast = constructListSquare(H8);
      final ImmutableList<Square> squareListSouth = constructListSquare(G7, G6, G5, G4, G3, G2, G1);
      final ImmutableList<Square> squareListWest = constructListSquare(F8, E8, D8, C8, B8, A8);
      final ImmutableList<Square> squareListNorthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthEast = constructListSquare(H7);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(F7, E6, D5, C4, B3, A2);
      final ImmutableList<Square> squareListNorthWest = constructListSquare();
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(G8, queenRange);
    }

    {
      final ImmutableList<Square> squareListNorth = constructListSquare();
      final ImmutableList<Square> squareListEast = constructListSquare();
      final ImmutableList<Square> squareListSouth = constructListSquare(H7, H6, H5, H4, H3, H2, H1);
      final ImmutableList<Square> squareListWest = constructListSquare(G8, F8, E8, D8, C8, B8, A8);
      final ImmutableList<Square> squareListNorthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthWest = constructListSquare(G7, F6, E5, D4, C3, B2, A1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare();
      final QueenRange queenRange = new QueenRange(squareListNorth, squareListEast, squareListSouth, squareListWest,
          squareListNorthEast, squareListSouthEast, squareListSouthWest, squareListNorthWest);
      queenMap.put(H8, queenRange);
    }

    QUEEN_SQUARES = NonNullWrapperCommon.copyOfMap(queenMap);

    ValidateMoveNumberUtility.validateOrthogonalMoveNumber(QUEEN_SQUARES, 896);
    ValidateMoveNumberUtility.validateDiagonalMovesNumber(QUEEN_SQUARES, 560);
  }

  // END generated code

  public static QueenRange getQueenSquares(Square fromSquare) {
    return NonNullWrapperCommon.get(QUEEN_SQUARES, fromSquare);
  }

}
