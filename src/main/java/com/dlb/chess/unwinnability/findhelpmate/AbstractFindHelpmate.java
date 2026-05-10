package com.dlb.chess.unwinnability.findhelpmate;

import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.dlb.chess.board.Board;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;
import com.dlb.chess.common.interfaces.ChessBoard;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.pgn.create.PgnCreate;

public abstract class AbstractFindHelpmate {

  private static final Logger logger = NonNullWrapperCommon.getLogger(AbstractFindHelpmate.class);

  private static final boolean IS_DEBUG = false;

  protected static ChessBoard checkHelpmate(String fen, List<LegalMove> moveProgressList) {

    final var boardCheck = new Board(fen);

    var isFoundFivefold = false;
    var isFoundSeventyFiveMoves = false;
    for (final LegalMove legalMove : moveProgressList) {
      boardCheck.move(legalMove.moveSpecification());
      if (!isFoundFivefold && boardCheck.isFivefoldRepetition()) {
        isFoundFivefold = true;
      }

      if (!isFoundSeventyFiveMoves && boardCheck.isSeventyFiveMove()) {
        isFoundSeventyFiveMoves = true;
      }
    }
    if (!boardCheck.isCheckmate()) {
      throw new ProgrammingMistakeException("It is not a checkmate line because it does not end in checkmate");
    }

    if (IS_DEBUG) {
      if (isFoundFivefold) {
        logger.printf(Level.WARN, "Not a checkmate for fivefold: %s", fen);
      }

      if (isFoundSeventyFiveMoves) {
        logger.printf(Level.WARN, "Not a checkmate for seventy-five moves: %s", fen);
      }

      final var numberOfMovesForCheckmate = (int) Math.ceil(moveProgressList.size() / 2.0);
      logger.printf(Level.INFO, "Checkmate in %d moves", numberOfMovesForCheckmate);
      logger.info(PgnCreate.createPgnFileString(boardCheck));
    }

    return boardCheck;
  }

}
