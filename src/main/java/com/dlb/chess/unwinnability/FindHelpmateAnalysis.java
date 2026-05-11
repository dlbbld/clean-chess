package com.dlb.chess.unwinnability;

import java.util.List;

import com.dlb.chess.model.UciMove;

record FindHelpmateAnalysis(FindHelpmateResult findHelpmateResult, int localNodesCount, List<UciMove> mateLine) {

}
