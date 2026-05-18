package com.dlb.chess.test.bitboard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;

import com.dlb.chess.bitboard.BitboardPosition;
import com.dlb.chess.bitboard.BitboardPositionUtility;
import com.dlb.chess.board.Board;
import com.dlb.chess.board.StaticPosition;
import com.dlb.chess.board.enums.CastlingMove;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.test.model.PgnTestCase;
import com.dlb.chess.test.model.PgnTestCaseList;
import com.dlb.chess.test.pgn.setup.PgnTestCaseCatalog;
import com.dlb.chess.test.pgntest.enums.PgnTest;

/**
 * The spine assertion of the bitboard release: for every fixture in the corpus, the bitboard's
 * {@link BitboardPosition#legalMoves(Side, long)} (non-castling only) must agree set-equal with
 * {@code Board.getLegalMoves()} after filtering out castling moves from the reference. Every piece type,
 * pin filtering, check evasion, double check, EP including the rank-pin edge case, and promotion expansion are
 * exercised together.
 *
 * <p>
 * Castling moves are excluded because they live on {@link Board} with the castling-rights state; the bitboard layer
 * is intentionally castling-stateless.
 */
class TestBitboardPositionLegalMoves {

  @SuppressWarnings("static-method")
  @Test
  void corpusLegalMovesAgreeForSideToMove() {
    for (final PgnTest pgnTest : PgnTest.values()) {
      final PgnTestCaseList testCaseList = PgnTestCaseCatalog.getTestList(pgnTest);
      for (final PgnTestCase testCase : testCaseList.list()) {
        final Board board = testCase.finalPosition();
        final StaticPosition staticPosition = board.getStaticPosition();
        final BitboardPosition bitboardPosition = BitboardPositionUtility.fromStaticPosition(staticPosition);
        final Side havingMove = board.getHavingMove();
        final Square boardEpTarget = board.getEnPassantCaptureTargetSquare();
        final long enPassantBit = boardEpTarget == Square.NONE ? 0L : 1L << boardEpTarget.ordinal();

        final Set<MoveSpecification> bitboardMoves = bitboardPosition.legalMoves(havingMove, enPassantBit);
        final Set<MoveSpecification> referenceNonCastlingMoves = referenceNonCastlingMoves(board);

        assertEquals(referenceNonCastlingMoves, bitboardMoves,
            "legalMoves disagreement for " + havingMove + " in fixture " + testCase.pgnName());
      }
    }
  }

  private static Set<MoveSpecification> referenceNonCastlingMoves(Board board) {
    final Set<MoveSpecification> result = new TreeSet<>();
    for (final LegalMove legalMove : board.getLegalMoves()) {
      final MoveSpecification spec = legalMove.moveSpecification();
      if (spec.castlingMove() == CastlingMove.NONE) {
        result.add(spec);
      }
    }
    return result;
  }

  @SuppressWarnings("static-method")
  @Test
  void initialPositionHasTwentyMoves() {
    // Initial position has exactly 20 legal moves for white: 16 pawn moves (8 × single+double push) + 4 knight moves.
    final Set<MoveSpecification> whiteMoves = BitboardPosition.INITIAL_POSITION.legalMoves(Side.WHITE, 0L);
    assertEquals(20, whiteMoves.size(), "white legal moves from initial position");
    final Set<MoveSpecification> blackMoves = BitboardPosition.INITIAL_POSITION.legalMoves(Side.BLACK, 0L);
    assertEquals(20, blackMoves.size(), "black legal moves from initial position");
  }

  @SuppressWarnings("static-method")
  @Test
  void emptyPositionHasNoMoves() {
    assertEquals(Set.of(), BitboardPosition.EMPTY_POSITION.legalMoves(Side.WHITE, 0L));
    assertEquals(Set.of(), BitboardPosition.EMPTY_POSITION.legalMoves(Side.BLACK, 0L));
  }

  @SuppressWarnings("static-method")
  @Test
  void noneSideThrows() {
    assertThrows(IllegalArgumentException.class,
        () -> BitboardPosition.INITIAL_POSITION.legalMoves(Side.NONE, 0L));
  }
}
