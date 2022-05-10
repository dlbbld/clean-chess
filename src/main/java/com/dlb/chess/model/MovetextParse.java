package com.dlb.chess.model;

import java.util.List;

import org.eclipse.jdt.annotation.Nullable;

public record MovetextParse(boolean hasMovesAndIsLastMoveBlack, List<PgnHalfMove> whiteHalfMoveList,
    List<PgnHalfMove> blackHalfMoveList) {
  public boolean hasMovesAndIsLastMoveBlack() {
    return hasMovesAndIsLastMoveBlack;
  }

  public List<PgnHalfMove> whiteHalfMoveList() {
    return whiteHalfMoveList;
  }

  public List<PgnHalfMove> blackHalfMoveList() {
    return blackHalfMoveList;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (MovetextParse) obj;
    return blackHalfMoveList.equals(other.blackHalfMoveList)
        && hasMovesAndIsLastMoveBlack == other.hasMovesAndIsLastMoveBlack
        && whiteHalfMoveList.equals(other.whiteHalfMoveList);
  }
}
