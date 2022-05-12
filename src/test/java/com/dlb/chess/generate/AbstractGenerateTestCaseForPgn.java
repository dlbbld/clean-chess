package com.dlb.chess.generate;

import com.dlb.chess.analysis.Analyzer;
import com.dlb.chess.analysis.enums.CheckmateOrStalemate;
import com.dlb.chess.analysis.model.Analysis;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.enums.EnPassantCaptureRuleThreefold;
import com.dlb.chess.common.enums.InsufficientMaterial;
import com.dlb.chess.test.analysis.output.RepetitionOutput;
import com.dlb.chess.test.analysis.output.YawnOutput;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.pgntest.enums.UnwinnableFullResultTest;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuickResult;

public abstract class AbstractGenerateTestCaseForPgn {

  static String generate(String pgnFolderPath, String pgnFileName) throws Exception {

    final Analysis analysis = Analyzer.calculateAnalysis(pgnFolderPath, pgnFileName);

    final StringBuilder result = new StringBuilder();
    result.append("list.add(new " + PgnFileTestCase.class.getSimpleName() + "(");

    // begin values
    result.append("\"");
    result.append(pgnFileName);
    result.append("\"");
    result.append(", ");

    final String repetition = RepetitionOutput.calculateOutputRepetitionAnalysis(analysis,
        EnPassantCaptureRuleThreefold.DO_NOT_IGNORE);
    result.append("\"");
    result.append(repetition);
    result.append("\"");
    result.append(", ");

    final String repetitionInitialEnPassantCapture = RepetitionOutput.calculateOutputRepetitionAnalysis(analysis,
        EnPassantCaptureRuleThreefold.DO_IGNORE);
    result.append("\"");
    result.append(repetitionInitialEnPassantCapture);
    result.append("\"");
    result.append(", ");

    final String yawnMove = YawnOutput.calculateOutputYawnMoveListList(analysis.yawnMoveListList());
    result.append("\"");
    result.append(yawnMove);
    result.append("\"");
    result.append(", ");

    // TODO clean up sequence - make ok or remove (then add later and better if needed)!!!
    final var sequenceRepetition = "BLANK_ESRL";
    result.append(sequenceRepetition);
    result.append(", ");

    final var firstCapture = analysis.firstCapture();
    result.append(firstCapture);
    result.append(", ");

    final var maxYawnSequence = analysis.maxYawnSequence();
    result.append(maxYawnSequence);
    result.append(", ");

    final CheckmateOrStalemate lastPositionEvaluation = analysis.lastPositionEvaluation();
    result.append(CheckmateOrStalemate.class.getSimpleName());
    result.append(".");
    result.append(lastPositionEvaluation.name());
    result.append(", ");

    final InsufficientMaterial insufficientMaterial = analysis.insufficientMaterial();
    result.append(InsufficientMaterial.class.getSimpleName());
    result.append(".");
    result.append(insufficientMaterial.name());
    result.append(", ");

    final var fen = analysis.fen();
    final UnwinnableQuickResult unwinnableQuickResultNotHavingMove = analysis.unwinnableQuickResultNotHavingMove();

    result.append(UnwinnableFullResultTest.class.getSimpleName());
    result.append(".");
    UnwinnableFullResultTest unwinnableFullResultTest = switch (unwinnableQuickResultNotHavingMove) {
      case UNWINNABLE -> UnwinnableFullResultTest.UNWINNABLE;
      case WINNABLE -> UnwinnableFullResultTest.WINNABLE;
      case POSSIBLY_WINNABLE -> UnwinnableFullResultTest.WINNABLE;
      default -> throw new IllegalArgumentException();
    };
    result.append(unwinnableFullResultTest.name());
    result.append(", ");

    result.append("\"");
    result.append(fen);
    result.append("\"");
    // end values

    result.append("));");

    return NonNullWrapperCommon.toString(result);
  }

}
