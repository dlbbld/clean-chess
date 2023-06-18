package com.dlb.chess.test.unwinnability.againstcha.model;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.fen.model.Fen;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;

@SuppressWarnings("null")
public record UnwinnabilityFullRead(@NonNull Fen fen, @NonNull String lichessGameId, @NonNull Side winner,
    @NonNull UnwinnableFull unwinnableFull, @NonNull String mateLine) {

}
