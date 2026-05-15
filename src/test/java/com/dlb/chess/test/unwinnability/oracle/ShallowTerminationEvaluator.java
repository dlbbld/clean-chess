package com.dlb.chess.test.unwinnability.oracle;

import java.util.Set;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.enums.GameStatus;
import com.dlb.chess.test.unwinnability.oracle.enums.LimitedUnwinnabilityVerdict;

/**
 * Interprets a set of game statuses collected by {@link ShallowTerminationCalculator} at a fixed search depth and
 * returns the {@link LimitedUnwinnabilityVerdict} for the side being evaluated.
 *
 * <p>
 * Two entry points, distinguished by who chose the last move on the paths that produced the set:
 *
 * <ul>
 * <li>{@link #evaluateUnderOwnChoice} — the side-to-evaluate picked the last move on each path. OR semantics:
 * a single branch ending in {@code WIN} for the side-to-evaluate is enough to conclude {@code WINNABLE}; only if
 * every branch ends in {@code LOSS_OR_DRAW} does the verdict become {@code UNWINNABLE}; otherwise
 * {@code UNKNOWN}.</li>
 * <li>{@link #evaluateUnderOpponentChoice} — the opponent picked the last move on each path. AND semantics: the
 * verdict can only be {@code UNWINNABLE} (every branch ends in loss-or-draw for the side-to-evaluate) or
 * {@code UNKNOWN}; {@code WINNABLE} cannot be concluded because the opponent would just avoid any branch that
 * lets us win.</li>
 * </ul>
 *
 * <p>
 * Both entry points reduce to the same per-status {@link #classify} function, parameterised by the colour of the
 * side that just moved at the leaf — which is determined by the entry-point choice.
 */
public class ShallowTerminationEvaluator {

  /** Verdict of a single leaf status, from the side-to-evaluate's perspective. */
  private enum LeafOutcome {
    /** Side-to-evaluate has won at this leaf (delivered the mate). */
    WIN,
    /** Leaf is terminal and side-to-evaluate cannot win on this branch (draw, or side-to-evaluate was mated). */
    LOSS_OR_DRAW,
    /** Leaf isn't decisive — game ongoing, or a per-side-diagnostic status that doesn't favour or disfavour us. */
    INCONCLUSIVE
  }

  /**
   * Reduces the set under the assumption that the side-to-evaluate picked the last move on each path (so a single
   * winning branch suffices for {@code WINNABLE}).
   */
  static LimitedUnwinnabilityVerdict evaluateUnderOwnChoice(Set<GameStatus> statuses, Side sideToEvaluate) {
    return reduce(statuses, sideToEvaluate, sideToEvaluate, /* sideToEvaluatePicked */ true);
  }

  /**
   * Reduces the set under the assumption that the opponent picked the last move on each path (so we can never
   * conclude {@code WINNABLE} — opponent would avoid any winning branch — but we can conclude {@code UNWINNABLE}
   * if every branch ends in loss-or-draw for the side-to-evaluate).
   */
  static LimitedUnwinnabilityVerdict evaluateUnderOpponentChoice(Set<GameStatus> statuses, Side sideToEvaluate) {
    return reduce(statuses, sideToEvaluate, sideToEvaluate.getOppositeSide(), /* sideToEvaluatePicked */ false);
  }

  private static LimitedUnwinnabilityVerdict reduce(Set<GameStatus> statuses, Side sideToEvaluate,
      Side sideThatJustMoved, boolean sideToEvaluatePicked) {
    if (statuses.isEmpty()) {
      return LimitedUnwinnabilityVerdict.UNKNOWN;
    }
    var allLossOrDraw = true;
    for (final GameStatus status : statuses) {
      switch (classify(status, sideToEvaluate, sideThatJustMoved)) {
        case WIN:
          if (sideToEvaluatePicked) {
            return LimitedUnwinnabilityVerdict.WINNABLE;
          }
          // Under opponent's choice, a WIN-branch only proves "possibly winnable"; opponent would just avoid it.
          // Doesn't help us conclude UNWINNABLE either (opponent might have a winning branch of their own).
          allLossOrDraw = false;
          break;
        case INCONCLUSIVE:
          allLossOrDraw = false;
          break;
        case LOSS_OR_DRAW:
          // keep going
          break;
        default:
          throw new IllegalArgumentException();
      }
    }
    return allLossOrDraw ? LimitedUnwinnabilityVerdict.UNWINNABLE : LimitedUnwinnabilityVerdict.UNKNOWN;
  }

  /**
   * Classifies a single leaf status from the perspective of {@code sideToEvaluate}, given that
   * {@code sideThatJustMoved} made the move producing the leaf.
   */
  private static LeafOutcome classify(GameStatus status, Side sideToEvaluate, Side sideThatJustMoved) {
    return switch (status) {
      case CHECKMATE -> sideThatJustMoved == sideToEvaluate ? LeafOutcome.WIN : LeafOutcome.LOSS_OR_DRAW;
      case STALEMATE, DEAD_POSITION_INSUFFICIENT_MATERIAL, DEAD_POSITION_UNWINNABLE_QUICK, FIVE_FOLD_REPETITION_RULE, SEVENTY_FIVE_MOVE_RULE -> LeafOutcome.LOSS_OR_DRAW;
      case INSUFFICIENT_MATERIAL_WHITE_ONLY -> sideToEvaluate == Side.WHITE
          ? LeafOutcome.LOSS_OR_DRAW
          : LeafOutcome.INCONCLUSIVE;
      case INSUFFICIENT_MATERIAL_BLACK_ONLY -> sideToEvaluate == Side.BLACK
          ? LeafOutcome.LOSS_OR_DRAW
          : LeafOutcome.INCONCLUSIVE;
      case ONGOING -> LeafOutcome.INCONCLUSIVE;
    };
  }

}
