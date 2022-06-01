package com.dlb.chess.test.unwinnability;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.unwinnability.mobility.Mobility;
import com.dlb.chess.unwinnability.mobility.model.MobilitySolution;
import com.dlb.chess.unwinnability.model.PiecePlacement;

public class TestMobilitySolution implements EnumConstants {

  private static final Set<Square> ALL_SQUARE_SET = new TreeSet<>(Square.BOARD_SQUARE_LIST);
  private static final Set<Square> LIGHT_SQUARE_SET;
  private static final Set<Square> DARK_SQUARE_SET;

  static {
    LIGHT_SQUARE_SET = new TreeSet<>();
    DARK_SQUARE_SET = new TreeSet<>();
    for (final Square square : Square.BOARD_SQUARE_LIST) {
      switch (square.getSquareType()) {
        case LIGHT_SQUARE:
          LIGHT_SQUARE_SET.add(square);
          break;
        case DARK_SQUARE:
          DARK_SQUARE_SET.add(square);
          break;
        case NONE:
        default:
          throw new IllegalArgumentException();
      }
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testBasicAllMaximum() throws Exception {
    // KvK
    checkAllMaximum("8/8/4k3/8/8/2K5/8/8 w - - 0 100");
    checkAllMaximum("8/8/4k3/8/8/2K5/8/8 b - - 0 100");

    // KRvK
    checkAllMaximum("8/8/4k3/8/6R1/2K5/8/8 w - - 0 100");
    checkAllMaximum("8/8/4k3/8/6R1/2K5/8/8 b - - 0 100");

    // KvKR
    checkAllMaximum("8/4r3/4k3/8/4K3/8/8/8 w - - 0 100");
    checkAllMaximum("8/4r3/4k3/8/4K3/8/8/8 b - - 0 100");

    // KNvK
    checkAllMaximum("8/8/4k3/8/8/2K1N3/8/8 w - - 0 100");
    checkAllMaximum("8/8/4k3/8/8/2K1N3/8/8 b - - 0 100");

    // KvKN
    checkAllMaximum("8/8/4k1n1/8/8/2K5/8/8 w - - 0 100");
    checkAllMaximum("8/8/4k1n1/8/8/2K5/8/8 b - - 0 100");

    // KQvK
    checkAllMaximum("8/8/4k3/8/4K3/4Q3/8/8 w - - 0 100");
    checkAllMaximum("8/8/4k3/8/4K3/4Q3/8/8 b - - 0 100");

    // KvKQ
    checkAllMaximum("1q6/8/4k3/8/4K3/8/8/8 w - - 0 100");
    checkAllMaximum("1q6/8/4k3/8/4K3/8/8/8 b - - 0 100");

    // KPvK
    checkAllMaximum("8/8/4k3/8/4K3/1P6/8/8 w - - 0 100");
    checkAllMaximum("8/8/4k3/8/4K3/1P6/8/8 b - - 0 100");

    // KvKP
    checkAllMaximum("8/4p3/4k3/8/4K3/8/8/8 w - - 0 100");
    checkAllMaximum("8/4p3/4k3/8/4K3/8/8/8 b - - 0 100");

    // KB'vK
    checkAllMaximum("4B3/8/4k3/8/4K3/8/8/8 w - - 0 100");
    checkAllMaximum("4B3/8/4k3/8/4K3/8/8/8 b - - 0 100");

    // KvKB'
    checkAllMaximum("8/8/b3k3/8/4K3/8/8/8 w - - 0 100");
    checkAllMaximum("8/8/b3k3/8/4K3/8/8/8 b - - 0 100");

    // KB''vK
    checkAllMaximum("8/8/4k3/8/4K3/8/8/B7 w - - 0 100");
    checkAllMaximum("8/8/4k3/8/4K3/8/8/B7 b - - 0 100");

    // KvKB''
    checkAllMaximum("7b/8/4k3/8/4K3/8/8/8 w - - 0 100");
    checkAllMaximum("7b/8/4k3/8/4K3/8/8/8 b - - 0 100");

    // special positions
    checkAllMaximum("1k6/1P5p/BP3p2/1P6/8/8/5PKP/8 w - - 0 100");
    checkAllMaximum("1k6/1P5p/BP3p2/1P6/8/8/5PKP/8 b - - 0 100");

    checkAllMaximum("2b5/1p6/pPp3k1/2Pp3p/P2PpBpP/4P1P1/5K2/8 w - - 0 100");
    checkAllMaximum("2b5/1p6/pPp3k1/2Pp3p/P2PpBpP/4P1P1/5K2/8 b - - 0 100");

  }

  @SuppressWarnings("static-method")
  @Test
  void testPawnWall() throws Exception {
    {
      final List<Square> whiteKingToSquareList = NonNullWrapperCommon.asList(A1, B1, C1, D1, E1, F1, G1, H1, A2, B2, C2,
          D2, E2, F2, G2, H2, A3, B3, C3, D3, E3, F3, G3, H3);
      final List<Square> blackKingToSquareList = NonNullWrapperCommon.asList(A6, B6, C6, D6, E6, F6, G6, H6, A7, B7, C7,
          D7, E7, F7, G7, H7, A8, B8, C8, D8, E8, F8, G8, H8);

      checkPawnWallOnlyKingAndPawn("4k3/8/8/p1p1p1p1/P1P1P1P1/8/8/4K3 w - - 0 50", whiteKingToSquareList,
          blackKingToSquareList);

      checkPawnWallOnlyKingAndPawn("4k3/8/8/p1p1p1p1/P1P1P1P1/8/8/4K3 b - - 0 50", whiteKingToSquareList,
          blackKingToSquareList);
    }

    {
      final List<Square> whiteKingToSquareList = NonNullWrapperCommon.asList(A1, B1, C1, D1, E1, F1, G1, H1, A2, B2, C2,
          D2, E2, F2, G2, H2, E3, F3, G3, H3, F4, G4, H4);
      final List<Square> blackKingToSquareList = NonNullWrapperCommon.asList(A6, B6, C6, A7, B7, C7, D7, A8, B8, C8, D8,
          E8, F8, G8, H8);
      checkPawnWallOnlyKingAndPawn("4k3/5p1p/4pPpP/1p1pP1P1/pPpP4/P1P5/8/4K3 w - - 0 50", whiteKingToSquareList,
          blackKingToSquareList);

      checkPawnWallOnlyKingAndPawn("4k3/5p1p/4pPpP/1p1pP1P1/pPpP4/P1P5/8/4K3 b - - 0 50", whiteKingToSquareList,
          blackKingToSquareList);
    }

    {
      final List<Square> whiteKingToSquareList = NonNullWrapperCommon.asList(A1, B1, C1, D1, E1, F1, G1, H1, A2, B2, C2,
          D2, E2, F2, G2, H2, E3, F3, G3, H3, F4, G4, H4, G5, H5);
      final List<Square> blackKingToSquareList = NonNullWrapperCommon.asList(A5, B5, A6, B6, C6, A7, B7, C7, D7, A8, B8,
          C8, D8, E8, F8, G8, H8);

      checkPawnWallOnlyKingAndPawn("4k3/5p1p/4pP1P/3pP3/p1pP4/P1P5/8/4K3 w - - 0 50", whiteKingToSquareList,
          blackKingToSquareList);

      checkPawnWallOnlyKingAndPawn("4k3/5p1p/4pP1P/3pP3/p1pP4/P1P5/8/4K3 b - - 0 50", whiteKingToSquareList,
          blackKingToSquareList);
    }

  }

  @SuppressWarnings("static-method")
  @Test
  void testPseudoPawnWall() throws Exception {
    checkAllMaximum("8/8/3p4/4p2k/4P3/3P4/6K1/8 b - - 2 41");
  }

  @SuppressWarnings("static-method")
  @Test
  void testAllLockedButKing() throws Exception {

    {
      final List<Square> whiteKingToSquareList = NonNullWrapperCommon.asList(A1, B1, C1, D1, E1, F1, G1, H1, E2, F2, G2,
          H2, D3, E3, F3, F3, G3, H3, E4, F4, G4, H4, B5, D5, E5, F5, G5, H5, A6, B6, C6, D6, E6, F6, G6, H6, A7, B7,
          C7, D7, E7, F7, G7, H7, A8, B8, C8, D8, E8, F8, G8, H8);
      final List<Square> blackKingToSquareList = NonNullWrapperCommon.asList(A1, B1, C1, D1, E1, F1, G1, H1, B2, D2, E2,
          F2, G2, H2, E3, F3, G3, H3, D4, E4, F4, G4, H4, E5, F5, G5, H5, A6, B6, C6, D6, E6, F6, G6, H6, A7, B7, C7,
          D7, E7, F7, G7, H7, A8, B8, C8, D8, E8, F8, G8, H8);

      checkAllLockedButKing("8/8/8/p1p4k/PbP5/pBp5/P1P3K1/8 w - - 2 41", whiteKingToSquareList, blackKingToSquareList);

      checkAllLockedButKing("8/8/8/p1p4k/PbP5/pBp5/P1P3K1/8 b - - 2 41", whiteKingToSquareList, blackKingToSquareList);
    }

    {
      final List<Square> whiteKingToSquareList = NonNullWrapperCommon.asList(A1, B1, C1, D1, E1, F1, G1, H1, A2, B2, C2,
          D2, E2, F2, G2, H2, A3, B3, C3, D3, E3, F3, G3, H3, A4, B4, C4, D4, E4, F4, G4, H4, A5, B5, C5, D5);
      final List<Square> blackKingToSquareList = NonNullWrapperCommon.asList(A8, B8, C8, D8, E8, F8, G7, H7, H8);

      checkAllLockedButKing("3k2n1/1p2p3/1P2Pp1p/5P1P/1K6/8/8/8 w - - 0 100", whiteKingToSquareList,
          blackKingToSquareList);

      checkAllLockedButKing("3k2n1/1p2p3/1P2Pp1p/5P1P/1K6/8/8/8 b - - 0 100", whiteKingToSquareList,
          blackKingToSquareList);
    }

    {
      final List<Square> whiteKingToSquareList = NonNullWrapperCommon.asList(A1, B1, C1, D1, E1, F1, G1, H1, A2, B2, C2,
          D2, E2, F2, G2, H2, A3, B3, C3, D3, E3, F3, G3, H3, D4, E4, F4, G4, H4);
      final List<Square> blackKingToSquareList = NonNullWrapperCommon.asList(A8, B8, C8, D8, E8, F8, G7, H7, H8);

      checkAllLockedButKing("3k2n1/1p2p3/1Pp1Pp1p/BpP2P1P/1P6/3K4/8/8 w - - 0 100", whiteKingToSquareList,
          blackKingToSquareList);

      checkAllLockedButKing("3k2n1/1p2p3/1Pp1Pp1p/BpP2P1P/1P6/3K4/8/8 b - - 0 100", whiteKingToSquareList,
          blackKingToSquareList);
    }

  }

  @SuppressWarnings("static-method")
  @Test
  void testCustom() throws Exception {

    {
      final Map<Square, List<Square>> mobilityMap = new HashMap<>();

      final List<Square> whiteKingToSquareList = NonNullWrapperCommon.asList(A1, B1, C1, D1, E1, F1, G1, H1, A2, B2, C2,
          D2, E2, F2, G2, H2, A3, B3, C3, D3, E3, F3, G3, H3, A4, B4, C4, D4, E4, F4, G4, H4, A5, C5, D5, E5, F5, G5,
          H5, C6, D6, E6, F6, G6, H6, D7, E7, F7, G7, H7, D8, E8, F8, G8, H8);
      mobilityMap.put(G2, whiteKingToSquareList);

      mobilityMap.put(B5, NonNullWrapperCommon.asList(B5));
      mobilityMap.put(A6, NonNullWrapperCommon.asList(A6));
      mobilityMap.put(B6, NonNullWrapperCommon.asList(B6));
      mobilityMap.put(B7, NonNullWrapperCommon.asList(B7));

      mobilityMap.put(B8, NonNullWrapperCommon.asList(B8));

      checkCustom("1k6/1P6/BP6/1P6/8/8/6K1/8 w - - 0 100", mobilityMap);

      checkCustom("1k6/1P6/BP6/1P6/8/8/6K1/8 b - - 0 100", mobilityMap);
    }

    {
      final Map<Square, List<Square>> mobilityMap = new HashMap<>();

      final List<Square> whiteKingToSquareList = NonNullWrapperCommon.asList(A1, B1, C1, D1, E1, F1, G1, H1, A2, B2, C2,
          D2, E2, F2, G2, H2, A3, B3, C3, A4, B4);
      mobilityMap.put(F2, whiteKingToSquareList);

      mobilityMap.put(E3, NonNullWrapperCommon.asList(E3));
      mobilityMap.put(G3, NonNullWrapperCommon.asList(G3));

      mobilityMap.put(D4, NonNullWrapperCommon.asList(D4));
      mobilityMap.put(E4, NonNullWrapperCommon.asList(E4));

      mobilityMap.put(F4, NonNullWrapperCommon.asList(F4, E5, G5, D6, F6, H6, A7, C7, E7, G7, B8, D8, F8, H8));

      mobilityMap.put(G4, NonNullWrapperCommon.asList(G4));
      mobilityMap.put(H4, NonNullWrapperCommon.asList(H4));

      mobilityMap.put(A5, NonNullWrapperCommon.asList(A5));

      mobilityMap.put(C5, NonNullWrapperCommon.asList(C5));
      mobilityMap.put(D5, NonNullWrapperCommon.asList(D5));
      mobilityMap.put(H5, NonNullWrapperCommon.asList(H5));

      mobilityMap.put(A6, NonNullWrapperCommon.asList(A6));
      mobilityMap.put(B6, NonNullWrapperCommon.asList(B6));
      mobilityMap.put(C6, NonNullWrapperCommon.asList(C6));

      final List<Square> blackKingToSquareList = NonNullWrapperCommon.asList(F5, E6, F6, G6, H6, D7, E7, F7, G7, H7, A8,
          B8, C8, D8, E8, F8, G8, H8);
      mobilityMap.put(G6, blackKingToSquareList);

      mobilityMap.put(B7, NonNullWrapperCommon.asList(B7));

      mobilityMap.put(C8, NonNullWrapperCommon.asList(F5, E6, G6, D7, F7, H7, C8, E8, G8));

      checkCustom("2b5/1p6/pPp3k1/P1Pp3p/3PpBpP/4P1P1/5K2/8 w - - 0 100", mobilityMap);

      checkCustom("2b5/1p6/pPp3k1/P1Pp3p/3PpBpP/4P1P1/5K2/8 b - - 0 100", mobilityMap);
    }

    {
      final Map<Square, List<Square>> mobilityMap = new HashMap<>();

      final List<Square> whiteKingToSquareList = NonNullWrapperCommon.asList(A1, B1, C1, D1, E1, F1, G1, H1, A2, B2, C2,
          D2, E2, F2, G2, H2, A3, B3, C3, D3, E3, F3, G3, H3, A4, B4, C4, D4, E4, F4, G4, H4, A5, B5, C5, D5, E5, F5,
          G5, H5);
      mobilityMap.put(E1, whiteKingToSquareList);

      mobilityMap.put(A2, NonNullWrapperCommon.asList(A2, A3, A4, A5));
      mobilityMap.put(C2, NonNullWrapperCommon.asList(C2, C3, C4, C5));
      mobilityMap.put(E2, NonNullWrapperCommon.asList(E2, E3, E4, E5));
      mobilityMap.put(G2, NonNullWrapperCommon.asList(G2, G3, G4, G5));

      mobilityMap.put(A5, NonNullWrapperCommon.asList(A5, A4, A3));
      mobilityMap.put(C5, NonNullWrapperCommon.asList(C5, C4, C3));
      mobilityMap.put(E5, NonNullWrapperCommon.asList(E5, E4, E3));
      mobilityMap.put(G5, NonNullWrapperCommon.asList(G5, G4, G3));

      mobilityMap.put(A6, NonNullWrapperCommon.asList(A6));
      mobilityMap.put(C6, NonNullWrapperCommon.asList(C6));
      mobilityMap.put(E6, NonNullWrapperCommon.asList(E6));
      mobilityMap.put(G6, NonNullWrapperCommon.asList(G6));

      mobilityMap.put(A7, NonNullWrapperCommon.asList(A7));
      mobilityMap.put(C7, NonNullWrapperCommon.asList(C7));
      mobilityMap.put(E7, NonNullWrapperCommon.asList(E7));
      mobilityMap.put(G7, NonNullWrapperCommon.asList(G7));

      final List<Square> blackKingToSquareList = NonNullWrapperCommon.asList(A8, B8, C8, D8, E8, F8, G8, H8);
      mobilityMap.put(B8, blackKingToSquareList);

      checkCustom("1k6/p1p1p1p1/P1P1P1P1/p1p1p1p1/8/8/P1P1P1P1/4K3 w - - 10 100", mobilityMap);

      checkCustom("1k6/p1p1p1p1/P1P1P1P1/p1p1p1p1/8/8/P1P1P1P1/4K3 b - - 10 100", mobilityMap);
    }

    {
      final Map<Square, List<Square>> mobilityMap = new HashMap<>();

      final List<Square> whiteKingToSquareList = NonNullWrapperCommon.asList(G1);
      mobilityMap.put(G1, whiteKingToSquareList);

      mobilityMap.put(A2, NonNullWrapperCommon.asList(A2, A3, A4, A5, A6));
      mobilityMap.put(C2, NonNullWrapperCommon.asList(C2, C3, C4, C5, C6));
      mobilityMap.put(E2, NonNullWrapperCommon.asList(E2, E3, E4, E5, E6));
      mobilityMap.put(G2, NonNullWrapperCommon.asList(G2));

      mobilityMap.put(G3, NonNullWrapperCommon.asList(G3));
      mobilityMap.put(H3, NonNullWrapperCommon.asList(H3));

      mobilityMap.put(G4, NonNullWrapperCommon.asList(G4));

      mobilityMap.put(G5, NonNullWrapperCommon.asList(G5));

      mobilityMap.put(G6, NonNullWrapperCommon.asList(G6));
      mobilityMap.put(H6, NonNullWrapperCommon.asList(H6));

      mobilityMap.put(A7, NonNullWrapperCommon.asList(A7, A6, A5, A4, A3));
      mobilityMap.put(C7, NonNullWrapperCommon.asList(C7, C6, C5, C4, C3));
      mobilityMap.put(E7, NonNullWrapperCommon.asList(E7, E6, E5, E4, E3));
      mobilityMap.put(G7, NonNullWrapperCommon.asList(G7));

      final List<Square> blackKingToSquareList = NonNullWrapperCommon.asList(G8);
      mobilityMap.put(G8, blackKingToSquareList);

      checkCustom("6k1/p1p1p1P1/6PB/6P1/6p1/6pb/P1P1P1p1/6K1 w - - 0 100", mobilityMap);

      checkCustom("6k1/p1p1p1P1/6PB/6P1/6p1/6pb/P1P1P1p1/6K1 b - - 0 100", mobilityMap);
    }

  }

  private static void checkCustom(String fen, Map<Square, List<Square>> mobilityMap) {
    final Board board = new Board(fen);
    final MobilitySolution mobilitySolution = Mobility.mobility(board);

    for (final PiecePlacement piecePlacement : mobilitySolution.getPiecePlacementSet()) {
      final Set<Square> squaresWithValueOne = mobilitySolution.calculateSquaresWithValueOne(piecePlacement);
      assertTrue(mobilityMap.containsKey(piecePlacement.squareOriginal()));
      final List<Square> expectedSquareList = NonNullWrapperCommon.get(mobilityMap, piecePlacement.squareOriginal());
      final Set<Square> expectedSquareSet = new TreeSet<>(expectedSquareList);
      assertEquals(expectedSquareSet, squaresWithValueOne);
    }

  }

  private static void checkAllLockedButKing(String fen, List<Square> whiteKingToSquareList,
      List<Square> blackKingToSquareList) {
    final Board board = new Board(fen);

    final MobilitySolution mobilitySolution = Mobility.mobility(board);

    for (final PiecePlacement piecePlacement : mobilitySolution.getPiecePlacementSet()) {
      final Set<Square> squaresWithValueOne = mobilitySolution.calculateSquaresWithValueOne(piecePlacement);
      switch (piecePlacement.pieceType()) {
        case PAWN:
        case ROOK:
        case KNIGHT:
        case BISHOP:
        case QUEEN:
          assertEquals(1, squaresWithValueOne.size());
          final Square onlyToSquare = NonNullWrapperCommon.getFirst(new ArrayList<>(squaresWithValueOne));
          assertEquals(piecePlacement.squareOriginal(), onlyToSquare);
          break;
        case KING:
          switch (piecePlacement.side()) {
            case WHITE:
              assertEquals(new TreeSet<>(whiteKingToSquareList), squaresWithValueOne);
              break;
            case BLACK:
              assertEquals(new TreeSet<>(blackKingToSquareList), squaresWithValueOne);
              break;
            case NONE:
            default:
              throw new IllegalArgumentException();
          }
          break;
        case NONE:
        default:
          throw new IllegalArgumentException();
      }
    }
  }

  private static void checkPawnWallOnlyKingAndPawn(String fen, List<Square> whiteKingToSquareList,
      List<Square> blackKingToSquareList) {
    final Board board = new Board(fen);

    final MobilitySolution mobilitySolution = Mobility.mobility(board);

    for (final PiecePlacement piecePlacement : mobilitySolution.getPiecePlacementSet()) {
      final Set<Square> squaresWithValueOne = mobilitySolution.calculateSquaresWithValueOne(piecePlacement);
      switch (piecePlacement.pieceType()) {
        case PAWN:
          assertEquals(1, squaresWithValueOne.size());
          final Square onlyToSquare = NonNullWrapperCommon.getFirst(new ArrayList<>(squaresWithValueOne));
          assertEquals(piecePlacement.squareOriginal(), onlyToSquare);
          break;
        case KING:
          switch (piecePlacement.side()) {
            case WHITE:
              assertEquals(new TreeSet<>(whiteKingToSquareList), squaresWithValueOne);
              break;
            case BLACK:
              assertEquals(new TreeSet<>(blackKingToSquareList), squaresWithValueOne);
              break;
            case NONE:
            default:
              throw new IllegalArgumentException();
          }
          break;
        case ROOK:
        case KNIGHT:
        case BISHOP:
        case QUEEN:
        case NONE:
        default:
          throw new IllegalArgumentException();
      }
    }
  }

  private static void checkAllMaximum(String fen) {
    final Board board = new Board(fen);

    final MobilitySolution mobilitySolution = Mobility.mobility(board);

    checkBoardOccupation(board.getStaticPosition(), mobilitySolution);

    for (final PiecePlacement piecePlacement : mobilitySolution.getPiecePlacementSet()) {
      final Set<Square> squaresWithValueOne = mobilitySolution.calculateSquaresWithValueOne(piecePlacement);

      switch (piecePlacement.pieceType()) {
        case ROOK:
        case KNIGHT:
        case QUEEN:
        case PAWN:
        case KING:
          assertEquals(ALL_SQUARE_SET, squaresWithValueOne);
          break;
        case BISHOP:
          switch (piecePlacement.squareOriginal().getSquareType()) {
            case LIGHT_SQUARE:
              assertEquals(LIGHT_SQUARE_SET, squaresWithValueOne);
              break;
            case DARK_SQUARE:
              assertEquals(DARK_SQUARE_SET, squaresWithValueOne);
              break;
            case NONE:
            default:
              throw new IllegalArgumentException();
          }
          break;
        case NONE:
        default:
          throw new IllegalArgumentException();
      }
    }
  }

  private static void checkNumberOfPieces(StaticPosition staticPosition, MobilitySolution mobilitySolution) {
    assertEquals(calculateNumberOfPieces(staticPosition), mobilitySolution.getPiecePlacementSet().size());
  }

  private static void checkBoardOccupation(StaticPosition staticPosition, MobilitySolution mobilitySolution) {
    checkNumberOfPieces(staticPosition, mobilitySolution);
    checkPieces(staticPosition, mobilitySolution);
    checkEmptySquares(staticPosition, mobilitySolution);
  }

  private static void checkPieces(StaticPosition staticPosition, MobilitySolution mobilitySolution) {
    for (final PiecePlacement piecePlacement : mobilitySolution.getPiecePlacementSet()) {
      assertFalse(staticPosition.isEmpty(piecePlacement.squareOriginal()));
      assertEquals(staticPosition.get(piecePlacement.squareOriginal()).getPieceType(), piecePlacement.pieceType());
      assertEquals(staticPosition.get(piecePlacement.squareOriginal()).getSide(), piecePlacement.side());
    }
  }

  private static void checkEmptySquares(StaticPosition staticPosition, MobilitySolution mobilitySolution) {
    final Set<Square> occupiedSquares = calculateOccupiedSquares(mobilitySolution);
    for (final Square square : Square.BOARD_SQUARE_LIST) {
      if (!occupiedSquares.contains(square)) {
        assertTrue(staticPosition.isEmpty(square));
      }
    }
  }

  private static int calculateNumberOfPieces(StaticPosition staticPosition) {
    var counter = 0;
    for (final Square square : Square.BOARD_SQUARE_LIST) {
      if (!staticPosition.isEmpty(square)) {
        counter++;
      }
    }
    return counter;
  }

  private static Set<Square> calculateOccupiedSquares(MobilitySolution mobilitySolution) {
    final Set<Square> nonEmptySquares = new TreeSet<>();
    for (final PiecePlacement piecePlacement : mobilitySolution.getPiecePlacementSet()) {
      nonEmptySquares.add(piecePlacement.squareOriginal());
    }
    return nonEmptySquares;
  }

}
