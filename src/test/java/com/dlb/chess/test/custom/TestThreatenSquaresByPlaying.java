package com.dlb.chess.test.custom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.squares.to.threaten.BishopThreatenSquares;
import com.dlb.chess.squares.to.threaten.KingNonCastlingThreatenSquares;
import com.dlb.chess.squares.to.threaten.KnightThreatenSquares;
import com.dlb.chess.squares.to.threaten.PawnThreatenSquares;
import com.dlb.chess.squares.to.threaten.QueenThreatenSquares;
import com.dlb.chess.squares.to.threaten.RookThreatenSquares;

class TestThreatenSquaresByPlaying implements EnumConstants {

  @SuppressWarnings("static-method")
  @Test
  void testRookSquares() {
    final ApiBoard board = new Board();

    // test initital position
    // white
    checkExceptionRook(board.getStaticPosition(), A2, WHITE);

    checkExceptionRook(board.getStaticPosition(), D5, WHITE);

    checkExceptionRook(board.getStaticPosition(), A8, WHITE);

    // black
    checkExceptionRook(board.getStaticPosition(), A7, BLACK);

    checkExceptionRook(board.getStaticPosition(), D5, BLACK);

    checkExceptionRook(board.getStaticPosition(), A1, BLACK);

    {
      final Set<Square> potentialToSquareSet = RookThreatenSquares
          .calculateRookThreatenSquares(board.getStaticPosition(), A1, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(A2, B1));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      final Set<Square> potentialToSquareSet = RookThreatenSquares
          .calculateRookThreatenSquares(board.getStaticPosition(), H1, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(G1, H2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      final Set<Square> potentialToSquareSet = RookThreatenSquares
          .calculateRookThreatenSquares(board.getStaticPosition(), A8, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(A7, B8));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      final Set<Square> potentialToSquareSet = RookThreatenSquares
          .calculateRookThreatenSquares(board.getStaticPosition(), H8, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(G8, H7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    // now we look on rooks initially on A1 and H8 after each move from white and
    // black
    {
      final MoveSpecification move = new MoveSpecification(WHITE, A2, A4);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = RookThreatenSquares
          .calculateRookThreatenSquares(board.getStaticPosition(), A1, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(A2, A3, A4, B1));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = RookThreatenSquares
          .calculateRookThreatenSquares(board.getStaticPosition(), H8, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(G8, H7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      final MoveSpecification move = new MoveSpecification(BLACK, H7, H5);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = RookThreatenSquares
          .calculateRookThreatenSquares(board.getStaticPosition(), A1, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(A2, A3, A4, B1));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = RookThreatenSquares
          .calculateRookThreatenSquares(board.getStaticPosition(), H8, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(H7, H6, H5, G8));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      final MoveSpecification move = new MoveSpecification(WHITE, A1, A3);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = RookThreatenSquares
          .calculateRookThreatenSquares(board.getStaticPosition(), A3, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(B3, C3, D3, E3, F3, G3, H3, A2, A1, A4));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = RookThreatenSquares
          .calculateRookThreatenSquares(board.getStaticPosition(), H8, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(H7, H6, H5, G8));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      final MoveSpecification move = new MoveSpecification(BLACK, H5, H4);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = RookThreatenSquares
          .calculateRookThreatenSquares(board.getStaticPosition(), A3, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(B3, C3, D3, E3, F3, G3, H3, A2, A1, A4));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = RookThreatenSquares
          .calculateRookThreatenSquares(board.getStaticPosition(), H8, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(H7, H6, H5, H4, G8));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      final MoveSpecification move = new MoveSpecification(WHITE, A3, D3);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = RookThreatenSquares
          .calculateRookThreatenSquares(board.getStaticPosition(), D3, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(
          Arrays.asList(D4, D5, D6, D7, E3, F3, G3, H3, C3, B3, A3, D2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = RookThreatenSquares
          .calculateRookThreatenSquares(board.getStaticPosition(), H8, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(H7, H6, H5, H4, G8));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      final MoveSpecification move = new MoveSpecification(BLACK, H8, H5);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = RookThreatenSquares
          .calculateRookThreatenSquares(board.getStaticPosition(), D3, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(
          Arrays.asList(D4, D5, D6, D7, E3, F3, G3, H3, C3, B3, A3, D2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = RookThreatenSquares
          .calculateRookThreatenSquares(board.getStaticPosition(), H5, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(H6, H7, H8, G5, F5, E5, D5, C5, B5, A5, H4));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      final MoveSpecification move = new MoveSpecification(WHITE, D3, D5);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = RookThreatenSquares
          .calculateRookThreatenSquares(board.getStaticPosition(), D5, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(
          Arrays.asList(D6, D7, E5, F5, G5, H5, D4, D3, C5, B5, A5, D2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = RookThreatenSquares
          .calculateRookThreatenSquares(board.getStaticPosition(), H5, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(H6, H7, H8, G5, F5, E5, D5, H4));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      final MoveSpecification move = new MoveSpecification(BLACK, H5, E5);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = RookThreatenSquares
          .calculateRookThreatenSquares(board.getStaticPosition(), D5, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D6, D7, E5, D4, D3, C5, B5, A5, D2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = RookThreatenSquares
          .calculateRookThreatenSquares(board.getStaticPosition(), E5, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E6, F5, G5, H5, E4, E3, E2, D5, E7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      final MoveSpecification move = new MoveSpecification(WHITE, E2, E3);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = RookThreatenSquares
          .calculateRookThreatenSquares(board.getStaticPosition(), D5, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D6, D7, E5, D4, D3, C5, B5, A5, D2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = RookThreatenSquares
          .calculateRookThreatenSquares(board.getStaticPosition(), E5, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E6, F5, G5, H5, E4, E3, D5, E7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      final MoveSpecification move = new MoveSpecification(BLACK, B8, C6);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = RookThreatenSquares
          .calculateRookThreatenSquares(board.getStaticPosition(), D5, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D6, D7, E5, D4, D3, C5, B5, A5, D2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = RookThreatenSquares
          .calculateRookThreatenSquares(board.getStaticPosition(), E5, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E6, F5, G5, H5, E4, E3, D5, E7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      final MoveSpecification move = new MoveSpecification(WHITE, D1, H5);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = RookThreatenSquares
          .calculateRookThreatenSquares(board.getStaticPosition(), D5, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D6, D7, E5, D4, D3, C5, B5, A5, D2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = RookThreatenSquares
          .calculateRookThreatenSquares(board.getStaticPosition(), E5, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E6, F5, G5, H5, E4, E3, D5, E7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      final MoveSpecification move = new MoveSpecification(BLACK, C6, D4);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = RookThreatenSquares
          .calculateRookThreatenSquares(board.getStaticPosition(), D5, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D6, D7, E5, D4, C5, B5, A5));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = RookThreatenSquares
          .calculateRookThreatenSquares(board.getStaticPosition(), E5, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E6, F5, G5, H5, E4, E3, D5, E7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      final MoveSpecification move = new MoveSpecification(WHITE, F1, B5);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = RookThreatenSquares
          .calculateRookThreatenSquares(board.getStaticPosition(), D5, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D6, D7, E5, D4, C5, B5));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = RookThreatenSquares
          .calculateRookThreatenSquares(board.getStaticPosition(), E5, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E6, F5, G5, H5, E4, E3, D5, E7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      final MoveSpecification move = new MoveSpecification(BLACK, D4, F5);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = RookThreatenSquares
          .calculateRookThreatenSquares(board.getStaticPosition(), D5, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D6, D7, E5, D4, D3, C5, D2, B5));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = RookThreatenSquares
          .calculateRookThreatenSquares(board.getStaticPosition(), E5, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E6, E4, E3, D5, F5, E7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // waiting move
      final MoveSpecification move = new MoveSpecification(WHITE, B1, C3);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = RookThreatenSquares
          .calculateRookThreatenSquares(board.getStaticPosition(), D5, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D6, D7, E5, D4, D3, C5, D2, B5));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = RookThreatenSquares
          .calculateRookThreatenSquares(board.getStaticPosition(), E5, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E6, E4, E3, D5, F5, E7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // black checks
      final MoveSpecification move = new MoveSpecification(BLACK, E5, E3);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = RookThreatenSquares
          .calculateRookThreatenSquares(board.getStaticPosition(), D5, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D6, D7, E5, F5, D4, D3, C5, B5, D2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = RookThreatenSquares
          .calculateRookThreatenSquares(board.getStaticPosition(), E3, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E4, E5, E6, F3, G3, H3, E2, E1, D3, C3, E7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // white blocks check with knight
      final MoveSpecification move = new MoveSpecification(WHITE, G1, E2);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = RookThreatenSquares
          .calculateRookThreatenSquares(board.getStaticPosition(), D5, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D6, D7, E5, F5, D4, D3, C5, D2, B5));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = RookThreatenSquares
          .calculateRookThreatenSquares(board.getStaticPosition(), E3, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E4, E5, E6, F3, G3, H3, E2, D3, C3, E7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

  }

  @SuppressWarnings("static-method")
  @Test
  void testKnightSquares() {
    final ApiBoard board = new Board();

    // test initital position
    // white
    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), B1, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(C3, A3, D2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    checkExceptionKnight(board.getStaticPosition(), A1, WHITE);

    checkExceptionKnight(board.getStaticPosition(), D4, WHITE);

    checkExceptionKnight(board.getStaticPosition(), B8, WHITE);

    // black
    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), G8, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(F6, H6, E7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    checkExceptionKnight(board.getStaticPosition(), A1, BLACK);

    checkExceptionKnight(board.getStaticPosition(), D4, BLACK);

    checkExceptionKnight(board.getStaticPosition(), B1, BLACK);

    // now we look on knights initially on B1 and F8 after each move from white and
    // black
    {
      final MoveSpecification move = new MoveSpecification(WHITE, B1, C3);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), C3, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D5, E4, B1, A4, B5, A2, E2, D1));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), G8, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(F6, H6, E7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      final MoveSpecification move = new MoveSpecification(BLACK, G8, H6);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), C3, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D5, E4, B1, A4, B5, A2, E2, D1));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), H6, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(G4, F5, G8, F7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      final MoveSpecification move = new MoveSpecification(WHITE, C3, D5);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), D5, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E7, F6, F4, E3, C3, B4, B6, C7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), H6, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(G4, F5, G8, F7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      final MoveSpecification move = new MoveSpecification(BLACK, H6, G4);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), D5, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E7, F6, F4, E3, C3, B4, B6, C7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), G4, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(F2, E3, E5, F6, H6, H2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      final MoveSpecification move = new MoveSpecification(WHITE, C2, C3);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), D5, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E7, F6, F4, E3, C3, B4, B6, C7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), G4, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(F2, E3, E5, F6, H6, H2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      final MoveSpecification move = new MoveSpecification(BLACK, H7, H6);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), D5, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E7, F6, F4, E3, C3, B4, B6, C7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), G4, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(F2, E3, E5, F6, H6, H2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      final MoveSpecification move = new MoveSpecification(WHITE, H2, H4);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), D5, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E7, F6, F4, E3, B4, B6, C7, C3));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), G4, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(F2, E3, E5, F6, H2, H6));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      final MoveSpecification move = new MoveSpecification(BLACK, E7, E6);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), D5, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E7, F6, F4, E3, B4, B6, C7, C3));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), G4, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(F2, E3, E5, F6, H2, H6));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      final MoveSpecification move = new MoveSpecification(WHITE, H1, H2);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), D5, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E7, F6, F4, E3, B4, B6, C7, C3));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), G4, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(F2, E3, E5, F6, H2, H6));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      final MoveSpecification move = new MoveSpecification(BLACK, D8, E7);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), D5, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E7, F6, F4, E3, B4, B6, C7, C3));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), G4, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(F2, E3, E5, F6, H2, H6));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // white checks
      final MoveSpecification move = new MoveSpecification(WHITE, D5, F6);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), F6, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(G8, H7, H5, G4, E4, D5, D7, E8));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), G4, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(F2, E3, E5, F6, H2, H6));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // black moves out of check
      final MoveSpecification move = new MoveSpecification(BLACK, E8, D8);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), F6, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(G8, H7, H5, G4, E4, D5, D7, E8));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), G4, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(F2, E3, E5, F6, H2, H6));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      final MoveSpecification move = new MoveSpecification(WHITE, E2, E4);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), F6, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(G8, H7, H5, G4, D5, D7, E8, E4));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), G4, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(F2, E3, E5, F6, H2, H6));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      final MoveSpecification move = new MoveSpecification(BLACK, E7, D6);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), F6, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(G8, H7, H5, G4, D5, D7, E8, E4));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), G4, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(F2, E3, E5, F6, H2, H6));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      final MoveSpecification move = new MoveSpecification(WHITE, F1, A6);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), F6, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(G8, H7, H5, G4, D5, D7, E8, E4));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), G4, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(F2, E3, E5, F6, H2, H6));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      final MoveSpecification move = new MoveSpecification(BLACK, D6, E5);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), F6, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(G8, H7, H5, G4, D5, D7, E8, E4));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), G4, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(F2, E3, F6, H2, H6, E5));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      final MoveSpecification move = new MoveSpecification(WHITE, E1, F1);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), F6, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(G8, H7, H5, G4, D5, D7, E8, E4));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), G4, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(F2, E3, F6, H2, H6, E5));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // black checks
      final MoveSpecification move = new MoveSpecification(BLACK, G4, H2);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), F6, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(G8, H7, H5, G4, D5, D7, E8, E4));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), H2, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(F1, F3, G4));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // white moves out of check
      final MoveSpecification move = new MoveSpecification(WHITE, F1, E2);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), F6, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(G8, H7, H5, G4, D5, D7, E8, E4));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KnightThreatenSquares
          .calculateKnightThreatenSquares(board.getStaticPosition(), H2, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(F1, F3, G4));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testBishopSquares() {
    final ApiBoard board = new Board();

    // test initital position
    // white

    checkExceptionBishop(board.getStaticPosition(), D1, WHITE);

    checkExceptionBishop(board.getStaticPosition(), E4, WHITE);

    checkExceptionBishop(board.getStaticPosition(), A8, WHITE);

    // black

    checkExceptionBishop(board.getStaticPosition(), D8, BLACK);

    checkExceptionBishop(board.getStaticPosition(), E4, BLACK);

    checkExceptionBishop(board.getStaticPosition(), A1, BLACK);

    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), C1, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(B2, D2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), F1, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E2, G2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), C8, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(B7, D7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), F8, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E7, G7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    // now we are making moves
    {
      final MoveSpecification move = new MoveSpecification(WHITE, B2, B3);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), C1, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(B2, D2, A3));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), F8, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E7, G7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      final MoveSpecification move = new MoveSpecification(BLACK, E7, E6);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), C1, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(B2, D2, A3));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), F8, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E7, D6, C5, B4, A3, G7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      final MoveSpecification move = new MoveSpecification(WHITE, C1, B2);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), B2, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(C3, D4, E5, F6, G7, C1, A1, A3));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), F8, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E7, D6, C5, B4, A3, G7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      final MoveSpecification move = new MoveSpecification(BLACK, F8, C5);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), B2, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(C3, D4, E5, F6, G7, C1, A1, A3));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), C5, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D6, E7, F8, D4, E3, F2, B4, A3, B6, A7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      final MoveSpecification move = new MoveSpecification(WHITE, B2, D4);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), D4, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E5, F6, G7, E3, C3, B2, C5, A1, F2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), C5, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D6, E7, F8, D4, B4, A3, B6, A7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      final MoveSpecification move = new MoveSpecification(BLACK, G7, G6);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), D4, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E5, F6, G7, H8, E3, C3, B2, C5, A1, F2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), C5, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D6, E7, F8, D4, B4, A3, B6, A7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // capture the rook with white's bishop so black bishop can later check
      final MoveSpecification move = new MoveSpecification(WHITE, D4, H8);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), H8, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(G7, F6, E5, D4, C3, B2, A1));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), C5, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D6, E7, F8, D4, E3, F2, B4, A3, B6, A7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // black retreats bishop as waiting move so can free a square for white king
      final MoveSpecification move = new MoveSpecification(BLACK, C5, B6);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), H8, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(G7, F6, E5, D4, C3, B2, A1));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), B6, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(C5, D4, E3, F2, A5, A7, C7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    // now we look at whites other bishop (the white light square bishop) so we can
    {
      // white prepares development of light square bishop so can check later
      final MoveSpecification move = new MoveSpecification(WHITE, E2, E4);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), F1, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E2, D3, C4, B5, A6, G2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), B6, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(C5, D4, E3, F2, A5, A7, C7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // we need another waiting move for black for prepare square so white king can
      // move out of check, we give retreat square
      final MoveSpecification move = new MoveSpecification(BLACK, A7, A6);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), F1, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E2, D3, C4, B5, A6, G2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), B6, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(C5, D4, E3, F2, A5, A7, C7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // free the square for white king
      final MoveSpecification move = new MoveSpecification(WHITE, D1, C1);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), F1, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E2, D3, C4, B5, A6, G2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), B6, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(C5, D4, E3, F2, A5, A7, C7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // black checks
      final MoveSpecification move = new MoveSpecification(BLACK, B6, F2);
      board.performMove(move);
    }

    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), F1, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E2, D3, C4, B5, A6, G2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), F2, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(G3, H4, G1, E1, E3, D4, C5, B6, A7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // white moves out of check
      final MoveSpecification move = new MoveSpecification(WHITE, E1, D1);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), F1, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E2, D3, C4, B5, A6, G2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), F2, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(G3, H4, G1, E1, E3, D4, C5, B6, A7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // black takes the knight
      final MoveSpecification move = new MoveSpecification(BLACK, F2, G1);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), F1, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E2, D3, C4, B5, A6, G2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), G1, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(H2, F2, E3, D4, C5, B6, A7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // white takes out the bishop for later check
      final MoveSpecification move = new MoveSpecification(WHITE, F1, C4);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), C4, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D5, E6, D3, E2, F1, B5, A6, B3));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), G1, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(H2, F2, E3, D4, C5, B6, A7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // black moves pawn so white bishop can check
      final MoveSpecification move = new MoveSpecification(BLACK, E6, E5);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), C4, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D5, E6, F7, D3, E2, F1, B5, A6, B3));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), G1, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(H2, F2, E3, D4, C5, B6, A7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // white checks
      final MoveSpecification move = new MoveSpecification(WHITE, C4, F7);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), F7, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(G8, G6, E6, D5, C4, E8, B3));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), G1, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(H2, F2, E3, D4, C5, B6, A7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // black moves out of check
      final MoveSpecification move = new MoveSpecification(BLACK, E8, F8);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), F7, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(G8, G6, E6, D5, C4, E8, B3));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = BishopThreatenSquares
          .calculateBishopThreatenSquares(board.getStaticPosition(), G1, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(H2, F2, E3, D4, C5, B6, A7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testQueenSquares() {
    final ApiBoard board = new Board();

    // test initital position
    // white

    checkExceptionQueen(board.getStaticPosition(), C1, WHITE);

    checkExceptionQueen(board.getStaticPosition(), E4, WHITE);

    checkExceptionQueen(board.getStaticPosition(), D8, WHITE);

    // black

    checkExceptionQueen(board.getStaticPosition(), B8, BLACK);

    checkExceptionQueen(board.getStaticPosition(), D5, BLACK);

    checkExceptionQueen(board.getStaticPosition(), D1, BLACK);

    {
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), D1, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(C1, C2, D2, E2, E1));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), D8, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(C8, C7, D7, E7, E8));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    // now we are making moves
    {
      // e4
      final MoveSpecification move = new MoveSpecification(WHITE, E2, E4);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), D1, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(C1, C2, D2, E2, F3, G4, H5, E1));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), D8, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(C8, C7, D7, E7, E8));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // d5
      final MoveSpecification move = new MoveSpecification(BLACK, D7, D5);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), D1, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(C1, C2, D2, E2, F3, G4, H5, E1));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), D8, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(C8, C7, D7, E7, E8, D6, D5));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // white queen enters the game
      final MoveSpecification move = new MoveSpecification(WHITE, D1, H5);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), H5, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(
          Arrays.asList(H6, H7, H4, H3, G4, F3, E2, D1, G5, F5, E5, D5, G6, F7, H2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), D8, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(C8, C7, D7, E7, E8, D6, D5));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // take away one square from white queen by black piece
      final MoveSpecification move = new MoveSpecification(BLACK, G8, H6);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), H5, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(
          Arrays.asList(H6, H4, H3, G4, F3, E2, D1, G5, F5, E5, D5, G6, F7, H2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), D8, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(C8, C7, D7, E7, E8, D6, D5));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // take away one more square from white queen by white piece
      final MoveSpecification move = new MoveSpecification(WHITE, G1, H3);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), H5, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(
          Arrays.asList(H6, H4, H3, G4, F3, E2, D1, G5, F5, E5, D5, G6, F7, H3));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), D8, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(C8, C7, D7, E7, E8, D6, D5));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // a black dummy move, not changing anything
      final MoveSpecification move = new MoveSpecification(BLACK, H8, G8);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), H5, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(
          Arrays.asList(H6, H4, H3, G4, F3, E2, D1, G5, F5, E5, D5, G6, F7, H3));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), D8, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(C8, C7, D7, E7, E8, D6, D5));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // white queen goes for d5
      final MoveSpecification move = new MoveSpecification(WHITE, H5, D5);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), D5, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(
          Arrays.asList(D6, D7, D8, E6, F7, E5, F5, G5, H5, D4, D3, C4, B3, C5, B5, A5, C6, B7, E4, D2, A2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), D8, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(C8, C7, D7, E7, E8, D6, D5));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // black queen comes out
      final MoveSpecification move = new MoveSpecification(BLACK, D8, D6);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), D5, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(
          Arrays.asList(D6, E6, F7, E5, F5, G5, H5, D4, D3, C4, B3, C5, B5, A5, C6, B7, E4, D2, A2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), D6, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(
          Arrays.asList(D7, D8, E6, F6, G6, E5, F4, G3, H2, D5, C5, B4, A3, C6, B6, A6, C7, E7, H6));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // white checks
      final MoveSpecification move = new MoveSpecification(WHITE, D5, F7);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), F7, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(
          Arrays.asList(F8, G8, G7, G6, H5, F6, F5, F4, F3, E6, D5, C4, B3, E7, E8, A2, F2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), D6, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(
          Arrays.asList(D7, D8, E6, F6, G6, E5, F4, G3, H2, D5, D4, D3, D2, C5, B4, A3, C6, B6, A6, C7, E7, H6));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // black moves out of check
      final MoveSpecification move = new MoveSpecification(BLACK, E8, D8);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), F7, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(
          Arrays.asList(F8, G8, G7, G6, H5, F6, F5, F4, F3, E6, D5, C4, B3, E7, E8, A2, F2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), D6, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(
          Arrays.asList(D7, D8, E6, F6, G6, E5, F4, G3, H2, D5, D4, D3, D2, C5, B4, A3, C6, B6, A6, C7, E7, H6));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // white moves out bishop to later take away a few squares from black queen
      final MoveSpecification move = new MoveSpecification(WHITE, F1, B5);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), F7, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(
          Arrays.asList(F8, G8, G7, G6, H5, F6, F5, F4, F3, E6, D5, C4, B3, E7, E8, A2, F2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), D6, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(
          Arrays.asList(D7, D8, E6, F6, G6, E5, F4, G3, H2, D5, D4, D3, D2, C5, B4, A3, C6, B6, A6, C7, E7, H6));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // black makes a dummy move
      final MoveSpecification move = new MoveSpecification(BLACK, G8, H8);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), F7, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(
          Arrays.asList(F8, G8, G7, G6, H5, F6, F5, F4, F3, E6, D5, C4, B3, E7, E8, A2, F2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), D6, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(
          Arrays.asList(D7, D8, E6, F6, G6, E5, F4, G3, H2, D5, D4, D3, D2, C5, B4, A3, C6, B6, A6, C7, E7, H6));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // white takes away two squares from black queen
      final MoveSpecification move = new MoveSpecification(WHITE, B5, C6);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), F7, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(
          Arrays.asList(F8, G8, G7, G6, H5, F6, F5, F4, F3, E6, D5, C4, B3, E7, E8, A2, F2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), D6, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(
          Arrays.asList(D7, E6, F6, G6, E5, F4, G3, H2, D5, D4, D3, D2, C5, B4, A3, C6, C7, E7, H6, D8));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // black checks
      final MoveSpecification move = new MoveSpecification(BLACK, D6, D2);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), F7, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(
          Arrays.asList(F8, G8, G7, G6, H5, F6, F5, F4, F3, E6, D5, C4, B3, E7, E8, A2, F2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), D2, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(
          Arrays.asList(D3, D4, D5, D6, D7, E3, F4, G5, E2, F2, E1, D1, C1, C2, C3, B4, A5, D8, H6));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // white moves out of check
      final MoveSpecification move = new MoveSpecification(WHITE, E1, F1);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), F7, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(
          Arrays.asList(F8, G8, G7, G6, H5, F6, F5, F4, F3, E6, D5, C4, B3, E7, E8, A2, F2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), D2, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(
          Arrays.asList(D3, D4, D5, D6, D7, E3, F4, G5, E2, F2, E1, D1, C1, C2, C3, B4, A5, D8, H6));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // let black move queen one more time
      final MoveSpecification move = new MoveSpecification(BLACK, D2, C2);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), F7, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(
          Arrays.asList(F8, G8, G7, G6, H5, F6, F5, F4, F3, E6, D5, C4, B3, E7, E8, A2, F2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), C2, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(
          Arrays.asList(C3, C4, C5, C6, D3, E4, D2, E2, F2, D1, C1, B1, B2, B3, A4));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // let white move queen one more time
      final MoveSpecification move = new MoveSpecification(WHITE, F7, G7);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), G7, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(
          Arrays.asList(G8, H8, H7, H6, G6, G5, G4, G3, F6, E5, D4, C3, F7, E7, F8, B2, G2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), C2, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(
          Arrays.asList(C3, C4, C5, C6, D3, E4, D2, E2, F2, D1, C1, B1, B2, B3, A4));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testKingNonCastlingSquares() {
    final ApiBoard board = new Board();

    // test initital position
    // white

    checkExceptionKingNonCastling(board.getStaticPosition(), C1, WHITE);

    checkExceptionKingNonCastling(board.getStaticPosition(), E4, WHITE);

    checkExceptionKingNonCastling(board.getStaticPosition(), E8, WHITE);

    // black

    checkExceptionKingNonCastling(board.getStaticPosition(), B8, BLACK);

    checkExceptionKingNonCastling(board.getStaticPosition(), D5, BLACK);

    checkExceptionKingNonCastling(board.getStaticPosition(), E1, BLACK);

    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), E1, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D1, D2, E2, F2, F1));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), E8, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D8, D7, E7, F7, F8));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    // now we are making moves
    {
      // e4
      final MoveSpecification move = new MoveSpecification(WHITE, E2, E4);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), E1, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D1, D2, E2, F2, F1));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), E8, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D8, D7, E7, F7, F8));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // d5
      final MoveSpecification move = new MoveSpecification(BLACK, D7, D5);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), E1, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D1, D2, E2, F2, F1));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), E8, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D8, D7, E7, F7, F8));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // white king walks to e2
      final MoveSpecification move = new MoveSpecification(WHITE, E1, E2);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), E2, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D1, D2, D3, E3, F3, F2, F1, E1));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), E8, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D8, D7, E7, F7, F8));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // black king walks to d7
      final MoveSpecification move = new MoveSpecification(BLACK, E8, D7);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), E2, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D1, D2, D3, E3, F3, F2, F1, E1));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), D7, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E8, E7, E6, D6, C6, C7, C8, D8));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // white king walks to e3
      final MoveSpecification move = new MoveSpecification(WHITE, E2, E3);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), E3, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D2, D3, D4, E4, F4, F3, F2, E2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), D7, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E8, E7, E6, D6, C6, C7, C8, D8));

      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // black king walks to d6
      final MoveSpecification move = new MoveSpecification(BLACK, D7, D6);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), E3, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D2, D3, D4, E4, F4, F3, F2, E2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), D6, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E7, E6, E5, D5, C5, C6, C7, D7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // white king walks to f4
      final MoveSpecification move = new MoveSpecification(WHITE, E3, F4);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), F4, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E3, E4, E5, F5, G5, G4, G3, F3));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), D6, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E7, E6, E5, D5, C5, C6, C7, D7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // black moves a pawn next to king
      final MoveSpecification move = new MoveSpecification(BLACK, C7, C6);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), F4, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E3, E4, E5, F5, G5, G4, G3, F3));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), D6, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E7, E6, E5, D5, C5, C6, C7, D7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // white moves a pawn next to king
      final MoveSpecification move = new MoveSpecification(WHITE, G2, G4);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), F4, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E3, E4, E5, F5, G5, G4, G3, F3));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), D6, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E7, E6, E5, D5, C5, C6, C7, D7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // black captures g4 pawn with bishop, white king can now capture g4, so has one
      // more square
      // available
      final MoveSpecification move = new MoveSpecification(BLACK, C8, G4);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), F4, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E3, E4, E5, F5, G5, G4, G3, F3));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), D6, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E7, E6, E5, D5, C5, C6, C7, D7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // white captures d5 pawn with pawn, black king can now capture d5, so has one
      // more square
      // available, white king has also one more square available e4
      final MoveSpecification move = new MoveSpecification(WHITE, E4, D5);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), F4, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E3, E4, E5, F5, G5, G4, G3, F3));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), D6, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E7, E6, E5, D5, C5, C6, C7, D7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // black checks white king with pawn
      final MoveSpecification move = new MoveSpecification(BLACK, E7, E5);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), F4, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E3, E4, E5, F5, G5, G4, G3, F3));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), D6, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E7, E6, E5, D5, C5, C6, C7, D7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // white moves king away
      final MoveSpecification move = new MoveSpecification(WHITE, F4, E4);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), E4, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D3, D4, D5, E5, F5, F4, F3, E3));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), D6, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E7, E6, E5, D5, C5, C6, C7, D7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // black brings knight out
      final MoveSpecification move = new MoveSpecification(BLACK, G8, F6);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), E4, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D3, D4, D5, E5, F5, F4, F3, E3));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), D6, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E7, E6, E5, D5, C5, C6, C7, D7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    // begin intermediary moves
    {
      final MoveSpecification move = new MoveSpecification(WHITE, E4, D3);
      board.performMove(move);
    }
    {
      final MoveSpecification move = new MoveSpecification(BLACK, F6, E8);
      board.performMove(move);
    }

    {
      final MoveSpecification move = new MoveSpecification(WHITE, D3, E4);
      board.performMove(move);
    }
    {
      final MoveSpecification move = new MoveSpecification(BLACK, A7, A6);
      board.performMove(move);
    }

    {
      final MoveSpecification move = new MoveSpecification(WHITE, D1, E2);
      board.performMove(move);
    }
    {
      final MoveSpecification move = new MoveSpecification(BLACK, G4, H3);
      board.performMove(move);
    }

    {
      final MoveSpecification move = new MoveSpecification(WHITE, E2, H5);
      board.performMove(move);
    }
    {
      final MoveSpecification move = new MoveSpecification(BLACK, H3, G4);
      board.performMove(move);
    }

    {
      final MoveSpecification move = new MoveSpecification(WHITE, H5, H4);
      board.performMove(move);
    }
    {
      final MoveSpecification move = new MoveSpecification(BLACK, H8, G8);
      board.performMove(move);
    }
    // end intermediary moves

    {
      final MoveSpecification move = new MoveSpecification(WHITE, H4, H5);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), E4, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D3, D4, D5, E5, F5, F4, F3, E3));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), D6, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E7, E6, E5, D5, C5, C6, C7, D7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // black checks king again
      final MoveSpecification move = new MoveSpecification(BLACK, G4, F3);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), E4, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D3, D4, D5, E5, F5, F4, F3, E3));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), D6, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E7, E6, E5, D5, C5, C6, C7, D7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // white king captures the checking piece
      final MoveSpecification move = new MoveSpecification(WHITE, E4, F3);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), F3, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E2, E3, E4, F4, G4, G3, G2, F2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), D6, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E7, E6, E5, D5, C5, C6, C7, D7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    // begin intermediary moves
    {
      final MoveSpecification move = new MoveSpecification(BLACK, E8, F6);
      board.performMove(move);
    }
    {
      final MoveSpecification move = new MoveSpecification(WHITE, A2, A3);
      board.performMove(move);
    }

    // end intermediary moves

    {
      // black knight captures in the middle
      final MoveSpecification move = new MoveSpecification(BLACK, F6, D5);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), F3, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E2, E3, E4, F4, G4, G3, G2, F2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), D6, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E7, E6, E5, D5, C5, C6, C7, D7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // white queen captures e5 and checks
      final MoveSpecification move = new MoveSpecification(WHITE, H5, E5);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), F3, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E2, E3, E4, F4, G4, G3, G2, F2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), D6, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E7, E6, E5, D5, C5, C6, C7, D7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // black king captures white queen
      final MoveSpecification move = new MoveSpecification(BLACK, D6, E5);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), F3, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E2, E3, E4, F4, G4, G3, G2, F2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = KingNonCastlingThreatenSquares
          .calculateKingNonCastlingThreatenSquares(board.getStaticPosition(), E5, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(F6, F5, F4, E4, D4, D5, D6, E6));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testPawnSquares() {
    final ApiBoard board = new Board();

    // test initital position
    // white

    checkExceptionPawn(board.getStaticPosition(), C1, WHITE);

    checkExceptionPawn(board.getStaticPosition(), E4, WHITE);

    checkExceptionPawn(board.getStaticPosition(), E7, WHITE);

    // black

    checkExceptionPawn(board.getStaticPosition(), B8, BLACK);

    checkExceptionPawn(board.getStaticPosition(), D5, BLACK);

    checkExceptionPawn(board.getStaticPosition(), E2, BLACK);

    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), C2, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(B3, D3));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), F7, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E6, G6));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    // now we are making moves
    {
      // c4
      final MoveSpecification move = new MoveSpecification(WHITE, C2, C4);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), C4, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(B5, D5));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), F7, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E6, G6));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // f5
      final MoveSpecification move = new MoveSpecification(BLACK, F7, F5);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), C4, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(B5, D5));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), F5, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E4, G4));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // giving black pawn a white pawn to feed
      final MoveSpecification move = new MoveSpecification(WHITE, G2, G4);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), C4, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(B5, D5));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), F5, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E4, G4));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // giving white pawn a black pawn to feed
      final MoveSpecification move = new MoveSpecification(BLACK, D7, D5);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), C4, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(B5, D5));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), F5, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E4, G4));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // moving a-pawn ahead for later en passant capture action - but en passant not
      // supported in potential to level
      // now looking at the a-pawn
      final MoveSpecification move = new MoveSpecification(WHITE, A2, A4);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), A4, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(B5));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), F5, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E4, G4));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // black captures the offered pawn
      final MoveSpecification move = new MoveSpecification(BLACK, F5, G4);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), C4, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(B5, D5));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), G4, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(F3, H3));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // white offers the en passant capture - but en passant not supported in potential to
      // level
      final MoveSpecification move = new MoveSpecification(WHITE, H2, H4);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), C4, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(B5, D5));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), G4, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(F3, H3));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // black captures en passant
      final MoveSpecification move = new MoveSpecification(BLACK, G4, H3);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), C4, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(B5, D5));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), H3, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(G2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // white further a-pawn advance
      final MoveSpecification move = new MoveSpecification(WHITE, A4, A5);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), A5, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(B6));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), H3, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(G2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // black allows en passant capture - but en passant not supported in potential to level
      final MoveSpecification move = new MoveSpecification(BLACK, B7, B5);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), A5, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(B6));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), H3, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(G2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // white captures en passant
      final MoveSpecification move = new MoveSpecification(WHITE, A5, B6);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), B6, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(A7, C7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), H3, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(G2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // black pushes h-pawn
      final MoveSpecification move = new MoveSpecification(BLACK, H3, H2);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), B6, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(A7, C7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), H2, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(G1));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // white captures zig-zag for promotion - zig
      final MoveSpecification move = new MoveSpecification(WHITE, B6, A7);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), A7, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(B8));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), H2, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(G1));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // black promotes to queen by capturing
      final MoveSpecification move = new MoveSpecification(BLACK, H2, G1, PromotionPieceType.QUEEN);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), A7, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(B8));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      // we check the queen
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), G1, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(G2, G3, G4, G5, G6, H2, H1, F1, F2, G7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // white promotes to queen by capturing - zag
      final MoveSpecification move = new MoveSpecification(WHITE, A7, B8, PromotionPieceType.QUEEN);
      board.performMove(move);
    }
    {
      // we check the queen
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), B8, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(C8, C7, B7, B6, B5, B4, B3, A7, A8, B2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), G1, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(G2, G3, G4, G5, G6, H2, H1, F1, F2, G7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // black prepares offering rook for capturing by pawn
      final MoveSpecification move = new MoveSpecification(BLACK, A8, A5);
      board.performMove(move);
    }
    {
      // we check the queen
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), B8, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(C8, C7, B7, B6, B5, B4, B3, A7, A8, B2));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = QueenThreatenSquares
          .calculateQueenThreatenSquares(board.getStaticPosition(), G1, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(G2, G3, G4, G5, G6, H2, H1, F1, F2, G7));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // white does the same with the knight
      // we start focusing the capturing pawns now
      final MoveSpecification move = new MoveSpecification(WHITE, B1, C3);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), C4, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(B5, D5));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), D5, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(C4, E4));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // black offers the rook
      final MoveSpecification move = new MoveSpecification(BLACK, A5, B5);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), C4, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(B5, D5));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), D5, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(C4, E4));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // the pawn takes it
      final MoveSpecification move = new MoveSpecification(WHITE, C4, B5);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), B5, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(A6, C6));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), D5, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(C4, E4));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // black attacks the pawn with a pawn
      final MoveSpecification move = new MoveSpecification(BLACK, C7, C6);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), B5, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(A6, C6));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), D5, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(C4, E4));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // white offers the knight to a pawn
      final MoveSpecification move = new MoveSpecification(WHITE, C3, E4);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), B5, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(A6, C6));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), D5, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(C4, E4));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // black captures the knight
      final MoveSpecification move = new MoveSpecification(BLACK, D5, E4);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), B5, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(A6, C6));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), E4, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D3, F3));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // white offers en passant
      final MoveSpecification move = new MoveSpecification(WHITE, D2, D4);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), B5, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(A6, C6));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), E4, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D3, F3));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // black ignores en passant
      final MoveSpecification move = new MoveSpecification(BLACK, E8, D7);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), B5, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(A6, C6));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), E4, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D3, F3));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // white starts to ignore en passant
      // we look at whites f-pawn now
      final MoveSpecification move = new MoveSpecification(WHITE, F2, F4);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), F4, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E5, G5));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), E4, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D3, F3));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // black ignores en passant capture possibility - but en passant not supported
      // in potential to level
      final MoveSpecification move = new MoveSpecification(BLACK, E7, E5);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), F4, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E5, G5));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), E4, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D3, F3));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // white f-pawn goes ahead
      final MoveSpecification move = new MoveSpecification(WHITE, F4, F5);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), F5, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E6, G6));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), E4, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D3, F3));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // black offers en passant capture - but en passant not supported in potential to level
      final MoveSpecification move = new MoveSpecification(BLACK, G7, G5);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), F5, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E6, G6));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), E4, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D3, F3));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }

    {
      // white ignores en passant capture - but en passant not supported in potential to
      // level
      final MoveSpecification move = new MoveSpecification(WHITE, B2, B3);
      board.performMove(move);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), F5, WHITE);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(E6, G6));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
    {
      final Set<Square> potentialToSquareSet = PawnThreatenSquares
          .calculatePawnThreatenSquares(board.getStaticPosition(), E4, BLACK);
      final Set<Square> expectedSquareSet = new TreeSet<>(Arrays.asList(D3, F3));
      assertEquals(expectedSquareSet, potentialToSquareSet);
    }
  }

  private static void checkExceptionRook(StaticPosition staticPosition, Square fromSquare, Side havingMove) {
    var isCorrectException = false;
    try {
      RookThreatenSquares.calculateRookThreatenSquares(staticPosition, fromSquare, havingMove);
    } catch (@SuppressWarnings("unused") final IllegalArgumentException iae) {
      isCorrectException = true;
    }
    assertTrue(isCorrectException);
  }

  private static void checkExceptionKnight(StaticPosition staticPosition, Square fromSquare, Side havingMove) {
    var isCorrectException = false;
    try {
      KnightThreatenSquares.calculateKnightThreatenSquares(staticPosition, fromSquare, havingMove);
    } catch (@SuppressWarnings("unused") final IllegalArgumentException iae) {
      isCorrectException = true;
    }
    assertTrue(isCorrectException);
  }

  private static void checkExceptionBishop(StaticPosition staticPosition, Square fromSquare, Side havingMove) {
    var isCorrectException = false;
    try {
      BishopThreatenSquares.calculateBishopThreatenSquares(staticPosition, fromSquare, havingMove);
    } catch (@SuppressWarnings("unused") final IllegalArgumentException iae) {
      isCorrectException = true;
    }
    assertTrue(isCorrectException);
  }

  private static void checkExceptionQueen(StaticPosition staticPosition, Square fromSquare, Side havingMove) {
    var isCorrectException = false;
    try {
      QueenThreatenSquares.calculateQueenThreatenSquares(staticPosition, fromSquare, havingMove);
    } catch (@SuppressWarnings("unused") final IllegalArgumentException iae) {
      isCorrectException = true;
    }
    assertTrue(isCorrectException);
  }

  private static void checkExceptionKingNonCastling(StaticPosition staticPosition, Square fromSquare, Side havingMove) {
    var isCorrectException = false;
    try {
      KingNonCastlingThreatenSquares.calculateKingNonCastlingThreatenSquares(staticPosition, fromSquare, havingMove);
    } catch (@SuppressWarnings("unused") final IllegalArgumentException iae) {
      isCorrectException = true;
    }
    assertTrue(isCorrectException);
  }

  private static void checkExceptionPawn(StaticPosition staticPosition, Square fromSquare, Side havingMove) {
    var isCorrectException = false;
    try {
      PawnThreatenSquares.calculatePawnThreatenSquares(staticPosition, fromSquare, havingMove);
    } catch (@SuppressWarnings("unused") final IllegalArgumentException iae) {
      isCorrectException = true;
    }
    assertTrue(isCorrectException);
  }

}
