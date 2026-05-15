package com.dlb.chess.test.unwinnability.oracle;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.Nulls;
import com.dlb.chess.common.enums.GameStatus;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.test.unwinnability.oracle.enums.LimitedUnwinnabilityVerdict;
import com.dlb.chess.test.unwinnability.oracle.model.EvaluatePositions;
import com.dlb.chess.test.unwinnability.oracle.model.GameForced;

public class LimitedUnwinnabilityOracle {

  public static final int MAX_NUMBER_OF_HALF_MOVES_FIRST_HALF_MOVE = 10;

  private static final Logger logger = Nulls.getLogger(LimitedUnwinnabilityOracle.class);

  // TODO test cases
  // ambrona_10.pgn
  // we return unwinnable but is winnable. that is a bug.

  // lichess_a2l4gphm.pgn
  // we return unknown but we could see that is is unwinnable. here there is a possible improvement.
  // we have a problem with an enemy pawn on the same square type as our bishop

  public static LimitedUnwinnabilityVerdict calculateUnwinnability(Board board, Side side) {

    if (board.isCheckmate()) {
      if (side == board.getHavingMove()) {
        return LimitedUnwinnabilityVerdict.UNWINNABLE;
      }
      return LimitedUnwinnabilityVerdict.WINNABLE;
    }

    if (board.isInsufficientMaterial(side) || board.isStalemate() || board.isFivefoldRepetition()
        || board.isSeventyFiveMove()) {
      return LimitedUnwinnabilityVerdict.UNWINNABLE;
    }

    if (board.getLegalMoves().isEmpty()) {
      throw new ProgrammingMistakeException("At this point we must have at least one legal move");
    }

    if (board.getHavingMove() == side) {
      return calculateUnwinnabilityHavingMove(board);
    }
    return calculateUnwinnabilityNotHavingMove(board);
  }

  private static LimitedUnwinnabilityVerdict calculateUnwinnabilityHavingMove(Board board) {

    final Side sideToEvaluate = board.getHavingMove();

    {
      final GameForced forced = LimitedUnwinnabilityGameStateCalculator.evaluateForcedLine(board);
      logger.printf(Level.DEBUG, "forced;madeTheMove: %s", forced.evaluatedPositions());
      final LimitedUnwinnabilityVerdict verdict = calculateUnwinnabilityForced(forced, sideToEvaluate);

      if (verdict != LimitedUnwinnabilityVerdict.UNKNOWN) {
        return verdict;
      }
    }

    {
      final EvaluatePositions evaluatePositions = LimitedUnwinnabilityGameStateCalculator.evaluateFirstHalfMoveMadeTheMove(board);
      logger.printf(Level.DEBUG, "first;madeTheMove: %s", evaluatePositions.evaluatedPositions());
      final LimitedUnwinnabilityVerdict verdict = LimitedUnwinnabilityGameStateEvaluator.calculateUnwinnabilityMadeTheMove(evaluatePositions.gameStatus());

      if (verdict != LimitedUnwinnabilityVerdict.UNKNOWN) {
        return verdict;
      }
    }

    if (board.getLegalMoves().size() <= MAX_NUMBER_OF_HALF_MOVES_FIRST_HALF_MOVE) {
      {
        final EvaluatePositions evaluatePositions = LimitedUnwinnabilityGameStateCalculator
            .evaluateSecondHalfMoveNotMadeTheMove(board);
        logger.printf(Level.DEBUG, "second;madeTheMove: %s", evaluatePositions.evaluatedPositions());
        final LimitedUnwinnabilityVerdict verdict = LimitedUnwinnabilityGameStateEvaluator
            .calculateUnwinnabilityNotMadeTheMove(evaluatePositions.gameStatus());

        if (verdict != LimitedUnwinnabilityVerdict.UNKNOWN) {
          return verdict;
        }
      }

      {
        final EvaluatePositions evaluatePositions = LimitedUnwinnabilityGameStateCalculator.evaluateThirdHalfMoveMadeTheMove(board);
        logger.printf(Level.DEBUG, "third;madeTheMove: %s", evaluatePositions.evaluatedPositions());
        final LimitedUnwinnabilityVerdict verdict = LimitedUnwinnabilityGameStateEvaluator
            .calculateUnwinnabilityMadeTheMove(evaluatePositions.gameStatus());

        if (verdict != LimitedUnwinnabilityVerdict.UNKNOWN) {
          return verdict;
        }
      }
    }

    if (PawnWallGeometricAnalyzer.calculate(board) == PawnWallVerdict.YES) {
      return LimitedUnwinnabilityVerdict.UNWINNABLE;
    }

    return LimitedUnwinnabilityVerdict.UNKNOWN;
  }

  private static LimitedUnwinnabilityVerdict calculateUnwinnabilityNotHavingMove(Board board) {

    final Side sideToEvaluate = board.getHavingMove().getOppositeSide();

    if (board.getLegalMoves().isEmpty()) {
      throw new ProgrammingMistakeException("At this point we must have at least one legal move");
    }

    {
      final GameForced forced = LimitedUnwinnabilityGameStateCalculator.evaluateForcedLine(board);
      logger.printf(Level.DEBUG, "forced;notMadeTheMove: %s", forced.evaluatedPositions());
      final LimitedUnwinnabilityVerdict verdict = calculateUnwinnabilityForced(forced, sideToEvaluate);

      if (verdict != LimitedUnwinnabilityVerdict.UNKNOWN) {
        return verdict;
      }
    }

    {
      final EvaluatePositions evaluatePositions = LimitedUnwinnabilityGameStateCalculator.evaluateFirstHalfMoveNotMadeTheMove(board);
      logger.printf(Level.DEBUG, "first;notMadeTheMove: %s", evaluatePositions.evaluatedPositions());
      final LimitedUnwinnabilityVerdict verdict = LimitedUnwinnabilityGameStateEvaluator
          .calculateUnwinnabilityNotMadeTheMove(evaluatePositions.gameStatus());

      if (verdict != LimitedUnwinnabilityVerdict.UNKNOWN) {
        return verdict;
      }
    }

    if (board.getLegalMoves().size() <= MAX_NUMBER_OF_HALF_MOVES_FIRST_HALF_MOVE) {
      {
        final EvaluatePositions evaluatePositions = LimitedUnwinnabilityGameStateCalculator.evaluateSecondHalfMoveMadeTheMove(board);
        logger.printf(Level.DEBUG, "second;notMadeTheMove: %s", evaluatePositions.evaluatedPositions());
        final LimitedUnwinnabilityVerdict verdict = LimitedUnwinnabilityGameStateEvaluator
            .calculateUnwinnabilityMadeTheMove(evaluatePositions.gameStatus());

        if (verdict != LimitedUnwinnabilityVerdict.UNKNOWN) {
          return verdict;
        }
      }

      {
        final EvaluatePositions evaluatePositions = LimitedUnwinnabilityGameStateCalculator
            .evaluateThirdHalfMoveNotMadeTheMove(board);
        logger.printf(Level.DEBUG, "third;notMadeTheMove: %s", evaluatePositions.evaluatedPositions());
        final LimitedUnwinnabilityVerdict verdict = LimitedUnwinnabilityGameStateEvaluator
            .calculateUnwinnabilityNotMadeTheMove(evaluatePositions.gameStatus());

        if (verdict != LimitedUnwinnabilityVerdict.UNKNOWN) {
          return verdict;
        }
      }
    }

    if (PawnWallGeometricAnalyzer.calculate(board) == PawnWallVerdict.YES) {
      return LimitedUnwinnabilityVerdict.UNWINNABLE;
    }

    return LimitedUnwinnabilityVerdict.UNKNOWN;
  }

  private static LimitedUnwinnabilityVerdict calculateUnwinnabilityForced(GameForced gameForced, Side sideToEvaluate) {
    if (sideToEvaluate == gameForced.sideMadeLastMove()) {
      return calculateUnwinnabilityForcedMadeLastMove(gameForced.gameStatus());
    }
    return calculateUnwinnabilityForcedNotMadeLastMove(gameForced.gameStatus());
  }

  private static LimitedUnwinnabilityVerdict calculateUnwinnabilityForcedMadeLastMove(GameStatus gameStatusFinal) {
    return switch (gameStatusFinal) {
      case CHECKMATE -> LimitedUnwinnabilityVerdict.WINNABLE;
      case STALEMATE, INSUFFICIENT_MATERIAL_BOTH, INSUFFICIENT_MATERIAL_MADE_THE_MOVE_ONLY, FIVE_FOLD_REPETITION_RULE, SEVENTY_FIVE_MOVE_RULE -> LimitedUnwinnabilityVerdict.UNWINNABLE;
      case INSUFFICIENT_MATERIAL_NOT_MADE_THE_MOVE_ONLY, ONGOING -> LimitedUnwinnabilityVerdict.UNKNOWN;
      default -> throw new IllegalArgumentException();
    };
  }

  private static LimitedUnwinnabilityVerdict calculateUnwinnabilityForcedNotMadeLastMove(GameStatus gameStatusFinal) {
    return switch (gameStatusFinal) {
      case CHECKMATE, STALEMATE, INSUFFICIENT_MATERIAL_BOTH, INSUFFICIENT_MATERIAL_NOT_MADE_THE_MOVE_ONLY, FIVE_FOLD_REPETITION_RULE, SEVENTY_FIVE_MOVE_RULE -> LimitedUnwinnabilityVerdict.UNWINNABLE;
      case INSUFFICIENT_MATERIAL_MADE_THE_MOVE_ONLY, ONGOING -> LimitedUnwinnabilityVerdict.UNKNOWN;
      default -> throw new IllegalArgumentException();
    };
  }

}
