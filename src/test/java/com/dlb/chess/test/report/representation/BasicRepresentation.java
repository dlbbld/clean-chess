package com.dlb.chess.test.report.representation;

import java.util.ArrayList;
import java.util.List;

import com.dlb.chess.common.enums.EnPassantCaptureRuleThreefold;
import com.dlb.chess.common.model.HalfMove;
import com.dlb.chess.common.utility.RepetitionUtility;
import com.dlb.chess.report.model.Report;

public class BasicRepresentation {

  private static final String ATTRIBUTE_FEN = "FEN";
  private static final String ATTRIBUTE_THREEFOLD_IGNORING_EN_PASSANT_CAPTURE_YES_NO = "Threefold ignoring en passant";
  private static final String ATTRIBUTE_THREEFOLD_NOT_IGNORING_EN_PASSANT_CAPTURE_YES_NO = "Threefold";

  private static final String ATTRIBUTE_THREEFOLD_IGNORING_EN_PASSANT_CAPTURE_SEQUENCE = "Sequence";
  private static final String ATTRIBUTE_THREEFOLD_NOT_IGNORING_EN_PASSANT_CAPTURE_SEQUENCE = "Sequence";

  private static final String ATTRIBUTE_FIVEFOLD_YES_NO = "Fivefold";

  private static final String ATTRIBUTE_YAWN_MOVE_RULE_YES_NO = "Yawn-move rule";
  private static final String ATTRIBUTE_YAWN_MOVE_RULE_SEQUENCE = "Sequence";

  private static final String ATTRIBUTE_FIRST_CAPTURE = "First capture";
  private static final String ATTRIBUTE_MAX_YAWN_SEQUENCE = "Max yawn sequence";
  private static final String ATTRIBUTE_BOARD_RESULT_NAME = "Board result";
  private static final String ATTRIBUTE_INSUFFICIENT_MATERIAL = "Insufficient material";

  private static final String ATTRIBUTE_VALUE_NA = "Na";

  public static List<String> calculateRepresentation(Report report, String pgnName) throws Exception {
    final List<String> list = new ArrayList<>();

    list.add("-----------------------------------------");
    list.add(pgnName);
    list.add("-----------------------------------------");

    list.add(calculateOutputFen(report));

    list.add(calculateOutputThreefoldRepetitionInitialEnPassantCapture(report));
    list.add(calculateOutputRepetitionInitialEnPassantCapture(report));

    list.add(calculateOutputThreefoldRepetition(report));

    if (report.hasThreefoldRepetition()) {
      list.add(calculateOutputRepetition(report));
      list.add(calculateOutputFivefoldRepetition(report));
      if (report.hasFivefoldRepetition()) {
        list.add(calculateOutputFivefoldRepetition(report));
      }
    }

    list.add(calculateOutputYawnMoveRule(report));

    if (report.hasFiftyMoveRule()) {
      list.add(calculateOutputYawnMoveRuleSequence(report));
    }

    list.add(calculateOutputFirstCapture(report));
    list.add(calculateOutputMaxYawnSequence(report));

    list.add(calculateOutputLastPositionEvaluation(report));
    list.add(calculateOutputInsufficientMaterial(report));

    return list;
  }

  private static String calculateOutputMaxYawnSequence(Report report) {
    return calculateOutput(ATTRIBUTE_MAX_YAWN_SEQUENCE, report.maxYawnSequence());
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

  private static String calculateOutputThreefoldRepetitionInitialEnPassantCapture(Report report) {
    return calculateOutput(ATTRIBUTE_THREEFOLD_IGNORING_EN_PASSANT_CAPTURE_YES_NO,
        report.hasThreefoldRepetitionInitialEnPassantCapture());
  }

  private static String calculateOutputFivefoldRepetition(Report report) {
    return calculateOutput(ATTRIBUTE_FIVEFOLD_YES_NO, report.hasFivefoldRepetition());
  }

  private static String calculateOutputRepetition(Report report) {
    return calculateOutputRepetitionType(report, EnPassantCaptureRuleThreefold.DO_NOT_IGNORE);
  }

  private static String calculateOutputRepetitionInitialEnPassantCapture(Report report) {
    return calculateOutputRepetitionType(report, EnPassantCaptureRuleThreefold.DO_IGNORE);
  }

  private static String calculateOutputYawnMoveRule(Report report) {
    return calculateOutput(ATTRIBUTE_YAWN_MOVE_RULE_YES_NO, report.hasFiftyMoveRule());
  }

  private static String calculateOutputYawnMoveRuleSequence(Report report) {
    final String attribute = ATTRIBUTE_YAWN_MOVE_RULE_SEQUENCE;
    if (!report.hasFiftyMoveRule()) {
      return calculateOutput(attribute, ATTRIBUTE_VALUE_NA);
    }
    final String repetition = YawnRepresentation.calculateRepresentationYawnMoveListList(report.yawnMoveListList());
    return calculateOutput(attribute, repetition);
  }

  private static String calculateOutputThreefoldRepetition(Report report) {
    return calculateOutput(ATTRIBUTE_THREEFOLD_NOT_IGNORING_EN_PASSANT_CAPTURE_YES_NO,
        report.hasThreefoldRepetition());
  }

  private static String calculateOutputRepetitionType(Report report,
      EnPassantCaptureRuleThreefold enPassantCaptureRule) {
    final String attributeName = calculateRepetitionAttributeSequence(enPassantCaptureRule);

    final List<List<HalfMove>> repetitionList = RepetitionUtility.getRepetitionListListType(report,
        enPassantCaptureRule);
    if (repetitionList.isEmpty()) {
      return calculateOutput(attributeName, BasicRepresentation.ATTRIBUTE_VALUE_NA);
    }
    final String representation = RepetitionRepresentation.calculateRepresentationRepetitionListList(repetitionList,
        enPassantCaptureRule);
    return calculateOutput(attributeName, representation);
  }

  private static String calculateRepetitionAttributeSequence(EnPassantCaptureRuleThreefold enPassantCaptureRule) {
    return switch (enPassantCaptureRule) {
      case DO_IGNORE -> ATTRIBUTE_THREEFOLD_IGNORING_EN_PASSANT_CAPTURE_SEQUENCE;
      case DO_NOT_IGNORE -> ATTRIBUTE_THREEFOLD_NOT_IGNORING_EN_PASSANT_CAPTURE_SEQUENCE;
      default -> throw new IllegalArgumentException();
    };
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
