package com.dlb.chess.unwinnability.full.model;

import java.util.List;

import com.dlb.chess.model.UciMove;
import com.dlb.chess.unwinnability.full.enums.UnwinnableFull;

public record UnwinnableFullAnalysis(UnwinnableFull unwinnableFull, List<UciMove> mateLine) {

}
