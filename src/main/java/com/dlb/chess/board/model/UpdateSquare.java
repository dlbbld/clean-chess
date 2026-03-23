package com.dlb.chess.board.model;

import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.Square;

public record UpdateSquare(Square square, Piece piece) {

  public UpdateSquare(Square square) {
    this(square, Piece.NONE);
  }

}
