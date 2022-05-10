package com.dlb.chess.test.pgn.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dlb.chess.pgn.reader.enums.PgnReaderValidationProblem;
import com.dlb.chess.pgn.reader.exceptions.PgnReaderValidationException;
import com.dlb.chess.san.enums.SanValidationProblem;

public abstract class AbstractTestPgnReaderException extends AbstractTestPgnReader {
  static void checkReadPgnException(String pgnTestFolderPath, String pgnFileName, PgnReaderValidationProblem expected) {
    var isException = false;
    try {
      PgnCacheForTestCases.getPgn(pgnTestFolderPath, pgnFileName);
    } catch (final PgnReaderValidationException e) {
      isException = true;
      assertEquals(expected, e.getPgnReaderValidationProblem());
      assertEquals(SanValidationProblem.NONE, e.getSanValidationProblem());
    }
    assertTrue(isException);
  }
}
