package com.dlb.chess.winnable;

import java.util.Set;
import java.util.TreeSet;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.NonNullWrapperCommon;
import com.dlb.chess.common.enums.GameStatus;
import com.dlb.chess.common.interfaces.ApiBoard;
import com.dlb.chess.common.model.MoveRepresentation;
import com.dlb.chess.common.utility.BasicChessUtility;
import com.dlb.chess.winnable.model.EvaluatePositions;
import com.dlb.chess.winnable.model.GameForced;

public class WinnableCalculateGameState {

  static EvaluatePositions evaluateFirstHalfMoveMadeTheMove(ApiBoard board) {

    final Set<GameStatus> gameStatusSet = new TreeSet<>();
    var countEvaluatedPositions = 0;

    for (final MoveRepresentation firstHalfMove : board.getLegalMovesRepresentation()) {
      countEvaluatedPositions++;
      board.performMove(firstHalfMove.moveSpecification());
      final GameStatus moveEvaluation = BasicChessUtility.calculateGameStatus(board);
      if (addMoveEvaluationMadeTheMove(moveEvaluation, gameStatusSet)) {
        // early return for found other, only interested in game ended or insufficient material oppponent
        board.unperformMove();
        return new EvaluatePositions(gameStatusSet, countEvaluatedPositions);
      }
      board.unperformMove();
    }
    return new EvaluatePositions(gameStatusSet, countEvaluatedPositions);
  }

  static EvaluatePositions evaluateFirstHalfMoveNotMadeTheMove(ApiBoard board) {

    final Set<GameStatus> gameStatusSet = new TreeSet<>();
    var countEvaluatedPositions = 0;

    for (final MoveRepresentation firstHalfMove : board.getLegalMovesRepresentation()) {
      countEvaluatedPositions++;
      board.performMove(firstHalfMove.moveSpecification());
      final GameStatus moveEvaluation = BasicChessUtility.calculateGameStatus(board);
      if (addMoveEvaluationNotMadeTheMove(moveEvaluation, gameStatusSet)) {
        // early return for found other, only interested in game ended or insufficient material oppponent
        board.unperformMove();
        return new EvaluatePositions(gameStatusSet, countEvaluatedPositions);
      }
      board.unperformMove();
    }
    return new EvaluatePositions(gameStatusSet, countEvaluatedPositions);
  }

  static EvaluatePositions evaluateSecondHalfMoveMadeTheMove(ApiBoard board) {

    final Set<GameStatus> gameStatusSet = new TreeSet<>();
    var countEvaluatedPositions = 0;

    for (final MoveRepresentation firstHalfMove : board.getLegalMovesRepresentation()) {
      countEvaluatedPositions++;
      board.performMove(firstHalfMove.moveSpecification());
      for (final MoveRepresentation secondHalfMove : board.getLegalMovesRepresentation()) {
        countEvaluatedPositions++;
        board.performMove(secondHalfMove.moveSpecification());
        final GameStatus moveEvaluation = BasicChessUtility.calculateGameStatus(board);
        if (addMoveEvaluationMadeTheMove(moveEvaluation, gameStatusSet)) {
          // early return for found other, only interested in game ended or insufficient material oppponent
          board.unperformMove();
          board.unperformMove();
          return new EvaluatePositions(gameStatusSet, countEvaluatedPositions);
        }
        board.unperformMove();
      }
      board.unperformMove();
    }

    return new EvaluatePositions(gameStatusSet, countEvaluatedPositions);
  }

  static EvaluatePositions evaluateSecondHalfMoveNotMadeTheMove(ApiBoard board) {

    final Set<GameStatus> gameStatusSet = new TreeSet<>();
    var countEvaluatedPositions = 0;

    for (final MoveRepresentation firstHalfMove : board.getLegalMovesRepresentation()) {
      countEvaluatedPositions++;
      board.performMove(firstHalfMove.moveSpecification());
      for (final MoveRepresentation secondHalfMove : board.getLegalMovesRepresentation()) {
        countEvaluatedPositions++;
        board.performMove(secondHalfMove.moveSpecification());
        final GameStatus moveEvaluation = BasicChessUtility.calculateGameStatus(board);
        if (addMoveEvaluationNotMadeTheMove(moveEvaluation, gameStatusSet)) {
          // early return for found other, only interested in game ended or insufficient material oppponent
          board.unperformMove();
          board.unperformMove();
          return new EvaluatePositions(gameStatusSet, countEvaluatedPositions);
        }
        board.unperformMove();
      }
      board.unperformMove();
    }

    return new EvaluatePositions(gameStatusSet, countEvaluatedPositions);
  }

  static EvaluatePositions evaluateThirdHalfMoveMadeTheMove(ApiBoard board) {

    final Set<GameStatus> gameStatusSet = new TreeSet<>();
    var countEvaluatedPositions = 0;

    for (final MoveRepresentation firstHalfMove : board.getLegalMovesRepresentation()) {
      countEvaluatedPositions++;
      board.performMove(firstHalfMove.moveSpecification());
      for (final MoveRepresentation secondHalfMove : board.getLegalMovesRepresentation()) {
        countEvaluatedPositions++;
        board.performMove(secondHalfMove.moveSpecification());
        for (final MoveRepresentation thirdHalfMove : board.getLegalMovesRepresentation()) {
          countEvaluatedPositions++;
          board.performMove(thirdHalfMove.moveSpecification());
          final GameStatus moveEvaluation = BasicChessUtility.calculateGameStatus(board);
          if (addMoveEvaluationMadeTheMove(moveEvaluation, gameStatusSet)) {
            // early return for found other, only interested in game ended or insufficient material oppponent
            board.unperformMove();
            board.unperformMove();
            board.unperformMove();
            return new EvaluatePositions(gameStatusSet, countEvaluatedPositions);
          }
          board.unperformMove();
        }
        board.unperformMove();
      }
      board.unperformMove();
    }

    return new EvaluatePositions(gameStatusSet, countEvaluatedPositions);
  }

  static EvaluatePositions evaluateThirdHalfMoveNotMadeTheMove(ApiBoard board) {

    final Set<GameStatus> gameStatusSet = new TreeSet<>();
    var countEvaluatedPositions = 0;

    for (final MoveRepresentation firstHalfMove : board.getLegalMovesRepresentation()) {
      countEvaluatedPositions++;
      board.performMove(firstHalfMove.moveSpecification());
      for (final MoveRepresentation secondHalfMove : board.getLegalMovesRepresentation()) {
        countEvaluatedPositions++;
        board.performMove(secondHalfMove.moveSpecification());
        for (final MoveRepresentation thirdHalfMove : board.getLegalMovesRepresentation()) {
          countEvaluatedPositions++;
          board.performMove(thirdHalfMove.moveSpecification());
          final GameStatus moveEvaluation = BasicChessUtility.calculateGameStatus(board);
          if (addMoveEvaluationNotMadeTheMove(moveEvaluation, gameStatusSet)) {
            // early return for found other, only interested in game ended or insufficient material oppponent
            board.unperformMove();
            board.unperformMove();
            board.unperformMove();
            return new EvaluatePositions(gameStatusSet, countEvaluatedPositions);
          }
          board.unperformMove();
        }
        board.unperformMove();
      }
      board.unperformMove();
    }

    return new EvaluatePositions(gameStatusSet, countEvaluatedPositions);
  }

  static GameForced evaluateForcedLine(ApiBoard board) {
    // we check position after series of forced moves
    // we cannot use early returns for after evaluation we need to undo the moves
    var countForcedHalfMoves = 0;
    while (board.getLegalMovesRepresentation().size() == 1) {
      countForcedHalfMoves++;
      final MoveRepresentation moveRepresentation = NonNullWrapperCommon.get(board.getLegalMovesRepresentation(), 0);
      board.performMove(moveRepresentation.moveSpecification());
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
            board.unperformMove();
          }
          return new GameForced(evaluation, countForcedHalfMoves, sideMadeLastMove);
        case OTHER:
          break;
        default:
          throw new IllegalArgumentException();
      }
    }

    final Side sideMadeLastMove = board.getHavingMove().getOppositeSide();
    for (var i = 1; i <= countForcedHalfMoves; i++) {
      board.unperformMove();
    }
    return new GameForced(GameStatus.OTHER, countForcedHalfMoves, sideMadeLastMove);
  }

  private static boolean calculateIsEndMoveEvaluationMadeTheMove(GameStatus moveEvaluation) {
    switch (moveEvaluation) {
      case CHECKMATE:
        return true;
      case FIVE_FOLD_REPETITION_RULE:
      case INSUFFICIENT_MATERIAL_BOTH:
      case INSUFFICIENT_MATERIAL_MADE_THE_MOVE_ONLY:
      case INSUFFICIENT_MATERIAL_NOT_MADE_THE_MOVE_ONLY:
      case OTHER:
      case SEVENTY_FIVE_MOVE_RULE:
      case STALEMATE:
        return false;
      default:
        throw new IllegalArgumentException();
    }
  }

  private static boolean calculateIsEndMoveEvaluationNotMadeTheMove(GameStatus moveEvaluation) {
    switch (moveEvaluation) {
      case OTHER:
      case INSUFFICIENT_MATERIAL_MADE_THE_MOVE_ONLY:
        return true;
      case CHECKMATE:
      case FIVE_FOLD_REPETITION_RULE:
      case INSUFFICIENT_MATERIAL_BOTH:
      case INSUFFICIENT_MATERIAL_NOT_MADE_THE_MOVE_ONLY:
      case SEVENTY_FIVE_MOVE_RULE:
      case STALEMATE:
        return false;
      default:
        throw new IllegalArgumentException();
    }
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
