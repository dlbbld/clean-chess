package com.dlb.chess.test.pgnall;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dlb.chess.common.enums.EnPassantCaptureRuleThreefold;
import com.dlb.chess.report.Reporter;
import com.dlb.chess.report.model.Report;
import com.dlb.chess.test.analysis.representation.YawnRepresentation;
import com.dlb.chess.test.librarycomparison.utility.RepetitionTestUtility;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;

public abstract class AbstractPgnTest {

  public static void testAnalysisAgainstTestCase(String pgnFileName, Report analysis) throws Exception {
    final PgnFileTestCase testCase = PgnExpectedValue.findTestCase(pgnFileName);
    testAnalysisAgainstTestCase(testCase, analysis);
  }

  public static void testAnalysisAgainstTestCase(PgnFileTestCaseList testCaseList, PgnFileTestCase testCase)
      throws Exception {
    final var analysis = Reporter.calculateAnalysis(testCaseList.pgnTest().getFolderPath(), testCase.pgnFileName());
    testAnalysisAgainstTestCase(testCase, analysis);
  }

  public static void testAnalysisAgainstTestCase(PgnFileTestCase testCase, Report analysis) throws Exception {
    testFen(testCase.fen(), analysis.fen());
    testRepetition(analysis, testCase);
    testRepetitionInitialEnPassantCapture(analysis, testCase);
    testYawnMoveRule(analysis, testCase);
    testFirstCapture(analysis, testCase);
    testMaxYawnSequence(analysis, testCase);
    testCheckmateOrStalemate(analysis, testCase);
    testRepetitionCountFinalPosition(analysis, testCase);
    testInsufficientMaterial(analysis, testCase);
  }

  private static void testFen(String expectedFen, String actualFen) {
    assertEquals(expectedFen, actualFen);
  }

  private static void testRepetition(Report analysis, PgnFileTestCase testCase) {
    RepetitionTestUtility.testRepetition(analysis, testCase, EnPassantCaptureRuleThreefold.DO_NOT_IGNORE);
  }

  private static void testRepetitionInitialEnPassantCapture(Report analysis, PgnFileTestCase testCase) {
    RepetitionTestUtility.testRepetition(analysis, testCase, EnPassantCaptureRuleThreefold.DO_IGNORE);
  }

  private static void testYawnMoveRule(Report analysis, PgnFileTestCase testCase) {
    assertEquals(testCase.expectedYawnMoveRule(),
        YawnRepresentation.calculateRepresentationYawnMoveListList(analysis.yawnMoveListList()));
  }

  private static void testFirstCapture(Report analysis, PgnFileTestCase testCase) {
    assertEquals(testCase.firstCapture(), analysis.firstCapture());
  }

  private static void testMaxYawnSequence(Report analysis, PgnFileTestCase testCase) {
    assertEquals(testCase.maxYawnSequence(), analysis.maxYawnSequence());
  }

  private static void testCheckmateOrStalemate(Report analysis, PgnFileTestCase testCase) {
    assertEquals(testCase.checkmateOrStalemate(), analysis.checkmateOrStalemate());
  }

  private static void testRepetitionCountFinalPosition(Report analysis, PgnFileTestCase testCase) {
    assertEquals(testCase.repetitionCountFinalPosition(), analysis.board().getRepetitionCount());
  }

  private static void testInsufficientMaterial(Report analysis, PgnFileTestCase testCase) {
    assertEquals(testCase.insufficientMaterial(), analysis.insufficientMaterial());
  }

}
