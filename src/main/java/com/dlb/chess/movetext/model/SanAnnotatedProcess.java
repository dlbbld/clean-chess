package com.dlb.chess.movetext.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

public record SanAnnotatedProcess(String sanAnnotated, boolean isExhausted, String remainingValue) {

  public String sanAnnotated() {
    return sanAnnotated;
  }

  public boolean isExhausted() {
    return isExhausted;
  }

  public String remainingValue() {
    return remainingValue;
  }

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
