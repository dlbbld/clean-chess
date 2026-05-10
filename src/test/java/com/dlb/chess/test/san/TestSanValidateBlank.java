package com.dlb.chess.test.san;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.interfaces.ChessBoard;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.san.validate.StrictSanParser;

class TestSanValidateBlank {

  @SuppressWarnings("static-method")
  @Test
  void test() {

    final ChessBoard board = new Board();

    checkException("", board);
    checkException(" ", board);

    board.moveStrict("e4");

    checkException("", board);
    checkException(" ", board);

    board.moveStrict("e5");

    checkException("", board);
    checkException(" ", board);

  }

  private static void checkException(String san, ChessBoard board) {
    boolean isException;
    try {
      StrictSanParser.parseText(san, board);
      isException = false;
    } catch (final SanValidationException e) {
      isException = true;
      assertEquals(SanValidationProblem.FORMAT_BLANK, e.getSanValidationProblem());
    }
    assertTrue(isException);
  }

}