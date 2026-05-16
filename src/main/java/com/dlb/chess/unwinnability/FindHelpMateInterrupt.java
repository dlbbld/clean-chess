package com.dlb.chess.unwinnability;

import java.util.HashMap;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.Nulls;
import com.dlb.chess.common.model.DynamicPosition;
import com.dlb.chess.common.ucimove.utility.UciMoveUtility;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.model.UciMove;

// Figure 5 Find-Helpmatec, the quick-check variant. Instance state carries the per-call
// transposition table (paper line 5-6: if (pos, D) in table with D >= d then return false) and the
// global "search-limit reached" signal (paper line 4: if cnt > nodesBound or d < 0 then return
// false; here we only enforce the depth bound). The recursion returns plain bool — true on a
// confirmed checkmate sequence for the intended winner, false otherwise — exactly as in Figure 5;
// limit-hit is a separate piece of information collected on the {@code isCanExhaust} flag and
// decided at the top of the search.
class FindHelpMateInterrupt {

  private static final boolean IS_DEBUG = false;

  private static final Logger logger = Nulls.getLogger(FindHelpMateInterrupt.class);

  // Our quick algorithm is extremely light, requiring only a few microseconds on average per
  // position. It is also sound, but not complete. However, as we detail in Section 5, with an
  // (empirically chosen) depth bound of D = 9, all incorrectly classified games from the Lichess
  // Database except three were correctly identified by Unwinnablequick.
  private static final int D = 9;

  public static FindHelpmateResult calculateHelpmate(Board board, Side c) {
    return new FindHelpMateInterrupt(c).search(board);
  }

  private final Side color;
  private final HashMap<DynamicPosition, Integer> transpositionMap = new HashMap<>();
  private boolean isCanExhaust = true;

  private FindHelpMateInterrupt(Side color) {
    this.color = color;
  }

  private FindHelpmateResult search(Board board) {
    final boolean found = searchRec(board, 0);
    if (found) {
      return FindHelpmateResult.YES;
    }
    if (!isCanExhaust) {
      return FindHelpmateResult.UNKNOWN;
    }
    return FindHelpmateResult.NO;
  }

  // Inputs: position, currentDepth.
  // Output: true if a checkmate sequence for the intended winner was found, false otherwise.
  // Side effect: isCanExhaust set to false if any recursive call hits the depth bound.
  private boolean searchRec(Board board, int currentDepth) {

    // 1: if the intended winner is checkmating their opponent in pos then return true
    if (board.isCheckmate() && board.getHavingMove() == color.getOppositeSide()) {
      return true;
    }

    // 2: terminal-loss conditions for the intended winner — return false. FIDE additions
    // (fivefold repetition / seventy-five-move rule) are folded in here because both auto-draw
    // and so close out the search for this branch.
    if (board.isInsufficientMaterial(color) || board.isFivefoldRepetition() || board.isSeventyFiveMove()) {
      return false;
    }

    // 4: if d < 0 then return false (search limit reached). Implemented as <= 0 to skip the
    // empty-budget child call — equivalent to the paper's d < 0 cutoff at the next level.
    final int movesLeft = D - currentDepth;
    if (movesLeft <= 0) {
      isCanExhaust = false;
      return false;
    }

    // 5: if (pos, D) in table with D >= d then return false (pos already analyzed at least this deep)
    final DynamicPosition cacheKey = board.getDynamicPosition();
    if (transpositionMap.containsKey(cacheKey)
        && Nulls.get(transpositionMap, cacheKey).intValue() >= movesLeft) {
      return false;
    }

    // 6: store (pos, d) in table
    transpositionMap.put(cacheKey, movesLeft);

    // 7-9: for every legal move m do: if FindHelpmate(pos.move(m), depth + 1) then return true
    for (final LegalMove legalMove : board.getLegalMoves()) {
      board.move(legalMove.moveSpecification());
      if (IS_DEBUG) {
        final UciMove uciMove = UciMoveUtility.convertMoveSpecificationToUci(legalMove.havingMove(),
            legalMove.moveSpecification());
        logger.printf(Level.DEBUG, "%s - %d", uciMove.text(), currentDepth + 1);
      }
      final boolean found = searchRec(board, currentDepth + 1);
      board.unmove();
      if (found) {
        return true;
      }
    }

    // 10: return false (no mate found after exploring every legal move)
    return false;
  }

}
