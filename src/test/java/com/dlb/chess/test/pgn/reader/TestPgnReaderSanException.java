package com.dlb.chess.test.pgn.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import com.dlb.chess.pgn.reader.enums.PgnReaderValidationProblem;
import com.dlb.chess.pgn.reader.exceptions.PgnReaderValidationException;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.test.common.utility.AbstractTestMovetextUtility;
import com.dlb.chess.test.pgntest.PgnTestConstants;

class TestPgnReaderSanException extends AbstractTestMovetextUtility {

  private static final Path PGN_TEST_FOLDER_PATH = PgnTestConstants.PGN_READER_NON_STRICT_TEST_ROOT_FOLDER_PATH
      .resolve("exception/san");

  @SuppressWarnings("static-method")
  @Test
  void testException() {
    checkReadPgnException("01_initial_position.pgn", SanValidationProblem.MOVING_ONTO_ONE_PIECE);
    checkReadPgnException("02_initial_position.pgn", SanValidationProblem.PIECE_NEITHER_NO_LEGAL_MOVE);

    checkReadPgnException("03_custom_position_white_start.pgn", SanValidationProblem.PIECE_NEITHER_NO_LEGAL_MOVE);
    checkReadPgnException("04_custom_position_black_start.pgn", SanValidationProblem.PIECE_NEITHER_NO_LEGAL_MOVE);

  }

  private static void checkReadPgnException(String pgnFileName, SanValidationProblem expected) {
    var isException = false;
    try {
      PgnCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH, pgnFileName);
    } catch (final PgnReaderValidationException e) {
      isException = true;
      assertEquals(PgnReaderValidationProblem.SAN, e.getPgnReaderValidationProblem());
      assertEquals(expected, e.getSanValidationProblem());
    }
    assertTrue(isException);
  }
}
