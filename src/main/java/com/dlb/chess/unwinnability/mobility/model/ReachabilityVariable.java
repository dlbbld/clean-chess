package com.dlb.chess.unwinnability.mobility.model;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;

public record ReachabilityVariable(Side sideWhichCanReach, Square toSquare) {

}
