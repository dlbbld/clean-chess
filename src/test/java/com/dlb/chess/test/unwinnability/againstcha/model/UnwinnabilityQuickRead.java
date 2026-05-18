package com.dlb.chess.test.unwinnability.againstcha.model;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.unwinnability.UnwinnabilityQuickVerdict;

public record UnwinnabilityQuickRead(Fen fen, String lichessGameId, Side winner,
    UnwinnabilityQuickVerdict unwinnableQuick, String mateLine) {

}
