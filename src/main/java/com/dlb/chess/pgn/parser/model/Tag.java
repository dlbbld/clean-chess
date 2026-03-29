package com.dlb.chess.pgn.parser.model;

import com.dlb.chess.pgn.parser.enums.StandardTag;

public record Tag(String name, String value) implements Comparable<Tag> {

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
