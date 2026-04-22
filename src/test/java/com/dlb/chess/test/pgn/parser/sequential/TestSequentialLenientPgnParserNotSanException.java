package com.dlb.chess.test.pgn.parser.sequential;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.pgn.parser.enums.LenientPgnParserValidationProblem;
import com.dlb.chess.pgn.parser.exceptions.LenientPgnParserValidationException;
import com.dlb.chess.pgn.parser.sequential.SequentialLenientPgnParser;
import com.dlb.chess.test.pgntest.constants.PgnTestConstants;

class TestSequentialLenientPgnParserNotSanException {

  private static final Path PGN_TEST_FOLDER_PATH = NonNullWrapperCommon
      .resolve(PgnTestConstants.LENIENT_PGN_PARSER_TEST_ROOT_FOLDER_PATH, "exception/notSan");

  @SuppressWarnings("static-method")
  @Test
  void test() {
    final Map<String, LenientPgnParserValidationProblem> expectations = new LinkedHashMap<>();
    expectations.put("01_tag_format.pgn", LenientPgnParserValidationProblem.TAG_FORMAT_INVALID);
    expectations.put("02_tag_format.pgn", LenientPgnParserValidationProblem.TAG_FORMAT_INVALID);
    expectations.put("03_tag_format.pgn", LenientPgnParserValidationProblem.TAG_FORMAT_INVALID);
    expectations.put("04_tag_format.pgn", LenientPgnParserValidationProblem.TAG_FORMAT_INVALID);
    expectations.put("05_tag_name_not_unique.pgn", LenientPgnParserValidationProblem.TAG_NAME_NOT_UNIQUE);
    expectations.put("06_tag_reappear.pgn", LenientPgnParserValidationProblem.TAG_REAPPEAR);
    expectations.put("07_tag_reappear.pgn", LenientPgnParserValidationProblem.TAG_REAPPEAR);
    expectations.put("08_tag_result_incorrect_value.pgn", LenientPgnParserValidationProblem.TAG_RESULT_VALUE_INVALID);
    expectations.put("09_tag_setup_tag_value_invalid.pgn", LenientPgnParserValidationProblem.TAG_SET_UP_VALUE_INVALID);
    expectations.put("10_tag_setup_tag_zero_but_fen_provided.pgn",
        LenientPgnParserValidationProblem.TAG_SET_UP_VALUE_ZERO_BUT_FEN_PROVIDED);

    final List<String> mismatches = new ArrayList<>();
    for (final Map.Entry<String, LenientPgnParserValidationProblem> entry : expectations.entrySet()) {
      final String fileName = NonNullWrapperCommon.getKey(entry);
      final LenientPgnParserValidationProblem expected = NonNullWrapperCommon.getValue(entry);
      try {
        SequentialLenientPgnParser.parse(PGN_TEST_FOLDER_PATH, fileName);
        mismatches.add(fileName + ": expected " + expected + " but parser accepted the file");
      } catch (final LenientPgnParserValidationException e) {
        if (e.getLenientPgnParserValidationProblem() != expected) {
          mismatches.add(fileName + ": expected " + expected + " but got "
              + e.getLenientPgnParserValidationProblem() + " — " + e.getMessage());
        }
      } catch (final RuntimeException e) {
        mismatches.add(fileName + ": expected " + expected + " but got "
            + e.getClass().getSimpleName() + " — " + e.getMessage());
      }
    }

    assertEquals(List.of(), mismatches, "Sequential lenient parser diverges on " + mismatches.size()
        + " exception fixture(s) out of " + expectations.size());
  }
}
