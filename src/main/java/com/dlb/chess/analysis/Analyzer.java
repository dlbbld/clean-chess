package com.dlb.chess.analysis;

import java.util.ArrayList;
import java.util.List;

import com.dlb.chess.analysis.model.Analysis;
import com.dlb.chess.analysis.model.YawnHalfMove;
import com.dlb.chess.analysis.print.AnalyzerPrint;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.ChessConstants;
import com.dlb.chess.common.constants.ConfigurationConstants;
import com.dlb.chess.common.enums.EnPassantCaptureRuleThreefold;
import com.dlb.chess.common.enums.InsufficientMaterial;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.HalfMove;
import com.dlb.chess.common.utility.GeneralUtility;
import com.dlb.chess.common.utility.RepetitionUtility;
import com.dlb.chess.common.utility.YawnMoveUtility;
import com.dlb.chess.unwinnability.full.UnwinnableFullAnalyzer;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;
import com.dlb.chess.unwinnability.mobility.Mobility;
import com.dlb.chess.unwinnability.mobility.model.MobilitySolution;
import com.dlb.chess.unwinnability.quick.UnwinnableQuickAnalyzer;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuick;

public class Analyzer extends AnalyzerPrint {

  // we set to false for faster testing runs
  public static boolean IS_CALCULATE_UNWINNABLE = false;

  public static void main(String[] args) throws Exception {
    printAnalysis(ConfigurationConstants.PROJECT_ROOT_FOLDER_PATH + "\\src\\test\\resources\\pgn\\games\\various",
        "Ob5ozxgG.pgn");
    System.out.println("");

    printAnalysis(ConfigurationConstants.PROJECT_ROOT_FOLDER_PATH + "\\src\\test\\resources\\pgn\\wikipedia\\threefold",
        "2_5_korchnoi_portisch_1970_game_4.pgn");
    System.out.println("");

    printAnalysis(ConfigurationConstants.PROJECT_ROOT_FOLDER_PATH + "\\src\\test\\resources\\pgn\\wikipedia\\threefold",
        "2_3_capablanca_lasker_1921.pgn");
    System.out.println("");

    printAnalysis(ConfigurationConstants.PROJECT_ROOT_FOLDER_PATH + "\\src\\test\\resources\\pgn\\wikipedia\\fiftyMove",
        "2_2_karpov_kasparov_1991.pgn");
  }

  public static void printAnalysis(String pgn) throws Exception {
    // delegated to package protected method for class organization
    AnalyzerPrint.printAnalysis(pgn);
  }

  public static void printAnalysis(String folderPath, String pgnFileName) throws Exception {
    // delegated to package protected method for class organization
    AnalyzerPrint.printAnalysis(folderPath, pgnFileName);
  }

  public static void printAnalysis(ApiBoard board) throws Exception {
    // delegated to package protected method for class organization
    AnalyzerPrint.printAnalysis(board);
  }

  public static Analysis calculateAnalysis(String folderPath, String pgnFileName) throws Exception {

    final ApiBoard board = GeneralUtility.calculateBoard(folderPath, pgnFileName);
    return calculateAnalysis(board);
  }

  public static Analysis calculateAnalysis(ApiBoard board) throws Exception {

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

    final var isGameContinuedOverFivefoldRepetition = calculateGameContinuedOverFivefoldRepetition(halfMoveList);
    final var isGameContinuedOverSeventyFiveMove = calculateGameContinuedOverSeventyFiveMove(yawnMoveListList);

    final var firstCapture = calculateFirstCapture(halfMoveList);
    final var hasCapture = calculateHasCapture(halfMoveList);

    final var maxYawnSequence = calculateMaxYawnSequence(board);

    final var checkmateOrStalemate = GeneralUtility.calculateLastPositionEvaluation(board);
    final InsufficientMaterial insufficientMaterial = board.calculateInsufficientMaterial();

    // for performance we calculate and reuse the mobility solution
    final MobilitySolution mobilitySolution = Mobility.mobility(board);

    final UnwinnableFull unwinnableFullWhite;
    final UnwinnableFull unwinnableFullBlack;

    final UnwinnableQuick unwinnableQuickWhite;
    final UnwinnableQuick unwinnableQuickBlack;

    if (IS_CALCULATE_UNWINNABLE) {
      unwinnableFullWhite = UnwinnableFullAnalyzer.unwinnableFull(board, Side.WHITE, true, mobilitySolution)
          .unwinnableFull();
      unwinnableFullBlack = UnwinnableFullAnalyzer.unwinnableFull(board, Side.BLACK, true, mobilitySolution)
          .unwinnableFull();

      unwinnableQuickWhite = UnwinnableQuickAnalyzer.unwinnableQuick(board, Side.WHITE, true, mobilitySolution);
      unwinnableQuickBlack = UnwinnableQuickAnalyzer.unwinnableQuick(board, Side.BLACK, true, mobilitySolution);
    } else {
      unwinnableFullWhite = UnwinnableFull.UNDETERMINED;
      unwinnableFullBlack = UnwinnableFull.UNDETERMINED;

      unwinnableQuickWhite = UnwinnableQuick.POSSIBLY_WINNABLE;
      unwinnableQuickBlack = UnwinnableQuick.POSSIBLY_WINNABLE;
    }

    final String fen = board.getFen();

    if (!invariant.equals(board.getFen())) {
      throw new ProgrammingMistakeException("Board was changed");
    }

    return new Analysis(havingMove, halfMoveList, repetitionListList, repetitionListListInitialEnPassantCapture,
        yawnMoveListList, hasThreefoldRepetition, hasThreefoldRepetitionInitialEnPassantCapture, hasFivefoldRepetition,
        hasFiftyMoveRule, hasSeventyFiveMoveRule, isGameContinuedOverFivefoldRepetition,
        isGameContinuedOverSeventyFiveMove, firstCapture, hasCapture, maxYawnSequence, checkmateOrStalemate,
        insufficientMaterial, unwinnableFullWhite, unwinnableFullBlack, unwinnableQuickWhite, unwinnableQuickBlack, fen,
        board);
  }

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

  private static boolean calculateGameContinuedOverFivefoldRepetition(List<HalfMove> halfMoveList) {

    for (final HalfMove halfMove : halfMoveList) {
      final var countRepetition = RepetitionUtility.getCountRepetition(halfMove,
          EnPassantCaptureRuleThreefold.DO_NOT_IGNORE);
      if (countRepetition >= ChessConstants.FIVEFOLD_REPETITION_RULE_THRESHOLD) {
        if (halfMove.equals(NonNullWrapperCommon.getLast(halfMoveList))) {
          return false;
        }
        return true;
      }
    }
    return false;
  }

  private static boolean calculateGameContinuedOverSeventyFiveMove(List<List<YawnHalfMove>> yawnMoveListList) {

    for (final List<YawnHalfMove> list : yawnMoveListList) {
      final YawnHalfMove lastYawnHalfMove = NonNullWrapperCommon.getLast(list);

      if (lastYawnHalfMove.sequenceLength() > ChessConstants.SEVENTY_FIVE_MOVE_RULE_HALF_MOVE_CLOCK_THRESHOLD) {
        return true;
      }
    }
    return false;
  }

  private static int calculateMaxYawnSequence(ApiBoard board) {
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

  public static boolean calculateIsHalfMoveTerminatesYawnSequence(HalfMove halfMove) {
    return halfMove.halfMoveClock() == 0;
  }

}
