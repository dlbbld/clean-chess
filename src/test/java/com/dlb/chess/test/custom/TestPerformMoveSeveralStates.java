package com.dlb.chess.test.custom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.enums.MoveCheck;
import com.dlb.chess.moves.utility.CastlingUtility;
import com.dlb.chess.moves.utility.EnPassantCaptureUtility;
import com.dlb.chess.moves.utility.PromotionUtility;

class TestPerformMoveSeveralStates implements EnumConstants {

  @SuppressWarnings("static-method")
  @Test
  void testTrivial() {
    final ApiBoard board = new Board();

    // white move 1
    // test before move
    {
      final Side havingMove = WHITE;
      final Square fromSquare = E2;
      final Square toSquare = E4;
      final Piece movingPiece = WHITE_PAWN;

      assertTrue(board.isFirstMove());
      assertEquals(havingMove, board.getHavingMove());

      assertEquals(movingPiece, board.getStaticPosition().get(fromSquare));
      assertEquals(Piece.NONE, board.getStaticPosition().get(toSquare));

      final MoveSpecification halfMoveWhite = new MoveSpecification(havingMove, fromSquare, toSquare);

      assertEquals(MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY, CastlingUtility
          .calculateQueenSideCastlingCheck(board.getStaticPosition(), havingMove, board.getCastlingRight(WHITE)));
      assertEquals(MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY, CastlingUtility
          .calculateKingSideCastlingCheck(board.getStaticPosition(), havingMove, board.getCastlingRight(WHITE)));
      assertEquals(0, board.getHalfMoveClock());

      board.performMove(halfMoveWhite);

      // test after move
      assertEquals("e4", board.getSan());
      assertEquals("e2e4", board.getLan());
      assertEquals(0, board.getHalfMoveClock());
      assertFalse(calculateIsEnPassantCaptureLastMove(board));
      assertFalse(CastlingUtility.calculateIsCastlingMove(board.getLastMove().moveSpecification()));
      assertFalse(PromotionUtility.calculateIsPromotion(board.getLastMove().moveSpecification()));

      assertEquals(Piece.NONE, board.getStaticPosition().get(fromSquare));
      assertEquals(movingPiece, board.getStaticPosition().get(toSquare));

      assertEquals(Piece.NONE, board.getLastMove().pieceCaptured());

      assertEquals(movingPiece, board.getLastMove().movingPiece());

      assertEquals(BLACK, board.getHavingMove());

    }

    // black move 1
    // test before move
    {
      final Side havingMove = BLACK;
      final Square fromSquare = D7;
      final Square toSquare = D5;
      final Piece movingPiece = BLACK_PAWN;

      assertFalse(board.isFirstMove());
      assertEquals(havingMove, board.getHavingMove());

      assertEquals(movingPiece, board.getStaticPosition().get(fromSquare));
      assertEquals(Piece.NONE, board.getStaticPosition().get(toSquare));

      final MoveSpecification halfMoveBlack = new MoveSpecification(havingMove, fromSquare, toSquare);

      assertEquals(MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY, CastlingUtility
          .calculateQueenSideCastlingCheck(board.getStaticPosition(), havingMove, board.getCastlingRight(BLACK)));
      assertEquals(MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY, CastlingUtility
          .calculateKingSideCastlingCheck(board.getStaticPosition(), havingMove, board.getCastlingRight(BLACK)));

      board.performMove(halfMoveBlack);

      // test after move
      assertEquals("d5", board.getSan());
      assertEquals("d7d5", board.getLan());
      assertEquals(0, board.getHalfMoveClock());
      assertFalse(calculateIsEnPassantCaptureLastMove(board));
      assertFalse(CastlingUtility.calculateIsCastlingMove(board.getLastMove().moveSpecification()));
      assertFalse(PromotionUtility.calculateIsPromotionLastMove(board));

      assertEquals(Piece.NONE, board.getStaticPosition().get(fromSquare));
      assertEquals(movingPiece, board.getStaticPosition().get(toSquare));

      assertEquals(movingPiece, board.getLastMove().movingPiece());

      assertEquals(WHITE, board.getHavingMove());

    }

    // white move 2
    // test before move
    {
      final Side havingMove = WHITE;
      final Square fromSquare = E4;
      final Square toSquare = D5;
      final Piece movingPiece = WHITE_PAWN;

      assertFalse(board.isFirstMove());
      assertEquals(havingMove, board.getHavingMove());

      assertEquals(movingPiece, board.getStaticPosition().get(fromSquare));
      assertEquals(BLACK_PAWN, board.getStaticPosition().get(toSquare));

      final MoveSpecification halfMoveWhite = new MoveSpecification(havingMove, fromSquare, toSquare);

      assertEquals(MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY, CastlingUtility
          .calculateQueenSideCastlingCheck(board.getStaticPosition(), havingMove, board.getCastlingRight(WHITE)));
      assertEquals(MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY, CastlingUtility
          .calculateKingSideCastlingCheck(board.getStaticPosition(), havingMove, board.getCastlingRight(WHITE)));

      board.performMove(halfMoveWhite);

      // test after move
      assertEquals("exd5", board.getSan());
      assertEquals("e4xd5", board.getLan());
      assertEquals(0, board.getHalfMoveClock());
      assertFalse(calculateIsEnPassantCaptureLastMove(board));
      assertFalse(CastlingUtility.calculateIsCastlingMove(board.getLastMove().moveSpecification()));
      assertFalse(PromotionUtility.calculateIsPromotionLastMove(board));

      assertEquals(Piece.NONE, board.getStaticPosition().get(fromSquare));
      assertEquals(movingPiece, board.getStaticPosition().get(toSquare));

      assertEquals(movingPiece, board.getLastMove().movingPiece());

      assertEquals(BLACK, board.getHavingMove());

    }

    // black move 2
    // test before move
    {
      final Side havingMove = BLACK;
      final Square fromSquare = D8;
      final Square toSquare = D5;
      final Piece movingPiece = BLACK_QUEEN;

      assertFalse(board.isFirstMove());
      assertEquals(havingMove, board.getHavingMove());

      assertEquals(movingPiece, board.getStaticPosition().get(fromSquare));
      assertEquals(WHITE_PAWN, board.getStaticPosition().get(toSquare));

      final MoveSpecification halfMoveBlack = new MoveSpecification(havingMove, fromSquare, toSquare);

      assertEquals(MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY, CastlingUtility
          .calculateQueenSideCastlingCheck(board.getStaticPosition(), havingMove, board.getCastlingRight(BLACK)));
      assertEquals(MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY, CastlingUtility
          .calculateKingSideCastlingCheck(board.getStaticPosition(), havingMove, board.getCastlingRight(BLACK)));

      board.performMove(halfMoveBlack);

      // test after move
      assertEquals("Qxd5", board.getSan());
      assertEquals("Qd8xd5", board.getLan());
      assertEquals(0, board.getHalfMoveClock());
      assertFalse(calculateIsEnPassantCaptureLastMove(board));
      assertFalse(CastlingUtility.calculateIsCastlingMove(board.getLastMove().moveSpecification()));
      assertFalse(PromotionUtility.calculateIsPromotionLastMove(board));

      assertEquals(Piece.NONE, board.getStaticPosition().get(fromSquare));
      assertEquals(movingPiece, board.getStaticPosition().get(toSquare));

      assertEquals(movingPiece, board.getLastMove().movingPiece());

      assertEquals(WHITE, board.getHavingMove());

    }

    // white move 3
    // test before move
    {
      final Side havingMove = WHITE;
      final Square fromSquare = G2;
      final Square toSquare = G4;
      final Piece movingPiece = WHITE_PAWN;

      assertFalse(board.isFirstMove());
      assertEquals(havingMove, board.getHavingMove());

      assertEquals(movingPiece, board.getStaticPosition().get(fromSquare));
      assertEquals(Piece.NONE, board.getStaticPosition().get(toSquare));

      final MoveSpecification halfMoveWhite = new MoveSpecification(havingMove, fromSquare, toSquare);

      assertEquals(MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY, CastlingUtility
          .calculateQueenSideCastlingCheck(board.getStaticPosition(), havingMove, board.getCastlingRight(WHITE)));
      assertEquals(MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY, CastlingUtility
          .calculateKingSideCastlingCheck(board.getStaticPosition(), havingMove, board.getCastlingRight(WHITE)));

      board.performMove(halfMoveWhite);

      // test after move
      assertEquals("g4", board.getSan());
      assertEquals("g2g4", board.getLan());
      assertEquals(0, board.getHalfMoveClock());
      assertFalse(calculateIsEnPassantCaptureLastMove(board));
      assertFalse(CastlingUtility.calculateIsCastlingMove(board.getLastMove().moveSpecification()));
      assertFalse(PromotionUtility.calculateIsPromotionLastMove(board));

      assertEquals(Piece.NONE, board.getStaticPosition().get(fromSquare));
      assertEquals(movingPiece, board.getStaticPosition().get(toSquare));

      assertEquals(movingPiece, board.getLastMove().movingPiece());

      assertEquals(BLACK, board.getHavingMove());

    }

    // black move 3
    // test before move
    {
      final Side havingMove = BLACK;
      final Square fromSquare = C8;
      final Square toSquare = D7;
      final Piece movingPiece = BLACK_BISHOP;

      assertFalse(board.isFirstMove());
      assertEquals(havingMove, board.getHavingMove());

      assertEquals(movingPiece, board.getStaticPosition().get(fromSquare));
      assertEquals(Piece.NONE, board.getStaticPosition().get(toSquare));

      final MoveSpecification halfMoveBlack = new MoveSpecification(havingMove, fromSquare, toSquare);

      assertEquals(MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY, CastlingUtility
          .calculateQueenSideCastlingCheck(board.getStaticPosition(), havingMove, board.getCastlingRight(BLACK)));
      assertEquals(MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY, CastlingUtility
          .calculateKingSideCastlingCheck(board.getStaticPosition(), havingMove, board.getCastlingRight(BLACK)));

      board.performMove(halfMoveBlack);

      // test after move
      assertEquals("Bd7", board.getSan());
      assertEquals("Bc8d7", board.getLan());
      assertEquals(1, board.getHalfMoveClock());
      assertFalse(calculateIsEnPassantCaptureLastMove(board));
      assertFalse(CastlingUtility.calculateIsCastlingMove(board.getLastMove().moveSpecification()));
      assertFalse(PromotionUtility.calculateIsPromotionLastMove(board));

      assertEquals(Piece.NONE, board.getStaticPosition().get(fromSquare));
      assertEquals(movingPiece, board.getStaticPosition().get(toSquare));

      assertEquals(movingPiece, board.getLastMove().movingPiece());

      assertEquals(WHITE, board.getHavingMove());

    }

    // white move 4
    // test before move
    {
      final Side havingMove = WHITE;
      final Square fromSquare = G4;
      final Square toSquare = G5;
      final Piece movingPiece = WHITE_PAWN;

      assertFalse(board.isFirstMove());
      assertEquals(havingMove, board.getHavingMove());

      assertEquals(movingPiece, board.getStaticPosition().get(fromSquare));
      assertEquals(Piece.NONE, board.getStaticPosition().get(toSquare));

      final MoveSpecification halfMoveWhite = new MoveSpecification(havingMove, fromSquare, toSquare);

      assertEquals(MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY, CastlingUtility
          .calculateQueenSideCastlingCheck(board.getStaticPosition(), havingMove, board.getCastlingRight(WHITE)));
      assertEquals(MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY, CastlingUtility
          .calculateKingSideCastlingCheck(board.getStaticPosition(), havingMove, board.getCastlingRight(WHITE)));

      board.performMove(halfMoveWhite);

      // test after move
      assertEquals("g5", board.getSan());
      assertEquals("g4g5", board.getLan());
      assertEquals(0, board.getHalfMoveClock());
      assertFalse(calculateIsEnPassantCaptureLastMove(board));
      assertFalse(CastlingUtility.calculateIsCastlingMove(board.getLastMove().moveSpecification()));
      assertFalse(PromotionUtility.calculateIsPromotionLastMove(board));

      assertEquals(Piece.NONE, board.getStaticPosition().get(fromSquare));
      assertEquals(movingPiece, board.getStaticPosition().get(toSquare));

      assertEquals(movingPiece, board.getLastMove().movingPiece());

      assertEquals(BLACK, board.getHavingMove());

    }

    // black move 4
    // test before move
    {
      final Side havingMove = BLACK;
      final Square fromSquare = F7;
      final Square toSquare = F5;
      final Piece movingPiece = BLACK_PAWN;

      assertFalse(board.isFirstMove());
      assertEquals(havingMove, board.getHavingMove());

      assertEquals(movingPiece, board.getStaticPosition().get(fromSquare));
      assertEquals(Piece.NONE, board.getStaticPosition().get(toSquare));

      final MoveSpecification halfMoveBlack = new MoveSpecification(havingMove, fromSquare, toSquare);

      assertEquals(MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY, CastlingUtility
          .calculateQueenSideCastlingCheck(board.getStaticPosition(), havingMove, board.getCastlingRight(BLACK)));
      assertEquals(MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY, CastlingUtility
          .calculateKingSideCastlingCheck(board.getStaticPosition(), havingMove, board.getCastlingRight(BLACK)));

      board.performMove(halfMoveBlack);

      // test after move
      assertEquals("f5", board.getSan());
      assertEquals("f7f5", board.getLan());
      assertEquals(0, board.getHalfMoveClock());
      assertFalse(calculateIsEnPassantCaptureLastMove(board));
      assertFalse(CastlingUtility.calculateIsCastlingMove(board.getLastMove().moveSpecification()));
      assertFalse(PromotionUtility.calculateIsPromotionLastMove(board));

      assertEquals(Piece.NONE, board.getStaticPosition().get(fromSquare));
      assertEquals(movingPiece, board.getStaticPosition().get(toSquare));

      assertEquals(movingPiece, board.getLastMove().movingPiece());

      assertEquals(WHITE, board.getHavingMove());

    }

    // white move 5
    // test before move
    {
      final Side havingMove = WHITE;
      final Square fromSquare = G5;
      final Square toSquare = F6;
      final Piece movingPiece = WHITE_PAWN;

      assertFalse(board.isFirstMove());
      assertEquals(havingMove, board.getHavingMove());

      assertEquals(movingPiece, board.getStaticPosition().get(fromSquare));
      assertEquals(Piece.NONE, board.getStaticPosition().get(toSquare));

      final MoveSpecification halfMoveWhite = new MoveSpecification(havingMove, fromSquare, toSquare);

      assertEquals(MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY, CastlingUtility
          .calculateQueenSideCastlingCheck(board.getStaticPosition(), havingMove, board.getCastlingRight(WHITE)));
      assertEquals(MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY, CastlingUtility
          .calculateKingSideCastlingCheck(board.getStaticPosition(), havingMove, board.getCastlingRight(WHITE)));

      board.performMove(halfMoveWhite);

      // test after move
      assertEquals("gxf6", board.getSan());
      assertEquals("g5xf6", board.getLan());
      assertEquals(0, board.getHalfMoveClock());
      assertTrue(calculateIsEnPassantCaptureLastMove(board));
      assertFalse(CastlingUtility.calculateIsCastlingMove(board.getLastMove().moveSpecification()));
      assertFalse(PromotionUtility.calculateIsPromotionLastMove(board));

      assertEquals(Piece.NONE, board.getStaticPosition().get(fromSquare));
      assertEquals(movingPiece, board.getStaticPosition().get(toSquare));

      assertEquals(movingPiece, board.getLastMove().movingPiece());

      assertEquals(BLACK, board.getHavingMove());

    }

    // black move 5
    // test before move
    {
      final Side havingMove = BLACK;
      final Square fromSquare = B8;
      final Square toSquare = C6;
      final Piece movingPiece = BLACK_KNIGHT;

      assertFalse(board.isFirstMove());
      assertEquals(havingMove, board.getHavingMove());

      assertEquals(movingPiece, board.getStaticPosition().get(fromSquare));
      assertEquals(Piece.NONE, board.getStaticPosition().get(toSquare));

      final MoveSpecification halfMoveBlack = new MoveSpecification(havingMove, fromSquare, toSquare);

      assertEquals(MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY, CastlingUtility
          .calculateQueenSideCastlingCheck(board.getStaticPosition(), havingMove, board.getCastlingRight(BLACK)));
      assertEquals(MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY, CastlingUtility
          .calculateKingSideCastlingCheck(board.getStaticPosition(), havingMove, board.getCastlingRight(BLACK)));

      board.performMove(halfMoveBlack);

      // test after move
      assertEquals("Nc6", board.getSan());
      assertEquals("Nb8c6", board.getLan());
      assertEquals(1, board.getHalfMoveClock());
      assertFalse(calculateIsEnPassantCaptureLastMove(board));
      assertFalse(CastlingUtility.calculateIsCastlingMove(board.getLastMove().moveSpecification()));
      assertFalse(PromotionUtility.calculateIsPromotionLastMove(board));

      assertEquals(Piece.NONE, board.getStaticPosition().get(fromSquare));
      assertEquals(movingPiece, board.getStaticPosition().get(toSquare));

      assertEquals(movingPiece, board.getLastMove().movingPiece());

      assertEquals(WHITE, board.getHavingMove());

    }

    // white move 6
    // test before move
    {
      final Side havingMove = WHITE;
      final Square fromSquare = G1;
      final Square toSquare = F3;
      final Piece movingPiece = WHITE_KNIGHT;

      assertFalse(board.isFirstMove());
      assertEquals(havingMove, board.getHavingMove());

      assertEquals(movingPiece, board.getStaticPosition().get(fromSquare));
      assertEquals(Piece.NONE, board.getStaticPosition().get(toSquare));

      final MoveSpecification halfMoveWhite = new MoveSpecification(havingMove, fromSquare, toSquare);

      assertEquals(MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY, CastlingUtility
          .calculateQueenSideCastlingCheck(board.getStaticPosition(), havingMove, board.getCastlingRight(WHITE)));
      assertEquals(MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY, CastlingUtility
          .calculateKingSideCastlingCheck(board.getStaticPosition(), havingMove, board.getCastlingRight(WHITE)));

      board.performMove(halfMoveWhite);

      // test after move
      assertEquals("Nf3", board.getSan());
      assertEquals("Ng1f3", board.getLan());
      assertEquals(2, board.getHalfMoveClock());
      assertFalse(calculateIsEnPassantCaptureLastMove(board));
      assertFalse(CastlingUtility.calculateIsCastlingMove(board.getLastMove().moveSpecification()));
      assertFalse(PromotionUtility.calculateIsPromotionLastMove(board));

      assertEquals(Piece.NONE, board.getStaticPosition().get(fromSquare));
      assertEquals(movingPiece, board.getStaticPosition().get(toSquare));

      assertEquals(movingPiece, board.getLastMove().movingPiece());

      assertEquals(BLACK, board.getHavingMove());

    }

    // black move 6
    // test before move
    {
      final Side havingMove = BLACK;
      final Square fromSquare = A7;
      final Square toSquare = A5;
      final Piece movingPiece = BLACK_PAWN;

      assertFalse(board.isFirstMove());
      assertEquals(havingMove, board.getHavingMove());

      assertEquals(movingPiece, board.getStaticPosition().get(fromSquare));
      assertEquals(Piece.NONE, board.getStaticPosition().get(toSquare));

      final MoveSpecification halfMoveBlack = new MoveSpecification(havingMove, fromSquare, toSquare);

      assertEquals(MoveCheck.SUCCESS, CastlingUtility.calculateQueenSideCastlingCheck(board.getStaticPosition(),
          havingMove, board.getCastlingRight(BLACK)));
      assertEquals(MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY, CastlingUtility
          .calculateKingSideCastlingCheck(board.getStaticPosition(), havingMove, board.getCastlingRight(BLACK)));

      board.performMove(halfMoveBlack);

      // test after move
      assertEquals("a5", board.getSan());
      assertEquals("a7a5", board.getLan());
      assertEquals(0, board.getHalfMoveClock());
      assertFalse(calculateIsEnPassantCaptureLastMove(board));
      assertFalse(CastlingUtility.calculateIsCastlingMove(board.getLastMove().moveSpecification()));
      assertFalse(PromotionUtility.calculateIsPromotionLastMove(board));

      assertEquals(Piece.NONE, board.getStaticPosition().get(fromSquare));
      assertEquals(movingPiece, board.getStaticPosition().get(toSquare));

      assertEquals(movingPiece, board.getLastMove().movingPiece());

      assertEquals(WHITE, board.getHavingMove());

    }

    // white move 7
    // test before move
    {
      final Side havingMove = WHITE;
      final Square fromSquare = F1;
      final Square toSquare = C4;
      final Piece movingPiece = WHITE_BISHOP;

      assertFalse(board.isFirstMove());
      assertEquals(havingMove, board.getHavingMove());

      assertEquals(movingPiece, board.getStaticPosition().get(fromSquare));
      assertEquals(Piece.NONE, board.getStaticPosition().get(toSquare));

      final MoveSpecification halfMoveWhite = new MoveSpecification(havingMove, fromSquare, toSquare);

      assertEquals(MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY, CastlingUtility
          .calculateQueenSideCastlingCheck(board.getStaticPosition(), havingMove, board.getCastlingRight(WHITE)));
      assertEquals(MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY, CastlingUtility
          .calculateKingSideCastlingCheck(board.getStaticPosition(), havingMove, board.getCastlingRight(WHITE)));

      board.performMove(halfMoveWhite);

      // test after move
      assertEquals("Bc4", board.getSan());
      assertEquals("Bf1c4", board.getLan());
      assertEquals(1, board.getHalfMoveClock());
      assertFalse(calculateIsEnPassantCaptureLastMove(board));
      assertFalse(CastlingUtility.calculateIsCastlingMove(board.getLastMove().moveSpecification()));
      assertFalse(PromotionUtility.calculateIsPromotionLastMove(board));

      assertEquals(Piece.NONE, board.getStaticPosition().get(fromSquare));
      assertEquals(movingPiece, board.getStaticPosition().get(toSquare));

      assertEquals(movingPiece, board.getLastMove().movingPiece());

      assertEquals(BLACK, board.getHavingMove());

    }

    // black move 7
    // test before move
    {
      final Side havingMove = BLACK;
      final Square fromSquare = A5;
      final Square toSquare = A4;
      final Piece movingPiece = BLACK_PAWN;

      assertFalse(board.isFirstMove());
      assertEquals(havingMove, board.getHavingMove());

      assertEquals(movingPiece, board.getStaticPosition().get(fromSquare));
      assertEquals(Piece.NONE, board.getStaticPosition().get(toSquare));

      final MoveSpecification halfMoveBlack = new MoveSpecification(havingMove, fromSquare, toSquare);

      assertEquals(MoveCheck.SUCCESS, CastlingUtility.calculateQueenSideCastlingCheck(board.getStaticPosition(),
          havingMove, board.getCastlingRight(BLACK)));
      assertEquals(MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY, CastlingUtility
          .calculateKingSideCastlingCheck(board.getStaticPosition(), havingMove, board.getCastlingRight(BLACK)));

      board.performMove(halfMoveBlack);

      // test after move
      assertEquals("a4", board.getSan());
      assertEquals("a5a4", board.getLan());
      assertEquals(0, board.getHalfMoveClock());
      assertFalse(calculateIsEnPassantCaptureLastMove(board));
      assertFalse(CastlingUtility.calculateIsCastlingMove(board.getLastMove().moveSpecification()));
      assertFalse(PromotionUtility.calculateIsPromotionLastMove(board));

      assertEquals(Piece.NONE, board.getStaticPosition().get(fromSquare));
      assertEquals(movingPiece, board.getStaticPosition().get(toSquare));

      assertEquals(movingPiece, board.getLastMove().movingPiece());

      assertEquals(WHITE, board.getHavingMove());

    }

    // white move 8
    // test before move
    {
      final Side havingMove = WHITE;
      final Square fromSquare = B1;
      final Square toSquare = C3;
      final Piece movingPiece = WHITE_KNIGHT;

      assertFalse(board.isFirstMove());
      assertEquals(havingMove, board.getHavingMove());

      assertEquals(movingPiece, board.getStaticPosition().get(fromSquare));
      assertEquals(Piece.NONE, board.getStaticPosition().get(toSquare));

      final MoveSpecification halfMoveWhite = new MoveSpecification(havingMove, fromSquare, toSquare);

      assertEquals(MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY, CastlingUtility
          .calculateQueenSideCastlingCheck(board.getStaticPosition(), havingMove, board.getCastlingRight(WHITE)));
      assertEquals(MoveCheck.SUCCESS, CastlingUtility.calculateKingSideCastlingCheck(board.getStaticPosition(),
          havingMove, board.getCastlingRight(WHITE)));

      board.performMove(halfMoveWhite);

      // test after move
      assertEquals("Nc3", board.getSan());
      assertEquals("Nb1c3", board.getLan());
      assertEquals(1, board.getHalfMoveClock());
      assertFalse(calculateIsEnPassantCaptureLastMove(board));
      assertFalse(CastlingUtility.calculateIsCastlingMove(board.getLastMove().moveSpecification()));
      assertFalse(PromotionUtility.calculateIsPromotionLastMove(board));

      assertEquals(Piece.NONE, board.getStaticPosition().get(fromSquare));
      assertEquals(movingPiece, board.getStaticPosition().get(toSquare));

      assertEquals(movingPiece, board.getLastMove().movingPiece());

      assertEquals(BLACK, board.getHavingMove());

    }
  }

  private static boolean calculateIsEnPassantCaptureLastMove(ApiBoard board) {
    return EnPassantCaptureUtility.calculateIsEnPassantCapture(board.getStaticPositionBeforeLastMove(),
        board.getLastMove().moveSpecification());
  }
}