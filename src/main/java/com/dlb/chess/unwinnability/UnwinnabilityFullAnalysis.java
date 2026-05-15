package com.dlb.chess.unwinnability;

import java.util.List;

import com.dlb.chess.model.UciMove;

public record UnwinnabilityFullAnalysis(UnwinnabilityFullVerdict verdict, List<UciMove> mateLine) {

}
