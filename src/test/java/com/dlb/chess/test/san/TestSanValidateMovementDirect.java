package com.dlb.chess.test.san;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.san.SanValidation;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;

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

class TestSanValidateMovementDirect extends AbstractTestSanValidate {

  @SuppressWarnings("static-method")
  @Test
  void testNonPawnValid() {
    testNonPawnValid(WHITE);
    testNonPawnValid(BLACK);
  }

  private static void testNonPawnValid(Side havingMove) {
    checkValid(havingMove, "Re5");
    checkValid(havingMove, "Rae5");
    checkValid(havingMove, "R2e5");
    checkValid(havingMove, "Rxe5");
    checkValid(havingMove, "Raxe5");
    checkValid(havingMove, "R2xe5");

    checkValid(havingMove, "Ne5");
    checkValid(havingMove, "Nce5");
    checkValid(havingMove, "N4e5");
    checkValid(havingMove, "Nd3e5");
    checkValid(havingMove, "Nxe5");
    checkValid(havingMove, "Ncxe5");
    checkValid(havingMove, "N4xe5");
    checkValid(havingMove, "Nd3xe5");

    checkValid(havingMove, "Be5");
    checkValid(havingMove, "Bbe5");
    checkValid(havingMove, "B2e5");
    checkValid(havingMove, "Bxe5");
    checkValid(havingMove, "Bbxe5");
    checkValid(havingMove, "B2xe5");

    checkValid(havingMove, "Qe5");
    checkValid(havingMove, "Qae5");
    checkValid(havingMove, "Q2e5");
    checkValid(havingMove, "Qc3e5");
    checkValid(havingMove, "Qxe5");
    checkValid(havingMove, "Qaxe5");
    checkValid(havingMove, "Q2xe5");
    checkValid(havingMove, "Qc3xe5");

    checkValid(havingMove, "Ke5");
    checkValid(havingMove, "Kxe5");
    checkValid(havingMove, "O-O");
    checkValid(havingMove, "O-O-O");
  }

  @SuppressWarnings("static-method")
  @Test
  void testNonPawnException() {
    testNonPawnException(WHITE);
    testNonPawnException(BLACK);
  }

  private static void testNonPawnException(Side havingMove) {

    checkExceptionNonPawnFromFile(havingMove, "Nbe5");
    checkExceptionNonPawnFromRank(havingMove, "N2e5");
    checkExceptionNonPawnFromSquare(havingMove, "Nb2e5");
    checkExceptionNonPawnFromFile(havingMove, "Nbxe5");
    checkExceptionNonPawnFromRank(havingMove, "N2xe5");
    checkExceptionNonPawnFromSquare(havingMove, "Nb2xe5");

    checkExceptionNonPawnFromFile(havingMove, "Bch5");
    checkExceptionNonPawnFromRank(havingMove, "B3d8");
    checkExceptionNonPawnFromFile(havingMove, "Bcxh5");
    checkExceptionNonPawnFromRank(havingMove, "B3xd8");

    checkExceptionNonPawnFromSquare(havingMove, "Qc4e5");
    checkExceptionNonPawnFromSquare(havingMove, "Qc4xe5");
  }

  @SuppressWarnings("static-method")
  @Test
  void testPawnWhite() {

    checkExceptionPawnToRank(WHITE, "d1");
    checkExceptionPawnToRank(WHITE, "d2");
    checkValid(WHITE, "d3");
    checkValid(WHITE, "d4");
    checkValid(WHITE, "d5");
    checkValid(WHITE, "d6");
    checkValid(WHITE, "d7");
    checkExceptionPromotionPiece(WHITE, "d8");

    checkExceptionPawnToRank(WHITE, "cxd1");
    checkExceptionPawnToRank(WHITE, "cxd2");
    checkValid(WHITE, "cxd3");
    checkValid(WHITE, "cxd4");
    checkValid(WHITE, "cxd5");
    checkValid(WHITE, "cxd6");
    checkValid(WHITE, "cxd7");
    checkExceptionPromotionPiece(WHITE, "cxd8");

    checkExceptionPawnToRank(WHITE, "exd1");
    checkExceptionPawnToRank(WHITE, "exd2");
    checkValid(WHITE, "exd3");
    checkValid(WHITE, "exd4");
    checkValid(WHITE, "exd5");
    checkValid(WHITE, "exd6");
    checkValid(WHITE, "exd7");
    checkExceptionPromotionPiece(WHITE, "exd8");

    checkExceptionPawnToRank(WHITE, "d1=Q");
    checkExceptionPawnToRank(WHITE, "d2=Q");
    checkExceptionPromotionRank(WHITE, "d3=Q");
    checkExceptionPromotionRank(WHITE, "d4=Q");
    checkExceptionPromotionRank(WHITE, "d5=Q");
    checkExceptionPromotionRank(WHITE, "d6=Q");
    checkExceptionPromotionRank(WHITE, "d7=Q");
    checkValid(WHITE, "d8=R");
    checkValid(WHITE, "d8=N");
    checkValid(WHITE, "d8=B");
    checkValid(WHITE, "d8=Q");

    checkExceptionPawnToRank(WHITE, "cxd1=Q");
    checkExceptionPawnToRank(WHITE, "cxd2=Q");
    checkExceptionPromotionRank(WHITE, "cxd3=Q");
    checkExceptionPromotionRank(WHITE, "cxd4=Q");
    checkExceptionPromotionRank(WHITE, "cxd5=Q");
    checkExceptionPromotionRank(WHITE, "cxd6=Q");
    checkExceptionPromotionRank(WHITE, "cxd7=Q");
    checkValid(WHITE, "cxd8=R");
    checkValid(WHITE, "cxd8=N");
    checkValid(WHITE, "cxd8=B");
    checkValid(WHITE, "cxd8=Q");

    checkExceptionPawnToRank(WHITE, "exd1=Q");
    checkExceptionPawnToRank(WHITE, "exd2=Q");
    checkExceptionPromotionRank(WHITE, "exd3=Q");
    checkExceptionPromotionRank(WHITE, "exd4=Q");
    checkExceptionPromotionRank(WHITE, "exd5=Q");
    checkExceptionPromotionRank(WHITE, "exd6=Q");
    checkExceptionPromotionRank(WHITE, "exd7=Q");
    checkValid(WHITE, "exd8=R");
    checkValid(WHITE, "exd8=N");
    checkValid(WHITE, "exd8=B");
    checkValid(WHITE, "exd8=Q");

  }

  @SuppressWarnings("static-method")
  @Test
  void testPawnBlack() {

    checkExceptionPawnToRank(BLACK, "d8");
    checkExceptionPawnToRank(BLACK, "d7");
    checkValid(BLACK, "d6");
    checkValid(BLACK, "d5");
    checkValid(BLACK, "d4");
    checkValid(BLACK, "d3");
    checkValid(BLACK, "d2");
    checkExceptionPromotionPiece(BLACK, "d1");

    checkExceptionPawnToRank(BLACK, "cxd8");
    checkExceptionPawnToRank(BLACK, "cxd7");
    checkValid(BLACK, "cxd6");
    checkValid(BLACK, "cxd5");
    checkValid(BLACK, "cxd4");
    checkValid(BLACK, "cxd3");
    checkValid(BLACK, "cxd2");
    checkExceptionPromotionPiece(BLACK, "cxd1");

    checkExceptionPawnToRank(BLACK, "exd8");
    checkExceptionPawnToRank(BLACK, "exd7");
    checkValid(BLACK, "exd6");
    checkValid(BLACK, "exd5");
    checkValid(BLACK, "exd4");
    checkValid(BLACK, "exd3");
    checkValid(BLACK, "exd2");
    checkExceptionPromotionPiece(BLACK, "exd1");

    checkExceptionPawnToRank(BLACK, "d8=Q");
    checkExceptionPawnToRank(BLACK, "d7=Q");
    checkExceptionPromotionRank(BLACK, "d6=Q");
    checkExceptionPromotionRank(BLACK, "d5=Q");
    checkExceptionPromotionRank(BLACK, "d4=Q");
    checkExceptionPromotionRank(BLACK, "d3=Q");
    checkExceptionPromotionRank(BLACK, "d2=Q");
    checkValid(BLACK, "d1=R");
    checkValid(BLACK, "d1=N");
    checkValid(BLACK, "d1=B");
    checkValid(BLACK, "d1=Q");

    checkExceptionPawnToRank(BLACK, "cxd8=Q");
    checkExceptionPawnToRank(BLACK, "cxd7=Q");
    checkExceptionPromotionRank(BLACK, "cxd6=Q");
    checkExceptionPromotionRank(BLACK, "cxd5=Q");
    checkExceptionPromotionRank(BLACK, "cxd4=Q");
    checkExceptionPromotionRank(BLACK, "cxd3=Q");
    checkExceptionPromotionRank(BLACK, "cxd2=Q");
    checkValid(BLACK, "cxd1=R");
    checkValid(BLACK, "cxd1=N");
    checkValid(BLACK, "cxd1=B");
    checkValid(BLACK, "cxd1=Q");

    checkExceptionPawnToRank(BLACK, "exd8=Q");
    checkExceptionPawnToRank(BLACK, "exd7=Q");
    checkExceptionPromotionRank(BLACK, "exd6=Q");
    checkExceptionPromotionRank(BLACK, "exd5=Q");
    checkExceptionPromotionRank(BLACK, "exd4=Q");
    checkExceptionPromotionRank(BLACK, "exd3=Q");
    checkExceptionPromotionRank(BLACK, "exd2=Q");
    checkValid(BLACK, "exd1=R");
    checkValid(BLACK, "exd1=N");
    checkValid(BLACK, "exd1=B");
    checkValid(BLACK, "exd1=Q");
  }

  static void checkExceptionMovingOntoItself(Side havingMove, String san) {
    checkException(havingMove, san, SanValidationProblem.MOVING_ONTO_ITSELF);
  }

  static void checkExceptionFormat(Side havingMove, String san) {
    checkException(havingMove, san, SanValidationProblem.FORMAT);
  }

  static void checkExceptionNonPawnFromFile(Side havingMove, String san) {
    checkException(havingMove, san, SanValidationProblem.INVALID_MOVEMENT_NON_PAWN_FROM_FILE);
  }

  static void checkExceptionNonPawnFromRank(Side havingMove, String san) {
    checkException(havingMove, san, SanValidationProblem.INVALID_MOVEMENT_NON_PAWN_FROM_RANK);
  }

  static void checkExceptionNonPawnFromSquare(Side havingMove, String san) {
    checkException(havingMove, san, SanValidationProblem.INVALID_MOVEMENT_NON_PAWN_FROM_SQUARE);
  }

  static void checkExceptionPawnToRank(Side havingMove, String san) {
    checkException(havingMove, san, SanValidationProblem.INVALID_MOVEMENT_PAWN_TO_RANK);
  }

  static void checkExceptionPawnFromFile(Side havingMove, String san) {
    checkException(havingMove, san, SanValidationProblem.INVALID_MOVEMENT_PAWN_FROM_FILE);
  }

  static void checkExceptionPromotionRank(Side havingMove, String san) {
    checkException(havingMove, san, SanValidationProblem.INVALID_PROMOTION_RANK_PAWN);
  }

  static void checkExceptionPromotionPiece(Side havingMove, String san) {
    checkException(havingMove, san, SanValidationProblem.INVALID_PROMOTION_NO_PROMOTION_PIECE);
  }

  private static void checkException(Side havingMove, String san, SanValidationProblem problem) {
    boolean isException;
    try {
      SanValidation.validateNonPositionRelatedExtended(san, havingMove);
      isException = false;
    } catch (final SanValidationException e) {
      isException = true;
      assertEquals(problem, e.getSanValidationProblem());
    }
    assertTrue(isException);
  }

}