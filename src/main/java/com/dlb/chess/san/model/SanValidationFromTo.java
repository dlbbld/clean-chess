package com.dlb.chess.san.model;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.File;
import com.dlb.chess.board.enums.Rank;
import com.dlb.chess.board.enums.Square;

public record SanValidationFromTo(File fromFile, Rank fromRank, Square toSquare) {

  public File fromFile() {
    return fromFile;
  }

  public Rank fromRank() {
    return fromRank;
  }

  public Square toSquare() {
    return toSquare;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (SanValidationFromTo) obj;
    return fromFile == other.fromFile && fromRank == other.fromRank && toSquare == other.toSquare;
  }
}
