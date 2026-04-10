package com.dlb.chess.test.san.move;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.CastlingRightLoss;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.enums.MoveCheck;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.san.validate.SanValidation;

class TestSanValidateCastling {

  // --- Priority 2: No castling right - king moved ---

  @SuppressWarnings("static-method")
  @Test
  void testNoRightKingMoved() {
    final ApiBoard board = new Board();
    board.performMoves("e4", "e5", "Ke2", "d6", "Ke1", "d5");
    checkCastlingException("O-O", board, MoveCheck.CASTLING_PRIORITY_2_NO_CASTLING_RIGHT_ON_THIS_SIDE,
        CastlingRightLoss.KING_MOVED);
    checkCastlingException("O-O-O", board, MoveCheck.CASTLING_PRIORITY_2_NO_CASTLING_RIGHT_ON_THIS_SIDE,
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
  void testSquaresNotEmpty() {
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
  void testKingInCheck() {
    final ApiBoard board = new Board("rnbqk2r/pppp1ppp/5n2/4p3/2B1P3/2N2N2/PPPP1bPP/R1BQK2R w KQkq - 0 5");
    checkCastlingException("O-O", board, MoveCheck.CASTLING_PRIORITY_4_KING_IN_CHECK, CastlingRightLoss.NONE);
  }

  // --- Priority 5: King would travel through check ---

  @SuppressWarnings("static-method")
  @Test
  void testKingWouldTravelThroughCheck() {
    final ApiBoard board = new Board("rnb1kbnr/pppp2pp/5q2/8/2B5/7N/PPPP2PP/RNBQK2R w KQkq - 0 25");
    checkCastlingException("O-O", board, MoveCheck.CASTLING_PRIORITY_5_KING_WOULD_TRAVEL_THROUGH_CHECK,
        CastlingRightLoss.NONE);
  }

  // --- Priority 6: King would end in check ---

  @SuppressWarnings("static-method")
  @Test
  void testKingWouldEndInCheck() {
    final ApiBoard board = new Board("rnbqk1nr/pppp1ppp/4p3/2b5/2B1P3/5P1N/PPPP2PP/RNBQK2R w KQkq - 0 25");
    checkCastlingException("O-O", board, MoveCheck.CASTLING_PRIORITY_6_KING_WOULD_END_IN_CHECK,
        CastlingRightLoss.NONE);
  }

  private static void checkCastlingException(String san, ApiBoard board, MoveCheck expectedMoveCheck,
      CastlingRightLoss expectedLoss) {
    try {
      SanValidation.validateSan(san, board);
      throw new AssertionError("Expected SanValidationException");
    } catch (final SanValidationException e) {
      assertEquals(SanValidationProblem.KING_CASTLING_NOT_POSSIBLE, e.getSanValidationProblem());
      assertEquals(expectedMoveCheck, e.getMoveCheck());
      assertEquals(expectedLoss, e.getCastlingRightLoss());
    }
  }

}
