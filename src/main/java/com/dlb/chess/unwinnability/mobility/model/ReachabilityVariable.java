package com.dlb.chess.unwinnability.mobility.model;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;

public record ReachabilityVariable(Side sideWhichCanReach, Square reachableSquare) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }
    final var other = (ReachabilityVariable) obj;
    return sideWhichCanReach == other.sideWhichCanReach && reachableSquare == other.reachableSquare;
  }

}
