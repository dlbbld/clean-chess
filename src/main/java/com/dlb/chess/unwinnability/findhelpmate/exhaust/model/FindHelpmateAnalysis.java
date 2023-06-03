package com.dlb.chess.unwinnability.findhelpmate.exhaust.model;

import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.model.UciMove;
import com.dlb.chess.unwinnability.findhelpmate.enums.FindHelpmateResult;

@SuppressWarnings("null")
public record FindHelpmateAnalysis(FindHelpmateResult findHelpmateResult, int localNodesCount,
    @NonNull List<UciMove> mateLine) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (FindHelpmateAnalysis) obj;
    return findHelpmateResult == other.findHelpmateResult && localNodesCount == other.localNodesCount
        && Objects.equals(mateLine, other.mateLine);
  }

}
