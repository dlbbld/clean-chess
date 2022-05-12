package com.dlb.chess.winnable;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.enums.GameStatus;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.utility.PawnWallUtility;
import com.dlb.chess.winnable.enums.Winnable;
import com.dlb.chess.winnable.model.EvaluatePositions;
import com.dlb.chess.winnable.model.GameForced;

public class WinnableUtility {

  public static final int MAX_NUMBER_OF_HALF_MOVES_FIRST_HALF_MOVE = 10;

  private static final Logger logger = NonNullWrapperCommon.getLogger(WinnableUtility.class);

  public static Winnable calculateWinnable(ApiBoard board, Side side) {
    if (board.getHavingMove() == side) {
      return calculateWinnableHavingMove(board);
    }
    return calculateWinnableNotHavingMove(board);
  }

  private static Winnable calculateWinnableHavingMove(ApiBoard board) {

    final Side sideToEvaluate = board.getHavingMove();

    if (board.isCheckmate() || board.isStalemate() || board.isFivefoldRepetition() || board.isSeventyFiftyMove()) {
      return Winnable.NO;
    }

    if (board.getLegalMovesRepresentation().isEmpty()) {
      throw new ProgrammingMistakeException("At this point we must have at least one legal move");
    }

    {
      final GameForced forced = WinnableCalculateGameState.evaluateForcedLine(board);
      logger.printf(Level.DEBUG, "forced;madeTheMove: %s", forced.evaluatedPositions());
      final Winnable winnable = calculateWinnableForced(forced, sideToEvaluate);

      if (winnable != Winnable.UNKNOWN) {
        return winnable;
      }
    }

    {
      final EvaluatePositions evaluatePositions = WinnableCalculateGameState.evaluateFirstHalfMoveMadeTheMove(board);
      logger.printf(Level.DEBUG, "first;madeTheMove: %s", evaluatePositions.evaluatedPositions());
      final Winnable winnable = WinnableEvaluateGameState.calculateWinnableMadeTheMove(evaluatePositions.gameStatus());

      if (winnable != Winnable.UNKNOWN) {
        return winnable;
      }
    }

    if (board.getLegalMovesRepresentation().size() <= MAX_NUMBER_OF_HALF_MOVES_FIRST_HALF_MOVE) {
      {
        final EvaluatePositions evaluatePositions = WinnableCalculateGameState.evaluateSecondHalfMoveNotMadeTheMove(board);
        logger.printf(Level.DEBUG, "second;madeTheMove: %s", evaluatePositions.evaluatedPositions());
        final Winnable winnable = WinnableEvaluateGameState.calculateWinnableNotMadeTheMove(evaluatePositions.gameStatus());

        if (winnable != Winnable.UNKNOWN) {
          return winnable;
        }
      }

      {
        final EvaluatePositions evaluatePositions = WinnableCalculateGameState.evaluateThirdHalfMoveMadeTheMove(board);
        logger.printf(Level.DEBUG, "third;madeTheMove: %s", evaluatePositions.evaluatedPositions());
        final Winnable winnable = WinnableEvaluateGameState.calculateWinnableMadeTheMove(evaluatePositions.gameStatus());

        if (winnable != Winnable.UNKNOWN) {
          return winnable;
        }
      }
    }

    if (PawnWallUtility.calculateHasPawnWall(board)) {
      return Winnable.NO;
    }

    return Winnable.UNKNOWN;
  }

  public static Winnable calculateWinnableNotHavingMove(ApiBoard board) {
    final Side sideToEvaluate = board.getHavingMove().getOppositeSide();

    if (board.isCheckmate()) {
      return Winnable.YES;
    }

    if (board.isStalemate() || board.isFivefoldRepetition() || board.isSeventyFiftyMove()) {
      return Winnable.NO;
    }

    if (board.getLegalMovesRepresentation().isEmpty()) {
      throw new ProgrammingMistakeException("At this point we must have at least one legal move");
    }

    {
      final GameForced forced = WinnableCalculateGameState.evaluateForcedLine(board);
      logger.printf(Level.DEBUG, "forced;notMadeTheMove: %s", forced.evaluatedPositions());
      final Winnable winnable = calculateWinnableForced(forced, sideToEvaluate);

      if (winnable != Winnable.UNKNOWN) {
        return winnable;
      }
    }

    {
      final EvaluatePositions evaluatePositions = WinnableCalculateGameState.evaluateFirstHalfMoveNotMadeTheMove(board);
      logger.printf(Level.DEBUG, "first;notMadeTheMove: %s", evaluatePositions.evaluatedPositions());
      final Winnable winnable = WinnableEvaluateGameState.calculateWinnableNotMadeTheMove(evaluatePositions.gameStatus());

      if (winnable != Winnable.UNKNOWN) {
        return winnable;
      }
    }

    if (board.getLegalMovesRepresentation().size() <= MAX_NUMBER_OF_HALF_MOVES_FIRST_HALF_MOVE) {
      {
        final EvaluatePositions evaluatePositions = WinnableCalculateGameState.evaluateSecondHalfMoveMadeTheMove(board);
        logger.printf(Level.DEBUG, "second;notMadeTheMove: %s", evaluatePositions.evaluatedPositions());
        final Winnable winnable = WinnableEvaluateGameState.calculateWinnableMadeTheMove(evaluatePositions.gameStatus());

        if (winnable != Winnable.UNKNOWN) {
          return winnable;
        }
      }

      {
        final EvaluatePositions evaluatePositions = WinnableCalculateGameState.evaluateThirdHalfMoveNotMadeTheMove(board);
        logger.printf(Level.DEBUG, "third;notMadeTheMove: %s", evaluatePositions.evaluatedPositions());
        final Winnable winnable = WinnableEvaluateGameState.calculateWinnableNotMadeTheMove(evaluatePositions.gameStatus());

        if (winnable != Winnable.UNKNOWN) {
          return winnable;
        }
      }
    }

    if (PawnWallUtility.calculateHasPawnWall(board)) {
      return Winnable.NO;
    }

    return Winnable.UNKNOWN;
  }

  private static Winnable calculateWinnableForced(GameForced gameForced, Side sideToEvaluate) {
    if (sideToEvaluate == gameForced.sideMadeLastMove()) {
      return calculateWinnableForcedMadeLastMove(gameForced.gameStatus());
    }
    return calculateWinnableForcedNotMadeLastMove(gameForced.gameStatus());
  }

  private static Winnable calculateWinnableForcedMadeLastMove(GameStatus gameStatusFinal) {
    switch (gameStatusFinal) {
      case CHECKMATE:
        return Winnable.YES;
      case STALEMATE:
      case INSUFFICIENT_MATERIAL_BOTH:
      case INSUFFICIENT_MATERIAL_MADE_THE_MOVE_ONLY:
      case FIVE_FOLD_REPETITION_RULE:
      case SEVENTY_FIVE_MOVE_RULE:
        return Winnable.NO;
      case INSUFFICIENT_MATERIAL_NOT_MADE_THE_MOVE_ONLY:
      case OTHER:
        return Winnable.UNKNOWN;
      default:
        throw new IllegalArgumentException();
    }
  }

  private static Winnable calculateWinnableForcedNotMadeLastMove(GameStatus gameStatusFinal) {
    switch (gameStatusFinal) {
      case CHECKMATE:
      case STALEMATE:
      case INSUFFICIENT_MATERIAL_BOTH:
      case INSUFFICIENT_MATERIAL_NOT_MADE_THE_MOVE_ONLY:
      case FIVE_FOLD_REPETITION_RULE:
      case SEVENTY_FIVE_MOVE_RULE:
        return Winnable.NO;
      case INSUFFICIENT_MATERIAL_MADE_THE_MOVE_ONLY:
      case OTHER:
        return Winnable.UNKNOWN;
      default:
        throw new IllegalArgumentException();
    }
  }

}
