package com.dlb.chess.movetext.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

@SuppressWarnings("null")
public record SanAnnotatedProcess(@NonNull String sanAnnotated, boolean isExhausted, @NonNull String remainingValue) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (SanAnnotatedProcess) obj;
    return isExhausted == other.isExhausted && Objects.equals(remainingValue, other.remainingValue)
        && Objects.equals(sanAnnotated, other.sanAnnotated);
  }

}
