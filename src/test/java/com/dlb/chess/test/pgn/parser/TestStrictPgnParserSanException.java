package com.dlb.chess.test.pgn.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.pgn.parser.StrictPgnParser;
import com.dlb.chess.pgn.parser.enums.StrictPgnParserValidationProblem;
import com.dlb.chess.pgn.parser.exceptions.StrictPgnParserValidationException;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.test.pgntest.constants.PgnTestConstants;

class TestStrictPgnParserSanException extends AbstractTestStrictPgnParserException {
  private static final Path PGN_TEST_FOLDER_PATH = NonNullWrapperCommon
      .resolve(PgnTestConstants.STRICT_PGN_PARSER_TEST_ROOT_FOLDER_PATH, "exception/san");

  @SuppressWarnings("static-method")
  @Test
  void testException() {
    checkException("01_initial_position.pgn", SanValidationProblem.NOT_REACHABLE_PAWN_NON_CAPTURING);
    checkException("02_initial_position.pgn", SanValidationProblem.NOT_REACHABLE_PAWN_CAPTURING);

    checkException("03_custom_position_white_start.pgn",
        SanValidationProblem.DESTINATION_RNBQK_EMPTY_CAPTURE_SYMBOL);
    checkException("04_custom_position_black_start.pgn", SanValidationProblem.MOVEMENT_RNBQ_FROM_RANK);

  }

  private static void checkException(String pgnFileName, SanValidationProblem expected) {
    var isException = false;
    try {
      StrictPgnParser.parse(PGN_TEST_FOLDER_PATH, pgnFileName);
    } catch (final StrictPgnParserValidationException e) {
      isException = true;
      assertEquals(StrictPgnParserValidationProblem.SAN, e.getStrictPgnParserValidationProblem());
      assertEquals(expected, e.getSanValidationProblem());
    }
    assertTrue(isException);
  }
}
