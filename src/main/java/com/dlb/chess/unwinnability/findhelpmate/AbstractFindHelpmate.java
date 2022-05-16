package com.dlb.chess.unwinnability.findhelpmate;

import java.util.List;

import org.apache.logging.log4j.Logger;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.pgn.create.PgnCreate;
import com.dlb.chess.unwinnability.findhelpmate.exhaust.classicalcheckmate.ClassicalCheckmate;

public abstract class AbstractFindHelpmate {

  private static final Logger logger = NonNullWrapperCommon.getLogger(AbstractFindHelpmate.class);

  protected static ApiBoard checkHelpmate(String fen, List<LegalMove> moveProgressList) {

    final Board boardCheck = new Board(fen);

    for (final LegalMove legalMove : moveProgressList) {
      boardCheck.performMove(legalMove.moveSpecification());
    }
    if (!boardCheck.isCheckmate()) {
      throw new ProgrammingMistakeException("It is not a checkmate");
    }

    final var numberOfMovesForCheckmate = (int) Math.ceil(moveProgressList.size() / 2.0);
    logger.info("Checkmate in " + numberOfMovesForCheckmate + " moves");
    System.out.println(PgnCreate.createPgnFileString(boardCheck));

    return boardCheck;
  }

  protected static ApiBoard checkClassicalCheckmate(Side color, String fen, List<LegalMove> moveProgressList) {

    final Board boardCheck = new Board(fen);

    for (final LegalMove legalMove : moveProgressList) {
      boardCheck.performMove(legalMove.moveSpecification());
    }
    if (!ClassicalCheckmate.isClassicalCheckmatePosition(color, boardCheck.getStaticPosition())) {
      throw new ProgrammingMistakeException("It is not a classical checkmate position");
    }

    final var numberOfMovesForClassicalCheckmatePosition = calculateNumberOfMoves(moveProgressList);
    logger.info("Classical checkmate position found in " + numberOfMovesForClassicalCheckmatePosition + " moves");
    System.out.println(PgnCreate.createPgnFileString(boardCheck));

    return boardCheck;
  }

  private static int calculateNumberOfMoves(List<LegalMove> moveProgressList) {
    return (int) Math.ceil(moveProgressList.size() / 2.0);
  }
}
