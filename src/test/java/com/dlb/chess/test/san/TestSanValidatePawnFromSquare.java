package com.dlb.chess.test.san;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.san.validate.SanValidation;

class TestSanValidatePawnFromSquare {

  // White pawn on d2, black pawn on f7, kings on e1 and e8
  private static final String FEN_BASE = "4k3/5p2/8/8/8/8/3P4/4K3 w - - 0 100";

  // White pawn on d4 (already advanced), black pawn on f5 (already advanced)
  private static final String FEN_ADVANCED = "4k3/8/8/5p2/3P4/8/8/4K3 w - - 0 100";

  // White pawn on d2 with d3 blocked by a black pawn
  private static final String FEN_BLOCKED = "4k3/5p2/8/8/8/3p4/3P4/4K3 w - - 0 100";

  // --- Non-capturing: one-square advance ---

  @SuppressWarnings("static-method")
  @Test
  void testNonCapturingOneSquareWhiteValid() {
    // d3 with white pawn on d2 — valid
    final ApiBoard board = new Board(FEN_BASE);
    checkValid("d3", board);
  }

  @SuppressWarnings("static-method")
  @Test
  void testNonCapturingOneSquareWhiteNoPawn() {
    // e3 — no white pawn on e-file (pawn existence fails first)
    final ApiBoard board = new Board(FEN_BASE);
    checkException("e3", board, SanValidationProblem.PAWN_NO_PIECE_EXISTS);
  }

  @SuppressWarnings("static-method")
  @Test
  void testNonCapturingOneSquareWhiteWrongSquare() {
    // d5 with white pawn on d4 — valid one-square advance
    final ApiBoard board = new Board(FEN_ADVANCED);
    checkValid("d5", board);
  }

  @SuppressWarnings("static-method")
  @Test
  void testNonCapturingOneSquareWhiteNoPawnOnFromSquare() {
    // d6 with white pawn on d4 — no pawn on d5
    final ApiBoard board = new Board(FEN_ADVANCED);
    checkException("d6", board, SanValidationProblem.PAWN_FROM_SQUARE);
  }

  @SuppressWarnings("static-method")
  @Test
  void testNonCapturingOneSquareBlackValid() {
    // f6 with black pawn on f7 — valid
    final ApiBoard board = new Board("4k3/5p2/8/8/8/8/3P4/4K3 b - - 0 100");
    checkValid("f6", board);
  }

  @SuppressWarnings("static-method")
  @Test
  void testNonCapturingOneSquareBlackNoPawnOnFromSquare() {
    // f3 with black pawn on f5 — no pawn on f4
    final ApiBoard board = new Board("4k3/8/8/5p2/3P4/8/8/4K3 b - - 0 100");
    checkException("f3", board, SanValidationProblem.PAWN_FROM_SQUARE);
  }

  // --- Non-capturing: two-square advance ---

  @SuppressWarnings("static-method")
  @Test
  void testNonCapturingTwoSquareWhiteValid() {
    // d4 with white pawn on d2 and d3 empty — valid
    final ApiBoard board = new Board(FEN_BASE);
    checkValid("d4", board);
  }

  @SuppressWarnings("static-method")
  @Test
  void testNonCapturingTwoSquareWhiteBlocked() {
    // d4 with white pawn on d2 but d3 blocked by black pawn — no valid from-square
    final ApiBoard board = new Board(FEN_BLOCKED);
    checkException("d4", board, SanValidationProblem.PAWN_FROM_SQUARE);
  }

  @SuppressWarnings("static-method")
  @Test
  void testNonCapturingTwoSquareWhiteAlreadyAdvanced() {
    {
      final ApiBoard board = new Board(FEN_BASE);
      board.performMove("d4");
      board.performMove("f5");
      checkException("d4", board, SanValidationProblem.PAWN_FROM_SQUARE);
    }
    {
      final ApiBoard board = new Board(FEN_BASE);
      board.performMove("d4");
      board.performMove("f5");
      board.performMove("d5");
      board.performMove("f4");
      checkException("d4", board, SanValidationProblem.PAWN_FROM_SQUARE);
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testNonCapturingTwoSquareBlackValid() {
    // f5 with black pawn on f7 and f6 empty — valid
    final ApiBoard board = new Board("4k3/5p2/8/8/8/8/3P4/4K3 b - - 0 100");
    checkValid("f5", board);
  }

  @SuppressWarnings("static-method")
  @Test
  void testNonCapturingTwoSquareBlackBlocked() {
    // f5 with black pawn on f7 but f6 blocked
    final ApiBoard board = new Board("4k3/5p2/5P2/8/8/8/8/4K3 b - - 0 100");
    checkException("f5", board, SanValidationProblem.PAWN_FROM_SQUARE);
  }

  @SuppressWarnings("static-method")
  @Test
  void testNonCapturingTwoSquareBlackAlreadyAdvanced() {
    {
      final ApiBoard board = new Board(FEN_BASE);
      board.performMove("d4");
      board.performMove("f5");
      board.performMove("d5");
      checkException("f5", board, SanValidationProblem.PAWN_FROM_SQUARE);
    }

    {
      final ApiBoard board = new Board(FEN_BASE);
      board.performMove("d4");
      board.performMove("f5");
      board.performMove("d5");
      board.performMove("f4");
      board.performMove("d6");
      checkException("f5", board, SanValidationProblem.PAWN_FROM_SQUARE);
    }

  }

  // --- Capturing moves ---

  @SuppressWarnings("static-method")
  @Test
  void testCapturingWhiteValid() {
    // dxe5 with white pawn on d4 — pawn on d4, one rank back from e5
    final ApiBoard board = new Board("4k3/8/8/4p3/3P4/8/8/4K3 w - - 0 100");
    checkValid("dxe5", board);
  }

  @SuppressWarnings("static-method")
  @Test
  void testCapturingWhiteNoPawn() {
    // cxd5 — no white pawn on c-file
    final ApiBoard board = new Board("4k3/8/8/3p4/3P4/8/8/4K3 w - - 0 100");
    checkException("cxd5", board, SanValidationProblem.PAWN_NO_PIECE_EXISTS);
  }

  @SuppressWarnings("static-method")
  @Test
  void testCapturingWhiteNoPawnOnFromSquare() {
    // dxe6 with white pawn on d4 — needs pawn on d5, but d5 is empty
    final ApiBoard board = new Board("4k3/8/4p3/8/3P4/8/8/4K3 w - - 0 100");
    checkException("dxe6", board, SanValidationProblem.PAWN_FROM_SQUARE);
  }

  @SuppressWarnings("static-method")
  @Test
  void testCapturingBlackValid() {
    // fxe4 with black pawn on f5 — pawn on f5, one rank back from e4
    final ApiBoard board = new Board("4k3/8/8/5p2/4P3/8/8/4K3 b - - 0 100");
    checkValid("fxe4", board);
  }

  @SuppressWarnings("static-method")
  @Test
  void testCapturingBlackNoPawnOnFromSquare() {
    // fxe3 with black pawn on f5 — needs pawn on f4, but f4 is empty
    final ApiBoard board = new Board("4k3/8/8/5p2/8/4P3/8/4K3 b - - 0 100");
    checkException("fxe3", board, SanValidationProblem.PAWN_FROM_SQUARE);
  }

  private static void checkValid(String san, ApiBoard board) {
    var isException = false;
    try {
      SanValidation.validateSan(san, board);
    } catch (@SuppressWarnings("unused") final SanValidationException e) {
      isException = true;
    }
    assertFalse(isException);
  }

  private static void checkException(String san, ApiBoard board, SanValidationProblem svp) {
    boolean isException;
    try {
      SanValidation.validateSan(san, board);
      isException = false;
    } catch (final SanValidationException e) {
      isException = true;
      assertEquals(svp, e.getSanValidationProblem());
    }
    assertTrue(isException);
  }

}
