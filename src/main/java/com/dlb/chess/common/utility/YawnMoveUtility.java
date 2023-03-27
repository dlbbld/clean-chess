package com.dlb.chess.common.utility;

import java.util.ArrayList;
import java.util.List;

import com.dlb.chess.analysis.Analyzer;
import com.dlb.chess.analysis.model.YawnHalfMove;
import com.dlb.chess.analysis.model.YawnIndex;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.BasicConstants;
import com.dlb.chess.common.constants.ChessConstants;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.HalfMove;

public abstract class YawnMoveUtility {

  public static List<List<YawnHalfMove>> calculateYawnMoveRule(ApiBoard board, int numberOfHalfMovesThreshold) {

    final List<YawnIndex> indexList = YawnMoveUtility.calculateYawnMoveIndex(board, numberOfHalfMovesThreshold);

    return YawnMoveUtility.calculateYawnMoveRule(board.getInitialFen().havingMove(),
        board.getInitialFen().fullMoveNumber(), board.getHalfMoveList(), indexList, numberOfHalfMovesThreshold);
  }

  private static List<List<YawnHalfMove>> calculateYawnMoveRule(Side havingMoveInitial, int fullMoveNumberInitial,
      final List<HalfMove> halfMoveList, List<YawnIndex> indexList, int numberOfHalfMovesThreshold) {

    if (numberOfHalfMovesThreshold > ChessConstants.FIFTY_MOVE_RULE_HALF_MOVE_CLOCK_THRESHOLD) {
      throw new IllegalArgumentException("The threshold cannot be below fifty moves");
    }

    final List<List<YawnHalfMove>> resultList = new ArrayList<>();

    for (final YawnIndex sequence : indexList) {

      final List<YawnHalfMove> result = new ArrayList<>();
      // we add the initial entry
      {
        final var performedIndex = sequence.beginPerformedIndex();
        final var sequenceLength = 1;
        final YawnHalfMove firstEntry = YawnMoveUtility.calculateYawnHalfMove(havingMoveInitial, fullMoveNumberInitial,
            halfMoveList, performedIndex, sequenceLength);
        result.add(firstEntry);
      }

      final var halfMoveClockEnd = sequence.halfMoveClockEnd();
      if (halfMoveClockEnd < numberOfHalfMovesThreshold) {
        throw new ProgrammingMistakeException();
      }

      if (halfMoveClockEnd == numberOfHalfMovesThreshold) {
        // we add the last entry, which also ends the sequence
        {
          final var performedIndex = sequence.endPerformedIndex();
          final var sequenceLength = numberOfHalfMovesThreshold;
          final YawnHalfMove secondEntry = YawnMoveUtility.calculateYawnHalfMove(havingMoveInitial,
              fullMoveNumberInitial, halfMoveList, performedIndex, sequenceLength);
          result.add(secondEntry);
        }
        // we add the entry reaching the threshold if sequence below fifty moves
      } else if (numberOfHalfMovesThreshold < ChessConstants.FIFTY_MOVE_RULE_HALF_MOVE_CLOCK_THRESHOLD
          && halfMoveClockEnd < ChessConstants.FIFTY_MOVE_RULE_HALF_MOVE_CLOCK_THRESHOLD) {

        // we can add the move per index as last entry
        {
          final var performedIndex = sequence.endPerformedIndex();
          final var sequenceLength = halfMoveClockEnd;
          final YawnHalfMove lastEntry = YawnMoveUtility.calculateYawnHalfMove(havingMoveInitial, fullMoveNumberInitial,
              halfMoveList, performedIndex, sequenceLength);
          result.add(lastEntry);
        }
      } else {

        // we add the half move when the fifty-move rule becomes effective
        {
          final var performedIndex = sequence.beginPerformedIndex()
              + ChessConstants.FIFTY_MOVE_RULE_HALF_MOVE_CLOCK_THRESHOLD - 1;
          final var sequenceLength = ChessConstants.FIFTY_MOVE_RULE_HALF_MOVE_CLOCK_THRESHOLD;
          final YawnHalfMove intermediaryEntry = YawnMoveUtility.calculateYawnHalfMove(havingMoveInitial,
              fullMoveNumberInitial, halfMoveList, performedIndex, sequenceLength);
          result.add(intermediaryEntry);
        }

        if (halfMoveClockEnd < ChessConstants.SEVENTY_FIVE_MOVE_RULE_HALF_MOVE_CLOCK_THRESHOLD) {
          // we can add the move per index as last entry
          {
            final var performedIndex = sequence.endPerformedIndex();
            final var sequenceLength = halfMoveClockEnd;
            final YawnHalfMove lastEntry = YawnMoveUtility.calculateYawnHalfMove(havingMoveInitial,
                fullMoveNumberInitial, halfMoveList, performedIndex, sequenceLength);
            result.add(lastEntry);
          }

        } else if (halfMoveClockEnd == ChessConstants.SEVENTY_FIVE_MOVE_RULE_HALF_MOVE_CLOCK_THRESHOLD) {
          // we add the last entry, which also ends the sequence
          {
            final var performedIndex = sequence.endPerformedIndex();
            final var sequenceLength = ChessConstants.SEVENTY_FIVE_MOVE_RULE_HALF_MOVE_CLOCK_THRESHOLD;
            final YawnHalfMove secondEntry = YawnMoveUtility.calculateYawnHalfMove(havingMoveInitial,
                fullMoveNumberInitial, halfMoveList, performedIndex, sequenceLength);
            result.add(secondEntry);
          }
        } else {

          // we add the first half move when the seventy-five-move rule becomes effective
          // (the
          // 150th)
          {
            final var performedIndex = sequence.beginPerformedIndex()
                + ChessConstants.SEVENTY_FIVE_MOVE_RULE_HALF_MOVE_CLOCK_THRESHOLD - 1;
            final var sequenceLength = ChessConstants.SEVENTY_FIVE_MOVE_RULE_HALF_MOVE_CLOCK_THRESHOLD;
            final YawnHalfMove intermediaryEntry = YawnMoveUtility.calculateYawnHalfMove(havingMoveInitial,
                fullMoveNumberInitial, halfMoveList, performedIndex, sequenceLength);
            result.add(intermediaryEntry);
          }

          // we add the last half move of the sequence
          // we add the last entry, which also ends the sequence
          {
            final var performedIndex = sequence.endPerformedIndex();
            final var sequenceLength = halfMoveClockEnd;
            final YawnHalfMove thirdEntry = YawnMoveUtility.calculateYawnHalfMove(havingMoveInitial,
                fullMoveNumberInitial, halfMoveList, performedIndex, sequenceLength);
            result.add(thirdEntry);
          }
        }
      }
      resultList.add(result);
    }

    return resultList;
  }

  private static YawnHalfMove calculateYawnHalfMove(Side havingMoveInitial, int fullMoveNumberInitial,
      List<HalfMove> halfMoveList, int performedIndex, int sequenceLength) {

    if (performedIndex < 0) {
      return YawnMoveUtility.calculateYawnHalfMoveNotPerformed(havingMoveInitial, fullMoveNumberInitial, performedIndex,
          sequenceLength);
    }

    return YawnMoveUtility.calculateYawnHalfMovePerformed(havingMoveInitial, halfMoveList, performedIndex,
        sequenceLength);
  }

  private static YawnHalfMove calculateYawnHalfMoveNotPerformed(Side havingMoveInitial, int fullMoveNumberInitial,
      int performedIndex, int sequenceLength) {

    final var performedHalfMoveCount = performedIndex + 1;
    final var fullMoveNumber = BasicChessUtility.calculateFullMoveNumber(havingMoveInitial, fullMoveNumberInitial,
        performedHalfMoveCount);
    // not SAN
    final String san = BasicConstants.NA;
    final Side sideMoved = BasicChessUtility.calculateSideMoved(havingMoveInitial, performedHalfMoveCount);

    return new YawnHalfMove(performedHalfMoveCount, fullMoveNumber, san, sideMoved, sequenceLength);
  }

  private static YawnHalfMove calculateYawnHalfMovePerformed(Side havingMoveInitial, List<HalfMove> halfMoveList,
      int performedIndex, int sequenceLength) {

    final HalfMove firstHalfMove = NonNullWrapperCommon.get(halfMoveList, performedIndex);

    final var performedHalfMoveCount = performedIndex + 1;
    final var fullMoveNumber = firstHalfMove.fullMoveNumber();
    final String san = firstHalfMove.san();
    final Side sideMoved = BasicChessUtility.calculateSideMoved(havingMoveInitial, performedHalfMoveCount);

    return new YawnHalfMove(performedHalfMoveCount, fullMoveNumber, san, sideMoved, sequenceLength);
  }

  private static List<YawnIndex> calculateYawnMoveIndex(ApiBoard board, int numberOfHalfMovesThreshold) {

    if (numberOfHalfMovesThreshold > ChessConstants.FIFTY_MOVE_RULE_HALF_MOVE_CLOCK_THRESHOLD) {
      throw new IllegalArgumentException("The threshold cannot be below fifty moves");
    }

    final List<HalfMove> halfMoveList = board.getHalfMoveList();

    final List<YawnIndex> result = new ArrayList<>();

    // if the game starts from a FEN where the half-move clock meets the limit,
    // and begins with a pawn move or catpure, we add the initial sequence here
    if (calculateIsUseInitialFenHalfMoveClockOnly(halfMoveList)) {
      final var halfMoveClockEnd = board.getInitialFen().halfMoveClock();
      if (halfMoveClockEnd >= numberOfHalfMovesThreshold) {
        final var halfMoveCount = 0;

        final var beginHalfMoveNumber = halfMoveCount - halfMoveClockEnd + 1;
        final var endHalfMoveNumber = halfMoveCount;

        final var beginPerformedHalfMoveNumber = beginHalfMoveNumber - halfMoveCount;
        final var endPerformedHalfMoveNumber = endHalfMoveNumber - halfMoveCount;

        final var beginPerformedIndex = beginPerformedHalfMoveNumber - 1;
        final var endPerformedIndex = endPerformedHalfMoveNumber - 1;

        final YawnIndex sequence = new YawnIndex(beginPerformedIndex, endPerformedIndex, halfMoveClockEnd);

        result.add(sequence);
        // NO EARLY RETURN HERE
      }
    }

    final var maxIndex = halfMoveList.size() - 1;
    var endPerformedIndex = -1;
    for (var i = 0; i <= maxIndex; i++) {
      if (i <= endPerformedIndex) {
        // If we have found a sequence with 100 half moves until i = 213. Then the start
        // is at i = 213 - 100 + 1 = 114.
        // We will calculate when the sequence ends. When it for example ends after
        // total 137 half moves long. Then the
        // sequence ends at i = 114 + 137 - 1 = 250. So we arrived at i = 213 and we
        // resume looking for new sequences at
        // i = 251.
        continue;
      }
      final HalfMove halfMove = NonNullWrapperCommon.get(halfMoveList, i);
      final var halfMoveClock = halfMove.halfMoveClock();
      // we are only interested in sequences of length starting from required length
      // for fifty-move rule
      if (halfMoveClock >= numberOfHalfMovesThreshold) {
        final var beginPerformedIndex = i - halfMoveClock + 1;

        // we check the first move of the sequence if in the PGN
        // the first move can not be in the PGN if starting from non initial position
        if (beginPerformedIndex >= 0) {
          final HalfMove beginHalfMove = NonNullWrapperCommon.get(halfMoveList, beginPerformedIndex);
          final var beginSequenceLength = beginHalfMove.halfMoveClock();
          if (beginSequenceLength != 1) {
            throw new ProgrammingMistakeException();
          }
        }

        // it's easiest to calculate the end index here
        // it' not so nice for we do not follow the loop
        endPerformedIndex = calculateEndIndexSequence(halfMoveList, i);
        final HalfMove endHalfMove = NonNullWrapperCommon.get(halfMoveList, endPerformedIndex);
        final var halfMoveClockEnd = endHalfMove.halfMoveClock();
        final YawnIndex sequence = new YawnIndex(beginPerformedIndex, endPerformedIndex, halfMoveClockEnd);

        result.add(sequence);
      }
    }
    return result;
  }

  private static int calculateEndIndexSequence(List<HalfMove> halfMoveList, int index) {
    final var maxIndex = halfMoveList.size() - 1;
    if (index == maxIndex) {
      return index;
    }
    for (var j = index + 1; j <= maxIndex; j++) {
      final HalfMove halfMove = NonNullWrapperCommon.get(halfMoveList, j);
      if (Analyzer.calculateIsHalfMoveTerminatesYawnSequence(halfMove)) {
        return j - 1;
      }
    }
    return maxIndex;
  }

  private static boolean calculateIsUseInitialFenHalfMoveClockOnly(final List<HalfMove> halfMoveList) {
    if (halfMoveList.isEmpty()) {
      return true;
    }
    final HalfMove firstHalfMove = NonNullWrapperCommon.get(halfMoveList, 0);
    // first half move is a pawn move or capture
    // we must calculate the indexes using the FEN only as the half move clock was
    // reset at the first half-move, so by
    // our logic no sequence will be found
    return firstHalfMove.halfMoveClock() == 0;
  }
}
