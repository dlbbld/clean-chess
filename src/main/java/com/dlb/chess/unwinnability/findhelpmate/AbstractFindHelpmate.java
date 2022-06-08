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

  private static final boolean IS_DEBUG = false;

  protected static ApiBoard checkHelpmate(String fen, List<LegalMove> moveProgressList) {

    final var boardCheck = new Board(fen);

    var isFoundFivefold = false;
    var isFoundSeventyFiveMoves = false;
    for (final LegalMove legalMove : moveProgressList) {
      boardCheck.performMove(legalMove.moveSpecification());
      if (!isFoundFivefold && boardCheck.isFivefoldRepetition()) {
        isFoundFivefold = true;
      }

      if (!isFoundSeventyFiveMoves && boardCheck.isSeventyFiftyMove()) {
        isFoundSeventyFiveMoves = true;
      }
    }
    if (!boardCheck.isCheckmate()) {
      throw new ProgrammingMistakeException("It is not a checkmate line because it does not end in checkmate");
    }

    if (IS_DEBUG) {
      if (isFoundFivefold) {
        logger.warn("Not a checkmate for fivefold: " + fen);
      }

      if (isFoundSeventyFiveMoves) {
        logger.warn("Not a checkmate for seventy-five moves: " + fen);
      }

      final var numberOfMovesForCheckmate = (int) Math.ceil(moveProgressList.size() / 2.0);
      logger.info("Checkmate in " + numberOfMovesForCheckmate + " moves");
      System.out.println(PgnCreate.createPgnFileString(boardCheck));
    }

    return boardCheck;
  }

  protected static ApiBoard checkClassicalCheckmate(Side color, String fen, List<LegalMove> moveProgressList) {

    final var boardCheck = new Board(fen);

    for (final LegalMove legalMove : moveProgressList) {
      boardCheck.performMove(legalMove.moveSpecification());
    }
    if (!ClassicalCheckmate.isClassicalCheckmatePosition(color, boardCheck.getStaticPosition())) {
      throw new ProgrammingMistakeException("It is not a classical checkmate position");
    }

    if (IS_DEBUG) {
      final var numberOfMovesForClassicalCheckmatePosition = calculateNumberOfMoves(moveProgressList);
      logger.info("Classical checkmate position found in " + numberOfMovesForClassicalCheckmatePosition + " moves");
      System.out.println(PgnCreate.createPgnFileString(boardCheck));
    }

    return boardCheck;
  }

  private static int calculateNumberOfMoves(List<LegalMove> moveProgressList) {
    return (int) Math.ceil(moveProgressList.size() / 2.0);
  }
}
