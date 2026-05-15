package com.dlb.chess.test.unwinnability.oracle;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.Nulls;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.test.unwinnability.oracle.enums.LimitedUnwinnabilityVerdict;
import com.dlb.chess.test.unwinnability.oracle.model.EvaluatePositions;

/**
 * Test-only oracle that performs a bounded 1/2/3-half-move scan over <em>all</em> legal moves at the root and
 * reports the verdict implied by any terminal status reachable inside that scan window. The scan stops at the
 * first depth where a non-{@code UNKNOWN} verdict can be concluded.
 *
 * <p>
 * This oracle deliberately does <em>not</em> walk the forced unique-move chain — that is
 * {@link ForcedLineOracle}'s job. The two are kept separate so each can be exercised in isolation:
 *
 * <ul>
 * <li>{@code ForcedLineOracle} — tested against {@code PgnTest.BASIC_FORCED}.</li>
 * <li>{@code ShallowTerminationOracle} — tested against {@code PgnTest.CHA_SHALLOW_TERMINATION}.</li>
 * </ul>
 *
 * <p>
 * {@link LimitedUnwinnabilityOracle} composes both plus the pawn-wall analyzer.
 *
 * <p>
 * The oracle is self-contained: it performs its own pre-checks for terminal-at-the-root and insufficient-material
 * positions, so callers do not need to filter the board beforehand.
 *
 * <p>
 * Note: there is no branching-factor gate on the depth-2/3 scan. Worst-case work is bounded by the legal-move
 * count cubed, which on a 64-square board with normal material is well within test-time budgets.
 */
public class ShallowTerminationOracle {

  private static final Logger logger = Nulls.getLogger(ShallowTerminationOracle.class);

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

    {
      final EvaluatePositions evaluatePositions = ShallowTerminationCalculator.evaluateFirstHalfMoveMadeTheMove(board);
      logger.printf(Level.DEBUG, "first;madeTheMove: %s", evaluatePositions.evaluatedPositions());
      final LimitedUnwinnabilityVerdict verdict = ShallowTerminationEvaluator
          .calculateUnwinnabilityMadeTheMove(evaluatePositions.gameStatus());

      if (verdict != LimitedUnwinnabilityVerdict.UNKNOWN) {
        return verdict;
      }
    }

    {
      final EvaluatePositions evaluatePositions = ShallowTerminationCalculator
          .evaluateSecondHalfMoveNotMadeTheMove(board);
      logger.printf(Level.DEBUG, "second;madeTheMove: %s", evaluatePositions.evaluatedPositions());
      final LimitedUnwinnabilityVerdict verdict = ShallowTerminationEvaluator
          .calculateUnwinnabilityNotMadeTheMove(evaluatePositions.gameStatus());

      if (verdict != LimitedUnwinnabilityVerdict.UNKNOWN) {
        return verdict;
      }
    }

    {
      final EvaluatePositions evaluatePositions = ShallowTerminationCalculator.evaluateThirdHalfMoveMadeTheMove(board);
      logger.printf(Level.DEBUG, "third;madeTheMove: %s", evaluatePositions.evaluatedPositions());
      final LimitedUnwinnabilityVerdict verdict = ShallowTerminationEvaluator
          .calculateUnwinnabilityMadeTheMove(evaluatePositions.gameStatus());

      if (verdict != LimitedUnwinnabilityVerdict.UNKNOWN) {
        return verdict;
      }
    }

    return LimitedUnwinnabilityVerdict.UNKNOWN;
  }

  private static LimitedUnwinnabilityVerdict calculateUnwinnabilityNotHavingMove(Board board) {

    {
      final EvaluatePositions evaluatePositions = ShallowTerminationCalculator
          .evaluateFirstHalfMoveNotMadeTheMove(board);
      logger.printf(Level.DEBUG, "first;notMadeTheMove: %s", evaluatePositions.evaluatedPositions());
      final LimitedUnwinnabilityVerdict verdict = ShallowTerminationEvaluator
          .calculateUnwinnabilityNotMadeTheMove(evaluatePositions.gameStatus());

      if (verdict != LimitedUnwinnabilityVerdict.UNKNOWN) {
        return verdict;
      }
    }

    {
      final EvaluatePositions evaluatePositions = ShallowTerminationCalculator.evaluateSecondHalfMoveMadeTheMove(board);
      logger.printf(Level.DEBUG, "second;notMadeTheMove: %s", evaluatePositions.evaluatedPositions());
      final LimitedUnwinnabilityVerdict verdict = calculateUnwinnabilityMadeTheMoveAfterOpponentChoice(
          evaluatePositions);

      if (verdict != LimitedUnwinnabilityVerdict.UNKNOWN) {
        return verdict;
      }
    }

    {
      final EvaluatePositions evaluatePositions = ShallowTerminationCalculator
          .evaluateThirdHalfMoveNotMadeTheMove(board);
      logger.printf(Level.DEBUG, "third;notMadeTheMove: %s", evaluatePositions.evaluatedPositions());
      final LimitedUnwinnabilityVerdict verdict = ShallowTerminationEvaluator
          .calculateUnwinnabilityNotMadeTheMove(evaluatePositions.gameStatus());

      if (verdict != LimitedUnwinnabilityVerdict.UNKNOWN) {
        return verdict;
      }
    }

    return LimitedUnwinnabilityVerdict.UNKNOWN;
  }

  private static LimitedUnwinnabilityVerdict calculateUnwinnabilityMadeTheMoveAfterOpponentChoice(
      EvaluatePositions evaluatePositions) {
    final LimitedUnwinnabilityVerdict verdict = ShallowTerminationEvaluator
        .calculateUnwinnabilityMadeTheMove(evaluatePositions.gameStatus());
    // The opponent chose the first move in this branch; a later mating reply is only a possible win, not a forced
    // shallow-termination conclusion. Forced single-move lines are handled before this bounded search.
    if (verdict == LimitedUnwinnabilityVerdict.WINNABLE) {
      return LimitedUnwinnabilityVerdict.UNKNOWN;
    }
    return verdict;
  }

}
