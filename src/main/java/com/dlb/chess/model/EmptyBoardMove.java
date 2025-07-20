package com.dlb.chess.model;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.board.enums.Square;

public record EmptyBoardMove(@NonNull Square fromSquare, @NonNull Square toSquare)
    implements Comparable<EmptyBoardMove> {

  @Override
  public String toString() {
    return fromSquare + " " + toSquare;
  }

  @Override
  public int compareTo(EmptyBoardMove otherEmptyBoardMove) {
    if (this.fromSquare().compareTo(otherEmptyBoardMove.fromSquare()) != 0) {
      return this.fromSquare().compareTo(otherEmptyBoardMove.fromSquare());
    }
    if (this.toSquare().compareTo(otherEmptyBoardMove.toSquare()) != 0) {
      return this.toSquare().compareTo(otherEmptyBoardMove.toSquare());
    }
    return 0;
  }

}
