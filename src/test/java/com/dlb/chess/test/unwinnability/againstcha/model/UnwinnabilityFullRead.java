package com.dlb.chess.test.unwinnability.againstcha.model;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;

public record UnwinnabilityFullRead(Fen fen, String lichessGameId, Side winner, UnwinnableFull unwinnableFull,
    String mateLine) {

}
