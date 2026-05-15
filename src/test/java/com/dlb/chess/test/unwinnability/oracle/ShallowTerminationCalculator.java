package com.dlb.chess.test.unwinnability.oracle;

import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.Board;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.enums.GameStatus;
import com.dlb.chess.common.utility.BasicChessUtility;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.test.unwinnability.oracle.model.EvaluatePositions;

public class ShallowTerminationCalculator {

  static EvaluatePositions evaluateFirstHalfMoveMadeTheMove(Board board) {

    final Set<GameStatus> gameStatusSet = new TreeSet<>();
    var countEvaluatedPositions = 0;

    for (final LegalMove firstHalfMove : board.getLegalMoves()) {
      countEvaluatedPositions++;
      board.move(firstHalfMove.moveSpecification());
      final GameStatus moveEvaluation = BasicChessUtility.calculateGameStatus(board);
      if (addMoveEvaluationMadeTheMove(moveEvaluation, gameStatusSet)) {
        // early return for found other, only interested in game ended or insufficient material oppponent
        board.unmove();
        return new EvaluatePositions(gameStatusSet, countEvaluatedPositions);
      }
      board.unmove();
    }
    return new EvaluatePositions(gameStatusSet, countEvaluatedPositions);
  }

  static EvaluatePositions evaluateFirstHalfMoveNotMadeTheMove(Board board) {

    final Set<GameStatus> gameStatusSet = new TreeSet<>();
    var countEvaluatedPositions = 0;

    for (final LegalMove firstHalfMove : board.getLegalMoves()) {
      countEvaluatedPositions++;
      board.move(firstHalfMove.moveSpecification());
      final GameStatus moveEvaluation = BasicChessUtility.calculateGameStatus(board);
      if (addMoveEvaluationNotMadeTheMove(board, moveEvaluation, gameStatusSet)) {
        // early return for found other, only interested in game ended or insufficient material oppponent
        board.unmove();
        return new EvaluatePositions(gameStatusSet, countEvaluatedPositions);
      }
      board.unmove();
    }
    return new EvaluatePositions(gameStatusSet, countEvaluatedPositions);
  }

  static EvaluatePositions evaluateSecondHalfMoveMadeTheMove(Board board) {

    final Set<GameStatus> gameStatusSet = new TreeSet<>();
    var countEvaluatedPositions = 0;

    for (final LegalMove firstHalfMove : board.getLegalMoves()) {
      countEvaluatedPositions++;
      board.move(firstHalfMove.moveSpecification());
      // If the first move creates a FIDE-automatic termination, the strict pipeline rejects
      // any further move from this position. The terminal status is already informative for
      // the analyzer; skip deeper exploration from this branch.
      if (BasicChessUtility.calculateGameStatus(board).isAutomaticTermination()) {
        board.unmove();
        continue;
      }
      for (final LegalMove secondHalfMove : board.getLegalMoves()) {
        countEvaluatedPositions++;
        board.move(secondHalfMove.moveSpecification());
        final GameStatus moveEvaluation = BasicChessUtility.calculateGameStatus(board);
        if (addMoveEvaluationMadeTheMove(moveEvaluation, gameStatusSet)) {
          // early return for found other, only interested in game ended or insufficient material oppponent
          board.unmove();
          board.unmove();
          return new EvaluatePositions(gameStatusSet, countEvaluatedPositions);
        }
        board.unmove();
      }
      board.unmove();
    }

    return new EvaluatePositions(gameStatusSet, countEvaluatedPositions);
  }

  static EvaluatePositions evaluateSecondHalfMoveNotMadeTheMove(Board board) {

    final Set<GameStatus> gameStatusSet = new TreeSet<>();
    var countEvaluatedPositions = 0;

    for (final LegalMove firstHalfMove : board.getLegalMoves()) {
      countEvaluatedPositions++;
      board.move(firstHalfMove.moveSpecification());
      if (BasicChessUtility.calculateGameStatus(board).isAutomaticTermination()) {
        board.unmove();
        continue;
      }
      for (final LegalMove secondHalfMove : board.getLegalMoves()) {

        countEvaluatedPositions++;
        board.move(secondHalfMove.moveSpecification());
        final GameStatus moveEvaluation = BasicChessUtility.calculateGameStatus(board);
        if (addMoveEvaluationNotMadeTheMove(board, moveEvaluation, gameStatusSet)) {
          // early return for found other, only interested in game ended or insufficient material oppponent
          board.unmove();
          board.unmove();
          return new EvaluatePositions(gameStatusSet, countEvaluatedPositions);
        }
        board.unmove();
      }
      board.unmove();
    }

    return new EvaluatePositions(gameStatusSet, countEvaluatedPositions);
  }

  static EvaluatePositions evaluateThirdHalfMoveMadeTheMove(Board board) {

    final Set<GameStatus> gameStatusSet = new TreeSet<>();
    var countEvaluatedPositions = 0;

    for (final LegalMove firstHalfMove : board.getLegalMoves()) {
      countEvaluatedPositions++;
      board.move(firstHalfMove.moveSpecification());
      if (BasicChessUtility.calculateGameStatus(board).isAutomaticTermination()) {
        board.unmove();
        continue;
      }
      for (final LegalMove secondHalfMove : board.getLegalMoves()) {
        countEvaluatedPositions++;
        board.move(secondHalfMove.moveSpecification());
        if (BasicChessUtility.calculateGameStatus(board).isAutomaticTermination()) {
          board.unmove();
          continue;
        }
        for (final LegalMove thirdHalfMove : board.getLegalMoves()) {

          countEvaluatedPositions++;
          board.move(thirdHalfMove.moveSpecification());
          final GameStatus moveEvaluation = BasicChessUtility.calculateGameStatus(board);
          if (addMoveEvaluationMadeTheMove(moveEvaluation, gameStatusSet)) {
            // early return for found other, only interested in game ended or insufficient material oppponent
            board.unmove();
            board.unmove();
            board.unmove();
            return new EvaluatePositions(gameStatusSet, countEvaluatedPositions);
          }
          board.unmove();
        }
        board.unmove();
      }
      board.unmove();
    }

    return new EvaluatePositions(gameStatusSet, countEvaluatedPositions);
  }

  static EvaluatePositions evaluateThirdHalfMoveNotMadeTheMove(Board board) {

    final Set<GameStatus> gameStatusSet = new TreeSet<>();
    var countEvaluatedPositions = 0;

    for (final LegalMove firstHalfMove : board.getLegalMoves()) {
      countEvaluatedPositions++;
      board.move(firstHalfMove.moveSpecification());
      if (BasicChessUtility.calculateGameStatus(board).isAutomaticTermination()) {
        board.unmove();
        continue;
      }
      for (final LegalMove secondHalfMove : board.getLegalMoves()) {
        countEvaluatedPositions++;
        board.move(secondHalfMove.moveSpecification());
        if (BasicChessUtility.calculateGameStatus(board).isAutomaticTermination()) {
          board.unmove();
          continue;
        }
        for (final LegalMove thirdHalfMove : board.getLegalMoves()) {

          countEvaluatedPositions++;
          board.move(thirdHalfMove.moveSpecification());
          final GameStatus moveEvaluation = BasicChessUtility.calculateGameStatus(board);
          if (addMoveEvaluationNotMadeTheMove(board, moveEvaluation, gameStatusSet)) {
            // early return for found other, only interested in game ended or insufficient material oppponent
            board.unmove();
            board.unmove();
            board.unmove();
            return new EvaluatePositions(gameStatusSet, countEvaluatedPositions);
          }
          board.unmove();
        }
        board.unmove();
      }
      board.unmove();
    }

    return new EvaluatePositions(gameStatusSet, countEvaluatedPositions);
  }

  private static boolean calculateIsEndMoveEvaluationMadeTheMove(GameStatus moveEvaluation) {
    return switch (moveEvaluation) {
      case CHECKMATE -> true;
      case FIVE_FOLD_REPETITION_RULE, INSUFFICIENT_MATERIAL_BOTH, INSUFFICIENT_MATERIAL_WHITE_ONLY, INSUFFICIENT_MATERIAL_BLACK_ONLY, ONGOING, SEVENTY_FIVE_MOVE_RULE, STALEMATE -> false;
    };
  }

  /**
   * Early-exit predicate for the "not made the move" iteration: the iteration can stop once it finds a position
   * that isn't a guaranteed loss for the not-made-the-move side. The two such cases are ONGOING (game continues,
   * nothing forced) and the insufficient-material flavour where the side that <em>just moved</em> has insufficient
   * material — that's good for the not-made-the-move side, not bad, so it doesn't contribute to an UNWINNABLE
   * conclusion and lets the iteration return early.
   */
  private static boolean calculateIsEndMoveEvaluationNotMadeTheMove(GameStatus moveEvaluation, Side sideThatJustMoved) {
    return switch (moveEvaluation) {
      case ONGOING -> true;
      case INSUFFICIENT_MATERIAL_WHITE_ONLY -> sideThatJustMoved == Side.WHITE;
      case INSUFFICIENT_MATERIAL_BLACK_ONLY -> sideThatJustMoved == Side.BLACK;
      case CHECKMATE, FIVE_FOLD_REPETITION_RULE, INSUFFICIENT_MATERIAL_BOTH, SEVENTY_FIVE_MOVE_RULE, STALEMATE -> false;
    };
  }

  private static boolean addMoveEvaluationMadeTheMove(GameStatus moveEvaluation, Set<GameStatus> gameStatusSet) {
    gameStatusSet.add(moveEvaluation);
    return calculateIsEndMoveEvaluationMadeTheMove(moveEvaluation);
  }

  private static boolean addMoveEvaluationNotMadeTheMove(Board board, GameStatus moveEvaluation,
      Set<GameStatus> gameStatusSet) {
    gameStatusSet.add(moveEvaluation);
    final Side sideThatJustMoved = board.getHavingMove().getOppositeSide();
    return calculateIsEndMoveEvaluationNotMadeTheMove(moveEvaluation, sideThatJustMoved);
  }

}
