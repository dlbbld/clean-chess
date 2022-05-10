package com.dlb.chess.san;

import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

public record SanExample(String san, String description) {

  public String san() {
    return san;
  }

  public String description() {
    return description;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (SanExample) obj;
    return Objects.equals(description, other.description) && Objects.equals(san, other.san);
  }

}
