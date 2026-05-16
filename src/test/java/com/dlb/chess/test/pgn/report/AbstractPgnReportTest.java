package com.dlb.chess.test.pgn.report;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dlb.chess.report.Report;
import com.dlb.chess.report.Reporter;
import com.dlb.chess.test.librarycomparison.utility.RepetitionTestUtility;
import com.dlb.chess.test.model.PgnTestCase;
import com.dlb.chess.test.model.PgnTestCaseList;
import com.dlb.chess.test.pgn.setup.PgnTestCaseCatalog;
import com.dlb.chess.test.report.representation.NoProgressRepresentation;

public abstract class AbstractPgnReportTest {

  public static void testReportAgainstTestCase(String pgnFileName, Report report) throws Exception {
    final PgnTestCase testCase = PgnTestCaseCatalog.findTestCase(pgnFileName);
    testReportAgainstTestCase(testCase, report);
  }

  public static void testReportAgainstTestCase(PgnTestCaseList testCaseList, PgnTestCase testCase)
      throws Exception {
    final var report = Reporter.calculateReport(testCaseList.pgnTest().getFolderPath(), testCase.pgnFileName());
    testReportAgainstTestCase(testCase, report);
  }

  public static void testReportAgainstTestCase(PgnTestCase testCase, Report report) throws Exception {
    testFen(testCase.finalFen(), report.fen());
    testRepetition(report, testCase);
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

  private static void testRepetition(Report report, PgnTestCase testCase) {
    RepetitionTestUtility.testRepetition(report, testCase);
  }

  private static void testNoProgressMoveRule(Report report, PgnTestCase testCase) {
    assertEquals(testCase.expectedNoProgressMoveRule(),
        NoProgressRepresentation.calculateRepresentationNoProgressMoveListList(report.noProgressMoveListList()));
  }

  private static void testFirstCapture(Report report, PgnTestCase testCase) {
    assertEquals(testCase.firstCapture(), report.firstCapture());
  }

  private static void testMaxNoProgressSequence(Report report, PgnTestCase testCase) {
    assertEquals(testCase.maxNoProgressSequence(), report.maxNoProgressSequence());
  }

  private static void testCheckmateOrStalemate(Report report, PgnTestCase testCase) {
    assertEquals(testCase.checkmateOrStalemate(), report.checkmateOrStalemate());
  }

  private static void testRepetitionCountFinalPosition(Report report, PgnTestCase testCase) {
    assertEquals(testCase.repetitionCountFinalPosition(), report.board().getRepetitionCount());
  }

  private static void testInsufficientMaterial(Report report, PgnTestCase testCase) {
    assertEquals(testCase.insufficientMaterial(), report.insufficientMaterial());
  }

}
