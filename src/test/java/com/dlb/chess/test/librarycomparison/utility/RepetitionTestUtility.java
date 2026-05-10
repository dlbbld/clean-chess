package com.dlb.chess.test.librarycomparison.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dlb.chess.common.enums.EnPassantCaptureRuleThreefold;
import com.dlb.chess.report.model.Report;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.report.representation.RepetitionRepresentation;

public abstract class RepetitionTestUtility {

  public static String getExpectedRepetition(PgnFileTestCase testCase,
      EnPassantCaptureRuleThreefold enPassantCaptureRule) {
    return switch (enPassantCaptureRule) {
      case DO_IGNORE -> testCase.expectedRepetitionInitialEnPassantCapture();
      case DO_NOT_IGNORE -> testCase.expectedRepetition();
      default -> throw new IllegalArgumentException();
    };
  }

  public static void testRepetition(Report report, PgnFileTestCase testCase,
      EnPassantCaptureRuleThreefold enPassantCaptureRule) {
    final String calculatedRepetitionRepresentation = RepetitionRepresentation
        .calculateRepresentationRepetitionReport(report, enPassantCaptureRule);
    assertEquals(getExpectedRepetition(testCase, enPassantCaptureRule), calculatedRepetitionRepresentation);
  }

}
