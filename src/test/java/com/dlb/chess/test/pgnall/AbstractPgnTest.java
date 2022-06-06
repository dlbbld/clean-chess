package com.dlb.chess.test.pgnall;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.logging.log4j.Logger;

import com.dlb.chess.analysis.model.Analysis;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.enums.EnPassantCaptureRuleThreefold;
import com.dlb.chess.test.analysis.output.YawnOutput;
import com.dlb.chess.test.apicomparison.utility.RepetitionTestUtility;
import com.dlb.chess.test.model.PgnFileTestCase;

public abstract class AbstractPgnTest {

  private static final Logger logger = NonNullWrapperCommon.getLogger(AbstractPgnTest.class);

  public static void testGame(PgnFileTestCase testCase, Analysis analysis) throws Exception {
    logger.info(testCase.pgnFileName());
    testFen(testCase.fen(), analysis.fen());
    testRepetition(analysis, testCase);
    testRepetitionInitialEnPassantCapture(analysis, testCase);
    testYawnMoveRule(analysis, testCase);
    testFirstCapture(analysis, testCase);
    testMaxYawnSequence(analysis, testCase);
    testCheckmateOrStalemate(analysis, testCase);
    testRepetitionCountFinalPosition(analysis, testCase);
    testInsufficientMaterial(analysis, testCase);
    testUnwinnableQuick(analysis, testCase);
    testUnwinnableFull(analysis, testCase);
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

  private static void testUnwinnableQuick(Analysis analysis, PgnFileTestCase testCase) {
    assertEquals(testCase.unwinnableQuickNotHavingMove(), analysis.unwinnableQuickNotHavingMove());
    assertEquals(testCase.unwinnableQuickHavingMove(), analysis.unwinnableQuickHavingMove());
  }

  private static void testUnwinnableFull(Analysis analysis, PgnFileTestCase testCase) {
    assertEquals(testCase.unwinnableFullHavingMove(), analysis.unwinnableFullHavingMove());
    assertEquals(testCase.unwinnableFullNotHavingMove(), analysis.unwinnableFullNotHavingMove());
  }

}
