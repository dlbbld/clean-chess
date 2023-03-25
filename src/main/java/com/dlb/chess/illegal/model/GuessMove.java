package com.dlb.chess.illegal.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

@SuppressWarnings("null")
public record GuessMove(boolean isLegal, @NonNull String san) {

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
