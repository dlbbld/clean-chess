package com.dlb.chess.unwinnability.mobility.model;

import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.Square;
import com.dlb.chess.unwinnability.model.PiecePlacement;

public record MobilitySolutionVariable(PiecePlacement piecePlacement, Square toSquare) {

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }
    final var other = (MobilitySolutionVariable) obj;
    return Objects.equals(piecePlacement, other.piecePlacement) && toSquare == other.toSquare;
  }

}
