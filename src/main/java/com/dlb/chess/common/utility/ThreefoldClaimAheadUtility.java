package com.dlb.chess.common.utility;

import java.util.ArrayList;
import java.util.List;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.interfaces.ChessBoard;
import com.dlb.chess.common.model.ClaimAhead;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.model.LegalMove;

public abstract class ThreefoldClaimAheadUtility {
  public static List<List<ClaimAhead>> calculateThreefoldClaimAhead(ChessBoard board) {
    return calculateThreefoldClaimAhead(board.getPerformedLegalMoveList(), board.getInitialFen());
  }

  private static List<List<ClaimAhead>> calculateThreefoldClaimAhead(List<LegalMove> legalMoveList, Fen initialFen) {

    final List<List<ClaimAhead>> resultListList = new ArrayList<>();
    final Board board = new Board(initialFen);

    for (final LegalMove legalMove : legalMoveList) {
      final List<ClaimAhead> resultList = new ArrayList<>();
      board.move(legalMove.moveSpecification());
      for (final LegalMove legalMoveCheckAhead : board.getLegalMoveSet()) {
        board.move(legalMoveCheckAhead.moveSpecification());
        if (board.isThreefoldRepetition()) {
          resultList.add(new ClaimAhead(legalMoveCheckAhead, board.getFullMoveNumber(), board.getSan()));
        }
        board.unmove();
      }
      if (!resultList.isEmpty()) {
        resultListList.add(resultList);
      }
    }

    return resultListList;

  }
}
