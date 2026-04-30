package com.dlb.chess.test.pgnall;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dlb.chess.analysis.Analyzer;
import com.dlb.chess.analysis.model.Analysis;
import com.dlb.chess.common.enums.EnPassantCaptureRuleThreefold;
import com.dlb.chess.test.analysis.output.YawnOutput;
import com.dlb.chess.test.apicomparison.utility.RepetitionTestUtility;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.model.PgnFileTestCaseList;
import com.dlb.chess.test.pgntest.PgnExpectedValue;

public abstract class AbstractPgnTest {

  public static void testAnalysisAgainstTestCase(String pgnFileName, Analysis analysis) throws Exception {
    final PgnFileTestCase testCase = PgnExpectedValue.findTestCase(pgnFileName);
    testAnalysisAgainstTestCase(testCase, analysis);
  }

  public static void testAnalysisAgainstTestCase(PgnFileTestCaseList testCaseList, PgnFileTestCase testCase)
      throws Exception {
    final var analysis = Analyzer.calculateAnalysis(testCaseList.pgnTest().getFolderPath(), testCase.pgnFileName());
    testAnalysisAgainstTestCase(testCase, analysis);
  }

  public static void testAnalysisAgainstTestCase(PgnFileTestCase testCase, Analysis analysis) throws Exception {
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

  private static void testRepetition(Analysis analysis, PgnFileTestCase testCase) {
    RepetitionTestUtility.testRepetition(analysis, testCase, EnPassantCaptureRuleThreefold.DO_NOT_IGNORE);
  }

  private static void testRepetitionInitialEnPassantCapture(Analysis analysis, PgnFileTestCase testCase) {
    RepetitionTestUtility.testRepetition(analysis, testCase, EnPassantCaptureRuleThreefold.DO_IGNORE);
  }

  private static void testYawnMoveRule(Analysis analysis, PgnFileTestCase testCase) {
    assertEquals(testCase.expectedYawnMoveRule(),
        YawnOutput.calculateOutputYawnMoveListList(analysis.yawnMoveListList()));
  }

  private static void testFirstCapture(Analysis analysis, PgnFileTestCase testCase) {
    assertEquals(testCase.firstCapture(), analysis.firstCapture());
  }

  private static void testMaxYawnSequence(Analysis analysis, PgnFileTestCase testCase) {
    assertEquals(testCase.maxYawnSequence(), analysis.maxYawnSequence());
  }

  private static void testCheckmateOrStalemate(Analysis analysis, PgnFileTestCase testCase) {
    assertEquals(testCase.checkmateOrStalemate(), analysis.checkmateOrStalemate());
  }

  private static void testRepetitionCountFinalPosition(Analysis analysis, PgnFileTestCase testCase) {
    assertEquals(testCase.repetitionCountFinalPosition(), analysis.board().getRepetitionCount());
  }

  private static void testInsufficientMaterial(Analysis analysis, PgnFileTestCase testCase) {
    assertEquals(testCase.insufficientMaterial(), analysis.insufficientMaterial());
  }

}
