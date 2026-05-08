package com.dlb.chess.pgn.diagnostic;

import java.util.List;

import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.ChessConstants;
import com.dlb.chess.common.enums.EnPassantCaptureRuleThreefold;
import com.dlb.chess.common.model.HalfMove;
import com.dlb.chess.common.utility.RepetitionUtility;
import com.dlb.chess.report.model.YawnHalfMove;

/**
 * Standalone scanner that detects whether a sequence of halfmoves continued past one of the two
 * FIDE-automatic, path-dependent terminations: fivefold repetition or the 75-move rule.
 *
 * <p>This utility is intended for corpus-mining workflows: scanning historical PGN databases to
 * identify games whose recorded halfmove sequence continued beyond the point at which the game
 * would have ended automatically under current FIDE rules. It operates on raw halfmove data and
 * does not interact with the strict move-validation pipeline.
 *
 * <p>The strict pipeline ({@code ValidateNewMove}, {@code SanValidation}) rejects further moves
 * once a terminal status is reached. As a result, a normally replayed {@code Board} will never
 * carry halfmove data that satisfies these predicates — they are only useful for offline scans
 * of pre-existing halfmove sequences obtained outside the strict pipeline.
 *
 * <p>Position-only terminations (checkmate, stalemate, insufficient material) are not addressed
 * here because they are not path-dependent and are queryable directly from the current position.
 */
public final class GameContinuationScanner {

  private GameContinuationScanner() {
  }

  /**
   * Returns {@code true} iff the supplied halfmove sequence reached a fivefold repetition at
   * some halfmove and continued past it (i.e., the fivefold-repeated halfmove is not the last in
   * the sequence).
   */
  public static boolean isContinuedOverFivefoldRepetition(List<HalfMove> halfMoveList) {
    for (final HalfMove halfMove : halfMoveList) {
      final var countRepetition = RepetitionUtility.getCountRepetition(halfMove,
          EnPassantCaptureRuleThreefold.DO_NOT_IGNORE);
      if (countRepetition >= ChessConstants.FIVEFOLD_REPETITION_RULE_THRESHOLD) {
        return !halfMove.equals(NonNullWrapperCommon.getLast(halfMoveList));
      }
    }
    return false;
  }

  /**
   * Returns {@code true} iff the supplied yawn-move sequence list contains a sequence whose
   * length strictly exceeds the 75-move rule threshold (150 halfmoves), indicating that play
   * continued past the automatic 75-move termination.
   */
  public static boolean isContinuedOverSeventyFiveMove(List<List<YawnHalfMove>> yawnMoveListList) {
    for (final List<YawnHalfMove> list : yawnMoveListList) {
      final YawnHalfMove lastYawnHalfMove = NonNullWrapperCommon.getLast(list);

      if (lastYawnHalfMove.sequenceLength() > ChessConstants.SEVENTY_FIVE_MOVE_RULE_HALF_MOVE_CLOCK_THRESHOLD) {
        return true;
      }
    }
    return false;
  }

}
