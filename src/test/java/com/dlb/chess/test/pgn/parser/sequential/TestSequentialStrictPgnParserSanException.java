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
import com.dlb.chess.san.enums.SanValidationProblem;
import com.dlb.chess.test.pgntest.constants.PgnTestConstants;

/**
 * Mirrors {@code TestStrictPgnParserSanException} against the sequential parser — each SAN-level violation must
 * surface with the same {@link SanValidationProblem} classification as the existing strict parser.
 */
class TestSequentialStrictPgnParserSanException {

  private static final Path PGN_TEST_FOLDER_PATH = NonNullWrapperCommon
      .resolve(PgnTestConstants.STRICT_PGN_PARSER_TEST_ROOT_FOLDER_PATH, "exception/san");

  @SuppressWarnings("static-method")
  @Test
  void test() {
    final Map<String, SanValidationProblem> expectations = new LinkedHashMap<>();
    expectations.put("01_initial_position.pgn", SanValidationProblem.NOT_REACHABLE_PAWN_NON_CAPTURING);
    expectations.put("02_initial_position.pgn", SanValidationProblem.NOT_REACHABLE_PAWN_CAPTURING);
    expectations.put("03_custom_position_white_start.pgn",
        SanValidationProblem.DESTINATION_RNBQK_EMPTY_CAPTURE_SYMBOL);
    expectations.put("04_custom_position_black_start.pgn", SanValidationProblem.MOVEMENT_RNBQ_FROM_RANK);

    final List<String> mismatches = new ArrayList<>();
    for (final Map.Entry<String, SanValidationProblem> entry : expectations.entrySet()) {
      final String fileName = NonNullWrapperCommon.getKey(entry);
      final SanValidationProblem expected = NonNullWrapperCommon.getValue(entry);
      try {
        SequentialStrictPgnParser.parse(PGN_TEST_FOLDER_PATH, fileName);
        mismatches.add(fileName + ": expected SAN problem " + expected + " but sequential parser accepted the file");
      } catch (final StrictPgnParserValidationException e) {
        if (e.getStrictPgnParserValidationProblem() != StrictPgnParserValidationProblem.SAN) {
          mismatches.add(fileName + ": expected top-level problem SAN but got "
              + e.getStrictPgnParserValidationProblem() + " — " + e.getMessage());
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

    assertEquals(List.of(), mismatches, "Sequential strict parser diverges on " + mismatches.size()
        + " SAN exception fixture(s) out of " + expectations.size());
  }
}
