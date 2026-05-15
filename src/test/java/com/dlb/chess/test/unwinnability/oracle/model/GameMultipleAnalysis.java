package com.dlb.chess.test.unwinnability.oracle.model;

import java.util.Set;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.test.unwinnability.oracle.enums.GameStatusAnalysis;

public record GameMultipleAnalysis(Set<GameStatusAnalysis> gameStatusSet, int numberOfHalfMoves, Side havingMove) {

}
