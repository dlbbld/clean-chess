package com.dlb.chess.analysis.print;

import java.util.ArrayList;
import java.util.List;

import com.dlb.chess.analysis.Analyzer;
import com.dlb.chess.analysis.model.Analysis;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.enums.DeadPosition;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.utility.GeneralUtility;
import com.dlb.chess.internationalization.Message;
import com.dlb.chess.pgn.reader.PgnReader;
import com.dlb.chess.pgn.reader.enums.ResultTagValue;
import com.dlb.chess.pgn.reader.model.PgnFile;

//for class organization only, keep methods protected as already used as delegate in main class Analysis
public class AnalyzerPrint {

  public static void printAnalysis(String pgn) throws Exception {

    final List<String> lines = NonNullWrapperCommon.asList(NonNullWrapperCommon.split(pgn, "\\n"));

    final PgnFile pgnFile = PgnReader.readPgn(lines);

    final ApiBoard board = GeneralUtility.calculateChessBoard(pgnFile);
    Analyzer.printAnalysis(board);
  }

  public static void printAnalysis(String folderPath, String pgnFileName) throws Exception {

    final ApiBoard board = GeneralUtility.calculateChessBoard(folderPath, pgnFileName);
    Analyzer.printAnalysis(board);
  }

  public static void printAnalysis(ApiBoard board) throws Exception {
    final Analysis analysis = Analyzer.calculateAnalysis(board);

    final List<String> output = new ArrayList<>();

    // repetition
    addMainSection(output, "analysis.threefold.list");
    if (analysis.repetitionListList().isEmpty()) {
      output.add(Message.getString("analysis.threefold.none"));
    } else {
      final var list = RepetitionPrint.calculateOutputRepetition(analysis.repetitionListList());
      output.addAll(list);

      addMainSection(output, "analysis.threefold.chronic");
      final var listChronic = RepetitionPrint.calculateOutputRepetitionChronlogically(analysis.repetitionListList());
      output.add(listChronic);
    }

    // yawn move
    addMainSection(output, "analysis.yawnmove.list");
    if (analysis.yawnMoveListList().isEmpty()) {
      output.add(Message.getString("analysis.yawnmove.none"));
    } else {
      final var list = YawnPrint.calculateOutputYawnMoveListList(analysis.yawnMoveListList());
      output.addAll(list);
    }

    // board result
    addMainSection(output, "analysis.board.result");

    final StringBuilder boardResult = new StringBuilder();
    if (board.isCheckmate()) {
      switch (board.getHavingMove()) {
        case BLACK:
          boardResult.append(Message.getString("analysis.board.checkmatebyWhite"));
          appendPoints(boardResult, ResultTagValue.WHITE_WON);
          break;
        case WHITE:
          boardResult.append(Message.getString("analysis.board.checkmatebyBlack"));
          appendPoints(boardResult, ResultTagValue.BLACK_WON);
          break;
        case NONE:
        default:
          throw new IllegalArgumentException();
      }
    } else if (board.isStalemate()) {
      boardResult.append(Message.getString("analysis.board.stalemate"));
      appendPoints(boardResult, ResultTagValue.DRAW);
    } else if (board.isFivefoldRepetition()) {
      boardResult.append(Message.getString("analysis.board.fivefoldRepetition"));
      appendPoints(boardResult, ResultTagValue.DRAW);
    } else if (board.isSeventyFiftyMove()) {
      boardResult.append(Message.getString("analysis.board.seventyFiveMoves"));
      appendPoints(boardResult, ResultTagValue.DRAW);
    } else if (board.isInsufficientMaterial()) {
      boardResult.append(Message.getString("analysis.board.insufficientMaterial"));
      appendPoints(boardResult, ResultTagValue.DRAW);
    } else if (board.isDeadPosition() == DeadPosition.YES) {
      boardResult.append(Message.getString("analysis.board.deadPosition"));
      appendPoints(boardResult, ResultTagValue.DRAW);
    } else {
      boardResult.append(Message.getString("analysis.board.ongoing"));
    }
    output.add(NonNullWrapperCommon.toString(boardResult));

    printList(output);
  }

  private static void appendPoints(StringBuilder result, ResultTagValue resultTagValue) {
    result.append(" (");
    result.append(resultTagValue.getValue());
    result.append(")");
  }

  private static void addMainSection(List<String> output, String key) {
    // add empty line before main section
    output.add("");

    // add main section
    final StringBuilder mainSection = new StringBuilder();
    mainSection.append(Message.getString(key));
    mainSection.append(":");
    output.add(NonNullWrapperCommon.toString(mainSection));
  }

  private static void printList(List<String> list) {
    for (final String line : list) {
      System.out.println(line);
    }
  }
}
