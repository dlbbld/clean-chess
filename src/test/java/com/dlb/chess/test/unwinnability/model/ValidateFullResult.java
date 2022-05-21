package com.dlb.chess.test.unwinnability.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;

public record ValidateFullResult(Fen fen, Side sideCheckingForWin, UnwinnableFull unwinnableFull) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (ValidateFullResult) obj;
    return Objects.equals(fen, other.fen) && sideCheckingForWin == other.sideCheckingForWin
        && unwinnableFull == other.unwinnableFull;
  }
}
