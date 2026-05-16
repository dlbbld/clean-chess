package com.dlb.chess.unwinnability;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.Nulls;
import com.dlb.chess.common.ucimove.utility.UciMoveUtility;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.model.UciMove;

class FindHelpMateInterrupt {

  private static final boolean IS_DEBUG = false;

  private static final Logger logger = Nulls.getLogger(FindHelpMateInterrupt.class);

  // Our quick algorithm is extremely light, requiring only a few microseconds on average per
  // position. It is also sound, but not complete. However, as we detail in Section 5, with an
  // (empirically chosen) depth bound of D = 9, all incorrectly classified games from the Lichess
  // Database except three were correctly identified by Unwinnablequick.
  private static final int D = 9;

  public static FindHelpmateResult calculateHelpmate(Board board, Side c) {
    final List<LegalMove> mateList = new ArrayList<>();
    final FindHelpMateInterruptResult result = calculateHelpmate(board, c, 0, mateList);

    return switch (result) {
      case TRUE -> FindHelpmateResult.YES;
      case FALSE -> FindHelpmateResult.NO;
      case INTERRUPTED -> FindHelpmateResult.UNKNOWN;
      default -> throw new IllegalArgumentException();
    };
  }

  private static FindHelpMateInterruptResult calculateHelpmate(Board board, Side c, int currentDepth,
      List<LegalMove> mateList) {
    final var isIntendedWinnerHavingCheckmate = board.isCheckmate() && board.getHavingMove() == c.getOppositeSide();
    if (isIntendedWinnerHavingCheckmate) {
      return FindHelpMateInterruptResult.TRUE;
    }

    if (currentDepth < D && !board.isInsufficientMaterial(c) && !board.isFivefoldRepetition()
        && !board.isSeventyFiveMove()) {

      var anyInterrupted = false;
      for (final LegalMove legalMove : board.getLegalMoves()) {
        board.move(legalMove.moveSpecification());
        if (IS_DEBUG) {
          final UciMove uciMove = UciMoveUtility.convertMoveSpecificationToUci(legalMove.havingMove(),
              legalMove.moveSpecification());
          logger.printf(Level.DEBUG, "%s - %d", uciMove.text(), currentDepth + 1);
        }

        mateList.add(legalMove);
        final var hasCheckmate = calculateHelpmate(board, c, currentDepth + 1, mateList);
        board.unmove();
        switch (hasCheckmate) {
          case TRUE -> {
            return FindHelpMateInterruptResult.TRUE;
          }
          case INTERRUPTED -> {
            // Paper Figure 5: limit-hit is a separate signal, not an abort. Set a flag and keep iterating —
            // a later root move may still mate within the depth bound. Returning INTERRUPTED here, as the
            // code did before, missed mates whose move was iterated after a deep non-mating subtree.
            mateList.remove(mateList.size() - 1);
            anyInterrupted = true;
          }
          case FALSE -> mateList.remove(mateList.size() - 1);
          default -> throw new IllegalArgumentException();
        }
      }
      if (anyInterrupted) {
        return FindHelpMateInterruptResult.INTERRUPTED;
      }
    }
    // search could have continued
    if (currentDepth == D) {
      return FindHelpMateInterruptResult.INTERRUPTED;
    }
    return FindHelpMateInterruptResult.FALSE;
  }

}
