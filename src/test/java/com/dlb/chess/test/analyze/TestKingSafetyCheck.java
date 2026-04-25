package com.dlb.chess.test.analyze;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.EnumSet;
import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import com.dlb.chess.analyze.ChessRuleAnalyzer;
import com.dlb.chess.board.Board;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.enums.KingSafetyCheck;

/**
 * Per-{@link KingSafetyCheck}-value tests for {@link ChessRuleAnalyzer#analyzeKingSafety}.
 * Each enum value (including {@code SUCCESS}) has at least one test method exercising it. An
 * {@code @AfterAll} hook verifies the entire enum is covered — adding a new value without a test
 * fails the build.
 */
class TestKingSafetyCheck implements EnumConstants {

  private static final Set<KingSafetyCheck> COVERED = new TreeSet<>();

  private static KingSafetyCheck analyze(ApiBoard board, MoveSpecification move) {
    final KingSafetyCheck result = ChessRuleAnalyzer.analyzeKingSafety(board.getStaticPosition(), board.getHavingMove(),
        board.getLegalMoveSet(), move);
    COVERED.add(result);
    return result;
  }

  private static void check(String fen, MoveSpecification move, KingSafetyCheck expected) {
    final ApiBoard board = new Board(fen);
    assertEquals(expected, analyze(board, move));
  }

  @SuppressWarnings("static-method")
  @Test
  void testSuccess() {
    // initial position: e2-e4 leaves king safe
    final ApiBoard board = new Board();
    assertEquals(KingSafetyCheck.SUCCESS, analyze(board, new MoveSpecification(E2, E4)));
  }

  @SuppressWarnings("static-method")
  @Test
  void testNonKingLeftInCheck() {
    // white king e1 in check from black rook e8; white knight c4 moves to d6 — doesn't escape check
    check("4r2k/8/8/8/2N5/8/8/4K3 w - - 0 1", new MoveSpecification(C4, D6),
        KingSafetyCheck.NON_KING_LEFT_IN_CHECK);
  }

  @SuppressWarnings("static-method")
  @Test
  void testNonKingExposedToCheck() {
    // pinned white knight on e2 between own king e1 and black rook e8; moving it exposes the king
    check("4r2k/8/8/8/8/8/4N3/4K3 w - - 0 1", new MoveSpecification(E2, C3),
        KingSafetyCheck.NON_KING_EXPOSED_TO_CHECK);
  }

  @SuppressWarnings("static-method")
  @Test
  void testKingExposedToCheck() {
    // king not in check; moves to f1, attacked by black rook on f8
    check("4kr2/8/8/8/8/8/8/4K3 w - - 0 1", new MoveSpecification(E1, F1),
        KingSafetyCheck.KING_EXPOSED_TO_CHECK);
  }

  @SuppressWarnings("static-method")
  @Test
  void testKingLeftInCheckLegalMoves() {
    // king on e1 in check from black rook on e8; king moves to e2 still on e-file → still in check;
    // other king escape moves exist (d1, d2, f1, f2)
    check("4r2k/8/8/8/8/8/8/4K3 w - - 0 1", new MoveSpecification(E1, E2),
        KingSafetyCheck.KING_LEFT_IN_CHECK_LEGAL_MOVES);
  }

  @SuppressWarnings("static-method")
  @Test
  void testKingLeftInCheckNoLegalMoves() {
    // white king a1 in check from black rook a8; black rook b8 covers b-file. Adjacent squares
    // a2, b1, b2 are all attacked → no legal king escape
    check("rr5k/8/8/8/8/8/8/K7 w - - 0 1", new MoveSpecification(A1, A2),
        KingSafetyCheck.KING_LEFT_IN_CHECK_NO_LEGAL_MOVES);
  }

  @AfterAll
  static void verifyExhaustive() {
    final Set<KingSafetyCheck> missing = EnumSet.allOf(KingSafetyCheck.class);
    missing.removeAll(COVERED);
    if (!missing.isEmpty()) {
      throw new AssertionError("KingSafetyCheck values not covered by tests: " + missing);
    }
  }
}
