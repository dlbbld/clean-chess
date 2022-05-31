package com.dlb.chess.test.unwinnability;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
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
  }

  @SuppressWarnings("static-method")
  @Test
  void testBasicBishops() throws Exception {

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
      final List<Square> blackKingToSquareList = NonNullWrapperCommon.asList(A1, B1, C1, D1, E1, F1, G1, H1, B2, D2, E2,
          F2, G2, H2, E3, F3, G3, H3, D4, E4, F4, G4, H4, E5, F5, G5, H5, A6, B6, C6, D6, E6, F6, G6, H6, A7, B7, C7,
          D7, E7, F7, G7, H7, A8, B8, C8, D8, E8, F8, G8, H8);

      checkAllLockedButKing("3k2n1/1p2p3/1P2Pp1p/5P1P/1K6/8/8/8 w - - 0 100", whiteKingToSquareList,
          blackKingToSquareList);

      checkAllLockedButKing("3k2n1/1p2p3/1P2Pp1p/5P1P/1K6/8/8/8 b - - 0 100", whiteKingToSquareList,
          blackKingToSquareList);
    }

  }

  private static void checkAllLockedButKing(String fen, List<Square> whiteKingToSquareList,
      List<Square> blackKingToSquareList) {
    final Board board = new Board(fen);

    final MobilitySolution mobilitySolution = Mobility.mobility(board);

    for (final PiecePlacement piecePlacement : mobilitySolution.getPiecePlacementSet()) {
      final Set<Square> squaresWithValueOne = mobilitySolution.calculateSquaresWithValueOne(piecePlacement);
      switch (piecePlacement.type()) {
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
      switch (piecePlacement.type()) {
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

      switch (piecePlacement.type()) {
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
      assertEquals(staticPosition.get(piecePlacement.squareOriginal()).getPieceType(), piecePlacement.type());
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
