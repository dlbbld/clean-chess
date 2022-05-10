package com.dlb.chess.test.legalmoves;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.ValidateNewMove;
import com.dlb.chess.board.enums.CastlingMove;
import com.dlb.chess.board.enums.CastlingRight;
import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.exceptions.InvalidMoveException;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.moves.legal.king.KingLegalMoves;
import com.dlb.chess.moves.legal.pawn.PawnLegalMoves;
import com.dlb.chess.moves.legal.pieces.BishopLegalMoves;
import com.dlb.chess.moves.legal.pieces.KnightLegalMoves;
import com.dlb.chess.moves.legal.pieces.QueenLegalMoves;
import com.dlb.chess.moves.legal.pieces.RookLegalMoves;

class TestLegalMovesForPiecesLegalPosition implements EnumConstants {

  @SuppressWarnings("static-method")
  @Test
  void testRookMoves() {
    final ApiBoard board = new Board();

    // test initital position
    // white

    checkExceptionRook(board.getStaticPosition(), board.getHavingMove(), A2);

    checkExceptionRook(board.getStaticPosition(), board.getHavingMove(), D5);

    checkExceptionRook(board.getStaticPosition(), board.getHavingMove(), A8);

    checkExceptionRook(board.getStaticPosition(), board.getHavingMove(), B7);

    checkExceptionRook(board.getStaticPosition(), board.getHavingMove(), G4);

    checkExceptionRook(board.getStaticPosition(), board.getHavingMove(), H8);

    // nowe we look at moves
    {
      // white rook possible moves
      final Set<LegalMove> calculatedLegalMoveSet = RookLegalMoves.calculateRookLegalMoves(board.getStaticPosition(),
          board.getHavingMove(), A1);

      final Set<MoveSpecification> expected = new TreeSet<>();

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, A2, A4);
      board.performMove(move);
    }

    {
      // black rook possible moves
      final Set<LegalMove> calculatedLegalMoveSet = RookLegalMoves.calculateRookLegalMoves(board.getStaticPosition(),
          board.getHavingMove(), A8);

      final Set<MoveSpecification> expected = new TreeSet<>();

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, H7, H5);
      board.performMove(move);
    }

    {
      // white rook possible moves
      final Set<LegalMove> calculatedLegalMoveSet = RookLegalMoves.calculateRookLegalMoves(board.getStaticPosition(),
          board.getHavingMove(), A1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, A1, A2));
      expected.add(new MoveSpecification(WHITE, A1, A3));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move rook out
      final MoveSpecification move = new MoveSpecification(WHITE, A1, A3);
      board.performMove(move);
    }

    {
      // black rook possible moves
      final Set<LegalMove> calculatedLegalMoveSet = RookLegalMoves.calculateRookLegalMoves(board.getStaticPosition(),
          board.getHavingMove(), H8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, H8, H7));
      expected.add(new MoveSpecification(BLACK, H8, H6));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black rook moves out
      final MoveSpecification move = new MoveSpecification(BLACK, H8, H6);
      board.performMove(move);
    }

    {
      // white rook possible moves
      final Set<LegalMove> calculatedLegalMoveSet = RookLegalMoves.calculateRookLegalMoves(board.getStaticPosition(),
          board.getHavingMove(), A3);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, A3, B3));
      expected.add(new MoveSpecification(WHITE, A3, C3));
      expected.add(new MoveSpecification(WHITE, A3, D3));
      expected.add(new MoveSpecification(WHITE, A3, E3));
      expected.add(new MoveSpecification(WHITE, A3, F3));
      expected.add(new MoveSpecification(WHITE, A3, G3));
      expected.add(new MoveSpecification(WHITE, A3, H3));
      expected.add(new MoveSpecification(WHITE, A3, A2));
      expected.add(new MoveSpecification(WHITE, A3, A1));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move - prepare pin
      final MoveSpecification move = new MoveSpecification(WHITE, D2, D3);
      board.performMove(move);
    }

    {
      // black rook possible moves
      final Set<LegalMove> calculatedLegalMoveSet = RookLegalMoves.calculateRookLegalMoves(board.getStaticPosition(),
          board.getHavingMove(), H6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, H6, G6));
      expected.add(new MoveSpecification(BLACK, H6, F6));
      expected.add(new MoveSpecification(BLACK, H6, E6));
      expected.add(new MoveSpecification(BLACK, H6, D6));
      expected.add(new MoveSpecification(BLACK, H6, C6));
      expected.add(new MoveSpecification(BLACK, H6, B6));
      expected.add(new MoveSpecification(BLACK, H6, A6));
      expected.add(new MoveSpecification(BLACK, H6, H7));
      expected.add(new MoveSpecification(BLACK, H6, H8));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move - prepare pin
      final MoveSpecification move = new MoveSpecification(BLACK, D7, D5);
      board.performMove(move);
    }

    {
      // white rook legal moves
      final Set<LegalMove> calculatedLegalMoveSet = RookLegalMoves.calculateRookLegalMoves(board.getStaticPosition(),
          board.getHavingMove(), A3);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, A3, B3));
      expected.add(new MoveSpecification(WHITE, A3, C3));
      expected.add(new MoveSpecification(WHITE, A3, A2));
      expected.add(new MoveSpecification(WHITE, A3, A1));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // move rook in diagonal with own king
      final MoveSpecification move = new MoveSpecification(WHITE, A3, C3);
      board.performMove(move);
    }

    {
      // black rook possible moves - same
      final Set<LegalMove> calculatedLegalMoveSet = RookLegalMoves.calculateRookLegalMoves(board.getStaticPosition(),
          board.getHavingMove(), H6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, H6, G6));
      expected.add(new MoveSpecification(BLACK, H6, F6));
      expected.add(new MoveSpecification(BLACK, H6, E6));
      expected.add(new MoveSpecification(BLACK, H6, D6));
      expected.add(new MoveSpecification(BLACK, H6, C6));
      expected.add(new MoveSpecification(BLACK, H6, B6));
      expected.add(new MoveSpecification(BLACK, H6, A6));
      expected.add(new MoveSpecification(BLACK, H6, H7));
      expected.add(new MoveSpecification(BLACK, H6, H8));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move queen out - to later put in diagonal with white king
      final MoveSpecification move = new MoveSpecification(BLACK, D8, D6);
      board.performMove(move);
    }

    {
      // white rook legal moves
      final Set<LegalMove> calculatedLegalMoveSet = RookLegalMoves.calculateRookLegalMoves(board.getStaticPosition(),
          board.getHavingMove(), C3);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, C3, C4));
      expected.add(new MoveSpecification(WHITE, C3, C5));
      expected.add(new MoveSpecification(WHITE, C3, C6));
      expected.add(new MoveSpecification(WHITE, C3, C7));
      expected.add(new MoveSpecification(WHITE, C3, B3));
      expected.add(new MoveSpecification(WHITE, C3, A3));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // move pawn to let white light square bishop out
      final MoveSpecification move = new MoveSpecification(WHITE, E2, E3);
      board.performMove(move);
    }

    {
      // black rook possible moves
      final Set<LegalMove> calculatedLegalMoveSet = RookLegalMoves.calculateRookLegalMoves(board.getStaticPosition(),
          board.getHavingMove(), H6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, H6, G6));
      expected.add(new MoveSpecification(BLACK, H6, F6));
      expected.add(new MoveSpecification(BLACK, H6, E6));
      expected.add(new MoveSpecification(BLACK, H6, H7));
      expected.add(new MoveSpecification(BLACK, H6, H8));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move queen in diagonal with white king
      final MoveSpecification move = new MoveSpecification(BLACK, D6, B4);
      board.performMove(move);
    }

    {
      // white rook is pinned - no legal moves!
      final Set<LegalMove> calculatedLegalMoveSet = RookLegalMoves.calculateRookLegalMoves(board.getStaticPosition(),
          board.getHavingMove(), C3);

      final Set<MoveSpecification> expected = new TreeSet<>();

      check(expected, calculatedLegalMoveSet);
    }
    {
      // d-pawn advance to take bishop out
      final MoveSpecification move = new MoveSpecification(WHITE, D3, D4);
      board.performMove(move);
    }

    {
      // black rook possible moves
      final Set<LegalMove> calculatedLegalMoveSet = RookLegalMoves.calculateRookLegalMoves(board.getStaticPosition(),
          board.getHavingMove(), H6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, H6, G6));
      expected.add(new MoveSpecification(BLACK, H6, F6));
      expected.add(new MoveSpecification(BLACK, H6, E6));
      expected.add(new MoveSpecification(BLACK, H6, D6));
      expected.add(new MoveSpecification(BLACK, H6, C6));
      expected.add(new MoveSpecification(BLACK, H6, B6));
      expected.add(new MoveSpecification(BLACK, H6, A6));
      expected.add(new MoveSpecification(BLACK, H6, H7));
      expected.add(new MoveSpecification(BLACK, H6, H8));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black retreats queen
      final MoveSpecification move = new MoveSpecification(BLACK, B4, A5);
      board.performMove(move);
    }

    {
      // white rook still pinned - no legal moves!
      final Set<LegalMove> calculatedLegalMoveSet = RookLegalMoves.calculateRookLegalMoves(board.getStaticPosition(),
          board.getHavingMove(), C3);

      final Set<MoveSpecification> expected = new TreeSet<>();

      check(expected, calculatedLegalMoveSet);
    }
    {
      // take bishop out
      final MoveSpecification move = new MoveSpecification(WHITE, F1, B5);
      board.performMove(move);
    }

    {
      // black rook possible moves - can only block check
      final Set<LegalMove> calculatedLegalMoveSet = RookLegalMoves.calculateRookLegalMoves(board.getStaticPosition(),
          board.getHavingMove(), H6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, H6, C6));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black blocks check with rook
      final MoveSpecification move = new MoveSpecification(BLACK, H6, C6);
      board.performMove(move);
    }

    {
      // white rook still pinned - no legal moves!
      final Set<LegalMove> calculatedLegalMoveSet = RookLegalMoves.calculateRookLegalMoves(board.getStaticPosition(),
          board.getHavingMove(), C3);

      final Set<MoveSpecification> expected = new TreeSet<>();

      check(expected, calculatedLegalMoveSet);
    }
    {
      // moves rook out of diagonal
      final MoveSpecification move = new MoveSpecification(WHITE, E1, E2);
      board.performMove(move);
    }

    {
      // black rook pinnned - no moves
      final Set<LegalMove> calculatedLegalMoveSet = RookLegalMoves.calculateRookLegalMoves(board.getStaticPosition(),
          board.getHavingMove(), C6);

      final Set<MoveSpecification> expected = new TreeSet<>();

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black moves king out of diagonal
      final MoveSpecification move = new MoveSpecification(BLACK, E8, D8);
      board.performMove(move);
    }

    {
      // white rook not pinned anymore
      final Set<LegalMove> calculatedLegalMoveSet = RookLegalMoves.calculateRookLegalMoves(board.getStaticPosition(),
          board.getHavingMove(), C3);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, C3, C4));
      expected.add(new MoveSpecification(WHITE, C3, C5));
      expected.add(new MoveSpecification(WHITE, C3, C6));
      expected.add(new MoveSpecification(WHITE, C3, D3));
      expected.add(new MoveSpecification(WHITE, C3, B3));
      expected.add(new MoveSpecification(WHITE, C3, A3));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // moves previously pinned rook to attack the queen
      final MoveSpecification move = new MoveSpecification(WHITE, C3, B3);
      board.performMove(move);
    }

    {
      // black rook not pinned anymore
      final Set<LegalMove> calculatedLegalMoveSet = RookLegalMoves.calculateRookLegalMoves(board.getStaticPosition(),
          board.getHavingMove(), C6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, C6, D6));
      expected.add(new MoveSpecification(BLACK, C6, E6));
      expected.add(new MoveSpecification(BLACK, C6, F6));
      expected.add(new MoveSpecification(BLACK, C6, G6));
      expected.add(new MoveSpecification(BLACK, C6, H6));
      expected.add(new MoveSpecification(BLACK, C6, C5));
      expected.add(new MoveSpecification(BLACK, C6, C4));
      expected.add(new MoveSpecification(BLACK, C6, C3));
      expected.add(new MoveSpecification(BLACK, C6, C2));
      expected.add(new MoveSpecification(BLACK, C6, B6));
      expected.add(new MoveSpecification(BLACK, C6, A6));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black guards the queen :-)
      final MoveSpecification move = new MoveSpecification(BLACK, C6, B6);
      board.performMove(move);
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testKnightMoves() {
    final ApiBoard board = new Board();

    // test initital position
    // white

    checkExceptionKnight(board.getStaticPosition(), board.getHavingMove(), A2);

    checkExceptionKnight(board.getStaticPosition(), board.getHavingMove(), D5);

    checkExceptionKnight(board.getStaticPosition(), board.getHavingMove(), B8);

    checkExceptionKnight(board.getStaticPosition(), board.getHavingMove(), B7);

    checkExceptionKnight(board.getStaticPosition(), board.getHavingMove(), G4);

    checkExceptionKnight(board.getStaticPosition(), board.getHavingMove(), G8);

    // now we look at moves
    {
      // white knight possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KnightLegalMoves
          .calculateKnightLegalMoves(board.getStaticPosition(), board.getHavingMove(), B1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, B1, C3));
      expected.add(new MoveSpecification(WHITE, B1, A3));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white moves knight out
      final MoveSpecification move = new MoveSpecification(WHITE, B1, A3);
      board.performMove(move);
    }

    {
      // black knight possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KnightLegalMoves
          .calculateKnightLegalMoves(board.getStaticPosition(), board.getHavingMove(), G8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, G8, F6));
      expected.add(new MoveSpecification(BLACK, G8, H6));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black moves knight out
      final MoveSpecification move = new MoveSpecification(BLACK, G8, F6);
      board.performMove(move);
    }

    {
      // white knight possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KnightLegalMoves
          .calculateKnightLegalMoves(board.getStaticPosition(), board.getHavingMove(), A3);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, A3, B5));
      expected.add(new MoveSpecification(WHITE, A3, C4));
      expected.add(new MoveSpecification(WHITE, A3, B1));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white offers black knight a pawn
      final MoveSpecification move = new MoveSpecification(WHITE, E2, E4);
      board.performMove(move);
    }

    {
      // black knight possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KnightLegalMoves
          .calculateKnightLegalMoves(board.getStaticPosition(), board.getHavingMove(), F6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, F6, E4));
      expected.add(new MoveSpecification(BLACK, F6, D5));
      expected.add(new MoveSpecification(BLACK, F6, G8));
      expected.add(new MoveSpecification(BLACK, F6, H5));
      expected.add(new MoveSpecification(BLACK, F6, G4));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black knight captures the pawn
      final MoveSpecification move = new MoveSpecification(BLACK, F6, E4);
      board.performMove(move);
    }

    {
      // white knight possible moves - unchanged
      final Set<LegalMove> calculatedLegalMoveSet = KnightLegalMoves
          .calculateKnightLegalMoves(board.getStaticPosition(), board.getHavingMove(), A3);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, A3, B5));
      expected.add(new MoveSpecification(WHITE, A3, C4));
      expected.add(new MoveSpecification(WHITE, A3, B1));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white moves knight into center
      final MoveSpecification move = new MoveSpecification(WHITE, A3, C4);
      board.performMove(move);
    }

    {
      // black offers white knight a pawn
      final Set<LegalMove> calculatedLegalMoveSet = KnightLegalMoves
          .calculateKnightLegalMoves(board.getStaticPosition(), board.getHavingMove(), E4);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E4, F6));
      expected.add(new MoveSpecification(BLACK, E4, G5));
      expected.add(new MoveSpecification(BLACK, E4, G3));
      expected.add(new MoveSpecification(BLACK, E4, F2));
      expected.add(new MoveSpecification(BLACK, E4, D2));
      expected.add(new MoveSpecification(BLACK, E4, C3));
      expected.add(new MoveSpecification(BLACK, E4, C5));
      expected.add(new MoveSpecification(BLACK, E4, D6));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black knight captures the pawn
      final MoveSpecification move = new MoveSpecification(BLACK, A7, A5);
      board.performMove(move);
    }

    {
      // white knight possible moves - unchanged
      final Set<LegalMove> calculatedLegalMoveSet = KnightLegalMoves
          .calculateKnightLegalMoves(board.getStaticPosition(), board.getHavingMove(), C4);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, C4, D6));
      expected.add(new MoveSpecification(WHITE, C4, E5));
      expected.add(new MoveSpecification(WHITE, C4, E3));
      expected.add(new MoveSpecification(WHITE, C4, A3));
      expected.add(new MoveSpecification(WHITE, C4, A5));
      expected.add(new MoveSpecification(WHITE, C4, B6));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white knight takes the pawn
      final MoveSpecification move = new MoveSpecification(WHITE, C4, A5);
      board.performMove(move);
    }

    {
      // black knight unchanged
      final Set<LegalMove> calculatedLegalMoveSet = KnightLegalMoves
          .calculateKnightLegalMoves(board.getStaticPosition(), board.getHavingMove(), E4);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E4, F6));
      expected.add(new MoveSpecification(BLACK, E4, G5));
      expected.add(new MoveSpecification(BLACK, E4, G3));
      expected.add(new MoveSpecification(BLACK, E4, F2));
      expected.add(new MoveSpecification(BLACK, E4, D2));
      expected.add(new MoveSpecification(BLACK, E4, C3));
      expected.add(new MoveSpecification(BLACK, E4, C5));
      expected.add(new MoveSpecification(BLACK, E4, D6));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black put knight in king diagonal
      final MoveSpecification move = new MoveSpecification(BLACK, B8, C6);
      board.performMove(move);
    }

    {
      // white knight possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KnightLegalMoves
          .calculateKnightLegalMoves(board.getStaticPosition(), board.getHavingMove(), A5);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, A5, B7));
      expected.add(new MoveSpecification(WHITE, A5, C6));
      expected.add(new MoveSpecification(WHITE, A5, C4));
      expected.add(new MoveSpecification(WHITE, A5, B3));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white moves knight away so black can gift the rook
      final MoveSpecification move = new MoveSpecification(WHITE, A5, B3);
      board.performMove(move);
    }

    {
      // black knight unchanged
      final Set<LegalMove> calculatedLegalMoveSet = KnightLegalMoves
          .calculateKnightLegalMoves(board.getStaticPosition(), board.getHavingMove(), E4);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E4, F6));
      expected.add(new MoveSpecification(BLACK, E4, G5));
      expected.add(new MoveSpecification(BLACK, E4, G3));
      expected.add(new MoveSpecification(BLACK, E4, F2));
      expected.add(new MoveSpecification(BLACK, E4, D2));
      expected.add(new MoveSpecification(BLACK, E4, C3));
      expected.add(new MoveSpecification(BLACK, E4, C5));
      expected.add(new MoveSpecification(BLACK, E4, D6));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black moves out rook so can be captured
      final MoveSpecification move = new MoveSpecification(BLACK, A8, A5);
      board.performMove(move);
    }

    {
      // white knight possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KnightLegalMoves
          .calculateKnightLegalMoves(board.getStaticPosition(), board.getHavingMove(), B3);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, B3, C5));
      expected.add(new MoveSpecification(WHITE, B3, D4));
      expected.add(new MoveSpecification(WHITE, B3, A5));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white knight captures the rook
      final MoveSpecification move = new MoveSpecification(WHITE, B3, A5);
      board.performMove(move);
    }

    {
      // we look at knight on C6 quickly
      final Set<LegalMove> calculatedLegalMoveSet = KnightLegalMoves
          .calculateKnightLegalMoves(board.getStaticPosition(), board.getHavingMove(), C6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, C6, E5));
      expected.add(new MoveSpecification(BLACK, C6, D4));
      expected.add(new MoveSpecification(BLACK, C6, B4));
      expected.add(new MoveSpecification(BLACK, C6, A5));
      expected.add(new MoveSpecification(BLACK, C6, A7));
      expected.add(new MoveSpecification(BLACK, C6, B8));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black moves pawn to open king diagonal
      final MoveSpecification move = new MoveSpecification(BLACK, D7, D6);
      board.performMove(move);
    }

    {
      // white knight possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KnightLegalMoves
          .calculateKnightLegalMoves(board.getStaticPosition(), board.getHavingMove(), A5);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, A5, B7));
      expected.add(new MoveSpecification(WHITE, A5, C6));
      expected.add(new MoveSpecification(WHITE, A5, C4));
      expected.add(new MoveSpecification(WHITE, A5, B3));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white moves queen out to be captured
      final MoveSpecification move = new MoveSpecification(WHITE, D1, G4);
      board.performMove(move);
    }

    {
      // black knight
      final Set<LegalMove> calculatedLegalMoveSet = KnightLegalMoves
          .calculateKnightLegalMoves(board.getStaticPosition(), board.getHavingMove(), E4);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E4, F6));
      expected.add(new MoveSpecification(BLACK, E4, G5));
      expected.add(new MoveSpecification(BLACK, E4, G3));
      expected.add(new MoveSpecification(BLACK, E4, F2));
      expected.add(new MoveSpecification(BLACK, E4, D2));
      expected.add(new MoveSpecification(BLACK, E4, C3));
      expected.add(new MoveSpecification(BLACK, E4, C5));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black makes an "escape" square for king
      final MoveSpecification move = new MoveSpecification(BLACK, E7, E6);
      board.performMove(move);
    }

    {
      // white knight possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KnightLegalMoves
          .calculateKnightLegalMoves(board.getStaticPosition(), board.getHavingMove(), A5);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, A5, B7));
      expected.add(new MoveSpecification(WHITE, A5, C6));
      expected.add(new MoveSpecification(WHITE, A5, C4));
      expected.add(new MoveSpecification(WHITE, A5, B3));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white moves bishop out
      final MoveSpecification move = new MoveSpecification(WHITE, F1, B5);
      board.performMove(move);
    }

    {
      // black knight on C6 is pinned
      final Set<LegalMove> calculatedLegalMoveSet = KnightLegalMoves
          .calculateKnightLegalMoves(board.getStaticPosition(), board.getHavingMove(), C6);

      final Set<MoveSpecification> expected = new TreeSet<>();

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black moves king out of diagonal
      final MoveSpecification move = new MoveSpecification(BLACK, E8, E7);
      board.performMove(move);
    }

    {
      // white knight possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KnightLegalMoves
          .calculateKnightLegalMoves(board.getStaticPosition(), board.getHavingMove(), A5);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, A5, B7));
      expected.add(new MoveSpecification(WHITE, A5, C6));
      expected.add(new MoveSpecification(WHITE, A5, C4));
      expected.add(new MoveSpecification(WHITE, A5, B3));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white offers queen to knight
      final MoveSpecification move = new MoveSpecification(WHITE, G4, G3);
      board.performMove(move);
    }

    {
      // black knight on C6 is unpinned
      final Set<LegalMove> calculatedLegalMoveSet = KnightLegalMoves
          .calculateKnightLegalMoves(board.getStaticPosition(), board.getHavingMove(), C6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, C6, E5));
      expected.add(new MoveSpecification(BLACK, C6, D4));
      expected.add(new MoveSpecification(BLACK, C6, B4));
      expected.add(new MoveSpecification(BLACK, C6, A5));
      expected.add(new MoveSpecification(BLACK, C6, A7));
      expected.add(new MoveSpecification(BLACK, C6, B8));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black knight takes the queen
      final MoveSpecification move = new MoveSpecification(BLACK, E4, G3);
      board.performMove(move);
    }

    {
      // we look at white knight on g1 now
      final Set<LegalMove> calculatedLegalMoveSet = KnightLegalMoves
          .calculateKnightLegalMoves(board.getStaticPosition(), board.getHavingMove(), G1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, G1, H3));
      expected.add(new MoveSpecification(WHITE, G1, F3));
      expected.add(new MoveSpecification(WHITE, G1, E2));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white knight on g1 to e2 for later ping
      final MoveSpecification move = new MoveSpecification(WHITE, G1, E2);
      board.performMove(move);
    }

    {
      // we look at black knight on g3 now again
      final Set<LegalMove> calculatedLegalMoveSet = KnightLegalMoves
          .calculateKnightLegalMoves(board.getStaticPosition(), board.getHavingMove(), G3);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, G3, H5));
      expected.add(new MoveSpecification(BLACK, G3, H1));
      expected.add(new MoveSpecification(BLACK, G3, F1));
      expected.add(new MoveSpecification(BLACK, G3, E2));
      expected.add(new MoveSpecification(BLACK, G3, E4));
      expected.add(new MoveSpecification(BLACK, G3, F5));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black king out of the way for black queen
      final MoveSpecification move = new MoveSpecification(BLACK, E7, D7);
      board.performMove(move);
    }

    {
      // we look at white knight
      final Set<LegalMove> calculatedLegalMoveSet = KnightLegalMoves
          .calculateKnightLegalMoves(board.getStaticPosition(), board.getHavingMove(), E2);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E2, F4));
      expected.add(new MoveSpecification(WHITE, E2, G3));
      expected.add(new MoveSpecification(WHITE, E2, G1));
      expected.add(new MoveSpecification(WHITE, E2, C3));
      expected.add(new MoveSpecification(WHITE, E2, D4));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // let other white knight capture a pawn
      final MoveSpecification move = new MoveSpecification(WHITE, A5, B7);
      board.performMove(move);
    }

    {
      // we look at black knight on g3 now again
      final Set<LegalMove> calculatedLegalMoveSet = KnightLegalMoves
          .calculateKnightLegalMoves(board.getStaticPosition(), board.getHavingMove(), G3);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, G3, H5));
      expected.add(new MoveSpecification(BLACK, G3, H1));
      expected.add(new MoveSpecification(BLACK, G3, F1));
      expected.add(new MoveSpecification(BLACK, G3, E2));
      expected.add(new MoveSpecification(BLACK, G3, E4));
      expected.add(new MoveSpecification(BLACK, G3, F5));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black queen to f6
      final MoveSpecification move = new MoveSpecification(BLACK, D8, F6);
      board.performMove(move);
    }

    {
      // we look at white knight
      final Set<LegalMove> calculatedLegalMoveSet = KnightLegalMoves
          .calculateKnightLegalMoves(board.getStaticPosition(), board.getHavingMove(), E2);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E2, F4));
      expected.add(new MoveSpecification(WHITE, E2, G3));
      expected.add(new MoveSpecification(WHITE, E2, G1));
      expected.add(new MoveSpecification(WHITE, E2, C3));
      expected.add(new MoveSpecification(WHITE, E2, D4));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // let other white knight capture another pawn
      final MoveSpecification move = new MoveSpecification(WHITE, B7, D6);
      board.performMove(move);
    }

    {
      // we look at black knight on g3 now again
      final Set<LegalMove> calculatedLegalMoveSet = KnightLegalMoves
          .calculateKnightLegalMoves(board.getStaticPosition(), board.getHavingMove(), G3);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, G3, H5));
      expected.add(new MoveSpecification(BLACK, G3, H1));
      expected.add(new MoveSpecification(BLACK, G3, F1));
      expected.add(new MoveSpecification(BLACK, G3, E2));
      expected.add(new MoveSpecification(BLACK, G3, E4));
      expected.add(new MoveSpecification(BLACK, G3, F5));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black queen into line with white queen
      final MoveSpecification move = new MoveSpecification(BLACK, F6, E5);
      board.performMove(move);
    }

    {
      // we look at white knight - which is pinned
      final Set<LegalMove> calculatedLegalMoveSet = KnightLegalMoves
          .calculateKnightLegalMoves(board.getStaticPosition(), board.getHavingMove(), E2);

      final Set<MoveSpecification> expected = new TreeSet<>();

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white king moves out of file with black queen
      final MoveSpecification move = new MoveSpecification(WHITE, E1, D1);
      board.performMove(move);
    }

    {
      // we look at black knight on g3 now again
      final Set<LegalMove> calculatedLegalMoveSet = KnightLegalMoves
          .calculateKnightLegalMoves(board.getStaticPosition(), board.getHavingMove(), G3);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, G3, H5));
      expected.add(new MoveSpecification(BLACK, G3, H1));
      expected.add(new MoveSpecification(BLACK, G3, F1));
      expected.add(new MoveSpecification(BLACK, G3, E2));
      expected.add(new MoveSpecification(BLACK, G3, E4));
      expected.add(new MoveSpecification(BLACK, G3, F5));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black knight captures white rook
      final MoveSpecification move = new MoveSpecification(BLACK, G3, H1);
      board.performMove(move);
    }

    {
      // we look at white knight - now unpinned
      final Set<LegalMove> calculatedLegalMoveSet = KnightLegalMoves
          .calculateKnightLegalMoves(board.getStaticPosition(), board.getHavingMove(), E2);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E2, F4));
      expected.add(new MoveSpecification(WHITE, E2, G3));
      expected.add(new MoveSpecification(WHITE, E2, G1));
      expected.add(new MoveSpecification(WHITE, E2, C3));
      expected.add(new MoveSpecification(WHITE, E2, D4));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // king establishes pin again by moving back
      final MoveSpecification move = new MoveSpecification(WHITE, D1, E1);
      board.performMove(move);
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testBishopMoves() {
    final ApiBoard board = new Board();

    // test initital position
    // white

    checkExceptionBishop(board.getStaticPosition(), board.getHavingMove(), A2);

    checkExceptionBishop(board.getStaticPosition(), board.getHavingMove(), D5);

    checkExceptionBishop(board.getStaticPosition(), board.getHavingMove(), C8);

    checkExceptionBishop(board.getStaticPosition(), board.getHavingMove(), B7);

    checkExceptionBishop(board.getStaticPosition(), board.getHavingMove(), G4);

    checkExceptionBishop(board.getStaticPosition(), board.getHavingMove(), F8);

    // now we look at moves
    {
      // white bishop possible moves
      final Set<LegalMove> calculatedLegalMoveSet = BishopLegalMoves
          .calculateBishopLegalMoves(board.getStaticPosition(), board.getHavingMove(), C1);

      final Set<MoveSpecification> expected = new TreeSet<>();

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white open square for white dark squares bishop
      final MoveSpecification move = new MoveSpecification(WHITE, D2, D3);
      board.performMove(move);
    }

    {
      // black bishop possible moves
      final Set<LegalMove> calculatedLegalMoveSet = BishopLegalMoves
          .calculateBishopLegalMoves(board.getStaticPosition(), board.getHavingMove(), C8);

      final Set<MoveSpecification> expected = new TreeSet<>();

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black open square for black light squares bishop
      final MoveSpecification move = new MoveSpecification(BLACK, D7, D6);
      board.performMove(move);
    }

    {
      // white bishop possible moves
      final Set<LegalMove> calculatedLegalMoveSet = BishopLegalMoves
          .calculateBishopLegalMoves(board.getStaticPosition(), board.getHavingMove(), C1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, C1, D2));
      expected.add(new MoveSpecification(WHITE, C1, E3));
      expected.add(new MoveSpecification(WHITE, C1, F4));
      expected.add(new MoveSpecification(WHITE, C1, G5));
      expected.add(new MoveSpecification(WHITE, C1, H6));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move bishop out
      final MoveSpecification move = new MoveSpecification(WHITE, C1, H6);
      board.performMove(move);
    }

    {
      // black bishop possible moves
      final Set<LegalMove> calculatedLegalMoveSet = BishopLegalMoves
          .calculateBishopLegalMoves(board.getStaticPosition(), board.getHavingMove(), C8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, C8, D7));
      expected.add(new MoveSpecification(BLACK, C8, E6));
      expected.add(new MoveSpecification(BLACK, C8, F5));
      expected.add(new MoveSpecification(BLACK, C8, G4));
      expected.add(new MoveSpecification(BLACK, C8, H3));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black moves bishop out
      final MoveSpecification move = new MoveSpecification(BLACK, C8, D7);
      board.performMove(move);
    }

    {
      // white bishop possible moves
      final Set<LegalMove> calculatedLegalMoveSet = BishopLegalMoves
          .calculateBishopLegalMoves(board.getStaticPosition(), board.getHavingMove(), H6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, H6, G5));
      expected.add(new MoveSpecification(WHITE, H6, F4));
      expected.add(new MoveSpecification(WHITE, H6, E3));
      expected.add(new MoveSpecification(WHITE, H6, D2));
      expected.add(new MoveSpecification(WHITE, H6, C1));
      expected.add(new MoveSpecification(WHITE, H6, G7));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move knight out
      final MoveSpecification move = new MoveSpecification(WHITE, G1, F3);
      board.performMove(move);
    }

    {
      // black bishop possible moves
      final Set<LegalMove> calculatedLegalMoveSet = BishopLegalMoves
          .calculateBishopLegalMoves(board.getStaticPosition(), board.getHavingMove(), D7);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, D7, E6));
      expected.add(new MoveSpecification(BLACK, D7, F5));
      expected.add(new MoveSpecification(BLACK, D7, G4));
      expected.add(new MoveSpecification(BLACK, D7, H3));
      expected.add(new MoveSpecification(BLACK, D7, C6));
      expected.add(new MoveSpecification(BLACK, D7, B5));
      expected.add(new MoveSpecification(BLACK, D7, A4));
      expected.add(new MoveSpecification(BLACK, D7, C8));
      check(expected, calculatedLegalMoveSet);
    }
    {
      // black moves knight out
      final MoveSpecification move = new MoveSpecification(BLACK, B8, C6);
      board.performMove(move);
    }

    {
      // white bishop possible moves
      final Set<LegalMove> calculatedLegalMoveSet = BishopLegalMoves
          .calculateBishopLegalMoves(board.getStaticPosition(), board.getHavingMove(), H6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, H6, G5));
      expected.add(new MoveSpecification(WHITE, H6, F4));
      expected.add(new MoveSpecification(WHITE, H6, E3));
      expected.add(new MoveSpecification(WHITE, H6, D2));
      expected.add(new MoveSpecification(WHITE, H6, C1));
      expected.add(new MoveSpecification(WHITE, H6, G7));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white moves knight towards own bishop
      final MoveSpecification move = new MoveSpecification(WHITE, F3, H4);
      board.performMove(move);
    }

    {
      // black bishop possible moves
      final Set<LegalMove> calculatedLegalMoveSet = BishopLegalMoves
          .calculateBishopLegalMoves(board.getStaticPosition(), board.getHavingMove(), D7);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, D7, E6));
      expected.add(new MoveSpecification(BLACK, D7, F5));
      expected.add(new MoveSpecification(BLACK, D7, G4));
      expected.add(new MoveSpecification(BLACK, D7, H3));
      expected.add(new MoveSpecification(BLACK, D7, C8));
      check(expected, calculatedLegalMoveSet);
    }
    {
      // opens square for black king
      final MoveSpecification move = new MoveSpecification(BLACK, E7, E6);
      board.performMove(move);
    }

    {
      // white bishop possible moves
      final Set<LegalMove> calculatedLegalMoveSet = BishopLegalMoves
          .calculateBishopLegalMoves(board.getStaticPosition(), board.getHavingMove(), H6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, H6, G5));
      expected.add(new MoveSpecification(WHITE, H6, F4));
      expected.add(new MoveSpecification(WHITE, H6, E3));
      expected.add(new MoveSpecification(WHITE, H6, D2));
      expected.add(new MoveSpecification(WHITE, H6, C1));
      expected.add(new MoveSpecification(WHITE, H6, G7));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white moves knight next to bishop
      final MoveSpecification move = new MoveSpecification(WHITE, H4, G6);
      board.performMove(move);
    }

    {
      // black bishop possible moves
      final Set<LegalMove> calculatedLegalMoveSet = BishopLegalMoves
          .calculateBishopLegalMoves(board.getStaticPosition(), board.getHavingMove(), D7);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, D7, C8));
      check(expected, calculatedLegalMoveSet);
    }
    {
      // black queen goes to the right
      final MoveSpecification move = new MoveSpecification(BLACK, D8, C8);
      board.performMove(move);
    }

    {
      // white bishop possible moves
      final Set<LegalMove> calculatedLegalMoveSet = BishopLegalMoves
          .calculateBishopLegalMoves(board.getStaticPosition(), board.getHavingMove(), H6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, H6, G5));
      expected.add(new MoveSpecification(WHITE, H6, F4));
      expected.add(new MoveSpecification(WHITE, H6, E3));
      expected.add(new MoveSpecification(WHITE, H6, D2));
      expected.add(new MoveSpecification(WHITE, H6, C1));
      expected.add(new MoveSpecification(WHITE, H6, G7));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white knight now blocks a few bishop fields
      final MoveSpecification move = new MoveSpecification(WHITE, G6, F4);
      board.performMove(move);
    }

    {
      // black bishop possible moves
      final Set<LegalMove> calculatedLegalMoveSet = BishopLegalMoves
          .calculateBishopLegalMoves(board.getStaticPosition(), board.getHavingMove(), D7);

      final Set<MoveSpecification> expected = new TreeSet<>();
      check(expected, calculatedLegalMoveSet);
    }
    {
      // black king goes out
      final MoveSpecification move = new MoveSpecification(BLACK, E8, E7);
      board.performMove(move);
    }

    {
      // white bishop possible moves
      final Set<LegalMove> calculatedLegalMoveSet = BishopLegalMoves
          .calculateBishopLegalMoves(board.getStaticPosition(), board.getHavingMove(), H6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, H6, G5));
      expected.add(new MoveSpecification(WHITE, H6, G7));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white king goes for a walk
      final MoveSpecification move = new MoveSpecification(WHITE, E1, D2);
      board.performMove(move);
    }

    {
      // black bishop possible moves
      final Set<LegalMove> calculatedLegalMoveSet = BishopLegalMoves
          .calculateBishopLegalMoves(board.getStaticPosition(), board.getHavingMove(), D7);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, D7, E8));
      check(expected, calculatedLegalMoveSet);
    }
    {
      // black king continues walk
      final MoveSpecification move = new MoveSpecification(BLACK, E7, F6);
      board.performMove(move);
    }

    {
      // white bishop possible moves
      final Set<LegalMove> calculatedLegalMoveSet = BishopLegalMoves
          .calculateBishopLegalMoves(board.getStaticPosition(), board.getHavingMove(), H6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, H6, G5));
      expected.add(new MoveSpecification(WHITE, H6, G7));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white king continues walk
      final MoveSpecification move = new MoveSpecification(WHITE, D2, E3);
      board.performMove(move);
    }

    {
      // black bishop possible moves
      final Set<LegalMove> calculatedLegalMoveSet = BishopLegalMoves
          .calculateBishopLegalMoves(board.getStaticPosition(), board.getHavingMove(), D7);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, D7, E8));
      check(expected, calculatedLegalMoveSet);
    }
    {
      // knight out of the way
      final MoveSpecification move = new MoveSpecification(BLACK, C6, B8);
      board.performMove(move);
    }

    {
      // white bishop possible moves
      final Set<LegalMove> calculatedLegalMoveSet = BishopLegalMoves
          .calculateBishopLegalMoves(board.getStaticPosition(), board.getHavingMove(), H6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, H6, G5));
      expected.add(new MoveSpecification(WHITE, H6, G7));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white queen enters the game
      final MoveSpecification move = new MoveSpecification(WHITE, D1, D2);
      board.performMove(move);
    }

    {
      // black bishop possible moves
      final Set<LegalMove> calculatedLegalMoveSet = BishopLegalMoves
          .calculateBishopLegalMoves(board.getStaticPosition(), board.getHavingMove(), D7);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, D7, E8));
      expected.add(new MoveSpecification(BLACK, D7, C6));
      expected.add(new MoveSpecification(BLACK, D7, B5));
      expected.add(new MoveSpecification(BLACK, D7, A4));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black bishop in line with own king
      final MoveSpecification move = new MoveSpecification(BLACK, D7, B5);
      board.performMove(move);
    }

    // two more
    {
      // white bishop possible moves
      final Set<LegalMove> calculatedLegalMoveSet = BishopLegalMoves
          .calculateBishopLegalMoves(board.getStaticPosition(), board.getHavingMove(), H6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, H6, G5));
      expected.add(new MoveSpecification(WHITE, H6, G7));

      check(expected, calculatedLegalMoveSet);
    }
    {
      final MoveSpecification move = new MoveSpecification(WHITE, E3, F3);
      board.performMove(move);
    }

    {
      // black bishop possible moves - pinned
      final Set<LegalMove> calculatedLegalMoveSet = BishopLegalMoves
          .calculateBishopLegalMoves(board.getStaticPosition(), board.getHavingMove(), B5);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, B5, C6));
      expected.add(new MoveSpecification(BLACK, B5, D7));
      expected.add(new MoveSpecification(BLACK, B5, E8));
      expected.add(new MoveSpecification(BLACK, B5, C4));
      expected.add(new MoveSpecification(BLACK, B5, D3));
      expected.add(new MoveSpecification(BLACK, B5, A4));
      expected.add(new MoveSpecification(BLACK, B5, A6));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black king moves in line with queen
      final MoveSpecification move = new MoveSpecification(BLACK, F6, F5);
      board.performMove(move);
    }
    // two more

    {
      // white bishop possible moves
      final Set<LegalMove> calculatedLegalMoveSet = BishopLegalMoves
          .calculateBishopLegalMoves(board.getStaticPosition(), board.getHavingMove(), H6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, H6, G5));
      expected.add(new MoveSpecification(WHITE, H6, G7));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white queen pins the black bishop
      final MoveSpecification move = new MoveSpecification(WHITE, D2, A5);
      board.performMove(move);
    }

    {
      // black bishop possible moves - pinned
      final Set<LegalMove> calculatedLegalMoveSet = BishopLegalMoves
          .calculateBishopLegalMoves(board.getStaticPosition(), board.getHavingMove(), B5);

      final Set<MoveSpecification> expected = new TreeSet<>();

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black king moves out of line - breaks the pin
      final MoveSpecification move = new MoveSpecification(BLACK, F5, F6);
      board.performMove(move);
    }

    // two more moves
    {
      // white bishop possible moves - the bishop can only protect the check!
      final Set<LegalMove> calculatedLegalMoveSet = BishopLegalMoves
          .calculateBishopLegalMoves(board.getStaticPosition(), board.getHavingMove(), H6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, H6, G5));
      expected.add(new MoveSpecification(WHITE, H6, G7));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white bishop checks black king
      final MoveSpecification move = new MoveSpecification(WHITE, H6, G5);
      board.performMove(move);
    }

    {
      // black bishop possible moves - king in check - no moves
      final Set<LegalMove> calculatedLegalMoveSet = BishopLegalMoves
          .calculateBishopLegalMoves(board.getStaticPosition(), board.getHavingMove(), B5);

      final Set<MoveSpecification> expected = new TreeSet<>();

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black king moves to the side
      final MoveSpecification move = new MoveSpecification(BLACK, F6, E5);
      board.performMove(move);
    }
    // two more moves

    {
      // white bishop possible moves
      final Set<LegalMove> calculatedLegalMoveSet = BishopLegalMoves
          .calculateBishopLegalMoves(board.getStaticPosition(), board.getHavingMove(), G5);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, G5, H6));
      expected.add(new MoveSpecification(WHITE, G5, H4));
      expected.add(new MoveSpecification(WHITE, G5, F6));
      expected.add(new MoveSpecification(WHITE, G5, E7));
      expected.add(new MoveSpecification(WHITE, G5, D8));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white moves knight out of the way of bishop
      final MoveSpecification move = new MoveSpecification(WHITE, F4, H3);
      board.performMove(move);
    }

    {
      // black bishop possible moves - pinned
      final Set<LegalMove> calculatedLegalMoveSet = BishopLegalMoves
          .calculateBishopLegalMoves(board.getStaticPosition(), board.getHavingMove(), B5);

      final Set<MoveSpecification> expected = new TreeSet<>();

      check(expected, calculatedLegalMoveSet);
    }
    {
      final MoveSpecification move = new MoveSpecification(BLACK, C8, D8);
      board.performMove(move);
    }

    // two more moves
    {
      // white bishop possible moves
      final Set<LegalMove> calculatedLegalMoveSet = BishopLegalMoves
          .calculateBishopLegalMoves(board.getStaticPosition(), board.getHavingMove(), G5);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, G5, H6));
      expected.add(new MoveSpecification(WHITE, G5, H4));
      expected.add(new MoveSpecification(WHITE, G5, F4));
      expected.add(new MoveSpecification(WHITE, G5, E3));
      expected.add(new MoveSpecification(WHITE, G5, D2));
      expected.add(new MoveSpecification(WHITE, G5, C1));
      expected.add(new MoveSpecification(WHITE, G5, F6));
      expected.add(new MoveSpecification(WHITE, G5, E7));
      expected.add(new MoveSpecification(WHITE, G5, D8));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white moves knight out of the way of bishop
      final MoveSpecification move = new MoveSpecification(WHITE, H1, G1);
      board.performMove(move);
    }
    {
      // black bishop possible moves - pinned
      final Set<LegalMove> calculatedLegalMoveSet = BishopLegalMoves
          .calculateBishopLegalMoves(board.getStaticPosition(), board.getHavingMove(), B5);

      final Set<MoveSpecification> expected = new TreeSet<>();

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black queen enters the game and checks the white king
      final MoveSpecification move = new MoveSpecification(BLACK, D8, F6);
      board.performMove(move);
    }
    // two more moves

    {
      // white bishop possible moves - the bishop can only protect the check!
      final Set<LegalMove> calculatedLegalMoveSet = BishopLegalMoves
          .calculateBishopLegalMoves(board.getStaticPosition(), board.getHavingMove(), G5);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, G5, F4));
      expected.add(new MoveSpecification(WHITE, G5, F6));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white bishop moves before king for check
      final MoveSpecification move = new MoveSpecification(WHITE, G5, F4);
      board.performMove(move);
    }

    {
      // black bishop possible moves - pinned
      final Set<LegalMove> calculatedLegalMoveSet = BishopLegalMoves
          .calculateBishopLegalMoves(board.getStaticPosition(), board.getHavingMove(), B5);

      final Set<MoveSpecification> expected = new TreeSet<>();

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black king moves out of check
      final MoveSpecification move = new MoveSpecification(BLACK, E5, D5);
      board.performMove(move);
    }

    {
      // white bishop possible moves - pinned
      final Set<LegalMove> calculatedLegalMoveSet = BishopLegalMoves
          .calculateBishopLegalMoves(board.getStaticPosition(), board.getHavingMove(), F4);

      final Set<MoveSpecification> expected = new TreeSet<>();

      check(expected, calculatedLegalMoveSet);
    }
    {
      final MoveSpecification move = new MoveSpecification(WHITE, G1, H1);
      board.performMove(move);
    }

    {
      // black bishop possible moves - pinned
      final Set<LegalMove> calculatedLegalMoveSet = BishopLegalMoves
          .calculateBishopLegalMoves(board.getStaticPosition(), board.getHavingMove(), B5);

      final Set<MoveSpecification> expected = new TreeSet<>();

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black king breaks the pin
      final MoveSpecification move = new MoveSpecification(BLACK, D5, C6);
      board.performMove(move);
    }

    {
      // white bishop possible moves - pinned
      final Set<LegalMove> calculatedLegalMoveSet = BishopLegalMoves
          .calculateBishopLegalMoves(board.getStaticPosition(), board.getHavingMove(), F4);

      final Set<MoveSpecification> expected = new TreeSet<>();

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white king moves out of line with black queen - breaking the pin
      final MoveSpecification move = new MoveSpecification(WHITE, F3, G3);
      board.performMove(move);
    }

    {
      // black bishop possible moves - pinned
      final Set<LegalMove> calculatedLegalMoveSet = BishopLegalMoves
          .calculateBishopLegalMoves(board.getStaticPosition(), board.getHavingMove(), B5);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, B5, C4));
      expected.add(new MoveSpecification(BLACK, B5, D3));
      expected.add(new MoveSpecification(BLACK, B5, A4));
      expected.add(new MoveSpecification(BLACK, B5, A6));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black bishop goes to edge of the board
      final MoveSpecification move = new MoveSpecification(BLACK, B5, A4);
      board.performMove(move);
    }

    {
      // white bishop possible moves - not pinned anymore
      final Set<LegalMove> calculatedLegalMoveSet = BishopLegalMoves
          .calculateBishopLegalMoves(board.getStaticPosition(), board.getHavingMove(), F4);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, F4, G5));
      expected.add(new MoveSpecification(WHITE, F4, H6));
      expected.add(new MoveSpecification(WHITE, F4, E3));
      expected.add(new MoveSpecification(WHITE, F4, D2));
      expected.add(new MoveSpecification(WHITE, F4, C1));
      expected.add(new MoveSpecification(WHITE, F4, E5));
      expected.add(new MoveSpecification(WHITE, F4, D6));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white bishop captures a pawn now
      final MoveSpecification move = new MoveSpecification(WHITE, F4, D6);
      board.performMove(move);
    }
  }

  // Queens cannot be pinged!! As they would deliver check themselves already.
  @SuppressWarnings("static-method")
  @Test
  void testQueenMoves() {
    final ApiBoard board = new Board();

    // test initital position
    // white

    checkExceptionQueen(board.getStaticPosition(), board.getHavingMove(), A2);

    checkExceptionQueen(board.getStaticPosition(), board.getHavingMove(), D5);

    checkExceptionQueen(board.getStaticPosition(), board.getHavingMove(), D8);

    checkExceptionQueen(board.getStaticPosition(), board.getHavingMove(), B7);

    checkExceptionQueen(board.getStaticPosition(), board.getHavingMove(), G4);

    checkExceptionQueen(board.getStaticPosition(), board.getHavingMove(), D8);

    // nowe we look at moves
    {
      // white queen possible moves
      final Set<LegalMove> calculatedLegalMoveSet = QueenLegalMoves.calculateQueenLegalMoves(board.getStaticPosition(),
          board.getHavingMove(), D1);

      final Set<MoveSpecification> expected = new TreeSet<>();

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white moves a central pawn
      final MoveSpecification move = new MoveSpecification(WHITE, E2, E4);
      board.performMove(move);
    }

    {
      // black queen possible moves
      final Set<LegalMove> calculatedLegalMoveSet = QueenLegalMoves.calculateQueenLegalMoves(board.getStaticPosition(),
          board.getHavingMove(), D8);

      final Set<MoveSpecification> expected = new TreeSet<>();

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black moves a central pawn
      final MoveSpecification move = new MoveSpecification(BLACK, D7, D5);
      board.performMove(move);
    }

    {
      // white queen possible moves
      final Set<LegalMove> calculatedLegalMoveSet = QueenLegalMoves.calculateQueenLegalMoves(board.getStaticPosition(),
          board.getHavingMove(), D1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, D1, E2));
      expected.add(new MoveSpecification(WHITE, D1, F3));
      expected.add(new MoveSpecification(WHITE, D1, G4));
      expected.add(new MoveSpecification(WHITE, D1, H5));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white moves queen out
      final MoveSpecification move = new MoveSpecification(WHITE, D1, H5);
      board.performMove(move);
    }

    {
      // black queen possible moves
      final Set<LegalMove> calculatedLegalMoveSet = QueenLegalMoves.calculateQueenLegalMoves(board.getStaticPosition(),
          board.getHavingMove(), D8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, D8, D7));
      expected.add(new MoveSpecification(BLACK, D8, D6));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black moves queen out
      final MoveSpecification move = new MoveSpecification(BLACK, D8, D6);
      board.performMove(move);
    }

    {
      // white queen possible moves
      final Set<LegalMove> calculatedLegalMoveSet = QueenLegalMoves.calculateQueenLegalMoves(board.getStaticPosition(),
          board.getHavingMove(), H5);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, H5, H6));
      expected.add(new MoveSpecification(WHITE, H5, H7));
      expected.add(new MoveSpecification(WHITE, H5, H4));
      expected.add(new MoveSpecification(WHITE, H5, H3));
      expected.add(new MoveSpecification(WHITE, H5, G4));
      expected.add(new MoveSpecification(WHITE, H5, F3));
      expected.add(new MoveSpecification(WHITE, H5, E2));
      expected.add(new MoveSpecification(WHITE, H5, D1));
      expected.add(new MoveSpecification(WHITE, H5, G5));
      expected.add(new MoveSpecification(WHITE, H5, F5));
      expected.add(new MoveSpecification(WHITE, H5, E5));
      expected.add(new MoveSpecification(WHITE, H5, D5));
      expected.add(new MoveSpecification(WHITE, H5, G6));
      expected.add(new MoveSpecification(WHITE, H5, F7));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white crashes queen in
      final MoveSpecification move = new MoveSpecification(WHITE, H5, H7);
      board.performMove(move);
    }

    {
      // black queen possible moves
      final Set<LegalMove> calculatedLegalMoveSet = QueenLegalMoves.calculateQueenLegalMoves(board.getStaticPosition(),
          board.getHavingMove(), D6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, D6, D7));
      expected.add(new MoveSpecification(BLACK, D6, D8));
      expected.add(new MoveSpecification(BLACK, D6, E6));
      expected.add(new MoveSpecification(BLACK, D6, F6));
      expected.add(new MoveSpecification(BLACK, D6, G6));
      expected.add(new MoveSpecification(BLACK, D6, H6));
      expected.add(new MoveSpecification(BLACK, D6, E5));
      expected.add(new MoveSpecification(BLACK, D6, F4));
      expected.add(new MoveSpecification(BLACK, D6, G3));
      expected.add(new MoveSpecification(BLACK, D6, H2));
      expected.add(new MoveSpecification(BLACK, D6, C5));
      expected.add(new MoveSpecification(BLACK, D6, B4));
      expected.add(new MoveSpecification(BLACK, D6, A3));
      expected.add(new MoveSpecification(BLACK, D6, C6));
      expected.add(new MoveSpecification(BLACK, D6, B6));
      expected.add(new MoveSpecification(BLACK, D6, A6));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black moves bishop out
      final MoveSpecification move = new MoveSpecification(BLACK, C8, H3);
      board.performMove(move);
    }

    {
      // white queen possible moves
      final Set<LegalMove> calculatedLegalMoveSet = QueenLegalMoves.calculateQueenLegalMoves(board.getStaticPosition(),
          board.getHavingMove(), H7);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, H7, H8));
      expected.add(new MoveSpecification(WHITE, H7, H6));
      expected.add(new MoveSpecification(WHITE, H7, H5));
      expected.add(new MoveSpecification(WHITE, H7, H4));
      expected.add(new MoveSpecification(WHITE, H7, H3));
      expected.add(new MoveSpecification(WHITE, H7, G6));
      expected.add(new MoveSpecification(WHITE, H7, F5));
      expected.add(new MoveSpecification(WHITE, H7, G7));
      expected.add(new MoveSpecification(WHITE, H7, G8));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white crashes queen in
      final MoveSpecification move = new MoveSpecification(WHITE, F1, A6);
      board.performMove(move);
    }

    {
      // black queen possible moves
      final Set<LegalMove> calculatedLegalMoveSet = QueenLegalMoves.calculateQueenLegalMoves(board.getStaticPosition(),
          board.getHavingMove(), D6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, D6, D7));
      expected.add(new MoveSpecification(BLACK, D6, D8));
      expected.add(new MoveSpecification(BLACK, D6, E6));
      expected.add(new MoveSpecification(BLACK, D6, F6));
      expected.add(new MoveSpecification(BLACK, D6, G6));
      expected.add(new MoveSpecification(BLACK, D6, H6));
      expected.add(new MoveSpecification(BLACK, D6, E5));
      expected.add(new MoveSpecification(BLACK, D6, F4));
      expected.add(new MoveSpecification(BLACK, D6, G3));
      expected.add(new MoveSpecification(BLACK, D6, H2));
      expected.add(new MoveSpecification(BLACK, D6, C5));
      expected.add(new MoveSpecification(BLACK, D6, B4));
      expected.add(new MoveSpecification(BLACK, D6, A3));
      expected.add(new MoveSpecification(BLACK, D6, C6));
      expected.add(new MoveSpecification(BLACK, D6, B6));
      expected.add(new MoveSpecification(BLACK, D6, A6));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black moves knight out
      final MoveSpecification move = new MoveSpecification(BLACK, B8, C6);
      board.performMove(move);
    }

    {
      // white queen possible moves
      final Set<LegalMove> calculatedLegalMoveSet = QueenLegalMoves.calculateQueenLegalMoves(board.getStaticPosition(),
          board.getHavingMove(), H7);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, H7, H8));
      expected.add(new MoveSpecification(WHITE, H7, H6));
      expected.add(new MoveSpecification(WHITE, H7, H5));
      expected.add(new MoveSpecification(WHITE, H7, H4));
      expected.add(new MoveSpecification(WHITE, H7, H3));
      expected.add(new MoveSpecification(WHITE, H7, G6));
      expected.add(new MoveSpecification(WHITE, H7, F5));
      expected.add(new MoveSpecification(WHITE, H7, G7));
      expected.add(new MoveSpecification(WHITE, H7, G8));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white retreats queen a bit
      final MoveSpecification move = new MoveSpecification(WHITE, H7, F5);
      board.performMove(move);
    }

    {
      // black queen possible moves
      final Set<LegalMove> calculatedLegalMoveSet = QueenLegalMoves.calculateQueenLegalMoves(board.getStaticPosition(),
          board.getHavingMove(), D6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, D6, D7));
      expected.add(new MoveSpecification(BLACK, D6, D8));
      expected.add(new MoveSpecification(BLACK, D6, E6));
      expected.add(new MoveSpecification(BLACK, D6, F6));
      expected.add(new MoveSpecification(BLACK, D6, G6));
      expected.add(new MoveSpecification(BLACK, D6, H6));
      expected.add(new MoveSpecification(BLACK, D6, E5));
      expected.add(new MoveSpecification(BLACK, D6, F4));
      expected.add(new MoveSpecification(BLACK, D6, G3));
      expected.add(new MoveSpecification(BLACK, D6, H2));
      expected.add(new MoveSpecification(BLACK, D6, C5));
      expected.add(new MoveSpecification(BLACK, D6, B4));
      expected.add(new MoveSpecification(BLACK, D6, A3));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black moves queeen to crush in on white king later...
      final MoveSpecification move = new MoveSpecification(BLACK, D6, B4);
      board.performMove(move);
    }

    {
      // white queen possible moves
      final Set<LegalMove> calculatedLegalMoveSet = QueenLegalMoves.calculateQueenLegalMoves(board.getStaticPosition(),
          board.getHavingMove(), F5);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, F5, F6));
      expected.add(new MoveSpecification(WHITE, F5, F7));
      expected.add(new MoveSpecification(WHITE, F5, G6));
      expected.add(new MoveSpecification(WHITE, F5, H7));
      expected.add(new MoveSpecification(WHITE, F5, G5));
      expected.add(new MoveSpecification(WHITE, F5, H5));
      expected.add(new MoveSpecification(WHITE, F5, G4));
      expected.add(new MoveSpecification(WHITE, F5, H3));
      expected.add(new MoveSpecification(WHITE, F5, F4));
      expected.add(new MoveSpecification(WHITE, F5, F3));
      expected.add(new MoveSpecification(WHITE, F5, E5));
      expected.add(new MoveSpecification(WHITE, F5, D5));
      expected.add(new MoveSpecification(WHITE, F5, E6));
      expected.add(new MoveSpecification(WHITE, F5, D7));
      expected.add(new MoveSpecification(WHITE, F5, C8));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white queen rushes in to black king
      final MoveSpecification move = new MoveSpecification(WHITE, F5, F7);
      board.performMove(move);
    }

    {
      // black queen possible moves - because king must move out of check first and
      // queen cannot block - empty
      final Set<LegalMove> calculatedLegalMoveSet = QueenLegalMoves.calculateQueenLegalMoves(board.getStaticPosition(),
          board.getHavingMove(), B4);

      final Set<MoveSpecification> expected = new TreeSet<>();

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black king moves out of check
      final MoveSpecification move = new MoveSpecification(BLACK, E8, D8);
      board.performMove(move);
    }

    {
      // white queen possible moves
      final Set<LegalMove> calculatedLegalMoveSet = QueenLegalMoves.calculateQueenLegalMoves(board.getStaticPosition(),
          board.getHavingMove(), F7);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, F7, F8));
      expected.add(new MoveSpecification(WHITE, F7, G8));
      expected.add(new MoveSpecification(WHITE, F7, G7));
      expected.add(new MoveSpecification(WHITE, F7, G6));
      expected.add(new MoveSpecification(WHITE, F7, H5));
      expected.add(new MoveSpecification(WHITE, F7, F6));
      expected.add(new MoveSpecification(WHITE, F7, F5));
      expected.add(new MoveSpecification(WHITE, F7, F4));
      expected.add(new MoveSpecification(WHITE, F7, F3));
      expected.add(new MoveSpecification(WHITE, F7, E6));
      expected.add(new MoveSpecification(WHITE, F7, D5));
      expected.add(new MoveSpecification(WHITE, F7, E7));
      expected.add(new MoveSpecification(WHITE, F7, E8));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white queen captures a knight
      final MoveSpecification move = new MoveSpecification(WHITE, F7, G8);
      board.performMove(move);
    }

    {
      // black queen can move now
      final Set<LegalMove> calculatedLegalMoveSet = QueenLegalMoves.calculateQueenLegalMoves(board.getStaticPosition(),
          board.getHavingMove(), B4);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, B4, B5));
      expected.add(new MoveSpecification(BLACK, B4, B6));
      expected.add(new MoveSpecification(BLACK, B4, C5));
      expected.add(new MoveSpecification(BLACK, B4, D6));
      expected.add(new MoveSpecification(BLACK, B4, C4));
      expected.add(new MoveSpecification(BLACK, B4, D4));
      expected.add(new MoveSpecification(BLACK, B4, E4));
      expected.add(new MoveSpecification(BLACK, B4, C3));
      expected.add(new MoveSpecification(BLACK, B4, D2));
      expected.add(new MoveSpecification(BLACK, B4, B3));
      expected.add(new MoveSpecification(BLACK, B4, B2));
      expected.add(new MoveSpecification(BLACK, B4, A3));
      expected.add(new MoveSpecification(BLACK, B4, A4));
      expected.add(new MoveSpecification(BLACK, B4, A5));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black queen crushes in on white king
      final MoveSpecification move = new MoveSpecification(BLACK, B4, D2);
      board.performMove(move);
    }

    {
      // white queen possible moves - white queen in check and queen can do nothing
      // about it - so no legal moves with queen
      final Set<LegalMove> calculatedLegalMoveSet = QueenLegalMoves.calculateQueenLegalMoves(board.getStaticPosition(),
          board.getHavingMove(), G8);

      final Set<MoveSpecification> expected = new TreeSet<>();

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white king moves away
      final MoveSpecification move = new MoveSpecification(WHITE, E1, F1);
      board.performMove(move);
    }

    {
      // black queen can move now
      final Set<LegalMove> calculatedLegalMoveSet = QueenLegalMoves.calculateQueenLegalMoves(board.getStaticPosition(),
          board.getHavingMove(), D2);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, D2, D3));
      expected.add(new MoveSpecification(BLACK, D2, D4));
      expected.add(new MoveSpecification(BLACK, D2, E3));
      expected.add(new MoveSpecification(BLACK, D2, F4));
      expected.add(new MoveSpecification(BLACK, D2, G5));
      expected.add(new MoveSpecification(BLACK, D2, H6));
      expected.add(new MoveSpecification(BLACK, D2, E2));
      expected.add(new MoveSpecification(BLACK, D2, F2));
      expected.add(new MoveSpecification(BLACK, D2, E1));
      expected.add(new MoveSpecification(BLACK, D2, D1));
      expected.add(new MoveSpecification(BLACK, D2, C1));
      expected.add(new MoveSpecification(BLACK, D2, C2));
      expected.add(new MoveSpecification(BLACK, D2, C3));
      expected.add(new MoveSpecification(BLACK, D2, B4));
      expected.add(new MoveSpecification(BLACK, D2, A5));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black queen takes a pawn
      final MoveSpecification move = new MoveSpecification(BLACK, D2, C2);
      board.performMove(move);
    }

    {
      // white queen possible moves
      final Set<LegalMove> calculatedLegalMoveSet = QueenLegalMoves.calculateQueenLegalMoves(board.getStaticPosition(),
          board.getHavingMove(), G8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, G8, H8));
      expected.add(new MoveSpecification(WHITE, G8, H7));
      expected.add(new MoveSpecification(WHITE, G8, G7));
      expected.add(new MoveSpecification(WHITE, G8, F7));
      expected.add(new MoveSpecification(WHITE, G8, E6));
      expected.add(new MoveSpecification(WHITE, G8, D5));
      expected.add(new MoveSpecification(WHITE, G8, F8));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white queen takes the rook, finally...
      final MoveSpecification move = new MoveSpecification(WHITE, G8, H8);
      board.performMove(move);
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testPawnMoves() {
    final ApiBoard board = new Board();

    // test initital position
    // white

    checkExceptionPawn(board.getStaticPosition(), board.getEnPassantCaptureTargetSquare(), board.getHavingMove(), A1);

    checkExceptionPawn(board.getStaticPosition(), board.getEnPassantCaptureTargetSquare(), board.getHavingMove(), D5);

    checkExceptionPawn(board.getStaticPosition(), board.getEnPassantCaptureTargetSquare(), board.getHavingMove(), D7);

    checkExceptionPawn(board.getStaticPosition(), board.getEnPassantCaptureTargetSquare(), board.getHavingMove(), B1);

    checkExceptionPawn(board.getStaticPosition(), board.getEnPassantCaptureTargetSquare(), board.getHavingMove(), G4);

    checkExceptionPawn(board.getStaticPosition(), board.getEnPassantCaptureTargetSquare(), board.getHavingMove(), D8);

    // now we look at moves
    {
      // white pawn possible moves
      final Set<LegalMove> calculatedLegalMoveSet = PawnLegalMoves.calculatePawnLegalMoves(board.getStaticPosition(),
          board.getEnPassantCaptureTargetSquare(), board.getHavingMove(), E2);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E2, E3));
      expected.add(new MoveSpecification(WHITE, E2, E4));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white pawn one square advance
      final MoveSpecification move = new MoveSpecification(WHITE, E2, E3);
      board.performMove(move);
    }

    {
      // black pawn possible moves
      final Set<LegalMove> calculatedLegalMoveSet = PawnLegalMoves.calculatePawnLegalMoves(board.getStaticPosition(),
          board.getEnPassantCaptureTargetSquare(), board.getHavingMove(), D7);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, D7, D6));
      expected.add(new MoveSpecification(BLACK, D7, D5));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black pawn one square advance
      final MoveSpecification move = new MoveSpecification(BLACK, D7, D6);
      board.performMove(move);
    }

    {
      // white pawn possible moves
      final Set<LegalMove> calculatedLegalMoveSet = PawnLegalMoves.calculatePawnLegalMoves(board.getStaticPosition(),
          board.getEnPassantCaptureTargetSquare(), board.getHavingMove(), D2);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, D2, D3));
      expected.add(new MoveSpecification(WHITE, D2, D4));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white pawn two squares advance
      final MoveSpecification move = new MoveSpecification(WHITE, D2, D4);
      board.performMove(move);
    }

    {
      // black pawn possible moves
      final Set<LegalMove> calculatedLegalMoveSet = PawnLegalMoves.calculatePawnLegalMoves(board.getStaticPosition(),
          board.getEnPassantCaptureTargetSquare(), board.getHavingMove(), F7);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, F7, F6));
      expected.add(new MoveSpecification(BLACK, F7, F5));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black pawn two squares advance
      final MoveSpecification move = new MoveSpecification(BLACK, F7, F5);
      board.performMove(move);
    }

    {
      // white pawn possible moves
      final Set<LegalMove> calculatedLegalMoveSet = PawnLegalMoves.calculatePawnLegalMoves(board.getStaticPosition(),
          board.getEnPassantCaptureTargetSquare(), board.getHavingMove(), D4);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, D4, D5));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white pawn one squares advance
      final MoveSpecification move = new MoveSpecification(WHITE, D4, D5);
      board.performMove(move);
    }

    {
      // black pawn possible moves
      final Set<LegalMove> calculatedLegalMoveSet = PawnLegalMoves.calculatePawnLegalMoves(board.getStaticPosition(),
          board.getEnPassantCaptureTargetSquare(), board.getHavingMove(), E7);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E7, E6));
      expected.add(new MoveSpecification(BLACK, E7, E5));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black pawn two squares advance so white can capture en passant
      final MoveSpecification move = new MoveSpecification(BLACK, E7, E5);
      board.performMove(move);
    }

    {
      // white pawn possible moves - can capture en passant
      final Set<LegalMove> calculatedLegalMoveSet = PawnLegalMoves.calculatePawnLegalMoves(board.getStaticPosition(),
          board.getEnPassantCaptureTargetSquare(), board.getHavingMove(), D5);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, D5, E6));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white pawn - en passant capture
      final MoveSpecification move = new MoveSpecification(WHITE, D5, E6);
      board.performMove(move);
    }

    {
      // black pawn possible moves
      final Set<LegalMove> calculatedLegalMoveSet = PawnLegalMoves.calculatePawnLegalMoves(board.getStaticPosition(),
          board.getEnPassantCaptureTargetSquare(), board.getHavingMove(), F5);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, F5, F4));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black pawn advance so can capture en passant later
      final MoveSpecification move = new MoveSpecification(BLACK, F5, F4);
      board.performMove(move);
    }

    {
      // white pawn possible moves
      final Set<LegalMove> calculatedLegalMoveSet = PawnLegalMoves.calculatePawnLegalMoves(board.getStaticPosition(),
          board.getEnPassantCaptureTargetSquare(), board.getHavingMove(), G2);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, G2, G3));
      expected.add(new MoveSpecification(WHITE, G2, G4));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white pawn two square advance to allow black en passant capture
      final MoveSpecification move = new MoveSpecification(WHITE, G2, G4);
      board.performMove(move);
    }

    {
      // black en passant capture possiblle
      final Set<LegalMove> calculatedLegalMoveSet = PawnLegalMoves.calculatePawnLegalMoves(board.getStaticPosition(),
          board.getEnPassantCaptureTargetSquare(), board.getHavingMove(), F4);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, F4, G3));
      expected.add(new MoveSpecification(BLACK, F4, F3));
      expected.add(new MoveSpecification(BLACK, F4, E3));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black en passant capture
      final MoveSpecification move = new MoveSpecification(BLACK, F4, G3);
      board.performMove(move);
    }

    {
      // white pawn possible moves
      final Set<LegalMove> calculatedLegalMoveSet = PawnLegalMoves.calculatePawnLegalMoves(board.getStaticPosition(),
          board.getEnPassantCaptureTargetSquare(), board.getHavingMove(), E6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E6, E7));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white pawn goes for promotion
      final MoveSpecification move = new MoveSpecification(WHITE, E6, E7);
      board.performMove(move);
    }

    {
      // black en passant capture possiblle
      final Set<LegalMove> calculatedLegalMoveSet = PawnLegalMoves.calculatePawnLegalMoves(board.getStaticPosition(),
          board.getEnPassantCaptureTargetSquare(), board.getHavingMove(), G3);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, G3, H2));
      expected.add(new MoveSpecification(BLACK, G3, G2));
      expected.add(new MoveSpecification(BLACK, G3, F2));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black pawn goes for promotion
      final MoveSpecification move = new MoveSpecification(BLACK, G3, G2);
      board.performMove(move);
    }

    {
      // white pawn possible moves
      final Set<LegalMove> calculatedLegalMoveSet = PawnLegalMoves.calculatePawnLegalMoves(board.getStaticPosition(),
          board.getEnPassantCaptureTargetSquare(), board.getHavingMove(), E7);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E7, D8, PromotionPieceType.ROOK));
      expected.add(new MoveSpecification(WHITE, E7, D8, PromotionPieceType.KNIGHT));
      expected.add(new MoveSpecification(WHITE, E7, D8, PromotionPieceType.BISHOP));
      expected.add(new MoveSpecification(WHITE, E7, D8, PromotionPieceType.QUEEN));
      expected.add(new MoveSpecification(WHITE, E7, F8, PromotionPieceType.ROOK));
      expected.add(new MoveSpecification(WHITE, E7, F8, PromotionPieceType.KNIGHT));
      expected.add(new MoveSpecification(WHITE, E7, F8, PromotionPieceType.BISHOP));
      expected.add(new MoveSpecification(WHITE, E7, F8, PromotionPieceType.QUEEN));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white pawn promotes to queen
      final MoveSpecification move = new MoveSpecification(WHITE, E7, D8, PromotionPieceType.QUEEN);
      board.performMove(move);
    }

    {
      // black in check - the pawn has no legal moves
      final Set<LegalMove> calculatedLegalMoveSet = PawnLegalMoves.calculatePawnLegalMoves(board.getStaticPosition(),
          board.getEnPassantCaptureTargetSquare(), board.getHavingMove(), G2);

      final Set<MoveSpecification> expected = new TreeSet<>();

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black captures promoted pawn
      final MoveSpecification move = new MoveSpecification(BLACK, E8, D8);
      board.performMove(move);
    }

    {
      // white pawn possible moves
      final Set<LegalMove> calculatedLegalMoveSet = PawnLegalMoves.calculatePawnLegalMoves(board.getStaticPosition(),
          board.getEnPassantCaptureTargetSquare(), board.getHavingMove(), A2);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, A2, A3));
      expected.add(new MoveSpecification(WHITE, A2, A4));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white moves another pawn
      final MoveSpecification move = new MoveSpecification(WHITE, A2, A3);
      board.performMove(move);
    }

    {
      final Set<LegalMove> calculatedLegalMoveSet = PawnLegalMoves.calculatePawnLegalMoves(board.getStaticPosition(),
          board.getEnPassantCaptureTargetSquare(), board.getHavingMove(), G2);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, G2, H1, PromotionPieceType.ROOK));
      expected.add(new MoveSpecification(BLACK, G2, H1, PromotionPieceType.KNIGHT));
      expected.add(new MoveSpecification(BLACK, G2, H1, PromotionPieceType.BISHOP));
      expected.add(new MoveSpecification(BLACK, G2, H1, PromotionPieceType.QUEEN));

      expected.add(new MoveSpecification(BLACK, G2, F1, PromotionPieceType.ROOK));
      expected.add(new MoveSpecification(BLACK, G2, F1, PromotionPieceType.KNIGHT));
      expected.add(new MoveSpecification(BLACK, G2, F1, PromotionPieceType.BISHOP));
      expected.add(new MoveSpecification(BLACK, G2, F1, PromotionPieceType.QUEEN));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black now promotes the pawn
      final MoveSpecification move = new MoveSpecification(BLACK, G2, F1, PromotionPieceType.KNIGHT);
      board.performMove(move);
    }

    {
      final Set<LegalMove> calculatedLegalMoveSet = PawnLegalMoves.calculatePawnLegalMoves(board.getStaticPosition(),
          board.getEnPassantCaptureTargetSquare(), board.getHavingMove(), A3);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, A3, A4));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white captures the promoted knight
      final MoveSpecification move = new MoveSpecification(WHITE, E1, F1);
      board.performMove(move);
    }

    // we now create a position where a black pawn is pinned against moving forward
    {
      final MoveSpecification move = new MoveSpecification(BLACK, D8, D7);
      board.performMove(move);
    }
    {
      final MoveSpecification move = new MoveSpecification(WHITE, D1, D4);
      board.performMove(move);
    }
    {
      // black pawn can move forward
      final Set<LegalMove> calculatedLegalMoveSet = PawnLegalMoves.calculatePawnLegalMoves(board.getStaticPosition(),
          board.getEnPassantCaptureTargetSquare(), board.getHavingMove(), D6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, D6, D5));

      check(expected, calculatedLegalMoveSet);
    }
    {
      final MoveSpecification move = new MoveSpecification(BLACK, D7, E6);
      board.performMove(move);
    }
    {
      final MoveSpecification move = new MoveSpecification(WHITE, D4, B6);
      board.performMove(move);
    }
    {
      // black pawn is pinned
      final Set<LegalMove> calculatedLegalMoveSet = PawnLegalMoves.calculatePawnLegalMoves(board.getStaticPosition(),
          board.getEnPassantCaptureTargetSquare(), board.getHavingMove(), D6);

      final Set<MoveSpecification> expected = new TreeSet<>();

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black king moves out of line with queen
      final MoveSpecification move = new MoveSpecification(BLACK, E6, F7);
      board.performMove(move);
    }
    // white moves queen on same rank
    {
      final MoveSpecification move = new MoveSpecification(WHITE, B6, A6);
      board.performMove(move);
    }
    {
      // black pawn now unpinned
      final Set<LegalMove> calculatedLegalMoveSet = PawnLegalMoves.calculatePawnLegalMoves(board.getStaticPosition(),
          board.getEnPassantCaptureTargetSquare(), board.getHavingMove(), D6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, D6, D5));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black king moves back to e8
      final MoveSpecification move = new MoveSpecification(BLACK, F7, E8);
      board.performMove(move);
    }

    // we now create a position where white pawn cannot capture en passant for own
    // king would be in check
    {
      final MoveSpecification move = new MoveSpecification(WHITE, F2, F4);
      board.performMove(move);
    }
    {
      final MoveSpecification move = new MoveSpecification(BLACK, F8, E7);
      board.performMove(move);
    }

    {
      final MoveSpecification move = new MoveSpecification(WHITE, F4, F5);
      board.performMove(move);
    }
    {
      final MoveSpecification move = new MoveSpecification(BLACK, G8, H6);
      board.performMove(move);
    }

    {
      final MoveSpecification move = new MoveSpecification(WHITE, F1, F2);
      board.performMove(move);
    }
    {
      final MoveSpecification move = new MoveSpecification(BLACK, H8, F8);
      board.performMove(move);
    }

    // white pawn can move forward
    {
      final Set<LegalMove> calculatedLegalMoveSet = PawnLegalMoves.calculatePawnLegalMoves(board.getStaticPosition(),
          board.getEnPassantCaptureTargetSquare(), board.getHavingMove(), F5);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, F5, F6));

      check(expected, calculatedLegalMoveSet);
    }
    {
      final MoveSpecification move = new MoveSpecification(WHITE, F2, F3);
      board.performMove(move);
    }
    {
      final MoveSpecification move = new MoveSpecification(BLACK, G7, G5);
      board.performMove(move);
    }
    // white pawn can move forward but not capture en passant for being pinned
    {
      final Set<LegalMove> calculatedLegalMoveSet = PawnLegalMoves.calculatePawnLegalMoves(board.getStaticPosition(),
          board.getEnPassantCaptureTargetSquare(), board.getHavingMove(), F5);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, F5, F6));

      check(expected, calculatedLegalMoveSet);
    }
    {
      final MoveSpecification move = new MoveSpecification(WHITE, F3, F2);
      board.performMove(move);
    }
    {
      final MoveSpecification move = new MoveSpecification(BLACK, F8, G8);
      board.performMove(move);
    }
    // white pawn can move forward but not capture en passant even it not pinned
    // anymore, as en passant capture right is gone
    {
      final Set<LegalMove> calculatedLegalMoveSet = PawnLegalMoves.calculatePawnLegalMoves(board.getStaticPosition(),
          board.getEnPassantCaptureTargetSquare(), board.getHavingMove(), F5);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, F5, F6));

      check(expected, calculatedLegalMoveSet);
    }
    {
      final MoveSpecification move = new MoveSpecification(WHITE, F5, F6);
      board.performMove(move);
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testKingMoves() {
    testKingInitialMoves();

    testKingWalk();

    testKingKingSideCastlingMoves();
    testKingQueenSideCastlingMoves();

    testKingKingSideCastlingRightChangeForRookMove();
    testKingKingSideCastlingRightChangeForKingMove();

    testKingQueenSideCastlingRightChangeForRookMove();
    testKingQueenSideCastlingRightChangeForKingMove();

    testKingKingQueenSideCastlingRightChangeForKingMove();

  }

  static void testKingInitialMoves() {
    final ApiBoard board = new Board();

    // test initital position
    // white

    checkExceptionKing(board.getStaticPosition(), board.getCastlingRight(WHITE), board.getHavingMove(), A2);

    checkExceptionKing(board.getStaticPosition(), board.getCastlingRight(WHITE), board.getHavingMove(), D5);

    checkExceptionKing(board.getStaticPosition(), board.getCastlingRight(WHITE), board.getHavingMove(), E8);

    checkExceptionKing(board.getStaticPosition(), board.getCastlingRight(WHITE), board.getHavingMove(), B7);

    checkExceptionKing(board.getStaticPosition(), board.getCastlingRight(WHITE), board.getHavingMove(), G4);

    checkExceptionKing(board.getStaticPosition(), board.getCastlingRight(WHITE), board.getHavingMove(), E2);

  }

  static void testKingWalk() {
    final ApiBoard board = new Board();
    {
      // white king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, E2, E4);
      board.performMove(move);
    }

    {
      // black king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, E7, E5);
      board.performMove(move);
    }

    {
      // white king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E1, E2));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, E1, E2);
      board.performMove(move);
    }

    {
      // black king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E8, E7));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, E8, E7);
      board.performMove(move);
    }

    {
      // white king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E2);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E2, E3));
      expected.add(new MoveSpecification(WHITE, E2, F3));
      expected.add(new MoveSpecification(WHITE, E2, E1));
      expected.add(new MoveSpecification(WHITE, E2, D3));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, E2, E3);
      board.performMove(move);
    }

    {
      // black king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E7);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E7, E6));
      expected.add(new MoveSpecification(BLACK, E7, D6));
      expected.add(new MoveSpecification(BLACK, E7, E8));
      expected.add(new MoveSpecification(BLACK, E7, F6));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, E7, E6);
      board.performMove(move);
    }

    {
      // white king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E3);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E3, F3));
      expected.add(new MoveSpecification(WHITE, E3, E2));
      expected.add(new MoveSpecification(WHITE, E3, D3));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, E3, D3);
      board.performMove(move);
    }

    {
      // black king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E6, D6));
      expected.add(new MoveSpecification(BLACK, E6, E7));
      expected.add(new MoveSpecification(BLACK, E6, F6));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, E6, D6);
      board.performMove(move);
    }

    {
      // white king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), D3);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, D3, E3));
      expected.add(new MoveSpecification(WHITE, D3, E2));
      expected.add(new MoveSpecification(WHITE, D3, C3));
      expected.add(new MoveSpecification(WHITE, D3, C4));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, D3, C4);
      board.performMove(move);
    }

    {
      // black king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), D6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, D6, C6));
      expected.add(new MoveSpecification(BLACK, D6, E7));
      expected.add(new MoveSpecification(BLACK, D6, E6));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, D6, C6);
      board.performMove(move);
    }

    {
      // white king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), C4);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, C4, D3));
      expected.add(new MoveSpecification(WHITE, C4, C3));
      expected.add(new MoveSpecification(WHITE, C4, B3));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move invalid
      final MoveSpecification move = new MoveSpecification(WHITE, C4, C5);
      // check
      assertThrows(InvalidMoveException.class, () -> {
        board.performMove(move);
      });
    }
    {
      // white move valid
      final MoveSpecification move = new MoveSpecification(WHITE, C4, B3);
      board.performMove(move);
    }

    {
      // black king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), C6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, C6, C5));
      expected.add(new MoveSpecification(BLACK, C6, B6));
      expected.add(new MoveSpecification(BLACK, C6, D6));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move invalid
      final MoveSpecification move = new MoveSpecification(BLACK, C6, D5);
      // check
      assertThrows(InvalidMoveException.class, () -> {
        board.performMove(move);
      });
    }
    {
      // black move valid
      final MoveSpecification move = new MoveSpecification(BLACK, C6, B6);
      board.performMove(move);
    }

    {
      // white king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), B3);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, B3, C4));
      expected.add(new MoveSpecification(WHITE, B3, C3));
      expected.add(new MoveSpecification(WHITE, B3, A4));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // prepares check
      final MoveSpecification move = new MoveSpecification(WHITE, D1, H5);
      board.performMove(move);
    }

    {
      // black king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), B6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, B6, A5));
      expected.add(new MoveSpecification(BLACK, B6, C6));
      expected.add(new MoveSpecification(BLACK, B6, C5));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // prepares check
      final MoveSpecification move = new MoveSpecification(BLACK, D8, H4);
      board.performMove(move);
    }

    {
      // white king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), B3);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, B3, C4));
      expected.add(new MoveSpecification(WHITE, B3, C3));
      expected.add(new MoveSpecification(WHITE, B3, A4));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // checks
      final MoveSpecification move = new MoveSpecification(WHITE, H5, H6);
      board.performMove(move);
    }

    {
      // black king possible moves - move out of check
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), B6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, B6, A5));
      expected.add(new MoveSpecification(BLACK, B6, C5));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // blocks check with pawn
      final MoveSpecification move = new MoveSpecification(BLACK, C7, C6);
      board.performMove(move);
    }

    {
      // white king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), B3);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, B3, C4));
      expected.add(new MoveSpecification(WHITE, B3, C3));
      expected.add(new MoveSpecification(WHITE, B3, A4));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // captures pawn
      final MoveSpecification move = new MoveSpecification(WHITE, H6, C6);
      board.performMove(move);
    }

    {
      // black king possible moves - move away or take queen
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), B6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, B6, A5));
      expected.add(new MoveSpecification(BLACK, B6, C6));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // king takes queen
      final MoveSpecification move = new MoveSpecification(BLACK, B6, C6);
      board.performMove(move);
    }

    {
      // white king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), B3);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, B3, C4));
      expected.add(new MoveSpecification(WHITE, B3, C3));
      expected.add(new MoveSpecification(WHITE, B3, A4));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // pawn towards queen
      final MoveSpecification move = new MoveSpecification(WHITE, H2, H3);
      board.performMove(move);
    }

    {
      // black king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), C6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, C6, C5));
      expected.add(new MoveSpecification(BLACK, C6, B6));
      expected.add(new MoveSpecification(BLACK, C6, C7));
      expected.add(new MoveSpecification(BLACK, C6, D6));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // king takes pawn and checks
      final MoveSpecification move = new MoveSpecification(BLACK, H4, H3);
      board.performMove(move);
    }

    {
      // white king possible moves - move out of check
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), B3);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, B3, C4));
      expected.add(new MoveSpecification(WHITE, B3, A4));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // block with knight
      final MoveSpecification move = new MoveSpecification(WHITE, B1, C3);
      board.performMove(move);
    }

    {
      // black king possible moves - move away or take queen
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), C6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, C6, C5));
      expected.add(new MoveSpecification(BLACK, C6, B6));
      expected.add(new MoveSpecification(BLACK, C6, C7));
      expected.add(new MoveSpecification(BLACK, C6, D6));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // queen takes knight
      final MoveSpecification move = new MoveSpecification(BLACK, H3, C3);
      board.performMove(move);
    }

    {
      // white king possible moves - move out of check or take queen
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), B3);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, B3, A4));
      expected.add(new MoveSpecification(WHITE, B3, C3));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // king takes queen
      final MoveSpecification move = new MoveSpecification(WHITE, B3, C3);
      board.performMove(move);
    }

    {
      // black king possible moves - move away or take queen
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), C6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, C6, C5));
      expected.add(new MoveSpecification(BLACK, C6, B6));
      expected.add(new MoveSpecification(BLACK, C6, C7));
      expected.add(new MoveSpecification(BLACK, C6, D6));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // queen takes knight
      final MoveSpecification move = new MoveSpecification(BLACK, C6, B6);
      board.performMove(move);
    }

    {
      // white king possible moves - move out of check or take queen
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), C3);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, C3, C4));
      expected.add(new MoveSpecification(WHITE, C3, D3));
      expected.add(new MoveSpecification(WHITE, C3, B3));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // king takes queen
      final MoveSpecification move = new MoveSpecification(WHITE, C3, B3);
      board.performMove(move);
    }

    {
      // black king possible moves - move away or take queen
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), B6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, B6, A5));
      expected.add(new MoveSpecification(BLACK, B6, C7));
      expected.add(new MoveSpecification(BLACK, B6, C6));
      expected.add(new MoveSpecification(BLACK, B6, C5));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // takes knight out
      final MoveSpecification move = new MoveSpecification(BLACK, B8, C6);
      board.performMove(move);
    }

    {
      // white king possible moves - move out of check or take queen
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), B3);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, B3, C4));
      expected.add(new MoveSpecification(WHITE, B3, C3));
      expected.add(new MoveSpecification(WHITE, B3, A4));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // bishop to black king
      final MoveSpecification move = new MoveSpecification(WHITE, F1, A6);
      board.performMove(move);
    }

    {
      // black king possible moves - move away or take queen
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), B6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, B6, A5));
      expected.add(new MoveSpecification(BLACK, B6, A6));
      expected.add(new MoveSpecification(BLACK, B6, C7));
      expected.add(new MoveSpecification(BLACK, B6, C5));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // bishop to black king
      final MoveSpecification move = new MoveSpecification(BLACK, F8, A3);
      board.performMove(move);
    }

    {
      // white king possible moves - move out of check or take queen
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), B3);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, B3, C4));
      expected.add(new MoveSpecification(WHITE, B3, C3));
      expected.add(new MoveSpecification(WHITE, B3, A3));
      expected.add(new MoveSpecification(WHITE, B3, A4));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // bishop to black king
      final MoveSpecification move = new MoveSpecification(WHITE, C2, C3);
      board.performMove(move);
    }

    {
      // black king possible moves - move away or take queen
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), B6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, B6, A5));
      expected.add(new MoveSpecification(BLACK, B6, A6));
      expected.add(new MoveSpecification(BLACK, B6, C7));
      expected.add(new MoveSpecification(BLACK, B6, C5));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // bishop takes a pawn
      final MoveSpecification move = new MoveSpecification(BLACK, A3, B2);
      board.performMove(move);
    }

    {
      // white king possible moves - move out of check or take queen
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), B3);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, B3, C4));
      expected.add(new MoveSpecification(WHITE, B3, C2));
      expected.add(new MoveSpecification(WHITE, B3, B2));
      expected.add(new MoveSpecification(WHITE, B3, A4));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white king towards black king
      final MoveSpecification move = new MoveSpecification(WHITE, B3, A4);
      board.performMove(move);
    }

    {
      // black king possible moves - move away or take queen
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), B6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, B6, A6));
      expected.add(new MoveSpecification(BLACK, B6, C7));
      expected.add(new MoveSpecification(BLACK, B6, C5));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // king takes the bishop
      final MoveSpecification move = new MoveSpecification(BLACK, B6, A6);
      board.performMove(move);
    }

    {
      // white king possible moves - move out of check or take queen
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), A4);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, A4, B3));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // bishop takes bishop
      final MoveSpecification move = new MoveSpecification(WHITE, C1, B2);
      board.performMove(move);
    }

    {
      // black king possible moves - move away or take queen
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), A6);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, A6, B6));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // a silly rook move for the end
      final MoveSpecification move = new MoveSpecification(BLACK, A8, B8);
      board.performMove(move);
    }

  }

  static void testKingKingSideCastlingMoves() {
    final ApiBoard board = new Board();
    {
      // white king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, E2, E4);
      board.performMove(move);
    }

    {
      // black king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, E7, E5);
      board.performMove(move);
    }

    {
      // white king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E1, E2));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, D2, D4);
      board.performMove(move);
    }

    {
      // black king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E8, E7));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, D7, D5);
      board.performMove(move);
    }

    {
      // white king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E1, E2));
      expected.add(new MoveSpecification(WHITE, E1, D2));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, F1, A6);
      board.performMove(move);
    }

    {
      // black king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E8, E7));
      expected.add(new MoveSpecification(BLACK, E8, D7));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, F8, A3);
      board.performMove(move);
    }

    {
      // white king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E1, E2));
      expected.add(new MoveSpecification(WHITE, E1, D2));
      expected.add(new MoveSpecification(WHITE, E1, F1));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, G1, H3);
      board.performMove(move);
    }

    {
      // black king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E8, E7));
      expected.add(new MoveSpecification(BLACK, E8, D7));
      expected.add(new MoveSpecification(BLACK, E8, F8));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, G8, H6);
      board.performMove(move);
    }

    {
      // white king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E1, E2));
      expected.add(new MoveSpecification(WHITE, E1, D2));
      expected.add(new MoveSpecification(WHITE, E1, F1));
      expected.add(new MoveSpecification(WHITE, CastlingMove.KING_SIDE));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, A6, D3);
      board.performMove(move);
    }

    {
      // black king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E8, E7));
      expected.add(new MoveSpecification(BLACK, E8, D7));
      expected.add(new MoveSpecification(BLACK, E8, F8));
      expected.add(new MoveSpecification(BLACK, CastlingMove.KING_SIDE));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, A3, D6);
      board.performMove(move);
    }

    {
      // white king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E1, E2));
      expected.add(new MoveSpecification(WHITE, E1, D2));
      expected.add(new MoveSpecification(WHITE, E1, F1));
      expected.add(new MoveSpecification(WHITE, CastlingMove.KING_SIDE));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, F2, F4);
      board.performMove(move);
    }

    {
      // black king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E8, E7));
      expected.add(new MoveSpecification(BLACK, E8, D7));
      expected.add(new MoveSpecification(BLACK, E8, F8));
      expected.add(new MoveSpecification(BLACK, CastlingMove.KING_SIDE));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, F7, F5);
      board.performMove(move);
    }

    {
      // white king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E1, E2));
      expected.add(new MoveSpecification(WHITE, E1, F2));
      expected.add(new MoveSpecification(WHITE, E1, F1));
      expected.add(new MoveSpecification(WHITE, E1, D2));
      expected.add(new MoveSpecification(WHITE, CastlingMove.KING_SIDE));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, D1, H5);
      board.performMove(move);
    }

    {
      // black king possible moves - checked - castling not possible
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E8, E7));
      expected.add(new MoveSpecification(BLACK, E8, D7));
      expected.add(new MoveSpecification(BLACK, E8, F8));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move - blocks check with pawn
      final MoveSpecification move = new MoveSpecification(BLACK, G7, G6);
      board.performMove(move);
    }

    {
      // white king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E1, E2));
      expected.add(new MoveSpecification(WHITE, E1, F2));
      expected.add(new MoveSpecification(WHITE, E1, F1));
      expected.add(new MoveSpecification(WHITE, E1, D1));
      expected.add(new MoveSpecification(WHITE, E1, D2));
      expected.add(new MoveSpecification(WHITE, CastlingMove.KING_SIDE));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, D3, A6);
      board.performMove(move);
    }

    {
      // black king possible moves - castling possible
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E8, E7));
      expected.add(new MoveSpecification(BLACK, E8, D7));
      expected.add(new MoveSpecification(BLACK, E8, F8));
      expected.add(new MoveSpecification(BLACK, E8, F7));
      expected.add(new MoveSpecification(BLACK, CastlingMove.KING_SIDE));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move - blocks check with pawn
      final MoveSpecification move = new MoveSpecification(BLACK, D8, H4);
      board.performMove(move);
    }

    {
      // white king possible moves - checked - castling not possible
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E1, E2));
      expected.add(new MoveSpecification(WHITE, E1, F1));
      expected.add(new MoveSpecification(WHITE, E1, D1));
      expected.add(new MoveSpecification(WHITE, E1, D2));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move - blocks check with pawn
      final MoveSpecification move = new MoveSpecification(WHITE, G2, G3);
      board.performMove(move);
    }

    {
      // black king possible moves - castling possible
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E8, E7));
      expected.add(new MoveSpecification(BLACK, E8, D7));
      expected.add(new MoveSpecification(BLACK, E8, D8));
      expected.add(new MoveSpecification(BLACK, E8, F8));
      expected.add(new MoveSpecification(BLACK, E8, F7));
      expected.add(new MoveSpecification(BLACK, CastlingMove.KING_SIDE));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, D6, A3);
      board.performMove(move);
    }

    {
      // white king possible moves - castling not possible
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E1, E2));
      expected.add(new MoveSpecification(WHITE, E1, F2));
      expected.add(new MoveSpecification(WHITE, E1, F1));
      expected.add(new MoveSpecification(WHITE, E1, D1));
      expected.add(new MoveSpecification(WHITE, E1, D2));
      expected.add(new MoveSpecification(WHITE, CastlingMove.KING_SIDE));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move - blocks black castling with bishop - step 1
      final MoveSpecification move = new MoveSpecification(WHITE, A6, C4);
      board.performMove(move);
    }

    {
      // black king possible moves - castling possible
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E8, E7));
      expected.add(new MoveSpecification(BLACK, E8, D7));
      expected.add(new MoveSpecification(BLACK, E8, D8));
      expected.add(new MoveSpecification(BLACK, E8, F8));
      expected.add(new MoveSpecification(BLACK, E8, F7));
      expected.add(new MoveSpecification(BLACK, CastlingMove.KING_SIDE));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move - blocks white castling with bishop - step 1
      final MoveSpecification move = new MoveSpecification(BLACK, A3, C5);
      board.performMove(move);
    }

    {
      // white king possible moves - castling possible
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E1, E2));
      expected.add(new MoveSpecification(WHITE, E1, F2));
      expected.add(new MoveSpecification(WHITE, E1, F1));
      expected.add(new MoveSpecification(WHITE, E1, D1));
      expected.add(new MoveSpecification(WHITE, E1, D2));
      expected.add(new MoveSpecification(WHITE, CastlingMove.KING_SIDE));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move - blocks black castling with bishop - step 2
      final MoveSpecification move = new MoveSpecification(WHITE, C4, D5);
      board.performMove(move);
    }

    {
      // black king possible moves - castling not possible
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E8, E7));
      expected.add(new MoveSpecification(BLACK, E8, D7));
      expected.add(new MoveSpecification(BLACK, E8, D8));
      expected.add(new MoveSpecification(BLACK, E8, F8));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move - blocks white castling with bishop - step 2
      final MoveSpecification move = new MoveSpecification(BLACK, C5, D4);
      board.performMove(move);
    }

    {
      // white king possible moves - castling not possible
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E1, E2));
      expected.add(new MoveSpecification(WHITE, E1, F1));
      expected.add(new MoveSpecification(WHITE, E1, D1));
      expected.add(new MoveSpecification(WHITE, E1, D2));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move - bishop makes harakiri
      final MoveSpecification move = new MoveSpecification(WHITE, D5, B7);
      board.performMove(move);
    }

    {
      // black king possible moves - castling possible
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E8, E7));
      expected.add(new MoveSpecification(BLACK, E8, D7));
      expected.add(new MoveSpecification(BLACK, E8, D8));
      expected.add(new MoveSpecification(BLACK, E8, F8));
      expected.add(new MoveSpecification(BLACK, E8, F7));
      expected.add(new MoveSpecification(BLACK, CastlingMove.KING_SIDE));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move - castling kingside
      final MoveSpecification move = new MoveSpecification(BLACK, CastlingMove.KING_SIDE);
      board.performMove(move);
    }

    {
      // white king possible moves - castling not possible
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E1, E2));
      expected.add(new MoveSpecification(WHITE, E1, F1));
      expected.add(new MoveSpecification(WHITE, E1, D1));
      expected.add(new MoveSpecification(WHITE, E1, D2));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move - bishop takes rook
      final MoveSpecification move = new MoveSpecification(WHITE, B7, A8);
      board.performMove(move);
    }

    {
      // black king possible moves - castling happened
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), G8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, G8, G7));
      expected.add(new MoveSpecification(BLACK, G8, F7));
      expected.add(new MoveSpecification(BLACK, G8, H8));
      expected.add(new MoveSpecification(BLACK, G8, F7));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move - bishop makes harakiri
      final MoveSpecification move = new MoveSpecification(BLACK, D4, B2);
      board.performMove(move);
    }

    {
      // white king possible moves - castling possible
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E1, E2));
      expected.add(new MoveSpecification(WHITE, E1, F2));
      expected.add(new MoveSpecification(WHITE, E1, F1));
      expected.add(new MoveSpecification(WHITE, E1, D1));
      expected.add(new MoveSpecification(WHITE, E1, D2));
      expected.add(new MoveSpecification(WHITE, CastlingMove.KING_SIDE));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move - castling kingside
      final MoveSpecification move = new MoveSpecification(WHITE, CastlingMove.KING_SIDE);
      board.performMove(move);
    }

    {
      // black king possible moves - castling happened
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), G8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, G8, G7));
      expected.add(new MoveSpecification(BLACK, G8, F7));
      expected.add(new MoveSpecification(BLACK, G8, H8));
      expected.add(new MoveSpecification(BLACK, G8, F7));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move - bishop takes the rook
      final MoveSpecification move = new MoveSpecification(BLACK, B2, A1);
      board.performMove(move);
    }

    {
      // white king possible moves - castling happened
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), G1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, G1, G2));
      expected.add(new MoveSpecification(WHITE, G1, H1));
      expected.add(new MoveSpecification(WHITE, G1, F2));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move - castling happened kingside
      final MoveSpecification move = new MoveSpecification(WHITE, G1, H1);
      board.performMove(move);
    }
  }

  static void testKingQueenSideCastlingMoves() {
    final ApiBoard board = new Board();
    {
      // white king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, E2, E4);
      board.performMove(move);
    }

    {
      // black king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, E7, E5);
      board.performMove(move);
    }

    {
      // white king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E1, E2));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, D2, D4);
      board.performMove(move);
    }

    {
      // black king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E8, E7));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, D7, D5);
      board.performMove(move);
    }

    {
      // white king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E1, E2));
      expected.add(new MoveSpecification(WHITE, E1, D2));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, B1, A3);
      board.performMove(move);
    }

    {
      // black king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E8, E7));
      expected.add(new MoveSpecification(BLACK, E8, D7));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, B8, A6);
      board.performMove(move);
    }

    {
      // white king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E1, E2));
      expected.add(new MoveSpecification(WHITE, E1, D2));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, C2, C4);
      board.performMove(move);
    }

    {
      // black king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E8, E7));
      expected.add(new MoveSpecification(BLACK, E8, D7));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, C7, C5);
      board.performMove(move);
    }

    {
      // white king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E1, E2));
      expected.add(new MoveSpecification(WHITE, E1, D2));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, D1, B3);
      board.performMove(move);
    }

    {
      // black king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E8, E7));
      expected.add(new MoveSpecification(BLACK, E8, D7));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, D8, B6);
      board.performMove(move);
    }

    {
      // white king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E1, E2));
      expected.add(new MoveSpecification(WHITE, E1, D1));
      expected.add(new MoveSpecification(WHITE, E1, D2));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, C1, D2);
      board.performMove(move);
    }

    {
      // black king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E8, E7));
      expected.add(new MoveSpecification(BLACK, E8, D7));
      expected.add(new MoveSpecification(BLACK, E8, D8));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, C8, D7);
      board.performMove(move);
    }

    {
      // white king possible moves - castling possible queenside first time
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E1, E2));
      expected.add(new MoveSpecification(WHITE, E1, D1));
      expected.add(new MoveSpecification(WHITE, CastlingMove.QUEEN_SIDE));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, D2, E3);
      board.performMove(move);
    }

    {
      // black king possible moves - castling possible queenside first time
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E8, E7));
      expected.add(new MoveSpecification(BLACK, E8, D8));
      expected.add(new MoveSpecification(BLACK, CastlingMove.QUEEN_SIDE));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, D7, E6);
      board.performMove(move);
    }

    {
      // white king possible moves - castling possible queenside first time
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E1, E2));
      expected.add(new MoveSpecification(WHITE, E1, D1));
      expected.add(new MoveSpecification(WHITE, E1, D2));
      expected.add(new MoveSpecification(WHITE, CastlingMove.QUEEN_SIDE));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move - check black king
      final MoveSpecification move = new MoveSpecification(WHITE, B3, A4);
      board.performMove(move);
    }

    {
      // black king possible moves - checked - castling not possible
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E8, E7));
      expected.add(new MoveSpecification(BLACK, E8, D8));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move - moves bishop into check
      final MoveSpecification move = new MoveSpecification(BLACK, E6, D7);
      board.performMove(move);
    }

    {
      // white king possible moves - castling possible queenside
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E1, E2));
      expected.add(new MoveSpecification(WHITE, E1, D1));
      expected.add(new MoveSpecification(WHITE, E1, D2));
      expected.add(new MoveSpecification(WHITE, CastlingMove.QUEEN_SIDE));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move - queen "sacrifice"
      final MoveSpecification move = new MoveSpecification(WHITE, A4, A6);
      board.performMove(move);
    }

    {
      // black king possible moves - no more check - castling possible
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E8, E7));
      expected.add(new MoveSpecification(BLACK, E8, D8));
      expected.add(new MoveSpecification(BLACK, CastlingMove.QUEEN_SIDE));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move - checks
      final MoveSpecification move = new MoveSpecification(BLACK, B6, A5);
      board.performMove(move);
    }

    {
      // white king possible moves - checked - castling not possible
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E1, E2));
      expected.add(new MoveSpecification(WHITE, E1, D1));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move - bishop blocks check
      final MoveSpecification move = new MoveSpecification(WHITE, E3, D2);
      board.performMove(move);
    }

    {
      // black king possible moves - no more check - castling possible
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E8, E7));
      expected.add(new MoveSpecification(BLACK, E8, D8));
      expected.add(new MoveSpecification(BLACK, CastlingMove.QUEEN_SIDE));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move - queen "sacrifice"
      final MoveSpecification move = new MoveSpecification(BLACK, A5, A3);
      board.performMove(move);
    }

    {
      // white king possible moves - no more checked - castling possible
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E1, E2));
      expected.add(new MoveSpecification(WHITE, E1, D1));
      expected.add(new MoveSpecification(WHITE, CastlingMove.QUEEN_SIDE));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move - bishop to block black castling
      final MoveSpecification move = new MoveSpecification(WHITE, D2, G5);
      board.performMove(move);
    }

    {
      // black king possible moves - king would need to pass check - castling not possible
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move - bishop to block white castling
      final MoveSpecification move = new MoveSpecification(BLACK, D7, G4);
      board.performMove(move);
    }

    {
      // white king possible moves - king would need to pass check - castling not possible
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E1, D2));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move - bring out knight to allow castling
      final MoveSpecification move = new MoveSpecification(WHITE, G1, E2);
      board.performMove(move);
    }

    {
      // black king possible moves - king would need to pass check - castling not possible
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E8, D7));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move - bring out knight to allow castling
      final MoveSpecification move = new MoveSpecification(BLACK, G8, E7);
      board.performMove(move);
    }

    {
      // white king possible moves - castling now popssible again
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E1, D1));
      expected.add(new MoveSpecification(WHITE, E1, D2));
      expected.add(new MoveSpecification(WHITE, CastlingMove.QUEEN_SIDE));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move - castling
      final MoveSpecification move = new MoveSpecification(WHITE, CastlingMove.QUEEN_SIDE);
      board.performMove(move);
    }

    {
      // black king possible moves - castling now possible again
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E8, D7));
      expected.add(new MoveSpecification(BLACK, E8, D8));
      expected.add(new MoveSpecification(BLACK, CastlingMove.QUEEN_SIDE));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move - castling
      final MoveSpecification move = new MoveSpecification(BLACK, CastlingMove.QUEEN_SIDE);
      board.performMove(move);
    }
  }

  static void testKingKingSideCastlingRightChangeForRookMove() {
    final ApiBoard board = new Board();
    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, E2, E4);
      board.performMove(move);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, E7, E5);
      board.performMove(move);
    }
    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, G1, F3);
      board.performMove(move);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, G8, F6);
      board.performMove(move);
    }
    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, F1, C4);
      board.performMove(move);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, F8, C5);
      board.performMove(move);
    }
    {
      // white king possible moves - castling possible king side
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E1, E2));
      expected.add(new MoveSpecification(WHITE, E1, F1));
      expected.add(new MoveSpecification(WHITE, CastlingMove.KING_SIDE));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move - king side castling gone by rook move
      final MoveSpecification move = new MoveSpecification(WHITE, H1, F1);
      board.performMove(move);
    }
    {
      // black king possible moves - castling possible king side
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E8, E7));
      expected.add(new MoveSpecification(BLACK, E8, F8));
      expected.add(new MoveSpecification(BLACK, CastlingMove.KING_SIDE));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move - king side castling gone by rook move
      final MoveSpecification move = new MoveSpecification(BLACK, H8, F8);
      board.performMove(move);
    }
    {
      // white king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E1, E2));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move - rook moves back
      final MoveSpecification move = new MoveSpecification(WHITE, F1, H1);
      board.performMove(move);
    }
    {
      // black king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E8, E7));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move - rook moves back
      final MoveSpecification move = new MoveSpecification(BLACK, F8, H8);
      board.performMove(move);
    }

    {
      // white king possible moves - no king side castling
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E1, E2));
      expected.add(new MoveSpecification(WHITE, E1, F1));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move - knight out
      final MoveSpecification move = new MoveSpecification(WHITE, B1, C3);
      board.performMove(move);
    }

    {
      // black king possible moves - no king side castling
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E8, E7));
      expected.add(new MoveSpecification(BLACK, E8, F8));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move - knight out
      final MoveSpecification move = new MoveSpecification(BLACK, B8, C6);
      board.performMove(move);
    }
  }

  static void testKingKingSideCastlingRightChangeForKingMove() {
    final ApiBoard board = new Board();
    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, E2, E4);
      board.performMove(move);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, E7, E5);
      board.performMove(move);
    }
    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, G1, F3);
      board.performMove(move);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, G8, F6);
      board.performMove(move);
    }
    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, F1, C4);
      board.performMove(move);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, F8, C5);
      board.performMove(move);
    }
    {
      // white king possible moves - castling possible king side
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E1, E2));
      expected.add(new MoveSpecification(WHITE, E1, F1));
      expected.add(new MoveSpecification(WHITE, CastlingMove.KING_SIDE));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move - king side castling gone by king move
      final MoveSpecification move = new MoveSpecification(WHITE, E1, F1);
      board.performMove(move);
    }
    {
      // black king possible moves - castling possible king side
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E8, E7));
      expected.add(new MoveSpecification(BLACK, E8, F8));
      expected.add(new MoveSpecification(BLACK, CastlingMove.KING_SIDE));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move - king side castling gone by king move
      final MoveSpecification move = new MoveSpecification(BLACK, E8, F8);
      board.performMove(move);
    }
    {
      // white king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), F1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, F1, G1));
      expected.add(new MoveSpecification(WHITE, F1, E1));
      expected.add(new MoveSpecification(WHITE, F1, E2));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move - king moves back
      final MoveSpecification move = new MoveSpecification(WHITE, F1, E1);
      board.performMove(move);
    }
    {
      // black king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), F8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, F8, E7));
      expected.add(new MoveSpecification(BLACK, F8, E8));
      expected.add(new MoveSpecification(BLACK, F8, G8));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move - king moves back
      final MoveSpecification move = new MoveSpecification(BLACK, F8, E8);
      board.performMove(move);
    }

    {
      // white king possible moves - no king side castling
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E1, E2));
      expected.add(new MoveSpecification(WHITE, E1, F1));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move - knight out
      final MoveSpecification move = new MoveSpecification(WHITE, B1, C3);
      board.performMove(move);
    }

    {
      // black king possible moves - no king side castling
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E8, E7));
      expected.add(new MoveSpecification(BLACK, E8, F8));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move - knight out
      final MoveSpecification move = new MoveSpecification(BLACK, B8, C6);
      board.performMove(move);
    }
  }

  static void testKingQueenSideCastlingRightChangeForRookMove() {
    final ApiBoard board = new Board();
    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, B2, B3);
      board.performMove(move);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, B7, B6);
      board.performMove(move);
    }

    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, C1, B2);
      board.performMove(move);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, C8, B7);
      board.performMove(move);
    }

    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, B1, C3);
      board.performMove(move);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, B8, C6);
      board.performMove(move);
    }

    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, D2, D3);
      board.performMove(move);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, D7, D6);
      board.performMove(move);
    }

    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, D1, D2);
      board.performMove(move);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, D8, D7);
      board.performMove(move);
    }

    {
      // white king possible moves - queen side castling
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E1, D1));
      expected.add(new MoveSpecification(WHITE, CastlingMove.QUEEN_SIDE));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move - move queenside rook away
      final MoveSpecification move = new MoveSpecification(WHITE, A1, D1);
      board.performMove(move);
    }

    {
      // black king possible moves - queen side castling
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E8, D8));
      expected.add(new MoveSpecification(BLACK, CastlingMove.QUEEN_SIDE));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move - move queenside rook away
      final MoveSpecification move = new MoveSpecification(BLACK, A8, D8);
      board.performMove(move);
    }

    {
      // white king possible moves - none
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move - move queenside rook back
      final MoveSpecification move = new MoveSpecification(WHITE, D1, A1);
      board.performMove(move);
    }

    {
      // black king possible moves - none
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move - move queenside rook back
      final MoveSpecification move = new MoveSpecification(BLACK, D8, A8);
      board.performMove(move);
    }

    {
      // white king possible moves - no castling
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E1, D1));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move - move knight out
      final MoveSpecification move = new MoveSpecification(WHITE, G1, F3);
      board.performMove(move);
    }

    {
      // black king possible moves - no castling
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E8, D8));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move - move knight out
      final MoveSpecification move = new MoveSpecification(BLACK, G8, F6);
      board.performMove(move);
    }
  }

  static void testKingQueenSideCastlingRightChangeForKingMove() {
    final ApiBoard board = new Board();
    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, D2, D4);
      board.performMove(move);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, D7, D5);
      board.performMove(move);
    }

    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, B1, C3);
      board.performMove(move);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, B8, C6);
      board.performMove(move);
    }

    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, C1, F4);
      board.performMove(move);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, C8, F5);
      board.performMove(move);
    }

    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, D1, D3);
      board.performMove(move);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, D8, D6);
      board.performMove(move);
    }

    {
      // white king possible moves - queen side castling
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E1, D1));
      expected.add(new MoveSpecification(WHITE, E1, D2));
      expected.add(new MoveSpecification(WHITE, CastlingMove.QUEEN_SIDE));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move - move king away
      final MoveSpecification move = new MoveSpecification(WHITE, E1, D1);
      board.performMove(move);
    }

    {
      // black king possible moves - queen side castling
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E8, D7));
      expected.add(new MoveSpecification(BLACK, E8, D8));
      expected.add(new MoveSpecification(BLACK, CastlingMove.QUEEN_SIDE));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move - move king away
      final MoveSpecification move = new MoveSpecification(BLACK, E8, D8);
      board.performMove(move);
    }

    {
      // white king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), D1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, D1, D2));
      expected.add(new MoveSpecification(WHITE, D1, E1));
      expected.add(new MoveSpecification(WHITE, D1, C1));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move - move king back
      final MoveSpecification move = new MoveSpecification(WHITE, D1, E1);
      board.performMove(move);
    }

    {
      // black king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), D8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, D8, D7));
      expected.add(new MoveSpecification(BLACK, D8, C8));
      expected.add(new MoveSpecification(BLACK, D8, E8));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move - move king back
      final MoveSpecification move = new MoveSpecification(BLACK, D8, E8);
      board.performMove(move);
    }

    {
      // white king possible moves - original position but castling right lost
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E1, D1));
      expected.add(new MoveSpecification(WHITE, E1, D2));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move - develop knight
      final MoveSpecification move = new MoveSpecification(WHITE, G1, F3);
      board.performMove(move);
    }

    {
      // black king possible moves - original position but castling right lost
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E8, D7));
      expected.add(new MoveSpecification(BLACK, E8, D8));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move - develop knight
      final MoveSpecification move = new MoveSpecification(BLACK, G8, F6);
      board.performMove(move);
    }
  }

  static void testKingKingQueenSideCastlingRightChangeForKingMove() {
    final ApiBoard board = new Board();
    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, E2, E4);
      board.performMove(move);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, E7, E5);
      board.performMove(move);
    }

    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, G1, F3);
      board.performMove(move);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, G8, F6);
      board.performMove(move);
    }

    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, F1, C4);
      board.performMove(move);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, F8, C5);
      board.performMove(move);
    }

    {
      // white king possible moves - king side castling
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E1, E2));
      expected.add(new MoveSpecification(WHITE, E1, F1));
      expected.add(new MoveSpecification(WHITE, CastlingMove.KING_SIDE));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move - prepare queen side castling
      final MoveSpecification move = new MoveSpecification(WHITE, D2, D4);
      board.performMove(move);
    }

    {
      // black king possible moves - king side castling
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E8, E7));
      expected.add(new MoveSpecification(BLACK, E8, F8));
      expected.add(new MoveSpecification(BLACK, CastlingMove.KING_SIDE));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move - prepare queen side castling
      final MoveSpecification move = new MoveSpecification(BLACK, D7, D5);
      board.performMove(move);
    }

    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, B1, C3);
      board.performMove(move);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, B8, C6);
      board.performMove(move);
    }

    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, C1, E3);
      board.performMove(move);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, C8, E6);
      board.performMove(move);
    }

    {
      // white move
      final MoveSpecification move = new MoveSpecification(WHITE, D1, E2);
      board.performMove(move);
    }
    {
      // black move
      final MoveSpecification move = new MoveSpecification(BLACK, D8, E7);
      board.performMove(move);
    }

    {
      // white king possible moves - queen and king side castling
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E1, F1));
      expected.add(new MoveSpecification(WHITE, E1, D1));
      expected.add(new MoveSpecification(WHITE, E1, D2));
      expected.add(new MoveSpecification(WHITE, CastlingMove.KING_SIDE));
      expected.add(new MoveSpecification(WHITE, CastlingMove.QUEEN_SIDE));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move - moves king away
      final MoveSpecification move = new MoveSpecification(WHITE, E1, D2);
      board.performMove(move);
    }

    {
      // black king possible moves - queen and king side castling
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E8, D7));
      expected.add(new MoveSpecification(BLACK, E8, D8));
      expected.add(new MoveSpecification(BLACK, E8, F8));
      expected.add(new MoveSpecification(BLACK, CastlingMove.KING_SIDE));
      expected.add(new MoveSpecification(BLACK, CastlingMove.QUEEN_SIDE));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move - moves king away
      final MoveSpecification move = new MoveSpecification(BLACK, E8, D7);
      board.performMove(move);
    }

    {
      // white king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), D2);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, D2, D3));
      expected.add(new MoveSpecification(WHITE, D2, E1));
      expected.add(new MoveSpecification(WHITE, D2, D1));
      expected.add(new MoveSpecification(WHITE, D2, C1));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move - moves king back
      final MoveSpecification move = new MoveSpecification(WHITE, D2, E1);
      board.performMove(move);
    }

    {
      // black king possible moves
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), D7);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, D7, D6));
      expected.add(new MoveSpecification(BLACK, D7, C8));
      expected.add(new MoveSpecification(BLACK, D7, D8));
      expected.add(new MoveSpecification(BLACK, D7, E8));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move - moves king back
      final MoveSpecification move = new MoveSpecification(BLACK, D7, E8);
      board.performMove(move);
    }

    {
      // white king possible moves - both castling rights lost
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(WHITE), board.getHavingMove(), E1);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(WHITE, E1, F1));
      expected.add(new MoveSpecification(WHITE, E1, D1));
      expected.add(new MoveSpecification(WHITE, E1, D2));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // white move - moves knight
      final MoveSpecification move = new MoveSpecification(WHITE, F3, G5);
      board.performMove(move);
    }

    {
      // black king possible moves - both castling rights lost
      final Set<LegalMove> calculatedLegalMoveSet = KingLegalMoves.calculateKingLegalMoves(board.getStaticPosition(),
          board.getCastlingRight(BLACK), board.getHavingMove(), E8);

      final Set<MoveSpecification> expected = new TreeSet<>();
      expected.add(new MoveSpecification(BLACK, E8, D7));
      expected.add(new MoveSpecification(BLACK, E8, D8));
      expected.add(new MoveSpecification(BLACK, E8, F8));

      check(expected, calculatedLegalMoveSet);
    }
    {
      // black move - moves knight
      final MoveSpecification move = new MoveSpecification(BLACK, F6, G4);
      board.performMove(move);
    }
  }

  private static void check(Set<MoveSpecification> expected, Set<LegalMove> calculatedLegalMoveSet) {
    final Set<MoveSpecification> actual = ValidateNewMove.calculateMoveSpecifications(calculatedLegalMoveSet);
    assertEquals(expected, actual);
  }

  private static void checkExceptionRook(StaticPosition staticPosition, Side havingMove, Square fromSquare) {
    var isCorrectException = false;
    try {
      RookLegalMoves.calculateRookLegalMoves(staticPosition, havingMove, fromSquare);
    } catch (@SuppressWarnings("unused") final IllegalArgumentException iae) {
      isCorrectException = true;
    }
    assertTrue(isCorrectException);
  }

  private static void checkExceptionKnight(StaticPosition staticPosition, Side havingMove, Square fromSquare) {
    var isCorrectException = false;
    try {
      KnightLegalMoves.calculateKnightLegalMoves(staticPosition, havingMove, fromSquare);
    } catch (@SuppressWarnings("unused") final IllegalArgumentException iae) {
      isCorrectException = true;
    }
    assertTrue(isCorrectException);
  }

  private static void checkExceptionBishop(StaticPosition staticPosition, Side havingMove, Square fromSquare) {
    var isCorrectException = false;
    try {
      BishopLegalMoves.calculateBishopLegalMoves(staticPosition, havingMove, fromSquare);
    } catch (@SuppressWarnings("unused") final IllegalArgumentException iae) {
      isCorrectException = true;
    }
    assertTrue(isCorrectException);
  }

  private static void checkExceptionQueen(StaticPosition staticPosition, Side havingMove, Square fromSquare) {
    var isCorrectException = false;
    try {
      QueenLegalMoves.calculateQueenLegalMoves(staticPosition, havingMove, fromSquare);
    } catch (@SuppressWarnings("unused") final IllegalArgumentException iae) {
      isCorrectException = true;
    }
    assertTrue(isCorrectException);
  }

  private static void checkExceptionKing(StaticPosition staticPosition, CastlingRight castlingRight, Side havingMove,
      Square fromSquare) {
    var isCorrectException = false;
    try {
      KingLegalMoves.calculateKingLegalMoves(staticPosition, castlingRight, havingMove, fromSquare);
    } catch (@SuppressWarnings("unused") final IllegalArgumentException iae) {
      isCorrectException = true;
    }
    assertTrue(isCorrectException);
  }

  private static void checkExceptionPawn(StaticPosition staticPosition, Square enPassantCaptureTargetSquare,
      Side havingMove, Square fromSquare) {
    var isCorrectException = false;
    try {
      PawnLegalMoves.calculatePawnLegalMoves(staticPosition, enPassantCaptureTargetSquare, havingMove, fromSquare);
    } catch (@SuppressWarnings("unused") final IllegalArgumentException iae) {
      isCorrectException = true;
    }
    assertTrue(isCorrectException);
  }

}