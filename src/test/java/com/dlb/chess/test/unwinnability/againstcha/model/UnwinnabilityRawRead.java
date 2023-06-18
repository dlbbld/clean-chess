package com.dlb.chess.test.unwinnability.againstcha.model;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.test.unwinnability.enums.UnwinnabilityMode;

public record UnwinnabilityRawRead(Fen fen, String lichessGameId, UnwinnabilityMode mode, Side winner, String result,
    String mateLine) {

  public Fen fen() {
    return fen;
  }

  public String lichessGameId() {
    return lichessGameId;
  }

  public UnwinnabilityMode mode() {
    return mode;
  }

  public Side winner() {
    return winner;
  }

  public String result() {
    return result;
  }

  public String mateLine() {
    return mateLine;
  }

}
