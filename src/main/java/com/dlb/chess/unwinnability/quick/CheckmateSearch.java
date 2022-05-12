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

  public static CheckmateSearchResult calculateHasCheckmate(ApiBoard board, Side c, int currentDepth) {
    final var isCheckmate = board.isCheckmate() && board.getHavingMove() == c.getOppositeSide();
    if (isCheckmate) {
      return CheckmateSearchResult.TRUE;
    }

    if (currentDepth < D && !board.isInsufficientMaterial(c)) {
      for (final LegalMove legalMove : board.getLegalMoveSet()) {
        board.performMove(legalMove.moveSpecification());
        final var hasCheckmate = calculateHasCheckmate(board, c, currentDepth + 1);
        board.unperformMove();
        switch (hasCheckmate) {
          case TRUE:
            return CheckmateSearchResult.TRUE;
          case INTERRUPTED:
            return CheckmateSearchResult.INTERRUPTED;
          case FALSE:
            // continue
            break;
          default:
            throw new IllegalArgumentException();
        }
      }
    }
    // search could have continued
    if (currentDepth == D && !board.getLegalMoveSet().isEmpty()) {
      return CheckmateSearchResult.INTERRUPTED;
    }
    return CheckmateSearchResult.FALSE;
  }

}
