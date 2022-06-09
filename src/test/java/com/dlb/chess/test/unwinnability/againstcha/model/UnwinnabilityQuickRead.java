package com.dlb.chess.test.unwinnability.againstcha.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuick;

public record UnwinnabilityQuickRead(Fen fen, String lichessGameId, Side winner, UnwinnableQuick unwinnableQuick,
    String mateLine) {

  @Override
  public int hashCode() {
    return Objects.hash(fen, lichessGameId, mateLine, unwinnableQuick, winner);
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final var other = (UnwinnabilityQuickRead) obj;
    return Objects.equals(fen, other.fen) && Objects.equals(lichessGameId, other.lichessGameId)
        && Objects.equals(mateLine, other.mateLine) && unwinnableQuick == other.unwinnableQuick
        && winner == other.winner;
  }

}
