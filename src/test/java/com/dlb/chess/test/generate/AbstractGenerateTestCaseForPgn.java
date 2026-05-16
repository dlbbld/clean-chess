package com.dlb.chess.test.generate;

import java.nio.file.Path;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.Nulls;
import com.dlb.chess.common.enums.InsufficientMaterial;
import com.dlb.chess.report.CheckmateOrStalemate;
import com.dlb.chess.report.Report;
import com.dlb.chess.report.Reporter;
import com.dlb.chess.test.model.PgnFileTestCase;
import com.dlb.chess.test.report.representation.NoProgressRepresentation;
import com.dlb.chess.test.report.representation.RepetitionRepresentation;
import com.dlb.chess.unwinnability.UnwinnabilityFullVerdict;
import com.dlb.chess.unwinnability.UnwinnabilityQuickVerdict;
import com.dlb.chess.unwinnability.UnwinnableFullAnalyzer;
import com.dlb.chess.unwinnability.UnwinnableQuickAnalyzer;

public abstract class AbstractGenerateTestCaseForPgn {

  static String generate(Path pgnFolderPath, String pgnFileName) throws Exception {

    final Report report = Reporter.calculateReport(pgnFolderPath, pgnFileName);

    final StringBuilder result = new StringBuilder();
    result.append("list.add(new ").append(PgnFileTestCase.class.getSimpleName()).append("(");

    // begin values
    result.append("\"");
    result.append(pgnFileName);
    result.append("\"");
    result.append(", ");

    final String repetition = RepetitionRepresentation.calculateRepresentationRepetitionReport(report);
    result.append("\"");
    result.append(repetition);
    result.append("\"");
    result.append(", ");

    final String noProgressMove = NoProgressRepresentation
        .calculateRepresentationNoProgressMoveListList(report.noProgressMoveListList());
    result.append("\"");
    result.append(noProgressMove);
    result.append("\"");
    result.append(", ");

    final var firstCapture = report.firstCapture();
    result.append(firstCapture);
    result.append(", ");

    final var maxNoProgressSequence = report.maxNoProgressSequence();
    result.append(maxNoProgressSequence);
    result.append(", ");

    final CheckmateOrStalemate lastPositionEvaluation = report.checkmateOrStalemate();
    result.append(CheckmateOrStalemate.class.getSimpleName());
    result.append(".");
    result.append(lastPositionEvaluation.name());
    result.append(", ");

    result.append(report.board().getRepetitionCount());
    result.append(", ");

    final InsufficientMaterial insufficientMaterial = report.insufficientMaterial();
    result.append(InsufficientMaterial.class.getSimpleName());
    result.append(".");
    result.append(insufficientMaterial.name());
    result.append(", ");

    final Board finalBoard = report.board();
    final UnwinnabilityFullVerdict unwinnableFullWhite = UnwinnableFullAnalyzer.unwinnableFull(finalBoard, Side.WHITE)
        .verdict();
    final UnwinnabilityFullVerdict unwinnableFullBlack = UnwinnableFullAnalyzer.unwinnableFull(finalBoard, Side.BLACK)
        .verdict();
    final UnwinnabilityQuickVerdict unwinnableQuickWhite = UnwinnableQuickAnalyzer.unwinnableQuick(finalBoard,
        Side.WHITE);
    final UnwinnabilityQuickVerdict unwinnableQuickBlack = UnwinnableQuickAnalyzer.unwinnableQuick(finalBoard,
        Side.BLACK);

    result.append(UnwinnabilityFullVerdict.class.getSimpleName()).append(".").append(unwinnableFullWhite.name())
        .append(", ");
    result.append(UnwinnabilityFullVerdict.class.getSimpleName()).append(".").append(unwinnableFullBlack.name())
        .append(", ");
    result.append(UnwinnabilityQuickVerdict.class.getSimpleName()).append(".").append(unwinnableQuickWhite.name())
        .append(", ");
    result.append(UnwinnabilityQuickVerdict.class.getSimpleName()).append(".").append(unwinnableQuickBlack.name())
        .append(", ");

    final var fen = report.fen();

    result.append("\"");
    result.append(fen);
    result.append("\"");
    // end values

    result.append("));");

    return Nulls.toString(result);
  }

}
