package com.dlb.chess.common.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

// own class to avoid mixing up the movetext and the move list
// per the PGN specification the movetext contains the move and the game termination marker
@SuppressWarnings("null")
public record Movetext(@NonNull String movetext) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (Movetext) obj;
    return Objects.equals(movetext, other.movetext);
  }

}
