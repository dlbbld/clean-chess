package com.dlb.chess.model;

import com.dlb.chess.board.enums.Square;

public record PawnDiagonalBoardMove(Square fromSquare, Square toSquare)
    implements Comparable<PawnDiagonalBoardMove> {

  @Override
  public String toString() {
    return fromSquare + " " + toSquare;
  }

  @Override
  public int compareTo(PawnDiagonalBoardMove otherEmptyBoardMove) {
    if (this.fromSquare().compareTo(otherEmptyBoardMove.fromSquare()) != 0) {
      return this.fromSquare().compareTo(otherEmptyBoardMove.fromSquare());
    }
    if (this.toSquare().compareTo(otherEmptyBoardMove.toSquare()) != 0) {
      return this.toSquare().compareTo(otherEmptyBoardMove.toSquare());
    }
    return 0;
  }

}
