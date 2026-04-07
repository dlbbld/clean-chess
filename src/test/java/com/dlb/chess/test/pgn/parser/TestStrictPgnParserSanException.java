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
import com.dlb.chess.test.pgntest.PgnTestConstants;

class TestStrictPgnParserSanException extends AbstractTestStrictPgnParserException {
  private static final Path PGN_TEST_FOLDER_PATH = NonNullWrapperCommon
      .resolve(PgnTestConstants.STRICT_PGN_PARSER_TEST_ROOT_FOLDER_PATH, "exception/san");

  @SuppressWarnings("static-method")
  @Test
  void testException() {
    checkException("01_initial_position.pgn", SanValidationProblem.PAWN_FROM_SQUARE);
    checkException("02_initial_position.pgn", SanValidationProblem.PAWN_FROM_SQUARE);

    checkException("03_custom_position_white_start.pgn", SanValidationProblem.CAPTURING_MOVING_ONTO_NO_PIECE);
    checkException("04_custom_position_black_start.pgn", SanValidationProblem.PIECE_RANK_NO_PIECE_EXISTS);

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
