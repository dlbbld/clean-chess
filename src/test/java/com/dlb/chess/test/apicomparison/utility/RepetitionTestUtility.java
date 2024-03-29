package com.dlb.chess.test.apicomparison.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dlb.chess.analysis.model.Analysis;
import com.dlb.chess.common.enums.EnPassantCaptureRuleThreefold;
import com.dlb.chess.test.analysis.output.RepetitionOutput;
import com.dlb.chess.test.model.PgnFileTestCase;

public abstract class RepetitionTestUtility {

  public static String getExpectedRepetition(PgnFileTestCase testCase,
      EnPassantCaptureRuleThreefold enPassantCaptureRule) {
    return switch (enPassantCaptureRule) {
      case DO_IGNORE -> testCase.expectedRepetitionInitialEnPassantCapture();
      case DO_NOT_IGNORE -> testCase.expectedRepetition();
      default -> throw new IllegalArgumentException();
    };
  }

  public static void testRepetition(Analysis analysis, PgnFileTestCase testCase,
      EnPassantCaptureRuleThreefold enPassantCaptureRule) {
    final String calculatedRepetitionRepresentation = RepetitionOutput.calculateOutputRepetitionAnalysis(analysis,
        enPassantCaptureRule);
    assertEquals(getExpectedRepetition(testCase, enPassantCaptureRule), calculatedRepetitionRepresentation);
  }

}
