package com.dlb.chess.pgn.reader.model;

import java.util.Objects;

// we need the comparison for tests, for an use case with imports

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.pgn.reader.enums.StandardTag;

public record Tag(String name, String value) implements Comparable<Tag> {

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
    final var other = (Tag) obj;
    return Objects.equals(name, other.name) && Objects.equals(value, other.value);
  }

  @Override
  public int compareTo(Tag o) {
    if (StandardTag.exists(name)) {
      if (StandardTag.exists(o.name)) {
        final StandardTag thisTag = StandardTag.calculate(name);
        final StandardTag otherTag = StandardTag.calculate(o.name);
        return Integer.compare(thisTag.getSortOrder(), otherTag.getSortOrder());
      }
      return -1;
    }

    if (StandardTag.exists(o.name)) {
      return 1;
    }
    return name.compareTo(o.name);

  }

}
