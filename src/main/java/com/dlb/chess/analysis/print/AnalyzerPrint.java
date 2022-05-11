package com.dlb.chess.analysis.print;

import java.util.ArrayList;
import java.util.List;

import com.dlb.chess.analysis.Analyzer;
import com.dlb.chess.analysis.model.Analysis;
import com.dlb.chess.analysis.model.YawnHalfMove;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.enums.DeadPosition;
import com.dlb.chess.common.enums.EnPassantCaptureRuleThreefold;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.HalfMove;
import com.dlb.chess.common.utility.GeneralUtility;
import com.dlb.chess.common.utility.RepetitionUtility;
import com.dlb.chess.common.utility.YawnMoveUtility;
import com.dlb.chess.internationalization.Message;
import com.dlb.chess.pgn.reader.PgnReader;
import com.dlb.chess.pgn.reader.enums.ResultTagValue;
import com.dlb.chess.pgn.reader.model.PgnFile;

//for class organization only, keep methods protected as already used as delegate in main class Analysis
public class AnalyzerPrint {

  private static final int REPETITION_COUNT_THRESHOLD = 2;
  private static final int YAWN_HALF_MOVE_COUNT_THRESHOLD = 50;

  protected static void printAnalysis(String pgn) throws Exception {

    final List<String> lines = NonNullWrapperCommon.asList(NonNullWrapperCommon.split(pgn, "\\n"));

    final PgnFile pgnFile = PgnReader.readPgn(lines);

    final ApiBoard board = GeneralUtility.calculateChessBoard(pgnFile);
    printAnalysis(board);
  }

  protected static void printAnalysis(String folderPath, String pgnFileName) throws Exception {

    final ApiBoard board = GeneralUtility.calculateChessBoard(folderPath, pgnFileName);
    printAnalysis(board);
  }

  protected static void printAnalysis(ApiBoard board) throws Exception {
    final Analysis analysis = Analyzer.calculateAnalysis(board);

    final List<String> output = new ArrayList<>();

    // repetition
    final List<List<HalfMove>> repetitionListList = RepetitionUtility.calculateRepetitionListList(
        board.getHalfMoveList(), REPETITION_COUNT_THRESHOLD, EnPassantCaptureRuleThreefold.DO_NOT_IGNORE);
    addMainSection(output, "analysis.repetition.sequence.title");
    if (repetitionListList.isEmpty()) {
      output.add(Message.getString("analysis.repetition.sequence.none"));
    } else {
      final var listChronic = RepetitionPrint.calculateOutputRepetitionChronlogically(repetitionListList);
      output.add(listChronic);
    }

    addMainSection(output, "analysis.repetition.threefold.title");
    if (analysis.hasThreefoldRepetition()) {
      output.add(Message.getString("analysis.repetition.threefold.yes"));
    } else {
      output.add(Message.getString("analysis.repetition.threefold.no"));
    }

    addMainSection(output, "analysis.repetition.fivefold.title");
    if (analysis.hasFivefoldRepetition()) {
      output.add(Message.getString("analysis.repetition.fivefold.yes"));
    } else {
      output.add(Message.getString("analysis.repetition.fivefold.no"));
    }

    // yawn move
    final List<List<YawnHalfMove>> yawnMoveListList = YawnMoveUtility.calculateYawnMoveRule(board,
        YAWN_HALF_MOVE_COUNT_THRESHOLD);
    addMainSection(output, "analysis.yawnmove.sequence.title");
    if (yawnMoveListList.isEmpty()) {
      output.add(Message.getString("analysis.yawnmove.sequence.none"));
    } else {
      final var list = YawnPrint.calculateOutputYawnMoveListList(yawnMoveListList);
      output.addAll(list);
    }

    addMainSection(output, "analysis.yawnmove.fiftyMoves.title");
    if (analysis.hasFiftyMoveRule()) {
      output.add(Message.getString("analysis.yawnmove.fiftyMoves.yes"));
    } else {
      output.add(Message.getString("analysis.yawnmove.fiftyMoves.no"));
    }

    addMainSection(output, "analysis.yawnmove.seventyFiveMoves.title");
    if (analysis.hasSeventyFiveMoveRule()) {
      output.add(Message.getString("analysis.yawnmove.seventyFiveMoves.yes"));
    } else {
      output.add(Message.getString("analysis.yawnmove.seventyFiveMoves.no"));
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
