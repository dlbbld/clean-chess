package com.dlb.chess.test.pgn.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import com.dlb.chess.pgn.parser.StrictPgnParser;
import com.dlb.chess.pgn.parser.enums.StrictPgnParserValidationProblem;
import com.dlb.chess.pgn.parser.exceptions.StrictPgnParserValidationException;
import com.dlb.chess.pgn.parser.model.StrictPgnParserValidationResult;
import com.dlb.chess.san.enums.SanValidationProblem;

public abstract class AbstractTestStrictPgnParserException {
  static void checkException(Path pgnTestFolderPath, String pgnFileName,
      StrictPgnParserValidationProblem expectedProblemParser, SanValidationProblem expectedProblemSan) {
    checkParse(pgnTestFolderPath, pgnFileName, expectedProblemParser, expectedProblemSan);
    checkValidate(pgnTestFolderPath, pgnFileName, expectedProblemParser, expectedProblemSan);
  }

  private static void checkParse(Path pgnTestFolderPath, String pgnFileName,
      StrictPgnParserValidationProblem expectedProblemParser, SanValidationProblem expectedProblemSan) {
    var isException = false;
    try {
      StrictPgnParser.parse(pgnTestFolderPath, pgnFileName);
    } catch (final StrictPgnParserValidationException e) {
      isException = true;
      assertEquals(expectedProblemParser, e.getStrictPgnParserValidationProblem());
      assertEquals(expectedProblemSan, e.getSanValidationProblem());
    }
    assertTrue(isException);
  }

  private static void checkValidate(Path pgnTestFolderPath, String pgnFileName,
      StrictPgnParserValidationProblem expectedParserProblem, SanValidationProblem expectedSanProblem) {
    final StrictPgnParserValidationResult result = StrictPgnParser.validate(pgnTestFolderPath, pgnFileName);
    assertFalse(result.isValid());
    assertEquals(expectedParserProblem, result.problemParser());
    assertEquals(expectedSanProblem, result.problemSan());
  }

}
