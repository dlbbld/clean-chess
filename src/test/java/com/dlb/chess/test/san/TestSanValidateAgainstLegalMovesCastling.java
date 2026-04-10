package com.dlb.chess.test.san;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.CastlingRightLoss;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.enums.MoveCheck;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.san.validate.SanValidation;

class TestSanValidateAgainstLegalMovesCastling {

  // --- Priority 1: King or rook not on required square ---

  @SuppressWarnings("static-method")
  @Test
  void testKingNotOnRequiredSquare() {
    // King moved off e1 via FEN
    final ApiBoard board = new Board("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R2K3R w - - 0 1");
    checkCastlingException("O-O", board, MoveCheck.CASTLING_PRIORITY_1_KING_OR_ROOK_NOT_ON_REQUIRED_SQUARE,
        CastlingRightLoss.NONE);
    checkCastlingException("O-O-O", board, MoveCheck.CASTLING_PRIORITY_1_KING_OR_ROOK_NOT_ON_REQUIRED_SQUARE,
        CastlingRightLoss.NONE);
  }

  @SuppressWarnings("static-method")
  @Test
  void testRookNotOnRequiredSquare() {
    // King-side rook not on h1
    final ApiBoard board = new Board("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K1R1 w Q - 0 1");
    checkCastlingException("O-O", board, MoveCheck.CASTLING_PRIORITY_1_KING_OR_ROOK_NOT_ON_REQUIRED_SQUARE,
        CastlingRightLoss.NONE);
  }

  // --- Priority 2: No castling right - king moved ---

  @SuppressWarnings("static-method")
  @Test
  void testNoRightKingMovedWhite() {
    final ApiBoard board = new Board();
    board.performMoves("e4", "e5", "Ke2", "d6", "Ke1", "d5");
    checkCastlingException("O-O", board, MoveCheck.CASTLING_PRIORITY_2_NO_CASTLING_RIGHT_ON_THIS_SIDE,
        CastlingRightLoss.KING_MOVED);
    checkCastlingException("O-O-O", board, MoveCheck.CASTLING_PRIORITY_2_NO_CASTLING_RIGHT_ON_THIS_SIDE,
        CastlingRightLoss.KING_MOVED);
  }

  @SuppressWarnings("static-method")
  @Test
  void testNoRightKingMovedBlack() {
    final ApiBoard board = new Board();
    board.performMoves("e4", "e5", "d4", "Ke7", "d5", "Ke8", "Nf3");
    checkCastlingException("O-O", board, MoveCheck.CASTLING_PRIORITY_2_NO_CASTLING_RIGHT_ON_THIS_SIDE,
        CastlingRightLoss.KING_MOVED);
  }

  // --- Priority 2: No castling right - rook moved ---

  @SuppressWarnings("static-method")
  @Test
  void testNoRightKingSideRookMoved() {
    final ApiBoard board = new Board();
    board.performMoves("h4", "e5", "Rh3", "d6", "Rh1", "d5");
    checkCastlingException("O-O", board, MoveCheck.CASTLING_PRIORITY_2_NO_CASTLING_RIGHT_ON_THIS_SIDE,
        CastlingRightLoss.ROOK_MOVED);
  }

  @SuppressWarnings("static-method")
  @Test
  void testNoRightQueenSideRookMoved() {
    final ApiBoard board = new Board();
    board.performMoves("a4", "e5", "Ra3", "d6", "Ra1", "d5");
    board.performMoves("b3", "Nc6", "Bb2", "Be7", "Nc3", "Nf6", "Qc1", "a6");
    checkCastlingException("O-O-O", board, MoveCheck.CASTLING_PRIORITY_2_NO_CASTLING_RIGHT_ON_THIS_SIDE,
        CastlingRightLoss.ROOK_MOVED);
  }

  // --- Priority 2: No castling right - unknown (FEN import) ---

  @SuppressWarnings("static-method")
  @Test
  void testNoRightUnknownFenImport() {
    final ApiBoard board = new Board("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w - - 0 1");
    checkCastlingException("O-O", board, MoveCheck.CASTLING_PRIORITY_2_NO_CASTLING_RIGHT_ON_THIS_SIDE,
        CastlingRightLoss.UNKNOWN_FEN_IMPORT);
    checkCastlingException("O-O-O", board, MoveCheck.CASTLING_PRIORITY_2_NO_CASTLING_RIGHT_ON_THIS_SIDE,
        CastlingRightLoss.UNKNOWN_FEN_IMPORT);
  }

  // --- Priority 3: Squares between king and rook not empty ---

  @SuppressWarnings("static-method")
  @Test
  void testSquaresNotEmptyWhite() {
    // Initial position, white tries to castle
    final ApiBoard board = new Board();
    board.performMoves("e4", "e5");
    // King-side: f1 and g1 occupied
    checkCastlingException("O-O", board, MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY,
        CastlingRightLoss.NONE);
    // Queen-side: b1, c1, d1 occupied
    checkCastlingException("O-O-O", board, MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY,
        CastlingRightLoss.NONE);
  }

  @SuppressWarnings("static-method")
  @Test
  void testSquaresNotEmptyBlack() {
    final ApiBoard board = new Board();
    board.performMoves("e4");
    checkCastlingException("O-O", board, MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY,
        CastlingRightLoss.NONE);
    checkCastlingException("O-O-O", board, MoveCheck.CASTLING_PRIORITY_3_SQUARES_BETWEEN_KING_AND_ROOK_NOT_EMPTY,
        CastlingRightLoss.NONE);
  }

  // --- Priority 4: King in check ---

  @SuppressWarnings("static-method")
  @Test
  void testKingInCheckWhite() {
    final ApiBoard board = new Board("rnbqk2r/pppp1ppp/5n2/4p3/2B1P3/2N2N2/PPPP1bPP/R1BQK2R w KQkq - 0 5");
    checkCastlingException("O-O", board, MoveCheck.CASTLING_PRIORITY_4_KING_IN_CHECK, CastlingRightLoss.NONE);
  }

  @SuppressWarnings("static-method")
  @Test
  void testKingInCheckBlack() {
    final ApiBoard board = new Board("r1bqk2r/1ppp1pNp/p1n2n2/2b1p3/2B1P3/8/PPPP1PPP/RNBQK2R b KQkq - 0 6");
    checkCastlingException("O-O", board, MoveCheck.CASTLING_PRIORITY_4_KING_IN_CHECK, CastlingRightLoss.NONE);
  }

  // --- Priority 5: King would travel through check ---

  @SuppressWarnings("static-method")
  @Test
  void testKingWouldTravelThroughCheckWhite() {
    final ApiBoard board = new Board("rnb1kbnr/pppp2pp/5q2/8/2B5/7N/PPPP2PP/RNBQK2R w KQkq - 0 25");
    checkCastlingException("O-O", board, MoveCheck.CASTLING_PRIORITY_5_KING_WOULD_TRAVEL_THROUGH_CHECK,
        CastlingRightLoss.NONE);
  }

  @SuppressWarnings("static-method")
  @Test
  void testKingWouldTravelThroughCheckBlack() {
    final ApiBoard board = new Board("rnbqk2r/ppppppbp/4Nnp1/8/8/8/PPPPPPPP/R1BQKBNR b KQkq - 0 25");
    checkCastlingException("O-O", board, MoveCheck.CASTLING_PRIORITY_5_KING_WOULD_TRAVEL_THROUGH_CHECK,
        CastlingRightLoss.NONE);
  }

  // --- Priority 6: King would end in check ---

  @SuppressWarnings("static-method")
  @Test
  void testKingWouldEndInCheckWhite() {
    final ApiBoard board = new Board("rnbqk1nr/pppp1ppp/4p3/2b5/2B1P3/5P1N/PPPP2PP/RNBQK2R w KQkq - 0 25");
    checkCastlingException("O-O", board, MoveCheck.CASTLING_PRIORITY_6_KING_WOULD_END_IN_CHECK,
        CastlingRightLoss.NONE);
  }

  @SuppressWarnings("static-method")
  @Test
  void testKingWouldEndInCheckBlack() {
    final ApiBoard board = new Board("rnbqk2r/ppppppbp/6pN/8/6n1/4P3/PPPP1PPP/RNBQKB1R b KQkq - 0 25");
    checkCastlingException("O-O", board, MoveCheck.CASTLING_PRIORITY_6_KING_WOULD_END_IN_CHECK,
        CastlingRightLoss.NONE);
  }

  private static void checkCastlingException(String san, ApiBoard board, MoveCheck expectedMoveCheck,
      CastlingRightLoss expectedLoss) {
    boolean isException;
    try {
      SanValidation.validateSan(san, board);
      isException = false;
    } catch (final SanValidationException e) {
      isException = true;
      assertEquals(SanValidationProblem.KING_CASTLING_NOT_POSSIBLE, e.getSanValidationProblem());
      assertEquals(expectedMoveCheck, e.getMoveCheck());
      assertEquals(expectedLoss, e.getCastlingRightLoss());
    }
    assertTrue(isException);
  }

}
