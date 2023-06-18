package com.dlb.chess.test.winnable.model;

import java.util.Set;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.enums.GameStatusAnalysis;

public record GameMultipleAnalysis(Set<GameStatusAnalysis> gameStatusSet, int numberOfHalfMoves, Side havingMove) {

  public Set<GameStatusAnalysis> gameStatusSet() {
    return gameStatusSet;
  }

  public int numberOfHalfMoves() {
    return numberOfHalfMoves;
  }

  public Side havingMove() {
    return havingMove;
  }
}
