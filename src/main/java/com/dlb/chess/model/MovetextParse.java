package com.dlb.chess.model;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

@SuppressWarnings("null")
public record MovetextParse(boolean hasMovesAndIsLastMoveBlack, @NonNull List<PgnHalfMove> whiteHalfMoveList,
    @NonNull List<PgnHalfMove> blackHalfMoveList) {

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
