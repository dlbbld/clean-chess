package com.dlb.chess.san.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

@SuppressWarnings("null")
public record SanExample(@NonNull String san, @NonNull String description) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (SanExample) obj;
    return Objects.equals(description, other.description) && Objects.equals(san, other.san);
  }

}
