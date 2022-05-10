package com.dlb.chess.test.san;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.san.SanValidateFormat;
import com.dlb.chess.san.SanValidateMove;
import com.dlb.chess.san.SanValidation;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.san.model.SanParse;

public abstract class AbstractTestSanValidate implements EnumConstants {

  static void checkValid(Side havingMove, String san) {
    var isException = false;
    try {
      final SanParse sanParse = SanValidateFormat.validateFormat(san);
      SanValidateMove.validateMovement(havingMove, sanParse);
    } catch (@SuppressWarnings("unused") final SanValidationException e) {
      isException = true;
    }
    assertFalse(isException);
  }

  static void checkExceptionMovingOntoItself(String san, ApiBoard board) {
    checkException(san, board, SanValidationProblem.MOVING_ONTO_ITSELF);
  }

  static void checkExceptionFormat(String san, ApiBoard board) {
    checkException(san, board, SanValidationProblem.FORMAT);
  }

  static void checkExceptionNonPawnFromFile(String san, ApiBoard board) {
    checkException(san, board, SanValidationProblem.INVALID_MOVEMENT_NON_PAWN_FROM_FILE);
  }

  static void checkExceptionNonPawnFromRank(String san, ApiBoard board) {
    checkException(san, board, SanValidationProblem.INVALID_MOVEMENT_NON_PAWN_FROM_RANK);
  }

  static void checkExceptionNonPawnFromSquare(String san, ApiBoard board) {
    checkException(san, board, SanValidationProblem.INVALID_MOVEMENT_NON_PAWN_FROM_SQUARE);
  }

  static void checkExceptionPawnToRank(String san, ApiBoard board) {
    checkException(san, board, SanValidationProblem.INVALID_MOVEMENT_PAWN_TO_RANK);
  }

  static void checkExceptionPawnFromFile(String san, ApiBoard board) {
    checkException(san, board, SanValidationProblem.INVALID_MOVEMENT_PAWN_FROM_FILE);
  }

  static void checkExceptionPromotionRank(String san, ApiBoard board) {
    checkException(san, board, SanValidationProblem.INVALID_PROMOTION_RANK_PAWN);
  }

  static void checkExceptionPromotionPiece(String san, ApiBoard board) {
    checkException(san, board, SanValidationProblem.INVALID_PROMOTION_NO_PROMOTION_PIECE);
  }

  private static void checkException(String san, ApiBoard board, SanValidationProblem problem) {
    boolean isException;
    try {
      SanValidation.calculateMoveSpecificationForSan(san, board);
      isException = false;
    } catch (final SanValidationException e) {
      isException = true;
      assertEquals(problem, e.getSanValidationProblem());
    }
    assertTrue(isException);
  }
}
