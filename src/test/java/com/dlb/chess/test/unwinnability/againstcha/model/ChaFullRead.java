package com.dlb.chess.test.unwinnability.againstcha.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;

public record ChaFullRead(Fen fen, Side sideCheckingForWin, UnwinnableFull unwinnableFull,
    String mateLine) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (ChaFullRead) obj;
    return Objects.equals(mateLine, other.mateLine) && Objects.equals(fen, other.fen)
        && sideCheckingForWin == other.sideCheckingForWin && unwinnableFull == other.unwinnableFull;
  }

  public Fen fen() {
    return fen;
  }

  public Side sideCheckingForWin() {
    return sideCheckingForWin;
  }

  public UnwinnableFull unwinnableFull() {
    return unwinnableFull;
  }

  public String mateLine() {
    return mateLine;
  }

}
