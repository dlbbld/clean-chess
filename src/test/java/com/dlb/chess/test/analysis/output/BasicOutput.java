package com.dlb.chess.test.analysis.output;

import java.util.ArrayList;
import java.util.List;

import com.dlb.chess.analysis.Analyzer;
import com.dlb.chess.analysis.model.Analysis;
import com.dlb.chess.analysis.model.SingleOutput;
import com.dlb.chess.common.enums.EnPassantCaptureRuleThreefold;
import com.dlb.chess.common.model.HalfMove;
import com.dlb.chess.common.utility.RepetitionUtility;

public class BasicOutput {

  private static final boolean IS_FULL = true;

  private static final String ATTRIBUTE_FEN = "FEN";
  private static final String ATTRIBUTE_THREEFOLD_IGNORING_EN_PASSANT_CAPTURE_YES_NO = "Threefold ignoring en passant";
  private static final String ATTRIBUTE_THREEFOLD_NOT_IGNORING_EN_PASSANT_CAPTURE_YES_NO = "Threefold";

  private static final String ATTRIBUTE_THREEFOLD_IGNORING_EN_PASSANT_CAPTURE_SEQUENCE = "Sequence";
  private static final String ATTRIBUTE_THREEFOLD_NOT_IGNORING_EN_PASSANT_CAPTURE_SEQUENCE = "Sequence";

  private static final String ATTRIBUTE_FIVEFOLD_YES_NO = "Fivefold";

  private static final String ATTRIBUTE_YAWN_MOVE_RULE_YES_NO = "Yawn-move rule";
  private static final String ATTRIBUTE_YAWN_MOVE_RULE_SEQUENCE = "Sequence";

  private static final String ATTRIBUTE_SEQUENCE_REPETITION_YES_NO = "Sequence repetition";

  private static final String ATTRIBUTE_FIRST_CAPTURE = "First capture";
  private static final String ATTRIBUTE_MAX_YAWN_SEQUENCE = "Max yawn sequence";
  private static final String ATTRIBUTE_BOARD_RESULT_NAME = "Board result";
  private static final String ATTRIBUTE_INSUFFICIENT_MATERIAL = "Insufficient material";
  private static final String ATTRIBUTE_UNWINNABLE_QUICK_NOT_HAVING_MOVE = "Winnable not having the move";

  private static final String ATTRIBUTE_VALUE_NA = "Na";

  public static SingleOutput calculateTestResult(String folderPath, String pgnFileName) throws Exception {
    final var analysis = Analyzer.calculateAnalysis(folderPath, pgnFileName);
    return calculateTestResult(analysis, pgnFileName);
  }

  public static SingleOutput calculateTestResult(Analysis analysis, String pgnName) throws Exception {
    final List<String> output = new ArrayList<>();

    output.add("-----------------------------------------");
    output.add(pgnName);
    output.add("-----------------------------------------");

    if (IS_FULL) {
      output.add(calculateOutputFen(analysis));
    }

    if (IS_FULL) {
      output.add(calculateOutputThreefoldRepetitionInitialEnPassantCapture(analysis));
      output.add(calculateOutputRepetitionInitialEnPassantCapture(analysis));
    }

    output.add(calculateOutputThreefoldRepetition(analysis));

    if (analysis.hasThreefoldRepetition()) {
      output.add(calculateOutputRepetition(analysis));
      output.add(calculateOutputFivefoldRepetition(analysis));
      if (analysis.hasFivefoldRepetition()) {
        output.add(calculateOutputFivefoldRepetition(analysis));
      }
    }

    output.add(calculateOutputYawnMoveRule(analysis));

    if (analysis.hasFiftyMoveRule()) {
      output.add(calculateOutputYawnMoveRuleSequence(analysis));
    }

    if (IS_FULL) {
      output.add(calculateOutputSequenceRepetition(analysis));
    }

    if (IS_FULL) {
      output.add(calculateOutputFirstCapture(analysis));
      output.add(calculateOutputMaxYawnSequence(analysis));
    }

    if (IS_FULL) {
      output.add(calculateOutputLastPositionEvaluation(analysis));
      output.add(calculateOutputWinnableNotHavingMove(analysis));
      output.add(calculateOutputInsufficientMaterial(analysis));
    }

    return new SingleOutput(analysis, output);
  }

  private static String calculateOutputMaxYawnSequence(Analysis analysis) {
    return calculateOutput(ATTRIBUTE_MAX_YAWN_SEQUENCE, analysis.maxYawnSequence());
  }

  private static String calculateOutputLastPositionEvaluation(Analysis analysis) {
    return calculateOutput(ATTRIBUTE_BOARD_RESULT_NAME, analysis.lastPositionEvaluation().getDescription());
  }

  private static String calculateOutputInsufficientMaterial(Analysis analysis) {
    return calculateOutput(ATTRIBUTE_INSUFFICIENT_MATERIAL, analysis.insufficientMaterial().getDescription());
  }

  private static String calculateOutputWinnableNotHavingMove(Analysis analysis) {
    return calculateOutput(ATTRIBUTE_UNWINNABLE_QUICK_NOT_HAVING_MOVE, analysis.unwinnableQuickResultNotHavingMove().getDescription());
  }

  private static String calculateOutputFirstCapture(Analysis analysis) {
    final String attribute = ATTRIBUTE_FIRST_CAPTURE;
    if (analysis.firstCapture() == 0) {
      return calculateOutput(attribute, " none");
    }
    return calculateOutput(attribute, analysis.firstCapture());
  }

  private static String calculateOutputFen(Analysis analysis) {
    return calculateOutput(ATTRIBUTE_FEN, analysis.fen());
  }

  private static String calculateOutputThreefoldRepetitionInitialEnPassantCapture(Analysis analysis) {
    return calculateOutput(ATTRIBUTE_THREEFOLD_IGNORING_EN_PASSANT_CAPTURE_YES_NO,
        analysis.hasThreefoldRepetitionInitialEnPassantCapture());
  }

  private static String calculateOutputFivefoldRepetition(Analysis analysis) {
    return calculateOutput(ATTRIBUTE_FIVEFOLD_YES_NO, analysis.hasFivefoldRepetition());
  }

  private static String calculateOutputRepetition(Analysis analysis) {
    return calculateOutputRepetitionType(analysis, EnPassantCaptureRuleThreefold.DO_NOT_IGNORE);
  }

  private static String calculateOutputRepetitionInitialEnPassantCapture(Analysis analysis) {
    return calculateOutputRepetitionType(analysis, EnPassantCaptureRuleThreefold.DO_IGNORE);
  }

  private static String calculateOutputYawnMoveRule(Analysis analysis) {
    return calculateOutput(ATTRIBUTE_YAWN_MOVE_RULE_YES_NO, analysis.hasFiftyMoveRule());
  }

  private static String calculateOutputYawnMoveRuleSequence(Analysis analysis) {
    final String attribute = ATTRIBUTE_YAWN_MOVE_RULE_SEQUENCE;
    if (!analysis.hasFiftyMoveRule()) {
      return calculateOutput(attribute, ATTRIBUTE_VALUE_NA);
    }
    final String repetition = YawnOutput.calculateOutputYawnMoveListList(analysis.yawnMoveListList());
    return calculateOutput(attribute, repetition);
  }

  private static String calculateOutputSequenceRepetition(Analysis analysis) {
    final String attribute = ATTRIBUTE_SEQUENCE_REPETITION_YES_NO;
    if (!Analyzer.IS_CALCULATE_SEQUENCE_REPETITION) {
      return calculateOutput(attribute, "not included");
    }

    return calculateOutput(attribute, analysis.hasThreeSequenceRepetition());
  }

  private static String calculateOutputThreefoldRepetition(Analysis analysis) {
    return calculateOutput(ATTRIBUTE_THREEFOLD_NOT_IGNORING_EN_PASSANT_CAPTURE_YES_NO,
        analysis.hasThreefoldRepetition());
  }

  private static String calculateOutputRepetitionType(Analysis analysis,
      EnPassantCaptureRuleThreefold enPassantCaptureRule) {
    final String attributeName = calculateRepetitionAttributeSequence(enPassantCaptureRule);

    final List<List<HalfMove>> repetitionList = RepetitionUtility.getRepetitionListListType(analysis,
        enPassantCaptureRule);
    if (repetitionList.isEmpty()) {
      return calculateOutput(attributeName, BasicOutput.ATTRIBUTE_VALUE_NA);
    }
    final String representation = RepetitionOutput.calculateOutputRepetitionListList(repetitionList,
        enPassantCaptureRule);
    return calculateOutput(attributeName, representation);
  }

  private static String calculateRepetitionAttributeSequence(EnPassantCaptureRuleThreefold enPassantCaptureRule) {
    switch (enPassantCaptureRule) {
      case DO_IGNORE:
        return ATTRIBUTE_THREEFOLD_IGNORING_EN_PASSANT_CAPTURE_SEQUENCE;
      case DO_NOT_IGNORE:
        return ATTRIBUTE_THREEFOLD_NOT_IGNORING_EN_PASSANT_CAPTURE_SEQUENCE;
      default:
        throw new IllegalArgumentException();
    }
  }

  private static String calculateOutput(String attributeName, String attributeValue) {
    return attributeName + ": " + attributeValue;
  }

  private static String calculateOutput(String attributeName, boolean isYes) {
    if (isYes) {
      return attributeName + ": yes";
    }
    return attributeName + ": no";
  }

  private static String calculateOutput(String attributeName, int attributeValue) {
    return attributeName + ": " + attributeValue;
  }
}
