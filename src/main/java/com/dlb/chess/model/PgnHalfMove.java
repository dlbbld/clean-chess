package com.dlb.chess.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.enums.MoveSuffixAnnotation;

@SuppressWarnings("null")
public record PgnHalfMove(@NonNull String san, @NonNull MoveSuffixAnnotation moveSuffixAnnotation,
    @NonNull String commentary) {

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
