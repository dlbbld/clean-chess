package com.dlb.chess.test.winnable.model;

import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.common.enums.GameStatusAnalysis;
import com.dlb.chess.test.winnable.enums.Winnable;

@SuppressWarnings("null")
public record WinnableAnalysis(@NonNull Winnable winnable, @NonNull Set<GameStatusAnalysis> gameStatusSet) {

}
