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
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuick;

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

    final CheckmateOrStalemate lastPositionEvaluation = analysis.checkmateOrStalemate();
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

    // we use the quick for the full so it does not take too long
    // we take into account of introducing some rare errors here
    // they will be detected on periodic checks
    final UnwinnableQuick unwinnableQuickResultHavingMove = analysis.unwinnableQuickHavingMove();
    result.append(UnwinnableFull.class.getSimpleName());
    result.append(".");
    result.append(approximateFullFromQuick(unwinnableQuickResultHavingMove).name());
    result.append(", ");

    final UnwinnableQuick unwinnableQuickResultNotHavingMove = analysis.unwinnableQuickNotHavingMove();
    result.append(UnwinnableFull.class.getSimpleName());
    result.append(".");
    result.append(approximateFullFromQuick(unwinnableQuickResultNotHavingMove).name());
    result.append(", ");

    result.append(UnwinnableQuick.class.getSimpleName());
    result.append(".");
    result.append(unwinnableQuickResultHavingMove.name());
    result.append(", ");

    result.append(UnwinnableQuick.class.getSimpleName());
    result.append(".");
    result.append(unwinnableQuickResultNotHavingMove.name());
    result.append(", ");

    result.append("\"");
    result.append(fen);
    result.append("\"");
    // end values

    result.append("));");

    return NonNullWrapperCommon.toString(result);
  }

  private static UnwinnableFull approximateFullFromQuick(UnwinnableQuick quick) {
    switch (quick) {
      case UNWINNABLE:
        return UnwinnableFull.UNWINNABLE;
      case WINNABLE:
      case POSSIBLY_WINNABLE:
        return UnwinnableFull.UNWINNABLE;
      default:
        throw new IllegalArgumentException();
    }

  }
}
