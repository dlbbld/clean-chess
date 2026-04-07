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
      SanValidateMove.validateMovement(sanParse, havingMove);
    } catch (@SuppressWarnings("unused") final SanValidationException e) {
      isException = true;
    }
    assertFalse(isException);
  }

  static void checkExceptionMovingOntoItself(String san, ApiBoard board) {
    checkException(san, board, SanValidationProblem.PIECE_SQUARE_MOVING_ONTO_ITSELF);
  }

  static void checkExceptionFormat(String san, ApiBoard board) {
    checkException(san, board, SanValidationProblem.FORMAT);
  }

  static void checkExceptionFormat(String san, SanValidationProblem problem, ApiBoard board) {
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
