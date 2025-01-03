package com.dlb.chess.test.pgn.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.pgn.reader.enums.PgnReaderStrictValidationProblem;
import com.dlb.chess.pgn.reader.exceptions.PgnReaderStrictValidationException;
import com.dlb.chess.test.pgntest.PgnTestConstants;

class TestPgnReaderStrictNotSanException {
  private static final Path PGN_TEST_FOLDER_PATH = NonNullWrapperCommon
      .resolve(PgnTestConstants.PGN_READER_STRICT_TEST_ROOT_FOLDER_PATH, "exception/notSan");

  @SuppressWarnings("static-method")
  @Test
  void test() {
    checkReadPgnException("01_empty_file.pgn", PgnReaderStrictValidationProblem.FILE_EMPTY);
    checkReadPgnException("02_starts_with_empty_line.pgn",
        PgnReaderStrictValidationProblem.FILE_EMPTY_LINE_CANNOT_START_WITH);
    checkReadPgnException("03_does_not_end_with_empty_line.pgn",
        PgnReaderStrictValidationProblem.FILE_EMPTY_LINE_MUST_END_WITH);
    checkReadPgnException("04_1_more_than_two_empty_lines.pgn",
        PgnReaderStrictValidationProblem.FILE_EMPTY_LINE_EXACTLY_TWO);
    checkReadPgnException("04_2_two_consecutive_empty_lines.pgn",
        PgnReaderStrictValidationProblem.FILE_EMPTY_LINE_NOT_CONSECUTIVE);

    checkReadPgnException("05_tag_no_start_brace.pgn",
        PgnReaderStrictValidationProblem.TAG_FORMAT_NOT_STARTING_WITH_LEFT_SQUARE_BRACKET);
    checkReadPgnException("06_tag_premature_end.pgn",
        PgnReaderStrictValidationProblem.TAG_FORMAT_NOT_ENDING_WITH_RIGHT_SQUARE_BRACKET);
    checkReadPgnException("07_tag_name_starts_with_space.pgn",
        PgnReaderStrictValidationProblem.TAG_FORMAT_LEFT_SQUARE_BRACKET_FOLLOWED_BY_SPACE);
    checkReadPgnException("08_tag_name_space_after_missing.pgn", PgnReaderStrictValidationProblem.TAG_FORMAT_INVALID);
    checkReadPgnException("09_tag_value_start_quote_missing.pgn", PgnReaderStrictValidationProblem.TAG_FORMAT_INVALID);
    checkReadPgnException("11_tag_value_ending_quote_not_found.pgn",
        PgnReaderStrictValidationProblem.TAG_FORMAT_INVALID);
    checkReadPgnException("12_1_tag_no_end_brace.pgn",
        PgnReaderStrictValidationProblem.TAG_FORMAT_NOT_ENDING_WITH_RIGHT_SQUARE_BRACKET);
    checkReadPgnException("12_2_tag_no_end_brace.pgn",
        PgnReaderStrictValidationProblem.TAG_FORMAT_NOT_ENDING_WITH_RIGHT_SQUARE_BRACKET);
    checkReadPgnException("13_tag_continues_above_end_brace.pgn",
        PgnReaderStrictValidationProblem.TAG_FORMAT_NOT_ENDING_WITH_RIGHT_SQUARE_BRACKET);

    checkReadPgnException("14_tag_name_not_unique.pgn", PgnReaderStrictValidationProblem.TAG_NAME_NOT_UNIQUE);
    checkReadPgnException("15_tag_not_all_required_tags_set.pgn",
        PgnReaderStrictValidationProblem.TAG_NOT_ALL_REQUIRED_TAGS_SET);

    checkReadPgnException("16_tag_result_value_invalid.pgn", PgnReaderStrictValidationProblem.TAG_RESULT_VALUE_INVALID);
    checkReadPgnException("17_tag_result_value_invalid.pgn", PgnReaderStrictValidationProblem.TAG_RESULT_VALUE_INVALID);

    checkReadPgnException("18_file_end_invalid.pgn", PgnReaderStrictValidationProblem.MOVETEXT_TERMINATION_INVALID);
    checkReadPgnException("19_file_end_invalid.pgn", PgnReaderStrictValidationProblem.MOVETEXT_TERMINATION_INVALID);
    checkReadPgnException("20_file_end_invalid.pgn", PgnReaderStrictValidationProblem.MOVETEXT_TERMINATION_INVALID);
    checkReadPgnException("21_file_end_invalid.pgn", PgnReaderStrictValidationProblem.MOVETEXT_TERMINATION_INVALID);

    checkReadPgnException("22_tag_set_up_value_invalid.pgn", PgnReaderStrictValidationProblem.TAG_SET_UP_VALUE_INVALID);
    checkReadPgnException("23_tag_set_up_requires_fen_tag_but_missing.pgn",
        PgnReaderStrictValidationProblem.TAG_SET_UP_REQUIRES_FEN_TAG_BUT_MISSING);

    checkReadPgnException("24_fen_tag_set_but_not_required.pgn",
        PgnReaderStrictValidationProblem.TAG_FEN_NOT_REQUIRED_BUT_SET);

    checkReadPgnException("25_1_invalid_commentary.pgn",
        PgnReaderStrictValidationProblem.MOVETEXT_COMMENTARY_START_BRACE_NOT_FOLLOWED_BY_END_BRACE);
    checkReadPgnException("25_2_invalid_commentary.pgn",
        PgnReaderStrictValidationProblem.MOVETEXT_SAN_CHARACTER_INVALID);
    checkReadPgnException("25_3_invalid_commentary.pgn",
        PgnReaderStrictValidationProblem.MOVETEXT_COMMENTARY_START_BRACE_NOT_FOLLOWED_BY_END_BRACE);
    checkReadPgnException("25_4_invalid_commentary.pgn",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_CONTINUE_AS_EXPECTED);
    checkReadPgnException("25_5_invalid_commentary.pgn",
        PgnReaderStrictValidationProblem.MOVETEXT_COMMENTARY_NOT_FOLLOWED_BY_SPACE);

    checkReadPgnException("26_1_move_number_for_non_initial_black_move.pgn",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_FOR_BLACK_NON_INITIAL_MOVE);
    checkReadPgnException("26_2_move_number_for_non_initial_black_move.pgn",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_FOR_BLACK_NON_INITIAL_MOVE);
    checkReadPgnException("26_3_move_number_for_non_initial_black_move.pgn",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_FOR_BLACK_NON_INITIAL_MOVE);
    checkReadPgnException("26_4_move_number_for_non_initial_black_move.pgn",
        PgnReaderStrictValidationProblem.MOVETEXT_MOVE_NUMBER_FOR_BLACK_NON_INITIAL_MOVE);

  }

  private static void checkReadPgnException(String pgnFileName, PgnReaderStrictValidationProblem expected) {
    var isException = false;
    try {
      PgnStrictCacheForTestCases.getPgn(PGN_TEST_FOLDER_PATH, pgnFileName);
    } catch (final PgnReaderStrictValidationException e) {
      isException = true;
      assertEquals(expected, e.getPgnReaderStrictValidationProblem());
    }
    assertTrue(isException);
  }

}
