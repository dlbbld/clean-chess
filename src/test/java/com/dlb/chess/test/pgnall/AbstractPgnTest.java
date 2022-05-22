package com.dlb.chess.test.pgnall;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.apache.logging.log4j.Logger;

import com.dlb.chess.analysis.Analyzer;
import com.dlb.chess.analysis.model.Analysis;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.enums.EnPassantCaptureRuleThreefold;
import com.dlb.chess.test.analysis.output.YawnOutput;
import com.dlb.chess.test.apicomparison.utility.RepetitionTestUtility;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.pgntest.PgnTestConstants;
import com.dlb.chess.test.pgntest.enums.UnwinnableFullResultTest;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuick;

public abstract class AbstractPgnTest {

  private static final Logger logger = NonNullWrapperCommon.getLogger(AbstractPgnTest.class);

  public static void testGame(PgnFileTestCase testCase, Analysis analysis) throws Exception {
    logger.info(testCase.pgnFileName());
    testFen(testCase.fen(), analysis.fen());
    testRepetition(analysis, testCase);
    testRepetitionInitialEnPassantCapture(analysis, testCase);
    testYawnMoveRule(analysis, testCase);
    testSequenceRepetition(analysis, testCase);
    testFirstCapture(analysis, testCase);
    testMaxYawnSequence(analysis, testCase);
    testCheckmateOrStalemate(analysis, testCase);
    testRepetitionCountFinalPosition(analysis, testCase);
    testInsufficientMaterial(analysis, testCase);
    testWinnableNotHavingMove(analysis, testCase);
    testWinnableHavingMove(analysis, testCase);
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

  private static void testSequenceRepetition(Analysis analysis, PgnFileTestCase testCase) {
    if (!Analyzer.IS_CALCULATE_SEQUENCE_REPETITION) {
      return;
    }
    if (testCase.expectedSequenceRepetition() == PgnTestConstants.SEQUENCE_REPETITION_NOT_DEFINED) {
      logger.error("Expected sequence repetition for test case " + testCase.pgnFileName() + " was not yet defined");
      throw new IllegalArgumentException("Please define expected values before running the tests");
    }
    final List<String> calculatedPositionSequenceList = Analyzer
        .calculateSequenceRepetitionRepresentation(analysis.sequenceRepetitionList());
    assertEquals(testCase.expectedSequenceRepetition().size(), calculatedPositionSequenceList.size());
    for (var i = 0; i < calculatedPositionSequenceList.size(); i++) {
      assertEquals(testCase.expectedSequenceRepetition().get(i), calculatedPositionSequenceList.get(i));
    }
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

  private static void testWinnableNotHavingMove(Analysis analysis, PgnFileTestCase testCase) {
    testSide(testCase.unwinnableNotHavingMove(), analysis.unwinnableQuickResultNotHavingMove());
  }

  private static void testWinnableHavingMove(Analysis analysis, PgnFileTestCase testCase) {
    testSide(testCase.unwinnableHavingMove(), analysis.unwinnableQuickResultHavingMove());
  }

  private static void testSide(UnwinnableFullResultTest unwinnableFullResultTestExpected,
      UnwinnableQuick unwinnableQuickResult) {
    switch (unwinnableFullResultTestExpected) {
      case UNWINNABLE:
        assertEquals(UnwinnableQuick.UNWINNABLE, unwinnableQuickResult);
        break;
      case UNWINNABLE_QUICK_DOES_NOT_SEE:
        assertEquals(UnwinnableQuick.POSSIBLY_WINNABLE, unwinnableQuickResult);
        break;
      case WINNABLE:
        final var isIncomplete = unwinnableQuickResult == UnwinnableQuick.WINNABLE
            || unwinnableQuickResult == UnwinnableQuick.POSSIBLY_WINNABLE;
        assertTrue(isIncomplete);
        break;
      default:
        throw new IllegalArgumentException();
    }
  }
}
