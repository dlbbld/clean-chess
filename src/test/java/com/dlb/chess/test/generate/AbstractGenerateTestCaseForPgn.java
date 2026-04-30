package com.dlb.chess.test.generate;

import java.nio.file.Path;

import com.dlb.chess.analysis.Analyzer;
import com.dlb.chess.analysis.enums.CheckmateOrStalemate;
import com.dlb.chess.analysis.model.Analysis;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.enums.EnPassantCaptureRuleThreefold;
import com.dlb.chess.common.enums.InsufficientMaterial;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.test.analysis.representation.RepetitionRepresentation;
import com.dlb.chess.test.analysis.representation.YawnRepresentation;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.unwinnability.full.UnwinnableFullAnalyzer;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;
import com.dlb.chess.unwinnability.quick.UnwinnableQuickAnalyzer;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuick;

public abstract class AbstractGenerateTestCaseForPgn {

  static String generate(Path pgnFolderPath, String pgnFileName) throws Exception {

    final Analysis analysis = Analyzer.calculateAnalysis(pgnFolderPath, pgnFileName);

    final StringBuilder result = new StringBuilder();
    result.append("list.add(new ").append(PgnFileTestCase.class.getSimpleName()).append("(");

    // begin values
    result.append("\"");
    result.append(pgnFileName);
    result.append("\"");
    result.append(", ");

    final String repetition = RepetitionRepresentation.calculateRepresentationRepetitionAnalysis(analysis,
        EnPassantCaptureRuleThreefold.DO_NOT_IGNORE);
    result.append("\"");
    result.append(repetition);
    result.append("\"");
    result.append(", ");

    final String repetitionInitialEnPassantCapture = RepetitionRepresentation.calculateRepresentationRepetitionAnalysis(analysis,
        EnPassantCaptureRuleThreefold.DO_IGNORE);
    result.append("\"");
    result.append(repetitionInitialEnPassantCapture);
    result.append("\"");
    result.append(", ");

    final String yawnMove = YawnRepresentation.calculateRepresentationYawnMoveListList(analysis.yawnMoveListList());
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

    final ApiBoard finalBoard = analysis.board();
    final UnwinnableFull unwinnableFullWhite = UnwinnableFullAnalyzer.unwinnableFull(finalBoard, Side.WHITE)
        .unwinnableFull();
    final UnwinnableFull unwinnableFullBlack = UnwinnableFullAnalyzer.unwinnableFull(finalBoard, Side.BLACK)
        .unwinnableFull();
    final UnwinnableQuick unwinnableQuickWhite = UnwinnableQuickAnalyzer.unwinnableQuick(finalBoard, Side.WHITE);
    final UnwinnableQuick unwinnableQuickBlack = UnwinnableQuickAnalyzer.unwinnableQuick(finalBoard, Side.BLACK);

    result.append(UnwinnableFull.class.getSimpleName()).append(".").append(unwinnableFullWhite.name()).append(", ");
    result.append(UnwinnableFull.class.getSimpleName()).append(".").append(unwinnableFullBlack.name()).append(", ");
    result.append(UnwinnableQuick.class.getSimpleName()).append(".").append(unwinnableQuickWhite.name()).append(", ");
    result.append(UnwinnableQuick.class.getSimpleName()).append(".").append(unwinnableQuickBlack.name()).append(", ");

    final var fen = analysis.fen();

    result.append("\"");
    result.append(fen);
    result.append("\"");
    // end values

    result.append("));");

    return NonNullWrapperCommon.toString(result);
  }

}
