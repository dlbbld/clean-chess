package com.dlb.chess.test.custom;

import java.util.ArrayList;
import java.util.List;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.constants.EnumConstants;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.MoveSpecification;
import com.dlb.chess.test.apicomparison.utility.CommonTestUtility;

public abstract class AbstractTestFenParser implements EnumConstants {
  // Using true takes much longer. With above second game about one minute to run through.
  public static void checkGames(String initialFen, List<MoveSpecification> moveList, boolean isIncludeCreatedBoards) {

    final List<ApiBoard> boardCreatedFromFenList = new ArrayList<>();
    final ApiBoard boardPlayMoves = new Board(initialFen);

    for (var i = 0; i < moveList.size(); i++) {
      final MoveSpecification move = NonNullWrapperCommon.get(moveList, i);
      boardPlayMoves.performMove(move);
      final String boardFen = boardPlayMoves.getFen();
      final ApiBoard boardFromFen = new Board(boardFen);

      // check newest board
      CommonTestUtility.checkChessBoardsAgainstEachOtherExcludeHistory(boardPlayMoves, boardFromFen);
      if (isIncludeCreatedBoards) {
        boardCreatedFromFenList.add(boardFromFen);

        // check older boards
        for (var j = 0; j < boardCreatedFromFenList.size() - 1; j++) {
          @SuppressWarnings("null") final ApiBoard boardFromFenCurrent = boardCreatedFromFenList.get(j);
          final MoveSpecification moveReplay = NonNullWrapperCommon.get(moveList, i);
          boardFromFenCurrent.performMove(moveReplay);
          CommonTestUtility.checkChessBoardsAgainstEachOtherExcludeHistory(boardPlayMoves, boardFromFenCurrent);
        }
      }
    }
  }
}
