package com.dlb.chess.test.librarycomparison.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dlb.chess.report.Report;
import com.dlb.chess.test.model.PgnTestCase;
import com.dlb.chess.test.report.representation.RepetitionRepresentation;

public abstract class RepetitionTestUtility {

  public static void testRepetition(Report report, PgnTestCase testCase) {
    final String calculatedRepetitionRepresentation = RepetitionRepresentation
        .calculateRepresentationRepetitionReport(report);
    assertEquals(testCase.expectedRepetition(), calculatedRepetitionRepresentation);
  }

}
