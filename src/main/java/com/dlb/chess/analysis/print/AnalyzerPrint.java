package com.dlb.chess.analysis.print;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.analysis.Analyzer;
import com.dlb.chess.analysis.model.Analysis;
import com.dlb.chess.analysis.model.YawnHalfMove;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.ChessConstants;
import com.dlb.chess.common.enums.DeadPosition;
import com.dlb.chess.common.enums.EnPassantCaptureRuleThreefold;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.ClaimAhead;
import com.dlb.chess.common.model.HalfMove;
import com.dlb.chess.common.utility.GeneralUtility;
import com.dlb.chess.common.utility.RepetitionUtility;
import com.dlb.chess.common.utility.ThreefoldClaimAheadUtility;
import com.dlb.chess.common.utility.YawnMoveUtility;
import com.dlb.chess.internationalization.Message;
import com.dlb.chess.pgn.reader.PgnReader;
import com.dlb.chess.pgn.reader.enums.ResultTagValue;
import com.dlb.chess.pgn.reader.model.PgnFile;

//for class organization only, keep methods protected as already used as delegate in main class Analysis
public class AnalyzerPrint {

  private static final int REPETITION_COUNT_THRESHOLD = ChessConstants.THREEFOLD_REPETITION_RULE_THRESHOLD;
  private static final int YAWN_FULL_MOVE_COUNT_THRESHOLD = 25;

  private static final boolean IS_PRINT_THREEFOLD_YES_NO = false;
  private static final boolean IS_PRINT_SEVENTY_FIVE_MOVE_RULE_YES_NO = false;
  private static final boolean IS_PRINT_RESULT = false;

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

    final @NonNull List<String> output = new ArrayList<>();

    // repetition
    addFirstMainSection(output, "analysis.repetition.threefold.ahead.title");
    final List<List<ClaimAhead>> claimAheadListList = ThreefoldClaimAheadUtility
        .calculateThreefoldClaimAhead(board.getPerformedLegalMoveList(), board.getInitialFen());
    if (claimAheadListList.isEmpty()) {
      output.add(Message.getString("analysis.repetition.threefold.ahead.none"));
    } else {
      final var claimAheadList = ThreefoldClaimAheadPrint.calculateClaimAheadList(claimAheadListList);
      output.addAll(claimAheadList);
    }

    final List<List<HalfMove>> repetitionListList = RepetitionUtility.calculateRepetitionListList(
        board.getHalfMoveList(), REPETITION_COUNT_THRESHOLD, EnPassantCaptureRuleThreefold.DO_NOT_IGNORE);
    addMainSection(output, "analysis.repetition.threefold.list.title");
    if (repetitionListList.isEmpty()) {
      output.add(Message.getString("analysis.repetition.threefold.list.none"));
    } else {
      final var listChronic = RepetitionPrint.calculateOutputRepetitionChronlogically(repetitionListList);
      output.add(listChronic);
    }

    if (IS_PRINT_THREEFOLD_YES_NO) {
      addMainSection(output, "analysis.repetition.threefold.yesno.title");
      if (analysis.hasThreefoldRepetition()) {
        output.add(Message.getString("analysis.repetition.threefold.yesno.yes"));
      } else {
        output.add(Message.getString("analysis.repetition.threefold.yesno.no"));
      }

      addMainSection(output, "analysis.repetition.fivefold.yesno.title");
      if (analysis.hasFivefoldRepetition()) {
        output.add(Message.getString("analysis.repetition.fivefold.yesno.yes"));
      } else {
        output.add(Message.getString("analysis.repetition.fivefold.yesno.no"));
      }
    }

    // yawn move
    final List<List<YawnHalfMove>> yawnMoveListList = YawnMoveUtility.calculateYawnMoveRule(board,
        2 * YAWN_FULL_MOVE_COUNT_THRESHOLD);
    addMainSection(output, "analysis.yawnmove.sequence.title",
        NonNullWrapperCommon.valueOf(YAWN_FULL_MOVE_COUNT_THRESHOLD));
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

    if (IS_PRINT_SEVENTY_FIVE_MOVE_RULE_YES_NO) {
      addMainSection(output, "analysis.yawnmove.seventyFiveMoves.title");
      if (analysis.hasSeventyFiveMoveRule()) {
        output.add(Message.getString("analysis.yawnmove.seventyFiveMoves.yes"));
      } else {
        output.add(Message.getString("analysis.yawnmove.seventyFiveMoves.no"));
      }
    }

    if (IS_PRINT_RESULT) {
      // board result
      addMainSection(output, "analysis.board.result");

      final var boardResult = new StringBuilder();
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
    }

    printList(output);
  }

  private static void appendPoints(StringBuilder result, ResultTagValue resultTagValue) {
    result.append(" (");
    result.append(resultTagValue.getValue());
    result.append(")");
  }

  private static void addFirstMainSection(List<String> output, String key) {
    // add main section
    final StringBuilder mainSection = new StringBuilder();
    mainSection.append(Message.getString(key));
    mainSection.append(":");
    output.add(NonNullWrapperCommon.toString(mainSection));
  }

  private static void addFirstMainSection(List<String> output, String key, String placeHolder) {
    // add main section
    final StringBuilder mainSection = new StringBuilder();
    mainSection.append(Message.getString(key, placeHolder));
    mainSection.append(":");
    output.add(NonNullWrapperCommon.toString(mainSection));
  }

  private static void addMainSection(List<String> output, String key) {
    // add empty line before main section
    output.add("");

    // add main section
    addFirstMainSection(output, key);
  }

  private static void addMainSection(List<String> output, String key, String placeHolder) {
    // add empty line before main section
    output.add("");

    // add main section
    addFirstMainSection(output, key, placeHolder);
  }

  private static void printList(List<String> list) {
    for (final String line : list) {
      System.out.println(line);
    }
  }
}
