package com.dlb.chess.report;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.ChessConstants;
import com.dlb.chess.common.enums.EnPassantCaptureRuleThreefold;
import com.dlb.chess.common.enums.InsufficientMaterial;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.interfaces.ChessBoard;
import com.dlb.chess.common.model.ClaimAhead;
import com.dlb.chess.common.model.HalfMove;
import com.dlb.chess.common.utility.GeneralUtility;
import com.dlb.chess.common.utility.RepetitionUtility;
import com.dlb.chess.common.utility.ThreefoldClaimAheadUtility;
import com.dlb.chess.common.utility.YawnMoveUtility;
import com.dlb.chess.internationalization.Message;
import com.dlb.chess.pgn.parser.LenientPgnParser;
import com.dlb.chess.pgn.parser.model.PgnFile;
import com.dlb.chess.report.model.Report;
import com.dlb.chess.report.model.YawnHalfMove;
import com.dlb.chess.report.print.RepetitionPrint;
import com.dlb.chess.report.print.ThreefoldClaimAheadPrint;
import com.dlb.chess.report.print.YawnPrint;

public final class Reporter {

  private static final int REPETITION_COUNT_THRESHOLD = ChessConstants.THREEFOLD_REPETITION_RULE_THRESHOLD;
  private static final int YAWN_FULL_MOVE_COUNT_THRESHOLD = 25;

  private Reporter() {
  }

  public static void printReport(String pgnString) {
    final PgnFile pgnFile = LenientPgnParser.parseText(pgnString);
    final ChessBoard board = GeneralUtility.calculateBoard(pgnFile);
    printReport(board);
  }

  public static void printReport(Path folderPath, String pgnFileName) {
    final ChessBoard board = GeneralUtility.calculateBoard(folderPath, pgnFileName);
    printReport(board);
  }

  public static void printReport(ChessBoard board) {
    final @NonNull List<String> output = new ArrayList<>();

    // repetition
    addFirstMainSection(output, "report.repetition.threefold.ahead.title");
    final List<List<ClaimAhead>> claimAheadListList = ThreefoldClaimAheadUtility
        .calculateThreefoldClaimAhead(board.getPerformedLegalMoveList(), board.getInitialFen());
    if (claimAheadListList.isEmpty()) {
      output.add(Message.getString("report.repetition.threefold.ahead.none"));
    } else {
      final var claimAheadList = ThreefoldClaimAheadPrint.calculateClaimAheadList(claimAheadListList);
      output.addAll(claimAheadList);
    }

    final List<List<HalfMove>> repetitionListList = RepetitionUtility.calculateRepetitionListList(
        board.getHalfMoveList(), REPETITION_COUNT_THRESHOLD, EnPassantCaptureRuleThreefold.DO_NOT_IGNORE);
    addMainSection(output, "report.repetition.threefold.list.title");
    if (repetitionListList.isEmpty()) {
      output.add(Message.getString("report.repetition.threefold.list.none"));
    } else {
      final var listChronic = RepetitionPrint.calculateOutputRepetitionChronlogically(repetitionListList);
      output.add(listChronic);
    }

    // yawn move
    final List<List<YawnHalfMove>> yawnMoveListList = YawnMoveUtility.calculateYawnMoveRule(board,
        2 * YAWN_FULL_MOVE_COUNT_THRESHOLD);
    addMainSection(output, "report.yawnmove.sequence.title",
        NonNullWrapperCommon.valueOf(YAWN_FULL_MOVE_COUNT_THRESHOLD));
    if (yawnMoveListList.isEmpty()) {
      output.add(Message.getString("report.yawnmove.sequence.none"));
    } else {
      final var list = YawnPrint.calculateOutputYawnMoveListList(yawnMoveListList);
      output.addAll(list);
    }

    addMainSection(output, "report.yawnmove.fiftyMoves.title");
    final var hasFiftyMoveRule = calculateHasFiftyMoveRule(yawnMoveListList);
    if (hasFiftyMoveRule) {
      output.add(Message.getString("report.yawnmove.fiftyMoves.yes"));
    } else {
      output.add(Message.getString("report.yawnmove.fiftyMoves.no"));
    }

    printList(output);
  }

  public static Report calculateReport(Path folderPath, String pgnFileName) throws Exception {

    final ChessBoard board = GeneralUtility.calculateBoard(folderPath, pgnFileName);
    return calculateReport(board);
  }

  public static Report calculateReport(ChessBoard board) {

    final String invariant = board.getFen();

    final Side havingMove = board.getHavingMove();

    final List<HalfMove> halfMoveList = board.getHalfMoveList();

    final List<List<HalfMove>> repetitionListList = RepetitionUtility.calculateRepetitionListList(halfMoveList,
        ChessConstants.THREEFOLD_REPETITION_RULE_THRESHOLD, EnPassantCaptureRuleThreefold.DO_NOT_IGNORE);
    final List<List<HalfMove>> repetitionListListInitialEnPassantCapture = calculateRepetitionListListInitialEnPassantCapture(
        halfMoveList);

    final List<List<YawnHalfMove>> yawnMoveListList = YawnMoveUtility.calculateYawnMoveRule(board,
        ChessConstants.FIFTY_MOVE_RULE_HALF_MOVE_CLOCK_THRESHOLD);

    final var hasThreefoldRepetition = !repetitionListList.isEmpty();
    final var hasThreefoldRepetitionInitialEnPassantCapture = !repetitionListListInitialEnPassantCapture.isEmpty();
    final var hasFivefoldRepetition = calculateHasFivefoldRepetition(repetitionListList);
    final var hasFiftyMoveRule = !yawnMoveListList.isEmpty();
    final var hasSeventyFiveMoveRule = calculateHasSeventyFiveMoveRule(yawnMoveListList);

    final var firstCapture = calculateFirstCapture(halfMoveList);
    final var hasCapture = calculateHasCapture(halfMoveList);

    final var maxYawnSequence = calculateMaxYawnSequence(board);

    final var checkmateOrStalemate = GeneralUtility.calculateLastPositionEvaluation(board);
    final InsufficientMaterial insufficientMaterial = board.calculateInsufficientMaterial();

    final String fen = board.getFen();

    if (!invariant.equals(board.getFen())) {
      throw new ProgrammingMistakeException("Board was changed");
    }

    return new Report(havingMove, halfMoveList, repetitionListList, repetitionListListInitialEnPassantCapture,
        yawnMoveListList, hasThreefoldRepetition, hasThreefoldRepetitionInitialEnPassantCapture, hasFivefoldRepetition,
        hasFiftyMoveRule, hasSeventyFiveMoveRule, firstCapture, hasCapture, maxYawnSequence, checkmateOrStalemate,
        insufficientMaterial, fen, board);
  }

  public static boolean calculateIsHalfMoveTerminatesYawnSequence(HalfMove halfMove) {
    return halfMove.halfMoveClock() == 0;
  }

  // ---------------------------------------------------------------------------------------------
  // Private helpers — calculate*
  // ---------------------------------------------------------------------------------------------

  private static boolean calculateHasFivefoldRepetition(List<List<HalfMove>> repetitionListList) {
    for (final List<HalfMove> currentHalfMoveList : repetitionListList) {
      if (currentHalfMoveList.size() >= ChessConstants.FIVEFOLD_REPETITION_RULE_THRESHOLD) {
        return true;
      }
    }
    return false;
  }

  private static boolean calculateHasSeventyFiveMoveRule(List<List<YawnHalfMove>> yawnMoveListList) {
    for (final List<YawnHalfMove> list : yawnMoveListList) {
      final YawnHalfMove lastYawnHalfMove = NonNullWrapperCommon.getLast(list);

      if (lastYawnHalfMove.sequenceLength() >= ChessConstants.SEVENTY_FIVE_MOVE_RULE_HALF_MOVE_CLOCK_THRESHOLD) {
        return true;
      }
    }
    return false;
  }

  private static boolean calculateHasFiftyMoveRule(List<List<YawnHalfMove>> yawnMoveListList) {
    for (final List<YawnHalfMove> yawnMoveList : yawnMoveListList) {
      for (final YawnHalfMove yawnHalfMove : yawnMoveList) {
        if (yawnHalfMove.sequenceLength() >= ChessConstants.FIFTY_MOVE_RULE_HALF_MOVE_CLOCK_THRESHOLD) {
          return true;
        }
      }
    }
    return false;
  }

  private static List<List<HalfMove>> calculateRepetitionListListInitialEnPassantCapture(List<HalfMove> halfMoveList) {

    final List<List<HalfMove>> repetitionListListIgnoringEnPassantCapture = RepetitionUtility
        .calculateRepetitionListList(halfMoveList, ChessConstants.THREEFOLD_REPETITION_RULE_THRESHOLD,
            EnPassantCaptureRuleThreefold.DO_IGNORE);

    final List<List<HalfMove>> list = new ArrayList<>();
    for (final List<HalfMove> currentHalfMoveList : repetitionListListIgnoringEnPassantCapture) {
      final HalfMove firstHalfMove = NonNullWrapperCommon.getFirst(currentHalfMoveList);
      if (firstHalfMove.dynamicPosition().isEnPassantCapturePossible()) {
        list.add(currentHalfMoveList);
      }
    }
    return list;
  }

  private static int calculateFirstCapture(List<HalfMove> halfMoveList) {
    for (final HalfMove halfMove : halfMoveList) {
      if (halfMove.isCapture()) {
        return halfMove.halfMoveCount();
      }
    }
    return -1;
  }

  private static boolean calculateHasCapture(List<HalfMove> halfMoveList) {
    for (final HalfMove halfMove : halfMoveList) {
      if (halfMove.isCapture()) {
        return true;
      }
    }
    return false;
  }

  private static int calculateMaxYawnSequence(ChessBoard board) {
    final List<HalfMove> halfMoveList = board.getHalfMoveList();

    if (board.getHalfMoveList().isEmpty()) {
      return board.getInitialFen().halfMoveClock();
    }

    // if the first move is capture or pawn move the initial half move clock is
    // reset
    // so we initialize the max with the initial half move clock to assure it is
    // considered in the calculation
    var maxHalfMoveClock = board.getInitialFen().halfMoveClock();
    final var maxIndex = halfMoveList.size() - 1;
    for (var i = 0; i <= maxIndex; i++) {
      final HalfMove halfMove = NonNullWrapperCommon.get(halfMoveList, i);
      final var halfMoveClock = halfMove.halfMoveClock();
      if (halfMoveClock > maxHalfMoveClock) {
        maxHalfMoveClock = halfMoveClock;
      }
    }
    return maxHalfMoveClock;
  }

  // ---------------------------------------------------------------------------------------------
  // Private helpers — output formatting
  // ---------------------------------------------------------------------------------------------

  private static void addFirstMainSection(List<String> output, String key) {
    final StringBuilder mainSection = new StringBuilder();
    mainSection.append(Message.getString(key));
    mainSection.append(":");
    output.add(NonNullWrapperCommon.toString(mainSection));
  }

  private static void addFirstMainSection(List<String> output, String key, String placeHolder) {
    final StringBuilder mainSection = new StringBuilder();
    mainSection.append(Message.getString(key, placeHolder));
    mainSection.append(":");
    output.add(NonNullWrapperCommon.toString(mainSection));
  }

  private static void addMainSection(List<String> output, String key) {
    output.add("");
    addFirstMainSection(output, key);
  }

  private static void addMainSection(List<String> output, String key, String placeHolder) {
    output.add("");
    addFirstMainSection(output, key, placeHolder);
  }

  private static void printList(List<String> list) {
    for (final String line : list) {
      System.out.println(line);
    }
  }
}
