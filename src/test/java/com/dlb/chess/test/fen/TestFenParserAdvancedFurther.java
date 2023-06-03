package com.dlb.chess.test.fen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.enums.FenAdvancedFurtherValidationProblem;
import com.dlb.chess.common.exceptions.FenAdvancedFurtherValidationException;
import com.dlb.chess.fen.FenParserAdvanced;
import com.dlb.chess.fen.FenParserAdvancedFurther;
import com.dlb.chess.fen.constants.FenConstants;
import com.dlb.chess.fen.model.Fen;

class TestFenParserAdvancedFurther implements EnumConstants {

  @SuppressWarnings("static-method")
  @Test
  void testParseFenExceptionHalfMoveClock() {

    checkParseFenException("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 3 2",
        FenAdvancedFurtherValidationProblem.INVALID_HALF_MOVE_CLOCK_TOO_BIG_RELATIVE_TO_FULL_MOVE_NUMBER);

    checkParseFenException("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 4 2",
        FenAdvancedFurtherValidationProblem.INVALID_HALF_MOVE_CLOCK_TOO_BIG_RELATIVE_TO_FULL_MOVE_NUMBER);

    checkParseFenException("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 100 10",
        FenAdvancedFurtherValidationProblem.INVALID_HALF_MOVE_CLOCK_TOO_BIG_RELATIVE_TO_FULL_MOVE_NUMBER);

    checkParseFenException("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 20 10",
        FenAdvancedFurtherValidationProblem.INVALID_HALF_MOVE_CLOCK_TOO_BIG_RELATIVE_TO_FULL_MOVE_NUMBER);

    checkParseFenException("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 22 11",
        FenAdvancedFurtherValidationProblem.INVALID_HALF_MOVE_CLOCK_TOO_BIG_RELATIVE_TO_FULL_MOVE_NUMBER);

    final var whiteHavingMoveMaxHalfMoveClock = 2 * (FenConstants.MAX_FULL_MOVE_NUMBER - 1);
    final var blackHavingMoveMaxHalfMoveClock = 2 * (FenConstants.MAX_FULL_MOVE_NUMBER - 1) + 1;

    // max values exceeded by one
    // max full move number - half-move clock one above possible value - white having the move
    checkParseFenException(
        "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - " + (whiteHavingMoveMaxHalfMoveClock + 1) + " "
            + FenConstants.MAX_FULL_MOVE_NUMBER,
        FenAdvancedFurtherValidationProblem.INVALID_HALF_MOVE_CLOCK_TOO_BIG_RELATIVE_TO_FULL_MOVE_NUMBER);

    // max full move number - half-move clock one above possible value - black having the move
    checkParseFenException(
        "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - " + (blackHavingMoveMaxHalfMoveClock + 1) + " "
            + FenConstants.MAX_FULL_MOVE_NUMBER,
        FenAdvancedFurtherValidationProblem.INVALID_HALF_MOVE_CLOCK_TOO_BIG_RELATIVE_TO_FULL_MOVE_NUMBER);
  }

  @SuppressWarnings("static-method")
  @Test
  void testParseFenExceptionFullMoveNumber() {

    checkParseFenException("8/8/6K1/7B/7N/7k/8/2q5 w - - 0 1",
        FenAdvancedFurtherValidationProblem.INVALID_FULL_MOVE_NUMBER_ONE_WHITE_IN_NON_STARTING_POSITION);

    // starting position, but castling rights not ok
    checkParseFenException("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQk - 0 1",
        FenAdvancedFurtherValidationProblem.INVALID_FULL_MOVE_NUMBER_ONE_WHITE_IN_NON_STARTING_POSITION);
    checkParseFenException("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQ - 0 1",
        FenAdvancedFurtherValidationProblem.INVALID_FULL_MOVE_NUMBER_ONE_WHITE_IN_NON_STARTING_POSITION);
    checkParseFenException("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w K - 0 1",
        FenAdvancedFurtherValidationProblem.INVALID_FULL_MOVE_NUMBER_ONE_WHITE_IN_NON_STARTING_POSITION);

    checkParseFenException("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b - - 0 1",
        FenAdvancedFurtherValidationProblem.INVALID_FULL_MOVE_NUMBER_ONE_BLACK_IN_NON_POSSIBLE_POSITION);

    checkParseFenException("8/8/6K1/7B/7N/7k/8/2q5 b - - 0 1",
        FenAdvancedFurtherValidationProblem.INVALID_FULL_MOVE_NUMBER_ONE_BLACK_IN_NON_POSSIBLE_POSITION);

  }

  private static void checkParseFenException(String fenStr, FenAdvancedFurtherValidationProblem expected) {
    final Fen fen = FenParserAdvanced.parseFenAdvanced(fenStr);
    var actual = FenAdvancedFurtherValidationProblem.SUCCESS;
    try {
      FenParserAdvancedFurther.parseFenAdvancedFurther(fen);
    } catch (final FenAdvancedFurtherValidationException e) {
      actual = e.getFenAdvancedFurtherValidationProblem();
    }
    assertEquals(expected, actual);
  }

}
