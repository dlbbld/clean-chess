package com.dlb.chess.board.model;

import org.eclipse.jdt.annotation.NonNull;

import com.dlb.chess.board.enums.Piece;
import com.dlb.chess.board.enums.Square;

@SuppressWarnings("null")
public record UpdateSquare(@NonNull Square square, @NonNull Piece piece) {

  public UpdateSquare(Square square) {
    this(square, Piece.NONE);
  }

}
