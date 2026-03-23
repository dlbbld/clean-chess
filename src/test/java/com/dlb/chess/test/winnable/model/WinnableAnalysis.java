package com.dlb.chess.test.winnable.model;

import java.util.Set;

import com.dlb.chess.common.enums.GameStatusAnalysis;
import com.dlb.chess.test.winnable.enums.Winnable;

public record WinnableAnalysis(Winnable winnable, Set<GameStatusAnalysis> gameStatusSet) {

}
