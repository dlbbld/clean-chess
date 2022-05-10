package com.dlb.chess.pgn.reader.model;

import java.util.Objects;

// we need the comparison for tests, for an use case with imports

import org.eclipse.jdt.annotation.Nullable;

public record TagCandidate(String name, String value) implements Comparable<TagCandidate> {

  public String name() {
    return name;
  }

  public String value() {
    return value;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (TagCandidate) obj;
    return Objects.equals(name, other.name) && Objects.equals(value, other.value);
  }

  @Override
  public int compareTo(TagCandidate o) {
    return this.name.compareTo(o.name);
  }

}
