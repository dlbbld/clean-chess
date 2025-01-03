package com.dlb.chess.test.pgn.reader;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.pgn.reader.enums.PgnReaderValidationProblem;
import com.dlb.chess.test.pgntest.PgnTestConstants;

class TestPgnReaderNotSanException extends AbstractTestPgnReaderException {
  private static final Path PGN_TEST_FOLDER_PATH = NonNullWrapperCommon
      .resolve(PgnTestConstants.PGN_READER_NON_STRICT_TEST_ROOT_FOLDER_PATH, "exception/notSan");

  @SuppressWarnings("static-method")
  @Test
  void testException() {
    checkReadPgnException("01_tag_format.pgn", PgnReaderValidationProblem.TAG_FORMAT_INVALID);
    checkReadPgnException("02_tag_format.pgn", PgnReaderValidationProblem.TAG_FORMAT_INVALID);
    checkReadPgnException("03_tag_format.pgn", PgnReaderValidationProblem.TAG_FORMAT_INVALID);
    checkReadPgnException("04_tag_format.pgn", PgnReaderValidationProblem.TAG_FORMAT_INVALID);

    checkReadPgnException("05_tag_name_not_unique.pgn", PgnReaderValidationProblem.TAG_NAME_NOT_UNIQUE);

    checkReadPgnException("06_tag_reappear.pgn", PgnReaderValidationProblem.TAG_REAPPEAR);
    checkReadPgnException("07_tag_reappear.pgn", PgnReaderValidationProblem.TAG_REAPPEAR);

    checkReadPgnException("08_tag_result_incorrect_value.pgn", PgnReaderValidationProblem.TAG_RESULT_VALUE_INVALID);

    checkReadPgnException("09_tag_setup_tag_value_invalid.pgn", PgnReaderValidationProblem.TAG_SET_UP_VALUE_INVALID);
    checkReadPgnException("10_tag_setup_tag_zero_but_fen_provided.pgn",
        PgnReaderValidationProblem.TAG_SET_UP_VALUE_ZERO_BUT_FEN_PROVIDED);

  }

  private static void checkReadPgnException(String pgnFileName, PgnReaderValidationProblem expected) {
    checkReadPgnException(PGN_TEST_FOLDER_PATH, pgnFileName, expected);
  }
}
