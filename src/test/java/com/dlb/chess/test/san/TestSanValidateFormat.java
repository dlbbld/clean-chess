package com.dlb.chess.test.san;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

class TestSanValidateFormat {

  // (1a) pawnNonCapturingNonPromotionMoves d3 not d8
  // (1b) pawnCapturingNonPromotionMoves dxe5 not dxe8
  // (1c) pawnNonCapturingPromotionMoves d8=Q
  // (1d) pawnCapturingPromotionMoves dxe8=Q
  // (2a) queenNonCapturingMoves Qe5, Qae5, Q2e5, Qc3e5
  // (2b) queenCapturingMoves Qxe5, Qaxe5, Q2xe5, Qc3xe5
  // (3a) rookNonCapturingMoves Re5, Rae5, R2e5
  // (3b) rookCapturingMoves Rxe5, Raxe5, R2xe5
  // (4a) knightNonCapturingMoves Ne5, Nce5, N4e5, Nd3e5
  // (4b) knightCapturingMoves Nxe5, Ncxe5, N4xe5, Nd3xe5
  // (5a) bishopNonCapturingMoves Be5, Bbe5, B2e5
  // (5b) bishopCapturingMoves Bxe5, Bbxe5, B2xe5
  // (6a) kingNonCastlingNonCapturingMoves Ke5
  // (6b) kingNonCastlingCapturingMoves Kxe5
  // (6c) kingMovesCastling O-O and O-O-O

  @SuppressWarnings("static-method")
  @Test
  void testInvalid() {
    // d3
    checkException("i3");
    checkException("d9");

    // dxe5
    checkException("Pxe5");
    checkException("d=e5");
    checkException("dxi5");
    checkException("dxe9");

    // d8=Q
    checkException("i8=Q");
    checkException("d9=Q");
    checkException("d8xQ");
    checkException("d8=P");

    // dxe8=Q
    checkException("ixe8=Q");
    checkException("d=e8=Q");
    checkException("dxi=Q");
    checkException("dxe9=Q");
    checkException("dxe8xQ");
    checkException("dxe8=P");

    // Qe5
    checkException("Pe5");
    checkException("Qi5");
    checkException("Qe9");

    // Qae5
    checkException("Pae5");
    checkException("Qie5");
    checkException("Qai5");
    checkException("Qae9");

    // Q2e5
    checkException("P2e5");
    checkException("Q9e5");
    checkException("Q2i5");
    checkException("Q2e9");

    // Qc3e5
    checkException("Pc3e5");
    checkException("Qi3e5");
    checkException("Qc9e5");
    checkException("Qc3i5");
    checkException("Qc3e9");

    // Qxe5
    checkException("Pxe5");
    checkException("Q=e5");
    checkException("Qxi5");
    checkException("Qxe9");

    // Qaxe5
    checkException("Paxe5");
    checkException("Qixe5");
    checkException("Qa=e5");
    checkException("Qaxi5");
    checkException("Qaxe9");

    // Q2xe5
    checkException("P2xe5");
    checkException("Q9xe5");
    checkException("Q2=e5");
    checkException("Q2xi5");
    checkException("Q2xe9");

    // Qc3xe5
    checkException("Pc3xe5");
    checkException("Qi3xe5");
    checkException("Qc9xe5");
    checkException("Qc3=e5");
    checkException("Qc3xi5");
    checkException("Qc3xe9");

    // Re5
    checkException("Pe5");
    checkException("Ri5");
    checkException("Re9");

    // Rae5
    checkException("Pae5");
    checkException("Rie5");
    checkException("Rai5");
    checkException("Rae9");

    // R2e5
    checkException("P2e5");
    checkException("R9e5");
    checkException("R2i5");
    checkException("R2e9");

    // Rxe5
    checkException("Pxe5");
    checkException("R=e5");
    checkException("Rxi5");
    checkException("Rxe9");

    // Raxe5
    checkException("Paxe5");
    checkException("Rixe5");
    checkException("Ra=e5");
    checkException("Raxi5");
    checkException("Raxe9");

    // R2xe5
    checkException("P2xe5");
    checkException("R9xe5");
    checkException("R2=e5");
    checkException("R2xi5");
    checkException("R2xe9");

    // Ne5
    checkException("Pe5");
    checkException("Ni5");
    checkException("Ne9");

    // Nce5
    checkException("Pce5");
    checkException("Nie5");
    checkException("Nci5");
    checkException("Nce9");

    // N4e5
    checkException("P4e5");
    checkException("N9e5");
    checkException("N4i5");
    checkException("N4e9");

    // Nd3e5
    checkException("Pd3e5");
    checkException("Ni3e5");
    checkException("Nd9e5");
    checkException("Nd3i5");
    checkException("Nd3e9");

    // Nxe5
    checkException("Pxe5");
    checkException("N=e5");
    checkException("Nxi5");
    checkException("Nxe9");

    // Ncxe5
    checkException("Pcxe5");
    checkException("Nixe5");
    checkException("Nc=e5");
    checkException("Ncxi5");
    checkException("Ncxe9");

    // N4xe5
    checkException("P4xe5");
    checkException("N9xe5");
    checkException("N4=e5");
    checkException("N4xi5");
    checkException("N4xe9");

    // Nd3xe5
    checkException("Pd3xe5");
    checkException("Ni3xe5");
    checkException("Nd9xe5");
    checkException("Nd3=e5");
    checkException("Nd3xi5");
    checkException("Nd3xe9");

    // Be5
    checkException("Pe5");
    checkException("Bi5");
    checkException("Be9");

    // Bbe5
    checkException("Pbe5");
    checkException("Bie5");
    checkException("Bbi5");
    checkException("Bbe9");

    // B2e5
    checkException("P2e5");
    checkException("B9e5");
    checkException("B2i5");
    checkException("B2e9");

    // Bxe5
    checkException("Pxe5");
    checkException("B=e5");
    checkException("Bxi5");
    checkException("Bxe9");

    // Bbxe5
    checkException("Pbxe5");
    checkException("Bixe5");
    checkException("Bb=e5");
    checkException("Bbxi5");
    checkException("Bbxe9");

    // B2xe5
    checkException("P2xe5");
    checkException("B9xe5");
    checkException("B2=e5");
    checkException("B2xi5");
    checkException("B2xe9");

    // Ke5
    checkException("Pe5");
    checkException("Ki5");
    checkException("Ke9");

    // Kxe5
    checkException("Pxe5");
    checkException("K=e5");
    checkException("Kxi5");
    checkException("Kxe9");

    // O-O
    checkException("P-O");
    checkException("O=O");
    checkException("O-P");

    // O-O-O
    checkException("P-O-O");
    checkException("O=O-O");
    checkException("O-P-O");
    checkException("O-O=O");
    checkException("O-O-P");

  }

  @SuppressWarnings("static-method")
  @Test
  void testValid() {
    // for pawn mves we are here not checking if valid destination or promotion rank
    // so we can use the same test for white and black

    // (1a) pawnNonCapturingNonPromotionMoves d3 not d8
    {
      final var expectedSanType = SanType.PAWN_NON_CAPTURING_NON_PROMOTION_MOVE;
      final var expectedSanConversion = new SanConversion(File.NONE, Rank.NONE, Square.D3, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final var san = "d3";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    // d8 allowed here
    {
      final var expectedSanType = SanType.PAWN_NON_CAPTURING_NON_PROMOTION_MOVE;
      final var expectedSanConversion = new SanConversion(File.NONE, Rank.NONE, Square.D8, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final var san = "d8";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    // d1 allowed here
    {
      final var expectedSanType = SanType.PAWN_NON_CAPTURING_NON_PROMOTION_MOVE;
      final var expectedSanConversion = new SanConversion(File.NONE, Rank.NONE, Square.D1, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final var san = "d1";
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
    // dxe8 allowed here
    {
      final var expectedSanType = SanType.PAWN_CAPTURING_NON_PROMOTION_MOVE;
      final var expectedSanConversion = new SanConversion(File.FILE_D, Rank.NONE, Square.E8, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final var san = "dxe8";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    // dxe1 black version
    {
      final var expectedSanType = SanType.PAWN_CAPTURING_NON_PROMOTION_MOVE;
      final var expectedSanConversion = new SanConversion(File.FILE_D, Rank.NONE, Square.E1, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final var san = "dxe1";
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
    // d1=Q black version
    {
      final var expectedSanType = SanType.PAWN_NON_CAPTURING_PROMOTION_MOVE;
      final var expectedSanConversion = new SanConversion(File.NONE, Rank.NONE, Square.D1, PromotionPieceType.QUEEN,
          CheckmateOrCheck.NONE);
      final var san = "d1=Q";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    // d7=Q allowed here
    {
      final var expectedSanType = SanType.PAWN_NON_CAPTURING_PROMOTION_MOVE;
      final var expectedSanConversion = new SanConversion(File.NONE, Rank.NONE, Square.D7, PromotionPieceType.QUEEN,
          CheckmateOrCheck.NONE);
      final var san = "d7=Q";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    // (1d) pawnCapturingPromotionMoves dxe8=Q
    {
      final var expectedSanType = SanType.PAWN_CAPTURING_PROMOTION_MOVE;
      final var expectedSanConversion = new SanConversion(File.FILE_D, Rank.NONE, Square.E8, PromotionPieceType.QUEEN,
          CheckmateOrCheck.NONE);
      final var san = "dxe8=Q";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    // dxe1=Q black version
    {
      final var expectedSanType = SanType.PAWN_CAPTURING_PROMOTION_MOVE;
      final var expectedSanConversion = new SanConversion(File.FILE_D, Rank.NONE, Square.E1, PromotionPieceType.QUEEN,
          CheckmateOrCheck.NONE);
      final var san = "dxe1=Q";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    // dxe7=Q allowed here
    {
      final var expectedSanType = SanType.PAWN_CAPTURING_PROMOTION_MOVE;
      final var expectedSanConversion = new SanConversion(File.FILE_D, Rank.NONE, Square.E7, PromotionPieceType.QUEEN,
          CheckmateOrCheck.NONE);
      final var san = "dxe7=Q";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    // (2a) queenNonCapturingMoves Qe5, Qae5, Q2e5, Qc3e5
    {
      final var expectedSanType = SanType.QUEEN_NON_CAPTURING_NEITHER_MOVE;
      final var expectedSanConversion = new SanConversion(File.NONE, Rank.NONE, Square.E5, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final var san = "Qe5";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    {
      final var expectedSanType = SanType.QUEEN_NON_CAPTURING_FILE_MOVE;
      final var expectedSanConversion = new SanConversion(File.FILE_A, Rank.NONE, Square.E5, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final var san = "Qae5";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    {
      final var expectedSanType = SanType.QUEEN_NON_CAPTURING_RANK_MOVE;
      final var expectedSanConversion = new SanConversion(File.NONE, Rank.RANK_2, Square.E5, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final var san = "Q2e5";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    {
      final var expectedSanType = SanType.QUEEN_NON_CAPTURING_SQUARE_MOVE;
      final var expectedSanConversion = new SanConversion(File.FILE_C, Rank.RANK_3, Square.E5, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final var san = "Qc3e5";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    // (2b) queenCapturingMoves Qxe5, Qaxe5, Q2xe5, Qc3xe5
    {
      final var expectedSanType = SanType.QUEEN_CAPTURING_NEITHER_MOVE;
      final var expectedSanConversion = new SanConversion(File.NONE, Rank.NONE, Square.E5, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final var san = "Qxe5";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    {
      final var expectedSanType = SanType.QUEEN_CAPTURING_FILE_MOVE;
      final var expectedSanConversion = new SanConversion(File.FILE_A, Rank.NONE, Square.E5, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final var san = "Qaxe5";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    {
      final var expectedSanType = SanType.QUEEN_CAPTURING_RANK_MOVE;
      final var expectedSanConversion = new SanConversion(File.NONE, Rank.RANK_2, Square.E5, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final var san = "Q2xe5";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    {
      final var expectedSanType = SanType.QUEEN_CAPTURING_SQUARE_MOVE;
      final var expectedSanConversion = new SanConversion(File.FILE_C, Rank.RANK_3, Square.E5, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final var san = "Qc3xe5";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    // (3a) rookNonCapturingMoves Re5, Rae5, R2e5
    {
      final var expectedSanType = SanType.ROOK_NON_CAPTURING_NEITHER_MOVE;
      final var expectedSanConversion = new SanConversion(File.NONE, Rank.NONE, Square.E5, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final var san = "Re5";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    {
      final var expectedSanType = SanType.ROOK_NON_CAPTURING_FILE_MOVE;
      final var expectedSanConversion = new SanConversion(File.FILE_A, Rank.NONE, Square.E5, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final var san = "Rae5";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    {
      final var expectedSanType = SanType.ROOK_NON_CAPTURING_RANK_MOVE;
      final var expectedSanConversion = new SanConversion(File.NONE, Rank.RANK_2, Square.E5, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final var san = "R2e5";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    // (3b) rookCapturingMoves Rxe5, Raxe5, R2xe5
    {
      final var expectedSanType = SanType.ROOK_CAPTURING_NEITHER_MOVE;
      final var expectedSanConversion = new SanConversion(File.NONE, Rank.NONE, Square.E5, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final var san = "Rxe5";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    {
      final var expectedSanType = SanType.ROOK_CAPTURING_FILE_MOVE;
      final var expectedSanConversion = new SanConversion(File.FILE_A, Rank.NONE, Square.E5, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final var san = "Raxe5";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    {
      final var expectedSanType = SanType.ROOK_CAPTURING_RANK_MOVE;
      final var expectedSanConversion = new SanConversion(File.NONE, Rank.RANK_2, Square.E5, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final var san = "R2xe5";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    // (4a) knightNonCapturingMoves Ne5, Nce5, N4e5, Nd3e5
    {
      final var expectedSanType = SanType.KNIGHT_NON_CAPTURING_NEITHER_MOVE;
      final var expectedSanConversion = new SanConversion(File.NONE, Rank.NONE, Square.E5, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final var san = "Ne5";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    {
      final var expectedSanType = SanType.KNIGHT_NON_CAPTURING_FILE_MOVE;
      final var expectedSanConversion = new SanConversion(File.FILE_C, Rank.NONE, Square.E5, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final var san = "Nce5";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    {
      final var expectedSanType = SanType.KNIGHT_NON_CAPTURING_RANK_MOVE;
      final var expectedSanConversion = new SanConversion(File.NONE, Rank.RANK_4, Square.E5, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final var san = "N4e5";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    {
      final var expectedSanType = SanType.KNIGHT_NON_CAPTURING_SQUARE_MOVE;
      final var expectedSanConversion = new SanConversion(File.FILE_D, Rank.RANK_3, Square.E5, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final var san = "Nd3e5";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    // (4b) knightCapturingMoves Nxe5, Ncxe5, N4xe5, Nd3xe5
    {
      final var expectedSanType = SanType.KNIGHT_CAPTURING_NEITHER_MOVE;
      final var expectedSanConversion = new SanConversion(File.NONE, Rank.NONE, Square.E5, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final var san = "Nxe5";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    {
      final var expectedSanType = SanType.KNIGHT_CAPTURING_FILE_MOVE;
      final var expectedSanConversion = new SanConversion(File.FILE_C, Rank.NONE, Square.E5, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final var san = "Ncxe5";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    {
      final var expectedSanType = SanType.KNIGHT_CAPTURING_RANK_MOVE;
      final var expectedSanConversion = new SanConversion(File.NONE, Rank.RANK_4, Square.E5, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final var san = "N4xe5";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    {
      final var expectedSanType = SanType.KNIGHT_CAPTURING_SQUARE_MOVE;
      final var expectedSanConversion = new SanConversion(File.FILE_D, Rank.RANK_3, Square.E5, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final var san = "Nd3xe5";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    // (5a) bishopNonCapturingMoves Be5, Bbe5, B2e5
    {
      final var expectedSanType = SanType.BISHOP_NON_CAPTURING_NEITHER_MOVE;
      final var expectedSanConversion = new SanConversion(File.NONE, Rank.NONE, Square.E5, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final var san = "Be5";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    {
      final var expectedSanType = SanType.BISHOP_NON_CAPTURING_FILE_MOVE;
      final var expectedSanConversion = new SanConversion(File.FILE_B, Rank.NONE, Square.E5, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final var san = "Bbe5";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    {
      final var expectedSanType = SanType.BISHOP_NON_CAPTURING_RANK_MOVE;
      final var expectedSanConversion = new SanConversion(File.NONE, Rank.RANK_2, Square.E5, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final var san = "B2e5";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    // (5b) bishopCapturingMoves Bxe5, Bbxe5, B2xe5
    {
      final var expectedSanType = SanType.BISHOP_CAPTURING_NEITHER_MOVE;
      final var expectedSanConversion = new SanConversion(File.NONE, Rank.NONE, Square.E5, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final var san = "Bxe5";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    {
      final var expectedSanType = SanType.BISHOP_CAPTURING_FILE_MOVE;
      final var expectedSanConversion = new SanConversion(File.FILE_B, Rank.NONE, Square.E5, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final var san = "Bbxe5";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    {
      final var expectedSanType = SanType.BISHOP_CAPTURING_RANK_MOVE;
      final var expectedSanConversion = new SanConversion(File.NONE, Rank.RANK_2, Square.E5, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final var san = "B2xe5";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    // (6a) kingNonCastlingNonCapturingMoves Ke5
    {
      final var expectedSanType = SanType.KING_NON_CASTLING_NON_CAPTURING_MOVE;
      final var expectedSanConversion = new SanConversion(File.NONE, Rank.NONE, Square.E5, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final var san = "Ke5";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    // (6b) kingNonCastlingCapturingMoves Kxe5
    {
      final var expectedSanType = SanType.KING_NON_CASTLING_CAPTURING_MOVE;
      final var expectedSanConversion = new SanConversion(File.NONE, Rank.NONE, Square.E5, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final var san = "Kxe5";
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    // (6c) kingMovesCastling O-O and O-O-O
    {
      final var expectedSanType = SanType.KING_CASTLING_KING_SIDE_MOVE;
      final var expectedSanConversion = new SanConversion(File.NONE, Rank.NONE, Square.NONE, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final String san = CastlingConstants.SAN_CASTLING_KING_SIDE;
      checkValid(expectedSanType, expectedSanConversion, san);
    }
    {
      final var expectedSanType = SanType.KING_CASTLING_QUEEN_SIDE_MOVE;
      final var expectedSanConversion = new SanConversion(File.NONE, Rank.NONE, Square.NONE, PromotionPieceType.NONE,
          CheckmateOrCheck.NONE);
      final String san = CastlingConstants.SAN_CASTLING_QUEEN_SIDE;
      checkValid(expectedSanType, expectedSanConversion, san);
    }
  }

  private static void checkException(String san) {
    boolean isException;
    try {
      SanValidateFormat.validateFormat(san);
      isException = false;
    } catch (final SanValidationException e) {
      isException = true;
      assertEquals(SanValidationProblem.FORMAT, e.getSanValidationProblem());
    }
    assertTrue(isException);
  }

  private static void checkValid(SanType expectedSanType, SanConversion expectedSanConversion, String san) {
    final SanParse expectedSanExtract = new SanParse(expectedSanType, expectedSanConversion);

    final SanParse calculatedSanExtractWhite = SanValidateFormat.validateFormat(san);
    assertEquals(expectedSanExtract, calculatedSanExtractWhite);

  }

}