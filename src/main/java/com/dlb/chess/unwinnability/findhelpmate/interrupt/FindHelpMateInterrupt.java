package com.dlb.chess.unwinnability.findhelpmate.interrupt;

import java.util.ArrayList;
import java.util.List;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.ucimove.utility.UciMoveUtility;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.model.UciMove;
import com.dlb.chess.unwinnability.findhelpmate.AbstractFindHelpmate;
import com.dlb.chess.unwinnability.findhelpmate.enums.FindHelpmateResult;
import com.dlb.chess.unwinnability.findhelpmate.interrupt.enums.FindHelpMateInterruptResult;

public class FindHelpMateInterrupt extends AbstractFindHelpmate {

  private static final boolean IS_DEBUG = false;

  // Our quick algorithm is extremely light, requiring only a few microseconds on average per
  // position. It is also sound, but not complete. However, as we detail in Section 5, with an
  // (empirically chosen) depth bound of D = 9, all unfairly classified games from the Lichess
  // Database except three were correctly identified by Unwinnablequick.
  private static final int D = 9;

  public static FindHelpmateResult calculateHelpmate(ApiBoard board, Side c) {
    final List<LegalMove> mateList = new ArrayList<>();
    final FindHelpMateInterruptResult result = calculateHelpmate(board, c, 0, mateList);

    switch (result) {
      case TRUE:
        checkHelpmate(board.getFen(), mateList);
        return FindHelpmateResult.YES;
      case FALSE:
        return FindHelpmateResult.NO;
      case INTERRUPTED:
        return FindHelpmateResult.UNKNOWN;
      default:
        throw new IllegalArgumentException();
    }
  }

  private static FindHelpMateInterruptResult calculateHelpmate(ApiBoard board, Side c, int currentDepth,
      List<LegalMove> mateList) {
    final var isIntendedWinnerHavingCheckmate = board.isCheckmate() && board.getHavingMove() == c.getOppositeSide();
    if (isIntendedWinnerHavingCheckmate) {
      return FindHelpMateInterruptResult.TRUE;
    }

    if (currentDepth < D && !board.isInsufficientMaterial(c) && !board.isFivefoldRepetition()
        && !board.isSeventyFiftyMove()) {

      for (final LegalMove legalMove : board.getLegalMoveSet()) {
        board.performMove(legalMove.moveSpecification());
        if (IS_DEBUG) {
          final UciMove uciMove = UciMoveUtility.convertMoveSpecificationToUci(legalMove.moveSpecification());
          System.out.println(uciMove.text() + " - " + (currentDepth + 1));
        }

        mateList.add(legalMove);
        final var hasCheckmate = calculateHelpmate(board, c, currentDepth + 1, mateList);
        board.unperformMove();
        switch (hasCheckmate) {
          case TRUE:
            return FindHelpMateInterruptResult.TRUE;
          case INTERRUPTED:
            mateList.remove(mateList.size() - 1);
            return FindHelpMateInterruptResult.INTERRUPTED;
          case FALSE:
            mateList.remove(mateList.size() - 1);
            // continue
            break;
          default:
            throw new IllegalArgumentException();
        }
      }
    }
    // search could have continued
    if (currentDepth == D) {
      return FindHelpMateInterruptResult.INTERRUPTED;
    }
    return FindHelpMateInterruptResult.FALSE;
  }

}
