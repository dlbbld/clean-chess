package com.dlb.chess.pgn.reader.model;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.pgn.reader.enums.StandardTag;

@SuppressWarnings("null")
public record Tag(@NonNull String name, @NonNull String value) implements Comparable<Tag> {

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
