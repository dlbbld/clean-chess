package com.dlb.chess.test.pgn.parser;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.pgn.parser.enums.LenientPgnParserValidationProblem;
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.test.pgntest.PgnTestConstants;

class TestLenientPgnParserNotSanException extends AbstractTestLenientPgnParserException {
  private static final Path PGN_TEST_FOLDER_PATH = NonNullWrapperCommon
      .resolve(PgnTestConstants.LENIENT_PGN_PARSER_TEST_ROOT_FOLDER_PATH, "exception/notSan");

  @SuppressWarnings("static-method")
  @Test
  void testException() {
    checkException("01_tag_format.pgn", LenientPgnParserValidationProblem.TAG_FORMAT_INVALID);
    checkException("02_tag_format.pgn", LenientPgnParserValidationProblem.TAG_FORMAT_INVALID);
    checkException("03_tag_format.pgn", LenientPgnParserValidationProblem.TAG_FORMAT_INVALID);
    checkException("04_tag_format.pgn", LenientPgnParserValidationProblem.TAG_FORMAT_INVALID);

    checkException("05_tag_name_not_unique.pgn", LenientPgnParserValidationProblem.TAG_NAME_NOT_UNIQUE);

    checkException("06_tag_reappear.pgn", LenientPgnParserValidationProblem.TAG_REAPPEAR);
    checkException("07_tag_reappear.pgn", LenientPgnParserValidationProblem.TAG_REAPPEAR);

    checkException("08_tag_result_incorrect_value.pgn", LenientPgnParserValidationProblem.TAG_RESULT_VALUE_INVALID);

    checkException("09_tag_setup_tag_value_invalid.pgn", LenientPgnParserValidationProblem.TAG_SET_UP_VALUE_INVALID);
    checkException("10_tag_setup_tag_zero_but_fen_provided.pgn",
        LenientPgnParserValidationProblem.TAG_SET_UP_VALUE_ZERO_BUT_FEN_PROVIDED);

  }

  private static void checkException(String pgnFileName, LenientPgnParserValidationProblem expected) {
    checkException(PGN_TEST_FOLDER_PATH, pgnFileName, expected, SanValidationProblem.NONE);
  }
}
