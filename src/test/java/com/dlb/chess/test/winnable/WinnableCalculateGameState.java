package com.dlb.chess.test.winnable;

import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.enums.GameStatus;
import com.dlb.chess.board.Board;
import com.dlb.chess.common.utility.BasicChessUtility;
import com.dlb.chess.common.utility.SetUtility;
import com.dlb.chess.model.LegalMove;
import com.dlb.chess.test.winnable.model.EvaluatePositions;
import com.dlb.chess.test.winnable.model.GameForced;

public class WinnableCalculateGameState {

  static EvaluatePositions evaluateFirstHalfMoveMadeTheMove(Board board) {

    final Set<GameStatus> gameStatusSet = new TreeSet<>();
    var countEvaluatedPositions = 0;

    for (final LegalMove firstHalfMove : board.getLegalMoveSet()) {
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

    for (final LegalMove firstHalfMove : board.getLegalMoveSet()) {
      countEvaluatedPositions++;
      board.move(firstHalfMove.moveSpecification());
      final GameStatus moveEvaluation = BasicChessUtility.calculateGameStatus(board);
      if (addMoveEvaluationNotMadeTheMove(moveEvaluation, gameStatusSet)) {
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

    for (final LegalMove firstHalfMove : board.getLegalMoveSet()) {
      countEvaluatedPositions++;
      board.move(firstHalfMove.moveSpecification());
      // If the first move creates a FIDE-automatic termination, the strict pipeline rejects
      // any further move from this position. The terminal status is already informative for
      // the analyzer; skip deeper exploration from this branch.
      if (BasicChessUtility.calculateGameStatus(board).isAutomaticTermination()) {
        board.unmove();
        continue;
      }
      for (final LegalMove secondHalfMove : board.getLegalMoveSet()) {
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

    for (final LegalMove firstHalfMove : board.getLegalMoveSet()) {
      countEvaluatedPositions++;
      board.move(firstHalfMove.moveSpecification());
      if (BasicChessUtility.calculateGameStatus(board).isAutomaticTermination()) {
        board.unmove();
        continue;
      }
      for (final LegalMove secondHalfMove : board.getLegalMoveSet()) {

        countEvaluatedPositions++;
        board.move(secondHalfMove.moveSpecification());
        final GameStatus moveEvaluation = BasicChessUtility.calculateGameStatus(board);
        if (addMoveEvaluationNotMadeTheMove(moveEvaluation, gameStatusSet)) {
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

    for (final LegalMove firstHalfMove : board.getLegalMoveSet()) {
      countEvaluatedPositions++;
      board.move(firstHalfMove.moveSpecification());
      if (BasicChessUtility.calculateGameStatus(board).isAutomaticTermination()) {
        board.unmove();
        continue;
      }
      for (final LegalMove secondHalfMove : board.getLegalMoveSet()) {
        countEvaluatedPositions++;
        board.move(secondHalfMove.moveSpecification());
        if (BasicChessUtility.calculateGameStatus(board).isAutomaticTermination()) {
          board.unmove();
          continue;
        }
        for (final LegalMove thirdHalfMove : board.getLegalMoveSet()) {

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

    for (final LegalMove firstHalfMove : board.getLegalMoveSet()) {
      countEvaluatedPositions++;
      board.move(firstHalfMove.moveSpecification());
      if (BasicChessUtility.calculateGameStatus(board).isAutomaticTermination()) {
        board.unmove();
        continue;
      }
      for (final LegalMove secondHalfMove : board.getLegalMoveSet()) {
        countEvaluatedPositions++;
        board.move(secondHalfMove.moveSpecification());
        if (BasicChessUtility.calculateGameStatus(board).isAutomaticTermination()) {
          board.unmove();
          continue;
        }
        for (final LegalMove thirdHalfMove : board.getLegalMoveSet()) {

          countEvaluatedPositions++;
          board.move(thirdHalfMove.moveSpecification());
          final GameStatus moveEvaluation = BasicChessUtility.calculateGameStatus(board);
          if (addMoveEvaluationNotMadeTheMove(moveEvaluation, gameStatusSet)) {
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

  static GameForced evaluateForcedLine(Board board) {
    // we check position after series of forced moves
    // we cannot use early returns for after evaluation we need to undo the moves
    var countForcedHalfMoves = 0;
    while (board.getLegalMoveSet().size() == 1) {
      countForcedHalfMoves++;
      final LegalMove legalMove = SetUtility.getOnly(board.getLegalMoveSet());
      board.move(legalMove.moveSpecification());
      final GameStatus evaluation = BasicChessUtility.calculateGameStatus(board);
      switch (BasicChessUtility.calculateGameStatus(board)) {
        case CHECKMATE:
        case FIVE_FOLD_REPETITION_RULE:
        case INSUFFICIENT_MATERIAL_BOTH:
        case INSUFFICIENT_MATERIAL_MADE_THE_MOVE_ONLY:
        case INSUFFICIENT_MATERIAL_NOT_MADE_THE_MOVE_ONLY:
        case SEVENTY_FIVE_MOVE_RULE:
        case STALEMATE:
          final Side sideMadeLastMove = board.getHavingMove().getOppositeSide();
          for (var i = 1; i <= countForcedHalfMoves; i++) {
            board.unmove();
          }
          return new GameForced(evaluation, countForcedHalfMoves, sideMadeLastMove);
        case ONGOING:
          break;
        default:
          throw new IllegalArgumentException();
      }
    }

    final Side sideMadeLastMove = board.getHavingMove().getOppositeSide();
    for (var i = 1; i <= countForcedHalfMoves; i++) {
      board.unmove();
    }
    return new GameForced(GameStatus.ONGOING, countForcedHalfMoves, sideMadeLastMove);
  }

  private static boolean calculateIsEndMoveEvaluationMadeTheMove(GameStatus moveEvaluation) {
    return switch (moveEvaluation) {
      case CHECKMATE -> true;
      case FIVE_FOLD_REPETITION_RULE, INSUFFICIENT_MATERIAL_BOTH, INSUFFICIENT_MATERIAL_MADE_THE_MOVE_ONLY, INSUFFICIENT_MATERIAL_NOT_MADE_THE_MOVE_ONLY, ONGOING, SEVENTY_FIVE_MOVE_RULE, STALEMATE -> false;
      default -> throw new IllegalArgumentException();
    };
  }

  private static boolean calculateIsEndMoveEvaluationNotMadeTheMove(GameStatus moveEvaluation) {
    return switch (moveEvaluation) {
      case ONGOING, INSUFFICIENT_MATERIAL_MADE_THE_MOVE_ONLY -> true;
      case CHECKMATE, FIVE_FOLD_REPETITION_RULE, INSUFFICIENT_MATERIAL_BOTH, INSUFFICIENT_MATERIAL_NOT_MADE_THE_MOVE_ONLY, SEVENTY_FIVE_MOVE_RULE, STALEMATE -> false;
      default -> throw new IllegalArgumentException();
    };
  }

  private static boolean addMoveEvaluationMadeTheMove(GameStatus moveEvaluation, Set<GameStatus> gameStatusSet) {
    gameStatusSet.add(moveEvaluation);
    return calculateIsEndMoveEvaluationMadeTheMove(moveEvaluation);
  }

  private static boolean addMoveEvaluationNotMadeTheMove(GameStatus moveEvaluation, Set<GameStatus> gameStatusSet) {
    gameStatusSet.add(moveEvaluation);
    return calculateIsEndMoveEvaluationNotMadeTheMove(moveEvaluation);
  }

}
