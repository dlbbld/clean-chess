package com.dlb.chess.unwinnability.quick;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.unwinnability.quick.enums.CheckmateSearchResult;

public class CheckmateSearch {
  // Our quick algorithm is extremely light, requiring only a few microseconds on average per
  // position. It is also sound, but not complete. However, as we detail in Section 5, with an
  // (empirically chosen) depth bound of D = 9, all unfairly classified games from the Lichess
  // Database except three were correctly identified by Unwinnablequick.
  private static final int D = 9;

  private boolean isInterrupted = false;

  public CheckmateSearchResult checkmateSearch(ApiBoard board, Side c) {
    final var hasCheckmate = calculateHasCheckmate(board, c, 0);

    if (hasCheckmate) {
      return CheckmateSearchResult.TRUE;
    }

    if (isInterrupted) {
      return CheckmateSearchResult.INTERRUPTED;
    }

    return CheckmateSearchResult.FALSE;
  }

  private boolean calculateHasCheckmate(ApiBoard board, Side c, int currentDepth) {
    final var isCheckmate = board.isCheckmate() && board.getHavingMove() == c.getOppositeSide();
    if (isCheckmate) {
      return true;
    }

    if (currentDepth < D && !board.isInsufficientMaterial(c)) {
      for (final LegalMove legalMove : board.getLegalMoveSet()) {
        board.performMove(legalMove.moveSpecification());
        final var hasCheckmate = calculateHasCheckmate(board, c, currentDepth + 1);
        board.unperformMove();
        if (hasCheckmate) {
          return true;
        }
      }
    }
    if (currentDepth == D && !isInterrupted) {
      // search could have continued
      isInterrupted = !board.getLegalMoveSet().isEmpty();
      if (isInterrupted) {
        return true;
      }
    }
    return false;
  }

}
