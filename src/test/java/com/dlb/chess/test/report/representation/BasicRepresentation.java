package com.dlb.chess.test.report.representation;

import java.util.ArrayList;
import java.util.List;

import com.dlb.chess.report.Report;

public class BasicRepresentation {

  private static final String ATTRIBUTE_FEN = "FEN";
  private static final String ATTRIBUTE_THREEFOLD_YES_NO = "Threefold";
  private static final String ATTRIBUTE_THREEFOLD_SEQUENCE = "Sequence";
  private static final String ATTRIBUTE_FIVEFOLD_YES_NO = "Fivefold";

  private static final String ATTRIBUTE_NO_PROGRESS_MOVE_RULE_YES_NO = "No progress rule";
  private static final String ATTRIBUTE_NO_PROGRESS_MOVE_RULE_SEQUENCE = "Sequence";

  private static final String ATTRIBUTE_FIRST_CAPTURE = "First capture";
  private static final String ATTRIBUTE_MAX_NO_PROGRESS_SEQUENCE = "Max no progress sequence";
  private static final String ATTRIBUTE_BOARD_RESULT_NAME = "Board result";
  private static final String ATTRIBUTE_INSUFFICIENT_MATERIAL = "Insufficient material";

  private static final String ATTRIBUTE_VALUE_NA = "Na";

  public static List<String> calculateRepresentation(Report report, String pgnName) throws Exception {
    final List<String> list = new ArrayList<>();

    list.add("-----------------------------------------");
    list.add(pgnName);
    list.add("-----------------------------------------");

    list.add(calculateOutputFen(report));

    list.add(calculateOutputThreefoldRepetition(report));

    if (report.hasThreefoldRepetition()) {
      list.add(calculateOutputRepetition(report));
      list.add(calculateOutputFivefoldRepetition(report));
      if (report.hasFivefoldRepetition()) {
        list.add(calculateOutputFivefoldRepetition(report));
      }
    }

    list.add(calculateOutputNoProgressMoveRule(report));

    if (report.hasFiftyMoveRule()) {
      list.add(calculateOutputNoProgressMoveRuleSequence(report));
    }

    list.add(calculateOutputFirstCapture(report));
    list.add(calculateOutputMaxNoProgressSequence(report));

    list.add(calculateOutputLastPositionEvaluation(report));
    list.add(calculateOutputInsufficientMaterial(report));

    return list;
  }

  private static String calculateOutputMaxNoProgressSequence(Report report) {
    return calculateOutput(ATTRIBUTE_MAX_NO_PROGRESS_SEQUENCE, report.maxNoProgressSequence());
  }

  private static String calculateOutputLastPositionEvaluation(Report report) {
    return calculateOutput(ATTRIBUTE_BOARD_RESULT_NAME, report.checkmateOrStalemate().getDescription());
  }

  private static String calculateOutputInsufficientMaterial(Report report) {
    return calculateOutput(ATTRIBUTE_INSUFFICIENT_MATERIAL, report.insufficientMaterial().getDescription());
  }

  private static String calculateOutputFirstCapture(Report report) {
    final String attribute = ATTRIBUTE_FIRST_CAPTURE;
    if (report.firstCapture() == 0) {
      return calculateOutput(attribute, " none");
    }
    return calculateOutput(attribute, report.firstCapture());
  }

  private static String calculateOutputFen(Report report) {
    return calculateOutput(ATTRIBUTE_FEN, report.fen());
  }

  private static String calculateOutputFivefoldRepetition(Report report) {
    return calculateOutput(ATTRIBUTE_FIVEFOLD_YES_NO, report.hasFivefoldRepetition());
  }

  private static String calculateOutputRepetition(Report report) {
    if (report.repetitionListList().isEmpty()) {
      return calculateOutput(ATTRIBUTE_THREEFOLD_SEQUENCE, ATTRIBUTE_VALUE_NA);
    }
    final String representation = RepetitionRepresentation
        .calculateRepresentationRepetitionListList(report.repetitionListList());
    return calculateOutput(ATTRIBUTE_THREEFOLD_SEQUENCE, representation);
  }

  private static String calculateOutputNoProgressMoveRule(Report report) {
    return calculateOutput(ATTRIBUTE_NO_PROGRESS_MOVE_RULE_YES_NO, report.hasFiftyMoveRule());
  }

  private static String calculateOutputNoProgressMoveRuleSequence(Report report) {
    final String attribute = ATTRIBUTE_NO_PROGRESS_MOVE_RULE_SEQUENCE;
    if (!report.hasFiftyMoveRule()) {
      return calculateOutput(attribute, ATTRIBUTE_VALUE_NA);
    }
    final String repetition = NoProgressRepresentation
        .calculateRepresentationNoProgressMoveListList(report.noProgressMoveListList());
    return calculateOutput(attribute, repetition);
  }

  private static String calculateOutputThreefoldRepetition(Report report) {
    return calculateOutput(ATTRIBUTE_THREEFOLD_YES_NO, report.hasThreefoldRepetition());
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
