package com.dlb.chess.unwinnability.findhelpmate.exhaust.model;

import java.util.List;

import com.dlb.chess.model.UciMove;
import com.dlb.chess.unwinnability.findhelpmate.enums.FindHelpmateResult;

public record FindHelpmateAnalysis(FindHelpmateResult findHelpmateResult, int localNodesCount,
    List<UciMove> mateLine) {

}
