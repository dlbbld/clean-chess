package com.dlb.chess.pgn.reader.model;

import org.eclipse.jdt.annotation.NonNull;

public record TagCandidate(@NonNull String name, @NonNull String value) implements Comparable<TagCandidate> {

  @Override
  public int compareTo(TagCandidate o) {
    return this.name.compareTo(o.name);
  }

}
