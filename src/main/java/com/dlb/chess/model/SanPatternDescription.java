package com.dlb.chess.model;

import java.util.Arrays;
import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.san.SanExample;

public record SanPatternDescription(String pattern, String comment, SanExample... sanExampleList) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (SanPatternDescription) obj;
    return Objects.equals(comment, other.comment) && Objects.equals(pattern, other.pattern)
        && Arrays.equals(sanExampleList, other.sanExampleList);
  }
}
