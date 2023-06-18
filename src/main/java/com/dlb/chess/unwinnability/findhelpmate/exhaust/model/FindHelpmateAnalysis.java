package com.dlb.chess.unwinnability.findhelpmate.exhaust.model;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.model.UciMove;
import com.dlb.chess.unwinnability.findhelpmate.enums.FindHelpmateResult;

@SuppressWarnings("null")
public record FindHelpmateAnalysis(FindHelpmateResult findHelpmateResult, int localNodesCount,
    @NonNull List<UciMove> mateLine) {

}
