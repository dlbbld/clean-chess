package com.dlb.chess.winnable.model;

import java.util.Objects;
import java.util.Set;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.enums.GameStatusAnalysis;

public record GameMultipleAnalysis(Set<GameStatusAnalysis> gameStatusSet, int numberOfHalfMoves, Side havingMove) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (GameMultipleAnalysis) obj;
    return Objects.equals(gameStatusSet, other.gameStatusSet) && havingMove == other.havingMove
        && numberOfHalfMoves == other.numberOfHalfMoves;
  }

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
