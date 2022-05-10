package com.dlb.chess.illegal.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

public record GuessMove(boolean isLegal, String san) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (GuessMove) obj;
    return isLegal == other.isLegal && Objects.equals(san, other.san);
  }
}
