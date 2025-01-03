package com.dlb.chess.test.pgn.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.pgn.reader.enums.PgnReaderStrictValidationProblem;
import com.dlb.chess.pgn.reader.exceptions.PgnReaderStrictValidationException;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.test.pgntest.PgnTestConstants;

class TestPgnReaderStrictSanException {
  private static final Path PGN_TEST_FOLDER_PATH = NonNullWrapperCommon
      .resolve(PgnTestConstants.PGN_READER_STRICT_TEST_ROOT_FOLDER_PATH, "exception/san");

  @SuppressWarnings("static-method")
  @Test
  void testException() {
    checkReadPgnException("01_initial_position.pgn", SanValidationProblem.PAWN_NON_PROMOTION_NO_LEGAL_MOVE);
    checkReadPgnException("02_initial_position.pgn", SanValidationProblem.PAWN_NON_PROMOTION_NO_LEGAL_MOVE);

    checkReadPgnException("03_custom_position_white_start.pgn", SanValidationProblem.CAPTURING_NO_PIECE);
    checkReadPgnException("04_custom_position_black_start.pgn",
        SanValidationProblem.INVALID_MOVEMENT_NON_PAWN_FROM_RANK);

  }

  private static void checkReadPgnException(String pgnFileName, SanValidationProblem expected) {
    var isException = false;
    try {
      PgnStrictCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH, pgnFileName);
    } catch (final PgnReaderStrictValidationException e) {
      isException = true;
      assertEquals(PgnReaderStrictValidationProblem.SAN, e.getPgnReaderStrictValidationProblem());
      assertEquals(expected, e.getSanValidationProblem());
    }
    assertTrue(isException);
  }
}
