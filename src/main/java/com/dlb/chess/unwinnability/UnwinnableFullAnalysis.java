package com.dlb.chess.unwinnability;

import java.util.List;

import com.dlb.chess.model.UciMove;
import com.dlb.chess.unwinnability.UnwinnableFull;

record UnwinnableFullAnalysis(UnwinnableFull unwinnableFull, List<UciMove> mateLine) {

}
