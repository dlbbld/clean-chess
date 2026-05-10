package com.dlb.chess.test.san;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.interfaces.ChessBoard;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.san.exceptions.SanValidationException;
import com.dlb.chess.san.validate.StrictSanParser;

public abstract class AbstractTestSanValidate implements EnumConstants {

  public static void checkExceptionNonMovement(String san, ChessBoard board) {
    checkException(san, board, SanValidationProblem.NON_MOVEMENT_RNBQ_SOURCE_SQUARE_EQUALS_DESTINATION_SQUARE);
  }

  public static void checkExceptionRnbqkMovingOntoOwnPiece(String san, ChessBoard board) {
    checkException(san, board, SanValidationProblem.DESTINATION_RNBQK_OWN_PIECE_NON_CAPTURING);
  }

  public static void checkExceptionRnbqkCapturingOwnPiece(String san, ChessBoard board) {
    checkException(san, board, SanValidationProblem.DESTINATION_RNBQK_OWN_PIECE_CAPTURING);
  }

  public static void checkExceptionPawnForwardOwnPiece(String san, ChessBoard board) {
    checkException(san, board, SanValidationProblem.DESTINATION_PAWN_FORWARD_OWN_PIECE);
  }

  public static void checkExceptionPawnCaptureOwnPiece(String san, ChessBoard board) {
    checkException(san, board, SanValidationProblem.DESTINATION_PAWN_CAPTURE_OWN_PIECE);
  }

  public static void checkExceptionFormat(String san, SanValidationProblem problem, ChessBoard board) {
    checkException(san, board, problem);
  }

  private static void checkException(String san, ChessBoard board, SanValidationProblem problem) {
    boolean isException;
    try {
      StrictSanParser.parseText(san, board);
      isException = false;
    } catch (final SanValidationException e) {
      isException = true;
      assertEquals(problem, e.getSanValidationProblem());
    }
    assertTrue(isException);
  }
}
