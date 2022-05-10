package com.dlb.chess.winnable.model;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.common.enums.GameStatus;

public record GameForced(GameStatus gameStatus, int evaluatedPositions, Side sideMadeLastMove) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (GameForced) obj;
    return evaluatedPositions == other.evaluatedPositions && gameStatus == other.gameStatus
        && sideMadeLastMove == other.sideMadeLastMove;
  }
}
