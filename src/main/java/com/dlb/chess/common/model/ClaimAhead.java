package com.dlb.chess.common.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.model.LegalMove;

public record ClaimAhead(LegalMove legalMove, int fullMoveNumber, String san) {

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
