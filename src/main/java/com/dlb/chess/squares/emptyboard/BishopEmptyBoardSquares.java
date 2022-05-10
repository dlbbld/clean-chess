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

  private static final ImmutableMap<Square, BishopRange> BISHOP_SQUARES;

  static {
    final EnumMap<Square, BishopRange> bishopMap = NonNullWrapperCommon.newEnumMap(Square.class);

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(B2, C3, D4, E5, F6, G7, H8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthWest = constructListSquare();
      final ImmutableList<Square> squareListNorthWest = constructListSquare();
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(A1, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(C2, D3, E4, F5, G6, H7);
      final ImmutableList<Square> squareListSouthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthWest = constructListSquare();
      final ImmutableList<Square> squareListNorthWest = constructListSquare(A2);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(B1, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(D2, E3, F4, G5, H6);
      final ImmutableList<Square> squareListSouthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthWest = constructListSquare();
      final ImmutableList<Square> squareListNorthWest = constructListSquare(B2, A3);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(C1, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(E2, F3, G4, H5);
      final ImmutableList<Square> squareListSouthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthWest = constructListSquare();
      final ImmutableList<Square> squareListNorthWest = constructListSquare(C2, B3, A4);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(D1, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(F2, G3, H4);
      final ImmutableList<Square> squareListSouthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthWest = constructListSquare();
      final ImmutableList<Square> squareListNorthWest = constructListSquare(D2, C3, B4, A5);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(E1, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(G2, H3);
      final ImmutableList<Square> squareListSouthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthWest = constructListSquare();
      final ImmutableList<Square> squareListNorthWest = constructListSquare(E2, D3, C4, B5, A6);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(F1, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(H2);
      final ImmutableList<Square> squareListSouthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthWest = constructListSquare();
      final ImmutableList<Square> squareListNorthWest = constructListSquare(F2, E3, D4, C5, B6, A7);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(G1, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthWest = constructListSquare();
      final ImmutableList<Square> squareListNorthWest = constructListSquare(G2, F3, E4, D5, C6, B7, A8);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(H1, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(B3, C4, D5, E6, F7, G8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(B1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare();
      final ImmutableList<Square> squareListNorthWest = constructListSquare();
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(A2, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(C3, D4, E5, F6, G7, H8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(C1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(A1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(A3);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(B2, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(D3, E4, F5, G6, H7);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(D1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(B1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(B3, A4);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(C2, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(E3, F4, G5, H6);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(E1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(C1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(C3, B4, A5);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(D2, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(F3, G4, H5);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(F1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(D1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(D3, C4, B5, A6);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(E2, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(G3, H4);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(G1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(E1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(E3, D4, C5, B6, A7);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(F2, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(H3);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(H1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(F1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(F3, E4, D5, C6, B7, A8);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(G2, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthWest = constructListSquare(G1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(G3, F4, E5, D6, C7, B8);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(H2, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(B4, C5, D6, E7, F8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(B2, C1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare();
      final ImmutableList<Square> squareListNorthWest = constructListSquare();
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(A3, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(C4, D5, E6, F7, G8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(C2, D1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(A2);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(A4);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(B3, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(D4, E5, F6, G7, H8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(D2, E1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(B2, A1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(B4, A5);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(C3, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(E4, F5, G6, H7);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(E2, F1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(C2, B1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(C4, B5, A6);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(D3, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(F4, G5, H6);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(F2, G1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(D2, C1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(D4, C5, B6, A7);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(E3, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(G4, H5);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(G2, H1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(E2, D1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(E4, D5, C6, B7, A8);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(F3, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(H4);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(H2);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(F2, E1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(F4, E5, D6, C7, B8);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(G3, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthWest = constructListSquare(G2, F1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(G4, F5, E6, D7, C8);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(H3, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(B5, C6, D7, E8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(B3, C2, D1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare();
      final ImmutableList<Square> squareListNorthWest = constructListSquare();
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(A4, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(C5, D6, E7, F8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(C3, D2, E1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(A3);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(A5);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(B4, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(D5, E6, F7, G8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(D3, E2, F1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(B3, A2);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(B5, A6);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(C4, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(E5, F6, G7, H8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(E3, F2, G1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(C3, B2, A1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(C5, B6, A7);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(D4, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(F5, G6, H7);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(F3, G2, H1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(D3, C2, B1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(D5, C6, B7, A8);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(E4, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(G5, H6);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(G3, H2);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(E3, D2, C1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(E5, D6, C7, B8);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(F4, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(H5);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(H3);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(F3, E2, D1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(F5, E6, D7, C8);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(G4, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthWest = constructListSquare(G3, F2, E1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(G5, F6, E7, D8);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(H4, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(B6, C7, D8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(B4, C3, D2, E1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare();
      final ImmutableList<Square> squareListNorthWest = constructListSquare();
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(A5, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(C6, D7, E8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(C4, D3, E2, F1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(A4);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(A6);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(B5, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(D6, E7, F8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(D4, E3, F2, G1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(B4, A3);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(B6, A7);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(C5, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(E6, F7, G8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(E4, F3, G2, H1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(C4, B3, A2);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(C6, B7, A8);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(D5, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(F6, G7, H8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(F4, G3, H2);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(D4, C3, B2, A1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(D6, C7, B8);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(E5, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(G6, H7);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(G4, H3);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(E4, D3, C2, B1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(E6, D7, C8);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(F5, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(H6);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(H4);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(F4, E3, D2, C1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(F6, E7, D8);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(G5, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthWest = constructListSquare(G4, F3, E2, D1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(G6, F7, E8);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(H5, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(B7, C8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(B5, C4, D3, E2, F1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare();
      final ImmutableList<Square> squareListNorthWest = constructListSquare();
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(A6, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(C7, D8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(C5, D4, E3, F2, G1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(A5);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(A7);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(B6, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(D7, E8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(D5, E4, F3, G2, H1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(B5, A4);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(B7, A8);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(C6, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(E7, F8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(E5, F4, G3, H2);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(C5, B4, A3);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(C7, B8);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(D6, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(F7, G8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(F5, G4, H3);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(D5, C4, B3, A2);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(D7, C8);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(E6, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(G7, H8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(G5, H4);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(E5, D4, C3, B2, A1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(E7, D8);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(F6, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(H7);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(H5);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(F5, E4, D3, C2, B1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(F7, E8);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(G6, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthWest = constructListSquare(G5, F4, E3, D2, C1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(G7, F8);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(H6, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(B8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(B6, C5, D4, E3, F2, G1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare();
      final ImmutableList<Square> squareListNorthWest = constructListSquare();
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(A7, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(C8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(C6, D5, E4, F3, G2, H1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(A6);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(A8);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(B7, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(D8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(D6, E5, F4, G3, H2);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(B6, A5);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(B8);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(C7, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(E8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(E6, F5, G4, H3);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(C6, B5, A4);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(C8);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(D7, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(F8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(F6, G5, H4);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(D6, C5, B4, A3);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(D8);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(E7, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(G8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(G6, H5);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(E6, D5, C4, B3, A2);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(E8);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(F7, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare(H8);
      final ImmutableList<Square> squareListSouthEast = constructListSquare(H6);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(F6, E5, D4, C3, B2, A1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(F8);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(G7, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthWest = constructListSquare(G6, F5, E4, D3, C2, B1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare(G8);
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(H7, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthEast = constructListSquare(B7, C6, D5, E4, F3, G2, H1);
      final ImmutableList<Square> squareListSouthWest = constructListSquare();
      final ImmutableList<Square> squareListNorthWest = constructListSquare();
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(A8, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthEast = constructListSquare(C7, D6, E5, F4, G3, H2);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(A7);
      final ImmutableList<Square> squareListNorthWest = constructListSquare();
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(B8, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthEast = constructListSquare(D7, E6, F5, G4, H3);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(B7, A6);
      final ImmutableList<Square> squareListNorthWest = constructListSquare();
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(C8, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthEast = constructListSquare(E7, F6, G5, H4);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(C7, B6, A5);
      final ImmutableList<Square> squareListNorthWest = constructListSquare();
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(D8, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthEast = constructListSquare(F7, G6, H5);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(D7, C6, B5, A4);
      final ImmutableList<Square> squareListNorthWest = constructListSquare();
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(E8, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthEast = constructListSquare(G7, H6);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(E7, D6, C5, B4, A3);
      final ImmutableList<Square> squareListNorthWest = constructListSquare();
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(F8, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthEast = constructListSquare(H7);
      final ImmutableList<Square> squareListSouthWest = constructListSquare(F7, E6, D5, C4, B3, A2);
      final ImmutableList<Square> squareListNorthWest = constructListSquare();
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(G8, bishopRange);
    }

    {
      final ImmutableList<Square> squareListNorthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthEast = constructListSquare();
      final ImmutableList<Square> squareListSouthWest = constructListSquare(G7, F6, E5, D4, C3, B2, A1);
      final ImmutableList<Square> squareListNorthWest = constructListSquare();
      final BishopRange bishopRange = new BishopRange(squareListNorthEast, squareListSouthEast, squareListSouthWest,
          squareListNorthWest);
      bishopMap.put(H8, bishopRange);
    }

    BISHOP_SQUARES = NonNullWrapperCommon.immutableEnumMap(bishopMap);

    ValidateMoveNumberUtility.validateDiagonalMovesNumber(BISHOP_SQUARES, 560);

  }

  // END generated code

  public static BishopRange getBishopSquares(Square fromSquare) {
    return NonNullWrapperCommon.get(BISHOP_SQUARES, fromSquare);
  }

}
