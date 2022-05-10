package com.dlb.chess.pgn.reader.model;

import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

public record PgnSan(String startingFen, List<String> sanList) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (PgnSan) obj;
    return Objects.equals(sanList, other.sanList) && Objects.equals(startingFen, other.startingFen);
  }
}
