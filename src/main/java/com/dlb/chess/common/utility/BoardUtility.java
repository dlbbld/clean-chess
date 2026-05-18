package com.dlb.chess.common.utility;

import com.dlb.chess.board.Board;
import com.dlb.chess.report.CheckmateOrStalemate;

public abstract class BoardUtility {

  public static CheckmateOrStalemate calculateEvaluation(Board board) {
    // order is crucial
    if (board.isCheckmate()) {
      return CheckmateOrStalemate.CHECKMATE;
    }
    if (board.isStalemate()) {
      return CheckmateOrStalemate.STALEMATE;
    }
    return CheckmateOrStalemate.NA;
  }

}
