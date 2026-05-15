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
    // Suppress dead-position auto-detect while we scan the 1/2/3-ply tree via board.move(...).
    return board.withDeadPositionDetectionSuppressed(() -> calculateUnwinnabilityInternal(board, side));
  }

  private static LimitedUnwinnabilityVerdict calculateUnwinnabilityInternal(Board board, Side side) {

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
      return calculateUnwinnabilityHavingMove(board, side);
    }
    return calculateUnwinnabilityNotHavingMove(board, side);
  }

  /**
   * Side-to-evaluate has the move at the root. Search alternates: depth 1 = side-to-evaluate, depth 2 = opponent,
   * depth 3 = side-to-evaluate. So the "who picked the last move" at depth N is side-to-evaluate for odd N and
   * opponent for even N.
   */
  private static LimitedUnwinnabilityVerdict calculateUnwinnabilityHavingMove(Board board, Side sideToEvaluate) {

    {
      final EvaluatePositions evaluatePositions = ShallowTerminationCalculator.evaluateFirstHalfMoveMadeTheMove(board);
      logger.printf(Level.DEBUG, "first;madeTheMove: %s", evaluatePositions.evaluatedPositions());
      final LimitedUnwinnabilityVerdict verdict = ShallowTerminationEvaluator
          .evaluateUnderOwnChoice(evaluatePositions.gameStatus(), sideToEvaluate);

      if (verdict != LimitedUnwinnabilityVerdict.UNKNOWN) {
        return verdict;
      }
    }

    {
      final EvaluatePositions evaluatePositions = ShallowTerminationCalculator
          .evaluateSecondHalfMoveNotMadeTheMove(board);
      logger.printf(Level.DEBUG, "second;madeTheMove: %s", evaluatePositions.evaluatedPositions());
      final LimitedUnwinnabilityVerdict verdict = ShallowTerminationEvaluator
          .evaluateUnderOpponentChoice(evaluatePositions.gameStatus(), sideToEvaluate);

      if (verdict != LimitedUnwinnabilityVerdict.UNKNOWN) {
        return verdict;
      }
    }

    {
      final EvaluatePositions evaluatePositions = ShallowTerminationCalculator.evaluateThirdHalfMoveMadeTheMove(board);
      logger.printf(Level.DEBUG, "third;madeTheMove: %s", evaluatePositions.evaluatedPositions());
      final LimitedUnwinnabilityVerdict verdict = ShallowTerminationEvaluator
          .evaluateUnderOwnChoice(evaluatePositions.gameStatus(), sideToEvaluate);

      if (verdict != LimitedUnwinnabilityVerdict.UNKNOWN) {
        return verdict;
      }
    }

    return LimitedUnwinnabilityVerdict.UNKNOWN;
  }

  /**
   * Side-to-evaluate does NOT have the move at the root (opponent moves first). Search alternates: depth 1 =
   * opponent, depth 2 = side-to-evaluate, depth 3 = opponent. So "who picked the last move" at depth N is
   * opponent for odd N and side-to-evaluate for even N.
   */
  private static LimitedUnwinnabilityVerdict calculateUnwinnabilityNotHavingMove(Board board, Side sideToEvaluate) {

    {
      final EvaluatePositions evaluatePositions = ShallowTerminationCalculator
          .evaluateFirstHalfMoveNotMadeTheMove(board);
      logger.printf(Level.DEBUG, "first;notMadeTheMove: %s", evaluatePositions.evaluatedPositions());
      final LimitedUnwinnabilityVerdict verdict = ShallowTerminationEvaluator
          .evaluateUnderOpponentChoice(evaluatePositions.gameStatus(), sideToEvaluate);

      if (verdict != LimitedUnwinnabilityVerdict.UNKNOWN) {
        return verdict;
      }
    }

    {
      final EvaluatePositions evaluatePositions = ShallowTerminationCalculator.evaluateSecondHalfMoveMadeTheMove(board);
      logger.printf(Level.DEBUG, "second;notMadeTheMove: %s", evaluatePositions.evaluatedPositions());
      // Depth 2 here: side-to-evaluate picked the last move, but opponent picked at depth 1.
      // evaluateUnderOwnChoice would treat a CHECKMATE branch as WINNABLE, but that's unsound here because
      // opponent's depth-1 choice could have avoided the path leading to that CHECKMATE. Switch to
      // evaluateUnderOpponentChoice — it correctly returns UNKNOWN on any WIN branch and only UNWINNABLE on
      // all-LOSS_OR_DRAW.
      final LimitedUnwinnabilityVerdict verdict = ShallowTerminationEvaluator
          .evaluateUnderOpponentChoice(evaluatePositions.gameStatus(), sideToEvaluate);

      if (verdict != LimitedUnwinnabilityVerdict.UNKNOWN) {
        return verdict;
      }
    }

    {
      final EvaluatePositions evaluatePositions = ShallowTerminationCalculator
          .evaluateThirdHalfMoveNotMadeTheMove(board);
      logger.printf(Level.DEBUG, "third;notMadeTheMove: %s", evaluatePositions.evaluatedPositions());
      final LimitedUnwinnabilityVerdict verdict = ShallowTerminationEvaluator
          .evaluateUnderOpponentChoice(evaluatePositions.gameStatus(), sideToEvaluate);

      if (verdict != LimitedUnwinnabilityVerdict.UNKNOWN) {
        return verdict;
      }
    }

    return LimitedUnwinnabilityVerdict.UNKNOWN;
  }

}
