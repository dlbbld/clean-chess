package com.dlb.chess.unwinnability.full.model;

import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.model.UciMove;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;

public record UnwinnableFullAnalysis(UnwinnableFull unwinnableFull, List<UciMove> mateLine) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (UnwinnableFullAnalysis) obj;
    return Objects.equals(mateLine, other.mateLine) && unwinnableFull == other.unwinnableFull;
  }

  public UnwinnableFull unwinnableFull() {
    return unwinnableFull;
  }

  public List<UciMove> mateLine() {
    return mateLine;
  }

}
