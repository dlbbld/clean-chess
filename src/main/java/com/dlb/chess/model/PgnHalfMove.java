package com.dlb.chess.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.enums.MoveSuffixAnnotation;

public record PgnHalfMove(String san, MoveSuffixAnnotation moveSuffixAnnotation, String commentary) {

  public String san() {
    return san;
  }

  public MoveSuffixAnnotation moveSuffixAnnotation() {
    return moveSuffixAnnotation;
  }

  public String commentary() {
    return commentary;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (PgnHalfMove) obj;
    return Objects.equals(commentary, other.commentary) && moveSuffixAnnotation == other.moveSuffixAnnotation
        && Objects.equals(san, other.san);
  }

}
