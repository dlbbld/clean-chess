package com.dlb.chess.movetext.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

@SuppressWarnings("null")
public record ReadComment(@NonNull String comment, boolean isExhausted, @NonNull String remainingValue) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (ReadComment) obj;
    return Objects.equals(comment, other.comment) && isExhausted == other.isExhausted
        && Objects.equals(remainingValue, other.remainingValue);
  }

}
