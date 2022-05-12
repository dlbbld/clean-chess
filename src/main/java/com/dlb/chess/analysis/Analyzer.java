package com.dlb.chess.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.analysis.enums.CheckmateOrStalemate;
import com.dlb.chess.analysis.model.Analysis;
import com.dlb.chess.analysis.model.RepeatingSequence;
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
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.common.utility.BasicUtility;
import com.dlb.chess.common.utility.GeneralUtility;
import com.dlb.chess.common.utility.HalfMoveUtility;
import com.dlb.chess.common.utility.RepetitionUtility;
import com.dlb.chess.common.utility.YawnMoveUtility;
import com.dlb.chess.unwinnability.quick.UnwinnableQuick;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuickResult;
import com.google.common.collect.ImmutableList;

public class Analyzer extends AnalyzerPrint {

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

  // TODO clean-up sequence repetition
  // not maintained anymore, keep false, including was too slow and not functional anymore
  public static final boolean IS_CALCULATE_SEQUENCE_REPETITION = false;

  public static final ImmutableList<RepeatingSequence> SEQUENCE_REPETITION_NOT_CALCULATED = NonNullWrapperCommon
      .copyOfList(new ArrayList<>());

  private static final int MIN_SEQUENCE_LENGTH = 2;
  private static final int MIN_SEQUENCE_REPETION = 3;

  public static List<String> calculateSequenceRepetitionRepresentation(List<RepeatingSequence> repeatingSequenceList) {

    final List<String> result = new ArrayList<>();
    for (final RepeatingSequence repeating : repeatingSequenceList) {

      final var item = "repSeq=" + repeating.repetitions() + ", seq=" + repeating.sequenceLength() + ": "
          + calculateSequenceRepetitionRepresentationList(repeating.sequence());

      result.add(item);
    }
    return result;
  }

  private static String calculateSequenceRepetitionRepresentationList(List<List<HalfMove>> singleSequence) {

    final StringBuilder result = new StringBuilder();
    for (var i = 0; i < singleSequence.size(); i++) {
      final List<HalfMove> repeatingOuter = NonNullWrapperCommon.get(singleSequence, i);
      for (var j = 0; j < repeatingOuter.size(); j++) {
        final HalfMove halfMove = NonNullWrapperCommon.get(repeatingOuter, j);
        final var isMadeByWhite = halfMove.moveSpecification().havingMove().getIsWhite();
        if (isMadeByWhite) {
          result.append(HalfMoveUtility.calculateMoveNumberAndSanWithoutSpace(halfMove));
        } else if (j > 0) {
          result.append(" ");
          result.append(halfMove.san());
        }
        final var isBlackMove = !isMadeByWhite;
        final var isAnotherCondition = 0 < j && j < repeatingOuter.size() - 1;
        if (isBlackMove && isAnotherCondition) {
          result.append(", ");
        }
      }
      if (i < singleSequence.size() - 1) {
        result.append("; ");
      }
    }
    return NonNullWrapperCommon.toString(result);
  }

  private static List<RepeatingSequence> calculateSequenceRepetition(List<HalfMove> halfMoveList) {

    // this method requires a lot of performance for long games
    // and array list runs out of standard limits for longest game

    if (!IS_CALCULATE_SEQUENCE_REPETITION) {
      return SEQUENCE_REPETITION_NOT_CALCULATED;
    }

    final List<List<List<List<List<HalfMove>>>>> sequenceLists = calculateAllSequences(halfMoveList);

    final List<RepeatingSequence> result = new ArrayList<>();

    for (final List<List<List<List<HalfMove>>>> subList1 : sequenceLists) {
      for (final List<List<List<HalfMove>>> subList2 : subList1) {
        for (final List<List<HalfMove>> subList3 : subList2) {
          if (calculateIsRepeatingSequence(subList3)) {
            final var sequenceLength = NonNullWrapperCommon.get(subList3, 0).size();
            final var repetitions = subList3.size();

            final RepeatingSequence sequence = new RepeatingSequence(subList3, sequenceLength, repetitions);

            result.add(sequence);
          }
        }
      }
    }
    return result;
  }

  private static boolean calculateIsRepeatingSequence(List<List<HalfMove>> sequencesList) {
    final Set<String> identifierSet = new TreeSet<>();
    for (final List<HalfMove> sequence : sequencesList) {
      final String identifier = calculateIdentifierForSequence(sequence);
      identifierSet.add(identifier);
      if (identifierSet.size() > 1) {
        return false;
      }
    }
    return true;
  }

  private static String calculateIdentifierForSequence(List<HalfMove> sequence) {
    final StringBuilder result = new StringBuilder();
    for (final HalfMove halfMove : sequence) {
      final String identifier = calculateHalfMoveIdentifier(halfMove);
      result.append(identifier);
      result.append("-");
    }
    BasicUtility.removeLastChar(result);
    return NonNullWrapperCommon.toString(result);
  }

  private static List<List<List<List<List<HalfMove>>>>> calculateAllSequences(List<HalfMove> halfMoveList) {

    final List<List<List<List<List<HalfMove>>>>> sequenceLists = new ArrayList<>();

    // we try to find sequences of length 2, starting from 1...last move - 1
    // then we try to find sequences of length 3, starting from 1...last move - 2
    // and so on
    final double halfMoveSize = halfMoveList.size();
    final var halfHalfMoveSize = halfMoveSize / 2.0;
    // 20 half moves
    // half = 10, half floor = 10, checking 1...10, 11...20
    // 21 half moves
    // half = 10.5, half floor = 10, checking 1...10, 11...20 and 2...11, 12...21
    final var halfHalfMoveSizeFloor = (int) Math.floor(halfHalfMoveSize);

    // 20 half moves, sequence=2, checking last at index = 18, so positions 19, 20
    // 21 half moves, sequence=2, checking last at index = 19, so positions 20, 21
    // ok, but doesn't make sense as there will be no repetition
    for (var sequenceLength = MIN_SEQUENCE_LENGTH; sequenceLength <= halfHalfMoveSizeFloor; sequenceLength++) {
      final List<List<List<List<HalfMove>>>> sequenceListsForFixedLength = new ArrayList<>();

      final var numberRepetitionsUpperBound = calculateUpperBoundRepetitons(sequenceLength, halfMoveSize);
      for (var numberRepetitionsSearch = MIN_SEQUENCE_REPETION; numberRepetitionsSearch <= numberRepetitionsUpperBound; numberRepetitionsSearch++) {
        final List<List<List<HalfMove>>> sequenceListsForFixedLengthFixedRepetition = new ArrayList<>();
        // attention, first entry was not a move, using the pgn extract, but also there
        // not necessary
        for (var i = 0; i <= halfMoveList.size() - sequenceLength; i++) {
          final List<List<HalfMove>> sequenceList = calculateSequence(i, sequenceLength, numberRepetitionsSearch,
              halfMoveList);

          if (!sequenceList.isEmpty()) {
            sequenceListsForFixedLengthFixedRepetition.add(sequenceList);
          }
        }
        if (!sequenceListsForFixedLengthFixedRepetition.isEmpty()) {
          sequenceListsForFixedLength.add(sequenceListsForFixedLengthFixedRepetition);
        }
      }
      if (!sequenceListsForFixedLength.isEmpty()) {
        sequenceLists.add(sequenceListsForFixedLength);
      }
    }
    return sequenceLists;
  }

  private static List<List<HalfMove>> calculateSequence(int indexBegin, int sequenceLength, int numberRepetitionsSearch,
      List<HalfMove> halfMoveList) {

    final var halfMoveListMaxIndex = halfMoveList.size() - 1;

    var indexFromIncluding = indexBegin;
    var indexToIncluding = indexFromIncluding + sequenceLength - 1;

    final List<List<HalfMove>> sequenceList = new ArrayList<>();
    for (var rep = 1; rep <= numberRepetitionsSearch; rep++) {
      final List<HalfMove> sequence = new ArrayList<>();
      if (indexToIncluding > halfMoveListMaxIndex) {
        break;
      }
      for (var currentSequenceIndex = indexFromIncluding; currentSequenceIndex <= indexToIncluding; currentSequenceIndex++) {

        final HalfMove halfMove = NonNullWrapperCommon.get(halfMoveList, currentSequenceIndex);

        sequence.add(halfMove);
      }
      sequenceList.add(sequence);

      indexFromIncluding = indexToIncluding + 1;
      indexToIncluding = indexFromIncluding + sequenceLength - 1;
    }

    if (sequenceList.size() != numberRepetitionsSearch) {
      return new ArrayList<>();
    }
    return sequenceList;
  }

  private static int calculateUpperBoundRepetitons(int sequenceLength, double halfMoveSize) {
    return (int) Math.floor(halfMoveSize / sequenceLength);
  }

  // must specify the move uniquely including side moved
  private static String calculateHalfMoveIdentifier(HalfMove halfMove) {
    final MoveSpecification moveSpecification = halfMove.moveSpecification();

    final StringBuilder moveIdentifier = new StringBuilder();

    moveIdentifier.append(moveSpecification.havingMove().toString());
    moveIdentifier.append("_");

    moveIdentifier.append(moveSpecification.fromSquare().toString());
    moveIdentifier.append("_");
    moveIdentifier.append(moveSpecification.toSquare().toString());
    moveIdentifier.append("_");
    moveIdentifier.append(moveSpecification.castlingMove().toString());
    moveIdentifier.append("_");
    moveIdentifier.append(moveSpecification.promotionPieceType().toString());
    moveIdentifier.append("_");
    moveIdentifier.append(halfMove.movingPiece());
    moveIdentifier.append("_");

    return NonNullWrapperCommon.toString(moveIdentifier);
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

    final ApiBoard board = GeneralUtility.calculateChessBoard(folderPath, pgnFileName);
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

    final List<RepeatingSequence> sequenceRepetitionList = calculateSequenceRepetition(halfMoveList);

    final var hasThreefoldRepetition = !repetitionListList.isEmpty();
    final var hasThreefoldRepetitionInitialEnPassantCapture = !repetitionListListInitialEnPassantCapture.isEmpty();
    final var hasFivefoldRepetition = calculateHasFivefoldRepetition(repetitionListList);
    final var hasFiftyMoveRule = !yawnMoveListList.isEmpty();
    final var hasSeventyFiveMoveRule = calculateHasSeventyFiveMoveRule(yawnMoveListList);
    final var hasThreeSequenceRepetition = !sequenceRepetitionList.isEmpty();

    final var isGameContinuedOverFivefoldRepetition = calculateGameContinuedOverFivefoldRepetition(halfMoveList);
    final var isGameContinuedOverSeventyFiveMove = calculateGameContinuedOverSeventyFiveMove(yawnMoveListList);

    final var firstCapture = calculateFirstCapture(halfMoveList);
    final var hasCapture = calculateHasCapture(halfMoveList);

    final var maxYawnSequence = calculateMaxYawnSequence(board);

    final CheckmateOrStalemate lastPositionEvaluation = GeneralUtility.calculateLastPositionEvaluation(board);
    final InsufficientMaterial insufficientMaterial = board.calculateInsufficientMaterial();
    final UnwinnableQuickResult unwinnableNotHavingMove = UnwinnableQuick.unwinnableQuick(board,
        board.getHavingMove().getOppositeSide());

    final String fen = board.getFen();

    if (!invariant.equals(board.getFen())) {
      throw new ProgrammingMistakeException("Board was changed");
    }

    return new Analysis(havingMove, halfMoveList, repetitionListList, repetitionListListInitialEnPassantCapture,
        sequenceRepetitionList, yawnMoveListList, hasThreefoldRepetition, hasThreefoldRepetitionInitialEnPassantCapture,
        hasFivefoldRepetition, hasFiftyMoveRule, hasSeventyFiveMoveRule, hasThreeSequenceRepetition,
        isGameContinuedOverFivefoldRepetition, isGameContinuedOverSeventyFiveMove, firstCapture, hasCapture,
        maxYawnSequence, lastPositionEvaluation, insufficientMaterial, unwinnableNotHavingMove, fen, board);
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
