package com.dlb.chess.test.san;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.san.SanValidation;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;

class TestSanValidatePieceExists {

  @SuppressWarnings("static-method")
  @Test
  void testWhiteNeither() {

    {
      final ApiBoard board = new Board("k7/1q6/8/8/8/8/6Q1/7K w - - 0 100");

      checkExceptionNeither("Ra1", board);
      checkExceptionNeither("Nb1", board);
      checkExceptionNeither("Bc1", board);
    }

    {
      final ApiBoard board = new Board("k7/1r6/8/8/8/8/6R1/7K w - - 0 100");

      checkExceptionNeither("Qd1", board);
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testBlackNeither() {

    {
      final ApiBoard board = new Board("k7/1q6/8/8/8/8/6Q1/7K b - - 0 100");

      checkExceptionNeither("Rh8", board);
      checkExceptionNeither("Ng8", board);
      checkExceptionNeither("Bf8", board);
    }

    {
      final ApiBoard board = new Board("k7/1r6/8/8/8/8/6R1/7K b - - 0 100");

      checkExceptionNeither("Qd8", board);
    }
  }

  private static void checkExceptionNeither(String san, ApiBoard board) {
    checkException(san, board, SanValidationProblem.PIECE_NEITHER_NO_PIECE_EXISTS);
  }

  @SuppressWarnings("static-method")
  @Test
  void testWhiteFile() {

    {
      final ApiBoard board = new Board("k7/rq6/n7/b7/7B/7N/6QR/7K w - - 0 100");

      checkExceptionFile("Raa1", board);
      checkExceptionFile("Nab1", board);

      // TODO below gets illegal movement but should give the no piece exists in future
      // piece check on board before movement check!!!
      // checkExceptionFile("Bcc1", board);

      checkExceptionFile("Bac1", board);
      checkExceptionFile("Qad1", board);
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testBlackFile() {

    {
      final ApiBoard board = new Board("k7/rq6/n7/b7/7B/7N/6QR/7K b - - 0 100");

      checkExceptionFile("Rhd8", board);

      // TODO below gets illegal movement but should give the no piece exists in future
      // piece check on board before movement check!!!
      // checkExceptionFile("Nbg8", board);

      checkExceptionFile("Nhg8", board);
      checkExceptionFile("Bhf8", board);
      checkExceptionFile("Qhf8", board);
    }

  }

  private static void checkExceptionFile(String san, ApiBoard board) {
    checkException(san, board, SanValidationProblem.PIECE_FILE_NO_PIECE_EXISTS);
  }

  @SuppressWarnings("static-method")
  @Test
  void testWhiteRank() {

    {
      final ApiBoard board = new Board("k7/rq6/n7/b7/7B/7N/6QR/7K w - - 0 100");

      checkExceptionRank("R1a1", board);

      // TODO below gets illegal movement but should give the no piece exists in future
      // piece check on board before movement check!!!
      // checkExceptionRank("N1b1", board);

      checkExceptionRank("N2b1", board);
      checkExceptionRank("B3c1", board);
      checkExceptionRank("Q4d1", board);
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testBlackRank() {

    {
      final ApiBoard board = new Board("k7/rq6/n7/b7/7B/7N/6QR/7K b - - 0 100");

      checkExceptionRank("R8d8", board);
      checkExceptionRank("N7g8", board);

      // TODO below gets illegal movement but should give the no piece exists in future
      // piece check on board before movement check!!!
      // checkExceptionRank("B8f8", board);

      checkExceptionRank("B6f8", board);
      checkExceptionRank("Q8f8", board);
    }

  }

  private static void checkExceptionRank(String san, ApiBoard board) {
    checkException(san, board, SanValidationProblem.PIECE_RANK_NO_PIECE_EXISTS);
  }

  @SuppressWarnings("static-method")
  @Test
  void testWhiteSquare() {

    {
      final ApiBoard board = new Board("k7/rq6/n7/b7/7B/7N/6QR/7K w - - 0 100");

      checkExceptionSquare("Rb1a1", board);
      checkExceptionSquare("Na3b1", board);
      checkExceptionSquare("Ba3c1", board);
      checkExceptionSquare("Qc4b3", board);
    }
  }

  @SuppressWarnings("static-method")
  @Test
  void testBlackSquare() {

    {
      final ApiBoard board = new Board("k7/rq6/n7/b7/7B/7N/6QR/7K b - - 0 100");

      checkExceptionSquare("Rf8e8", board);
      checkExceptionSquare("Nh6g8", board);
      checkExceptionSquare("Bf8h6", board);
      checkExceptionSquare("Qd8e7", board);
    }

  }

  private static void checkExceptionSquare(String san, ApiBoard board) {
    checkException(san, board, SanValidationProblem.PIECE_SQUARE_NO_PIECE_EXISTS);
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