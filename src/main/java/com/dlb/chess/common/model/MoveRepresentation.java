package com.dlb.chess.common.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.model.LegalMove;

@SuppressWarnings("null")
public record MoveRepresentation(@NonNull MoveSpecification moveSpecification, @NonNull LegalMove legalMove,
    @NonNull String san, @NonNull String lan, @NonNull String uci) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (MoveRepresentation) obj;
    return Objects.equals(lan, other.lan) && Objects.equals(legalMove, other.legalMove)
        && Objects.equals(moveSpecification, other.moveSpecification) && Objects.equals(san, other.san)
        && Objects.equals(uci, other.uci);
  }

}
