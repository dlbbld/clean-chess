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
import com.dlb.chess.common.utility.NoProgressMoveUtility;
import com.dlb.chess.common.utility.RepetitionUtility;
import com.dlb.chess.common.utility.ThreefoldClaimAheadUtility;
import com.dlb.chess.messages.Message;
import com.dlb.chess.pgn.LenientPgnParser;
import com.dlb.chess.pgn.PgnFile;
import com.dlb.chess.report.NoProgressHalfMove;
import com.dlb.chess.report.Report;
import com.dlb.chess.report.NoProgressPrint;
import com.dlb.chess.report.RepetitionPrint;
import com.dlb.chess.report.ThreefoldClaimAheadPrint;

/**
 * Generates game-level reports — threefold-repetition listings (including missed-claim-ahead opportunities),
 * no-progress (50/75-move-rule) sequences, and a printable summary — from a {@link ChessBoard} or a parsed PGN.
 *
 * <p>
 * Two surfaces:
 *
 * <ul>
 * <li>{@code calculateReport(...)} returns a {@link com.dlb.chess.report.Report} record carrying all the
 * analytical data — repetition lists, threefold-claim-ahead slots, no-progress sequences. Use this for programmatic
 * inspection.</li>
 * <li>{@code printReport(...)} emits a human-readable summary to {@code stdout} via
 * {@link com.dlb.chess.messages.Message}. Use this for the kind of CLI-style output shown in the README examples.</li>
 * </ul>
 *
 * <p>
 * The report distinguishes the on-board predicates ("threefold has occurred") from the with-move predicates ("some
 * legal move would create a threefold position the side could claim before playing it"). The latter surfaces missed
 * claim opportunities other libraries don't.
 *
 * <p>
 * Final class with a private constructor — all entry points are static.
 */
public final class Reporter {

  private static final int REPETITION_COUNT_THRESHOLD = ChessConstants.THREEFOLD_REPETITION_RULE_THRESHOLD;
  private static final int NO_PROGRESS_FULL_MOVE_COUNT_THRESHOLD = 25;

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
    printList(calculateReportLines(board));
  }

  /**
   * Returns the same human-readable report as {@link #printReport(String)} but as a single string, lines joined by
   * {@code "\n"}. Use this when the consumer is not stdout — web responses, file writes, GUI displays, etc.
   */
  public static String calculateReportText(String pgnString) {
    final PgnFile pgnFile = LenientPgnParser.parseText(pgnString);
    final ChessBoard board = GeneralUtility.calculateBoard(pgnFile);
    return calculateReportText(board);
  }

  /**
   * Returns the same human-readable report as {@link #printReport(Path, String)} but as a single string, lines joined
   * by {@code "\n"}.
   */
  public static String calculateReportText(Path folderPath, String pgnFileName) {
    final ChessBoard board = GeneralUtility.calculateBoard(folderPath, pgnFileName);
    return calculateReportText(board);
  }

  /**
   * Returns the same human-readable report as {@link #printReport(ChessBoard)} but as a single string, lines joined by
   * {@code "\n"}.
   */
  public static String calculateReportText(ChessBoard board) {
    return NonNullWrapperCommon.join("\n", calculateReportLines(board));
  }

  private static List<String> calculateReportLines(ChessBoard board) {
    final @NonNull List<String> output = new ArrayList<>();

    // repetition
    addFirstMainSection(output, "report.repetition.threefold.ahead.title");
    final List<List<ClaimAhead>> claimAheadListList = ThreefoldClaimAheadUtility.calculateThreefoldClaimAhead(board);
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

    // no progress move
    final List<List<NoProgressHalfMove>> noProgressMoveListList = NoProgressMoveUtility
        .calculateNoProgressMoveRule(board, 2 * NO_PROGRESS_FULL_MOVE_COUNT_THRESHOLD);
    addMainSection(output, "report.noProgressMove.sequence.title",
        NonNullWrapperCommon.valueOf(NO_PROGRESS_FULL_MOVE_COUNT_THRESHOLD));
    if (noProgressMoveListList.isEmpty()) {
      output.add(Message.getString("report.noProgressMove.sequence.none"));
    } else {
      final var list = NoProgressPrint.calculateOutputNoProgressMoveListList(noProgressMoveListList);
      output.addAll(list);
    }

    addMainSection(output, "report.noProgressMove.fiftyMoves.title");
    final var hasFiftyMoveRule = calculateHasFiftyMoveRule(noProgressMoveListList);
    if (hasFiftyMoveRule) {
      output.add(Message.getString("report.noProgressMove.fiftyMoves.yes"));
    } else {
      output.add(Message.getString("report.noProgressMove.fiftyMoves.no"));
    }

    return output;
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

    final List<List<NoProgressHalfMove>> noProgressMoveListList = NoProgressMoveUtility
        .calculateNoProgressMoveRule(board, ChessConstants.FIFTY_MOVE_RULE_HALF_MOVE_CLOCK_THRESHOLD);

    final var hasThreefoldRepetition = !repetitionListList.isEmpty();
    final var hasThreefoldRepetitionInitialEnPassantCapture = !repetitionListListInitialEnPassantCapture.isEmpty();
    final var hasFivefoldRepetition = calculateHasFivefoldRepetition(repetitionListList);
    final var hasFiftyMoveRule = !noProgressMoveListList.isEmpty();
    final var hasSeventyFiveMoveRule = calculateHasSeventyFiveMoveRule(noProgressMoveListList);

    final var firstCapture = calculateFirstCapture(halfMoveList);
    final var hasCapture = calculateHasCapture(halfMoveList);

    final var maxNoProgressSequence = calculateMaxNoProgressSequence(board);

    final var checkmateOrStalemate = GeneralUtility.calculateLastPositionEvaluation(board);
    final InsufficientMaterial insufficientMaterial = board.calculateInsufficientMaterial();

    final String fen = board.getFen();

    if (!invariant.equals(board.getFen())) {
      throw new ProgrammingMistakeException("Board was changed");
    }

    return new Report(havingMove, halfMoveList, repetitionListList, repetitionListListInitialEnPassantCapture,
        noProgressMoveListList, hasThreefoldRepetition, hasThreefoldRepetitionInitialEnPassantCapture,
        hasFivefoldRepetition, hasFiftyMoveRule, hasSeventyFiveMoveRule, firstCapture, hasCapture,
        maxNoProgressSequence, checkmateOrStalemate, insufficientMaterial, fen, board);
  }

  public static boolean calculateIsHalfMoveTerminatesNoProgressSequence(HalfMove halfMove) {
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

  private static boolean calculateHasSeventyFiveMoveRule(List<List<NoProgressHalfMove>> noProgressMoveListList) {
    for (final List<NoProgressHalfMove> list : noProgressMoveListList) {
      final NoProgressHalfMove lastNoProgressHalfMove = NonNullWrapperCommon.getLast(list);

      if (lastNoProgressHalfMove.sequenceLength() >= ChessConstants.SEVENTY_FIVE_MOVE_RULE_HALF_MOVE_CLOCK_THRESHOLD) {
        return true;
      }
    }
    return false;
  }

  private static boolean calculateHasFiftyMoveRule(List<List<NoProgressHalfMove>> noProgressMoveListList) {
    for (final List<NoProgressHalfMove> noProgressMoveList : noProgressMoveListList) {
      for (final NoProgressHalfMove noProgressHalfMove : noProgressMoveList) {
        if (noProgressHalfMove.sequenceLength() >= ChessConstants.FIFTY_MOVE_RULE_HALF_MOVE_CLOCK_THRESHOLD) {
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

  private static int calculateMaxNoProgressSequence(ChessBoard board) {
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
