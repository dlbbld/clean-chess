package com.dlb.chess.report;

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
