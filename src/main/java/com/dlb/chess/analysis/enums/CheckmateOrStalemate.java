package com.dlb.chess.analysis.enums;

public enum CheckmateOrStalemate {

  CHECKMATE("checkmate"),
  STALEMATE("stalemate"),
  NA("na");

  private final String description;

  CheckmateOrStalemate(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

}
