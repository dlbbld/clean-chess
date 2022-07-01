package com.dlb.chess.test.unwinnability.againstcha.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;

public record UnwinnabilityFullRead(Fen fen, String lichessGameId, Side winner, UnwinnableFull unwinnableFull, String mateLine) {

  @Override
  public int hashCode() {
    return Objects.hash(fen, lichessGameId, mateLine, unwinnableFull, winner);
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
    final var other = (UnwinnabilityFullRead) obj;
    return Objects.equals(fen, other.fen) && Objects.equals(lichessGameId, other.lichessGameId)
        && Objects.equals(mateLine, other.mateLine) && unwinnableFull == other.unwinnableFull && winner == other.winner;
  }

}
