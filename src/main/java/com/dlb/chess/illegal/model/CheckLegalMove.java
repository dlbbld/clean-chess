package com.dlb.chess.illegal.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.common.model.MoveRepresentation;

@SuppressWarnings("null")
public record CheckLegalMove(boolean isLegal, @NonNull MoveRepresentation moveRepresentation) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (CheckLegalMove) obj;
    return isLegal == other.isLegal && Objects.equals(moveRepresentation, other.moveRepresentation);
  }

}
