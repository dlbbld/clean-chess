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
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.test.pgntest.constants.PgnTestConstants;

class TestSequentialLenientPgnParserSanException {

  private static final Path PGN_TEST_FOLDER_PATH = NonNullWrapperCommon
      .resolve(PgnTestConstants.LENIENT_PGN_PARSER_TEST_ROOT_FOLDER_PATH, "exception/san");

  @SuppressWarnings("static-method")
  @Test
  void test() {
    final Map<String, SanValidationProblem> expectations = new LinkedHashMap<>();
    expectations.put("01_initial_position.pgn", SanValidationProblem.DESTINATION_RNBQK_OWN_PIECE_NON_CAPTURING);
    expectations.put("02_initial_position.pgn", SanValidationProblem.NOT_REACHABLE_RNBQ_NEITHER_MULTIPLE);
    expectations.put("03_custom_position_white_start.pgn", SanValidationProblem.NOT_REACHABLE_RNBQ_NEITHER_MULTIPLE);
    expectations.put("04_custom_position_black_start.pgn", SanValidationProblem.NOT_REACHABLE_RNBQ_NEITHER_SINGLE);

    final List<String> mismatches = new ArrayList<>();
    for (final Map.Entry<String, SanValidationProblem> entry : expectations.entrySet()) {
      final String fileName = NonNullWrapperCommon.getKey(entry);
      final SanValidationProblem expected = NonNullWrapperCommon.getValue(entry);
      try {
        SequentialLenientPgnParser.parse(PGN_TEST_FOLDER_PATH, fileName);
        mismatches.add(fileName + ": expected SAN problem " + expected + " but parser accepted the file");
      } catch (final LenientPgnParserValidationException e) {
        if (e.getLenientPgnParserValidationProblem() != LenientPgnParserValidationProblem.SAN) {
          mismatches.add(fileName + ": expected top-level SAN but got "
              + e.getLenientPgnParserValidationProblem() + " — " + e.getMessage());
          continue;
        }
        if (e.getSanValidationProblem() != expected) {
          mismatches.add(fileName + ": expected SAN problem " + expected + " but got " + e.getSanValidationProblem()
              + " — " + e.getMessage());
        }
      } catch (final RuntimeException e) {
        mismatches.add(fileName + ": expected SAN problem " + expected + " but got "
            + e.getClass().getSimpleName() + " — " + e.getMessage());
      }
    }

    assertEquals(List.of(), mismatches, "Sequential lenient parser diverges on " + mismatches.size()
        + " SAN exception fixture(s) out of " + expectations.size());
  }
}
