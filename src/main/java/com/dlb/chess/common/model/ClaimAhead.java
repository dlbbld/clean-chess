package com.dlb.chess.common.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.model.LegalMove;

@SuppressWarnings("null")
public record ClaimAhead(@NonNull LegalMove legalMove, int fullMoveNumber, @NonNull String san) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (ClaimAhead) obj;
    return fullMoveNumber == other.fullMoveNumber && Objects.equals(legalMove, other.legalMove)
        && Objects.equals(san, other.san);
  }

}
