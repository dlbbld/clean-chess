package com.dlb.chess.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.enums.MoveSuffixAnnotation;

public record SanAndMoveSuffix(String san, MoveSuffixAnnotation moveSuffixAnnotation) {

  public String san() {
    return san;
  }

  public MoveSuffixAnnotation moveSuffixAnnotation() {
    return moveSuffixAnnotation;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (SanAndMoveSuffix) obj;
    return moveSuffixAnnotation == other.moveSuffixAnnotation && Objects.equals(san, other.san);
  }

}
