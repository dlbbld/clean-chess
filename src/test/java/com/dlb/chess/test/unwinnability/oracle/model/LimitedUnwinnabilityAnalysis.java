package com.dlb.chess.test.unwinnability.oracle.model;

import java.util.Set;

import com.dlb.chess.test.unwinnability.oracle.enums.GameStatusAnalysis;
import com.dlb.chess.test.unwinnability.oracle.enums.LimitedUnwinnabilityVerdict;

public record LimitedUnwinnabilityAnalysis(LimitedUnwinnabilityVerdict verdict, Set<GameStatusAnalysis> gameStatusSet) {

}
