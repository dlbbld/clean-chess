package com.dlb.chess.test.san;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.san.validate.SanValidation;

public abstract class AbstractTestSanValidate implements EnumConstants {

  public static void checkExceptionMovingOntoOwnPiece(String san, ApiBoard board) {
    checkException(san, board, SanValidationProblem.MOVING_ONTO_OWN_PIECE);
  }

  public static void checkExceptionCapturingOwnPiece(String san, ApiBoard board) {
    checkException(san, board, SanValidationProblem.CAPTURING_OWN_PIECE);
  }

  static void checkExceptionFormat(String san, ApiBoard board) {
    checkException(san, board, SanValidationProblem.FORMAT);
  }

  public static void checkExceptionFormat(String san, SanValidationProblem problem, ApiBoard board) {
    checkException(san, board, problem);
  }

  private static void checkException(String san, ApiBoard board, SanValidationProblem problem) {
    boolean isException;
    try {
      SanValidation.validateSan(san, board);
      isException = false;
    } catch (final SanValidationException e) {
      isException = true;
      assertEquals(problem, e.getSanValidationProblem());
    }
    assertTrue(isException);
  }
}
