package com.dlb.chess.test.unwinnability.againstcha.model;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.test.unwinnability.enums.UnwinnabilityMode;

public record UnwinnabilityRawRead(Fen fen, String lichessGameId, UnwinnabilityMode mode, Side winner, String result,
    String mateLine) {

}
