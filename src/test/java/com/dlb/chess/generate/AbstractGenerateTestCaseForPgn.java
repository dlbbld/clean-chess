package com.dlb.chess.generate;

import java.nio.file.Path;

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

  static String generate(Path pgnFolderPath, String pgnFileName) throws Exception {

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

    result.append(analysis.board().getRepetitionCount());
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
    final UnwinnableFull unwinnableFullResultWhite = analysis.unwinnableFullWhite();
    result.append(UnwinnableFull.class.getSimpleName());
    result.append(".");
    result.append(unwinnableFullResultWhite.name());
    result.append(", ");

    final UnwinnableFull unwinnableFullResultBlack = analysis.unwinnableFullBlack();
    result.append(UnwinnableFull.class.getSimpleName());
    result.append(".");
    result.append(unwinnableFullResultBlack.name());
    result.append(", ");

    final UnwinnableQuick unwinnableQuickResultWhite = analysis.unwinnableQuickWhite();
    result.append(UnwinnableQuick.class.getSimpleName());
    result.append(".");
    result.append(unwinnableQuickResultWhite.name());
    result.append(", ");

    final UnwinnableQuick unwinnableQuickResultBlack = analysis.unwinnableQuickBlack();
    result.append(UnwinnableQuick.class.getSimpleName());
    result.append(".");
    result.append(unwinnableQuickResultBlack.name());
    result.append(", ");

    result.append("\"");
    result.append(fen);
    result.append("\"");
    // end values

    result.append("));");

    return NonNullWrapperCommon.toString(result);
  }

}
