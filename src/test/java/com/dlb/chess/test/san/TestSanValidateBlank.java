package com.dlb.chess.test.san;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.san.validate.SanValidation;

class TestSanValidateBlank {

  @SuppressWarnings("static-method")
  @Test
  void test() {

    final ApiBoard board = new Board();

    checkException("", board);
    checkException(" ", board);

    board.performMove("e4");

    checkException("", board);
    checkException(" ", board);

    board.performMove("e5");

    checkException("", board);
    checkException(" ", board);

  }

  private static void checkException(String san, ApiBoard board) {
    boolean isException;
    try {
      SanValidation.validateSan(san, board);
      isException = false;
    } catch (final SanValidationException e) {
      isException = true;
      assertEquals(SanValidationProblem.BLANK, e.getSanValidationProblem());
    }
    assertTrue(isException);
  }

}