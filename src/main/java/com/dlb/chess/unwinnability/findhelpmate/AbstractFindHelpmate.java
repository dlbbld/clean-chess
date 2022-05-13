package com.dlb.chess.unwinnability.findhelpmate;

import java.util.List;

import org.apache.logging.log4j.Logger;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.model.LegalMove;

public abstract class AbstractFindHelpmate {

  private static final Logger logger = NonNullWrapperCommon.getLogger(AbstractFindHelpmate.class);

  protected static ApiBoard checkHelpmate(String fen, List<LegalMove> mateList) {

    final Board boardCheck = new Board(fen);

    for (final LegalMove legalMove : mateList) {
      boardCheck.performMove(legalMove.moveSpecification());
    }
    if (!boardCheck.isCheckmate()) {
      throw new ProgrammingMistakeException("It is not a checkmate");
    }

    final var numberOfMovesForCheckmate = (int) Math.ceil(mateList.size() / 2.0);
    logger.info("Checkmate in " + numberOfMovesForCheckmate + " moves");
    // System.out.println(PgnCreate.createPgnFileString(boardCheck));

    return boardCheck;
  }
}
