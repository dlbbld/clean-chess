package com.dlb.chess.test.bitboard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.dlb.chess.bitboard.BitboardPosition;
import com.dlb.chess.bitboard.BitboardPositionUtility;
import com.dlb.chess.bitboard.PawnAttacks;
import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.squares.PawnDiagonalSquares;
import com.dlb.chess.test.model.PgnTestCase;
import com.dlb.chess.test.model.PgnTestCaseList;
import com.dlb.chess.test.pgn.setup.PgnTestCaseCatalog;
import com.dlb.chess.test.pgntest.enums.PgnTest;

/**
 * Differential test for {@link PawnAttacks}: per side, the precomputed bitboard table must agree with the existing
 * {@link PawnDiagonalSquares}-backed reference for every from-square. Both representations follow the same
 * pawns-only-on-rank-2-to-7 convention, so ranks 1 and 8 return empty sets / {@code 0L} on both sides.
 */
class TestPawnAttacks {

  @SuppressWarnings("static-method")
  @Test
  void directAgainstReferenceWhite() {
    for (final Square fromSquare : Square.REAL) {
      final Set<Square> bitboardAttacks = BitboardPositionUtility
          .toSquareSet(PawnAttacks.attacks(fromSquare, Side.WHITE));
      final Set<Square> referenceAttacks = PawnDiagonalSquares.getPawnDiagonalSquares(Side.WHITE, fromSquare);
      assertEquals(referenceAttacks, bitboardAttacks, "white pawn attacks from " + fromSquare.getName());
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void directAgainstReferenceBlack() {
    for (final Square fromSquare : Square.REAL) {
      final Set<Square> bitboardAttacks = BitboardPositionUtility
          .toSquareSet(PawnAttacks.attacks(fromSquare, Side.BLACK));
      final Set<Square> referenceAttacks = PawnDiagonalSquares.getPawnDiagonalSquares(Side.BLACK, fromSquare);
      assertEquals(referenceAttacks, bitboardAttacks, "black pawn attacks from " + fromSquare.getName());
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void corpusEveryPawnAgrees() {
    for (final PgnTest pgnTest : PgnTest.values()) {
      final PgnTestCaseList testCaseList = PgnTestCaseCatalog.getTestList(pgnTest);
      for (final PgnTestCase testCase : testCaseList.list()) {
        final StaticPosition staticPosition = testCase.finalPosition().getStaticPosition();
        final BitboardPosition bitboardPosition = BitboardPositionUtility.fromStaticPosition(staticPosition);
        assertSideAgrees(bitboardPosition.whitePawns(), Side.WHITE, testCase);
        assertSideAgrees(bitboardPosition.blackPawns(), Side.BLACK, testCase);
      }
    }
  }

  private static void assertSideAgrees(long pawns, Side side, PgnTestCase testCase) {
    long remaining = pawns;
    while (remaining != 0L) {
      final Square fromSquare = Square.REAL.get(Long.numberOfTrailingZeros(remaining));
      final Set<Square> bitboardAttacks = BitboardPositionUtility.toSquareSet(PawnAttacks.attacks(fromSquare, side));
      final Set<Square> referenceAttacks = PawnDiagonalSquares.getPawnDiagonalSquares(side, fromSquare);
      assertEquals(referenceAttacks, bitboardAttacks,
          side + " pawn attacks from " + fromSquare.getName() + " in fixture " + testCase.pgnName());
      remaining &= remaining - 1L;
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void noneSquareThrows() {
    assertThrows(IllegalArgumentException.class, () -> PawnAttacks.attacks(Square.NONE, Side.WHITE));
    assertThrows(IllegalArgumentException.class, () -> PawnAttacks.attacks(Square.NONE, Side.BLACK));
  }

  @SuppressWarnings("static-method")
  @Test
  void noneSideThrows() {
    assertThrows(IllegalArgumentException.class, () -> PawnAttacks.attacks(Square.A1, Side.NONE));
  }
}
