package com.dlb.chess.test.unwinnability.againstcha.model;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.unwinnability.quick.enums.UnwinnableQuick;

public record UnwinnabilityQuickRead(Fen fen, String lichessGameId, Side winner, UnwinnableQuick unwinnableQuick,
    String mateLine) {

}
