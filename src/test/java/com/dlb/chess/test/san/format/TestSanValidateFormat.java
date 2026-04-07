package com.dlb.chess.test.san.format;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.constants.CastlingConstants;
import com.dlb.chess.model.SanConversion;
import com.dlb.chess.san.SanValidateFormat;
import com.dlb.chess.san.enums.CheckmateOrCheck;
import com.dlb.chess.san.enums.SanType;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.san.model.SanParse;
import com.dlb.chess.san.reference.SanValidateFormatReference;

class TestSanValidateFormat {

  @SuppressWarnings("static-method")
  @Test
  void testInvalid() {
    // d3 — replace first char with valid non-file, second char with valid non-digit
    checkException("=3", SanValidationProblem.FORMAT_FIRST_CHARACTER);
    checkException("dd", SanValidationProblem.FORMAT_PAWN_SECOND_CHARACTER);

    // dxe5 — invalid first, second, third, fourth chars
    checkException("+xe5", SanValidationProblem.FORMAT_FIRST_CHARACTER);
    checkException("d=e5", SanValidationProblem.FORMAT_PAWN_SECOND_CHARACTER);
    checkException("dx15", SanValidationProblem.FORMAT_PAWN_CAPTURE_TO_FILE);
    checkException("dxeR", SanValidationProblem.FORMAT_PAWN_CAPTURE_TO_RANK);

    // d8=Q — invalid first, second (rank), structure, promotion piece
    checkException("18=Q", SanValidationProblem.FORMAT_FIRST_CHARACTER);
    checkException("dd=Q", SanValidationProblem.FORMAT_PAWN_PROMOTION_RANK);
    checkException("d8xQ", SanValidationProblem.FORMAT_PAWN_SECOND_CHARACTER);
    checkException("d8=K", SanValidationProblem.FORMAT);

    // dxe8=Q — invalid chars replaced with valid SAN chars
    checkException("1xe8=Q", SanValidationProblem.FORMAT_FIRST_CHARACTER);
    checkException("d=e8=Q", SanValidationProblem.FORMAT);
    checkException("dxa=Q", SanValidationProblem.FORMAT_PAWN_LENGTH);
    checkException("dxeR=Q", SanValidationProblem.FORMAT);
    checkException("dxe8xQ", SanValidationProblem.FORMAT);
    checkException("dxe8=K", SanValidationProblem.FORMAT);

    // Qe5
    checkException("+e5", SanValidationProblem.FORMAT_FIRST_CHARACTER);
    checkException("QK5", SanValidationProblem.FORMAT_PIECE_DESTINATION);
    checkException("Qe=", SanValidationProblem.FORMAT);

    // Qae5
    checkException("+ae5", SanValidationProblem.FORMAT_FIRST_CHARACTER);
    checkException("QKe5", SanValidationProblem.FORMAT_PIECE_MIDDLE);
    checkException("QaK5", SanValidationProblem.FORMAT_PIECE_DESTINATION);
    checkException("QaeR", SanValidationProblem.FORMAT);

    // Q2e5
    checkException("+2e5", SanValidationProblem.FORMAT_FIRST_CHARACTER);
    checkException("QRe5", SanValidationProblem.FORMAT);
    checkException("Q2K5", SanValidationProblem.FORMAT_PIECE_DESTINATION);
    checkException("Q2eR", SanValidationProblem.FORMAT);

    // Qc3e5
    checkException("+c3e5", SanValidationProblem.FORMAT_FIRST_CHARACTER);
    checkException("QK3e5", SanValidationProblem.FORMAT_PIECE_MIDDLE);
    checkException("QcRe5", SanValidationProblem.FORMAT);
    checkException("Qc3K5", SanValidationProblem.FORMAT_PIECE_DESTINATION);
    checkException("Qc3eR", SanValidationProblem.FORMAT);

    // Qxe5
    checkException("+xe5", SanValidationProblem.FORMAT_FIRST_CHARACTER);
    checkException("Q=e5", SanValidationProblem.FORMAT_PIECE_MIDDLE);
    checkException("QxK5", SanValidationProblem.FORMAT_PIECE_DESTINATION);
    checkException("QxeR", SanValidationProblem.FORMAT);

    // Qaxe5
    checkException("+axe5", SanValidationProblem.FORMAT_FIRST_CHARACTER);
    checkException("QKxe5", SanValidationProblem.FORMAT_PIECE_MIDDLE);
    checkException("Qa=e5", SanValidationProblem.FORMAT_PIECE_MIDDLE);
    checkException("QaxK5", SanValidationProblem.FORMAT_PIECE_DESTINATION);
    checkException("QaxeR", SanValidationProblem.FORMAT);

    // Q2xe5
    checkException("+2xe5", SanValidationProblem.FORMAT_FIRST_CHARACTER);
    checkException("QRxe5", SanValidationProblem.FORMAT);
    checkException("Q2=e5", SanValidationProblem.FORMAT_PIECE_MIDDLE);
    checkException("Q2xK5", SanValidationProblem.FORMAT_PIECE_DESTINATION);
    checkException("Q2xeR", SanValidationProblem.FORMAT);

    // Qc3xe5
    checkException("+c3xe5", SanValidationProblem.FORMAT_FIRST_CHARACTER);
    checkException("QK3xe5", SanValidationProblem.FORMAT_PIECE_MIDDLE);
    checkException("QcRxe5", SanValidationProblem.FORMAT);
    checkException("Qc3=e5", SanValidationProblem.FORMAT_PIECE_MIDDLE);
    checkException("Qc3xK5", SanValidationProblem.FORMAT_PIECE_DESTINATION);
    checkException("Qc3xeR", SanValidationProblem.FORMAT);

    // Re5
    checkException("+e5", SanValidationProblem.FORMAT_FIRST_CHARACTER);
    checkException("RK5", SanValidationProblem.FORMAT_PIECE_DESTINATION);
    checkException("ReR", SanValidationProblem.FORMAT);

    // Rae5
    checkException("+ae5", SanValidationProblem.FORMAT_FIRST_CHARACTER);
    checkException("RKe5", SanValidationProblem.FORMAT_PIECE_MIDDLE);
    checkException("RaK5", SanValidationProblem.FORMAT_PIECE_DESTINATION);
    checkException("RaeR", SanValidationProblem.FORMAT);

    // R2e5
    checkException("+2e5", SanValidationProblem.FORMAT_FIRST_CHARACTER);
    checkException("RRe5", SanValidationProblem.FORMAT);
    checkException("R2K5", SanValidationProblem.FORMAT_PIECE_DESTINATION);
    checkException("R2eR", SanValidationProblem.FORMAT);

    // Rxe5
    checkException("+xe5", SanValidationProblem.FORMAT_FIRST_CHARACTER);
    checkException("R=e5", SanValidationProblem.FORMAT_PIECE_MIDDLE);
    checkException("RxK5", SanValidationProblem.FORMAT_PIECE_DESTINATION);
    checkException("RxeR", SanValidationProblem.FORMAT);

    // Raxe5
    checkException("+axe5", SanValidationProblem.FORMAT_FIRST_CHARACTER);
    checkException("RKxe5", SanValidationProblem.FORMAT_PIECE_MIDDLE);
    checkException("Ra=e5", SanValidationProblem.FORMAT_PIECE_MIDDLE);
    checkException("RaxK5", SanValidationProblem.FORMAT_PIECE_DESTINATION);
    checkException("RaxeR", SanValidationProblem.FORMAT);

    // R2xe5
    checkException("+2xe5", SanValidationProblem.FORMAT_FIRST_CHARACTER);
    checkException("RRxe5", SanValidationProblem.FORMAT);
    checkException("R2=e5", SanValidationProblem.FORMAT_PIECE_MIDDLE);
    checkException("R2xK5", SanValidationProblem.FORMAT_PIECE_DESTINATION);
    checkException("R2xeR", SanValidationProblem.FORMAT);

    // Ne5
    checkException("+e5", SanValidationProblem.FORMAT_FIRST_CHARACTER);
    checkException("NK5", SanValidationProblem.FORMAT_PIECE_DESTINATION);
    checkException("NeR", SanValidationProblem.FORMAT);

    // Nce5
    checkException("+ce5", SanValidationProblem.FORMAT_FIRST_CHARACTER);
    checkException("NKe5", SanValidationProblem.FORMAT_PIECE_MIDDLE);
    checkException("NcK5", SanValidationProblem.FORMAT_PIECE_DESTINATION);
    checkException("NceR", SanValidationProblem.FORMAT);

    // N4e5
    checkException("+4e5", SanValidationProblem.FORMAT_FIRST_CHARACTER);
    checkException("NRe5", SanValidationProblem.FORMAT);
    checkException("N4K5", SanValidationProblem.FORMAT_PIECE_DESTINATION);
    checkException("N4eR", SanValidationProblem.FORMAT);

    // Nd3e5
    checkException("+d3e5", SanValidationProblem.FORMAT_FIRST_CHARACTER);
    checkException("NK3e5", SanValidationProblem.FORMAT_PIECE_MIDDLE);
    checkException("NdRe5", SanValidationProblem.FORMAT);
    checkException("Nd3K5", SanValidationProblem.FORMAT_PIECE_DESTINATION);
    checkException("Nd3eR", SanValidationProblem.FORMAT);

    // Nxe5
    checkException("+xe5", SanValidationProblem.FORMAT_FIRST_CHARACTER);
    checkException("N=e5", SanValidationProblem.FORMAT_PIECE_MIDDLE);
    checkException("NxK5", SanValidationProblem.FORMAT_PIECE_DESTINATION);
    checkException("NxeR", SanValidationProblem.FORMAT);

    // Ncxe5
    checkException("+cxe5", SanValidationProblem.FORMAT_FIRST_CHARACTER);
    checkException("NKxe5", SanValidationProblem.FORMAT_PIECE_MIDDLE);
    checkException("Nc=e5", SanValidationProblem.FORMAT_PIECE_MIDDLE);
    checkException("NcxK5", SanValidationProblem.FORMAT_PIECE_DESTINATION);
    checkException("NcxeR", SanValidationProblem.FORMAT);

    // N4xe5
    checkException("+4xe5", SanValidationProblem.FORMAT_FIRST_CHARACTER);
    checkException("NRxe5", SanValidationProblem.FORMAT);
    checkException("N4=e5", SanValidationProblem.FORMAT_PIECE_MIDDLE);
    checkException("N4xK5", SanValidationProblem.FORMAT_PIECE_DESTINATION);
    checkException("N4xeR", SanValidationProblem.FORMAT);

    // Nd3xe5
    checkException("+d3xe5", SanValidationProblem.FORMAT_FIRST_CHARACTER);
    checkException("NK3xe5", SanValidationProblem.FORMAT_PIECE_MIDDLE);
    checkException("NdRxe5", SanValidationProblem.FORMAT);
    checkException("Nd3=e5", SanValidationProblem.FORMAT_PIECE_MIDDLE);
    checkException("Nd3xK5", SanValidationProblem.FORMAT_PIECE_DESTINATION);
    checkException("Nd3xeR", SanValidationProblem.FORMAT);

    // Be5
    checkException("+e5", SanValidationProblem.FORMAT_FIRST_CHARACTER);
    checkException("BK5", SanValidationProblem.FORMAT_PIECE_DESTINATION);
    checkException("BeR", SanValidationProblem.FORMAT);

    // Bbe5
    checkException("+be5", SanValidationProblem.FORMAT_FIRST_CHARACTER);
    checkException("BKe5", SanValidationProblem.FORMAT_PIECE_MIDDLE);
    checkException("BbK5", SanValidationProblem.FORMAT_PIECE_DESTINATION);
    checkException("BbeR", SanValidationProblem.FORMAT);

    // B2e5
    checkException("+2e5", SanValidationProblem.FORMAT_FIRST_CHARACTER);
    checkException("BRe5", SanValidationProblem.FORMAT);
    checkException("B2K5", SanValidationProblem.FORMAT_PIECE_DESTINATION);
    checkException("B2eR", SanValidationProblem.FORMAT);

    // Bxe5
    checkException("+xe5", SanValidationProblem.FORMAT_FIRST_CHARACTER);
    checkException("B=e5", SanValidationProblem.FORMAT_PIECE_MIDDLE);
    checkException("BxK5", SanValidationProblem.FORMAT_PIECE_DESTINATION);
    checkException("BxeR", SanValidationProblem.FORMAT);

    // Bbxe5
    checkException("+bxe5", SanValidationProblem.FORMAT_FIRST_CHARACTER);
    checkException("BKxe5", SanValidationProblem.FORMAT_PIECE_MIDDLE);
    checkException("Bb=e5", SanValidationProblem.FORMAT_PIECE_MIDDLE);
    checkException("BbxK5", SanValidationProblem.FORMAT_PIECE_DESTINATION);
    checkException("BbxeR", SanValidationProblem.FORMAT);

    // B2xe5
    checkException("+2xe5", SanValidationProblem.FORMAT_FIRST_CHARACTER);
    checkException("BRxe5", SanValidationProblem.FORMAT);
    checkException("B2=e5", SanValidationProblem.FORMAT_PIECE_MIDDLE);
    checkException("B2xK5", SanValidationProblem.FORMAT_PIECE_DESTINATION);
    checkException("B2xeR", SanValidationProblem.FORMAT);

    // Ke5
    checkException("+e5", SanValidationProblem.FORMAT_FIRST_CHARACTER);
    checkException("KK5", SanValidationProblem.FORMAT);
    checkException("KeR", SanValidationProblem.FORMAT_KING_DESTINATION);

    // Kxe5
    checkException("+xe5", SanValidationProblem.FORMAT_FIRST_CHARACTER);
    checkException("K=e5", SanValidationProblem.FORMAT_KING_SECOND_CHARACTER);
    checkException("KxK5", SanValidationProblem.FORMAT);
    checkException("KxeR", SanValidationProblem.FORMAT_KING_CAPTURE_DESTINATION);

    // O-O
    checkException("+-O", SanValidationProblem.FORMAT_FIRST_CHARACTER);
    checkException("O=O", SanValidationProblem.FORMAT_CASTLING);
    checkException("O-x", SanValidationProblem.FORMAT_CASTLING);

    // O-O-O
    checkException("+-O-O", SanValidationProblem.FORMAT_FIRST_CHARACTER);
    checkException("O=O-O", SanValidationProblem.FORMAT);
    checkException("O-x-O", SanValidationProblem.FORMAT_CASTLING);
    checkException("O-O=O", SanValidationProblem.FORMAT_CASTLING);
    checkException("O-O-x", SanValidationProblem.FORMAT_CASTLING);

  }

  @SuppressWarnings("static-method")
  @Test
  void testPromotionValid() {

    checkValid("d1=Q");
    checkValid("cxd1=Q");

    checkValid("d8=Q");
    checkValid("cxd8=Q");
    checkValid("d8=R");
    checkValid("d8=B");
    checkValid("d8=N");
    checkValid("d8=Q");

    checkValid("cxd8=R");
    checkValid("cxd8=B");
    checkValid("cxd8=N");
    checkValid("cxd8=Q");

    checkValid("c1=R");
    checkValid("c1=B");
    checkValid("c1=N");
    checkValid("c1=Q");

    checkValid("cxb1=R");
    checkValid("cxb1=B");
    checkValid("cxb1=N");
    checkValid("cxb1=Q");

  }

  @SuppressWarnings("static-method")
  @Test
  void testPromotionMissingPromotionPiece() {
    checkException("d8=", SanValidationProblem.FORMAT);
    checkException("d1=", SanValidationProblem.FORMAT);
  }

  @SuppressWarnings("static-method")
  @Test
  void testPromotionToKing() {
    checkException("d8=K", SanValidationProblem.FORMAT);
    checkException("d1=K", SanValidationProblem.FORMAT);
  }

  @SuppressWarnings("static-method")
  @Test
  void testPromotionToPawn() {
    checkException("d8=P", SanValidationProblem.FORMAT_INVALID_CHARACTER);
    checkException("d1=P", SanValidationProblem.FORMAT_INVALID_CHARACTER);
  }

  @SuppressWarnings("static-method")
  @Test
  void testPromotionMiddleOfBoard() {
    checkValid("d8=Q");
    checkValid("d1=N");
  }

  @SuppressWarnings("static-method")
  @Test
  void testValid() {
    // for pawn moves we are here not checking if valid destination or promotion rank
    // so we can use the same test for white and black

    // (1a) pawnNonCapturingNonPromotionMoves d3 not d8
    {
      final var expectedSanType = SanType.PAWN_NON_CAPTURING_NON_PROMOTION_MOVE;
      final var expectedSanConversion = new SanConversion(File.NONE, Rank.NONE, Square.D3, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final var san = "d3";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    {
      final var expectedSanType = SanType.PAWN_NON_CAPTURING_PROMOTION_MOVE;
      final var expectedSanConversion = new SanConversion(File.NONE, Rank.NONE, Square.D8, PromotionPieceType.KNIGHT,
          CheckmateOrCheck.NONE);
      final var san = "d8=N";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    {
      final var expectedSanType = SanType.PAWN_NON_CAPTURING_PROMOTION_MOVE;
      final var expectedSanConversion = new SanConversion(File.NONE, Rank.NONE, Square.D1, PromotionPieceType.QUEEN,
          CheckmateOrCheck.NONE);
      final var san = "d1=Q";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    // (1b) pawnCapturingNonPromotionMoves dxe5 not dxe8
    {
      final var expectedSanType = SanType.PAWN_CAPTURING_NON_PROMOTION_MOVE;
      final var expectedSanConversion = new SanConversion(File.FILE_D, Rank.NONE, Square.E5, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final var san = "dxe5";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    {
      final var expectedSanType = SanType.PAWN_CAPTURING_PROMOTION_MOVE;
      final var expectedSanConversion = new SanConversion(File.FILE_D, Rank.NONE, Square.E8, PromotionPieceType.QUEEN,
          CheckmateOrCheck.NONE);
      final var san = "dxe8=Q";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    {
      final var expectedSanType = SanType.PAWN_CAPTURING_PROMOTION_MOVE;
      final var expectedSanConversion = new SanConversion(File.FILE_D, Rank.NONE, Square.E1, PromotionPieceType.QUEEN,
          CheckmateOrCheck.NONE);
      final var san = "dxe1=Q";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    // (1c) pawnNonCapturingPromotionMoves d8=Q
    {
      final var expectedSanType = SanType.PAWN_NON_CAPTURING_PROMOTION_MOVE;
      final var expectedSanConversion = new SanConversion(File.NONE, Rank.NONE, Square.D8, PromotionPieceType.QUEEN,
          CheckmateOrCheck.NONE);
      final var san = "d8=Q";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    {
      final var expectedSanType = SanType.PAWN_NON_CAPTURING_PROMOTION_MOVE;
      final var expectedSanConversion = new SanConversion(File.NONE, Rank.NONE, Square.D1, PromotionPieceType.QUEEN,
          CheckmateOrCheck.NONE);
      final var san = "d1=Q";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    {
      final var san = "d7=Q";
      checkException(san, SanValidationProblem.FORMAT_PAWN_LENGTH);
    }
    // (1d) pawnCapturingPromotionMoves dxe8=Q
    {
      final var expectedSanType = SanType.PAWN_CAPTURING_PROMOTION_MOVE;
      final var expectedSanConversion = new SanConversion(File.FILE_D, Rank.NONE, Square.E8, PromotionPieceType.QUEEN,
          CheckmateOrCheck.NONE);
      final var san = "dxe8=Q";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    {
      final var expectedSanType = SanType.PAWN_CAPTURING_PROMOTION_MOVE;
      final var expectedSanConversion = new SanConversion(File.FILE_D, Rank.NONE, Square.E1, PromotionPieceType.QUEEN,
          CheckmateOrCheck.NONE);
      final var san = "dxe1=Q";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    {
      final var san = "dxe7=Q";
      checkException(san, SanValidationProblem.FORMAT_PAWN_LENGTH);
    }
    // (2a) queenNonCapturingMoves Qe5, Qae5, Q2e5, Qc3e5
    {
      checkValid(SanType.QUEEN_NON_CAPTURING_NEITHER_MOVE,
          new SanConversion(File.NONE, Rank.NONE, Square.E5, PromotionPieceType.NONE, CheckmateOrCheck.NONE), "Qe5");
    }
    {
      checkValid(SanType.QUEEN_NON_CAPTURING_FILE_MOVE,
          new SanConversion(File.FILE_A, Rank.NONE, Square.E5, PromotionPieceType.NONE, CheckmateOrCheck.NONE), "Qae5");
    }
    {
      checkValid(SanType.QUEEN_NON_CAPTURING_RANK_MOVE,
          new SanConversion(File.NONE, Rank.RANK_2, Square.E5, PromotionPieceType.NONE, CheckmateOrCheck.NONE), "Q2e5");
    }
    {
      checkValid(SanType.QUEEN_NON_CAPTURING_SQUARE_MOVE,
          new SanConversion(File.FILE_C, Rank.RANK_3, Square.E5, PromotionPieceType.NONE, CheckmateOrCheck.NONE),
          "Qc3e5");
    }
    // (2b) queenCapturingMoves Qxe5, Qaxe5, Q2xe5, Qc3xe5
    {
      checkValid(SanType.QUEEN_CAPTURING_NEITHER_MOVE,
          new SanConversion(File.NONE, Rank.NONE, Square.E5, PromotionPieceType.NONE, CheckmateOrCheck.NONE), "Qxe5");
    }
    {
      checkValid(SanType.QUEEN_CAPTURING_FILE_MOVE,
          new SanConversion(File.FILE_A, Rank.NONE, Square.E5, PromotionPieceType.NONE, CheckmateOrCheck.NONE),
          "Qaxe5");
    }
    {
      checkValid(SanType.QUEEN_CAPTURING_RANK_MOVE,
          new SanConversion(File.NONE, Rank.RANK_2, Square.E5, PromotionPieceType.NONE, CheckmateOrCheck.NONE),
          "Q2xe5");
    }
    {
      checkValid(SanType.QUEEN_CAPTURING_SQUARE_MOVE,
          new SanConversion(File.FILE_C, Rank.RANK_3, Square.E5, PromotionPieceType.NONE, CheckmateOrCheck.NONE),
          "Qc3xe5");
    }
    // (3a) rookNonCapturingMoves Re5, Rae5, R2e5
    {
      checkValid(SanType.ROOK_NON_CAPTURING_NEITHER_MOVE,
          new SanConversion(File.NONE, Rank.NONE, Square.E5, PromotionPieceType.NONE, CheckmateOrCheck.NONE), "Re5");
    }
    {
      checkValid(SanType.ROOK_NON_CAPTURING_FILE_MOVE,
          new SanConversion(File.FILE_A, Rank.NONE, Square.E5, PromotionPieceType.NONE, CheckmateOrCheck.NONE), "Rae5");
    }
    {
      checkValid(SanType.ROOK_NON_CAPTURING_RANK_MOVE,
          new SanConversion(File.NONE, Rank.RANK_2, Square.E5, PromotionPieceType.NONE, CheckmateOrCheck.NONE), "R2e5");
    }
    // (3b) rookCapturingMoves Rxe5, Raxe5, R2xe5
    {
      checkValid(SanType.ROOK_CAPTURING_NEITHER_MOVE,
          new SanConversion(File.NONE, Rank.NONE, Square.E5, PromotionPieceType.NONE, CheckmateOrCheck.NONE), "Rxe5");
    }
    {
      checkValid(SanType.ROOK_CAPTURING_FILE_MOVE,
          new SanConversion(File.FILE_A, Rank.NONE, Square.E5, PromotionPieceType.NONE, CheckmateOrCheck.NONE),
          "Raxe5");
    }
    {
      checkValid(SanType.ROOK_CAPTURING_RANK_MOVE,
          new SanConversion(File.NONE, Rank.RANK_2, Square.E5, PromotionPieceType.NONE, CheckmateOrCheck.NONE),
          "R2xe5");
    }
    // (4a) knightNonCapturingMoves Ne5, Nce5, N4e5, Nd3e5
    {
      checkValid(SanType.KNIGHT_NON_CAPTURING_NEITHER_MOVE,
          new SanConversion(File.NONE, Rank.NONE, Square.E5, PromotionPieceType.NONE, CheckmateOrCheck.NONE), "Ne5");
    }
    {
      checkValid(SanType.KNIGHT_NON_CAPTURING_FILE_MOVE,
          new SanConversion(File.FILE_C, Rank.NONE, Square.E5, PromotionPieceType.NONE, CheckmateOrCheck.NONE), "Nce5");
    }
    {
      checkValid(SanType.KNIGHT_NON_CAPTURING_RANK_MOVE,
          new SanConversion(File.NONE, Rank.RANK_4, Square.E5, PromotionPieceType.NONE, CheckmateOrCheck.NONE), "N4e5");
    }
    {
      checkValid(SanType.KNIGHT_NON_CAPTURING_SQUARE_MOVE,
          new SanConversion(File.FILE_D, Rank.RANK_3, Square.E5, PromotionPieceType.NONE, CheckmateOrCheck.NONE),
          "Nd3e5");
    }
    // (4b) knightCapturingMoves Nxe5, Ncxe5, N4xe5, Nd3xe5
    {
      checkValid(SanType.KNIGHT_CAPTURING_NEITHER_MOVE,
          new SanConversion(File.NONE, Rank.NONE, Square.E5, PromotionPieceType.NONE, CheckmateOrCheck.NONE), "Nxe5");
    }
    {
      checkValid(SanType.KNIGHT_CAPTURING_FILE_MOVE,
          new SanConversion(File.FILE_C, Rank.NONE, Square.E5, PromotionPieceType.NONE, CheckmateOrCheck.NONE),
          "Ncxe5");
    }
    {
      checkValid(SanType.KNIGHT_CAPTURING_RANK_MOVE,
          new SanConversion(File.NONE, Rank.RANK_4, Square.E5, PromotionPieceType.NONE, CheckmateOrCheck.NONE),
          "N4xe5");
    }
    {
      checkValid(SanType.KNIGHT_CAPTURING_SQUARE_MOVE,
          new SanConversion(File.FILE_D, Rank.RANK_3, Square.E5, PromotionPieceType.NONE, CheckmateOrCheck.NONE),
          "Nd3xe5");
    }
    // (5a) bishopNonCapturingMoves Be5, Bbe5, B2e5
    {
      checkValid(SanType.BISHOP_NON_CAPTURING_NEITHER_MOVE,
          new SanConversion(File.NONE, Rank.NONE, Square.E5, PromotionPieceType.NONE, CheckmateOrCheck.NONE), "Be5");
    }
    {
      checkValid(SanType.BISHOP_NON_CAPTURING_FILE_MOVE,
          new SanConversion(File.FILE_B, Rank.NONE, Square.E5, PromotionPieceType.NONE, CheckmateOrCheck.NONE), "Bbe5");
    }
    {
      checkValid(SanType.BISHOP_NON_CAPTURING_RANK_MOVE,
          new SanConversion(File.NONE, Rank.RANK_2, Square.E5, PromotionPieceType.NONE, CheckmateOrCheck.NONE), "B2e5");
    }
    // (5b) bishopCapturingMoves Bxe5, Bbxe5, B2xe5
    {
      checkValid(SanType.BISHOP_CAPTURING_NEITHER_MOVE,
          new SanConversion(File.NONE, Rank.NONE, Square.E5, PromotionPieceType.NONE, CheckmateOrCheck.NONE), "Bxe5");
    }
    {
      checkValid(SanType.BISHOP_CAPTURING_FILE_MOVE,
          new SanConversion(File.FILE_B, Rank.NONE, Square.E5, PromotionPieceType.NONE, CheckmateOrCheck.NONE),
          "Bbxe5");
    }
    {
      checkValid(SanType.BISHOP_CAPTURING_RANK_MOVE,
          new SanConversion(File.NONE, Rank.RANK_2, Square.E5, PromotionPieceType.NONE, CheckmateOrCheck.NONE),
          "B2xe5");
    }
    // (6a) kingNonCastlingNonCapturingMoves Ke5
    {
      checkValid(SanType.KING_NON_CASTLING_NON_CAPTURING_MOVE,
          new SanConversion(File.NONE, Rank.NONE, Square.E5, PromotionPieceType.NONE, CheckmateOrCheck.NONE), "Ke5");
    }
    // (6b) kingNonCastlingCapturingMoves Kxe5
    {
      checkValid(SanType.KING_NON_CASTLING_CAPTURING_MOVE,
          new SanConversion(File.NONE, Rank.NONE, Square.E5, PromotionPieceType.NONE, CheckmateOrCheck.NONE), "Kxe5");
    }
    // (6c) kingMovesCastling O-O and O-O-O
    {
      checkValid(SanType.KING_CASTLING_KING_SIDE_MOVE,
          new SanConversion(File.NONE, Rank.NONE, Square.NONE, PromotionPieceType.NONE, CheckmateOrCheck.NONE),
          CastlingConstants.SAN_CASTLING_KING_SIDE);
    }
    {
      checkValid(SanType.KING_CASTLING_QUEEN_SIDE_MOVE,
          new SanConversion(File.NONE, Rank.NONE, Square.NONE, PromotionPieceType.NONE, CheckmateOrCheck.NONE),
          CastlingConstants.SAN_CASTLING_QUEEN_SIDE);
    }
  }

  private static void checkValid(SanType expectedSanType, SanConversion expectedSanConversion, String san) {
    final SanParse expectedSanExtract = new SanParse(expectedSanType, expectedSanConversion);

    final SanParse calculatedSanExtract = SanValidateFormat.validateFormat(san);
    assertEquals(expectedSanExtract, calculatedSanExtract);
    assertEquals(validateFormatReference(san), calculatedSanExtract);
  }

  private static void checkValid(String san) {
    checkValidateFormat(san, false, null);
  }

  private static void checkException(String san, SanValidationProblem expectedProblem) {
    checkValidateFormat(san, true, expectedProblem);
  }

  private static void checkValidateFormat(String san, boolean isExceptionExpected,
      SanValidationProblem expectedProblem) {
    checkValidateFormatTest(san, isExceptionExpected, expectedProblem);
    checkValidateFormatReference(san, isExceptionExpected, expectedProblem);

  }

  private static void checkValidateFormatTest(String san, boolean isExceptionExpected,
      SanValidationProblem expectedProblem) {
    boolean isException;
    SanParse result = null;
    try {
      result = SanValidateFormat.validateFormat(san);
      isException = false;
    } catch (final SanValidationException e) {
      isException = true;
      System.out.println("SAN: " + san + " -> " + e.getMessage());
      assertEquals(expectedProblem, e.getSanValidationProblem());
    }
    assertEquals(isExceptionExpected, isException);
  }

  private static void checkValidateFormatReference(String san, boolean isExceptionExpected,
      SanValidationProblem expectedProblem) {

    boolean isException;
    SanParse result = null;
    try {
      result = validateFormatReference(san);
      isException = false;
    } catch (@SuppressWarnings("unused") final SanValidationException e) {
      isException = true;
    }
    assertEquals(isExceptionExpected, isException);
  }

  private static SanParse validateFormatReference(String san) {
    return SanValidateFormatReference.validateFormat(san);
  }

}
