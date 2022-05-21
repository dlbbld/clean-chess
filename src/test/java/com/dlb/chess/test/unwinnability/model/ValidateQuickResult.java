package com.dlb.chess.test.unwinnability.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuick;

public record ValidateQuickResult(Fen fen, Side sideCheckingForWin, UnwinnableQuick unwinnableQuick) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }
    final var other = (ValidateQuickResult) obj;
    return Objects.equals(fen, other.fen) && sideCheckingForWin == other.sideCheckingForWin
        && unwinnableQuick == other.unwinnableQuick;
  }
}
