package com.dlb.chess.analysis.print;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.analysis.model.YawnHalfMove;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.ChessConstants;
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
import com.dlb.chess.pgn.reader.model.PgnFile;

//for class organization only, keep methods protected as already used as delegate in main class Analysis
public class AnalyzerPrint {

  private static final int REPETITION_COUNT_THRESHOLD = ChessConstants.THREEFOLD_REPETITION_RULE_THRESHOLD;
  private static final int YAWN_FULL_MOVE_COUNT_THRESHOLD = 25;

  protected static void printAnalysis(String pgn) {

    final List<String> lines = NonNullWrapperCommon.asList(NonNullWrapperCommon.split(pgn, "\\n"));

    final PgnFile pgnFile = PgnReader.readPgn(lines);

    final ApiBoard board = GeneralUtility.calculateBoard(pgnFile);
    printAnalysis(board);
  }

  protected static void printAnalysis(Path folderPath, String pgnFileName) {

    final ApiBoard board = GeneralUtility.calculateBoard(folderPath, pgnFileName);
    printAnalysis(board);
  }

  protected static void printAnalysis(ApiBoard board) {
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
    final var hasFiftyMoveRule = calculateHasFiftyMoveRule(yawnMoveListList);
    if (hasFiftyMoveRule) {
      output.add(Message.getString("analysis.yawnmove.fiftyMoves.yes"));
    } else {
      output.add(Message.getString("analysis.yawnmove.fiftyMoves.no"));
    }

    printList(output);
  }

  private static boolean calculateHasFiftyMoveRule(List<List<YawnHalfMove>> yawnMoveListList) {
    // add main section
    for (final List<YawnHalfMove> yawnMoveList : yawnMoveListList) {
      for (final YawnHalfMove yawnHalfMove : yawnMoveList) {
        if (yawnHalfMove.sequenceLength() >= ChessConstants.FIFTY_MOVE_RULE_HALF_MOVE_CLOCK_THRESHOLD) {
          return true;
        }
      }
    }
    return false;
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
