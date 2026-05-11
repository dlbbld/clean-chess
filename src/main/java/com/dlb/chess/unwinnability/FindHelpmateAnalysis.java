package com.dlb.chess.unwinnability;

import java.util.List;

import com.dlb.chess.model.UciMove;
import com.dlb.chess.unwinnability.FindHelpmateResult;

record FindHelpmateAnalysis(FindHelpmateResult findHelpmateResult, int localNodesCount, List<UciMove> mateLine) {

}
