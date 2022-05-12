package com.dlb.chess.test.winnable.model;

import java.util.Objects;
import java.util.Set;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.common.enums.GameStatusAnalysis;
import com.dlb.chess.test.winnable.enums.Winnable;

public record WinnableAnalysis(Winnable winnable, Set<GameStatusAnalysis> gameStatusSet) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (WinnableAnalysis) obj;
    return Objects.equals(gameStatusSet, other.gameStatusSet) && winnable == other.winnable;
  }

}
