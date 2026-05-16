package com.dlb.chess.test.generate;

import java.nio.file.Path;

import com.dlb.chess.common.Nulls;
import com.dlb.chess.common.enums.InsufficientMaterial;
import com.dlb.chess.report.CheckmateOrStalemate;
import com.dlb.chess.report.Report;
import com.dlb.chess.report.Reporter;
import com.dlb.chess.test.model.PgnTestCase;
import com.dlb.chess.test.report.representation.NoProgressRepresentation;
import com.dlb.chess.test.report.representation.RepetitionRepresentation;

public abstract class AbstractGenerateTestCaseForPgn {

  static String generate(Path pgnFolderPath, String pgnFileName) throws Exception {

    final Report report = Reporter.calculateReport(pgnFolderPath, pgnFileName);

    final StringBuilder result = new StringBuilder();
    result.append("list.add(new ").append(PgnTestCase.class.getSimpleName()).append("(");

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

    final var fen = report.fen();

    result.append("\"");
    result.append(fen);
    result.append("\"");
    // end values

    result.append("));");

    return Nulls.toString(result);
  }

}
