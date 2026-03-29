package com.dlb.chess.test.pgn.parser;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.pgn.parser.enums.LenientPgnParserValidationProblem;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.test.pgntest.PgnTestConstants;

class TestLenientPgnParserSanException extends AbstractTestLenientPgnParserException {

  private static final Path PGN_TEST_FOLDER_PATH = NonNullWrapperCommon
      .resolve(PgnTestConstants.LENIENT_PGN_PARSER_TEST_ROOT_FOLDER_PATH, "exception/san");

  @SuppressWarnings("static-method")
  @Test
  void testException() {
    checkException("01_initial_position.pgn", SanValidationProblem.MOVING_ONTO_ONE_PIECE);
    checkException("02_initial_position.pgn", SanValidationProblem.PIECE_NEITHER_NO_LEGAL_MOVE);

    checkException("03_custom_position_white_start.pgn", SanValidationProblem.PIECE_NEITHER_NO_LEGAL_MOVE);
    checkException("04_custom_position_black_start.pgn", SanValidationProblem.PIECE_NEITHER_NO_LEGAL_MOVE);

  }

  private static void checkException(String pgnFileName, SanValidationProblem expectedSanValidationProblem) {
    checkException(PGN_TEST_FOLDER_PATH, pgnFileName, LenientPgnParserValidationProblem.SAN,
        expectedSanValidationProblem);
  }
}
