package com.dlb.chess.test.pgn.parser;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.pgn.parser.enums.LenientPgnParserValidationProblem;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.test.pgntest.PgnTestConstants;

class TestLenientPgnParserResultException extends AbstractTestLenientPgnParserException {
  private static final Path PGN_TEST_FOLDER_PATH = NonNullWrapperCommon
      .resolve(PgnTestConstants.LENIENT_PGN_PARSER_TEST_ROOT_FOLDER_PATH, "result");

  @SuppressWarnings("static-method")
  @Test
  void testException() {

    checkException("04_has_result_tag_has_termination_tag_different.pgn",
        LenientPgnParserValidationProblem.TAG_RESULT_BOTH_SET_BUT_DIFFERENT);
    checkException("05_has_result_tag_has_termination_tag_different.pgn",
        LenientPgnParserValidationProblem.TAG_RESULT_BOTH_SET_BUT_DIFFERENT);
    checkException("06_has_result_tag_has_termination_tag_different.pgn",
        LenientPgnParserValidationProblem.TAG_RESULT_BOTH_SET_BUT_DIFFERENT);
  }

  private static void checkException(String pgnFileName, LenientPgnParserValidationProblem expected) {
    checkException(PGN_TEST_FOLDER_PATH, pgnFileName, expected, SanValidationProblem.NONE);
  }
}