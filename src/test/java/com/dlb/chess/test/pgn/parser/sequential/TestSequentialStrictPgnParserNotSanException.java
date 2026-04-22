package com.dlb.chess.test.pgn.parser.sequential;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.pgn.parser.enums.StrictPgnParserValidationProblem;
import com.dlb.chess.pgn.parser.exceptions.StrictPgnParserValidationException;
import com.dlb.chess.pgn.parser.sequential.SequentialStrictPgnParser;
import com.dlb.chess.test.pgntest.constants.PgnTestConstants;

/**
 * Parity check — the sequential strict parser should reject each exception fixture with the same
 * {@link StrictPgnParserValidationProblem} enum as the existing strict parser. Mirrors
 * {@code TestStrictPgnParserNotSanException}.
 */
class TestSequentialStrictPgnParserNotSanException {

  private static final Path PGN_TEST_FOLDER_PATH = NonNullWrapperCommon
      .resolve(PgnTestConstants.STRICT_PGN_PARSER_TEST_ROOT_FOLDER_PATH, "exception/notSan");

  @SuppressWarnings("static-method")
  @Test
  void test() {
    final Map<String, StrictPgnParserValidationProblem> expectations = new LinkedHashMap<>();
    expectations.put("01_empty_file.pgn", StrictPgnParserValidationProblem.FILE_EMPTY);
    expectations.put("02_starts_with_empty_line.pgn", StrictPgnParserValidationProblem.FILE_EMPTY_LINE_CANNOT_START_WITH);
    expectations.put("03_does_not_end_with_empty_line.pgn",
        StrictPgnParserValidationProblem.FILE_EMPTY_LINE_MUST_END_WITH);
    expectations.put("04_1_more_than_two_empty_lines.pgn",
        StrictPgnParserValidationProblem.FILE_EMPTY_LINE_EXACTLY_TWO);
    expectations.put("04_2_two_consecutive_empty_lines.pgn",
        StrictPgnParserValidationProblem.FILE_EMPTY_LINE_NOT_CONSECUTIVE);

    expectations.put("05_tag_no_start_brace.pgn",
        StrictPgnParserValidationProblem.TAG_FORMAT_NOT_STARTING_WITH_LEFT_SQUARE_BRACKET);
    expectations.put("06_tag_premature_end.pgn",
        StrictPgnParserValidationProblem.TAG_FORMAT_NOT_ENDING_WITH_RIGHT_SQUARE_BRACKET);
    expectations.put("07_tag_name_starts_with_space.pgn",
        StrictPgnParserValidationProblem.TAG_FORMAT_LEFT_SQUARE_BRACKET_FOLLOWED_BY_SPACE);
    expectations.put("08_tag_name_space_after_missing.pgn", StrictPgnParserValidationProblem.TAG_FORMAT_INVALID);
    expectations.put("09_tag_value_start_quote_missing.pgn", StrictPgnParserValidationProblem.TAG_FORMAT_INVALID);
    expectations.put("11_tag_value_ending_quote_not_found.pgn", StrictPgnParserValidationProblem.TAG_FORMAT_INVALID);
    expectations.put("12_1_tag_no_end_brace.pgn",
        StrictPgnParserValidationProblem.TAG_FORMAT_NOT_ENDING_WITH_RIGHT_SQUARE_BRACKET);
    expectations.put("12_2_tag_no_end_brace.pgn",
        StrictPgnParserValidationProblem.TAG_FORMAT_NOT_ENDING_WITH_RIGHT_SQUARE_BRACKET);
    expectations.put("13_tag_continues_above_end_brace.pgn",
        StrictPgnParserValidationProblem.TAG_FORMAT_NOT_ENDING_WITH_RIGHT_SQUARE_BRACKET);

    expectations.put("14_tag_name_not_unique.pgn", StrictPgnParserValidationProblem.TAG_NAME_NOT_UNIQUE);
    expectations.put("15_tag_not_all_required_tags_set.pgn",
        StrictPgnParserValidationProblem.TAG_NOT_ALL_REQUIRED_TAGS_SET);

    expectations.put("16_tag_result_value_invalid.pgn", StrictPgnParserValidationProblem.TAG_RESULT_VALUE_INVALID);
    expectations.put("17_tag_result_value_invalid.pgn", StrictPgnParserValidationProblem.TAG_RESULT_VALUE_INVALID);

    expectations.put("18_file_end_invalid.pgn", StrictPgnParserValidationProblem.MOVETEXT_TERMINATION_INVALID);
    expectations.put("19_file_end_invalid.pgn", StrictPgnParserValidationProblem.MOVETEXT_TERMINATION_INVALID);
    expectations.put("20_file_end_invalid.pgn", StrictPgnParserValidationProblem.MOVETEXT_TERMINATION_INVALID);
    expectations.put("21_file_end_invalid.pgn", StrictPgnParserValidationProblem.MOVETEXT_TERMINATION_INVALID);

    expectations.put("22_tag_set_up_value_invalid.pgn", StrictPgnParserValidationProblem.TAG_SET_UP_VALUE_INVALID);
    expectations.put("23_tag_set_up_requires_fen_tag_but_missing.pgn",
        StrictPgnParserValidationProblem.TAG_SET_UP_REQUIRES_FEN_TAG_BUT_MISSING);

    expectations.put("24_fen_tag_set_but_not_required.pgn",
        StrictPgnParserValidationProblem.TAG_FEN_NOT_REQUIRED_BUT_SET);

    expectations.put("25_1_invalid_commentary.pgn",
        StrictPgnParserValidationProblem.MOVETEXT_COMMENTARY_START_BRACE_NOT_FOLLOWED_BY_END_BRACE);
    expectations.put("25_2_invalid_commentary.pgn",
        StrictPgnParserValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_CONTINUE_AS_EXPECTED);
    expectations.put("25_3_invalid_commentary.pgn",
        StrictPgnParserValidationProblem.MOVETEXT_COMMENTARY_START_BRACE_NOT_FOLLOWED_BY_END_BRACE);
    expectations.put("25_4_invalid_commentary.pgn",
        StrictPgnParserValidationProblem.MOVETEXT_MOVE_NUMBER_DOES_NOT_CONTINUE_AS_EXPECTED);
    expectations.put("25_5_invalid_commentary.pgn",
        StrictPgnParserValidationProblem.MOVETEXT_COMMENTARY_NOT_FOLLOWED_BY_SPACE);

    expectations.put("26_1_move_number_for_non_initial_black_move.pgn",
        StrictPgnParserValidationProblem.MOVETEXT_MOVE_NUMBER_FOR_BLACK_NON_INITIAL_MOVE);
    expectations.put("26_2_move_number_for_non_initial_black_move.pgn",
        StrictPgnParserValidationProblem.MOVETEXT_MOVE_NUMBER_FOR_BLACK_NON_INITIAL_MOVE);
    expectations.put("26_3_move_number_for_non_initial_black_move.pgn",
        StrictPgnParserValidationProblem.MOVETEXT_MOVE_NUMBER_FOR_BLACK_NON_INITIAL_MOVE);
    expectations.put("26_4_move_number_for_non_initial_black_move.pgn",
        StrictPgnParserValidationProblem.MOVETEXT_MOVE_NUMBER_FOR_BLACK_NON_INITIAL_MOVE);

    final List<String> mismatches = new ArrayList<>();
    for (final Map.Entry<String, StrictPgnParserValidationProblem> entry : expectations.entrySet()) {
      final String fileName = NonNullWrapperCommon.getKey(entry);
      final StrictPgnParserValidationProblem expected = NonNullWrapperCommon.getValue(entry);
      try {
        SequentialStrictPgnParser.parse(PGN_TEST_FOLDER_PATH, fileName);
        mismatches.add(fileName + ": expected " + expected + " but sequential parser accepted the file");
      } catch (final StrictPgnParserValidationException e) {
        if (e.getStrictPgnParserValidationProblem() != expected) {
          mismatches.add(fileName + ": expected " + expected + " but got "
              + e.getStrictPgnParserValidationProblem() + " — " + e.getMessage());
        }
      } catch (final RuntimeException e) {
        mismatches.add(fileName + ": expected " + expected + " but got "
            + e.getClass().getSimpleName() + " — " + e.getMessage());
      }
    }

    assertEquals(List.of(), mismatches, "Sequential strict parser diverges on " + mismatches.size()
        + " exception fixture(s) out of " + expectations.size());
  }
}
