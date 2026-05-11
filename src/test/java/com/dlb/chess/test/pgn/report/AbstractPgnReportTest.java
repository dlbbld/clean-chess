package com.dlb.chess.test.pgn.report;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dlb.chess.common.enums.EnPassantCaptureRuleThreefold;
import com.dlb.chess.report.Reporter;
import com.dlb.chess.report.Report;
import com.dlb.chess.test.librarycomparison.utility.RepetitionTestUtility;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgn.setup.CreatePgnTestCases;
import com.dlb.chess.test.report.representation.NoProgressRepresentation;

public abstract class AbstractPgnReportTest {

  public static void testReportAgainstTestCase(String pgnFileName, Report report) throws Exception {
    final PgnFileTestCase testCase = CreatePgnTestCases.findTestCase(pgnFileName);
    testReportAgainstTestCase(testCase, report);
  }

  public static void testReportAgainstTestCase(PgnFileTestCaseList testCaseList, PgnFileTestCase testCase)
      throws Exception {
    final var report = Reporter.calculateReport(testCaseList.pgnTest().getFolderPath(), testCase.pgnFileName());
    testReportAgainstTestCase(testCase, report);
  }

  public static void testReportAgainstTestCase(PgnFileTestCase testCase, Report report) throws Exception {
    testFen(testCase.fen(), report.fen());
    testRepetition(report, testCase);
    testRepetitionInitialEnPassantCapture(report, testCase);
    testNoProgressMoveRule(report, testCase);
    testFirstCapture(report, testCase);
    testMaxNoProgressSequence(report, testCase);
    testCheckmateOrStalemate(report, testCase);
    testRepetitionCountFinalPosition(report, testCase);
    testInsufficientMaterial(report, testCase);
  }

  private static void testFen(String expectedFen, String actualFen) {
    assertEquals(expectedFen, actualFen);
  }

  private static void testRepetition(Report report, PgnFileTestCase testCase) {
    RepetitionTestUtility.testRepetition(report, testCase, EnPassantCaptureRuleThreefold.DO_NOT_IGNORE);
  }

  private static void testRepetitionInitialEnPassantCapture(Report report, PgnFileTestCase testCase) {
    RepetitionTestUtility.testRepetition(report, testCase, EnPassantCaptureRuleThreefold.DO_IGNORE);
  }

  private static void testNoProgressMoveRule(Report report, PgnFileTestCase testCase) {
    assertEquals(testCase.expectedNoProgressMoveRule(),
        NoProgressRepresentation.calculateRepresentationNoProgressMoveListList(report.noProgressMoveListList()));
  }

  private static void testFirstCapture(Report report, PgnFileTestCase testCase) {
    assertEquals(testCase.firstCapture(), report.firstCapture());
  }

  private static void testMaxNoProgressSequence(Report report, PgnFileTestCase testCase) {
    assertEquals(testCase.maxNoProgressSequence(), report.maxNoProgressSequence());
  }

  private static void testCheckmateOrStalemate(Report report, PgnFileTestCase testCase) {
    assertEquals(testCase.checkmateOrStalemate(), report.checkmateOrStalemate());
  }

  private static void testRepetitionCountFinalPosition(Report report, PgnFileTestCase testCase) {
    assertEquals(testCase.repetitionCountFinalPosition(), report.board().getRepetitionCount());
  }

  private static void testInsufficientMaterial(Report report, PgnFileTestCase testCase) {
    assertEquals(testCase.insufficientMaterial(), report.insufficientMaterial());
  }

}
