package com.dlb.chess.test.unwinnability.againstcha.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.test.unwinnability.enums.UnwinnabilityMode;

public record UnwinnabilityRawRead(Fen fen, String lichessGameId, UnwinnabilityMode mode, Side winner, String result,
    String mateLine) {

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
    final var other = (UnwinnabilityRawRead) obj;
    return Objects.equals(fen, other.fen) && Objects.equals(lichessGameId, other.lichessGameId)
        && Objects.equals(mateLine, other.mateLine) && mode == other.mode && Objects.equals(result, other.result)
        && winner == other.winner;
  }

}
