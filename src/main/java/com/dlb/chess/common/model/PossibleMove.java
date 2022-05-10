package com.dlb.chess.common.model;

import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.CastlingMove;
import com.dlb.chess.board.enums.PieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;

// TODO change LegalMove so below constructor is not required
public record PossibleMove(Side havingMove, PieceType movingPieceType, Square fromSquare, Square toSquare,
    CastlingMove castlingMove, PieceType promotionPieceType) implements Comparable<PossibleMove> {

  public PossibleMove(Side havingMove, PieceType movingPieceType, Square fromSquare, Square toSquare) {
    this(havingMove, movingPieceType, fromSquare, toSquare, CastlingMove.NONE, PieceType.NONE);
  }

  public PossibleMove(Side havingMove, PieceType movingPieceType, Square fromSquare, Square toSquare,
      PieceType promotionPieceType) {
    this(havingMove, movingPieceType, fromSquare, toSquare, CastlingMove.NONE, promotionPieceType);
  }

  public PossibleMove(Side havingMove, PieceType movingPieceType, CastlingMove castlingMove) {
    this(havingMove, movingPieceType, Square.NONE, Square.NONE, castlingMove, PieceType.NONE);
  }

  @Override
  public String toString() {
    switch (castlingMove) {
      case KING_SIDE:
      case QUEEN_SIDE:
        return "castling " + castlingMove;
      case NONE:
        if (promotionPieceType == PieceType.NONE) {
          return fromSquare + " " + toSquare;
        }
        return fromSquare + " " + toSquare + " " + promotionPieceType;
      default:
        throw new IllegalArgumentException();
    }
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final var other = (PossibleMove) obj;
    return castlingMove == other.castlingMove && fromSquare == other.fromSquare
        && promotionPieceType == other.promotionPieceType && havingMove == other.havingMove
        && toSquare == other.toSquare;
  }

  @Override
  public int compareTo(PossibleMove move) {
    if (this.equals(move)) {
      return 0;
    }

    if (this.havingMove() != move.havingMove()) {
      return this.havingMove().compareTo(move.havingMove());
    }

    if (this.fromSquare() != move.fromSquare()) {
      return this.fromSquare().compareTo(move.fromSquare());
    }

    if (this.toSquare() != move.toSquare()) {
      return this.toSquare().compareTo(move.toSquare());
    }

    if (this.castlingMove() != move.castlingMove()) {
      return this.castlingMove().compareTo(move.castlingMove());
    }

    if (this.promotionPieceType() != move.promotionPieceType()) {
      return this.promotionPieceType().compareTo(move.promotionPieceType());
    }

    // code cannot come here
    throw new ProgrammingMistakeException("now all fields are equal so objects are equal");
  }

}
