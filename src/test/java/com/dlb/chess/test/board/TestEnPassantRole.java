package com.dlb.chess.test.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.model.EnPassantRole;
import com.dlb.chess.model.LegalMove;

/**
 * Lock-down coverage for {@link EnPassantRole} as stored on {@link LegalMove}.
 *
 * <p>
 * Each of the three enum values must be producible by the legal-move generator for at least one constructible position.
 * These tests exercise representative board states and assert that the expected move in each position carries the
 * expected {@code EnPassantRole}.
 */
class TestEnPassantRole {

  @SuppressWarnings("static-method")
  @Test
  void testNoneForRegularMoves() {
    final Board board = new Board();
    // From the initial position, any piece-move (e.g. Nb1-c3) has role NONE.
    final LegalMove knightToC3 = findLegalMoveByFromTo(board, Square.B1, Square.C3);
    assertEquals(EnPassantRole.NONE, knightToC3.enPassantRole());
  }

  @SuppressWarnings("static-method")
  @Test
  void testTwoSquareAdvanceForPawnInitialDoublePush() {
    final Board board = new Board();
    // 1.e4 from the initial position — white pawn e2 → e4 is a two-square advance.
    final LegalMove e2ToE4 = findLegalMoveByFromTo(board, Square.E2, Square.E4);
    assertEquals(EnPassantRole.TWO_SQUARE_ADVANCE, e2ToE4.enPassantRole());
    assertTrue(e2ToE4.enPassantRole().createsEnPassantTarget());
  }

  @SuppressWarnings("static-method")
  @Test
  void testNoneForPawnSingleAdvance() {
    final Board board = new Board();
    // 1.e3 is a one-square advance, not two-square — role NONE.
    final LegalMove e2ToE3 = findLegalMoveByFromTo(board, Square.E2, Square.E3);
    assertEquals(EnPassantRole.NONE, e2ToE3.enPassantRole());
  }

  @SuppressWarnings("static-method")
  @Test
  void testEnPassantCaptureAfterTwoSquareAdvance() {
    // Classic en-passant setup: 1.e4 Nf6 2.e5 d5 — now white's e5 pawn can capture d-pawn en passant.
    final Board board = new Board();
    board.movesStrict("e4", "Nf6", "e5", "d5");
    final LegalMove exd6 = findLegalMoveByFromTo(board, Square.E5, Square.D6);
    assertEquals(EnPassantRole.EN_PASSANT_CAPTURE, exd6.enPassantRole());
    assertTrue(exd6.enPassantRole().isEnPassantCapture());
  }

  @SuppressWarnings("static-method")
  @Test
  void testNoneForRegularPawnCapture() {
    // Plain diagonal pawn capture (not en passant).
    final Board board = new Board();
    board.movesStrict("e4", "d5");
    // Now 2.exd5 is a regular capture, not en passant.
    final LegalMove exd5 = findLegalMoveByFromTo(board, Square.E4, Square.D5);
    assertEquals(EnPassantRole.NONE, exd5.enPassantRole());
  }

  /**
   * Utility: find the unique legal move from {@code from} to {@code to} on the given board, or fail.
   */
  private static LegalMove findLegalMoveByFromTo(Board board, Square from, Square to) {
    for (final LegalMove legalMove : board.getLegalMoveSet()) {
      if (legalMove.moveSpecification().fromSquare() == from && legalMove.moveSpecification().toSquare() == to) {
        return legalMove;
      }
    }
    throw new AssertionError("no legal move from " + from + " to " + to);
  }
}
