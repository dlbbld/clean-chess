package com.dlb.chess.common.model;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.dlb.chess.board.enums.CastlingMove;
import com.dlb.chess.board.enums.PromotionPieceType;
import com.dlb.chess.board.enums.Side;
import com.dlb.chess.board.enums.Square;
import com.dlb.chess.common.exceptions.ProgrammingMistakeException;

// Specification: side must be white or black, for the non-castling non-promotion move the from and to square must be
// different board squares, for the non-castling promotion move the from and to square must be different board squares
// and the promotion piece type not the none piece type, for the castling move the castling move must be king-side or
// queen-side.
@SuppressWarnings("null")
public record MoveSpecification(@NonNull Side havingMove, @NonNull Square fromSquare, @NonNull Square toSquare,
    @NonNull CastlingMove castlingMove, @NonNull PromotionPieceType promotionPieceType)
    implements Comparable<MoveSpecification> {

  public MoveSpecification(Side havingMove, Square fromSquare, Square toSquare) {
    this(havingMove, fromSquare, toSquare, CastlingMove.NONE, PromotionPieceType.NONE);

    validate(havingMove, fromSquare, toSquare);
  }

  public MoveSpecification(Side havingMove, Square fromSquare, Square toSquare, PromotionPieceType promotionPieceType) {
    this(havingMove, fromSquare, toSquare, CastlingMove.NONE, promotionPieceType);

    validate(havingMove, fromSquare, toSquare);

    if (promotionPieceType == PromotionPieceType.NONE) {
      throw new IllegalArgumentException("The promotion piece type cannot be the none piece type");
    }
  }

  public MoveSpecification(Side havingMove, CastlingMove castlingMove) {
    this(havingMove, Square.NONE, Square.NONE, castlingMove, PromotionPieceType.NONE);

    if (havingMove == Side.NONE) {
      throw new IllegalArgumentException("The side to move cannot be the none side");
    }
    if (castlingMove == CastlingMove.NONE) {
      throw new IllegalArgumentException("The castling move cannot be the none castling move");
    }
  }

  private static void validate(Side havingMove, Square fromSquare, Square toSquare) {
    if (havingMove == Side.NONE) {
      throw new IllegalArgumentException("The side to move cannot be the none side");
    }
    if (fromSquare == Square.NONE) {
      throw new IllegalArgumentException("The from square cannot be the none square");
    }
    if (toSquare == Square.NONE) {
      throw new IllegalArgumentException("The to square cannot be the none square");
    }
    if (fromSquare == toSquare) {
      throw new IllegalArgumentException("The from and to square must be different");
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
    final var other = (MoveSpecification) obj;
    return castlingMove == other.castlingMove && fromSquare == other.fromSquare
        && promotionPieceType == other.promotionPieceType && havingMove == other.havingMove
        && toSquare == other.toSquare;
  }

  @Override
  public int compareTo(MoveSpecification move) {
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
