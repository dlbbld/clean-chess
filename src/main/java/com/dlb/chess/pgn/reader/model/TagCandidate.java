package com.dlb.chess.pgn.reader.model;

public record TagCandidate(String name, String value) implements Comparable<TagCandidate> {

  @Override
  public int compareTo(TagCandidate o) {
    return this.name.compareTo(o.name);
  }

}
