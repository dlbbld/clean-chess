package com.dlb.chess.unwinnability.mobility.model;

import com.dlb.chess.board.enums.Square;
import com.dlb.chess.unwinnability.model.PiecePlacement;

public record MobilitySolutionVariable(PiecePlacement piecePlacement, Square toSquare) {

}
